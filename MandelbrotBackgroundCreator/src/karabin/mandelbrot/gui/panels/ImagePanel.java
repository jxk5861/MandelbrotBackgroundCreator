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

/**
 * The panel which renders the image of the fractal for the user to view and
 * browse.
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -8485184329163899100L;

	private BufferedImage image;
	private Rectangle2D domain;

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
		this.domain = new Rectangle2D.Double(-2.5, -1, 3.5, 2);

		this.domainX = domainX;
		this.domainY = domainY;
		this.domainW = domainW;
		this.domainH = domainH;

		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		DrawingManager.INSTANCE.getSelected().draw(image, domain);

		ZoomMouseListener listener = new ZoomMouseListener(this);
		this.addMouseWheelListener(listener);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.addKeyListener(listener);

		this.setPreferredSize(new Dimension(width, height+50));
		this.setFocusable(true);
	}

	/**
	 * Set new dimensions for the image. This is useful for switching between
	 * different printing resolutions such as 16:9 or IPhone 13 resolution.
	 */
	public void setImageDimensions(int width, int height) {
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * This is over ridden to allow the image to be drawn on the screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int x = (this.getWidth() - image.getWidth()) / 2;
		int y = (this.getHeight() - image.getHeight()) / 2;
		
		g.drawImage(image, x, y, null);
	}

	/**
	 * Get the current image that is displayed on the screen. All drawing threads
	 * draw on the same image to save memory and no side effects have been observed.
	 */
	public BufferedImage getImage() {
		return this.image;
	}

	/**
	 * Set the current domain of the fractal to new values and update the domain
	 * labels. Setting the domain while the program is drawing can cause issues but
	 * most rendering methods create a clone beforehand.
	 */
	// TODO: Make is so rendering methods do not need to clone the domain and that
	// instead they are passed an immutable domain / a clone.
	public void domainSetRect(double x, double y, double w, double h) {
		this.domain.setRect(x, y, w, h);

		this.domainX.setText(String.format("x: %f", x));
		this.domainY.setText(String.format("y: %f", y));
		this.domainW.setText(String.format("w: %ef", w));
		this.domainH.setText(String.format("h: %ef", h));
	}

	/**
	 * Returns the domain as a clone since it is not immutable. Setting the domain
	 * while the program is drawing can cause issues so returning a clone prevents
	 * this.
	 */
	// TODO: Create a singleton domain to improve code.
	public Rectangle2D getDomain() {
		return (Rectangle2D) this.domain.clone();
	}

	/**
	 * Asynchronously attempt to draw the fractal. All previous attempts to render a
	 * fractal will be cleared. This means if the user is scrolling only the latest
	 * frame is rendered, instead of needing to render a frame for each scroll
	 * change. This makes rendering much faster since only the desired frame is
	 * rendered and all others are dropped.
	 */
	public void draw() {
		executor.getQueue().clear();
		executor.execute(runDraw);
	}
}
