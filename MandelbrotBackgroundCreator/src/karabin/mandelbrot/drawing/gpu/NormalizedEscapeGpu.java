package karabin.mandelbrot.drawing.gpu;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import karabin.mandelbrot.drawing.gpu.kernel.FractalKernel;

public abstract class NormalizedEscapeGpu extends DrawingMethodGpu {

	public NormalizedEscapeGpu(int iterations, int max, Color color, FractalKernel kernel) {
		super(iterations, max, color, kernel);
	}

	@Override
	public void draw(BufferedImage image, Rectangle2D unsafeDomain) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		double [][] rates = this.computeRates(width, height, unsafeDomain);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double iteration = rates[x][y];
				double hue = (double) iteration / this.iterations;
				image.setRGB(x, y, this.color(hue));
			}
		}
	}

}
