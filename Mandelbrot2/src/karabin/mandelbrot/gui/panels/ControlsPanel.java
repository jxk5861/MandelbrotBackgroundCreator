package karabin.mandelbrot.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import karabin.mandelbrot.drawing.coloring.Coloring;
import karabin.mandelbrot.drawing.escape.EscapeRate;
import karabin.mandelbrot.drawing.escape.EscapeRateIF;

public class ControlsPanel extends JPanel {
	private static final long serialVersionUID = -8346672836124829397L;

	private ImagePanel panel;
	
	private EscapeRateIF escape;
	private Coloring coloring;
	
	private JPanel chooseColor;
	
	public ControlsPanel(int width, int height, EscapeRate escape, Coloring coloring, ImagePanel panel) {
		this.escape = escape;
		this.coloring = coloring;
		this.panel = panel;
		
		chooseColor = new JPanel();
		chooseColor.setBackground(Color.red);
		chooseColor.setPreferredSize(new Dimension(25, 25));
		chooseColor.setBorder(BorderFactory.createLineBorder(Color.black));
		chooseColor.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Color color = JColorChooser.showDialog(null, "Choose a color", coloring.getColor());
				if(color == null) {
					return;
				}
				ControlsPanel.this.chooseColor.setBackground(color);
				ControlsPanel.this.coloring.setColor(color);
				ControlsPanel.this.panel.draw();
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
		this.add(chooseColor);

		this.setPreferredSize(new Dimension(200, height));
	}
}
