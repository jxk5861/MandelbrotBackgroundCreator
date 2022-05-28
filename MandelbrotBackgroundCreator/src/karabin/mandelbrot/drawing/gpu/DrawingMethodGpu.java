package karabin.mandelbrot.drawing.gpu;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import com.aparapi.Range;

import karabin.mandelbrot.drawing.DrawingMethod;
import karabin.mandelbrot.drawing.gpu.kernel.FractalKernel;

public abstract class DrawingMethodGpu extends DrawingMethod{

	private FractalKernel kernel;
	public DrawingMethodGpu(int iterations, int max, Color color, FractalKernel kernel) {
		super(iterations, max, color);
		this.kernel = kernel;
	}

	protected double[][] computeRates(final int width, final int height, final Rectangle2D unsafeDomain){
		
		final Rectangle2D domain = (Rectangle2D) unsafeDomain.clone();
		final double[][] rates = new double[width][height];

		final double[] rates1D = new double[width * height];

		kernel.set(domain, this.iterations, this.max, rates1D);
		Range range = Range.create2D(width, height);
		kernel.execute(range);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				rates[x][y] = rates1D[x + width * y];
			}
		}
		
		return rates;
	}

}
