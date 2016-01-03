
// Splits the plane horizontally; IE, uses the Y value
public class HorizontalNode extends Node {
	
	public HorizontalNode(IPoint point) {
		super(point.getY(), point);
	}
	
	public boolean isVertical() {
		return false;
	}
	
	public Node construct(IPoint value) {
		return new VerticalNode (value);
	}
	
	
	// Split the region based on this node
	protected void split (Node child, boolean above) {
		
		child.region = new Rectangle (region);
		if (above) {
			child.region.setBottom(coord);
		} else {
			child.region.setTop(coord);
		}
	}

	// Range comparison
	protected boolean inBelowRange(Rectangle r) {
		return r.getBottom() < coord;

	}
	
	// Range comparison
	protected boolean inAboveRange(Rectangle r) {
		return r.getTop() > coord;
		
	}
	
	// Range comparison
	public boolean isBelow(IPoint point) {
		return point.getY() < coord;
	}

	
	// Distance from this node to a target solely on the Y axis
	double perpendicularDistance(IPoint target) {
		return Math.abs(coord - target.getY())*6371000;
	}
}
