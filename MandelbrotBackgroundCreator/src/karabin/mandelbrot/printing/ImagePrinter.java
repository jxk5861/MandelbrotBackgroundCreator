package karabin.mandelbrot.printing;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

		int id = getNextFileId(file);

		// Log the fractal's domain.
		String fractalString = String.format("Fractal%d.png", id);
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File(file, "Fractal Logs.txt"), true))) {
			pw.println(fractalString + " " + domain.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Save the fractal to the specified folder.
		File output = new File(file, fractalString);
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e) {
			 e.printStackTrace();
		}
	}

	private int getNextFileId(File file) {
		int id = 1;
		for (File f : file.listFiles()) {
			if (f.getName().matches("Fractal\\d+.png")) {
				Pattern pattern = Pattern.compile("\\d+");
				Matcher matcher = pattern.matcher(f.getName());

				if (matcher.find()) {
					id = Math.max(id, Integer.parseInt(matcher.group()) + 1);
				}

			}
		}
		return id;
	}
}
