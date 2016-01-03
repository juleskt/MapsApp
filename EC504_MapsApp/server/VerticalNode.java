

public class VerticalNode extends Node {

	
	public VerticalNode(IPoint point) {
		super (point.getX(), point);
	}

	public boolean isVertical() {
		return true;
	}


	public Node construct(IPoint value) {
		return new HorizontalNode (value);
	}

	
	// Split the region based on this node
	protected void split (Node child, boolean above) {
		
		child.region = new Rectangle (region);
		if (above) {
			child.region.setLeft(coord);
		} else {
			child.region.setRight(coord);
		}
	}
	
	
	// Range comparison
	protected boolean inBelowRange(Rectangle r) {
		return r.getLeft() < coord;
	}
	
	
	//Range comparison
	protected boolean inAboveRange(Rectangle r) {
		return r.getRight() > coord;
	}
	
	// Range comparison
	public boolean isBelow(IPoint point) {
		return point.getX() < coord;
	}

	// Distance from this node to a target solely on the X axis
	double perpendicularDistance(IPoint target) {
		return Math.abs(coord - target.getX())*6371000;
	}
}
