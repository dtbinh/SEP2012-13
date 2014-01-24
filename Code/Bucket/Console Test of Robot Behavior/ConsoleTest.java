
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;



/**
 * Console Manual Controller
 * @author DaweiG
 * @version 1.0
 */
public class ConsoleTest {
	private static PCThread btc;
	private static ArrayBlockingQueue<int[]> q;
	static int wallFront, wallRight, wallLeft;
	static boolean ready = false;
	static int op = 0;
	public static void main(String [] args)
	{
		
		q = new ArrayBlockingQueue<int[]>(200);
		btc = new PCThread();
		q = btc.getQueueAccess();
		btc.start();
		boolean repeatInfinite = true;
		//Start control
		while(repeatInfinite){
			String line;
			Scanner in = new Scanner(System.in);
			line = in.nextLine();
			//System.out.println("Name :"+line);
			
			String info[] = line.split(" ");
			//decode command
			int[] command = null;
			if((command = commandTranslator(info)) !=null)
				try {
					q.put(command);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
	}
	}
	private static int[] commandTranslator(String[] input){
		if(input.length == 1){
			return commandTranslator(input[0]);
		}else if(input.length == 2){
			return commandTranslator(input[0], Integer.parseInt(input[1]));
		}else
			return null;
	}
	private static int[] commandTranslator(String commandName){
		int[] command = new int[1];
		if(commandName.equals("f"))
			command[0] = 1;
		else if(commandName.equals("b"))
			command[0] = 2;
		else if(commandName.equals("l"))
			command[0] = 3;
		else if(commandName.equals("r"))
			command[0] = 4;
		else if(commandName.equals("fp"))
			command[0] = 5;
		else if(commandName.equals("bp"))
			command[0] = 6;
		else if(commandName.equals("lp"))
			command[0] = 7;
		else if(commandName.equals("rp"))
			command[0] = 8;
		else if(commandName.equals("trp"))
			command[0] = 9;
		else if(commandName.equals("w"))
			command[0] = 10;
		else if(commandName.equals("g"))
			command[0] = 11;
		else if(commandName.equals("s"))
			command[0] = 12;
		else if(commandName.equals("ba"))
			command[0] = 13;
		else if(commandName.equals("ss"))
			System.err.println("Command: Set Speed error");
		else if(commandName.equals("q"))
			command[0] = 15;
		else{
			System.err.println("Can not understant this command, please enter:");
			System.err.println("f;b;l;r;fp;bp;lp;rp;trp;w;g;s;ba;q");
			command[0] = 0;
		}
		return command;
	}
	
	private static int[] commandTranslator(String commandName, int value){
		int[] command = new int[2];
		if(commandName.equals("f"))
			command[0] = 1;
		else if(commandName.equals("b"))
			command[0] = 2;
		else if(commandName.equals("l"))
			command[0] = 3;
		else if(commandName.equals("r"))
			command[0] = 4;
		else if(commandName.equals("ss"))
			command[0] = 14;
		else{
			System.err.println("Can not understant this command, please enter:");
			System.err.println("f;b;l;r;fp;bp;lp;rp;w;g;s;ba;q");
			command[0] = 0;
		}
		
		command[1] = value;
		return command;
	}
}
