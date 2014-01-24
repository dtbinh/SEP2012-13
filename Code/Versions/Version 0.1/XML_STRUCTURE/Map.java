package XML_STRUCTURE;

import java.awt.event.KeyEvent;
/**
 * Map data structure version: 0.1
 * @author DaweiG
 * @date 25.09.2012
 * @version 0.3
 * 
 * Added PathFinder.java 
 * 
 */
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Map object.
 * @author DaweiG
 *
 */
public class Map {
	private int id;
	private int startxPos;
	private int startyPos;
	private Pixel currentPixel;
	private Pixel startPixel;
	private Pixel goal;
	private int length;
	private int width;
	private int unexplorePixels;
	private double routate; 
	private RobotStatus rs;
	private ArrayList<Pixel> pixels = new ArrayList<Pixel>();
	
	//Static value
	private final int east = 2;
    private final int south = 3;
    private final int west = 4;
    private final int north = 1;
    public int direction = south;
	static final int UNEXPLORED=0;
	static final int NOFEATURED=1;
	static final int HIDDENWALL=2;
	static final int WALL=999;
	static final int NOGOZONE=Integer.MAX_VALUE;
	static final int BUFFERZONE=500;
	/**
	 *  Constructor one 
	 */
	public Map(){
		setPixels(null);
	}
	
	/**
	 * Constructor two: Main Constructor
	 * @param id: Map's ID
	 * @param length: Map's length.
	 * @param width: Map's width.
	 * @param startxPos: Starting point x position.
	 * @param startyPos: Starting point y position.
	 */
	public Map(int id, int length, int width, int startxPos, int startyPos, int robotDirection){
		this.setId(id);
		this.setLength(length);
		this.setWidth(width);
		this.setStartxPos(startxPos);
		this.setStartyPos(startyPos); 
		this.setUnexplorePixels(width * length); 
		this.setGoal(null);
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		for(int i = 0; i < length; i++){
			for(int j = 0; j < width; j++){
				Pixel onePixel = new Pixel(j,i,0,this);
				pixels.add(onePixel);
			}
		}
		this.setPixels(pixels);
		for(Pixel p: pixels){
			p.setE(findPixel(p.getxPos()+1,p.getyPos()));
			p.setS(findPixel(p.getxPos(),p.getyPos()+1));
			p.setW(findPixel(p.getxPos()-1,p.getyPos()));
			p.setN(findPixel(p.getxPos(),p.getyPos()-1));
		}
		this.setStartPixel(findPixel(startxPos,startyPos));
		this.setCurrentPixel(getStartPixel());
		RobotStatus rs = new RobotStatus(robotDirection,this.getCurrentPixel());
		this.setRs(rs);
		System.out.println("MapId: " + id + " Length:"+ length + " Width:"+ width+ " StartXPos:"+ startxPos + " StartYPos:"+startyPos );
	}
	/**
	 * 
	 * @param xPos
	 * @param yPos
	 * @return Pixel object which at that position
	 */
	public Pixel findPixel(int xPos, int yPos) {
		if(xPos >= 0 && yPos >= 0 && xPos < width && yPos < length)
			return this.pixels.get(yPos * width + xPos);
		return null;
	}

	/**
	 * Constructor three
	 * @param id
	 * @param length
	 * @param width
	 * @param pixels
	 */
	public Map(int id, int length, int width, ArrayList<Pixel> pixels){
		this.setId(id);
		this.setLength(length);
		this.setWidth(width);
		this.setPixels(pixels);
	}
	
	
	public RobotStatus getRs() {
		return rs;
	}

	public void setRs() {
		this.rs = rs;
	} 
	
	public void setRs(RobotStatus rs) {
		this.rs = rs;
	} 
	
	
	/**
	 * @return the pixels
	 */
	public ArrayList<Pixel> getPixels() {
		return pixels;
	}
	
	/**
	 * Set the No-go zone with two coordinates 
	 * @param xPos
	 * @param yPos
	 * @param xPos2
	 * @param yPos2
	 */
	public void setNoGoZone(int xPos, int yPos, int xPos2, int yPos2) {
		if(xPos >= 0 && yPos >= 0 && xPos < width && yPos < length &&
		   xPos2 >= 0 && yPos2 >= 0 && xPos2 < width && yPos2 < length){
			for (int i = xPos; i <=xPos2; i++) {
				for (int j = yPos; j <= yPos2; j++) {
					findPixel(i, j).setValue(NOGOZONE);
				}
			}
		}
	}
	
