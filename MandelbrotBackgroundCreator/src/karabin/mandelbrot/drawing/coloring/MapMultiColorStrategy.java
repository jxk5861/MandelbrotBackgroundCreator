package karabin.mandelbrot.drawing.coloring;

import java.awt.Color;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class MapMultiColorStrategy extends MapColorStrategy {

	private ColorGradient colorGraph;

	public MapMultiColorStrategy(ColorGradient colorGraph) {
		super();

		this.colorGraph = colorGraph;
		this.initMap();
	}
	
	public static MapMultiColorStrategy of(ColorGradient colorGraph) {
		return new MapMultiColorStrategy(colorGraph);
	}

	public void initMap() {
		var interpolatable = this.colorGraph.interpolatable();
		var x = interpolatable.getX();
		var y = interpolatable.getY();

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
