package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

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
	private int currxPos;
	private int curryPos;
	private int length;
	private int width;
	private int unexplorePixels;
	private ArrayList<Pixel> pixels = new ArrayList<Pixel>();
	
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
		this.setCurrxPos(startxPos);
		this.setCurryPos(startyPos);
		this.setUnexplorePixels(width * length); 
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
			int index1 = yPos * width + xPos;
			int index2 = yPos2 * width + xPos2;
			if(index1 > index2){
				int temp = index1;
				index1 = index2;
				index2 = temp;
			}
			for(int i = index1; i <= index2; i++)
				pixels.get(i).setValue(Integer.MAX_VALUE);
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
	 * @return the currxPos
	 */
	public int getCurrxPos() {
		return currxPos;
	}
	/**
	 * @param currxPos the currxPos to set
	 */
	public void setCurrxPos(int currxPos) {
		this.currxPos = currxPos;
	}
	/**
	 * @return the curryPos
	 */
	public int getCurryPos() {
		return curryPos;
	}
	/**
	 * @param curryPos the curryPos to set
	 */
	public void setCurryPos(int curryPos) {
		this.curryPos = curryPos;
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
	
}
