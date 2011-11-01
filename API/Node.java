import java.util.AbstractList;
import java.util.Vector;

/** 
 * @author eamocanu
 * 
 * Used for graphs or tracing the entire path for a derived word.
 *
 * Graph node. All graph nodes are children with respect to other graph nodes.
 * If A is a child of B then B is a child of A.
 * 
 * However, if traversing the graph in such a way that we are interested
 * in visiting only new nodes, we can use addParent(..) and getParents() to ensure 
 * that only new nodes nodes are visited.
 * If we come with the visiting algorithm from A to B, then A is the parent of B. 
 * B is not the parent of A.
 * */
class Node {//extends DefaultMutableTreeNode{
		String wd;
		public final static Node ROOT_MARKER= new Node("R");
		public static final Node EMPTY_NODE = new Node("E");
		public static final AbstractList<Node> EMPTY_LIST= new Vector<Node>();
		Vector<Node> children;
		Vector<Node> parents;
		
		
		
		public Node(String wd){
//			super(wd);
			this.wd=wd;
		}
	
		
		public String getData(){ return wd; }
		public AbstractList<Node> getParents(){
			if (parents==null) return EMPTY_LIST;
			return parents; 
		}		
		public AbstractList<Node> getChildren(){
			if (children==null) return EMPTY_LIST;
			//should actually make a deep copy for security purposes
			return children;
		}
		
		
		public void addChild(Node child){
			if (children==null){
				children= new Vector<Node>();
			}
			children.add(child);
		}
		
		
		public void addParent(Node child){
			if (parents==null){
				parents= new Vector<Node>();
			}
			parents.add(child);
		}
		
		
		@Override
		public String toString(){ return this.wd; }
		
		@Override
		public int hashCode(){ return this.wd.hashCode(); }
		
		@Override
		public boolean equals(Object o){
			if (!(o instanceof Node)) return false;
			Node that=(Node)o;
			return this.wd.equals(that.wd); 
		}
		
	}
