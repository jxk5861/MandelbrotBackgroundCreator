package karabin.mandelbrot.drawing.gpu.burningship;

import java.awt.Color;

import karabin.mandelbrot.drawing.gpu.HistogramGpu;
import karabin.mandelbrot.drawing.gpu.kernel.BurningShipKernel;

public class BurningShipHistogramGpu extends HistogramGpu {

	public BurningShipHistogramGpu(int iterations, int max, Color color) {
		super(iterations, max, color, new BurningShipKernel());
	}
	
}
