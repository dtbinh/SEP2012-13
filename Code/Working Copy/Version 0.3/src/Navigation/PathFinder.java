package Navigation;


/**
 * Path finder
 * @author DaweiG
 * @author Khoi
 * @date 08.10.2012
 * @version 0.5
 * 
 * Added PathFinder.java 
 * 
 */
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import MapStructure.Map;
import MapStructure.Pixel;
/**
 * Path finder algorithm provider.
 * @author DaweiG
 * 
 */
public class PathFinder {

static Map m;

static ArrayList<Pixel> unexploredPixels = new ArrayList<Pixel>();
static Stack<Pixel> s = new Stack<Pixel>();

/**
 * Constructor
 * @param m
 */
public PathFinder(Map m){
	this.m = m;
}
/**
 * Simple pathfinding function.
 * @return 1 when all the pixels were covered
 * @return 0 when else 
 */
public static int DFS(Pixel start ) {
	Pixel temp = start;
	System.out.println("PathFinding..");
	start.setPrevMark(0);
	for(Pixel p : m.getPixels()){
		if(p.getValue()==0)
			unexploredPixels.add(p);
	}
	unexploredPixels.remove(temp);
	boolean repeat = true;
	while(unexploredPixels.size() > 0){
		//System.out.println(temp);
		temp.setPathFindingFlag(1);
		   repeat = false;
		   if(temp.getN()!=null)
			   if(temp.getN().getValue() == 0 && temp.getN().getPathFindingFlag() == 0){
				   s.add(temp.getN());
				   temp.setPostMark(1);
				   temp.getN().setPrevMark(3);
				   temp = temp.getN();
				   repeat = true;
				   unexploredPixels.remove(temp);
				   m.setGoal(temp);
				   continue;
			   }
		   
		   if(temp.getS()!=null )
			   if(temp.getS().getValue() == 0 && temp.getS().getPathFindingFlag() == 0){
				   s.add(temp.getS());
				   temp.setPostMark(3);
				   temp.getS().setPrevMark(1);
				   temp = temp.getS();
				   repeat = true;
				   unexploredPixels.remove(temp);
				   m.setGoal(temp);
				   continue;
			   }
		   
		   if(temp.getW()!=null )
			   if(temp.getW().getValue() == 0 && temp.getW().getPathFindingFlag() == 0){
				   s.add(temp.getW());
				   temp.setPostMark(4);
				   temp.getW().setPrevMark(2);
				   temp = temp.getW();
				   repeat = true;
				   unexploredPixels.remove(temp);
				   m.setGoal(temp);
				   continue;
			   }
		   
		   if(temp.getE()!=null )
			   if(temp.getE().getValue() == 0 && temp.getE().getPathFindingFlag() == 0){
				   s.add(temp.getE());
				   temp.setPostMark(2);
				   temp.getE().setPrevMark(4);
				   temp = temp.getE();
				   repeat = true;
				   unexploredPixels.remove(temp);
				   m.setGoal(temp);
				   continue;
			   }
			   
		  
			   
		   if(!repeat)
			   //when no neighbors around returns 0;
			   return 0;

	}
	
	return 1;
	
}

/**
 * Find the nearest unexplored pixel from the start pixel
 * @param start
 * @return a Pixel which is the nearest unexplored pixel from Pixel Start
 */
public static Pixel findNearestUnexplorePixel(Pixel start){
	Pixel result = null;
	int distance = Integer.MAX_VALUE;
	for(Pixel p : unexploredPixels){
		int tempDis = calcDistance(start, p);
		if(distance > tempDis){
			distance = tempDis;
			result = p;
		}
	}
	return result;
}
/**
 * Calc Manhanten distance from one pixel to another
 * @param current pixel
 * @param end the other pixel
 * @return the Manhanten distance between the two pixels
 */
private static int calcDistance(Pixel current,Pixel end){return (Math.abs(end.getxPos()-current.getxPos())+Math.abs(end.getyPos()-current.getyPos()));}



Queue<Pixel> pixQueue = new LinkedList<Pixel>();
//Map map;// = new Map(1, 4, 4, 0, 0);

public static void setMap(Map m1) {
	m = m1;
}

public static Map getMap() {
	return m;
}
/**
 * Set a path from the Pixel *from* to Pixel *to*
 * return an empty linked list when no path was found
 * A-Star implementation
 * @param from
 * @param to
 */
public LinkedList<Pixel> setPath(Pixel from, Pixel to) {
	if(from==null||to==null||from==to){
		return null;
	}
	Comparator<Pixel> comparator = new Comparator<Pixel>(){
		@Override
		public int compare(Pixel arg0, Pixel arg1) {
			// TODO Auto-generated method stub
			if(arg0.getF()>arg1.getF())
				return 1;
			else if(arg0.getF()<arg1.getF())
				return -1;
			return 0;
		}
	};
	PriorityQueue<Pixel> frontier=new PriorityQueue<Pixel>(m.getLength()*m.getWidth(),
			comparator);
	HashSet<Pixel> explored=new HashSet<Pixel>();
	int w= m.getWidth();
	int l=m.getLength();
	Pixel[][] prev=new Pixel[w][l];
	for(int i=0;i<w;i++){
		for(int j=0;j<l;j++){
			prev[i][j]=null;
		}
	}
	for(Pixel p:m.getPixels()){
		p.setG(Integer.MAX_VALUE);
		p.setF(Integer.MAX_VALUE);
		p.setPostMark(0);
	}
	from.setG(0);
	from.setF(from.getG()+PathFinder.calcDistance(from,to));
	frontier.add(from);
	while(!frontier.isEmpty()){
		Pixel curr=frontier.remove();
		if(curr==to){
			LinkedList<Pixel> path=new LinkedList<Pixel>();
			Pixel root=curr;
			path.add(root); 
			while(prev[root.getxPos()][root.getyPos()]!=null){
				int i=-root.getxPos()+prev[root.getxPos()][root.getyPos()].getxPos();
				int j=-root.getyPos()+prev[root.getxPos()][root.getyPos()].getyPos();
				root=prev[root.getxPos()][root.getyPos()];
				if(i==0&&j==1)
					root.setPostMark(Pixel.NORTH);
				if(i==0&&j==-1)
					root.setPostMark(Pixel.SOUTH);
				if(i==1&&j==0)
					root.setPostMark(Pixel.WEST);
				if(i==-1&&j==0)
					root.setPostMark(Pixel.EAST);
				
				path.addFirst(root);
			}
			return path;
		}
		explored.add(curr);
		int xpos=curr.getxPos();
		int ypos=curr.getyPos();
		for(int i=-1;i<2;i++){
			for(int j=-1;j<2;j++){
				Pixel child;
				if(Math.abs(i)!=Math.abs(j)){
					//WARNING: the DEFAULT SETTING 
					// needs to incorporate with what the robot actually find
					child=m.findPixel(xpos+i,ypos+j);		
					if(child!=null){
						if(explored.contains(child))
							continue;
						int tempG= curr.getG() +1;
						if((!frontier.contains(child)||tempG<child.getG())
								&&child.getValue()!=Pixel.NOGOZONE
								&&child.getValue()!=Pixel.WALL
								&&child.getValue()!=Pixel.BUFFERZONE
								){
							prev[xpos+i][ypos+j]=curr;
							child.setG(tempG);
							child.setF(child.getG()+PathFinder.calcDistance(child,to));
							if(!frontier.contains(child)){
								frontier.add(child);
							}
						}
					}	
				}
			}
		}
		}
	
	return null;
	}
}
