package karabin.mandelbrot.drawing.mulithreaded.renderingsection;

import java.awt.geom.Rectangle2D;

public abstract class RenderingSection extends Thread{
	protected int width;
	protected int height;
	protected int startHeight;
	protected int endHeight;
	protected Rectangle2D domain;
	protected int[][] rates;
	protected boolean interrupted;

	public RenderingSection(int width, int height, int startHeight, int endHeight, Rectangle2D domain,
			int[][] rates) {
		super();
		this.width = width;
		this.height = height;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.domain = domain;
		this.rates = rates;
		this.interrupted = false;
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		this.interrupted = true;
	}
}
