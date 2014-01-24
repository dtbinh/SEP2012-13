package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class MapBoard extends JComponent {
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	
//	int x,y;
//	Color color_id;
//	
//	MapBoard(int x, int y, Color c) {
//		this.color_id = c;
//		this.x = x;
//		this.y = y;
//	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);

	}

}
