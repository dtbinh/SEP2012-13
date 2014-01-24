package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class PathFromTo {

	
	Queue<Pixel> pixQueue = new LinkedList<Pixel>();
	Map map;// = new Map(1, 4, 4, 0, 0);
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public Map getMap() {
		return map;
	}

	public boolean goal_test(Pixel current, Pixel dest) {
		if (current.getxPos() == dest.getxPos()
				&& current.getyPos() == dest.getyPos())
			return true;
		else
			return false;
	}

	public boolean visited(int x, int y, List<Pixel> visitedNodes) {
		for (Pixel p : visitedNodes) {
			if (x == p.getxPos() && y == p.getyPos())
				return true;
		}
		return false;
	}

	public void SetPath(Pixel from, Pixel to) throws UnexpectedException {
		Queue<Pixel> NodeQueue = new LinkedList<Pixel>();
		List<Pixel> visitedNodes = new ArrayList<Pixel>();
		NodeQueue.add(from);
//		Pixel goalNode = null;
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
//			System.out.print("");
			
//			System.out.println(goal_test(currentPixel, to));

			if (goal_test(currentPixel, to)) {
//				currentPixel = to;
//				System.out.println("reach here");
				break;
			}

			if (!visited(currentPixel.getxPos(), currentPixel.getyPos(), visitedNodes)) {
				expand_Astar(currentPixel, NodeQueue, map, visitedNodes);
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
	
	
	public void buildPath() {

	}

	private void printPath(Stack<Pixel> pathNodes) {
			while (!pathNodes.isEmpty()) {
				Pixel p = pathNodes.pop();
				if (p != null) 
					System.out.println(String.format("x: %d y: %d", p.getxPos(), p.getyPos()));			
			}
	}

	public void expand_Astar(Pixel p, Queue<Pixel> fringe, Map m, List<Pixel> visitedNodes) {
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

}
