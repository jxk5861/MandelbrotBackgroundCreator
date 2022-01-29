package karabin.mandelbrot.drawing.gpu.mandelbrot;

import java.awt.Color;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.gpu.HistogramGpu;
import karabin.mandelbrot.drawing.gpu.kernel.MandelbrotKernel;

public class MandelbrotHistogramGpu extends HistogramGpu {

	public MandelbrotHistogramGpu(int iterations, int max, Color color) {
		super(iterations, max, color, new MandelbrotKernel());
	}

	@Override
	protected double escape(Complex c) {
		return -1;
	}

}
