package karabin.mandelbrot.gui.listener;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.gui.panels.ImagePanel;
import karabin.mandelbrot.utils.MandelbrotUtils;

public class ZoomMouseWheelListener implements MouseWheelListener {

	private ImagePanel panel;
	private int width;
	private int height;

	public ZoomMouseWheelListener(ImagePanel panel, int width, int height) {
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
		Complex c = MandelbrotUtils.pixelToComplex(e.getPoint().getX(), e.getPoint().getY(), width, height, domain);
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

}
