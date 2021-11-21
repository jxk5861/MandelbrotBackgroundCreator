package karabin.mandelbrot.drawing.mulithreaded.mandelbrot;

import java.awt.Color;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.mulithreaded.HistogramSingleColor;

public class MandelbrotHistogramSingleColor extends HistogramSingleColor {

	public MandelbrotHistogramSingleColor(int iterations, int max, Color color) {
		super(iterations, max, color);
	}

	@Override
	protected double escape(Complex c) {
		double real = 0;//c.getReal();
		double image = 0;//c.getImaginary();
		
//		Complex c2 = new Complex(-0.8, 0.156);
//		Complex c2 = new Complex(-0.4, 0.6);
//		Complex c2 = new Complex(0, -0.8);
//		Complex c2 = new Complex(-0.7269, 0.1889);
//		double alpha = 2*Math.PI * 0.7;
//		Complex c2 = new Complex(0.7885).multiply(Complex.valueOf(Math.E).pow(Complex.valueOf(0, alpha)));

		for (int i = 0; i < iterations; i++) {
			double s1 = real * real;
			double s2 = image * image;
			if (s1 + s2 > max) {
//				normalizedRates[x][y] = i + 1 - Math.log10(Math.log(s1 + s2) / Math.log(2));
				return i + 1 - Math.log10(Math.log(s1 + s2) / Math.log(2));
			}
			double real2 = s1 - s2 + c.getReal(); //c2 for julia
			double image2 = 2 * real * image + c.getImaginary(); //c2 for julia
			real = real2;
			image = image2;
		}

		return -1;
	}
}
