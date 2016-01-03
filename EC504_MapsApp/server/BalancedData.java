import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class BalancedData {
	
	ArrayList<Location> points;
	ArrayList<Location> balancedTree;
	public BalancedData(String fileName, String fileOutname) throws IOException {
		Scanner sc = new Scanner(new FileReader(fileName));
		Location test;
		points = new ArrayList<Location>(); // Original data
		balancedTree = new ArrayList<Location>(); // Resorted data
		
		String state = "";
		String county = "";
		double lat=0;
		double lng=0;
		sc.nextLine(); //Discard the header
		
		//Read in the data from the fileName
		while (sc.hasNextLine()) {
				String input = sc.nextLine();
				String split[] = input.split("\\s+");

				if(split.length == 4)
				{
					state = split[0];
					county = split[1];
					lat = Double.parseDouble(split[2]);
					lng = Double.parseDouble(split[3]);

					
				}
				else if(split.length == 5)
				{
					state = split[0];
					county = split[1] + split[2];
					lat = Double.parseDouble(split[3]);
					lng = Double.parseDouble(split[4]);

					
				}
				else if(split.length == 6)
				{
					state = split[0];
					county = split[1] + split[2] +split[3];
					lat = Double.parseDouble(split[4]);
					lng = Double.parseDouble(split[5]);
				}
				
				if (lat != 0 && lng != 0) {
					test = new Location(state, county, lat,lng);
					points.add(test);
				}
				
		}
		System.out.println("Loaded");
		
		// Balance the data
		balance(points, 0);
		
		// Write out the data to the out file
		PrintWriter writer = new PrintWriter(fileOutname, "UTF-8");
		writer.println("STATE_ALPHA	COUNTY_NAME	PRIM_LAT_DEC PRIM_LONG_DEC");
		
		for (Location a : balancedTree) {
			writer.println(a);
		}
		writer.close();
		
	}
	
	
	//Grab the median, alternating every time X and Y
	public void balance(List<Location> list, int axis) {
		if (list.size() == 0)
			return;
		sort(list, axis);
		int median = list.size()/2;
		balancedTree.add(list.get(median));
		
		balance(list.subList(0, median), (axis+1)%2);
		balance(list.subList(median+1, list.size()), (axis+1)%2);
		
	}
	
	// Sort a list based on an axis
	public void sort(List<Location> list, int axis) {
		if (axis == 0) { //Sort on X
			Collections.sort(list, new Comparator<IPoint>() {

				public int compare(IPoint one, IPoint two) {
					double x = one.getX() - two.getX();
					if (x < 0) {
						return -1;
					}
					if (x > 0) {
						return 1;
					}
					
					return 0;
					
				}});
		}
		else { //Sort on Y
			Collections.sort(list, new Comparator<IPoint>() {

				public int compare(IPoint one, IPoint two) {

					double y = one.getY() - two.getY();
					if (y < 0 ) {
						return -1;
					}
					if (y > 0) {
						return 1;
					}
					
					return 0;
					
				}});
		}
	}
	
	/*
	// For our own sanity
	public void sanityCheck(Location target) {
		ArrayList<Location> best = new ArrayList<Location>();
		double d, d2;
		best.add(points.get(0));
		for (int i=1; i< points.size();i++) {
			if (best.size() < 10) {
				best.add(points.get(i));
			}
			else {
				d = distance(target, points.get(i));
				double farthest = distance(target, best.get(0));
				int farthest_index=0;
				
				for (int j = 1; j < best.size(); j++) {
					d2 = distance(target,best.get(j));
					if (d2 > farthest) {
						farthest_index=j;
						farthest = d2;
					}
				}
				if (d >=0 && d < farthest) {
					best.set(farthest_index, points.get(i));
					
				}
			}
			
		}
		for (Location a:best) {
			System.out.println(a+" "+ distance(target,a));
		}
	}
	
	static double distance (IPoint p1, IPoint p2) {
		double x = (p2.getY() - p1.getY()) * Math.cos((p1.getX()+p2.getX())/2);
    	double y = p2.getX()-p1.getX();
    	return Math.sqrt(x*x + y*y) * 6371000;
	}*/
}
