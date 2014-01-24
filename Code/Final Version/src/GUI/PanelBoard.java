package GUI;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.*;
import MapStructure.Map;
import org.w3c.dom.events.MouseEvent;
import XMLDocuments.XMLReaderWriter;

@SuppressWarnings("serial")
public class PanelBoard extends JPanel {
    Map map;
	public int[] grid_width_pos; // the positions of horizontal grids
	public int[] grid_height_pos; // the positions of vertical grids

	

	public int map_width;
	public int map_height;
	public int pixel_size;
	private int mapTLX = 0, mapTLY = 0;
	private int TopLeftX, TopLeftY;
	private int mouseMoveX = 0, mouseMoveY = 0;
	private int mouseStartX, mouseStartY;
	private Rectangle selectionRectangle;
	// no go zone 
	private int[][] NoGo = null;
    private int[] tempRec = null;
    private int NoGoCounter,NoGoCir;
    private boolean NoGoSetting =false;
    private Shape s = null;
    // map representation
     
	private static final int UNEXPOLORED = 0;
	private static final int EXPLORERD = 1 ;
	private static final int HIDDENWALL = 2;
	private static final int NOGOZONE = Integer.MAX_VALUE;
	private static final int WALL = 999;
	private static final int BUFFERZONE = 500;
	      
	public PanelBoard(int map_width, int map_height, int pixel_size) {
		// round to the closest integer which is divisible by the pixel_size
		this.map_width = ((int) Math.round(map_width / (double) pixel_size) * pixel_size);
		this.map_height = ((int) Math.round(map_height / (double) pixel_size) * pixel_size);
		this.pixel_size = pixel_size;
		selectionRectangle = new Rectangle(0, 0, 0, 0);

		// update horizontal positions of the grids
		grid_width_pos = new int[this.map_width / pixel_size + 1];
		// update vertical positions of the grids
		grid_height_pos = new int[this.map_height / pixel_size + 1];

		for (int i = 0, WIDTH = mapTLX; i < grid_width_pos.length; i++, WIDTH += pixel_size) {
			grid_width_pos[i] = WIDTH;
		}

		for (int i = 0, HEIGHT = mapTLY; i < grid_height_pos.length; i++, HEIGHT += pixel_size) {
			grid_height_pos[i] = HEIGHT;
		}
		
		this.addMouseWheelListener(listener);
		this.addMouseListener(ML2);
		this.addMouseMotionListener(MML2);
		
	}
	/**
	 * 
	 * @param new_pixel_size update the new size of the pixel
	 * @param XShift is the distance of horizontal shifting 
	 * @param YShift is the distance of vertical shifting 
	 */
	public void drag_set_pixel_size(int new_pixel_size, int XShift, int YShift) {
		this.pixel_size = new_pixel_size;
		for (int i = 0, WIDTH = mapTLX; i < grid_width_pos.length; i++, WIDTH += new_pixel_size) {
			grid_width_pos[i] = WIDTH + XShift;
		}
		for (int i = 0, DEPTH = mapTLY; i < grid_height_pos.length; i++, DEPTH += new_pixel_size) {
			grid_height_pos[i] = DEPTH + YShift;
		}
	}
	
	/**
	 * 
	 * @param new_pixel_size is the new size of pixel after mouse wheel move
	 */
	public void wheel_set_pixel_size(int new_pixel_size) {
		mapTLX = mapTLX - grid_height_pos.length * (new_pixel_size - pixel_size) / 2;
		mapTLY = mapTLY - grid_height_pos.length * (new_pixel_size - pixel_size) / 2;
		this.pixel_size = new_pixel_size;
		for (int i = 0, WIDTH = mapTLX; i < grid_width_pos.length; i++, WIDTH += new_pixel_size) {
			grid_width_pos[i] = WIDTH;
		}
		for (int i = 0, DEPTH = mapTLY; i < grid_height_pos.length; i++, DEPTH += new_pixel_size) {
			grid_height_pos[i] = DEPTH;
		}	
	}
	
	/**
	 * 
	 * @return the width of map
	 */
	public int getMap_width() {
		return map_width;
	}
	
	/**
	 * 
	 * @return the height of map
	 */
	public int getMap_height() {
		return map_height;
	}
	
	/**
	 * 
	 * @return size of one pixel
	 */
	public int getPixel_size() {
		return pixel_size;
	}

