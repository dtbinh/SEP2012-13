import java.io.IOException;
import java.lang.Thread;
import lejos.nxt.*;
public class RobotMonitor extends Thread {
    private static int delay;
    private static StandardRobot me;
    public RobotMonitor(StandardRobot me, int d) {
      this.setDaemon(true);
      this.me = me;
      delay = d;
      setPriority(NORM_PRIORITY);
}
    public void run() {
    	 while(true) {
    	  LCD.clear();
    	 // LCD.drawString("Color  = "+me.cs.getColorID(),0,0);
    	  float range = me.ultraSonicSensor.getRange();
    	  LCD.drawString("Ultra  = "+ range,  0,1);
    	  if(range <= me.MIN_DIS && BTRobotDriver.isForwarding){
    		  BTRobotDriver.robotStop(me.pilot);
    		  BTRobotDriver.noForward = true;
    		  if(BTRobotDriver.btc != null){
    			  try {
    				  BTRobotDriver.dOut.writeInt((int) (Operations.TRANSFER_CODE_SENSOR_1+range));
    				  BTRobotDriver.dOut.flush();
    			  } catch (IOException e) {
    				  e.printStackTrace();
				}
    		  }
 
    	  }else if(range > me.MIN_DIS){
    		  BTRobotDriver.noForward = false;
    	  }
    	 // LCD.drawString("Touch  = "+me.ts.isPressed(), 0,2);
    	  //LCD.drawString(me.pilot.getMovement().toString(), 0,2);
    	  try { this.sleep(delay); } catch (Exception e) { ; }
    	 } // end while
    	} // end run
 }