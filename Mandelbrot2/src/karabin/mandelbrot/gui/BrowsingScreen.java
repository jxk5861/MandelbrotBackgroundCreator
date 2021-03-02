package karabin.mandelbrot.gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import karabin.mandelbrot.drawing.DistanceEstimator;
import karabin.mandelbrot.drawing.DrawingMethod;
import karabin.mandelbrot.drawing.mulithreaded.HistogramSingleColor;
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
		DrawingMethod method = new HistogramSingleColor(0xff, 4, Color.red);//new DistanceEstimator(0xff, 4, Color.red);

		imagePanel = new ImagePanel(width, height, method);
		
		ZoomMouseListener listener = new ZoomMouseListener(imagePanel, width, height);
		imagePanel.addMouseWheelListener(listener);
		imagePanel.addMouseListener(listener);
		imagePanel.addMouseMotionListener(listener);

		controlsPanel = new ControlsPanel(width, 40, method, imagePanel);

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
