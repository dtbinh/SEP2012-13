package Navigation;

import MapStructure.Map;
/**
 * Robot Manual Scanner, help executing manual scan operations
 * @author DaweiG
 *
 */
public class RobotManualScanner {
	private Map myMap;
	private RobotNavigator rn;
	/**
	 * Constructor
	 */
	public RobotManualScanner(Map m, RobotNavigator rn){
		this.setMyMap(m);
		this.setRn(rn);
	}
	/**
	 * Simple move to direction and scan.
	 * @param direction
	 */
	public void MoveAndScan(int direction){
		myMap.resetMarksandFlags();
		myMap.getCurrentPixel().setPostMark(direction);
		rn.update();
		try{
		rn.startNavigation();
		}catch(Exception e){
			System.out.println("ex");
		}
		
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
