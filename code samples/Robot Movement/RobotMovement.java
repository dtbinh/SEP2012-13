import lejos.nxt.*;

import lejos.robotics.navigation.*;

public class RobotMovement {
	public static final NXTRegulatedMotor LeftMotor = Motor.A;	
	public static final NXTRegulatedMotor RightMotor = Motor.C;	
	public static final int rotatedegree = 360;
	public static int distance;
	public static void main(String[] args){
		DifferentialPilot pilot=new DifferentialPilot(2.2f,4.4f,Motor.A,Motor.C,false);
		pilot.setRotateSpeed(80);
		pilot.setTravelSpeed(4);
		
		pilot.travel(10);
		
		pilot.travel(-10);
		
		TurnLeft(pilot);
		
		TurnRight(pilot);
		
		Sound.beepSequence();
		
		Button.waitForAnyPress();

	}
	   
	public static void TurnLeft(DifferentialPilot pilot){
		LeftMotor.stop();
		RightMotor.rotate(rotatedegree);
	}
	public static void TurnRight(DifferentialPilot pilot){
		RightMotor.stop();
		LeftMotor.rotate(rotatedegree);
		//pilot.rotate(-90);
	}
	
}

