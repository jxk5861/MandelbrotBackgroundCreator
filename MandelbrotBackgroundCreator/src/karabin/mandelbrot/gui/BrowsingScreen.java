package karabin.mandelbrot.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import karabin.mandelbrot.gui.listener.ZoomMouseListener;
import karabin.mandelbrot.gui.panels.ControlsPanel;
import karabin.mandelbrot.gui.panels.ImagePanel;

public class BrowsingScreen extends JFrame {
	private static final long serialVersionUID = 6785151737124858356L;
	// seperate into classes.
	private JPanel panel;
	private ImagePanel imagePanel;
	private JPanel controlsPanel;

	public BrowsingScreen(int width, int height) {
		imagePanel = new ImagePanel(width, height);
		controlsPanel = new ControlsPanel(width, 60, imagePanel);

//		imagePanel.setPreferredSize(new Dimension(width, height));
//		controlsPanel.setPreferredSize(new Dimension(200, height));

		panel = new JPanel();
		panel.add(controlsPanel);
		panel.add(imagePanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
