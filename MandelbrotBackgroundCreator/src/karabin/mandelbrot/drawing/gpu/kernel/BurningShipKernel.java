package karabin.mandelbrot.drawing.gpu.kernel;

public class BurningShipKernel extends FractalKernel {

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

		int i;
		for (i = 0; i < iterations && real * real + image * image < max; i++) {
			temp = real * real - image * image + cr;
			image = 2 * Math.abs(real * image) + ci;
			real = temp;
		}
		
		// id must be defined immediately for Aparapi to run on GPU without kernel compilation errors.
		double id = Double.NaN;
		if (i != iterations) {
			// Additional iterations to minimize error in normalized escape calculation.
			for (int j = 0; j < 5; j++) {
				temp = real * real - image * image + cr;
				image = 2 * real * image + ci;
				real = temp;
			}

			double logzn = Math.log(real * real + image * image) / 2;
			id = i + 1 - Math.log(logzn / Math.log(2)) / Math.log(2);
		}

		rates[xg + yg * width] = id;
	}
}
