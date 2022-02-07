package karabin.mandelbrot.drawing.mulithreaded.burningship;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.DrawingMethod;
import karabin.mandelbrot.utils.FractalUtils;

public class GhostShip extends DrawingMethod {

	public GhostShip(int iterations, int max, Color color) {
		super(iterations, max, color);
	}

	private List<Complex> buddahbrotPoints(final Complex c, final int iterations) {
		List<Complex> points = new ArrayList<>(iterations);
		double cr = c.getReal();
		double ci = c.getImaginary();
		double real = 0;
		double image = 0;
		double temp = 0;

		int i;
		for (i = 0; i < iterations && real * real + image * image < this.max; i++) {
			temp = real * real - image * image + cr;
			image = 2 * Math.abs(real * image) + ci;
			real = temp;

			// need to avoid this.
			points.add(Complex.valueOf(real, image));
		}

		if (i != iterations) {
			return points;
		}
		return new ArrayList<>();
	}

	private int[][] rates = null;

	@Override
	public void draw(BufferedImage image, Rectangle2D unsafeDomain) {
		int width = image.getWidth();
		int height = image.getHeight();
		Rectangle2D domain = (Rectangle2D) unsafeDomain.clone();
	}

}
