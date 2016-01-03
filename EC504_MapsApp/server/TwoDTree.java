import java.util.ArrayList;

public class TwoDTree {

	//To make things easy, first node splits based on X.
	VerticalNode root = null;
	
	public void insert (IPoint value) {
		if (value == null) {
			throw new IllegalArgumentException ("Unable to insert null value");
		}
		
		if (root == null) {
			root = new VerticalNode(value);
			return;
		}
		
		// Walk down the tree; alternate between horizontal and vertical nodes
		Node node = root;
		Node next;
		do {
			
			if (node.isBelow(value)) {
				next = node.getBelow();
				if (next == null) {
					
					node.setBelow(node.construct(value));
					break;
				} 
				else {
					node = next;
				}
			} 
			else {
				next = node.getAbove();
				if (next == null) {
				
					node.setAbove(node.construct(value));
					break;
				} 
				else {
					node = next;
				}
			}
		} while (node != null);
	}
	

	public VerticalNode getRoot() {
		return root;
	}
	
	
	public Node parent (IPoint value) {
		if (value == null) {
			throw new IllegalArgumentException ("Unable to insert null value into TwoDTree");
		}
		
		if (root == null) return null;
		
		// Walk down the tree; alternate between horizontal and vertical nodes
		Node node = root;
		Node next;
		while (node != null) {
			
			if (node.isBelow(value)) {
				next = node.getBelow();
				if (next == null) {
					return node;
				} 
				else {
					node = next;
				}
			} 
			else {
				next = node.getAbove();
				if (next == null) {
					return node;
				} 
				else {
					node = next;
				}
			}
		}
		
		throw new RuntimeException ("TwoDTree::parent reached invalid location");
	}
	
	// Rectangular distance formula given to us.
	static double distance (IPoint p1, IPoint p2) {
		double x = (p2.getY() - p1.getY()) * Math.cos((p1.getX()+p2.getX())/2);
    	double y = p2.getX()-p1.getX();
    	return Math.sqrt(x*x + y*y) * 6371000;
	}
	
	// Nearest neighbor 
	public IPoint nearest (IPoint target) {
		if (root == null || target == null) return null;
	
		
		Node parent = parent(target);
		IPoint result = parent.point;
		double smallest = distance(target, result);
		
		double best[] = new double[] {smallest }; 
		
		IPoint betterOne = root.nearest (target, best);
		if (betterOne != null) { 
			return betterOne; 
		}
		return result;
	}
	
	// Nearest 10 neighbors
	public ArrayList<IPoint> nearest10 (IPoint target) {
		ArrayList<IPoint> results = new ArrayList<IPoint> ();
		
		if (root == null || target == null) return null;
		
		results = root.nearest10 (target, results);
		return results;
	}

	
	// Search for points that intersect with the given Rectangle.
	public ArrayList<IPoint> search (Rectangle space) {
		ArrayList<IPoint> results = new ArrayList<IPoint> (); 
		
		if (root == null) {
			return results;
		}
		
		// Search, placing results into 'results'.
		root.search(space, results);
		
		return results;
	}

}
