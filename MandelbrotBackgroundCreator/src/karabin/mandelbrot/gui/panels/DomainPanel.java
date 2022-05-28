package karabin.mandelbrot.gui.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class DoubleFieldListener implements KeyListener {

	private JTextField field;

	public DoubleFieldListener(JTextField field) {
		super();
		this.field = field;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		try {
			Double.parseDouble(this.field.getText());
			field.setForeground(Color.BLACK);
		} catch (NumberFormatException e2) {
			field.setForeground(Color.RED);
		}
	}

}

public class DomainPanel extends JPanel {
	JLabel x;
	JTextField xi;
	JLabel y;
	JTextField yi;
	JLabel w;
	JTextField wi;
	JLabel h;
	JTextField hi;

	JButton parseClipboardButton;

	public DomainPanel(final Rectangle2D currentDomain) {
		x = new JLabel("x: ");
		y = new JLabel("y: ");
		w = new JLabel("w: ");
		h = new JLabel("h: ");

		xi = new JTextField(Double.toString(currentDomain.getX()), 20);
		yi = new JTextField(Double.toString(currentDomain.getY()), 20);
		wi = new JTextField(Double.toString(currentDomain.getWidth()), 20);
		hi = new JTextField(Double.toString(currentDomain.getHeight()), 20);

		xi.addKeyListener(new DoubleFieldListener(xi));
		yi.addKeyListener(new DoubleFieldListener(yi));
		wi.addKeyListener(new DoubleFieldListener(wi));
		hi.addKeyListener(new DoubleFieldListener(hi));

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(4, 2));

		inputPanel.add(x);
		inputPanel.add(xi);
		inputPanel.add(y);
		inputPanel.add(yi);
		inputPanel.add(w);
		inputPanel.add(wi);
		inputPanel.add(h);
		inputPanel.add(hi);

		parseClipboardButton = new JButton("Parse From Clipboard");
		parseClipboardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
							.getData(DataFlavor.stringFlavor);
					Pattern p = Pattern.compile("x=-?[0-9]+.?[0-9]*E?-?[0-9]*");
					Matcher m = p.matcher(data);
					if (m.find()) {
						String value = m.group().substring(2);
						xi.setText(Double.toString(Double.parseDouble(value)));
						xi.setForeground(Color.BLACK);
					} else {
						parseClipboardButton.setForeground(Color.RED);
						return;
					}
					p = Pattern.compile("y=-?[0-9]+.?[0-9]*E?-?[0-9]*");
					m = p.matcher(data);
					if (m.find()) {
						String value = m.group().substring(2);
						yi.setText(Double.toString(Double.parseDouble(value)));
						yi.setForeground(Color.BLACK);
					} else {
						parseClipboardButton.setForeground(Color.RED);
						return;
					}
					p = Pattern.compile("w=-?[0-9]+.?[0-9]*E?-?[0-9]*");
					m = p.matcher(data);
					if (m.find()) {
						String value = m.group().substring(2);
						wi.setText(Double.toString(Double.parseDouble(value)));
						wi.setForeground(Color.BLACK);
					} else {
						parseClipboardButton.setForeground(Color.RED);
						return;
					}
					p = Pattern.compile("h=-?[0-9]+.?[0-9]*E?-?[0-9]*");
					m = p.matcher(data);
					if (m.find()) {
						String value = m.group().substring(2);
						hi.setText(Double.toString(Double.parseDouble(value)));
						hi.setForeground(Color.BLACK);
					} else {
						parseClipboardButton.setForeground(Color.RED);
						return;
					}
					parseClipboardButton.setForeground(Color.BLACK);
				} catch (HeadlessException | UnsupportedFlavorException | IOException e1) {
					e1.printStackTrace();
				} catch (NumberFormatException e2) {
					parseClipboardButton.setForeground(Color.RED);
				}
			}
		});

		this.add(inputPanel);
		this.add(parseClipboardButton);
	}

	public Rectangle2D getDomain() {
		double x = Double.parseDouble(xi.getText());
		double y = Double.parseDouble(yi.getText());
		double w = Double.parseDouble(wi.getText());
		double h = Double.parseDouble(hi.getText());

		return new Rectangle2D.Double(x, y, w, h);
	}
}
