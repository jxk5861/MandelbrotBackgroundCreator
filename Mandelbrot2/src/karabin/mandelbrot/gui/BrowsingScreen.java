package karabin.mandelbrot.gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import karabin.mandelbrot.drawing.DrawingMethodIF;
import karabin.mandelbrot.drawing.coloring.Coloring;
import karabin.mandelbrot.drawing.coloring.algorithms.MyColoringAlgorithm;
import karabin.mandelbrot.drawing.escape.EscapeRate;
import karabin.mandelbrot.drawing.escape.algorithms.NormalizedEscape;
import karabin.mandelbrot.drawing.mulithreading.MultiThreadedDrawingMethod;
import karabin.mandelbrot.gui.listener.ZoomMouseWheelListener;
import karabin.mandelbrot.gui.panels.ControlsPanel;
import karabin.mandelbrot.gui.panels.ImagePanel;

public class BrowsingScreen extends JFrame{
	private static final long serialVersionUID = 6785151737124858356L;
	// seperate into classes.
	private JPanel panel;
	private ImagePanel imagePanel;
	private JPanel controlsPanel;
	
	public BrowsingScreen(int width, int height) {
		EscapeRate escape = new NormalizedEscape(0xff, 4);
		Coloring coloring = new MyColoringAlgorithm(Color.red);
		DrawingMethodIF method = new MultiThreadedDrawingMethod(escape, coloring);		
		
		
		imagePanel = new ImagePanel(width, height, method);
		imagePanel.addMouseWheelListener(new ZoomMouseWheelListener(imagePanel, width, height));
		
		controlsPanel = new ControlsPanel(200, height, escape, coloring, imagePanel);
		
		
//		imagePanel.setPreferredSize(new Dimension(width, height));
//		controlsPanel.setPreferredSize(new Dimension(200, height));

		panel = new JPanel();
		panel.add(imagePanel);
		panel.add(controlsPanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
