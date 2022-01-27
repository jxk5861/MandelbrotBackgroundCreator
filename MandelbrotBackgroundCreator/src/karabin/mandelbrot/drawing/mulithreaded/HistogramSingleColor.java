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

public abstract class HistogramSingleColor extends DrawingMethod {

	public HistogramSingleColor(int iterations, int max, Color color) {
		super(iterations, max, color);
	}

	protected abstract double escape(Complex c);

	@Override
	public void draw(BufferedImage image, Rectangle2D domain) {
		final int processors = Runtime.getRuntime().availableProcessors();
		final List<Thread> threads = new ArrayList<>(processors);
		final int width = image.getWidth();
		final int height = image.getHeight();
		domain = (Rectangle2D) domain.clone();

		final int[][] rates = new int[width][height];

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
				if (this.interrupted) {
					return;
				}
				for (int x = 0; x < width; x++) {
					Complex c = FractalUtils.pixelToComplex(x, y, width, height, domain);
					rates[x][y] = (int) HistogramSingleColor.this.escape(c);
				}
			}
		}
	}

}
