package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

/**
 * Map data structure version: 0.1
 * @author DaweiG
 * @date 25.09.2012
 * @version 0.3
 * Added PathFinder.java 
 * 
 */

import java.rmi.UnexpectedException;
import java.util.ArrayList;

import java.util.Queue;
import java.util.Stack;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;



public class PathFinder {

static Map m;
static Pixel currentPixel;
static ArrayList<Pixel> unexploredPixels = new ArrayList<Pixel>();
static Stack<Pixel> s = new Stack<Pixel>();


public static void start(Pixel startPixel,Map map) throws UnexpectedException{
	m = map;
	currentPixel=startPixel;
	while(DFS(startPixel,m) != 1){
		setPath(currentPixel,findNearestUnexplorePixel(currentPixel));
	}
	setPath(currentPixel,m.findPixel(m.getStartxPos(), m.getStartyPos()));
}
/**
 * 
 * @return 1 when all the pixels were covered
 * @return 0 when else 
 */
static int DFS(Pixel start, Map map ) {
	Pixel temp = start;
	start.setPrevMark(0);
	for(Pixel p : map.getPixels()){
		if(p.getValue()==0)
			unexploredPixels.add(p);
	}
	unexploredPixels.remove(temp);
	boolean repeat = true;
	while(unexploredPixels.size() > 0){
		
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
				   continue;
			   }
		   if(!repeat)
			   //when no neighbors around returns 0;
			   return 0;
	}
	return 1;
}

/**
 * 
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
//Calculate Manhattan distance form one pixel to another.
private static int calcDistance(Pixel current,Pixel end){return (Math.abs(end.getxPos()-current.getxPos())+Math.abs(end.getyPos()-current.getyPos()));}


/**
 * Set a path from the Pixel *from* to Pixel *to*
 * throws UnexpectedException when no path was found
 * @param from
 * @param to
 */



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
public static LinkedList<Pixel> setPath(Pixel from, Pixel to) {
	if(from==null||to==null||from==to){
		return null;
	}
	Comparator<Pixel> comparator = new CostComparator();
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
								&&child.getValue()!=Pixel.HIDDENWALL
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
/*
public static boolean goal_test(Pixel current, Pixel dest) {
	if (current.getxPos() == dest.getxPos()
			&& current.getyPos() == dest.getyPos())
		return true;
	else
		return false;
}

public static boolean visited(int x, int y, List<Pixel> visitedNodes) {
	for (Pixel p : visitedNodes) {
		if (x == p.getxPos() && y == p.getyPos())
			return true;
	}
	return false;
}

public static void SetPath(Pixel from, Pixel to) throws UnexpectedException {
	Queue<Pixel> NodeQueue = new LinkedList<Pixel>();
	List<Pixel> visitedNodes = new ArrayList<Pixel>();
	NodeQueue.add(from);
//	Pixel goalNode = null;
	Pixel currentPixel = null;

	
	final int endX = to.getxPos();
	final int endY = to.getyPos();

	while (!NodeQueue.isEmpty()) {
		
		Collections.sort((LinkedList<Pixel>)NodeQueue, new Comparator<Pixel>(){
			public int compare(Pixel p1, Pixel p2){
				if(f(p1)==f(p2))
					return 0;
				else if(f(p1)<f(p2))
					return -1;
				else
					return 1;
			}
			public int f(Pixel p){
				return Math.abs(p.getxPos() - endX) + Math.abs(p.getyPos() - endY);
			}
		});
		
		
		currentPixel = NodeQueue.poll();
//		System.out.print("");
		
//		System.out.println(goal_test(currentPixel, to));

		if (goal_test(currentPixel, to)) {
//			currentPixel = to;
//			System.out.println("reach here");
			break;
		}

		if (!visited(currentPixel.getxPos(), currentPixel.getyPos(), visitedNodes)) {
			expand_Astar(currentPixel, NodeQueue, m, visitedNodes);
			visitedNodes.add(currentPixel);
		}
		
	}
	
	if (goal_test(currentPixel, to)) {
		Pixel track = currentPixel.getParent();
		Stack<Pixel> pathNodes = new Stack<Pixel>();
		pathNodes.add(currentPixel);
		pathNodes.add(track);
		while(track != null){
			track = track.getParent();
			pathNodes.add(track);
		}
		printPath(pathNodes);
	}

}

private static void printPath(Stack<Pixel> pathNodes) {
		while (!pathNodes.isEmpty()) {
			Pixel p = pathNodes.pop();
			if (p != null) 
				System.out.println(String.format("x: %d y: %d", p.getxPos(), p.getyPos()));			
		}
}

public static void expand_Astar(Pixel p, Queue<Pixel> fringe, Map m, List<Pixel> visitedNodes) {
	//north
	if (!visited(p.getxPos(), p.getyPos() - 1, visitedNodes) && p.getyPos() - 1 > 0 && m.findPixel(p.getxPos(), p.getyPos() - 1).getValue() == 0) {
		Pixel upChild = new Pixel(p.getxPos(), p.getyPos() - 1, 0);
		upChild.setParent(p);
		p.setN(upChild);
		fringe.add(upChild);
	}
	//south
	if (!visited(p.getxPos(), p.getyPos() + 1, visitedNodes) && p.getyPos() + 1 < m.getWidth() && m.findPixel(p.getxPos(), p.getyPos() + 1).getValue() == 0) {
		Pixel downChild = new Pixel(p.getxPos(), p.getyPos() + 1, 0);
		downChild.setParent(p);
		p.setS(downChild);
		fringe.add(downChild);
	}
	//left
	if (!visited(p.getxPos() - 1, p.getyPos(), visitedNodes) && p.getxPos() - 1 > 0 && m.findPixel(p.getxPos() - 1, p.getyPos()).getValue() == 0) {
		Pixel leftChild = new Pixel(p.getxPos() - 1, p.getyPos(), 0);
		leftChild.setParent(p);
		p.setW(leftChild);
		fringe.add(leftChild);
	}
	
	//right
	if (!visited(p.getxPos() + 1, p.getyPos(), visitedNodes) && p.getxPos() + 1 < m.getLength() && m.findPixel(p.getxPos() + 1, p.getyPos()).getValue() == 0) {
		Pixel rightChild = new Pixel(p.getxPos() + 1, p.getyPos(), 0);
		rightChild.setParent(p);
		p.setE(rightChild);
		fringe.add(rightChild);
	}
}
*/

