import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

@SuppressWarnings("serial")
public class PanelBoard extends JPanel {
//	public static final int BOARD_WIDTH = 720;
//	public static final int BOARD_DEPTH = 540;

	public int[] grid_width_pos; // the positions of horizontal grids
	public int[] grid_height_pos; // the positions of vertical grids

	public int[][] mapMat = null;

	public int map_width;
	public int map_height;
	public int pixel_size;
	private int mapTLX = 0, mapTLY = 0;
	private int TopLeftX, TopLeftY;
	private int mouseMoveX = 0, mouseMoveY = 0;
	private int mouseStartX, mouseStartY;
	private Rectangle selectionRectangle;
	
	MouseWheelListener listener = new MouseWheelListener() {
		public void mouseWheelMoved(MouseWheelEvent e) {
			int count = e.getWheelRotation();
			if (getPixel_size() > 1)
				set_pixel_size(getPixel_size() - count, 0, 0);
			else if (count < 0)
				set_pixel_size(getPixel_size() - count, 0, 0);
			repaint();
		}
	};
	
	MouseListener ML = new MouseListener() {
		
		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
			selectionRectangle = new Rectangle(0, 0, 0, 0);
			repaint();				
		}
		
		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			TopLeftX = e.getX();
			TopLeftY = e.getY();				
		}
		
		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
		}
		
		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
		}
		
		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
