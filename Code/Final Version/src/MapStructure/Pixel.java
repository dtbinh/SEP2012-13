package MapStructure;


/**
 * Map data structure: Pixel
 * @author DaweiG
 * @date 25.09.2012
 * @version 0.3
 * 
 * Added PathFinder.java 
 * 
 */

/**
 * Pixel object class
 * @author DaweiG
 * @version 1.0.07.10.2012
 */
public class Pixel {
	/**
	 * Basic fields
	 */
	private int xPos,yPos;
	private Map map;
	private Pixel n,s,e,w;
	/**
	 * Fields help path finding 
	 */
	private double f;
	private int	prevMark;
	private int g;
	private int pathFindingFlag;
	private int postMark;
	private int value;
	private Pixel parent;
	
	/**
	 * Static values
	 */
	public static final int UNEXPLORED=0;
	public static final int NOFEATURED=1;
	public static final int HIDDENWALL=2;
	public static final int BUFFERZONE = 500;
	public static final int WALL=999;
	public static final int NOGOZONE=Integer.MAX_VALUE;
	public static final int BUFFER_ZONE_WIDTH = 4;
	public static final int NORTH=1;
	public static final int EAST=2;
	public static final int SOUTH=3;
	public static final int WEST=4;
	
	/**
	 * Constructor one
	 */
	public Pixel(){
		this.setxPos(0);
		this.setyPos(0);
		this.setN(null);
		this.setS(null);
		this.setE(null);
		this.setW(null);
		this.setValue(0);
		this.setPathFindingFlag(0);
	}
	
	/**
	 * Constructor two: Main Constructor
	 * @param xPos
	 * @param yPos
	 * @param value
	 */
	public Pixel(int xPos, int yPos, int value, Map map){
		this.setxPos(xPos);
		this.setyPos(yPos);
		this.setN(null);
		this.setS(null);
		this.setE(null);
		this.setW(null);
		this.setValue(value);
		this.setPathFindingFlag(0);
		this.setMap(map);
	}
	
	/**
	 * Constructor two: OmniConstructor 
	 * @param xPos
	 * @param yPos
	 * @param n
	 * @param s
	 * @param e
	 * @param w
	 * @param value
	 */
	public Pixel(int xPos, int yPos, Pixel n, Pixel s, Pixel e, Pixel w, int value){
		this.setxPos(xPos);
		this.setyPos(yPos);
		this.setN(n);
		this.setS(s);
		this.setE(e);
		this.setW(w);
		this.setValue(value);
		this.setPathFindingFlag(0);
	}

	/**
	 * Set this pixel with value WALL, and set pixels around as BUFFERZONE.
	 */
	public void setWall(){
		if(this.getValue() != WALL)
			this.setValue(WALL);
		this.setBufferZone();
	}
	
	/**
	 * set current pixel with value NOGOZONE, and set pixels around as BUFFERZONE.
	 */
	public void setNoGoZone(){
		if(this.getValue() != NOGOZONE)
			this.setValue(NOGOZONE);
		this.setBufferZone();
	}
	private void setBufferZone() {
		for(int i = this.xPos - BUFFER_ZONE_WIDTH; i <= (this.xPos + BUFFER_ZONE_WIDTH);i++ ){
			for(int j = this.yPos - BUFFER_ZONE_WIDTH; j <= (this.yPos + BUFFER_ZONE_WIDTH); j++){
				Pixel temp = null;
				if( i >= 0 && i < this.getMap().getWidth() && j >= 0 && j < this.getMap().getLength()){
					temp = this.getMap().findPixel(i, j);
				if(temp != null)
					if(temp.getValue() != WALL && temp.getValue() != Integer.MAX_VALUE)
						temp.setValue(BUFFERZONE);
				}
					
			}
		}
		
	}

	
	/**
	 * @return map
	 */
	public Map getMap() {
		return map;
	}
	/**
	 * @param map
	 */
	public void setMap(Map map) {
		this.map = map;
	}
	
	/**
	 * @return the e
	 */
	public Pixel getE() {
		return e;
	}
	/**
	 * @param e the e to set
	 */
	public void setE(Pixel e) {
		this.e = e;
	}
	/**
	 * @return the n
	 */
	public Pixel getN() {
		return n;
	}
	/**
	 * @param n the n to set
	 */
	public void setN(Pixel n) {
		this.n = n;
	}
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	/**
	 * @return the w
	 */
	public Pixel getW() {
		return w;
	}
	/**
	 * @param w the w to set
	 */
	public void setW(Pixel w) {
		this.w = w;
	}
	/**
	 * @return the s
	 */
	public Pixel getS() {
		return s;
	}
	/**
	 * @param s the s to set
	 */
	public void setS(Pixel s) {
		this.s = s;
	}
	/**
	 * @return the yPos
	 */
	public int getyPos() {
		return yPos;
	}
	/**
	 * @param yPos the yPos to set
	 */
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	/**
	 * @return the xPos
	 */
	public int getxPos() {
		return xPos;
	}
	/**
	 * @param xPos the xPos to set
	 */
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	/**
	 * @return the prevMark
	 */
	public int getPrevMark() {
		return prevMark;
	}
	/**
	 * @param prevMark the prevMark to set
	 */
	public void setPrevMark(int prevMark) {
		this.prevMark = prevMark;
	}
	/**
	 * @return the postMark
	 */
	public int getPostMark() {
		return postMark;
	}
	/**
	 * @param postMark the postMark to set
	 */
	public void setPostMark(int postMark) {
		this.postMark = postMark;
	}
	/**
	 * @return the pathFindingFlag
	 */
	public int getPathFindingFlag() {
		return pathFindingFlag;
	}
	/**
	 * @param pathFindingFlag the pathFindingFlag to set
	 */
	public void setPathFindingFlag(int pathFindingFlag) {
		this.pathFindingFlag = pathFindingFlag;
	}

	/**
	 * Used by the pathfinder.
	 * @return true if current pixel has next pixel to travel to
	 */
	public boolean hasNextPixel() {
		return(getPostMark() == 1 || getPostMark() == 2 || getPostMark() == 3 || getPostMark() == 4);
	}
	
	/**
	 * @author Khoi
	 * @param parent
	 */
	public void setParent(Pixel parent) {
		this.parent = parent;
	}
	
	/**
	 * @author Khoi
	 * @return parent pixel
	 */
	public Pixel getParent() {
		return parent;
	}


	public String toString(){
		return "Position: "+ "("+xPos+" ,"+yPos+")"+
				"Value: "+value+" "+
				"PostMark: "+ postMark;
	}

	/**
	 * @return the g
	 */
	public int getG() {
		return g;
	}

	/**
	 * @param g the g to set
	 */
	public void setG(int g) {
		this.g = g;
	}

	/**
	 * @return the f
	 */
	public double getF() {
		return f;
	}

	/**
	 * @param f the f to set
	 */
	public void setF(double f) {
		this.f = f;
	}
	
}
