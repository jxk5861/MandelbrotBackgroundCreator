package karabin.mandelbrot.printing;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import karabin.mandelbrot.drawing.DrawingManager;

public enum ImagePrinter {
	INSTANCE;

	private ImagePrinter() {

	}

	public void printToPNG(File file, int width, int height, Rectangle2D domain) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		DrawingManager.INSTANCE.getSelected().draw(image, domain);
		
		int id = 1;
		for (File f : file.listFiles()) {
			if (f.getName().matches("Mandelbrot\\d+.png")) {
				Pattern pattern = Pattern.compile("\\d+");
				Matcher matcher = pattern.matcher(f.getName());

				if (matcher.find()) {
					id = Math.max(id, Integer.parseInt(matcher.group()) + 1);
				}

			}
		}

		File output = new File(file, String.format("Mandelbrot%d.png", id));
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
}
