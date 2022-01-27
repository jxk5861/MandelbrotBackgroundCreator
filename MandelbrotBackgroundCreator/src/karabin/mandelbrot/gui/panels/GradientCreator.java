package karabin.mandelbrot.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;

import karabin.mandelbrot.drawing.DrawingManager;
import karabin.mandelbrot.drawing.coloring.ColorGradient;
import karabin.mandelbrot.drawing.coloring.ColorStrategy;
import karabin.mandelbrot.drawing.coloring.MapMultiColorStrategy;

public class GradientCreator extends JComponent {

	private static final int MARGIN = 5;
	private static final int WIDTH = 100;
	private static final int HEIGHT = 20;

	private ColorGradient gradient;
	private int selected = -1;
	private boolean pressed = false;

	private ImagePanel panel;

	public void redraw() {
		ColorStrategy coloring = MapMultiColorStrategy.of(this.getGradient());
		DrawingManager.INSTANCE.getSelected().setColoring(coloring);
		this.panel.draw();

		repaint();
	}

	public GradientCreator(JPanel chooseColor, ImagePanel panel, ColorGradient gradient) {
		this.panel = panel;

		Dimension dim = new Dimension(WIDTH + 2 * MARGIN, HEIGHT);

		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
		this.setFocusable(true);
//		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.gradient = gradient;

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				GradientCreator.this.pressed = false;

				GradientCreator.this.redraw();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1) {
					return;
				}
				GradientCreator.this.pressed = true;
				selected = -1;

				final int width = 3;
				for (int i = 0; i < GradientCreator.this.gradient.size(); i++) {
					double x = GradientCreator.this.gradient.getPositions().get(i);
					int ix = (int) ((GradientCreator.this.getWidth() - 2 * width) * x) + width;

					if (e.getX() > ix - width && e.getX() < ix + width) {
						if (selected == i) {
							selected = -1;
						} else {
							selected = i;
							chooseColor.setBackground(GradientCreator.this.gradient.getColors().get(i));
							GradientCreator.this.setSelectedColor(GradientCreator.this.gradient.getColors().get(i));
							GradientCreator.this.repaint();
							GradientCreator.this.requestFocus();
						}
						repaint();
						break;
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
//				System.out.println("exited");
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
//				System.out.println("entered");
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					if (selected == -1) {
						double v = (double) (e.getX() - MARGIN) / (getWidth() - 2 * MARGIN);
						v = Math.max(0, v);
						v = Math.min(1, v);

						Color color = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
						if (color == null) {
							color = Color.WHITE;
						}

						GradientCreator.this.gradient.add(v, color);
						selected = GradientCreator.this.gradient.getColors().size() - 1;
						chooseColor.setBackground(color);
						GradientCreator.this.setSelectedColor(color);
						GradientCreator.this.requestFocus();
						GradientCreator.this.redraw();
					} else {
						Color color = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
						if (color == null) {
							color = Color.WHITE;
						}
						GradientCreator.this.gradient.getColors().set(selected, color);
						chooseColor.setBackground(GradientCreator.this.gradient.getColors().get(selected));
						GradientCreator.this.setSelectedColor(GradientCreator.this.gradient.getColors().get(selected));
						GradientCreator.this.requestFocus();
						GradientCreator.this.redraw();
					}
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (pressed && selected != -1) {
					// dumb
					// i think the only solution to this is making colorgradient reference
					// colorpoints and differentiate with those
					// 0-1
					// 5 w 5
					// x - margin / w - 2margin
					double v = (double) (e.getX() - MARGIN) / (getWidth() - 2 * MARGIN);
					v = Math.max(0, v);
					v = Math.min(1, v);
					GradientCreator.this.gradient.getPositions().set(selected, v);

					GradientCreator.this.redraw();
				}
			}
		});

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
					if (selected == -1 || GradientCreator.this.gradient.size() == 1) {
						return;
					}

					GradientCreator.this.gradient.remove(selected);
					selected = -1;

					GradientCreator.this.redraw();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}

	public void setSelectedColor(Color c) {
		if (this.selected != -1) {
			this.gradient.getColors().set(this.selected, c);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		BufferedImage image = new BufferedImage(GradientCreator.this.getWidth(), GradientCreator.this.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d2 = image.createGraphics();
		var unique = this.gradient.unique();
		// draw colors
		if (unique.size() > 1) {
			var paint = new LinearGradientPaint(0, 0, this.getWidth(), this.getHeight(), unique.getPositions(),
					unique.getColors());
			g2d.setPaint(paint);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2d2.setPaint(paint);
			g2d2.fillRect(0, 0, this.getWidth(), this.getHeight());
		} else if (unique.size() == 1) {
			g2d.setColor(unique.getColors()[0]);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2d2.setColor(unique.getColors()[0]);
			g2d2.fillRect(0, 0, this.getWidth(), this.getHeight());
		}

		// draw selectors
		final int width = 3;
		for (int i = 0; i < this.gradient.size(); i++) {
			double x = this.gradient.getPositions().get(i);
			int ix = (int) ((this.getWidth() - 1 - 2 * width) * x) + width;
			g2d.setColor(new Color(0x55FFFFFF - image.getRGB(ix, 2), true));

			g2d.fillRect(ix - width, 0, 2 * width, this.getHeight());

			if (selected == i) {
				g2d.setColor(new Color(0xAAFFFFFF - image.getRGB(ix, 2), true));
				g2d.drawRect(ix - width, 0, 2 * width, this.getHeight() - 1);
			}
		}
	}

	public ColorGradient getGradient() {
		return gradient;
	}

	public void setGradient(ColorGradient gradient) {
		this.gradient = gradient;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}
}
