package karabin.mandelbrot.drawing.gpu.kernel;

import java.awt.geom.Rectangle2D;

import com.aparapi.Kernel;

public abstract class FractalKernel extends Kernel {
	protected double[] rates;
	protected double dw;
	protected double dh;
	protected double dx;
	protected double dy;
	protected int iterations;
	protected int max;

	public void set(Rectangle2D domain, int iterations, int max, double[] rates) {
		this.dw = domain.getWidth();
		this.dh = domain.getHeight();
		this.dx = domain.getX();
		this.dy = domain.getY();
		this.iterations = iterations;
		this.max = max;
		this.rates = rates;
	}
}
