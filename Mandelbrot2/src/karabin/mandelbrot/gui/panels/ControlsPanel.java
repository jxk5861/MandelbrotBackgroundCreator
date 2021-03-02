package karabin.mandelbrot.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import karabin.mandelbrot.drawing.DrawingMethod;

public class ControlsPanel extends JPanel {
	private static final long serialVersionUID = -8346672836124829397L;

	private ImagePanel panel;

	private DrawingMethod method;
	private JPanel chooseColor;
	private JLabel iterationsLabel;
	private JTextField iterationsField;

	public ControlsPanel(int width, int height, DrawingMethod method, ImagePanel panel) {
		this.panel = panel;
		this.method = method;

		chooseColor = new JPanel();
		chooseColor.setBackground(Color.red);
		chooseColor.setPreferredSize(new Dimension(25, 25));
		chooseColor.setBorder(BorderFactory.createLineBorder(Color.black));
		chooseColor.addMouseListener(new MouseListener() {
			boolean clicking = false;

			@Override
			public void mouseReleased(MouseEvent e) {
				if (clicking) {
					Color color = JColorChooser.showDialog(null, "Choose a color", ControlsPanel.this.method.getColor());
					if (color == null) {
						return;
					}
					ControlsPanel.this.chooseColor.setBackground(color);
					ControlsPanel.this.method.setColor(color);
					ControlsPanel.this.panel.draw();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				clicking = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (clicking) {
					clicking = false;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
//		chooseColor.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Color c = JColorChooser.showDialog(null, "Choose a color", coloring.getColor());
//				ControlsPanel.this.coloring.setColor(c);
//				ControlsPanel.this.panel.draw();
//			}
//		});

		this.iterationsLabel = new JLabel("Iterations: ");
		this.iterationsField = new JTextField("255", 3);
		this.iterationsField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					int iterations = Integer.parseInt(iterationsField.getText());
					if(iterations <= 0) {
						throw new NumberFormatException("For input string \"%s\"".formatted(iterationsField.getText()));
					}
					ControlsPanel.this.method.setIterations(iterations);
					ControlsPanel.this.panel.draw();
					ControlsPanel.this.iterationsField.setForeground(Color.black);
				} catch (NumberFormatException ee) {
					 ee.printStackTrace();
					ControlsPanel.this.iterationsField.setForeground(Color.red);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

		this.add(chooseColor);
		this.add(iterationsLabel);
		this.add(iterationsField);

		this.setPreferredSize(new Dimension(width, height));
	}
}
