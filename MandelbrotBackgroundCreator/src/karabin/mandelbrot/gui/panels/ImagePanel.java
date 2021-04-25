package karabin.mandelbrot.gui.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.swing.JPanel;

import karabin.mandelbrot.drawing.DrawingManager;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -8485184329163899100L;
	
	private BufferedImage image;
	private Rectangle2D domain = new Rectangle2D.Double(-2.5, -1, 3.5, 2);
	
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	Runnable runDraw = new Runnable() {
		
		@Override
		public void run() {
			DrawingManager.INSTANCE.getSelected().draw(image, domain);
			ImagePanel.this.repaint();
		}
	};

	public ImagePanel(int width, int height) {
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		DrawingManager.INSTANCE.getSelected().draw(image, domain);
		
		this.setPreferredSize(new Dimension(width, height));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	public BufferedImage getImage() {
		return this.image;
	}

	public Rectangle2D getDomain() {
		return domain;
	}
	
	public void draw() {
		executor.getQueue().clear();
		executor.execute(runDraw);
	}
}
