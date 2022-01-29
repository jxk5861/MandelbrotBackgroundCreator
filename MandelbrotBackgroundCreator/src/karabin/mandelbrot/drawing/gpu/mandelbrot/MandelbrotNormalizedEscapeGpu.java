package karabin.mandelbrot.drawing.gpu.mandelbrot;

import java.awt.Color;

import karabin.mandelbrot.drawing.gpu.NormalizedEscapeGpu;
import karabin.mandelbrot.drawing.gpu.kernel.MandelbrotKernel;

public class MandelbrotNormalizedEscapeGpu extends NormalizedEscapeGpu{

	public MandelbrotNormalizedEscapeGpu(int iterations, int max, Color color) {
		super(iterations, max, color, new MandelbrotKernel());
	}

}
