package karabin.mandelbrot.drawing.gpu;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import karabin.mandelbrot.drawing.gpu.kernel.FractalKernel;

/**
 * The abstract Histogram implementation which can take a list of rates from the
 * computeRates method and draw them on an image with the histogram algorithm.
 * 
 * Histogram ensures that differences are always visible in the image and do not
 * disappear as the number of iterations increases. For example, if iterations
 * is set to 10 million, then a point with 300 thousand iterations would appear
 * the same as a point with 1 iteration when using
 * color=iterations/maxIterations as a coloring algorithm. Histogram fixes this
 * by grouping common numbers of iterations in buckets and coloring each bucket
 * differently.
 */
public abstract class HistogramGpu extends DrawingMethodGpu {

	/**
	 * Create a new HistogramGpu implementation. Most implementations will only need
	 * to change the type of FractalKernel used so that it produces different escape
	 * rates. For example, Mandelbrot, Burning Ship, and Julia sets can all be drawn
	 * by slightly changing the escape rate function.
	 */
	public HistogramGpu(int iterations, int max, Color color, FractalKernel kernel) {
		super(iterations, max, color, kernel);
	}

	/**
	 * Compute the rates from the implemented GPU kernel and render the image based
	 * on the rates. This allows different implementations of Histogram to reuse
	 * this method and only provide an implementation of the GPU kernel for the
	 * specific algorithm.
	 */
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

				// If the iteration count is fractional then interpolate between
				// floor(iteration) and ceil(iteration) to produce smooth gradients. Normal
				// implementations will produce only integer values but normalized escape
				// allows for fractional values to produce better images.
				if (iteration != 0 && (int) iteration + 1 < this.iterations + 1) {
					hue += (iteration % 1) * numIterationsPerPixel[(int) iteration + 1] / total;
				}

				image.setRGB(x, y, this.color(hue));
			}
		}
	}
}
