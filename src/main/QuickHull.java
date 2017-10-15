package main;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class QuickHull implements ConvexHullFinder{

	@Override
	public List<Point2D> computeHull(List<Point2D> points) {
		//Initial setup:

		//Find the leftmost and rightmost points
		Point2D furthestLeft = points.get(0);
		Point2D furthestRight = points.get(0);
		for(Point2D p : points) {
			if(p.getX() < furthestLeft.getX()) {
				furthestLeft = p;
			}
			else if(p.getX() > furthestRight.getX()) {
				furthestRight = p;
			}
		}
		
		//Construct a line connecting the two
		Line2D midLine = new Line2D.Double(furthestLeft, furthestRight); 
		
		//Divide the points into two sets, those above the line and those below
		
		List<Point2D> upperPoints = new ArrayList<Point2D>();
		List<Point2D> lowerPoints = new ArrayList<Point2D>();
		
		for(Point2D p : points) {
			if(midLine.relativeCCW(p) == 1) {
				upperPoints.add(p);
			}
			else if(midLine.relativeCCW(p) == -1) {
				lowerPoints.add(p);
			}
			else {
				//The point cannot be on the hull because it's between the edge points
			}

		}
		
		//Call the recursive method, giving the line and the top set of points to produce the top half of the hull
		List<Point2D> firstList = recursiveQuickHull(midLine, upperPoints);
		//Call the recursive method, giving the reversed line and the bottom set of points to produce the bottom half of the hull
		Line2D midLineReverse = new Line2D.Double(furthestRight, furthestLeft);
		List<Point2D> secondList = recursiveQuickHull(midLineReverse, lowerPoints);
		secondList.remove(0);
		//Once both halves are obtained, glue them together to form a counter clock wise set of hull
		//points – note any starting location is acceptable, but the points need to go in
		//ccw order around the hull.
		
		for(Point2D p : secondList) {
			firstList.add(p);
		}
		for(Point2D p : firstList) {
			System.out.println("X: " + p.getX() + " Y: " + p.getY());
		}
		return firstList;
	}
	
	//Recursive method to be called
	private List<Point2D> recursiveQuickHull(Line2D lineAB, List<Point2D> pointsAB){
		//Base cases
		if (pointsAB.size() == 0) {
			ArrayList<Point2D> list = new ArrayList<Point2D>();
			list.add(lineAB.getP2());
			list.add(lineAB.getP1());
			return list;
		}
		if (pointsAB.size() == 1) {
			ArrayList<Point2D> list = new ArrayList<Point2D>();
			list.add(lineAB.getP2());
			list.add(pointsAB.get(0));
			list.add(lineAB.getP1());
			return list;
		}
		
		//Divide the large problem into smaller subproblems
		Point2D furthest = pointsAB.get(0);
		double furthestDist = lineAB.ptLineDist(pointsAB.get(0));
		for(Point2D p : pointsAB) {
			double distance = lineAB.ptLineDist(p);
			if(distance > furthestDist) {
				furthest = p;
				furthestDist = distance;
			}
		}
		//We have the furthest point from the line now, so we can call the method again
		Line2D BC = new Line2D.Double(furthest, lineAB.getP2());
		List<Point2D> upperBC = new ArrayList<Point2D>();
		for(Point2D p : pointsAB) {
			if(BC.relativeCCW(p) == 1) {
				upperBC.add(p);
			}
		}
		
		List<Point2D> firstList = recursiveQuickHull(BC, upperBC);
		firstList.remove(firstList.size()-1);
		
		Line2D AC = new Line2D.Double(lineAB.getP1(), furthest);
		List<Point2D> upperAC = new ArrayList<Point2D>();
		for(Point2D p : pointsAB) {
			if(AC.relativeCCW(p) == 1) {
				upperAC.add(p);
			}
		}
		List<Point2D> secondList = recursiveQuickHull(AC, upperAC);
		//Recursively conquer the subproblems
		
		//Combine the results of the subproblems into a solution for the large problem
		for(Point2D p : secondList) {
			firstList.add(p);
		}
		return firstList;
	}

}
