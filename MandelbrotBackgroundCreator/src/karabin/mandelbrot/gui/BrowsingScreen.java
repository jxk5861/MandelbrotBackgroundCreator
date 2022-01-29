package karabin.mandelbrot.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import karabin.mandelbrot.gui.panels.ControlsPanel;
import karabin.mandelbrot.gui.panels.ImagePanel;

public class BrowsingScreen extends JFrame {
	private static final long serialVersionUID = 6785151737124858356L;
	// seperate into classes.
	private JPanel panel;
	private ImagePanel imagePanel;
	private JPanel controlsPanel;

	public BrowsingScreen(int width, int height) {
		JLabel domainX = new JLabel("x: -2.5");
		JLabel domainY = new JLabel("y: -1.0");
		JLabel domainW = new JLabel("w: 3.5");
		JLabel domainH = new JLabel("h: 2.0");
		imagePanel = new ImagePanel(width, height, domainX, domainY, domainW, domainH);
		controlsPanel = new ControlsPanel(width, 80, imagePanel, domainX, domainY, domainW, domainH);

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
