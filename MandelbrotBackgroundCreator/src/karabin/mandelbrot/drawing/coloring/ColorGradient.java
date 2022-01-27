package karabin.mandelbrot.drawing.coloring;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class ColorGradient {
	public class Unique {
		private float[] positions;
		private Color[] colors;

		public Unique(float[] positions, Color[] colors) {
			super();

			this.positions = positions;
			this.colors = colors;
		}

		public float[] getPositions() {
			return positions;
		}

		public Color[] getColors() {
			return colors;
		}

		public int size() {
			return positions.length;
		}

		@Override
		public String toString() {
			return "Unique [positions=" + Arrays.toString(positions) + ", colors=" + Arrays.toString(colors) + "]";
		}

	}

	public class Interpolatable {
		private double[] x;
		private List<Color> y;

		public Interpolatable(double[] x, List<Color> y) {
			super();
			this.x = x;
			this.y = y;
		}

		public double[] getX() {
			return x;
		}

		public List<Color> getY() {
			return y;
		}

		public int size() {
			return x.length;
		}

		@Override
		public String toString() {
			return "Interpolatable [x=" + Arrays.toString(x) + ", y=" + y + "]";
		}

	}

	private List<Double> positions;
	private List<Color> colors;

	public ColorGradient() {
		super();

		positions = new ArrayList<>();
		colors = new ArrayList<>();
	}

	public List<Double> getPositions() {
		return positions;
	}

	public List<Color> getColors() {
		return colors;
	}

	public void remove(int i) {
		positions.remove(i);
		colors.remove(i);
	}

	public void add(double x, Color y) {
		positions.add(x);
		colors.add(y);
	}

	public Unique unique() {
		TreeMap<Double, Color> map = this.toUniqueMap();

		float[] positions = new float[map.size()];
		Color[] colors = new Color[map.size()];
		int i = 0;
		for (var e : map.entrySet()) {
			positions[i] = e.getKey().floatValue();
			colors[i] = e.getValue();
			i++;
		}

		return new Unique(positions, colors);
	}

	public Interpolatable interpolatable() {
		TreeMap<Double, Color> map = this.toUniqueMap();
		double first = map.firstKey();
		double last = map.lastKey();

		if (first > 0) {
			map.put(0D, map.get(first));
		}
		if (last < 1) {
			map.put(1D, map.get(last));
		}

		double[] x = new double[map.size()];
		List<Color> y = new ArrayList<>();
		int i = 0;
		for (var e : map.entrySet()) {
			x[i++] = e.getKey().floatValue();
			y.add(e.getValue());
		}

		return new Interpolatable(x, y);
	}

	private TreeMap<Double, Color> toUniqueMap() {
		TreeMap<Double, Color> map = new TreeMap<>();
		for (int i = 0; i < this.size(); i++) {
			map.put(this.positions.get(i), this.colors.get(i));
		}
		return map;
	}

	public int size() {
		return this.positions.size();
	}

	@Override
	public String toString() {
		return "ColorGradient [positions=" + positions + ", colors=" + colors + "]";
	}

}
