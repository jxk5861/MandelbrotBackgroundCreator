package karabin.mandelbrot.drawing;

import java.awt.Color;

public abstract class DrawingMethod implements DrawingMethodIF {

	protected int iterations;
	protected int max;
	protected Color color;

	public DrawingMethod(int iterations, int max, Color color) {
		super();
		this.iterations = iterations;
		this.max = max;
		this.color = color;
	}

	protected int color(double rate) {
		if (rate == -1) {
			return 0;
		}

		
		double factor = rate / this.iterations;
		
		// normalized escape with mods and alternating color
//		Color color = this.color;
//		if((int)(rate / 255) % 2 == 1) {
//			color = new Color(255, 255, 100);
//		}
//		double factor = (rate % 255) / 255;

		
		return new Color((int)(factor * color.getRed()), (int) (factor * color.getGreen()), (int)(factor * color.getBlue())).getRGB();
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
