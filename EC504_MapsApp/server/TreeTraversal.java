public abstract class TreeTraversal {
	
	TwoDTree tree;
	
	protected TreeTraversal() {	}
	
	
	public TreeTraversal(TwoDTree tree) {
		this.tree = tree;
	}

	protected void traverse (Node node) {
		Node next = node.getBelow();
		if (next != null) { 
			traverse (next); 
		}
			
		visit (node);
		
		next = node.getAbove();
		if (next != null) { 
			traverse (next);
		}
	}
	
	
	abstract public void visit(Node node);
	
	
	public final void drain(Node node) {} 
	
	public void traverse () {
		if (tree == null) return;
		
		Node node = tree.getRoot();
		if (node == null) return;
		
		traverse(node);
	}
	
}
