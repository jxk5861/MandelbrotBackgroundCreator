package karabin.mandelbrot.drawing.escape;

import org.apache.commons.math3.complex.Complex;

public abstract class EscapeRate implements EscapeRateIF {
	protected int iterations;
	protected int max;
	
	public EscapeRate(int iterations, int max) {
		super();
		this.iterations = iterations;
		this.max = max;
	}

	@Override
	public double rate(Complex c) {
		double real = 0;
		double image = 0;
		
		for(int i = 1; i < iterations; i++) {
			double real2 = real*real - image*image + c.getReal();
			double image2 = 2 * real * image + c.getImaginary();
			real = real2;
			image = image2;
			if(real*real + image*image > max) {
				return i;
			}
		}
		
		return 0;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
}