//			mapTLX = e.getX();
//			mapTLY = e.getY();
//			selectionRectangle = new Rectangle(TopLeftX, TopLeftY, mapTLX, mapTLY);				
		}
	};
	
	MouseMotionListener MML = new MouseMotionListener() {
		@Override
		public void mouseMoved(java.awt.event.MouseEvent e) {
		}
		@Override
		public void mouseDragged(java.awt.event.MouseEvent arg0) {
			int TLX = TopLeftX < arg0.getX() ? TopLeftX : arg0.getX();
			int TLY = TopLeftY < arg0.getY() ? TopLeftY : arg0.getY();
			int SRW = (TopLeftX > arg0.getX() ? TopLeftX : arg0.getX()) - TLX;
			int SRH = (TopLeftY > arg0.getY() ? TopLeftY : arg0.getY()) - TLY;
			selectionRectangle = new Rectangle(TLX, TLY, SRW, SRH);
			repaint();			
		}
	};
	
	
	MouseListener ML2 = new MouseListener() {
		
		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
			mapTLX = mapTLX + (e.getX() - mouseStartX);
			mapTLY = mapTLY + (e.getY() - mouseStartY);
			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage("hand.png"), new Point(0, 0), "Slef"));
		}
		
		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			mouseStartX = e.getX();
			mouseStartY = e.getY();
		}
		
		@Override
		public void mouseExited(java.awt.event.MouseEvent arg0) {
		}
		
		@Override
		public void mouseEntered(java.awt.event.MouseEvent arg0) {
			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage("hand.png"), new Point(0, 0), "Slef"));
		}
		
		@Override
		public void mouseClicked(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	MouseMotionListener MML2 = new MouseMotionListener() {
		
		@Override
		public void mouseMoved(java.awt.event.MouseEvent arg0) {
			
		}
		
		@Override
		public void mouseDragged(java.awt.event.MouseEvent arg0) {
			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage("drag.png"), new Point(0, 0), "Slef"));
			mouseMoveX = arg0.getX() - mouseStartX;
			mouseMoveY = arg0.getY() - mouseStartY;
			
			set_pixel_size(pixel_size, mouseMoveX, mouseMoveY);
			
			repaint();			
		}
	};



	public PanelBoard(int map_width, int map_height, int pixel_size) {
		// round to the closest integer which is divisible by the pixel_size
		this.map_width = ((int) Math.round(map_width / (double) pixel_size) * pixel_size);
		this.map_height = ((int) Math.round(map_height / (double) pixel_size) * pixel_size);
		this.pixel_size = pixel_size;
		selectionRectangle = new Rectangle(0, 0, 0, 0);

		// horizontal positions of the grids
		grid_width_pos = new int[this.map_width / pixel_size + 1];
		// vertical positions of the grids
		grid_height_pos = new int[this.map_height / pixel_size + 1];

		mapMat = new int[map_width / pixel_size][map_height / pixel_size];

		for (int i = 0, WIDTH = mapTLX; i < grid_width_pos.length; i++, WIDTH += pixel_size) {
			grid_width_pos[i] = WIDTH;
		}

		for (int i = 0, HEIGHT = mapTLY; i < grid_height_pos.length; i++, HEIGHT += pixel_size) {
			grid_height_pos[i] = HEIGHT;
		}
		

//		this.setCursor(new Cursor(1));
		
//		this.addMouseWheelListener(listener);
//		this.addMouseListener(ML);
//		this.addMouseMotionListener(MML);
		this.addMouseWheelListener(listener);
		this.addMouseListener(ML2);
		this.addMouseMotionListener(MML2);
		
	}

	public void set_pixel_size(int new_pixel_size, int XShift, int YShift) {
		this.pixel_size = new_pixel_size;
		
		for (int i = 0, WIDTH = mapTLX; i < grid_width_pos.length; i++, WIDTH += new_pixel_size) {
			grid_width_pos[i] = WIDTH + XShift;
		}
		
		for (int i = 0, DEPTH = mapTLY; i < grid_height_pos.length; i++, DEPTH += new_pixel_size) {
			grid_height_pos[i] = DEPTH + YShift;
		}
		
	}
	
	
	public int getMap_width() {
		return map_width;
	}
	
	public int getMap_height() {
		return map_height;
	}

	public int getPixel_size() {
		return pixel_size;
	}

	public void setMapMat(int[][] mapMat) {
		this.mapMat = mapMat;
	}

	public int[][] getMapMat() {
		return mapMat;
	}

	
	//map travel
	public void setExplored(int x_pos, int y_pos) {
		
		mapMat[x_pos][y_pos] = 1;
		repaint();
	}

	public void setNoGoZone(int x_pos, int y_pos) {
		mapMat[x_pos][y_pos] = 2;
		repaint();
	}

	public void setHiddenWall(int x_pos, int y_pos) {
		mapMat[x_pos][y_pos] = 3;
		repaint();
	}
	
	public void setBuffered(int x_pos, int y_pos) {
		mapMat[x_pos][y_pos] = 4;
		repaint();
	}
	
	

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// draw horizontal lines
		for (int i = 0; i < grid_height_pos.length; i++) {
			g.setColor(Color.black);
			g.drawLine(grid_width_pos[0], grid_height_pos[i], grid_width_pos[grid_width_pos.length - 1], grid_height_pos[i]);
		}

		// draw vertical lines
		for (int i = 0; i < grid_width_pos.length; i++) {
			g.setColor(Color.BLACK);
			g.drawLine(grid_width_pos[i], grid_height_pos[0], grid_width_pos[i], grid_height_pos[grid_height_pos.length - 1]);
		}
		
		for (int i = 0; i < mapMat.length; i++) {
			for (int j = 0; j < mapMat[0].length; j++) {
				if (mapMat[i][j] == 0) {
					g.setColor(Color.black);
					g.fillRect(i * pixel_size + 1, j * pixel_size + 1, pixel_size - 1, pixel_size - 1);
				} else if (mapMat[i][j] == 1) {
					g.setColor(Color.white);
					g.fillRect(i * pixel_size + 1, j * pixel_size + 1, pixel_size - 1, pixel_size - 1);
				} else if (mapMat[i][j] == 2) {
					g.setColor(Color.red);
					g.fillRect(i * pixel_size + 1, j * pixel_size + 1, pixel_size - 1, pixel_size - 1);
				} else if (mapMat[i][j] == 3) {
					g.setColor(Color.pink);
					g.fillRect(i * pixel_size + 1, j * pixel_size + 1, pixel_size - 1, pixel_size - 1);
				} else if (mapMat[i][j] == 4) {
					g.setColor(Color.yellow);
					g.fillRect(i * pixel_size + 1, j * pixel_size + 1, pixel_size - 1, pixel_size - 1);
				}
				
			}
		}
		
		g.setColor(Color.BLUE);
		g.drawRect((int) selectionRectangle.getX(),
				(int) selectionRectangle.getY(),
				(int) selectionRectangle.getWidth(),
				(int) selectionRectangle.getHeight());
		g.setColor(new Color(0, 0, 255, 100));
		g.fillRect((int) selectionRectangle.getX() + 1,
				(int) selectionRectangle.getY() + 1,
				(int) selectionRectangle.getWidth() - 1,
				(int) selectionRectangle.getHeight() - 1);
	}

}