package karabin.mandelbrot.gui.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import karabin.mandelbrot.drawing.coloring.ColorGradient;

public class SetGradientPanel extends JPanel {
	private JButton parseClipboard;
	private JLabel gradientLabel;
	private ColorGradient gradient;

	public SetGradientPanel(ColorGradient gradient) {
		this.gradient = gradient;

		this.parseClipboard = new JButton("Parse From Clipboard");
		this.parseClipboard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
							.getData(DataFlavor.stringFlavor);
					Pattern p = Pattern.compile("positions=\\[(\\d(.\\d)?(, )?)+\\]");
					Matcher m = p.matcher(data);

					List<Double> positions = new ArrayList<>();
					List<Color> colors = new ArrayList<>();
					;

					if (!m.find()) {
						parseClipboard.setForeground(Color.RED);
						return;
					}

					String s = m.group();
					s = s.substring(s.lastIndexOf('[') + 1, s.lastIndexOf(']'));
					for (String positionString : s.split(", ")) {
						Double position = Double.parseDouble(positionString);
						if (position < 0D || position > 1D) {
							parseClipboard.setForeground(Color.RED);
							return;
						}

						positions.add(position);
					}

					p = Pattern.compile("colors=\\[(java.awt.Color\\[r=\\d{1,3},g=\\d{1,3},b=\\d{1,3}\\](, )?)+\\]");
					m = p.matcher(data);

					if (!m.find()) {
						parseClipboard.setForeground(Color.RED);
						return;
					}
					s = m.group();

					Pattern rp = Pattern.compile("r=\\d{1,3}");
					Pattern gp = Pattern.compile("g=\\d{1,3}");
					Pattern bp = Pattern.compile("b=\\d{1,3}");

					Matcher rm = rp.matcher(s);
					Matcher gm = gp.matcher(s);
					Matcher bm = bp.matcher(s);

					while (rm.find() && gm.find() && bm.find()) {
						int red = Integer.parseInt(rm.group().substring(2));
						int green = Integer.parseInt(gm.group().substring(2));
						int blue = Integer.parseInt(bm.group().substring(2));

						Color color = new Color(red, green, blue);
						colors.add(color);
					}

					if (positions.size() == colors.size() && positions.size() > 1) {
						SetGradientPanel.this.gradient = new ColorGradient();

						for (int i = 0; i < positions.size(); i++) {
							SetGradientPanel.this.gradient.add(positions.get(i), colors.get(i));
						}
					}

					SetGradientPanel.this.gradientLabel.setText(SetGradientPanel.this.gradient.toString());
					parseClipboard.setForeground(Color.BLACK);
				} catch (HeadlessException | UnsupportedFlavorException | IOException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e2) {
					parseClipboard.setForeground(Color.RED);
				}
			}
		});

		this.gradientLabel = new JLabel(this.gradient.toString());

		this.setLayout(new GridLayout(2, 1));

		this.add(parseClipboard);
		this.add(gradientLabel);
	}

	public ColorGradient getGradient() {
		return gradient;
	}
}
