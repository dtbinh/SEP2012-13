
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;

import lejos.nxt.Battery;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.DifferentialPilot;

public class BTRobotDriver{
	public static boolean noForward;
	public static boolean isForwarding;
	public static BTConnection btc;
	public static DataOutputStream dOut;
	public static DataInputStream dIn;
	public static void robotForward(DifferentialPilot pilot){
		if(!noForward){
			isForwarding = true;
			pilot.forward();
		}
	}
	public static void robotForward(DifferentialPilot pilot,int dis){
		if(!noForward){
			isForwarding = true;
			pilot.travel(dis, true);
			isForwarding = false;
		}
	}
	public static void robotStop(DifferentialPilot pilot){
		isForwarding = false;
		pilot.stop();
	}
	
	public static void main(String [] args) throws Exception 
	{
		noForward = false;
		isForwarding = false;
		StandardRobot me = new StandardRobot();
		RobotMonitor rm = new RobotMonitor(me,200);
		rm.start();
		LCD.drawString("Waiting for Connection", 0, 0);
		btc = Bluetooth.waitForConnection();
		dOut = btc.openDataOutputStream();
		dIn = btc.openDataInputStream();
		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		while (true) 
		{
			try {
				me.q.push(dIn.readInt());
				if(me.q.size()!=0){
					int receivedOperationCode = (int) me.q.pop();

					if(receivedOperationCode / 0x100 == 0)
						if(receivedOperationCode == 0)
							robotForward(me.pilot);
						else
							robotForward(me.pilot,receivedOperationCode);
					
					if(receivedOperationCode / 0x100 == 1)
						if(receivedOperationCode - 0x100 == 0)
							me.pilot.backward();
						else
							me.pilot.travel(receivedOperationCode%0x100 * -1, true);
					
					if(receivedOperationCode / 0x100 == 2)
						if(receivedOperationCode - 0x200 == 0)
							me.pilot.rotateLeft();
						else
							me.pilot.rotate(receivedOperationCode % 0x200);
					
					if(receivedOperationCode / 0x100 == 3)
						if(receivedOperationCode - 0x300 == 0)
							me.pilot.rotateRight();
						else
							me.pilot.rotate(receivedOperationCode % 0x300 * -1);
					
					if(receivedOperationCode == 0xFFF)
							robotStop(me.pilot);
					
					if(receivedOperationCode == 0x400){
						int dis = me.ultraSonicSensor.getDistance();
						if (dis < 0x100)
							receivedOperationCode += dis;
					}
						
					
					dOut.writeInt(receivedOperationCode);
					dOut.flush();
				}
			} catch (IOException e) {
				break;
			}
		}				
		dOut.close();
		dIn.close();
		btc.close();
	}
}
