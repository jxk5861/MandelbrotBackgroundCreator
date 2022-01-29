package karabin.mandelbrot.drawing.gpu.mandelbrot;

import java.awt.Color;

import karabin.mandelbrot.drawing.gpu.HistogramGpu;
import karabin.mandelbrot.drawing.gpu.kernel.MandelbrotKernel;

public class MandelbrotHistogramGpu extends HistogramGpu {

	public MandelbrotHistogramGpu(int iterations, int max, Color color) {
		super(iterations, max, color, new MandelbrotKernel());
	}

}
