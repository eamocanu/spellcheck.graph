/**
 * Experimental spell check API
 *
 * Created by Adrian M in 2011
 *
 * Downloaded from https://github.com/eamocanu/spellcheck.graph/downloads
 * Git repository https://github.com/eamocanu/spellcheck.graph
 */

/** Copyright (c) 2011, Adrian M
    All rights reserved.
    
Copyright (c) 2001-2009, JGraph Ltd
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list 
of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this 
list of conditions and the following disclaimer in the documentation and/or 
other materials provided with the distribution.
Neither the name of JGraph Ltd nor the names of its contributors may be used 
to endorse or promote products derived from this software without specific prior written permission.
Termination for Patent Action. This License shall terminate
automatically, as will all licenses assigned to you by the copyright
holders of this software, and you may no longer exercise any of the
rights granted to you by this license as of the date you commence an
action, including a cross-claim or counterclaim, against the
copyright holders of this software or any licensee of this software
alleging that any part of the JGraph, JGraphX and/or mxGraph software
libraries infringe a patent. This termination provision shall not
apply for an action alleging patent infringement by combinations of
this software with other software or hardware.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package eamocanu.dictionary.graph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

public class WordGraph extends JPanel {

	private static final long serialVersionUID = -3831788247969123528L;


	protected JGraph graph;


	/** maps node string data to port */
	Map<String, DefaultPort> portMap = new HashMap<String,DefaultPort>();
	
	
	
	public JGraph getGraph(){ return graph; }
	

	// Main Method
	public static void main(String[] args) {
		try {
			// Construct Frame
			JFrame frame = new JFrame("GraphEd");
			// Set Close Operation to Exit
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// Add an Editor Panel
			frame.getContentPane().add(new WordGraph());

			frame.setSize(520, 390);
			// Show Frame
			frame.show();
		}
		catch (Exception e){
			System.out.println("Exception " + e);
		}

	}


	// Construct an Editor Panel
	public WordGraph(){
		// Use Border Layout
		setLayout(new BorderLayout());
		
		// Construct the Graph
		graph = new MyGraph(new MyModel());
//		graph = new MyGraph();
		
		add(new JScrollPane(graph), BorderLayout.CENTER);
	}

	
	public void initToTestData(){
		DefaultPort p1 = insertVertex("1");
		DefaultPort p2 = insertVertex("2");
		DefaultPort p3 = insertVertex("3");

		insertEdge(p1,p2);
		insertEdge(p1,p3);
	}
	
	
	/** Get port (vertex) which has value */
	public DefaultPort getPort(String value){
		if (portMap.containsKey(value)){
			return portMap.get(value);
		} else return null;
	}
	
	
	/** Insert a vertex into this graph. 
	 * If this is a new vertext it creates it then returns it.
	 * If the vertex already exists it just returns it */
	public DefaultPort insertVertex(String value){
		if (portMap.containsKey(value)) return portMap.get(value);

		// Construct Vertex with no Label
		DefaultGraphCell vertex = new DefaultGraphCell(value);
		graph.getGraphLayoutCache().insert(vertex);
		
		// Add one Floating Port
		DefaultPort p = new DefaultPort();

		vertex.add(p);
		portMap.put(value, p);

		// Default Size for the new Vertex
//		Dimension size = new Dimension(25, 25);
		
		// Create a Map that holds the attributes for the Vertex
		Map map = new HashMap();//GraphConstants.createMap();
		
		// Add a Bounds Attribute to the Map
//		GraphConstants.setBounds(map, new Rectangle(point, size));
		
		// Add a Border Color Attribute to the Map
		GraphConstants.setBorderColor(map, Color.black);
		
		// Add a White Background
		GraphConstants.setBackground(map, Color.white);
		
		// Make Vertex Opaque
		GraphConstants.setOpaque(map, true);
		
		// Construct a Map from cells to Maps (for insert)
		Hashtable attributes = new Hashtable();
		
		// Associate the Vertex with its Attributes
		attributes.put(vertex, map);
		
		// Insert the Vertex and its Attributes (can also use model)
		graph.getGraphLayoutCache().insert(
				new Object[]
						{
						vertex
						},
						attributes,
						null,
						null,
						null);

		return p;
	}
	
