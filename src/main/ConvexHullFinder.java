package main;

import java.awt.geom.Point2D;
import java.util.List;

public interface ConvexHullFinder {
	public List<Point2D> computeHull(List<Point2D> points);
}
