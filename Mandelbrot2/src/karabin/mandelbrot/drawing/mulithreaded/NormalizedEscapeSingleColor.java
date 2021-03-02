package karabin.mandelbrot.drawing.mulithreaded;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.DrawingMethod;
import karabin.mandelbrot.utils.MandelbrotUtils;

public class NormalizedEscapeSingleColor extends DrawingMethod {
	public NormalizedEscapeSingleColor(int iterations, int max, Color color) {
		super(iterations, max, color);
	}

	@Override
	public void draw(BufferedImage image, Rectangle2D domain) {
		final int processors = Runtime.getRuntime().availableProcessors();
		final List<Thread> threads = new ArrayList<>();
		final int width = image.getWidth();
		final int height = image.getHeight();

		for (int i = 0; i < processors; i++) {
			RenderingSection section = new RenderingSection(width, height, i * height / processors,
					(i + 1) * height / processors, domain, image);
			Thread thread = new Thread(section);
			threads.add(thread);
			thread.start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class RenderingSection implements Runnable {
		private int width;
		private int height;
		private int startHeight;
		private int endHeight;
		private Rectangle2D domain;
		private BufferedImage image;

		public RenderingSection(int width, int height, int startHeight, int endHeight, Rectangle2D domain,
				BufferedImage image) {
			super();
			this.width = width;
			this.height = height;
			this.startHeight = startHeight;
			this.endHeight = endHeight;
			this.domain = domain;
			this.image = image;
		}

		@Override
		public void run() {

			for (int y = startHeight; y < endHeight; y++) {
				for (int x = 0; x < width; x++) {
					Complex c = MandelbrotUtils.pixelToComplex(x, y, width, height, domain);
					double rate = NormalizedEscapeSingleColor.this.normalizedEscape(c);
					int rgb = NormalizedEscapeSingleColor.this.color(rate);
					image.setRGB(x, y, rgb);
				}
			}
		}

	}
}
