package karabin.mandelbrot.drawing;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public interface DrawingMethodIF {
	public abstract void draw(BufferedImage image, Rectangle2D unsafeDomain);
}
