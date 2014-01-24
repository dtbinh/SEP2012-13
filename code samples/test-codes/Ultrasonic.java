package com.mydomain;
import lejos.nxt.*;
//import ;
import lejos.util.*;
import java.lang.*;
public class Ultrasonic{

	static public int distance;
	static TimerListener time;

public static void main(String[] args){
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
	
	Button.waitForAnyPress();
}
public static void motorstop() {
	Motor.A.stop();
	Motor.C.stop();
}
static class Time implements TimerListener{
	public void timedOut(){
		UltrasonicSensor ultra = new UltrasonicSensor(SensorPort.S3);
		distance=ultra.getDistance();
		if (distance <= 30){
			motorstop();
		}
	}

	
}
public static void timer(){
	time= new Time();
	Timer t = new Timer(100,time);
	t.start();
}
}

