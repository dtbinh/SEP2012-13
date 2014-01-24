package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class ButtonTest
{
	public static void main (String[] args)
	throws Exception
	{
		Button.ENTER.waitForPressAndRelease();		
		LCD.drawString("Finished", 3, 4);
		Thread.sleep(2000);
	}
}