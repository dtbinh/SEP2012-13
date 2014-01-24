package com.mydomain;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class TouchTest {
  public static void main(String[] args) throws Exception {
    TouchSensor touch = new TouchSensor(SensorPort.S1);
    TouchSensor touch1 = new TouchSensor(SensorPort.S2);
    while (!touch.isPressed()&&!touch1.isPressed()) {
    	 Motor.A.forward();
    	 Motor.C.forward();
    }
    Motor.A.stop();
    Motor.C.stop();
    LCD.drawString("Warnning", 3, 4);
   //Motor.A.setSpeed(800);
   //Motor.C.setSpeed(800);
    Button.waitForAnyPress();
  }
}