import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class Node {
	
	public final double coord;
	public final IPoint point; //Point of this node
	Rectangle region; //Region that this node is associated with
	Node below; // Child < node
	Node above; // Child > node
	
	public Node (double coord, IPoint point) {
		this.coord = coord;
		this.point = point;
		
		this.region = new Rectangle (Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	// Getters 
	public Node getBelow() {
		return below;
	}

	public Node getAbove() {
		return above;
	}
	
	public Rectangle getRegion() {
		return region;
	}
		
	
	// Set the below and above
	public void setBelow(Node node) {
		if (node == null) { 
			this.below = null; 
			return; 
		}
		
		if (node.isVertical() == isVertical()) {
			throw new IllegalArgumentException ("Can only set as children nodes whose isVertical() is different.");
		}
		
		this.below = node;
		split(this.below, false);
	}
	
	
	public void setAbove(Node node) {
		if (node == null) { 
			this.above = null; 
			return; 
		}
		
		if (node.isVertical() == isVertical()) {
			throw new IllegalArgumentException ("Can only set as children nodes whose isVertical() is different.");
		}
		
		this.above = node;
		split(this.above, true);
	}
	
	
	// Implementation varies based on HoriztonalNode or Vertical Node
	public abstract boolean isVertical();
	public abstract boolean isBelow (IPoint point);
	protected abstract void split (Node child, boolean above);
	abstract double perpendicularDistance(IPoint target);
	public abstract Node construct(IPoint value);
	protected abstract boolean inBelowRange(Rectangle r);
	protected abstract boolean inAboveRange(Rectangle r);
	
	// See if this point is closer to the target than the distance stored in min[]
	IPoint nearest (IPoint target, double min[]) {
		IPoint result = null;
		
		// If it's closer to the current min, update
		double d = TwoDTree.distance(target, point);
		if (d >= 0 && d < min[0]) {
			min[0] = d;
			result = point;
		}
		
		double dp = perpendicularDistance(target);
		IPoint newResult = null;

		//If points can be on the other side, we must search into both branches.
		if (dp < min[0]) {
			if (above != null) {
				newResult = above.nearest (target, min); 
				if (newResult != null) { result = newResult; }
			}
			
			if (below != null) {
				newResult = below.nearest(target, min);
				if (newResult != null) { result = newResult; }
			}
		} 
		else {
			//Otherwise, search the one you need
			if ((isVertical() && target.getX() < coord) ||
			    (!isVertical() && target.getY() < coord)) {
				if (below != null) {
					newResult = below.nearest (target, min); 
				}
			} 
			else {
				if (above != null) {
					newResult = above.nearest (target, min); 
				}
			}
			
			if (newResult != null) { 
				return newResult;
			}
		}
		return result;
	}

	// See if this point is one of the 10th nearest neighbors to target compared to min
	ArrayList<IPoint> nearest10 (IPoint target, ArrayList<IPoint> min) {
		ArrayList<IPoint> results = new ArrayList<IPoint>();
		ArrayList<IPoint> newResults = new ArrayList<IPoint>();
		
		double d = TwoDTree.distance(target, point);
		
		// If we don't have 10 nearest neighbors yet, collect 10
		if (min.size() < 10) {
			min.add(point);
			results = new ArrayList<IPoint>(min);
			
			// Travel both branches to see if we can get better results
			if (below != null) {
				newResults = below.nearest10(target, min);
				if (!newResults.isEmpty() && (newResults.size() > results. size() || maxDistance(target, newResults) < maxDistance(target,results))) { 
					results = new ArrayList<IPoint>(newResults); 
					}
			}
			if (above != null) {
				newResults = above.nearest10 (target, min); 
				if (!newResults.isEmpty() && (newResults.size() > results. size() || maxDistance(target, newResults) < maxDistance(target,results))) { 
					results = new ArrayList<IPoint>(newResults); 
				}
			}

		}
		else {
			// Get the current farthest distance of the current 10 neighbors
			double farthest = TwoDTree.distance(target, min.get(0));
			int farthest_index=0;
			for (int i = 1; i < min.size(); i++) {
				double checkD = TwoDTree.distance(target, min.get(i));
				if (checkD > farthest) {
					farthest_index=i;
					farthest = checkD;
				}
			}
			
			//If this point is closer than that farthest one, kick it out and replace it with this node
			if (d >=0 && d < farthest) {
				min.set(farthest_index, point);
				farthest = d;
				results = new ArrayList<IPoint>(min);
			}
			

			double dp = perpendicularDistance(target);
			
			//See if 
			if (dp < farthest) {
				// There can be points in the other branch that are closer, so travel both branches
				if (above != null) {
					newResults = above.nearest10 (target, min); 
					if (!newResults.isEmpty() && (newResults.size() > results. size() || maxDistance(target, newResults) < maxDistance(target,results))) { 
						results = new ArrayList<IPoint>(newResults);
						}
				}
				
				if (below != null) {
					newResults = below.nearest10(target, min);
					if (!newResults.isEmpty() && (newResults.size() > results. size() || maxDistance(target, newResults) < maxDistance(target,results))) { 
						results = new ArrayList<IPoint>(newResults);; }
				}
			} else {
				// Other wise go through one
				if ((isVertical() && target.getX() < coord) ||
				    (!isVertical() && target.getY() < coord)) {
					if (below != null) {
						newResults = below.nearest10(target, min); 
					}
				} else {
					if (above != null) {
						newResults = above.nearest10 (target, min); 
					}
				}
				
				// Use smaller result, if found.
				if (!newResults.isEmpty() && (newResults.size() > results. size() || maxDistance(target, newResults) < maxDistance(target,results))) { 
					return newResults; }
			}
		}
		return results;
		
		
	}

	// Get the farthest distance from target in points
	public double maxDistance(IPoint target, ArrayList<IPoint> points) {
		double farthest = TwoDTree.distance(target, points.get(0));
		for (int i = 1; i < points.size(); i++) {
			double checkD = TwoDTree.distance(target, points.get(i));
			if (checkD > farthest) {
				farthest = checkD;
			}
		}
		
		return farthest;
	}
	

	public void search (Rectangle space, ArrayList<IPoint> results) {
		// If the entire region defined by the node is contained in the rectangle...
		if (space.contains(region)) {
			this.drain(results); // Grab all the children on this node!
			return;
		}
		
		//If the point is in the rectangle, add it!
		if (space.intersects (point)) {
			results.add(point);
		}
		
		//Look to see which point is in the rectangle
		if (inBelowRange(space)) {
			if (below != null) { below.search(space, results); }
		}
		if (inAboveRange(space)) {
			if (above != null) { above.search(space, results); }
		}
	}
	

	public void search (Rectangle space, TreeTraversal visitor) {

		// If the entire region defined by the node is contained in the rectangle...
		if (space.contains(region)) {
			this.drain(visitor); // Grab all the children on this node!
			return;
		}
		
		// Is this point in the rectangle?
		if (space.intersects (point)) {
			visitor.visit(this);
		}
		
		// Search the other children that could be in the rectangle.
		if (inBelowRange(space)) {
			if (below != null) {
				below.search(space, visitor); 
			}
		}
		if (inAboveRange(space)) {
			if (above != null) { 
				above.search(space, visitor);
			}
		}
	}
	

	
	// Helper method to record all descendant nodes in the tree rooted at given node. 
	private void drain(ArrayList<IPoint> results) {
		if (below != null) { 
			below.drain (results); 
		}
		results.add(point);
		if (above != null) { 
			above.drain (results);
		}
	}
	
	// Helper method to visit all descendant nodes in the tree rooted at given node. 
	private void drain(TreeTraversal visitor) {
		if (below != null) { 
			below.drain (visitor);
		}
		visitor.drain(this);
		if (above != null) { 
			above.drain (visitor); 
		}
	}
	
}
