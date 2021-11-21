package karabin.mandelbrot.drawing.mulithreaded.burningship;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.DrawingMethod;
import karabin.mandelbrot.drawing.mulithreaded.renderingsection.RenderingSection;
import karabin.mandelbrot.utils.FractalUtils;

public class BurningShipDefault extends DrawingMethod{

	public BurningShipDefault(int iterations, int max, Color color) {
		super(iterations, max, color);
	}
	
	protected double normalizedEscape(Complex c) {
		double real = 0;
		double image = 0;

		for (int i = 0; i < iterations; i++) {
			double s1 = real * real;
			double s2 = image * image;
			if (s1 + s2 > max) {
				return i + 1 - Math.log10(Math.log(s1 + s2) / Math.log(2));
			}
			// z2 = (|Re(z)| + i|Im(z)|)^2 + c
			// z2 =  (|real| + i|image|)^2 + c
			// z2 = |real||real| + 2*i*|real||image| - |image||image| + c
			// z2 = real^2 + 2*i*|real||image| - image^2 + c
			double real2 = s1 - s2 + c.getReal();
			double image2 = 2 * Math.abs(real) * Math.abs(image) + c.getImaginary();
			real = real2;
			image = image2;
		}

		return -1;
	}

	@Override
	public void draw(BufferedImage image, Rectangle2D domain) {
		final int processors = Runtime.getRuntime().availableProcessors();
		final List<Thread> threads = new ArrayList<>();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final int [][] rates = new int[width][height];
		
		for (int i = 0; i < processors; i++) {
			Thread thread = new NormalizedEscapeSingleColorRenderingSection(width, height, i * height / processors,
					(i + 1) * height / processors, domain, rates);
			threads.add(thread);
			thread.start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				image.setRGB(x, y, rates[x][y]);
			}
		}
	}

	class NormalizedEscapeSingleColorRenderingSection extends RenderingSection {

		public NormalizedEscapeSingleColorRenderingSection(int width, int height, int startHeight, int endHeight,
				Rectangle2D domain, int[][] rates) {
			super(width, height, startHeight, endHeight, domain, rates);
		}

		@Override
		public void run() {
			for (int y = startHeight; y < endHeight; y++) {
				if(this.interrupted) {
					return;
				}
				for (int x = 0; x < width; x++) {
					Complex c = FractalUtils.pixelToComplex(x, y, width, height, domain);
					double rate = BurningShipDefault.this.normalizedEscape(c);
					int rgb = BurningShipDefault.this.color(rate);
					rates[x][y] = rgb;
				}
			}
		}
	}

}