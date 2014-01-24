package GUI;
import java.awt.*;
/** Description of MiniMap	 
*
* @author Li Shikai
* @version 3.0 build 18/10/2012.
*/
import java.awt.geom.Area;
import javax.swing.*;
import MapStructure.Map;

public class MiniMap extends JFrame{
	static Map map;
	int width,height,tablePixel;

	 /** 
     *  @param map get and set current map object
  	 */
	public MiniMap(Map map){
		this.map = map;	
		initialMiniMap();
	}
	 /** 
     *  initial current mini map, base on the map object
  	 */
	private void initialMiniMap() {
		 width = map.getWidth()*25;
		 height = map.getLength()*25;
		if(width>=height){
			height = height * 250/width;
			tablePixel = 25 * 250/width;
			width = 250;
			
		}else{
			width = width * 250/height;
			tablePixel = 25 * 250/height;
			height = 250;
		}
		// border + white blank
		this.setSize(width+15+10,height+35+10);
		this.setTitle("Mini Map >>> ID:" +  map.getId());
		this.setLayout(null);
		MiniPanel miniPanel = new MiniPanel(width,height,tablePixel);
		this.add(miniPanel);
		this.setResizable(false);
		this.setFocusable(false);
		this.setFocusableWindowState(false);
		this.setAlwaysOnTop(false);
		this.setVisible(true);
		toBack();
	}

	 class MiniPanel extends JPanel{
		private int tablePixel;
		private int pixel_size;
		private int count = 0;
		private int mapTLX = 0, mapTLY = 0;
		private int TopLeftX, TopLeftY;
		private static final int UNEXPOLORED = 0;
		private static final int EXPLORERD = 1 ;
		private static final int HIDDENWALL = 2;
		private static final int NOGOZONE = Integer.MAX_VALUE;
		private static final int WALL = 999;
		private static final int BUFFERZONE = 500;
		 /** 
		  * initial			 	Mini Map Panel
	     * @param	width	 	width of panel
	     * @param	height 		height of panel
	     * @param	tablePixel	length of side of each grid of pixel
	  	 */
		public MiniPanel(int width,int height,int tablePixel){
			width = (int)(width/tablePixel)*tablePixel+1;
			height = (int)(height/tablePixel)*tablePixel+1;
			this.setBounds(5,5,width,height);
			this.setBackground(Color.blue);
			this.tablePixel = tablePixel;
			pixel_size = tablePixel;
			repaintThread();
		}
		/****************** mini map paint method****************************/
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g.setColor(Color.red);
			for(int i = 0; i < map.getWidth(); i ++){
				for(int j = 0;j < map.getLength();j++){
					g.drawLine(i*tablePixel,1, i*tablePixel,map.getLength()*tablePixel);
					g.drawLine(1,j*tablePixel,map.getWidth()*tablePixel,j*tablePixel);
					
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
					g.fillRect(i*tablePixel,j*tablePixel,tablePixel-1,tablePixel-1);
				}
			}
			// current screen frame
			
			{
			 if(count == 0){
				 g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
				 count = 1;
			 }
			 else{
				 g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
				 count = 0;
			 }
			 g.setColor(Color.white);
			 //System.out.println("width = " + width+ " pixel_size =" + pixel_size +  " tablePixel =" + tablePixel + " " + width*pixel_size/tablePixel +  "  " + height*pixel_size/tablePixel);
			// g.fillRect(mapTLX,mapTLY,width*pixel_size/tablePixel-1,height*pixel_size/tablePixel-1);
			 Area a1 = new Area(new Rectangle(mapTLX,mapTLY,width*pixel_size/tablePixel-9,height*pixel_size/tablePixel-9)); 
			 Area a2 = new Area(new Rectangle(mapTLX+4,mapTLY+4,width*pixel_size/tablePixel-17,height*pixel_size/tablePixel-17)); 
			 a1.subtract(a2);
			 g2d.fill(a1);
			}
		} 
		
		
		
		/*public void setMapX(int mapTLX){
			this.mapTLX = mapTLX;
			repaint();
		}
		public void setMapY(int mapTLY){
			this.mapTLY = mapTLY;
			repaint();
		}
		public void setPixelSize(int size){
			this.pixel_size = pixel_size + size;
			repaint();
		}*/
		 /** 
	     *  repaint method, update mini map every 350ms
	  	 */
		public void repaintThread(){
			new Thread(){
            	public void run(){
            		try {
            			while(true){
            				repaint();  
            				Thread.sleep(350);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            	}    	
            }.start();
		}
	}
 }
