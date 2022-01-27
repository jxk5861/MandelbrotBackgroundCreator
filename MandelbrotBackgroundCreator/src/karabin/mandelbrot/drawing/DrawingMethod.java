package karabin.mandelbrot.drawing;

import java.awt.Color;

import karabin.mandelbrot.drawing.coloring.ColorStrategy;
import karabin.mandelbrot.drawing.coloring.MapSingleColorStrategy;

public abstract class DrawingMethod implements DrawingMethodIF {

	protected int iterations;
	protected int max;
	// To avoid concurrent modification, set the reference of this to a new value.
	private ColorStrategy coloring;

	public DrawingMethod(int iterations, int max, Color color) {
		super();
		this.iterations = iterations;
		this.max = max;
		this.coloring = new MapSingleColorStrategy(color);
	}

	protected int color(double rate) {
		return coloring.color(rate).getRGB();
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public ColorStrategy getColoring() {
		return coloring;
	}

	public void setColoring(ColorStrategy coloring) {
		this.coloring = coloring;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
