package karabin.mandelbrot.drawing;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.utils.MandelbrotUtils;

public abstract class DrawingMethod implements DrawingMethodIF {

	protected int iterations;
	protected int max;
	protected Color color;

	public DrawingMethod(int iterations, int max, Color color) {
		super();
		this.iterations = iterations;
		this.max = max;
		this.color = color;
	}

	protected int color(double rate) {
		if (rate == -1) {
			return 0;
		}

		double factor = rate / this.iterations;
		
		return new Color((int)(factor * color.getRed()), (int) (factor * color.getGreen()), (int)(factor * color.getBlue())).getRGB();
	}

	protected double normalizedEscape(Complex c) {
		double real = 0;
		double image = 0;

		for (int i = 0; i < iterations; i++) {
			double s1 = real * real;
			double s2 = image * image;
			if (s1 + s2 > max) {
				return i + 1 - Math.log10(Math.log(s1 + s2) / Math.log(2));
			}
			double real2 = s1 - s2 + c.getReal();
			double image2 = 2 * real * image + c.getImaginary();
			real = real2;
			image = image2;
		}

		return -1;
	}

	@Override
	public void draw(BufferedImage image, Rectangle2D domain) {
		int height = image.getHeight();
		int width = image.getWidth();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Complex c = MandelbrotUtils.pixelToComplex(x, y, width, height, domain);
				double rate = this.normalizedEscape(c);
				int rgb = this.color(rate);
				image.setRGB(x, y, rgb);
			}
		}
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
