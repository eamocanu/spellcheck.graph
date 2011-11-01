import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author eamocanu
 * Experimental spell checker. 
 */
public class Dictionary {
	
	//most words are 2 edits away not 3
	final int MAX_DEPTH = 2 +0;
	
	/** Correctly spelled words */
	private Map<String, Node> originalWords;
	
	/** Caches levels generated from dictionary words */
	private List <Map<String,Node>> generatedLevels;
	
	/** Start at level 1 */
	private int crtLevel=1;
		
	/** Edits/generations/levels from currently misspelled word. 
	 * Stores all generated words in this session. No duplicates allowed. */
	private Map<String, Node> crtWordGenerations;
	
	/** Root of all words in dictionary */
	private Node dictionaryRoot;

	/** Holds list of characters which are to be tried when replacing a given character
	 * in the given word to correct */
	private Chars charsMapping;
	
	
	
	public Dictionary() {
		originalWords= new HashMap<String,Node>();
		generatedLevels = new ArrayList<Map<String,Node>>();
		charsMapping= new Chars();
	}
	
	
	/** Build dictionary from given dictionary file.
	 * The file must contain each word on a separate line 
	 * 
	 * @param path	path and name of dictionary file
	 * @throws FileNotFoundException	if dictionary file not found
	 */
	public void buildDictionary(String path) throws FileNotFoundException{
		Scanner scanner=new Scanner(new File(path));
		dictionaryRoot= Node.ROOT_MARKER;
		
		Map<String,Node> firstLevelWords= new HashMap<String,Node>();
		generatedLevels.add(firstLevelWords);
		
		while (scanner.hasNext()){
			String crtWord= scanner.next();
			Node correctNode = new Node(crtWord);
			originalWords.put(crtWord, correctNode);
			dictionaryRoot.addChild(correctNode);
			correctNode.addParent(dictionaryRoot);
			
			//not enough memory to hold generations for all dictionary words
			//with more memory we would generate a few levels for each word
			//in the dictionary, then when having to correct a given word
			//we could look it up in the hashmap of generations
			
//			List <String> misspelledWords= generateMisspelledWords(crtWord);
////			generatedWords.put(crtWord, misspelledWords);
//			
//			for (String editWord:misspelledWords){
//				if (crtWord.equals(editWord)) continue; //no 0-len cycles
//				if (originalWords.containsKey(editWord)) continue;//no orig words dups
//					
//				Node crtChild=new Node(editWord);
//				
//				//if an edit = an original word discard the edit
////				if (originalWords.containsKey(editWord)) continue;
//				
//				if (firstLevelWords.containsKey(editWord)){ //found back edge
//					//if we generated 2 similar nodes (eEs & Ees); if they have the same parent
//					//merge them into one (ie get rid of one); otherwise clump them into one 
//					//and allow edges bw the old one and the new ones parent
//					
//					if (haveSameParent(firstLevelWords.get(editWord),correctNode)) continue;
////					System.out.println(firstLevelWords.get(editWord));
//					crtChild= firstLevelWords.get(editWord);
//				} else {
//					firstLevelWords.put(editWord,crtChild);
//				}
//
//				//we need to know parents - so we know how to go up the tree to correct word
//				crtChild.addParent(correctNode);
//				
//				//bi-directional graph
//				crtChild.addChild(correctNode);
//				correctNode.addChild(crtChild);
//			}
		
		}
		
		
//		generateMappings();
	}


	/** Generates modified words based on original word. 
	 * Generated words are 1 edit distance away.
	 * 
	 * It does character (1) replacement, (2) insertion, (3) removal, and (4) swapping
	 * 
	 * TODO optimize
	 */
	private List<String> generateMisspelledWords(String validWord) {
		List<String> results = new ArrayList<String>();
		if (validWord==null) return results;
		if (validWord.length() == 0) return results;
		
		
		for (int i=0; i<validWord.length(); i++){
			String prefix= validWord.substring(0,i);
			String suffix= validWord.substring(i+1,validWord.length());
			String suffix2= validWord.substring(i,validWord.length());
			
			List<Character> chars = charsMapping.getCharsFor(validWord.charAt(i));
			//replace 1 char
//			for (char ch='a'; ch<='z';ch++){
			for (Character ch:chars){
				String generatedWd= prefix + ch+ suffix;//TODO slow->buffer it
				results.add(generatedWd);
			}
			
			//add insert 1 char (make longer by 1) - inserts as prefix and in mid word
			for (char ch='a'; ch<='z';ch++){
				String generatedWd= prefix + ch + suffix2;
				results.add(generatedWd);
				
				//insert new char as suffix
				generatedWd= validWord + ch;
				results.add(generatedWd);
			}
			
			//remove a char
			String generatedWd= prefix + suffix;//slow
			results.add(generatedWd);
			
			//swap chars
			if (i==0) continue;
			generatedWd= validWord.substring(0,i-1) + validWord.charAt(i) + validWord.charAt(i-1) + validWord.substring(i+1);
			results.add(generatedWd);
		}
		
		results.remove(validWord);
		return results;
	}

	
	/** Checks to see if given word is in the dictionary or not. 
	 * If it is, then it is correct.
	 * 
	 * @param word	word to test
	 * @return	true if the word to be tested is in dictionary
	 */
	public boolean isCorrectWord(String word){
		return originalWords.containsKey("word");
	}

	
	
	
	
	/** Traverses graph of generations in a breadth first search (BFS) fashion 
	 * from given word up to a (or more) correct word parent(s), if any at all.
	 *
	 * TODO
	 * Improvements for memory usage: 
	 * - use depth first search up to 2 levels deep then discard subtree. Keep 
	 * 	 repeating
	 * 
	 * @param word	misspelled word to start generating words from
	 * @return		list of possible corrections
	 */
	public Collection<String> correctWord(String misspelledWord){
		Set<String> possibleCorrections = new HashSet<String>();

		if (originalWords.isEmpty()) {
			return possibleCorrections;
		}

		if (originalWords.containsKey(misspelledWord)){//make comparison based on node.dataString
			possibleCorrections.add(misspelledWord);
			return possibleCorrections;
		}
		
		crtLevel= 1;
		crtWordGenerations= new HashMap<String, Node>();
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
				possibleCorrections.add(crtNode.toString());
				
				//found 1 match or none -> now see if we can find more matches BUT search
				//only this leftover level (that has been generated, but not fully processed
				//go thru the rest of Q and see if any other combs match correct words
				while(!workQ.isEmpty()){
					crtNode = workQ.poll();//maybe should leave it in and still do generations for it
					if (crtNode==separator) continue;
					
					//allows no duplicates in the possible word corrections set
					//this means only one path to given root (correct word) will
					//be printed since in order to allow multiple paths, we
					//need add more root nodes here with the same string data
					//but this is disallowed now by adding equals() and hashCode to Node
					//so hashset knows how uniqueness is defined
					if (originalWords.containsKey(crtNode.toString())){
						possibleCorrections.add(crtNode.toString());
					}
				}
				//Q is empty so it will exit the while loop automatically after this
			}else {
				//crt word is not in dictionary -> continue generating children from it
				List <String> misspelledWords= generateMisspelledWords(crtNode.toString());

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
				possibleCorrections.add(crtNode.toString());
			}
		}
		
		//mark for GC
		workQ=null;
		crtWordGenerations=null;

		return possibleCorrections;
	}
	
	

	
}
