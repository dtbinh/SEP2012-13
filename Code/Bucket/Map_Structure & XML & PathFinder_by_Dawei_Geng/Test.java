import java.awt.geom.Path2D;
import java.rmi.UnexpectedException;
import java.util.LinkedList;

/**
 * Map data structure version: 0.1
 * @author DaweiG
 * @date 18.09.2012
 * @version 0.2
 * 
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map m = new Map(1,3,3,0,0);
		//PCThread btc = new PCThread();
		//btc.start();
		PathFinder pf = new PathFinder(m);
		//PathFinder.DFS(m.findPixel(m.getStartxPos(), m.getStartyPos()), m);
		
		XMLReaderWriter xml = new XMLReaderWriter();
		LinkedList<Pixel> pix = pf.setPath(m.getStartPixel(), m.findPixel(1,1));
		if(pix != null)
			for(Pixel p : pix)
				System.out.println(p);
		
		/**try {
			
			RobotAutoScanner.AutoScan(m,btc);
		} catch (UnexpectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		**/
		
		System.out.println();
		
		
	}

}
