package karabin.mandelbrot.drawing.gpu;

import java.awt.Color;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.gpu.kernel.BurningShipKernel;

public class BurningShipHistogramGpu extends HistogramGpu {

	public BurningShipHistogramGpu(int iterations, int max, Color color) {
		super(iterations, max, color, new BurningShipKernel());
	}

	@Override
	protected double escape(Complex c) {
		return -1;
	}

}
