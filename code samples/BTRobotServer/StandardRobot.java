import java.util.Queue;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;


public class StandardRobot {
	public int TRAVEL_SPEED = 6;
	public NXTRegulatedMotor leftMotor;	
	public NXTRegulatedMotor rightMotor;	
	public UltrasonicSensor  ultraSonicSensor;
	public DifferentialPilot pilot;
	public Queue<Integer> q;
	public int MIN_DIS;
	public StandardRobot() {
		MIN_DIS = 23;
		leftMotor = Motor.C;	
		rightMotor = Motor.A;
		ultraSonicSensor = new UltrasonicSensor(SensorPort.S1);
		pilot=new DifferentialPilot(2.25f,7.03f,Motor.A,Motor.C);
		pilot.setTravelSpeed(TRAVEL_SPEED);
		 q = new Queue<Integer>();
	}
}
