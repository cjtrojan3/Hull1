package main;

import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class MergeHull implements ConvexHullFinder{

	/**
	 * Main method that does initial setup then calls the recursive method
	 * 
	 * @param points - The list of points of which we need to find the hull
	 */
	@Override
	public List<Point2D> computeHull(List<Point2D> points) {
		//Sort the points
		Collections.sort(points, createComparator());
		
		List<Point2D> hullList = recursiveMergeHull(points);
		
		return hullList;
	}
	
	/**
	 * Recursive method that is being called
	 * 
	 * @param points List of points that we are finding the hull of
	 * @return List of points that are on the hull
	 */
	private List<Point2D> recursiveMergeHull(List<Point2D> points){
		//Base Case(s):
		if(points.size() <= 3) {
			QuickHull q = new QuickHull();
			return q.computeHull(points);
		}
		
		//Divide + Conquer:
		//Divide the sorted list into two equal sets and compute the hull of them
		List<Point2D> listA = recursiveMergeHull(points.subList(0, points.size()/2));
		List<Point2D> listB = recursiveMergeHull(points.subList(points.size()/2, points.size()));
		
		//Combine:
		//Combine the smaller hulls into one larger one
		
		//Get the furthest right of left list and the furthest left of the right list
		//List is supposed to be in CCW order, but might not be sorted from left to right
		//Stevenson's write-up says that, so I'm finding the points with these for-loops
		Point2D midLeft = listA.get(0);
		for(Point2D p : listA) {
			if(p.getX() > midLeft.getX()) {
				midLeft = p;
			}
		}
		
		Point2D midRight = listB.get(0);
		for(Point2D p : listB) {
			if(p.getX() < midRight.getX()) {
				midRight = p;
			}
		}
		Point2D midLowerLeft = (Point2D) midLeft.clone();
		Point2D midLowerRight = (Point2D) midRight.clone();
		
		Point2D midUpperLeft = (Point2D) midLeft.clone();
		Point2D midUpperRight = (Point2D) midRight.clone();
		
		Line2D lowerTangent = new Line2D.Double(midLowerLeft, midLowerRight);
		Line2D upperTangent = new Line2D.Double(midUpperLeft, midUpperRight);
		
		//HARD PART HERE, THOTS
		//The walking of the tangent line relies heavily on the order of the lists being in CCW order.
		
		//LowerTangent
		while(!tanTest(listA, lowerTangent, -1) && !tanTest(listB, lowerTangent, -1)) {
			while(!tanTest(listA, lowerTangent, -1)) {
				try {
					midLowerLeft = listA.get(listA.indexOf(midLeft)-1);
				}
				catch(IndexOutOfBoundsException e) {
					midLowerLeft = listA.get(listA.size()-1);
				}
				lowerTangent.setLine(midLowerLeft, midLowerRight);
			}
			while(!tanTest(listB, lowerTangent, -1)) {
				try {
					midLowerRight = listB.get(listB.indexOf(midRight)+1);
				}
				catch(IndexOutOfBoundsException e) {
					midLowerRight = listB.get(0);
				}
				lowerTangent.setLine(midLowerLeft, midLowerRight);
			}
		}
		//UpperTangent
		while(!tanTest(listA, upperTangent, 1) && !tanTest(listB, upperTangent, 1)) {
			while(!tanTest(listA, upperTangent, 1)) {
				try {
					midUpperLeft = listA.get(listA.indexOf(midLeft)+1);
				}
				catch(IndexOutOfBoundsException e) {
					midUpperLeft = listA.get(0);
				}
				upperTangent.setLine(midUpperLeft, midUpperRight);
			}
			while(!tanTest(listB, upperTangent, 1)) {
				try {
					midUpperRight = listB.get(listB.indexOf(midUpperRight)-1);
				}
				catch(IndexOutOfBoundsException e) {
					midUpperRight = listB.get(listB.size()-1);
				}
				lowerTangent.setLine(midUpperLeft, midUpperRight);
			}
		}
		ArrayList<Point2D> aaa = new ArrayList<Point2D>();
		aaa.add(lowerTangent.getP1());
		aaa.add(lowerTangent.getP2());
		return aaa;
	}
	
	
	//Helper
	private boolean tanTest(List<Point2D> list, Line2D hullConnector, int side) {
		//TODO Make it so it only checks the points surrounding the current point, so three total
		for(Point2D p : list) {
			if(hullConnector.relativeCCW(p) == side) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Comparater that helps sort the List<Point2D>
	 * @return Thing that compares the list of Points in O(nlog2n) time
	 */
	private static Comparator<Point2D> createComparator(){
        return new Comparator<Point2D>(){
            @Override
            public int compare(Point2D p0, Point2D p1){
                double p0x = p0.getX();
                double p1x = p1.getX();
                return Double.compare(p0x, p1x);
            }
        };
    }
}
