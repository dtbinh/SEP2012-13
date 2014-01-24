/**
 * Map data structure version: 0.1
 * @author DaweiG
 * @date 25.09.2012
 * @version 0.3
 * 
 * Added PathFinder.java 
 * 
 */
import java.lang.Thread;
import lejos.nxt.*;
import lejos.nxt.Battery;
public class BatteryMonitor extends Thread {
    private static int delay;
    private static Robot me;
    public BatteryMonitor(Robot me, int d) {
      this.setDaemon(true);
      BatteryMonitor.me = me;
      delay = d;
      setPriority(MIN_PRIORITY);
}
    public void run() {
    	 while(true) {
    	  float battery = Battery.getVoltageMilliVolt();
    	  if(battery > 8330)
    		  battery = 100;
    	  else
    		  battery = battery/8330 *100;
    	  int persentageBattery = (int)battery;
    	  LCD.drawString("Battery  = "+ persentageBattery + "%",  0,2);
    	  try { Thread.sleep(delay); } catch (Exception e) { ; }
    	 } // end while
    	} // end run
 }