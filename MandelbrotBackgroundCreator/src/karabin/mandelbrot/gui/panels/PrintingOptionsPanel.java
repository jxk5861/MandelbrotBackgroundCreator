package karabin.mandelbrot.gui.panels;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

public class PrintingOptionsPanel extends JPanel {
	private static final long serialVersionUID = 5409689966314053773L;

	private JList<PrintingOption> options;

	public PrintingOptionsPanel(int selectedIndex) {
		var model = new DefaultListModel<PrintingOption>();

		model.addElement(new PrintingOption("1920x1080", 1920, 1080));
		model.addElement(new PrintingOption("3840x2160", 3840, 2160));
		model.addElement(new PrintingOption("IPhone 13 Pro", 1170, 2532));

		options = new JList<>(model);
		options.setSelectedIndex(selectedIndex);

		this.add(options);
	}

	public int getSelectedIndex() {
		return options.getSelectedIndex();
	}

	public int getPrintWidth() {
		return options.getSelectedValue().getWidth();
	}

	public int getPrintHeight() {
		return options.getSelectedValue().getHeight();
	}
}

class PrintingOption {
	private String name;

	private int width;
	private int height;

	public PrintingOption(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}