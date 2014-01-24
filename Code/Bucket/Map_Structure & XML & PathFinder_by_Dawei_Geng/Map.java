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
	private ArrayList<Pixel> pixels = new ArrayList<Pixel>();
	
	//Static value
	static final int UNEXPLORED=0;
	static final int NOFEATURED=1;
	static final int HIDDENWALL=3;
	static final int WALL=999;
	static final int NOGOZONE=Integer.MAX_VALUE;
	
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
	public Map(int id, int length, int width, int startxPos, int startyPos){
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
				Pixel onePixel = new Pixel(j,i,0);
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
	
	
}