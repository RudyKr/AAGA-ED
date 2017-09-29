package algorithms;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class DefaultTeam
{
static int cpt = -1;

private static boolean isValid (ArrayList<Point> graphe, ArrayList<Point> dominantSet, int edgeThreshold)
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
			if (distancecarree(p, q) < edgeThreshold * edgeThreshold)
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

private static double distancecarree (Point p1, Point p2)
{
	double var1 = p1.getX() - p2.getX();
	double var2 = p1.getY() - p2.getY();
	return var1 * var1 + var2 * var2;
}

public ArrayList<Point> calculDominatingSet (ArrayList<Point> points, int edgeThreshold)
{
//	return localSearch(points, edgeThreshold);
//	return testValid(points, edgeThreshold);
	cpt++;
	if (cpt % 100 == -1)
		save();
	return bestLS(points, edgeThreshold);
//	return result();
}

private void save ()
{
	for (int i = 0; i < 100; i++)
	{
		printToFile("records/backup/res" + i + ".points", readFromFile("records/res" + i + ".points"));
	}
}

private ArrayList<Point> result ()
{
	return readFromFile("records/res" + cpt % 100 + ".points");
}

private ArrayList<Point> bestLS (ArrayList<Point> graphe, int edgeThreshold)
{
//	long               time   = System.currentTimeMillis();
	ArrayList<Integer> scores = new ArrayList<>();
	ArrayList<Point>   bestDS = readFromFile("records/res" + cpt % 100 + ".points");
	//	if (temp.size() < bestDS.size())
//	{
//	}
	scores.add(bestDS.size());
	for (int i = 0; i < 5; i++)
	{
		ArrayList<Point> local = localSearch(graphe, edgeThreshold);
		scores.add(local.size());
		if (local.size() < bestDS.size())
			bestDS = local;
	}
//	Double avg = scores.stream().mapToInt(val -> val).average());
	if (scores.get(1) < scores.get(0))
	{
		System.out.println(
				  "New high score on instance n°" + cpt % 100 + " ! " + scores.get(1) + " !!    (Previous one was " +
				  scores.get(0) + ")        Average : " + avg());
	}
//	System.out.println(
//			  "\nFinished : high score :" + bestDS.size() + " \t \t \t scores : " + scores + "      avg : " + avg);
//	time -= System.currentTimeMillis();
//	System.out.println("Execution time : " + (double) (-time) / 1000 + "sec");
	printToFile("records/res" + Integer.toString(cpt % 100) + ".points", bestDS);
	return bestDS;

}

private ArrayList<Point> localsearch3_2 (ArrayList<Point> dominantSet, ArrayList<Point> graphe, int edgeThreshold)
{
	ArrayList<Point> rest = (ArrayList<Point>) graphe.clone();
//	System.out.println("start");
//	System.out.println(rest.size());
//	rest.removeAll(dominantSet);
//	System.out.println(rest.size());
	double tooFar = 5 * edgeThreshold * edgeThreshold;
	for (int i = 0; i < dominantSet.size(); i++)
	{
		Point ii = dominantSet.get(i);
		for (int j = i + 1; j < dominantSet.size(); j++)
		{
			Point jj = dominantSet.get(j);
			if (distancecarree(ii, jj) > tooFar)
				continue;
			for (int k = i + j; k < dominantSet.size(); k++)
			{
//				if (k < i || k < j)
//					continue;
				Point kk = dominantSet.get(k);
				if (distancecarree(kk, ii) > tooFar || distancecarree(kk, jj) > tooFar || kk.equals(ii) || kk.equals(jj))
					continue;
				for (int x = 0; x < rest.size(); x++)
				{
					Point xx = rest.get(x);
					if (distancecarree(xx, ii) > tooFar || distancecarree(xx, jj) > tooFar ||
					    distancecarree(xx, kk) > tooFar || xx.equals(ii) || xx.equals(ii) || xx.equals(jj) || xx.equals(kk))
						continue;
					for (int y = x; y < rest.size(); y++)
					{
						Point yy = rest.get(y);
						if (distancecarree(yy, ii) > tooFar || distancecarree(yy, jj) > tooFar ||
						    distancecarree(yy, kk) > tooFar || distancecarree(yy, xx) > tooFar || yy.equals(ii) ||
						    yy.equals(ii) || yy.equals(jj) || yy.equals(kk) || yy.equals(xx))
							continue;
						ArrayList<Point> dsClone = (ArrayList<Point>) dominantSet.clone();
						dsClone.remove(ii);
						dsClone.remove(jj);
						dsClone.remove(kk);
						dsClone.add(xx);
						dsClone.add(yy);
						if (isValid(graphe, dsClone, edgeThreshold))
						{
//							System.out.println(
//									  "Score :" + dsClone.size() + "\nValid trouvé après " + i + 1 + " x " + j + 1 + " x " + k +
//									  1 + " x " + x + 1 + " x " + y + 1 + "" + " itérations");
							return dsClone;
						}
					}
				}
			}
		}
	}
	return dominantSet;
}

private ArrayList<Point> localSearch (ArrayList<Point> graphe, int edgeThreshold)
{
//	long             time     = System.currentTimeMillis();
//	int              i        = 0;
	ArrayList<Point> ds       = (ArrayList<Point>) graphe.clone();
	ArrayList<Point> dsreturn = new ArrayList<>();
	while (isValid(graphe, ds, edgeThreshold))
	{
		int sizebefore = ds.size();
//		long timeloop   = System.currentTimeMillis();
		dsreturn = (ArrayList<Point>) ds.clone();
		Collections.shuffle(graphe);
		ds = localsearch2_1(ds, graphe, edgeThreshold);
//		ds = localsearch3_2(ds, graphe, edgeThreshold);
//		long timeendloop = System.currentTimeMillis();
//		long loop        = timeendloop - timeloop;
//		graphe.add(graphe.remove(0));

//		System.out.println(
//				  "localsearch n°" + i++ + "   taille de dominantSet : " + ds.size() + "   temps d'execution : " + "" +
//				  (double) (timeendloop - time) / 1000 + "s" + "   temps de loop :" + (double) (loop) / 1000 + "s");
		int sizeafter = ds.size();
		if (sizeafter - sizebefore == 0)
		{
//			long end = System.currentTimeMillis() - time;
//			System.out.println("Terminé ! Score : " + sizeafter + "   Execution time : " + (double)end/1000 + " sec");
			break;
		}
	}
//	System.out.println(ds.size());
	return dsreturn;
}

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

private ArrayList<Point> localsearch2_1 (ArrayList<Point> dominantSet, ArrayList<Point> graphe, int edgeThreshold)
{
//	int              i      = 0, j, k;
	ArrayList<Point> rest   = (ArrayList<Point>) graphe.clone();
	double           tooFar = 5 * edgeThreshold * edgeThreshold;
	for (int i = 0; i < dominantSet.size(); i++)
	{
//		i++;
//		j = 0;
		Point ii = dominantSet.get(i);
		for (int j = i + 1; j < dominantSet.size(); j++)
		{
//			j++;
			Point jj = dominantSet.get(j);
			if (distancecarree(ii, jj) > tooFar)
				continue;
//			k = 0;
			for (Point kk : rest)
			{
				if (distancecarree(ii, kk) > tooFar || distancecarree(jj, kk) > tooFar)
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

private double avg ()
{
	double avg = 0;
	for (int i = 0; i < 100; i++)
		avg += readFromFile("records/res" + i + ".points").size();
	return avg / 100;
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

private int countFiles () throws Exception
{
	File file = new File("records");

	if (!file.exists())
		throw new FileNotFoundException();
	return file.list().length;
}

private ArrayList<Point> testValid (ArrayList<Point> graphe, int edgeThreshold)
{
	ArrayList<Point> ds   = localSearch(graphe, edgeThreshold);
	long             time = System.currentTimeMillis();
	for (int i = 0; i < 100000; i++)
	{
		isValid(graphe, ds, edgeThreshold);
	}
	long timeafter = System.currentTimeMillis();
//	System.out.println("Temps des isValid :");//72296 avec distance et 171611 avec distancecarree
//	System.out.println(timeafter - time);
	return ds;
}
}