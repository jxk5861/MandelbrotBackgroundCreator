package karabin.mandelbrot.drawing.gpu;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import karabin.mandelbrot.drawing.gpu.kernel.FractalKernel;

public abstract class HistogramGpu extends DrawingMethodGpu {

	public HistogramGpu(int iterations, int max, Color color, FractalKernel kernel) {
		super(iterations, max, color, kernel);
	}

	@Override
	public void draw(BufferedImage image, Rectangle2D unsafeDomain) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		double[][] rates = this.computeRates(width, height, unsafeDomain);

		int[] numIterationsPerPixel = new int[iterations + 1];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (Double.isNaN(rates[x][y])) {
					continue;
				}
				if (rates[x][y] < 0) {
					continue;
				}

				double iteration = Math.min(rates[x][y], this.iterations);
				numIterationsPerPixel[(int) iteration]++;
			}
		}

		int total = 0;
		for (int i = 0; i < numIterationsPerPixel.length; i++) {
			total += numIterationsPerPixel[i];
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (Double.isNaN(rates[x][y])) {
					// TODO: Allow user to change.
					image.setRGB(x, y, Color.BLACK.getRGB());
					continue;
				}
				if (rates[x][y] < 0) {
					image.setRGB(x, y, this.color(0));
					continue;
				}

				double iteration = Math.min(rates[x][y], this.iterations);
				double hue = 0;
				for (int i = 0; i <= iteration; i++) {
					hue += (double) numIterationsPerPixel[i] / total;
				}
				// For Mandelbrot Normalized Escape iterations will smooth it out but for
				// Burning Ship it will change nothing.
				if (iteration != 0 && (int) iteration + 1 < this.iterations + 1) {
					hue += (iteration % 1) * numIterationsPerPixel[(int) iteration + 1] / total;
				}

				image.setRGB(x, y, this.color(hue));
			}
		}
	}
}
