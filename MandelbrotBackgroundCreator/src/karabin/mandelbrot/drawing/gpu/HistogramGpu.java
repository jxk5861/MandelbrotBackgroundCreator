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
		int [][] rates = this.computeRates(width, height, unsafeDomain);

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

}
