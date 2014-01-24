package MapStructure;

public class RobotStatus {
	private int robotDirection;
	private Pixel robotLocation;
	
	public RobotStatus(int robotDirection,Pixel robotLocation){
		this.setRobotDirection(robotDirection);
		this.setRobotLocation(robotLocation);
	}
	public int getRobotDirection() {
		return robotDirection;
	}

	public void setRobotDirection(int robotDirection) {
		this.robotDirection = robotDirection;
	}

	public Pixel getRobotLocation() {
		return robotLocation;
	}

	public void setRobotLocation(Pixel robotLocation) {
		this.robotLocation = robotLocation;
	}
	
	@SuppressWarnings("null")
	public RobotStatus clone(){
		RobotStatus clonedRobotStatus = null;
		clonedRobotStatus.setRobotDirection(robotDirection);
		clonedRobotStatus.setRobotLocation(robotLocation);
		return clonedRobotStatus;
	}
	
}
