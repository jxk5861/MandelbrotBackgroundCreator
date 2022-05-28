package karabin.mandelbrot.drawing.gpu.kernel;

public class MandelbrotKernel extends FractalKernel {
	@Override
	public void run() {
		final int width = this.getGlobalSize(0);
		final int height = this.getGlobalSize(1);
		final int xg = this.getGlobalId(0);
		final int yg = this.getGlobalId(1);

		// This does what MandelbrotUtils.pixelToComplex does, since we can't use Java
		// objects.
		final double cr = this.dw * xg / width + this.dx;
		final double ci = this.dh * yg / height + this.dy;

		double real = 0;
		double image = 0;
		double temp = 0;

		// We actually do 5 less iterations. This prevents the normalized escape method
		// from creating negative values which will all be the same colors. For example,
		// the outside of the Mandelbrot set may normally have iterations 0, 1, 2, 3, 4
		// but normalized escape could make it -3, -2.5, -2, -1.5, -1 which will all be
		// in the histogram cell of 0. Subtracting from iterations and adding to the
		// normalized escape value of the iterations helps prevent this without changing
		// the max value.

		// Alternatively we could find the min value in rates and adjust them before
		// drawing the image, but this method is simpler and doesn't violate the
		// assumption that 0 <= rates[x][y] <= maxIterations.
		int i;
		for (i = 0; i < iterations - 5 && real * real + image * image < max; i++) {
			temp = real * real - image * image + cr;
			image = 2 * real * image + ci;
			real = temp;
		}

		double id = Double.NaN;
		if (i != iterations - 5) {
			// Additional iterations to minimize error in normalized escape calculation.
			for (int j = 0; j < 5; j++) {
				temp = real * real - image * image + cr;
				image = 2 * real * image + ci;
				real = temp;
			}

			double logzn = Math.log(real * real + image * image) / 2;
			id = i + 1 + 5 - Math.log(logzn / Math.log(2)) / Math.log(2);
		}

		rates[xg + yg * width] = id;
	}
}
