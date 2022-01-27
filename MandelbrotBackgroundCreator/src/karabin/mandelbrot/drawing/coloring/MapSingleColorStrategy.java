package karabin.mandelbrot.drawing.coloring;

import java.awt.Color;
import java.util.List;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class MapSingleColorStrategy extends MapColorStrategy {
	private Color color;

	public MapSingleColorStrategy(Color color) {
		super();

		this.color = color;
		this.initMap();
	}

	public static MapSingleColorStrategy of(Color color) {
		return new MapSingleColorStrategy(color);
	}

	public void initMap() {
		double[] x = { 0, 1 };
		List<Color> y = List.of(Color.BLACK, color);

		LinearInterpolator lin = new LinearInterpolator();

		PolynomialSplineFunction r = lin.interpolate(x, y.stream().mapToDouble(Color::getRed).toArray());
		PolynomialSplineFunction g = lin.interpolate(x, y.stream().mapToDouble(Color::getGreen).toArray());
		PolynomialSplineFunction b = lin.interpolate(x, y.stream().mapToDouble(Color::getBlue).toArray());

		for (int i = 1; i <= MAP_SIZE; i++) {
			double position = (double) i / MAP_SIZE;
			int red = (int) r.value(position);
			int green = (int) g.value(position);
			int blue = (int) b.value(position);

			map.put(position, new Color(red, green, blue));
		}
	}

}
