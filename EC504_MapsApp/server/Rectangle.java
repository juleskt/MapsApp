
public class Rectangle {

	double left;
	double bottom;
	double right;
	double top;
	
	public Rectangle (double left, double bottom, double right, double top) {
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.top = top;
	}
	
	public Rectangle (Rectangle d) {
		this.left = d.getLeft();
		this.right = d.getRight();
		this.bottom = d.getBottom();
		this.top = d.getTop();
	}
	
	// Getters
	public double getBottom() {
		return bottom;
	}

	public double getLeft() {
		return left;
	}

	public double getRight() {
		return right;
	}

	public double getTop() {
		return top;
	}

	public void setTop (double d) {
		top = d;
	}
	
	public void setBottom (double d) {
		bottom = d;
	}
	
	public void setLeft (double d) {
		left = d;
	}
	
	public void setRight (double d) {
		right = d;
	}
	
	
	// If a point is inside this rectangle
	public boolean intersects(IPoint p) {
		double x = p.getX();
		double y = p.getY();
		
		
		return (x >= left) && (x <= right) && (y >= bottom) && (y <= top);
	}
	
	// If a rectangle is completely contained in this rectangle
	public boolean contains (Rectangle r) {
		double rl = r.getLeft();
		double rr = r.getRight();
		
		if (left <= rl && rl <= rr && rr <= right) {
			double rb = r.getBottom();
			double rt = r.getTop();
			
			if (bottom <= rb && rb <= rt && rt <= top) {
				return true;
			}
		}
		
		// nope!
		return false;
	}
	
	// Printable version
	public String toString () {
		return "[" + left + "," + bottom + " : " + right + "," + top + "]";
	}
	

}