	public void setNoGoZoneCircle(int xCenter, int yCenter, int radius){
		for(int Y = yCenter-radius; Y<=(yCenter+radius);Y++){
			for(int X =xCenter-radius;X<=(xCenter+radius);X++){
				
				
				if((X-xCenter)*(X-xCenter)+(Y-yCenter)*(Y-yCenter)<=(radius*radius)){
					findPixel(X, Y).setValue(NOGOZONE);
				}
			}
		}
		
	}
	
	
	
	/**
	 * @param pixels the pixels to set
	 */
	public void setPixels(ArrayList<Pixel> pixels) {
		this.pixels = pixels;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the startxPos
	 */
	public int getStartxPos() {
		return startxPos;
	}
	/**
	 * @param startxPos the startxPos to set
	 */
	public void setStartxPos(int startxPos) {
		this.startxPos = startxPos;
	}
	/**
	 * @return the startyPos
	 */
	public int getStartyPos() {
		return startyPos;
	}
	/**
	 * @param startyPos the startyPos to set
	 */
	public void setStartyPos(int startyPos) {
		this.startyPos = startyPos;
	}

	/**
	 * @return the unexplorePixels
	 */
	public int getUnexplorePixels() {
		return unexplorePixels;
	}
	/**
	 * @param unexplorePixels the unexplorePixels to set
	 */
	public void setUnexplorePixels(int unexplorePixels) {
		this.unexplorePixels = unexplorePixels;
	}
	
	/**
	 * @return 1 if successes
	 */
	public int resetPathFindingFlag() {
		for(Pixel p: pixels){
			p.setPathFindingFlag(0);
		}
		return 1;
	}

	/**
	 * @return the currentPixel
	 */
	public Pixel getCurrentPixel() {
		return currentPixel;
	}

	/**
	 * @param currentPixel the currentPixel to set
	 */
	public void setCurrentPixel(Pixel currentPixel) {
		this.currentPixel = currentPixel;
		this.getRs().setRobotLocation(this.getCurrentPixel());
	}

	/**
	 * @return the startPixel
	 */
	public Pixel getStartPixel() {
		return startPixel;
	}

	/**
	 * @param startPixel the startPixel to set
	 */
	public void setStartPixel(Pixel startPixel) {
		this.startPixel = startPixel;
	}

	/**
	 * @return the goal
	 */
	public Pixel getGoal() {
		return goal;
	}

	/**
	 * @param goal the goal to set
	 */
	public void setGoal(Pixel goal) {
		this.goal = goal;
	}
	public void setRoutate(double routate){
        this.routate= routate;
    }
    public double getRoutate(){
        return routate;
    }
    public boolean outOfBounds(Pixel pixel){
    	boolean out = true;
    	if(pixel == null){
    		out = false;
    		JOptionPane.showMessageDialog( null,"Warning out of Bounds!");
    	}else if(pixel.getValue()!=0 && pixel.getValue()!=1){
    		out = false;
    	}
    	return out;
    }
    public void moveInstruction(int key){
        if (key==KeyEvent.VK_S) {
          if(direction == south){
        	  if(outOfBounds(getCurrentPixel().getS()))
        		  setCurrentPixel(getCurrentPixel().getS());
        	  getCurrentPixel().setValue(NOFEATURED);
          }else{
              direction = south;
              setRoutate(0);
         }
         }else if (key==KeyEvent.VK_W) {
          if(direction == north){
        	  if(outOfBounds(getCurrentPixel().getN()))
        		  setCurrentPixel(getCurrentPixel().getN());
        	  getCurrentPixel().setValue(NOFEATURED);
           }else{
               setRoutate(Math.PI);    
               direction = north;
               
           }
         }else if (key==KeyEvent.VK_D) {
         if(direction == east){
        	 if(outOfBounds(getCurrentPixel().getE()))
        		 setCurrentPixel(getCurrentPixel().getE());
        	 getCurrentPixel().setValue(NOFEATURED);
           }else{      
              setRoutate(-Math.PI/2); 
              direction = east;
           }
         }else if (key==KeyEvent.VK_A) {
           if(direction == west){   
        	   if(outOfBounds(getCurrentPixel().getW()))
        		   setCurrentPixel(getCurrentPixel().getW());
        	   getCurrentPixel().setValue(NOFEATURED);
          }else{   
           setRoutate(Math.PI/2); 
           direction = west;
           }   
        }
   }

	public void resetMarksandFlags() {
		for(Pixel p : this.getPixels()){
			p.setPathFindingFlag(0);
			p.setPrevMark(0);
			p.setPostMark(0);
		}
		
	}


	
}