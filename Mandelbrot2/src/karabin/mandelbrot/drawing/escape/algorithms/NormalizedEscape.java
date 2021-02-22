package karabin.mandelbrot.drawing.escape.algorithms;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.escape.EscapeRate;

public class NormalizedEscape extends EscapeRate {

	public NormalizedEscape(int iterations, int max) {
		super(iterations, max);
	}

	@Override
	public double rate(Complex c) {
		return this.normalizedEscape(c);
	}

	private double normalizedEscape(Complex c) {
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

}
