package karabin.mandelbrot.drawing.coloring;

import java.awt.Color;

public class SingleColorStrategy implements ColorStrategy {
	private MapMultiColorStrategy strategy;

	public SingleColorStrategy(Color color) {
		super();

		ColorGradient colorGradient = new ColorGradient();
		colorGradient.add(0, Color.BLACK);
		colorGradient.add(1, color);

		this.strategy = new MapMultiColorStrategy(colorGradient);
	}

	public static SingleColorStrategy of(Color color) {
		return new SingleColorStrategy(color);
	}

	@Override
	public Color color(double rate) {
		return strategy.color(rate);
	}

	@Override
	public String toString() {
		return "SingleColorStrategy [positions=" + strategy + "]";
	}
}