//	
//	int count=0;
//	// Insert a new Vertex at point
//	private DefaultGraphCell insert(Point point)
//   {
//		// Construct Vertex with no Label
//		DefaultGraphCell vertex = new DefaultGraphCell(count++);
//		// Add one Floating Port
//      
//		DefaultPort p = new DefaultPort();
//       System.out.println("p "  + p);
//      
//		vertex.add(p);
//      
//		// Snap the Point to the Grid
//		point = (Point) graph.snap(new Point(point));
//		// Default Size for the new Vertex
//		Dimension size = new Dimension(25, 25);
//		// Create a Map that holds the attributes for the Vertex
//		Map map = new HashMap();//GraphConstants.createMap();
//		// Add a Bounds Attribute to the Map
//		GraphConstants.setBounds(map, new Rectangle(point, size));
//		// Add a Border Color Attribute to the Map
//		GraphConstants.setBorderColor(map, Color.black);
//		// Add a White Background
//		GraphConstants.setBackground(map, Color.white);
//		// Make Vertex Opaque
//		GraphConstants.setOpaque(map, true);
//		// Construct a Map from cells to Maps (for insert)
//		Hashtable attributes = new Hashtable();
//		// Associate the Vertex with its Attributes
//		attributes.put(vertex, map);
//		// Insert the Vertex and its Attributes (can also use model)
//		graph.getGraphLayoutCache().insert(
//			new Object[]
//         {
//           vertex
//         },
//			attributes,
//			null,
//			null,
//			null);
//         
//      return vertex;
//	}

	
	/** Insert a new Edge between source and target */
	public void insertEdge(Port source, Port target){
		// Connections that will be inserted into the Model
		ConnectionSet cs = new ConnectionSet();
		
		// Construct Edge with no label
		DefaultEdge edge = new DefaultEdge();
		
		// Create Connection between source and target using edge
		cs.connect(edge, source, target);
		
		// Create a Map that holds the attributes for the edge
		Map map = new HashMap();//GraphConstants.createMap();
		
		// Add a Line End Attribute
		GraphConstants.setLineEnd(map, GraphConstants.ARROW_SIMPLE);
		
		// Construct a Map from cells to Maps (for insert)
		Hashtable attributes = new Hashtable();

		// Associate the Edge with its Attributes
		attributes.put(edge, map);

		// Insert the Edge and its Attributes
		graph.getGraphLayoutCache().insert(
				new Object[]
						{
						edge
						},
						attributes,
						cs,
						null,
						null);
	}


	// Returns the total number of cells in a graph
//	protected int getCellCount(JGraph graph)
//   {
//		Object[] cells = graph.getDescendants(graph.getRoots());
//
//	   System.out.println("List of cells: ");
//      for (int i=0; i<cells.length; i++)
//      {
//         System.out.println("  " + cells[i]);
//      }
//		return cells.length;
//	}


	//
	// Custom Graph
	//

	// Defines a Graph that uses the Shift-Button (Instead of the Right
	// Mouse Button, which is Default) to add/remove point to/from an edge.
	public class MyGraph extends JGraph {
		private static final long serialVersionUID = 424431090164775046L;

//		public MyGraph(){
//		}
		
		// Construct the Graph using the Model as its Data Source
		public MyGraph(GraphModel model) {
			super(model);

			// Tell the Graph to Select new Cells upon Insertion
//			setSelectNewCells(true);
			// Make Ports Visible by Default
			setPortsVisible(true);
			// Use the Grid (but don't make it Visible)
			setGridEnabled(true);
			// Set the Grid Size to 10 Pixel
			setGridSize(6);
			// Set the Tolerance to 2 Pixel
			setTolerance(2);
			setPortsVisible(true);
			setMinimumSize(new Dimension(250, 250));
		}
	}

	
	// A Custom Model that does not allow Self-References
	public class MyModel extends DefaultGraphModel {
		private static final long serialVersionUID = 1954516219556027319L;

		public boolean acceptsSource(Object edge, Object port){
			// Source only Valid if not Equal Target
			return (((Edge) edge).getTarget() != port);
		}
		
		public boolean acceptsTarget(Object edge, Object port){
			// Target only Valid if not Equal Source
			return (((Edge) edge).getSource() != port);
		}
	}
	
}

