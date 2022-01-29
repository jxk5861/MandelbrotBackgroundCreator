package karabin.mandelbrot.gui.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.swing.JLabel;
import javax.swing.JPanel;

import karabin.mandelbrot.drawing.DrawingManager;
import karabin.mandelbrot.gui.listener.ZoomMouseListener;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -8485184329163899100L;

	private BufferedImage image;
	private Rectangle2D domain = new Rectangle2D.Double(-2.5, -1, 3.5, 2);
	
	private JLabel domainX;
	private JLabel domainY;
	private JLabel domainW;
	private JLabel domainH;

	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	Runnable runDraw = new Runnable() {

		@Override
		public void run() {
			DrawingManager.INSTANCE.getSelected().draw(image, domain);
			ImagePanel.this.repaint();
		}
	};

	public ImagePanel(int width, int height, JLabel domainX, JLabel domainY, JLabel domainW, JLabel domainH) {
		this.domainX = domainX;
		this.domainY = domainY;
		this.domainW = domainW;
		this.domainH = domainH;
		
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		DrawingManager.INSTANCE.getSelected().draw(image, domain);

		ZoomMouseListener listener = new ZoomMouseListener(this, width, height);
		this.addMouseWheelListener(listener);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.addKeyListener(listener);

		this.setPreferredSize(new Dimension(width, height));
		this.setFocusable(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	public BufferedImage getImage() {
		return this.image;
	}

	public void domainSetRect(double x, double y, double w, double h) {
		this.domain.setRect(x, y, w, h);
		
		this.domainX.setText(String.format("x: %f", x));
		this.domainY.setText(String.format("y: %f", y));
		this.domainW.setText(String.format("w: %ef", w));
		this.domainH.setText(String.format("h: %ef", h));
	}
	
	public Rectangle2D getDomain() {
		return this.domain;
	}

	public void draw() {
		executor.getQueue().clear();
		executor.execute(runDraw);
	}
}
