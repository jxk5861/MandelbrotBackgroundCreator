package karabin.mandelbrot.drawing.mulithreaded.burningship;

import java.awt.Color;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.mulithreaded.HistogramSingleColor;

public class BurningShipHistogramSingleColor extends HistogramSingleColor{

	public BurningShipHistogramSingleColor(int iterations, int max, Color color) {
		super(iterations, max, color);
	}
	
	@Override
	protected double escape(Complex c, int x, int y) {
		double real = 0;
		double image = 0;

		for (int i = 0; i < iterations; i++) {
			double s1 = real * real;
			double s2 = image * image;
			if (s1 + s2 > max) {
				return i + 1 - Math.log10(Math.log(s1 + s2) / Math.log(2));
			}
			double real2 = s1 - s2 + c.getReal();
			double image2 = 2 * Math.abs(real * image) + c.getImaginary();
			real = real2;
			image = image2;
		}

		return -1;
	}

}
