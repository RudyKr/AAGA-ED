package algorithms;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class DefaultTeam
{
static int cpt = -1;

/* return true if dominantSet is a dominant set of graphe */
private static boolean isValid (ArrayList<Point> graphe, ArrayList<Point> dominantSet, int edgeThreshold)
{
	int edgeThresholdSquare = edgeThreshold * edgeThreshold;
	for (Point p : graphe)
	{
		if (dominantSet.contains(p))
		{
			continue;
		}
		boolean exists = false;
		for (Point q : dominantSet)
		{
			if (distancecarree(p, q) < edgeThresholdSquare)
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

/* Optimization for not using Point.distance() method */
private static double distancecarree (Point p1, Point p2)
{
	double var1 = p1.getX() - p2.getX();
	double var2 = p1.getY() - p2.getY();
	return var1 * var1 + var2 * var2;
}

/* Function called on app (save on backup every fully completed run) */
public ArrayList<Point> calculDominatingSet (ArrayList<Point> points, int edgeThreshold)
{
	cpt++;
	if (cpt % 100 == 0 && cpt > 0)
		save();
//	return localSearch(points, edgeThreshold);
//	return testValid(points, edgeThreshold);
//	ArrayList<Point> sol = bestLS(points, edgeThreshold);
//	System.out.println("start 3 pour 2 : "+sol.size());
//	return localsearch3for2(sol, points, edgeThreshold);
//	printPB();

//	return improve(points, edgeThreshold, 95, 96, 97, 98, 99);
//	return improve(points, edgeThreshold, 22, 23, 32, 35, 48, 66, 73, 75, 77, 85, 91);
	return result();
//	return bestLS(points, edgeThreshold);
}

/* Run multiples local searches, keep the best of them and write it on a file */
private ArrayList<Point> bestLS (ArrayList<Point> graphe, int edgeThreshold)
{
	long               time   = System.currentTimeMillis();
	ArrayList<Integer> scores = new ArrayList<>();
	ArrayList<Point>   bestDS = readFromFile("records/res" + cpt % 100 + ".points");
	scores.add(bestDS.size());
	System.out.println("records/res" + cpt % 100 + ".points   Score :" + bestDS.size());
	for (int i = 0; i < Math.max((bestDS.size()) / 6, 1); i++)
	{
		ArrayList<Point> local = localSearch(graphe, edgeThreshold);
		scores.add(local.size());
		if (local.size() < bestDS.size())
			bestDS = local;
	}
//	Double avg = scores.stream().mapToInt(val -> val).average());
//	System.out.println(scores);
	System.out.println(avg());
	int pb = Collections.min(scores);
	if (pb < scores.get(0))
	{
		System.out.println(
				  "New high score on instance n°" + cpt % 100 + " ! " + pb + " !!    (Previous one was " + scores.get(0) +
				  ")        Average : " + avg());
	}
//	System.out.println(
//			  "\nFinished : high score :" + bestDS.size() + " \t \t \t scores : " + scores + "      avg : " + avg);
	time -= System.currentTimeMillis();
	System.out.println("Execution time : " + (double) (-time) / 1000 + "sec");
	printToFile("records/res" + Integer.toString(cpt % 100) + ".points", bestDS);
	return bestDS;

}

/* local searching using differents localsearching methods (2for1 , 3for2 , or 4for3 */
private ArrayList<Point> localSearch (ArrayList<Point> graphe, int edgeThreshold)
{
//	long             time = System.currentTimeMillis();
//	int              i    = 0;
	ArrayList<Point> ds = (ArrayList<Point>) graphe.clone();
	while (isValid(graphe, ds, edgeThreshold))
	{
		int sizebefore = ds.size();
//		long timeloop   = System.currentTimeMillis();
		Collections.shuffle(graphe); //instauring some random
		ds = localsearch2for1(ds, graphe, edgeThreshold);
//		ds = localsearch3for2(ds, graphe, edgeThreshold);
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
	//
	int size;
	for (int i = 0; i < 100; i++)
	{
		size = ds.size();
		ds = localsearch3for2(ds, graphe, edgeThreshold);
//		System.out.println(ds.size());
		if (ds.size() == size)
			break;
	}

//	for (int i = 0; i < 100; i++)
//	{
//		size = ds.size();
//		ds = localsearch4for3(ds, graphe, edgeThreshold);
////		System.out.println("   "+ds.size());
//		if (ds.size() == size)
//			break;
//	}
////	System.out.println("      "+ds.size()+".");
	return ds;
}

/* local search by removing 2 points and adding 1 */
private ArrayList<Point> localsearch2for1 (ArrayList<Point> dominantSet, ArrayList<Point> graphe, int edgeThreshold)
{
//	int              i      = 0, j, k;
	ArrayList<Point> rest   = (ArrayList<Point>) graphe.clone();
	double           tooFar = 3.5 * edgeThreshold * edgeThreshold;
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

/* local search by removing 3 points and adding 2 */
private ArrayList<Point> localsearch3for2 (ArrayList<Point> dominantSet, ArrayList<Point> graphe, int edgeThreshold)
{
	ArrayList<Point> rest   = (ArrayList<Point>) graphe.clone();
	double           tooFar = 5.5 * edgeThreshold * edgeThreshold;
	for (int i = 0; i < dominantSet.size(); i++)
	{
		Point ii = dominantSet.get(i);
		for (int j = i + 1; j < dominantSet.size(); j++)
		{
			Point jj = dominantSet.get(j);
			if (distancecarree(ii, jj) > tooFar)
				continue;
			for (int k = j + 1; k < dominantSet.size(); k++)
			{
				Point kk = dominantSet.get(k);
				if (distancecarree(kk, ii) > tooFar || distancecarree(kk, jj) > tooFar)
					continue;
				for (int x = 0; x < rest.size(); x++)
				{
					Point xx = rest.get(x);
					if (distancecarree(xx, ii) > tooFar || distancecarree(xx, jj) > tooFar ||
					    distancecarree(xx, kk) > tooFar || xx.equals(ii) || xx.equals(ii) || xx.equals(jj) || xx.equals(kk))
						continue;
					for (int y = x + 1; y < rest.size(); y++)
					{
						Point yy = rest.get(y);
						if (distancecarree(yy, ii) > tooFar || distancecarree(yy, jj) > tooFar ||
						    distancecarree(yy, kk) > tooFar || distancecarree(yy, xx) > tooFar || yy.equals(ii) ||
						    yy.equals(ii) || yy.equals(jj) || yy.equals(kk))
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
//	System.out.println("3 pour 2 terminé (down) " + dominantSet.size());
	return dominantSet;
}

/* local search by removing 4 points and adding 3 */
private ArrayList<Point> localsearch4for3 (ArrayList<Point> dominantSet, ArrayList<Point> graphe, int edgeThreshold)
{
	ArrayList<Point> rest = (ArrayList<Point>) graphe.clone();
//	System.out.println(rest.size());
//	rest.removeAll(dominantSet);
//	System.out.println(rest.size());
	double tooFar = 6 * edgeThreshold * edgeThreshold;
	for (int i = 0; i < dominantSet.size(); i++)
	{
		Point ii = dominantSet.get(i);
		for (int j = i + 1; j < dominantSet.size(); j++)
		{
			Point jj = dominantSet.get(j);
			if (distancecarree(ii, jj) > tooFar)
				continue;
			for (int k = j + 1; k < dominantSet.size(); k++)
			{
//				if (k < i || k < j)
//					continue;
				Point kk = dominantSet.get(k);
				if (distancecarree(kk, ii) > tooFar || distancecarree(kk, jj) > tooFar)
					continue;
				for (int l = k + 1; l < dominantSet.size(); l++)
				{
					Point ll = dominantSet.get(l);
					if (distancecarree(ll, ii) > tooFar || distancecarree(ll, jj) > tooFar ||
					    distancecarree(ll, kk) > tooFar)
						continue;
					for (int x = 0; x < rest.size(); x++)
					{
						Point xx = rest.get(x);
						if (distancecarree(xx, ii) > tooFar || distancecarree(xx, jj) > tooFar ||
						    distancecarree(xx, kk) > tooFar || distancecarree(xx, ll) > tooFar || xx.equals(ii) ||
						    xx.equals(ii) || xx.equals(jj) || xx.equals(kk) || xx.equals(ll))
							continue;
						for (int y = x + 1; y < rest.size(); y++)
						{
							Point yy = rest.get(y);
							if (distancecarree(yy, ii) > tooFar || distancecarree(yy, jj) > tooFar ||
							    distancecarree(yy, kk) > tooFar || distancecarree(yy, ll) > tooFar ||
							    distancecarree(yy, xx) > tooFar || yy.equals(ii) || yy.equals(ii) || yy.equals(jj) ||
							    yy.equals(kk) || yy.equals(ll))
								continue;
							for (int z = y + 1; z < rest.size(); z++)
							{
								Point zz = rest.get(y);
								if (distancecarree(zz, ii) > tooFar || distancecarree(zz, jj) > tooFar ||
								    distancecarree(zz, kk) > tooFar || distancecarree(zz, ll) > tooFar ||
								    distancecarree(zz, xx) > tooFar || distancecarree(zz, yy) > tooFar || zz.equals(ii) ||
								    zz.equals(jj) || zz.equals(kk) || zz.equals(ll))
									continue;
								ArrayList<Point> dsClone = (ArrayList<Point>) dominantSet.clone();
								dsClone.remove(ii);
								dsClone.remove(jj);
								dsClone.remove(kk);
								dsClone.remove(ll);
								dsClone.add(xx);
								dsClone.add(yy);
								dsClone.add(zz);
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
		}
	}
//	System.out.println("3 pour 2 terminé (down) " + dominantSet.size());
	return dominantSet;
}

/* return the personnal best score */
private ArrayList<Point> result ()
{
	return readFromFile("records/res" + cpt % 100 + ".points");
}

/* Improve only the instances specified in index */
private ArrayList<Point> improve (ArrayList<Point> graphe, int edgeThreshold, int... index)
{
	ArrayList<Integer> toImprove = new ArrayList<Integer>();
	for (int i : index)
	{
		toImprove.add(i);
	}
	if (toImprove.contains(cpt))
	{
		System.out.println("Start improving instance " + cpt);
		return bestLS(graphe, edgeThreshold);
	}
	else
		return readFromFile("records/res" + cpt % 100 + ".points");
}

/* save on backup best high scores */
private void save ()
{
	for (int i = 0; i < 100; i++)
	{
		printToFile("records/backup/res" + i + ".points", readFromFile("records/res" + i + ".points"));
	}
	System.out.println("Scores saved");
}

/* Print matrix of best scores and stats */
private void printPB ()
{
	int min = 1000, max = 0;
	for (int i = 0; i < 100; i++)
	{
		int i1 = readFromFile("records/res" + i + ".points").size();
		if (i % 10 == 0)
			System.out.println("\n");
		System.out.print("  " + i1 + "  ");
		if (min > i1)
			min = i1;
		if (max < i1)
			max = i1;
	}
	System.out.println("\n");
	System.out.println("Min : " + min);
	System.out.println("Max : " + max);
	System.out.println("Avg : " + avg());
}

/* Print average score */
private double avg ()
{
	double avg = 0;
	for (int i = 0; i < 100; i++)
		avg += readFromFile("records/res" + i + ".points").size();
	return avg / 100;
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
		System.err.println(filename + "Input file not found.");
	}
	return points;
}

/* Function for benchmarking isValid's function optimizations */
private ArrayList<Point> testValid (ArrayList<Point> graphe, int edgeThreshold)
{
	ArrayList<Point> ds   = localSearch(graphe, edgeThreshold);
	long             time = System.currentTimeMillis();
	for (int i = 0; i < 100000; i++)
	{
		isValid(graphe, ds, edgeThreshold);
	}
	long timeafter = System.currentTimeMillis();
	System.out.println("Temps des isValid : ");//72296 avec Point.distance(Point) et 171611 avec distancecarree
	System.out.println(timeafter - time);
	return ds;
}
}