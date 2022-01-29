package karabin.mandelbrot.drawing.gpu.julia;

import java.awt.Color;

import karabin.mandelbrot.drawing.gpu.HistogramGpu;
import karabin.mandelbrot.drawing.gpu.kernel.JuliaKernel;

public class JuliaHistogramGpu extends HistogramGpu{

	public JuliaHistogramGpu(int iterations, int max, Color color) {
		super(iterations, max, color, new JuliaKernel());
	}
}
