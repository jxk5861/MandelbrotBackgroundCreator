package karabin.mandelbrot.drawing.coloring;

import java.awt.Color;

public interface ColorStrategy {
	/**
	 * @param rate A double in the range [0, ~1)
	 * */
	public Color color(double rate);
}
