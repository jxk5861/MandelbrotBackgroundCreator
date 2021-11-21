package karabin.mandelbrot.drawing.mulithreaded;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.DrawingMethod;
import karabin.mandelbrot.drawing.mulithreaded.renderingsection.RenderingSection;
import karabin.mandelbrot.utils.FractalUtils;

public class HistogramSingleColor extends DrawingMethod {

	public HistogramSingleColor(int iterations, int max, Color color) {
		super(iterations, max, color);
	}

	@Override
	protected int color(double rate) {
		return new Color((int) (rate * color.getRed()), (int) (rate * color.getGreen()), (int) (rate * color.getBlue()))
				.getRGB();
	}

	private void escape(Complex c, int x, int y, int[][] rates) {
		double real = 0;
		double image = 0;

		for (int i = 0; i < iterations; i++) {
			double s1 = real * real;
			double s2 = image * image;
			if (s1 + s2 > max) {
				rates[x][y] = i;
//				normalizedRates[x][y] = i + 1 - Math.log10(Math.log(s1 + s2) / Math.log(2));
				return;
			}
			double real2 = s1 - s2 + c.getReal();
			double image2 = 2 * real * image + c.getImaginary();
			real = real2;
			image = image2;
		}

		rates[x][y] = -1;
	}

	@Override
	public void draw(BufferedImage image, Rectangle2D domain) {
		final int processors = Runtime.getRuntime().availableProcessors();
		final List<Thread> threads = new ArrayList<>();
		final int width = image.getWidth();
		final int height = image.getHeight();

		int[][] rates = new int[width][height];

		// Make this into a new interruptable thread?
		for (int i = 0; i < processors; i++) {
			Thread thread = new HistogramSingleColorSection(width, height, i * height / processors,
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

		int[] numIterationsPerPixel = new int[iterations + 1];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (rates[x][y] == -1)
					continue;
				int iteration = rates[x][y];
				numIterationsPerPixel[iteration]++;
			}
		}

		int total = 0;
		for (int i = 0; i < numIterationsPerPixel.length; i++) {
			total += numIterationsPerPixel[i];
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int iteration = rates[x][y];
				double hue = 0;
				for (int i = 0; i <= iteration; i++) {
					hue += (double) numIterationsPerPixel[i] / total;
				}

				image.setRGB(x, y, this.color(hue));
			}
		}
	}

	class HistogramSingleColorSection extends RenderingSection {

		public HistogramSingleColorSection(int width, int height, int startHeight, int endHeight, Rectangle2D domain,
				int[][] rates) {
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
					HistogramSingleColor.this.escape(c, x, y, rates);
				}
			}
		}
	}

}
