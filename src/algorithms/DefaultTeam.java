package algorithms;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class DefaultTeam
{

public ArrayList<Point> calculDominatingSet (ArrayList<Point> points, int edgeThreshold)
{
	return localSearch(points, edgeThreshold);
}

private ArrayList<Point> localSearch (ArrayList<Point> graphe, int edgeThreshold)
{
	int              i  = 0;
	ArrayList<Point> ds = (ArrayList<Point>) graphe.clone();
	while (Evaluation.isValid(graphe, ds, edgeThreshold))
	{
		System.out.println("localsearch n°" + ++i + "   taille de dominantSet : " + ds.size());
		ds = localsearch2_1(ds, graphe, edgeThreshold);
	}
	return ds;
}

private ArrayList<Point> localsearch2_1 (ArrayList<Point> dominantSet, ArrayList<Point> graphe, int edgeThreshold)
{
	ArrayList<Point> rest   = (ArrayList<Point>) graphe.clone();
	int              tooFar = 3 * edgeThreshold;
	for (Point p1 : dominantSet)
	{
		for (Point p2 : dominantSet)
		{
			if (p1.equals(p2))
				continue;
			if (p1.distance(p2) > tooFar)
				continue;

			for (Point x : rest)
			{
				ArrayList<Point> dsClone = (ArrayList<Point>) dominantSet.clone();
				dsClone.remove(p1);
				dsClone.remove(p2);
				dsClone.add(x);
				if (Evaluation.isValid(graphe, dsClone, edgeThreshold))
					return dsClone;
			}
		}
	}
	return dominantSet;
}

private ArrayList<Point> localsearch3_2 (ArrayList<Point> dominantSet, ArrayList<Point> graphe, int edgeThreshold)
{
	ArrayList<Point> rest   = (ArrayList<Point>) graphe.clone();
	int              tooFar = 3 * edgeThreshold;
	for (Point p1 : dominantSet)
	{
		for (Point p2 : dominantSet)
		{
			if (p1.equals(p2))
				continue;
			for (Point p3 : dominantSet)
			{
				if (p1.equals(p2) || p2.equals(p3))
					continue;
				if (p1.distance(p2) > tooFar || p2.distance(p3) > tooFar || p2.distance(p3) > tooFar)
					continue;

				for (Point x : rest)
				{
					for (Point y : rest)
					{
						if (x.equals(y))
							continue;

						ArrayList<Point> dsClone = (ArrayList<Point>) dominantSet.clone();
						dsClone.remove(p1);
						dsClone.remove(p2);
						dsClone.remove(p3);
						dsClone.add(x);
						dsClone.add(y);
						if (Evaluation.isValid(graphe, dsClone, edgeThreshold))
							return dsClone;
					}
				}
			}
		}
	}
	return dominantSet;
}

private ArrayList<Point> glouton (ArrayList<Point> points, int edgeThreshold)
{
	ArrayList<Point> reste     = (ArrayList<Point>) points.clone();
	ArrayList<Point> dominants = new ArrayList<Point>();
	while (!Evaluation.isValid(points, dominants, edgeThreshold))
	{

		dominants.add(reste.remove(0));
		System.out.println(dominants.size());
	}
	return dominants;
}

private ArrayList<Point> gloutonbis (ArrayList<Point> points, int edgeThreshold)
{
	ArrayList<Point> reste     = (ArrayList<Point>) points.clone();
	ArrayList<Point> dominants = new ArrayList<Point>();
	while (Evaluation.isValid(points, reste, edgeThreshold))
	{
		dominants.add(reste.remove(0));
		System.out.println(dominants.size());
	}
	return reste;
}

//FILE PRINTER
private void saveToFile (String filename, ArrayList<Point> result)
{
	int index = 0;
	try
	{
		while (true)
		{
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename + Integer.toString(
					  index) + ".points")));
			try
			{
				input.close();
			} catch (IOException e)
			{
				System.err.println("I/O exception: unable to close " + filename + Integer.toString(index) + ".points");
			}
			index++;
		}
	} catch (FileNotFoundException e)
	{
		printToFile(filename + Integer.toString(index) + ".points", result);
	}
}

private void printToFile (String filename, ArrayList<Point> points)
{
	try
	{
		PrintStream output = new PrintStream(new FileOutputStream(filename));
		int         x, y;
		for (Point p : points)
			output.println(Integer.toString((int) p.getX()) + " " + Integer.toString((int) p.getY()));
		output.close();
	} catch (FileNotFoundException e)
	{
		System.err.println("I/O exception: unable to create " + filename);
	}
}

//FILE LOADER
private ArrayList<Point> readFromFile (String filename)
{
	String           line;
	String[]         coordinates;
	ArrayList<Point> points = new ArrayList<Point>();
	try
	{
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		try
		{
			while ((line = input.readLine()) != null)
			{
				coordinates = line.split("\\s+");
				points.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
			}
		} catch (IOException e)
		{
			System.err.println("Exception: interrupted I/O.");
		} finally
		{
			try
			{
				input.close();
			} catch (IOException e)
			{
				System.err.println("I/O exception: unable to close " + filename);
			}
		}
	} catch (FileNotFoundException e)
	{
		System.err.println("Input file not found.");
	}
	return points;
}
}
