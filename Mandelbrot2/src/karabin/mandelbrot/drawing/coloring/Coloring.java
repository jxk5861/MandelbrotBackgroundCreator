package karabin.mandelbrot.drawing.coloring;

import java.awt.Color;

public abstract class Coloring implements ColoringIF {
	protected Color color;
	
	/**
	 * @param color the color to use for this coloring algorithm
	 * */
	public Coloring(Color color) {
		super();
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
