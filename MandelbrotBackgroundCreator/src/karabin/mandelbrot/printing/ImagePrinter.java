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

/**
 * The image printer is used to print images to a folder. It is a singleton
 * class.
 */
public enum ImagePrinter {
	INSTANCE;

	private ImagePrinter() {

	}

	/**
	 * Print the current region to a file with resolution width x height.
	 */
	public void printToPNG(File file, int width, int height, Rectangle2D domain) {
		int id = getNextFileId(file);

		// Log the fractal's domain and the color gradient.
		String fractalString = String.format("Fractal%d.png", id);
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File(file, "Fractal Logs.txt"), true))) {
			pw.println(fractalString + " " + domain.toString());
			pw.println(DrawingManager.INSTANCE.getSelected().getColoring().toString());
			pw.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		DrawingManager.INSTANCE.getSelected().draw(image, domain);

		// Save the fractal to the specified folder.
		File output = new File(file, fractalString);
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Find the next file name in the folder by finding the largest number from
	 * files of the form: Fractal{number}.png and adding one to it.
	 * 
	 * If the folder has Fractal1.png & Fractal3.png the next image name will be
	 * Fractal4.png.
	 */
	private int getNextFileId(File folder) {
		int id = 1;
		for (File f : folder.listFiles()) {
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
