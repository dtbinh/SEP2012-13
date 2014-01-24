import javax.swing.JFrame;

public class Driver {

	public static void main(String[] args) throws InterruptedException {

		int a = 850, b = 600;
		GUI mainFrame = new GUI(a, b);
		int[][] m = mainFrame.getPanel().getMapMat();
		
		
		mainFrame.explore(mainFrame.getPanel());

//		Thread.sleep(100);
//		mainFrame.getPanel().setNoGoZone(3, 13);
//		Thread.sleep(100);
//		mainFrame.getPanel().setHiddenWall(5, 8);
//		Thread.sleep(100);
//		mainFrame.getPanel().setBuffered(7, 13);
		
	}
}
