/**
 * 
 * Robot Class
 * @author DaweiG
 * @date 01.10.2012
 * @version 1.0
 * 
 */		

import java.util.Queue;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * robot class defines robot key features.
 * @author DaweiG
 *
 */
public class Robot {
	public static boolean noForward;
	public static boolean isForwarding;
	public int TRAVEL_SPEED = 3;
	
	//Define motor and sensors.
	
	public NXTRegulatedMotor leftMotor;	
	public NXTRegulatedMotor rightMotor;	
	public NXTRegulatedMotor sensorMotor;
	public UltrasonicSensor  ultraSonicSensor;
	public LightSensor lightSensor;
	public DifferentialPilot pilot;
	
	//Light sensor color code.
	
	public static final int WHITE_COLOR = 51;
	public static final int BLACK_COLOR = 35;
	public Queue<Integer> q;
	public int MIN_DIS;
	
	public Robot() {
		
		noForward = false;
		isForwarding = false;
		MIN_DIS = 23;
		leftMotor = Motor.C;	
		rightMotor = Motor.A;
		sensorMotor = Motor.B;
		sensorMotor.setSpeed(100);
		ultraSonicSensor = new UltrasonicSensor(SensorPort.S1);
		lightSensor = new LightSensor(SensorPort.S2);
		pilot=new DifferentialPilot(2.25f,4.726f,Motor.A,Motor.C);
		pilot.setTravelSpeed(TRAVEL_SPEED);
		pilot.setRotateSpeed(30);
		q = new Queue<Integer>();
		
	}
	/**
	 * Robot travels backward.
	 */
	public void robotBackward(){
			pilot.stop();
			pilot.backward();
	}
	/**
	 * robot travels backward with fixed distance.
	 * @param dis
	 */
	public void robotBackward(int dis){
			pilot.travel(dis, true);
	}
	/**
	 * Robot travels forward.
	 */
	public void robotForward(){
		if(!noForward){
			isForwarding = true;
			pilot.forward();
		}
	}
	/**
	 * Robot travels forward with fixed distance.
	 * @param dis
	 */
	public void robotForward(int dis){
		if(!noForward){
			isForwarding = true;
			pilot.travel(dis, true);
			isForwarding = false;
		}
	}
	/**
	 * Robot stops
	 */
	public void robotStop(){
		pilot.stop();
		isForwarding = false;
	}
	
	/**
	 * Robot turns left.
	 */
	public void robotRotateLeft() {
		pilot.rotateLeft();
	}
	
	
	/**
	 * Robot turns with fixed angle.
	 * @param angle
	 */
	public void robotRotate(int angle) {
		pilot.rotate(angle);
	}
	
	/**
	 * Robot turns right.
	 */
	public void robotRotateRight() {
		pilot.rotateRight();
		
	}
	
	/**
	 * Robot goes forward pixel by pixel.
	 */
	public void autoForward(){
		pilot.travel(1, true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Robot goes backward pixel by pixel.
	 */
    public void autoBackward(){
    	pilot.travel(-1, true);
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
	/**
	 * Robot goes turns right and the light sensor at the same location when it is done.
	 * 
	 * 1: go forward
	 * 2: turn
	 * 
	 * 
	 */
    public void autoTurnRight(){
    	try {
    		pilot.travel(2, true);
    		Thread.sleep(1000);
    		pilot.rotate(-90);
    		Thread.sleep(1000);
    		pilot.travel(-1, true);
    		Thread.sleep(1000);
    		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
	/**
	 * Robot goes turns left and the light sensor at the same location when it is done.
	 * 
	 * 1: go forward
	 * 2: turn
	 */
    public void autoTurnLeft(){
    	try {
    		pilot.travel(2, true);
    		Thread.sleep(1000);
    		pilot.rotate(90);
    		Thread.sleep(1000);
    		pilot.travel(-1, true);
    		Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Robot turn around and the light sensor at the next pixel;
     */
    public void autoTurnAround(){
    	try {
    		pilot.travel(2, true);
    		Thread.sleep(1000);
    		pilot.rotate(180);
    		Thread.sleep(1000);
    		pilot.travel(-1, true);
    		Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    

    /**
     * Robot scan three directions with ultraSonicSensor.
     * @return a array that contain three values for forward, left, and right wall information.
     */
    public float[] scanWall(){
    	try {
    		float[] wallInfo = new float[3];
        	wallInfo[0] = ultraSonicSensor.getRange();
       	  	sensorMotor.rotate(-90);
       	  	Thread.sleep(1000);
       	  	wallInfo[1] = ultraSonicSensor.getRange();	
    	   	sensorMotor.rotate(180);
    	   	Thread.sleep(1000);
    	   	wallInfo[2] = ultraSonicSensor.getRange();
       	 	sensorMotor.rotate(-90);
       		Thread.sleep(1000);
       	 	
       	 	//Largest value will be cap at 255. 
       	 	if(wallInfo[0] >= 255)wallInfo[0] = 255; 
       		if(wallInfo[1] >= 255)wallInfo[1] = 255; 
       		if(wallInfo[2] >= 255)wallInfo[2] = 255; 
    		
    		return wallInfo;
			
		} catch (InterruptedException e) {
			return null;
		}
    	
    }
    
    /**
     * Robot read from light sensor.
     * @return 1: black 0: white -1: undetermined color
     */
    public int scanGround(){
    	int value = lightSensor.getLightValue();
    	if(value >= WHITE_COLOR){
    		return 0;
    	}else if (value <= BLACK_COLOR){
    		return 1;
    	}else
    		return -1;
    }
}
