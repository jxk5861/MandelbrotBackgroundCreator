package karabin.mandelbrot.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import karabin.mandelbrot.drawing.DrawingManager;
import karabin.mandelbrot.drawing.DrawingMethod;
import karabin.mandelbrot.drawing.coloring.ColorGradient;
import karabin.mandelbrot.drawing.coloring.ColorStrategy;
import karabin.mandelbrot.drawing.coloring.MapMultiColorStrategy;
import karabin.mandelbrot.printing.ImagePrinter;

/**
 * The panel with the controls such as gradient controls, domain controls, and
 * number of iterations as well as some information.
 */
public class ControlsPanel extends JPanel {
	private static final long serialVersionUID = -8346672836124829397L;

	private ImagePanel panel;

	private GradientPanel gradientCreator;
	private JPanel chooseColor;

	private JLabel iterationsLabel;
	private JTextField iterationsField;

	private JLabel domainX;
	private JLabel domainY;
	private JLabel domainW;
	private JLabel domainH;

	private JButton domainButton;
	private JButton setGradient;

	private JList<DrawingMethod> list;
	private JButton printButton;

	private File file;

	private Map<DrawingMethod, ColorGradient> gradientMap;

	public ControlsPanel(int width, int height, ImagePanel panel, JLabel domainX, JLabel domainY, JLabel domainW,
			JLabel domainH) {
		gradientMap = new HashMap<>();

		this.panel = panel;

		List<DrawingMethod> drawingMethods = DrawingManager.INSTANCE.getDrawingMethods();
		for (int i = 0; i < drawingMethods.size(); i++) {
			ColorGradient gradient = new ColorGradient();
			gradient.add(0, Color.BLACK);
			gradient.add(1, Color.RED);
			gradientMap.put(drawingMethods.get(i), gradient);
		}

		this.chooseColor = new JPanel();
		this.chooseColor.setBackground(Color.red);
		this.chooseColor.setPreferredSize(new Dimension(20, 20));
		this.chooseColor.setBorder(BorderFactory.createLineBorder(Color.black));
		this.chooseColor.addMouseListener(new MouseListener() {
			boolean clicking = false;

			@Override
			public void mouseReleased(MouseEvent e) {
				if (clicking) {
					Color color = JColorChooser.showDialog(null, "Choose a color",
							Color.WHITE/* DrawingManager.INSTANCE.getSelected().getColor() */);
					if (color == null) {
						return;
					}
					ControlsPanel.this.chooseColor.setBackground(color);
					ControlsPanel.this.gradientCreator.setSelectedColor(color);
					ControlsPanel.this.gradientCreator.repaint();
					ColorStrategy coloring = MapMultiColorStrategy.of(ControlsPanel.this.gradientCreator.getGradient());
					DrawingManager.INSTANCE.getSelected().setColoring(coloring);
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
		this.iterationsField = new JTextField("255", 6);
		this.iterationsField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					int iterations = Integer.parseInt(iterationsField.getText());
					if (iterations <= 0) {
						throw new NumberFormatException(
								String.format("For input string \"%s\"", iterationsField.getText()));
					}
					DrawingManager.INSTANCE.getSelected().setIterations(iterations);
					ControlsPanel.this.panel.draw();
					ControlsPanel.this.iterationsField.setForeground(Color.black);
				} catch (NumberFormatException ee) {
//					ee.printStackTrace();
					ControlsPanel.this.iterationsField.setForeground(Color.red);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

		this.file = null;

		printButton = new JButton("Print");
		printButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ControlsPanel.this.file == null) {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					// must open due to bug in apple implementation...
					chooser.showOpenDialog(ControlsPanel.this.panel);
					ControlsPanel.this.file = chooser.getSelectedFile();

					if (ControlsPanel.this.file == null) {
						return;
					}

					ControlsPanel.this.file = new File(ControlsPanel.this.file, "MandelbrotImages");

					if (!ControlsPanel.this.file.isDirectory()) {
						ControlsPanel.this.file.mkdir();
					}
				}
				ImagePrinter.INSTANCE.printToPNG(file, 1920, 1080, panel.getDomain());
			}
		});

		DefaultListModel<DrawingMethod> model = new DefaultListModel<>();
		model.addAll(DrawingManager.INSTANCE.getDrawingMethods());
		list = new JList<>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				DrawingManager.INSTANCE.setSelected(ControlsPanel.this.list.getSelectedIndex());
				DrawingMethod selected = DrawingManager.INSTANCE.getSelected();

				if (ControlsPanel.this.gradientMap.containsKey(selected)) {
					ColorGradient gradient = ControlsPanel.this.gradientMap.get(selected);
					ControlsPanel.this.gradientCreator.setGradient(gradient);
					ControlsPanel.this.gradientCreator.setSelected(-1);
					ControlsPanel.this.gradientCreator.repaint();
				}

				ControlsPanel.this.panel.draw();
//TODO:				ControlsPanel.this.chooseColor.setBackground(selected.getColor());
				ControlsPanel.this.iterationsField.setText(Integer.toString(selected.getIterations()));
			}
		});

		this.gradientCreator = new GradientPanel(chooseColor, panel, gradientMap.get(drawingMethods.get(0)));

		this.domainX = domainX;
		this.domainY = domainY;
		this.domainW = domainW;
		this.domainH = domainH;

		this.domainButton = new JButton("Set Domain");
		this.domainButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DomainPanel panel = new DomainPanel(ControlsPanel.this.panel.getDomain());
				int option = JOptionPane.showInternalConfirmDialog(null, panel, "Domain Selection",
						JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					try {
						Rectangle2D domain = panel.getDomain();
						ControlsPanel.this.panel.domainSetRect(domain.getX(), domain.getY(), domain.getWidth(),
								domain.getHeight());
						ControlsPanel.this.panel.draw();
					} catch (NumberFormatException e2) {

					}

				}
			}
		});

		this.setGradient = new JButton("Set Gradient");
		this.setGradient.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SetGradientPanel panel = new SetGradientPanel(ControlsPanel.this.gradientCreator.getGradient());
				int option = JOptionPane.showInternalConfirmDialog(null, panel, "Gradient Loader",
						JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					ControlsPanel.this.gradientCreator.setGradient(panel.getGradient());
					ControlsPanel.this.gradientCreator.redraw();
					ControlsPanel.this.panel.draw();
				}
			}
		});

		JPanel domainPanel = new JPanel();
		domainPanel.setLayout(new BoxLayout(domainPanel, BoxLayout.Y_AXIS));
		domainPanel.add(this.domainX);
		domainPanel.add(this.domainY);
		domainPanel.add(this.domainW);
		domainPanel.add(this.domainH);

		this.add(gradientCreator);
		this.add(chooseColor);
		this.add(iterationsLabel);
		this.add(iterationsField);
		this.add(printButton);
		this.add(list);
		this.add(domainPanel);

		JPanel setPanel = new JPanel();
		setPanel.setLayout(new GridLayout(2, 1));
		setPanel.add(this.domainButton);
		setPanel.add(this.setGradient);

		this.add(setPanel);

		this.setPreferredSize(new Dimension(width, height));
	}
}
