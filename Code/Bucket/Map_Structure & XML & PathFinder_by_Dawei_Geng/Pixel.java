
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
 *
 */
public class Pixel {
	private int xPos,yPos;
	private Pixel n,s,e,w;
	private int	prevMark;
	private int postMark;
	private int value;
	private int pathFindingFlag;
	private Pixel parent;
	private int g;
	private double f;
	
	
	//Static value
	static final int UNEXPLORED=0;
	static final int NOFEATURED=1;
	static final int HIDDENWALL=3;
	static final int WALL=999;
	static final int NOGOZONE=Integer.MAX_VALUE;
	
	static final int NORTH=1;
	static final int EAST=2;
	static final int SOUTH=3;
	static final int WEST=4;
	
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
	public Pixel(int xPos, int yPos, int value){
		this.setxPos(xPos);
		this.setyPos(yPos);
		this.setN(null);
		this.setS(null);
		this.setE(null);
		this.setW(null);
		this.setValue(value);
		this.setPathFindingFlag(0);
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

	public boolean hasNextPixel() {
		return(getPostMark() == 1 || getPostMark() == 2 || getPostMark() == 3 || getPostMark() == 4);
	}
	
	
	public void setParent(Pixel parent) {
		this.parent = parent;
	}
	
	public Pixel getParent() {
		return parent;
	}

	public String toString(){
		return "Position: "+ "("+xPos+" ,"+yPos+")"+"\n"+
				"Value: "+value+"\n"+
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
