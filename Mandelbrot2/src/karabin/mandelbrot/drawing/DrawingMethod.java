package karabin.mandelbrot.drawing;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.apache.commons.math3.complex.Complex;

import karabin.mandelbrot.drawing.coloring.ColoringIF;
import karabin.mandelbrot.drawing.escape.EscapeRateIF;
import karabin.mandelbrot.utils.MandelbrotUtils;

public abstract class DrawingMethod implements DrawingMethodIF {

	protected EscapeRateIF escapeRate;
	protected ColoringIF coloring;

	public DrawingMethod(EscapeRateIF escapeRate, ColoringIF coloring) {
		super();
		this.escapeRate = escapeRate;
		this.coloring = coloring;
	}

	@Override
	public void draw(BufferedImage image, Rectangle2D domain) {
		int height = image.getHeight();
		int width = image.getWidth();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Complex c = MandelbrotUtils.pixelToComplex(x, y, width, height, domain);
				double rate = escapeRate.rate(c);
				int rgb = coloring.color(rate);
				image.setRGB(x, y, rgb);
			}
		}
		
//		Complex [][] cs = new Complex[width][height];
//		double [][] reals = new double[width][height];
//		double [][] images = new double[width][height];
//		for(int y = 0; y < height; y++) {
//			for(int x = 0; x < width; x++) {
//				Complex c = MandelbrotUtils.pixelToComplex(x, y, width, height, domain);
//				reals[x][y] = c.getReal();
//				images[x][y] = c.getImaginary();
//				cs[x][y] = c;
//			}
//		}
//		
//		
//		int [][] rates = new int[width][height];
//		boolean [][] b = new boolean[width][height];
//		long start = System.currentTimeMillis();
//		int mr = 0;
//		while(System.currentTimeMillis() - start < 1000 || mr < 255) {
//			for(int y = 0; y < height; y++) {
//				for(int x = 0; x < width; x++) {
//					if(b[x][y]) {
//						continue;
//					}
//					Complex c = cs[x][y];
//					if(inc(x, y, c, reals, images, 4)) {
//						rates[x][y]++;
//						if(rates[x][y] > mr) {
//							mr = rates[x][y];
//						}
//					}else {
//						b[x][y] = true;
//					}
//				}
//			}
//		}
//		System.out.println(mr);
//		
//		for(int y = 0; y < height; y++) {
//			for(int x = 0; x < width; x++) {
//				if(!b[x][y]) {
//					image.setRGB(x, y, 0);
//					continue;
//				}
//				double s1 = reals[x][y] * reals[x][y];
//				double s2 = images[x][y] * images[x][y];
//				double normalizedEscape = rates[x][y] + 1 - Math.log10(Math.log(s1 + s2) / Math.log(2));
//				int rgb = coloring.color(normalizedEscape);
//				image.setRGB(x, y, rgb);
//			}
//		}
		
	}
	
//	private boolean inc(int x, int y, Complex c, double [][] reals, double[][] images, int max) {
//		double s1 = reals[x][y] * reals[x][y];
//		double s2 = images[x][y] * images[x][y];
//		if (s1 + s2 > max) {
//			return false;
//			//return i + 1 - Math.log10(Math.log(s1 + s2) / Math.log(2));
//		}
//		double real2 = s1 - s2 + c.getReal();
//		double image2 = 2 * reals[x][y] * images[x][y] + c.getImaginary();
//		reals[x][y] = real2;
//		images[x][y] = image2;
//		return true;
//	}
}
