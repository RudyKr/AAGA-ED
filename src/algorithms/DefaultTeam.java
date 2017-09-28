package algorithms;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class DefaultTeam
{

private int cpt = 0;

public static boolean isValid (ArrayList<Point> graphe, ArrayList<Point> dominantSet, int edgeThreshold)
{
	for (Point p : graphe)
	{
		if (dominantSet.contains(p))
		{
			continue;
		}
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

protected static ArrayList<Point> neighbor (Point p, ArrayList<Point> vertices, int edgeThreshold)
{
	ArrayList<Point> result = new ArrayList<Point>();

	for (Point point : vertices)
		if (point.distance(p) < edgeThreshold && !point.equals(p))
			result.add((Point) point.clone());

	return result;
}

public ArrayList<Point> calculDominatingSet (ArrayList<Point> points, int edgeThreshold)
{
//	return localSearch(points, edgeThreshold);
	return localSearch10(points, edgeThreshold);
//	return gloutonDist(points, edgeThreshold);
}

private ArrayList<Point> localSearch10 (ArrayList<Point> graphe, int edgeThreshold)
{
	long               time   = System.currentTimeMillis();
	ArrayList<Integer> scores = new ArrayList<>();
	ArrayList<Point>   bestDS = localSearch(graphe, edgeThreshold);
	scores.add(bestDS.size());
	for (int i = 0; i < 10; i++)
	{
		ArrayList<Point> local = localSearch(graphe, edgeThreshold);
		scores.add(local.size());
		if (local.size() < bestDS.size())
			bestDS = local;
		System.out.print(local.size() + "   ");
	}
	System.out.println("\nFinished : high score :" + bestDS.size() + " \t \t \t scores : " + scores);
	time -= System.currentTimeMillis();
	System.out.println("Execution time : " + (double) (-time) / 1000 + "sec");
	return bestDS;

}

private ArrayList<Point> localSearch (ArrayList<Point> graphe, int edgeThreshold)
{
//	long             time     = System.currentTimeMillis();
//	int              i        = 0;
	ArrayList<Point> ds       = (ArrayList<Point>) graphe.clone();
	ArrayList<Point> dsreturn = new ArrayList<Point>();
	while (isValid(graphe, ds, edgeThreshold))
	{
		int sizebefore = ds.size();
//		long timeloop   = System.currentTimeMillis();
		dsreturn = (ArrayList<Point>) ds.clone();
		ds = localsearch2_1(ds, graphe, edgeThreshold);
//		ds = localsearch3_2(ds, graphe, edgeThreshold);
//		long timeendloop = System.currentTimeMillis();
//		long loop        = timeendloop - timeloop;
//		graphe.add(graphe.remove(0));
//		if (i * 0.0012 > 1)
//		{
//			graphe.add(graphe.remove(0));
//			System.out.println("DOUBLE SHIFT");
//			for (int z = 0; z < 2 * loop / 1000; z++)
//			{
//				graphe.add(graphe.remove(0));
//				System.out.println(z + 1 + " RE-SHIFT !");
//			}
//		}
//		if (i % 3 == 0)
//		{
//			graphe.add(graphe.remove(0));
//			graphe.add(graphe.remove(0));
//			System.out.println("DOUBLE SHIFT !");
//		}

//		System.out.println(
//				  "localsearch n°" + i++ + "   taille de dominantSet : " + ds.size() + "   temps d'execution : " + "" +
//				  (double) (timeendloop - time) / 1000 + "s" + "   temps de loop :" + (double) (loop) / 1000 + "s");
		int sizeafter = ds.size();
		if (sizeafter - sizebefore == 0)
		{
			System.out.println("Terminé ! Score : " + sizeafter);
			break;
		}
	}
	return dsreturn;
}

private ArrayList<Point> localsearch2_1 (ArrayList<Point> dominantSet, ArrayList<Point> graphe, int edgeThreshold)
{
//	int              i      = 0, j, k;
	ArrayList<Point> rest   = (ArrayList<Point>) graphe.clone();
	double           tooFar = Math.sqrt(2) * 3 * edgeThreshold;
	for (int i = 0; i < dominantSet.size(); i++)
	{
//		i++;
//		j = 0;
		Point ii = dominantSet.get(i);
		for (int j = i + 1; j < dominantSet.size(); j++)
		{
//			j++;
			Point jj = dominantSet.get(j);
			if (ii.distance(jj) > tooFar)
				continue;
//			k = 0;
			for (int k = 0; k < rest.size(); k++)
			{
				Point kk = rest.get(k);
				if (ii.distance(kk) > tooFar || jj.distance(kk) > tooFar)
					continue;

//				k++;
				ArrayList<Point> dsClone = (ArrayList<Point>) dominantSet.clone();
				dsClone.remove(ii);
				dsClone.remove(jj);
				dsClone.add(kk);
				if (isValid(graphe, dsClone, edgeThreshold))
				{
//					System.out.println("Valid trouvé après " + i + " x " + j + " x " + k + " itérations");
					return dsClone;
				}
			}
		}
	}
	return dominantSet;
}

private ArrayList<Point> localsearch3_2 (ArrayList<Point> dominantSet, ArrayList<Point> graphe, int edgeThreshold)
{
	ArrayList<Point> rest = (ArrayList<Point>) graphe.clone();
	System.out.println("start");
	System.out.println(rest.size());
	rest.removeAll(dominantSet);
	System.out.println(rest.size());
	double tooFar = Math.sqrt(2) * 3 * edgeThreshold;
	for (int i = 0; i < dominantSet.size(); i++)
	{
		Point ii = dominantSet.get(i);
		for (int j = i; j < dominantSet.size(); j++)
		{
			System.out.println(i + " / " + j);
			Point jj = dominantSet.get(j);
			if (ii.distance(jj) > tooFar || ii.equals(jj))
				continue;
			for (int k = i + j; k < dominantSet.size(); k++)
			{
				if (k < i || k < j)
					continue;
				Point kk = dominantSet.get(k);
				if (kk.distance(ii) > tooFar || kk.distance(jj) > tooFar || kk.equals(ii) || kk.equals(jj))
					continue;
				for (int x = 0; x < rest.size(); x++)
				{
					Point xx = rest.get(x);
					if (xx.distance(ii) > tooFar || xx.distance(jj) > tooFar || xx.distance(kk) > tooFar || xx.equals(ii) ||
					    xx.equals(ii) || xx.equals(jj) || xx.equals(kk))
						continue;
					for (int y = 0; y < rest.size(); y++)
					{
						Point yy = rest.get(y);
						if (yy.distance(ii) > tooFar || yy.distance(jj) > tooFar || yy.distance(kk) > tooFar ||
						    yy.distance(xx) > tooFar || yy.equals(ii) || yy.equals(ii) || yy.equals(jj) || yy.equals(kk) ||
						    yy.equals(xx))
							continue;
						ArrayList<Point> dsClone = (ArrayList<Point>) dominantSet.clone();
						dsClone.remove(ii);
						dsClone.remove(jj);
						dsClone.remove(kk);
						dsClone.add(xx);
						dsClone.add(yy);
						if (isValid(graphe, dsClone, edgeThreshold))
						{
							System.out.println(
									  "Score :" + dsClone.size() + "\nValid trouvé après " + i + 1 + " x " + j + 1 + " x " + k +
									  1 + " x " + x + 1 + " x " + y + 1 + "" + " itérations");
							return dsClone;
						}
					}
				}
			}
		}
	}
	return dominantSet;
}

private ArrayList<Point> gloutonbis (ArrayList<Point> points, int edgeThreshold)
{
	ArrayList<Point> reste     = (ArrayList<Point>) points.clone();
	ArrayList<Point> dominants = new ArrayList<Point>();
	while (isValid(points, reste, edgeThreshold))
	{
		dominants.add(reste.remove(0));
		System.out.println(dominants.size());
	}
	return reste;
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

//FILE PRINTER
private void saveToFile (String filename, ArrayList<Point> result)
{
	int index = 0;
	try
	{
		while (true)
		{
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(
					  filename + Integer.toString(index) + ".points")));
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

private ArrayList<Point> gloutonDist (ArrayList<Point> graphe, int edgeThreshold)
{
	ArrayList<Point> ds           = (ArrayList<Point>) graphe.clone();
	ArrayList<Point> pointsGrades = new ArrayList<>();
	ArrayList<Point> artirer      = new ArrayList<>();
	Point            a            = new Point();
	Point            b            = new Point();
	double           distMin      = 1000.0;
	int              gradeMax     = 0;
	while (true)
	{
		ds.removeAll(artirer);
		ArrayList<Point> dsreturn = (ArrayList<Point>) ds.clone();
		for (Point i : ds)
		{
			int s = neighbor(i, ds, edgeThreshold).size();
			if (s > gradeMax)
			{
				pointsGrades.clear();
				gradeMax = s;
				pointsGrades.add(i);
			}
		}
		for (Point i : pointsGrades)
		{
			for (Point p : ds)
			{
				if (i.distance(p) < distMin)
				{
					a = i;
					b = p;
				}
			}
		}
		if (neighbor(a, ds, edgeThreshold).size() > neighbor(b, ds, edgeThreshold).size())
		{
			ds.remove(a);
			artirer.add(a);
		}
		else
		{
			ds.remove(b);
			artirer.add(b);
		}
		System.out.println(dsreturn.size());
		if (!isValid(graphe, ds, edgeThreshold))
			return dsreturn;

	}
//		Collections.shuffle(graphe);
}
}