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

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jgraph.graph.DefaultPort;

import com.jgraph.layout.demo.JGraphLayoutPanel;

import eamocanu.dictionary.Chars;
import eamocanu.dictionary.SpellCheckerInterface;
/**
 * @author Adrian
 * 
 * Displays the generated graph of words.
 */
public class VisualSpellChecker implements SpellCheckerInterface {
	/** Default maximum depth for DFS */
	final int MAX_DEPTH = 2;

	/** Correctly spelled words */ //TODO fix for memory
	private Map<String, Node> originalWords;

	/** Holds list of characters which are to be tried when replacing a given character
	 * in the given word to correct */
	private Chars charsMapping;

	private WordGraph grf;

	
	
	
	public VisualSpellChecker() {
		originalWords= new HashMap<String,Node>();

		charsMapping= new Chars();

	}


	/* (non-Javadoc)
	 * @see eamocanu.dictionary.SpellCheckerInterface#buildDictionary(java.lang.String)
	 */
	@Override
	public void buildDictionary(String path) throws FileNotFoundException {
		Scanner scanner=new Scanner(new File(path));

		while (scanner.hasNext()){
			String crtWord= scanner.next();
			originalWords.put(crtWord, new Node(crtWord));
		}
	}


	/* (non-Javadoc)
	 * @see eamocanu.dictionary.SpellCheckerInterface#correctWord(java.lang.String)
	 */
	@Override
	public Collection<String> correctWord(String misspelledWord) {
		grf=new WordGraph();
		int crtLevel=1;

		Set<Node> possibleCorrectionsNodes = new HashSet<Node>();

		if (originalWords.isEmpty()) {
			return Collections.emptyList();
		}

		if (originalWords.containsKey(misspelledWord)){//make comparison based on node.dataString
			possibleCorrectionsNodes.add(new Node(misspelledWord));
			return Collections.emptyList();
		}

		Map<String, Node> crtWordGenerations= new HashMap<String, Node>();
		AbstractQueue <Node> workQ = new LinkedBlockingQueue<Node>();
		Node nodeToBeCorrected= new Node(misspelledWord);
		workQ.add(nodeToBeCorrected);
		final Node separator = new Node(""); 
		workQ.add(separator);

		//move to method XXX
		while(!workQ.isEmpty() && crtLevel<MAX_DEPTH +1){
			Node crtNode = workQ.poll();

			if (crtNode==separator){
				workQ.offer(crtNode);
				crtLevel++;//generated a new level but it has not checked it yet here
				continue;
			}

			//allow no dups: aggregate them into the same 1 node
			if(crtWordGenerations.containsKey(crtNode.toString())) continue;

			crtWordGenerations.put(crtNode.toString(), crtNode);

			//crt word in dictionary
			if (originalWords.containsKey(crtNode.toString())) {
				possibleCorrectionsNodes.add(crtNode);

				//found 1 match or none -> now see if we can find more matches BUT search
				//only this leftover level (that has been generated, but not fully processed
				//go thru the rest of Q and see if any other combs match correct words
				while(!workQ.isEmpty()){
					crtNode = workQ.poll();
					if (crtNode==separator) continue;

					//allows no duplicates in the possible word corrections set
					//this means only one path to given root (correct word) will
					//be printed since in order to allow multiple paths, we
					//need add more root nodes here with the same string data
					//but this is disallowed now by adding equals() and hashCode to Node
					//so hashset knows how uniqueness is defined
					if (originalWords.containsKey(crtNode.toString())){
						possibleCorrectionsNodes.add(crtNode);
					}
				}
				//Q is empty so it will exit the while loop automatically after this
			}else {
				//crt word is not in dictionary -> continue generating children from it
				List <String> misspelledWords= generateModifiedWords(crtNode.toString());

				//link each newly generated child to its parent
				for (String editWord:misspelledWords){
					Node crtChild= new Node(editWord);
					workQ.offer(crtChild);

					crtChild.addParent(crtNode);//for graph backwards traversal
					crtChild.addChild(crtNode);
					crtNode.addChild(crtChild);
				}//for
			}//else
		}//while

		//TODO externalize to method(workQ, originalWords, possibleCorrections) 
		//add rest leftover words that were generated and still need be checked
		while(!workQ.isEmpty()){
			Node crtNode = workQ.poll();
			if (crtNode==separator) continue;

			//allows no duplicates in the possible word corrections set
			//this means only one path to given root (correct word) will
			//be printed since in order to allow multiple paths, we
			//need add more root nodes here with the same string data
			//but this is disallowed now by adding equals() and hashCode to Node
			//so hashset knows how uniqueness is defined
			if (originalWords.containsKey(crtNode.toString())){
				possibleCorrectionsNodes.add(crtNode);
			}
		}

		//mark for GC
		workQ=null;
		crtWordGenerations=null;

		//create graph trees for found words
		for (Node crtWordNode: possibleCorrectionsNodes){
			createPathTree(crtWordNode, misspelledWord);
		}

		return Collections.emptyList();
	}


