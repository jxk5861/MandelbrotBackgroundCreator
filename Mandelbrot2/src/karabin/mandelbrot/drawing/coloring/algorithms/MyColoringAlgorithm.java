package karabin.mandelbrot.drawing.coloring.algorithms;

import java.awt.Color;

import karabin.mandelbrot.drawing.coloring.Coloring;

public class MyColoringAlgorithm extends Coloring {
	public MyColoringAlgorithm(Color color) {
		super(color);
	}

	@Override
	public int color(double rate) {
		if (rate == -1) {
			return 0;
		}
		int factor = (int) rate;
		return new Color(Math.min(factor * color.getRed() / 255, 255), Math.min(factor * color.getGreen() / 255, 255), Math.min(factor * color.getBlue() / 255, 255)).getRGB();
	}
}
