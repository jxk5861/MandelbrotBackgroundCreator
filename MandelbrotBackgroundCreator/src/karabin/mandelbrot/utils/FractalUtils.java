package karabin.mandelbrot.utils;

import java.awt.geom.Rectangle2D;

import org.apache.commons.math3.complex.Complex;

public class FractalUtils {
	public static Complex pixelToComplex(double x, double y, int width, int height, Rectangle2D domain) {
		// cr = x * domain.getWidth() / width + domain.getX()
		// (cr - domain.getX()) * width / domain.getWidth()
		Complex c = new Complex(domain.getWidth() * x / width, domain.getHeight() * y / height);
		return c.add(Complex.valueOf(domain.getX(), domain.getY()));
	}

	public static Complex complexToPixel(Complex c, int width, int height, Rectangle2D domain) {
		return new Complex((c.getReal() - domain.getX()) * width / domain.getWidth(),
				(c.getImaginary() - domain.getY()) * height / domain.getHeight());
	}
}
