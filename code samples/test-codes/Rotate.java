package com.mydomain;
import com.mydomain.Ultrasonic.Time;

import lejos.nxt.*;
import lejos.robotics.navigation.*;
import lejos.util.Timer;
import lejos.util.TimerListener;
public class Rotate {
	public static int distance;
    static TimerListener time;
	public static void main(String[] args){
  //DifferentialPilot pilot=new DifferentialPilot(2.2f,4.4f,Motor.A,Motor.C,false);
  DifferentialPilot pilot=new DifferentialPilot(2.2f,4.4f,Motor.A,Motor.C,false);
  pilot.setTravelSpeed(3);
  pilot.setRotateSpeed(40);
  pilot.travel(5);
 // pilot.rotate(-90);
  //TurnLeft(pilot);
  TurnRight(pilot);
	
   Button.waitForAnyPress();

}
	
	public static void TurnLeft(DifferentialPilot pilot){
		pilot.rotate(90);
	}
	public static void TurnRight(DifferentialPilot pilot){
		pilot.rotate(-90);
	}
	public void GoForward(DifferentialPilot pilot){
		pilot.travel(5);
	}
	public void GoBackward(DifferentialPilot pilot){
		pilot.travel(-5);
	}
	public void forward(){
		timer();
		//Move newm = new Move(0,100);
		Motor.A.forward();
		//Motor.C.forward();
			//Motor.A.stop();
			//Motor.C.stop();
			 //System.out.println("find block");
	    int speed=Motor.A.getSpeed();
	        speed--;
	        System.out.println(speed);
		
		while(distance!=0){
		distance--;
		System.out.println(distance);
		}
	    	if(distance==0){
	    		speed=0;
	    	}
		
		//Button.waitForAnyPress();
		
	}
	static class Time implements TimerListener{
		public void timedOut(){
			UltrasonicSensor ultra = new UltrasonicSensor(SensorPort.S3);
			distance=ultra.getDistance();
			if (distance <= 30){
				motorstop();
			}
		}

		private void motorstop() {
			Motor.A.stop();
			Motor.C.stop();
			// TODO Auto-generated method stub
			
		}

		
	}
	public static void timer(){
		time= new Time();
		Timer t = new Timer(100,time);
		t.start();
	}
	


}