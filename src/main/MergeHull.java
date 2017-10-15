package main;

import java.awt.geom.Point2D;
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
		//Divide:
		//Divide the sorted list into two equal sets
		
		//Conquer:
		//Recursively compute the hull of each subset of points
		//**Be careful of the base cases**
		
		//Combine:
		//Combine the smaller hulls into one larger one
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
