package XML_STRUCTURE;

public class RobotManualScanner {
	/**
	 * To Be tested.
	 */
	private Map myMap;
	private RobotNavigator rn;
	public RobotManualScanner(Map m, RobotNavigator rn){
		this.setMyMap(m);
		this.setRn(rn);
	}
	public void MoveAndScan(int direction){
		myMap.resetMarksandFlags();
		myMap.getCurrentPixel().setPostMark(direction);
		rn.startNavigation();
		
	}
	/**
	 * @return the myMap
	 */
	public Map getMyMap() {
		return myMap;
	}

	/**
	 * @param myMap the myMap to set
	 */
	public void setMyMap(Map myMap) {
		this.myMap = myMap;
	}
	/**
	 * @return the rn
	 */
	public RobotNavigator getRn() {
		return rn;
	}
	/**
	 * @param rn the rn to set
	 */
	public void setRn(RobotNavigator rn) {
		this.rn = rn;
	}
}
