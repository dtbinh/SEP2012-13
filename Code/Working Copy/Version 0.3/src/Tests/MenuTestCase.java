package Tests;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import GUI.*;
import org.junit.Test;



public class MenuTestCase {

	@Test
	public void testDefaultMenuEnabled(){
		
		LEGOGUI lego = new LEGOGUI();
		
		//test FIleMenu load ,save statues
		assertTrue(lego.file_new.isEnabled());
		assertTrue(lego.file_open.isEnabled());
		assertFalse(lego.file_save.isEnabled());
		assertTrue(lego.file_close.isEnabled());
		
		//test OptionMenu initial statues
		assertFalse(lego.start_navigation.isEnabled());
		assertFalse(lego.manual_scan.isEnabled());
		
		assertFalse(lego.connect_connectRob.isEnabled());
		assertFalse(lego.connect_disconRob.isEnabled());
		
		assertFalse(lego.file_setNoGoZone.isEnabled());
		assertFalse(lego.file_UndoNoGoZone.isEnabled());
		assertFalse(lego.file_RedoNoGoZone.isEnabled());
		assertFalse(lego.abordMisson.isEnabled());
			
	}
	@Test
	public void testNewMapInitialedMenuEnabled(){
		LEGOGUI lego = new LEGOGUI();
		// set addNew map clicked;
		// set new map panel confirm button clicked
		lego.addNew.doClick ();
		lego.nms.mi.Confirm.doClick();
		
		//test FIleMenu load ,save statues
		assertTrue(lego.file_save.isEnabled());// file is enabled
				
		//test OptionMenu initial statues
		assertFalse(lego.start_navigation.isEnabled());
		assertFalse(lego.manual_scan.isEnabled());
				
		assertTrue(lego.connect_connectRob.isEnabled()); // connect_robot is enabled
		assertFalse(lego.connect_disconRob.isEnabled());
				
		assertTrue(lego.file_setNoGoZone.isEnabled());  //set no go zone is enabled
		assertFalse(lego.file_UndoNoGoZone.isEnabled());
		assertFalse(lego.file_RedoNoGoZone.isEnabled());
		assertFalse(lego.abordMisson.isEnabled());
		
	}
	
	@Test//(expected = NullPointerException.class , timeout=1000)
	public void testConnectedMenuEnabled() {
		final LEGOGUI lego = new LEGOGUI();
		// set addNew map clicked;
		// set new map panel confirm button clicked
		lego.addNew.doClick ();
		lego.nms.mi.Confirm.doClick();		
		lego.bluetoothConnection.doClick();
		
		//test FIleMenu load ,save statues
		assertTrue(lego.file_save.isEnabled());// file is enabled
				
		//test OptionMenu initial statues
		assertFalse(lego.start_navigation.isEnabled());
		assertFalse(lego.manual_scan.isEnabled());
				
		assertFalse(lego.connect_connectRob.isEnabled()); // connect_robot is enabled
		assertTrue(lego.connect_disconRob.isEnabled());
				
		assertTrue(lego.file_setNoGoZone.isEnabled());  //set no go zone is enabled
		assertFalse(lego.file_UndoNoGoZone.isEnabled());
		assertFalse(lego.file_RedoNoGoZone.isEnabled());
		assertFalse(lego.abordMisson.isEnabled());
		
		// click auto button
		// thread automatic click enter, to ensure select right mode
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
				
		assertTrue(lego.start_navigation.isEnabled());
		assertTrue(lego.manual_scan.isEnabled());
		assertTrue(lego.abordMisson.isEnabled());
		// click manual button
		lego.manual.doClick();
		assertFalse(lego.start_navigation.isEnabled());
		assertFalse(lego.manual_scan.isEnabled());
		assertFalse(lego.abordMisson.isEnabled());
	}
	@Test
	public void testTestField() {
		LEGOGUI lego = new LEGOGUI();
		
		// test append function for JTextFiled
		String text = lego.text.getText();
		String append = "append";
		lego.append(append);
		assertEquals(text+"\n"+append,lego.text.getText());
					
		// test setText function for JTextFiled
		String setText = "setText";
		lego.setText(setText);
		assertEquals(setText,lego.text.getText());
		
		// test TextAreaPrintStream
		String Stream = "Stream";
		text = lego.text.getText();
		System.out.println(Stream);
		assertEquals(text+Stream,lego.text.getText().trim());
	}
	
	@Test
	public void testRobotCurrentCoordinateValue() {
		LEGOGUI lego = new LEGOGUI();
		lego.addNew.doClick ();
		lego.nms.mi.Confirm.doClick();
		
		// first test default robot coordinate x and y value
		// and the label relate display on GUI
		int posX, posY;
		posX = lego.map.getCurrentPixel().getxPos();
		posY = lego.map.getCurrentPixel().getyPos();
		assertEquals(0,posX);
		assertEquals(0,posX);
		assertEquals(0,Integer.parseInt(lego.xValue.getText().trim()));
		assertEquals(0,Integer.parseInt(lego.yValue.getText().trim()));
		
		// then test when robot current position has been changed
		// and the label relate display on GUI
		lego.map.setCurrentPixel(lego.map.findPixel(5,8));
		posX = lego.map.getCurrentPixel().getxPos();
		posY = lego.map.getCurrentPixel().getyPos();
		assertEquals(5,posX);
		assertEquals(8,posY);
		lego.updateCoordinateValue();
		assertEquals(5,Integer.parseInt(lego.xValue.getText().trim()));
		assertEquals(8,Integer.parseInt(lego.yValue.getText().trim()));
	}
	
		
}
