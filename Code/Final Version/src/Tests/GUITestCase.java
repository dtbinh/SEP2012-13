package Tests;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import GUI.*;
import org.junit.Test;

public class GUITestCase {
	LEGOGUI lego;
	@Test
	public void testDefaultButtonEnabled(){
		LEGOGUI lego = new LEGOGUI();
		//test addNew, loadMap initial statues
		assertTrue(lego.addNew.isEnabled());
		assertTrue(lego.loadMap.isEnabled());
		//test auto, manual,bluetoothConnection button initial statues
		assertFalse(lego.auto.isEnabled());
		assertFalse(lego.manual.isEnabled());
		assertFalse(lego.bluetoothConnection.isEnabled());
		
		//test 6 control button initial statues
		assertFalse(lego.up.isEnabled());
		assertFalse(lego.down.isEnabled());
		assertFalse(lego.left.isEnabled());
		assertFalse(lego.right.isEnabled());
		assertFalse(lego.turnLeft.isEnabled());
		assertFalse(lego.turnRight.isEnabled());
		//lego = null;
	}
	
	@Test //(expected = NullPointerException.class)
	public void testNewMapInitialedButtonEnabled(){
		
		// when a new map initialed 
		// test the statues of each button
		 lego = new LEGOGUI();
		
		
		// set addNew map clicked;
		// set new map panel confirm button clicked
		lego.addNew.doClick ();
		lego.nms.mi.Confirm.doClick();
		
		// addNew and loadMap disabled
		assertFalse(lego.loadMap.isEnabled());
		assertTrue(lego.nms.mi.Confirm.isEnabled());
		
		// bluetoothConnection button is enabled
		assertFalse(lego.auto.isEnabled());
		assertFalse(lego.manual.isEnabled());
		assertTrue(lego.bluetoothConnection.isEnabled());
		
		// new map panel is enabled
		assertTrue(lego.nms.mi.isEnabled());	
		lego = null;
		
	}
	@Test //(expected = NullPointerException.class)
	public void testBlueToothConnectedButtonEnabled(){
		lego = new LEGOGUI();
		lego.addNew.doClick ();
		lego.nms.mi.Confirm.doClick();
		//precondition tested through above
		// start connect robot, expected NullPointerException
		lego.bluetoothConnection.doClick();
		// auto and manual Mode enabled
		assertTrue(lego.auto.isEnabled());
		assertTrue(lego.manual.isEnabled());
		// 6 control button enabled
		assertTrue(lego.up.isEnabled());
		assertTrue(lego.down.isEnabled());
		assertTrue(lego.left.isEnabled());
		assertTrue(lego.right.isEnabled());
		assertTrue(lego.turnLeft.isEnabled());
		assertTrue(lego.turnRight.isEnabled());

		//if mode changed 
	lego.auto.requestFocus();
		new Thread(){
        	public void run(){
        		int i = 0;
        		while(i< 10){
        			lego.robot.keyPress(KeyEvent.VK_ENTER);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i++;
				}
        	}    	
        }.start();

		lego.auto.doClick();
		assertFalse(lego.auto.isEnabled());
		assertTrue(lego.manual.isEnabled());
		lego.manual.doClick();
		assertTrue(lego.auto.isEnabled());
		assertFalse(lego.manual.isEnabled());}

	
	@Test
	public void testSpeedSlider() {
		LEGOGUI lego = new LEGOGUI();
		
		//test speed slider bar reaction and relate label display
		assertEquals(String.valueOf(lego.slider.getValue()), lego.speedLabel.getText().trim());
		
		//test when speed slider is changed
		//if the label is change with it or not
		
		lego.slider.setValue(8);
		assertEquals(String.valueOf(lego.slider.getValue()), lego.speedLabel.getText().trim());
		
		// test when speed is changed, if the textField update the change
		int value = 5;
		String text = lego.text.getText();
		String str = "Speed set to be: " + String.valueOf(value);
		lego.slider.setValue(value);
		assertEquals(String.valueOf(lego.slider.getValue()), lego.speedLabel.getText().trim());
		assertEquals(text+"\n"+str,lego.text.getText());
	}

}