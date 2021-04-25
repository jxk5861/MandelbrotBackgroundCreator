package karabin.mandelbrot.drawing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import karabin.mandelbrot.drawing.mulithreaded.HistogramSingleColor;
import karabin.mandelbrot.drawing.mulithreaded.NormalizedEscapeSingleColor;

public enum DrawingManager {
	INSTANCE;
	
	private DrawingMethod selected;
	private List<DrawingMethod> methods;
	
	private DrawingManager() {
		methods = new ArrayList<DrawingMethod>();
		methods.add(new HistogramSingleColor(0xff, 4, Color.red));
		methods.add(new NormalizedEscapeSingleColor(0xff, 4, Color.red));
		selected = methods.get(0);
	}
	
	public List<DrawingMethod> getDrawingMethods(){
		return this.methods;
	}
	
	public void setSelected(int index) {
		this.selected = methods.get(index);
	}
	
	public void setSelected(DrawingMethod method) {
		if(this.methods.contains(method)) {
			this.selected = method;
		}
	}
	
	public DrawingMethod getSelected() {
		return this.selected;
	}
}
