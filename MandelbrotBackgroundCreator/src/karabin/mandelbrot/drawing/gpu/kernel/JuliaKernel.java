package karabin.mandelbrot.drawing.gpu.kernel;

public class JuliaKernel extends FractalKernel {
	private double alpha = Math.PI - .15;
	@Override
	public void run() {
		final int width = this.getGlobalSize(0);
		final int height = this.getGlobalSize(1);
		final int xg = this.getGlobalId(0);
		final int yg = this.getGlobalId(1);

		// This does what MandelbrotUtils.pixelToComplex does, since we can't use Java
		// objects.
		// let c = 0.7885e^ia and let a->0 to 2pi
		final double cr = 0.7885 * Math.cos(alpha);
		final double ci = 0.7885 * Math.sin(alpha);

		double real = this.dw * xg / width + this.dx;
		double image = this.dh * yg / height + this.dy;
		double temp = 0;

		int i;
		for (i = 0; i < iterations && real * real + image * image < max; i++) {
			temp = real * real - image * image + cr;
			image = 2 * real * image + ci;
			real = temp;
		}

		if (i == iterations) {
			i = -1;
		}

		rates[xg + yg * width] = i;
	}
}
