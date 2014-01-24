package MapStructure;

/**
 * RobotStatus object.
 * @author DaweiG
 * @version 1.0.15.10.2012
 * 
 *	Robot direction: 	1 = north;
 *						2 = east;
 *						3 = south;
 *						4 = west;
 */
public class RobotStatus {
	private int robotDirection;
	private Pixel robotLocation;
	
	/**
	 * Constructor
	 * @param robotDirection
	 * @param robotLocation
	 */
	public RobotStatus(int robotDirection,Pixel robotLocation){
		this.setRobotDirection(robotDirection);
		this.setRobotLocation(robotLocation);
	}
	/**
	 * @return robot's direction
	 */
	public int getRobotDirection() {
		return robotDirection;
	}

	/**
	 * 
	 * @param robotdirection
	 */
	public void setRobotDirection(int robotDirection) {
		this.robotDirection = robotDirection;
	}

	/**
	 * @return robotlocation
	 */
	public Pixel getRobotLocation() {
		return robotLocation;
	}

	/**
	 * set robot's location
	 * @param robotLocation
	 */
	public void setRobotLocation(Pixel robotLocation) {
		this.robotLocation = robotLocation;
	}
	
	/**
	 * Clone robot.
	 */
	@SuppressWarnings("null")
	public RobotStatus clone(){
		RobotStatus clonedRobotStatus = null;
		clonedRobotStatus.setRobotDirection(robotDirection);
		clonedRobotStatus.setRobotLocation(robotLocation);
		return clonedRobotStatus;
	}
	
}