	/**
	 * 
	 * @param mapObj is the map data structure that will be displayed on this board
	 */
	public void setMapObject(Map mapObj) {
		this.map = mapObj;
	}

	
	/**
	 * 
	 * @param x_pos is the horizontal coordinate of the target explored position
	 * @param y_pos is the vertical coordinate of the target explored position
	 */
	public void setExplored(int x_pos, int y_pos) {
		map.findPixel(x_pos,y_pos).setValue(1);
		repaint();
	}
	/**
	 * 
	 * @param x_pos is the horizontal coordinate of the target NoGoZone position
	 * @param y_pos is the vertical coordinate of the target NoGoZone position
	 */
	public void setNoGoZone(int x_pos, int y_pos) {
		map.findPixel(x_pos,y_pos).setValue(2);
		repaint();
	}
	/**
	 * 
	 * @param x_pos is the horizontal coordinate of the target HiddenWall position
	 * @param y_pos is the vertical coordinate of the target HiddenWall position
	 */
	public void setHiddenWall(int x_pos, int y_pos) {
		map.findPixel(x_pos,y_pos).setValue(3);
		repaint();
	}
	/**
	 * 
	 * @param x_pos is the horizontal coordinate of the target Buffered position
	 * @param y_pos is the vertical coordinate of the target Buffered position
	 */
	public void setBuffered(int x_pos, int y_pos) {
		map.findPixel(x_pos,y_pos).setValue(4);
		repaint();
	}
	
	
/************************ paint Map ************************/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// map frame
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.blue);
		for (int i = 0; i < grid_height_pos.length; i++) 
			g.drawLine(grid_width_pos[0], grid_height_pos[i], grid_width_pos[grid_width_pos.length - 1], grid_height_pos[i]);
		for (int i = 0; i < grid_width_pos.length; i++) 
			g.drawLine(grid_width_pos[i], grid_height_pos[0], grid_width_pos[i], grid_height_pos[grid_height_pos.length - 1]);
		
		// map pixel representation
		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getLength(); j++) {
				int value = map.findPixel(i,j).getValue();
				if(map.findPixel(i,j) == map.getCurrentPixel()){
					g.setColor(Color.green);
				}else if (value == UNEXPOLORED){					
					g.setColor(Color.black);
				} else if (value == EXPLORERD){
					g.setColor(Color.white);
				} else if (value== HIDDENWALL ) {
					g.setColor(Color.gray);
				} else if (value== NOGOZONE ) {
					g.setColor(Color.red);
				} else if (value== WALL ) {
					g.setColor(new Color(139,69,19));
			    }else if (value== BUFFERZONE ) {
						g.setColor(Color.yellow);
				}
				g.fillRect(grid_width_pos[i] + 1, grid_height_pos[j] + 1, pixel_size - 1, pixel_size - 1);
			}
		}
		
		//SET No go zone
		/**  
		* display current no go zone setting process
		* @author Li Shikai
		* 
		*/
   		if(NoGoSetting){
   			g.setColor(Color.CYAN);
   			g.fillOval(NoGo[NoGoCounter-1][0]-4,NoGo[NoGoCounter-1][1]-4,8,8);
   			if(NoGoCir ==0){
   				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
   				g.drawOval(NoGo[NoGoCounter-1][0]-6,NoGo[NoGoCounter-1][1]-6,12,12);
   				NoGoCir =1;
   			}else{
   				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
   				g.drawOval(NoGo[NoGoCounter-1][0]-8,NoGo[NoGoCounter-1][1]-8,16,16);
   				NoGoCir =0;
   			}
   			for(int i = 0; i < NoGoCounter-1;i++){  				
   				g.drawLine(NoGo[i][0],NoGo[i][1],NoGo[i+1][0],NoGo[i+1][1]);
   				if(NoGoCounter == 4){
   					g.drawLine(NoGo[0][0],NoGo[0][1],NoGo[3][0],NoGo[3][1]);
   					NoGoZoneClose();
   					g.setColor(Color.red);
   					g2d.fill(s);
   				}
   			}
   		}
		 
   		//g2d.rotate(routate,mapX+map.getCurrentPixel().getxPos()*mapAlter-12+Robot.getWidth(null)/2,mapY+ map.getCurrentPixel().getyPos()*mapAlter-12+ Robot.getHeight(null)/2);
        //BufferedImage RobotImage = new BufferedImage(Robot,(int)(Robot.getWidth(null)*mapProportion),(int)(Robot.getHeight(null)*mapProportion), BufferedImage.TYPE_INT_RGB);  
        //g.drawImage(Robot, mapX+map.getCurrentPixel().getxPos()*mapAlter-12,mapY+ map.getCurrentPixel().getyPos()*mapAlter-12, null); 		
	}
	
	/************************ set no go zone function  *********************/
	public void NoGoZoneInit() {
	   	 int response = JOptionPane.showConfirmDialog(null, "Click 4 Points on the Map to set up No-go Zone", "No Go Zone Setting", JOptionPane.YES_NO_OPTION);
	   	 if(response==0){ 
	   		 this.addMouseListener(NoGoZoneListener);
	        	 NoGo = new int[4][2];
	        	 NoGoCounter = 0;
	        	 NoGoCir = 0;
	            System.out.println("Start Setting No-go Zone:");
	        }else{
	        	System.out.println("No-go Zone Setting Canceled.");
	        }
	   	
	   }    
	/**  
	*
	* calculate the shape generated by 4 point clicked on screen to set value as nogozone
	*/
	public void NoGoZoneClose(){
		Map prevMap=(Map) map.clone();
		LEGOGUI.redoMapStack.clear();
		tempRec = new int[4];
		int[] tempX = new int[4];
		int[] tempY = new int[4];
		sort();
		for(int i =0; i < 4; i++){
			tempX[i] = NoGo[i][0];
			tempY[i] = NoGo[i][1];
		}
		s = new Polygon(tempX,tempY,4); 
		for(int i = tempRec[0]; i < tempRec[2]; i ++ )
			for(int j = tempRec[1] ; j < tempRec[3] ; j ++)
				if(s.contains(i,j))    	
		        	map.findPixel((int)((i - mapTLX)/pixel_size), (int)((j - mapTLY)/pixel_size)).setNoGoZone();
		          		    	
		LEGOGUI.undoMapStack.push(prevMap);
		NoGoSetting =false;
		NoGoCounter = 0;
		NoGo = null;
		repaint();
		this.removeMouseListener(NoGoZoneListener);
	}
	/**  
	* sort 4 point to make a max size rectangle
	* 
	*/
	public void sort(){
		tempRec[0] = NoGo[0][0];
		tempRec[1] = NoGo[0][1];
		tempRec[2] = NoGo[0][0];
		tempRec[3] = NoGo[0][1];	
		for(int i =0;i<3;i++){
			tempRec[0] = Math.min(tempRec[0],NoGo[i+1][0]);// x min
			tempRec[1] = Math.min(tempRec[1],NoGo[i+1][1]);// y min
			tempRec[2] = Math.max(tempRec[2],NoGo[i+1][0]);// x max
			tempRec[3] = Math.max(tempRec[3],NoGo[i+1][1]);	// y max
		} 			
	}
	/**  
	* check if click not in the map
	* @param x		click position x 
	* @param y		click position y 
	* 
	*/
	public boolean outOfMap(int x, int y){
    	if(x < (int) (mapTLX*pixel_size/25) || x > (int)((mapTLX + map_width)*pixel_size/25) || y < (int)(mapTLY*pixel_size/25) || y > (int)((mapTLY + map_height)*pixel_size/25))
    		return true;
    	else
    		return false;
    }
	/************************ Listener *********************/
    MouseWheelListener listener = new MouseWheelListener() {
		public void mouseWheelMoved(MouseWheelEvent e) {
			if(!NoGoSetting){
			int count = e.getWheelRotation();
			if (getPixel_size() > 1)
				wheel_set_pixel_size(getPixel_size() - count);
			else if (count < 0)
				wheel_set_pixel_size(getPixel_size() - count);
			repaint();
			}
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
			if(!NoGoSetting){
			mapTLX = mapTLX + (e.getX() - mouseStartX);
			mapTLY = mapTLY + (e.getY() - mouseStartY);
			
			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage("src/GUI/images/hand.png"), new Point(0, 0), "Slef"));
			}
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
			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage("src/GUI/images/hand.png"), new Point(0, 0), "Slef"));
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
			if(!NoGoSetting){
			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage("src/GUI/images/drag.png"), new Point(0, 0), "Slef"));
			mouseMoveX = arg0.getX() - mouseStartX;
			mouseMoveY = arg0.getY() - mouseStartY;
			
			drag_set_pixel_size(pixel_size, mouseMoveX, mouseMoveY);
			
			repaint();	
			}
		}
	};
	/************************ set no go zone listener *********************/
	MouseListener NoGoZoneListener  = new MouseListener(){      
		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
			int x = ((java.awt.event.MouseEvent) e).getX();
        	int y = ((java.awt.event.MouseEvent) e).getY();
        	if(!outOfMap(x,y)){
        		if(NoGoCounter < 4){
        			NoGoSetting = true;
        			NoGo[NoGoCounter][0] = x;
        			NoGo[NoGoCounter][1] = y;
        			NoGoCounter ++;
        			System.out.println("Point " + NoGoCounter + ": >>>>  X: " + NoGo[NoGoCounter-1][0] + " Y: " + NoGo[NoGoCounter-1][1]);
                	repaint();
        		}
        	}
        	else{
        		int response = JOptionPane.showConfirmDialog(null, "Point setted out of map bound, please reset", "Emergency Save", JOptionPane.YES_NO_OPTION);
        	}
		}
		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
		}
		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
		}
		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {						
		}
		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {					
		}
		};	
	}
	