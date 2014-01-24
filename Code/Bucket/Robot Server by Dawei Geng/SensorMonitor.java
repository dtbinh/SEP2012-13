/**
 * 
 * Front Sensor Thread
 * @author DaweiG
 * @date 01.10.2012
 * @version 1.0
 * 
 */		

import java.io.IOException;
import java.lang.Thread;
import lejos.nxt.*;
/**
 * a Thread that monitor the ultrasonic sensor when moving to avoid near walls. 
 * @author DaweiG
 *
 */
public class SensorMonitor extends Thread {
	volatile boolean stop = false;
    private static int delay;
    private static Robot me;
    public SensorMonitor(Robot me, int d) {
      this.setDaemon(true);
      SensorMonitor.me = me;
      delay = d;
      setPriority(MAX_PRIORITY);
}
    public void run() {
    	
    	 while(!stop) {
	    		 
	    	  float range = me.ultraSonicSensor.getRange();
	    	  LCD.drawString("Ultra  = "+ range,  0,1);
	    	  if(range <= me.MIN_DIS && Robot.isForwarding){
	    		  me.robotStop();
	    		  Robot.noForward = true;
    		  
	    		  if(BTRobotDriver.btc != null){
	    			  try {
	    				  BTRobotDriver.dOut.writeInt((int) (Operations.TRANSFER_CODE_SENSOR_1+range));
	    				  BTRobotDriver.dOut.flush();
	    			  } catch (IOException e) {
	    				  e.printStackTrace();
					}
    		  }
 
    	  }else if(range > me.MIN_DIS){
    		  Robot.noForward = false;
    	  }
    	  try { Thread.sleep(delay); } catch (Exception e) { ; }
    	 } // end while
    	} // end run
 }