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
import java.util.AbstractList;
import java.util.Vector;

/** Graph node
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