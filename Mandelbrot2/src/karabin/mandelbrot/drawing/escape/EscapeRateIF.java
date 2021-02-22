package karabin.mandelbrot.drawing.escape;

import org.apache.commons.math3.complex.Complex;

public interface EscapeRateIF {
	public abstract double rate(Complex c);
}
