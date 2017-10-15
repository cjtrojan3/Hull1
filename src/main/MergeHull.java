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
		
		System.out.println("Sorted list of points:");
		System.out.println("----------------------");
		for(Point2D p : points) {
			System.out.println("X: " + p.getX() + " Y: " + p.getY());
		}
		
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
		//TODO Base Case(s):
		//Stevenson's write-up says that we have to be careful about these, so stay frosty, Dan
		
//		if(points.size() == 1 || points.size() == 2) {
//			return points;
//		}
		
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
		
		Line2D hullConnector = new Line2D.Double(midLeft, midRight);
		
		//HARD PART HERE, THOTS
		//The walking of the tangent line relies heavily on the order of the lists being in CCW order.
		
//		A <-- rightmost point of left hull
//		B <-- leftmost point of right hull
//		while (T = AB is not the lower tangent to both left and right hulls) {
//			while (T not lower tangent to left hull) {
//				A <-- A – 1 (assumption is that hulls are defined in ccw order)
//			}
//			while (T not lower tangent to right hull) {
//				B <-- B + 1 (assumption is that hulls are defined in ccw order)
//			}
//		}
		
		return null;
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
