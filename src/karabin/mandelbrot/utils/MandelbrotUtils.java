package karabin.mandelbrot.utils;

import java.awt.geom.Rectangle2D;

import org.apache.commons.math3.complex.Complex;

public class MandelbrotUtils {
	public static Complex pixelToComplex(double x, double y, int width, int height, Rectangle2D domain) {
		Complex c = new Complex(domain.getWidth() * x / width, domain.getHeight() * y / height);
		return c.add(Complex.valueOf(domain.getX(), domain.getY()));
	}
}