	/* (non-Javadoc)
	 * @see eamocanu.dictionary.SpellCheckerInterface#isCorrectWord(java.lang.String)
	 */
	@Override
	public boolean isCorrectWord(String word) {
		return originalWords.containsKey("word");
	}


	/** Generates modified words based on original word. 
	 * Generated words are 1 edit distance away.
	 * 
	 * It does character (1) replacement, (2) insertion, (3) removal, and (4) swapping
	 * 
	 * TODO optimize all string allocation and concats by preallocating a big byte array
	 * 
	 * Currently optimized by preallocating array list.
	 * StringBuffer and CharSequences have very slightly improved performance thus they
	 * have been removed
	 */
	private List<String> generateModifiedWords(String word) {
		//preallocate entire array
		List<String> results = new ArrayList<String>(word.length()*(9 + 'z'-'a'+1)*2 +1 +1);
		//		String[] res=new String[word.length()*(9 + 'z'-'a'+1)*2 +1 +1];

		if (word==null) return results;
		if (word.length() == 0) return results;

		StringBuffer newWord= new StringBuffer();

		for (int i=0; i<word.length(); i++){
			String prefix= word.substring(0,i);
			String suffix= word.substring(i+1,word.length());
			String suffix2= word.substring(i,word.length());

			List<Character> chars = charsMapping.getCharsFor(word.charAt(i));
			//replace 1 char
			for (Character ch:chars){
				String generatedWd= prefix + ch+ suffix;//TODO slow
				results.add(generatedWd);
			}

			//add insert 1 char (make longer by 1) - inserts as prefix and in mid word
			for (char ch='a'; ch<='z';ch++){
				String generatedWd= prefix + ch + suffix2;//TODO slow
				results.add(generatedWd);

				//insert new char as suffix
				generatedWd= word + ch;
				results.add(generatedWd);
			}

			//remove a char
			String generatedWd= prefix + suffix;
			results.add(generatedWd);
			newWord.append(prefix);

			//swap chars
			if (i==0) continue;
			generatedWd= word.substring(0,i-1) + word.charAt(i) + word.charAt(i-1) + word.substring(i+1);
			results.add(generatedWd);
		}

		//don't allow back edges to parent node
		results.remove(word);

		return results;
	}




	/** For each word node (which is assumed to be leaf, it traverses
	 * up the tree to its parent. After last parent is reached, it is 
	 * topped by another parent with value given by param word
	 * 
	 * @param aSourceNode	current leaf where search starts at
	 * @param parentOfAll	last parent to be added on top of whatever parents are found
	 * 						by traversing 'up' from aNode
	 */
	private void createPathTree(Node aSourceNode, String parentOfAll){
		if(aSourceNode.getParents()!=Node.EMPTY_LIST){
			Node parent = aSourceNode.getParents().get(0);

			if (!grf.getGraph().getModel().contains(parent)){
				grf.insertVertex(parent.toString());
			}
			if (!grf.getGraph().getModel().contains(aSourceNode.toString())){
				grf.insertVertex(aSourceNode.toString());
			}

			DefaultPort crtChildPort = grf.getPort(parent.toString());
			DefaultPort crtPort = grf.getPort(aSourceNode.toString());

			grf.insertEdge(crtChildPort, crtPort);

			createPathTree(parent, parentOfAll);
		}//if
		else {
			//add last parent - the misspelled word
			if (!grf.getGraph().getModel().contains(parentOfAll)){
				grf.insertVertex(parentOfAll);
			}
		}
	}

	
	public WordGraph getGraph(){ return grf; }
	
	
}
