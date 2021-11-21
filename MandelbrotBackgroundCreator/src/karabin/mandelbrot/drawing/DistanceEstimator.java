package karabin.mandelbrot.drawing;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.utils.FractalUtils;

public class DistanceEstimator extends DrawingMethod{

	public DistanceEstimator(int iterations, int max, Color color) {
		super(iterations, max, color);
	}
	
	protected double distanceEstimator(Complex c) {
		return Double.NaN;
	}
	
	@Override
	public void draw(BufferedImage image, Rectangle2D domain) {
		int height = image.getHeight();
		int width = image.getWidth();
 
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Complex c = FractalUtils.pixelToComplex(x, y, width, height, domain);
				
			}
		}
	}
}
