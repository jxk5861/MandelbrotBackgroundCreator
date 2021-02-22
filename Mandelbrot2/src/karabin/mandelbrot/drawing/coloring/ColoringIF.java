package karabin.mandelbrot.drawing.coloring;

public interface ColoringIF {
	/**
	 * Get the rgb color for the escape rate of a point in the fractal.
	 * @param rate the escape rate of the pixel to color. 
	 * @return the color as an rgb int.
	 * */
	public int color(double rate);
}
