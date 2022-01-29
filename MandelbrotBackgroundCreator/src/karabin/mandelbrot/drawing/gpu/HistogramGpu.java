package karabin.mandelbrot.drawing.gpu;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.apache.commons.math3.complex.Complex;

import com.aparapi.Range;

import karabin.mandelbrot.drawing.DrawingMethod;
import karabin.mandelbrot.drawing.gpu.kernel.FractalKernel;

public abstract class HistogramGpu extends DrawingMethod {

	private FractalKernel kernel;

	public HistogramGpu(int iterations, int max, Color color, FractalKernel kernel) {
		super(iterations, max, color);
		this.kernel = kernel;
	}

	protected abstract double escape(Complex c);

	@Override
	public void draw(BufferedImage image, Rectangle2D unsafeDomain) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		final Rectangle2D domain = (Rectangle2D) unsafeDomain.clone();
		final int[][] rates = new int[width][height];

		final int[] rates1D = new int[width * height];

		kernel.set(domain, this.iterations, this.max, rates1D);
		Range range = Range.create2D(width, height);
		kernel.execute(range);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				rates[x][y] = rates1D[x + width * y];
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

}
