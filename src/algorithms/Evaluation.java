package algorithms;

import java.awt.*;
import java.util.ArrayList;

public class Evaluation
{
private static boolean isMember (ArrayList<Point> points, Point p)
{
	for (Point point : points)
		if (point.equals(p))
			return true;
	return false;
}

public static boolean isValid (ArrayList<Point> graphe, ArrayList<Point> dominantSet, int edgeThreshold)
{
	boolean valid = true;
	for (Point p : graphe)
	{
		boolean exists = false;
		for (Point q : dominantSet)
		{
			if (p.distance(q) < edgeThreshold)
			{
				exists = true;
				break;
			}
		}
		if (!exists)
			return false;
	}
	return true;
}

public static boolean isValid (ArrayList<Point> origPoints,
                               ArrayList<Point> edominant,
                               ArrayList<Point> edomines,
                               int edgeThreshold)
{
	boolean valid = true;
	for (Point p : origPoints)
	{
		if (edominant.contains(p))
			continue;
		if (edomines.contains(p))
			continue;
		valid = false;
	}
	return valid;
}

protected static ArrayList<Point> neighbor (Point p, ArrayList<Point> vertices, int edgeThreshold)
{
	ArrayList<Point> result = new ArrayList<Point>();

	for (Point point : vertices)
		if (point.distance(p) < edgeThreshold && !point.equals(p))
			result.add((Point) point.clone());

	return result;
}
}
