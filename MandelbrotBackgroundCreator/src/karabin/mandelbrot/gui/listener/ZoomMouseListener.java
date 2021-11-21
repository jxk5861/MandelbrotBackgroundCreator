package karabin.mandelbrot.gui.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.gui.panels.ImagePanel;
import karabin.mandelbrot.utils.FractalUtils;

public class ZoomMouseListener implements MouseWheelListener, MouseListener, MouseMotionListener {

	private ImagePanel panel;
	private int width;
	private int height;

	private int x;
	private int y;
	private double rx;
	private double ry;

	public ZoomMouseListener(ImagePanel panel, int width, int height) {
		super();
		this.panel = panel;
		this.width = width;
		this.height = height;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Rectangle2D domain = panel.getDomain();

		AffineTransform tr2 = new AffineTransform();
		double zoom = 1 + .1 * Math.abs(e.getPreciseWheelRotation());
		Complex c = FractalUtils.pixelToComplex(e.getPoint().getX(), e.getPoint().getY(), width, height, domain);
		tr2.translate(c.getReal(), c.getImaginary());
		tr2.scale(zoom, zoom);
		tr2.translate(-c.getReal(), -c.getImaginary());

		if (e.getPreciseWheelRotation() > 0) {
			Point2D l = new Point2D.Double(domain.getX(), domain.getY());
			Point2D r = new Point2D.Double(domain.getMaxX(), domain.getMaxY());

			Point2D l2 = new Point2D.Double();
			Point2D r2 = new Point2D.Double();

			tr2.transform(l, l2);
			tr2.transform(r, r2);

			domain.setRect(l2.getX(), l2.getY(), r2.getX() - l2.getX(), r2.getY() - l2.getY());
		} else {
			try {
				tr2.invert();
			} catch (NoninvertibleTransformException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Point2D l = new Point2D.Double(domain.getX(), domain.getY());
			Point2D r = new Point2D.Double(domain.getMaxX(), domain.getMaxY());

			Point2D l2 = new Point2D.Double();
			Point2D r2 = new Point2D.Double();

			tr2.transform(l, l2);
			tr2.transform(r, r2);

			domain.setRect(l2.getX(), l2.getY(), r2.getX() - l2.getX(), r2.getY() - l2.getY());
		}

		panel.draw();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.x = e.getX();
		this.y = e.getY();
		
		this.rx = this.panel.getDomain().getX();
		this.ry = this.panel.getDomain().getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.reDraw(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.reDraw(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	private void reDraw(int x, int y) {
		Complex start = FractalUtils.pixelToComplex(this.x, this.y, width, height, panel.getDomain());
		Complex end = FractalUtils.pixelToComplex(x, y, width, height, panel.getDomain());
		Complex offset = start.subtract(end);
		Rectangle2D domain = panel.getDomain();

		domain.setRect(rx + offset.getReal(),ry + offset.getImaginary(), domain.getWidth(), domain.getHeight());

		panel.draw();
	}
}
