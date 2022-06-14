package karabin.mandelbrot;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import karabin.mandelbrot.gui.BrowsingScreen;

public class Main {
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		int factor = 50;
		// 2*195, 2*422 | 16*factor, 9*factor
		SwingUtilities.invokeAndWait(() -> new BrowsingScreen(16*factor, 9*factor).setVisible(true));
	}
}
