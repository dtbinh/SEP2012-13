package com.mydomain;
import lejos.nxt.*;
public class RotateTest2 {
public static void main(String[] args){
	Motor.A.rotate(360);
	Motor.C.rotate(0);
	Motor.A.forward();
	Motor.C.forward();
	Button.waitForAnyPress();
}
}
