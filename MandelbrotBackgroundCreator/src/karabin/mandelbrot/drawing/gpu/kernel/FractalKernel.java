package karabin.mandelbrot.drawing.gpu.kernel;

import java.awt.geom.Rectangle2D;

import com.aparapi.Kernel;

/**
 * The abstract GPU kernel for a fractal. The run method runs an algorithm on
 * the GPU with Aparapi and must follow special rules such as not calling most
 * methods and not using Objects.
 */
public abstract class FractalKernel extends Kernel {
	protected double[] rates;
	protected double dw;
	protected double dh;
	protected double dx;
	protected double dy;
	protected int iterations;
	protected int max;

	/**
	 * Setup the data the kernel needs for the computation. It uses the fractal's
	 * domain, max iterations, and max radius to compute the rates for each pixel.
	 * 
	 * @param domain     The domain of the region to render.
	 * @param iterations The maximum iterations to run on each pixel.
	 * @param max        The max radius from the origin before a pixel escapes the
	 *                   set.
	 * @param rates      The place to store the result of the calculations.
	 */
	public void set(Rectangle2D domain, int iterations, int max, double[] rates) {
		this.dw = domain.getWidth();
		this.dh = domain.getHeight();
		this.dx = domain.getX();
		this.dy = domain.getY();
		this.iterations = iterations;
		this.max = max;
		this.rates = rates;
	}

	/**
	 * Since each point's escape rate does not impact the others all pixels can be
	 * run on separate GPU cores and escaped at the same time.
	 */
	@Override
	public abstract void run();
}
