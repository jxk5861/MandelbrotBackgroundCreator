package karabin.mandelbrot.drawing.coloring;

import java.awt.Color;
import java.util.TreeMap;

public abstract class MapColorStrategy implements ColorStrategy {
	protected final static long MAP_SIZE = 10000;
	protected final TreeMap<Double, Color> map;

	public MapColorStrategy() {
		super();

		map = new TreeMap<>();
	}

	@Override
	public final Color color(double rate) {
		if (rate < 0) {
			rate = 0;
		} else if (rate > 1) {
			rate = 1;
		}

		if(rate == 0) {
			return Color.BLACK;
		}
		
		return map.ceilingEntry(rate).getValue();
	}
}
