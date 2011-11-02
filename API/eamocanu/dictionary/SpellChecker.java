package eamocanu.dictionary;
/**
 * Experimental spell check API
 *
 * Created by Adrian M in 2011
 *
 * Downloaded from https://github.com/eamocanu/spellcheck.graph/downloads
 * Git repository https://github.com/eamocanu/spellcheck.graph
 */

/* Released under the BSD license
Copyright (c) 2011, Adrian M
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;


/**
 * @author eamocanu
 * 
 * Experimental spell checker.
 *  
 * Creates a dictionary from a file with words. Each word on a separate line.
 * When a given word is given to check if it is correct, the current algorithm
 * builds a graph of generations and checks to see which new nodes lead to words
 * which exist in the dictionary. The algorithm is a modified version of DFS.
 * See the readme file for more info.
 * 
 * Currently it generates words for a maximum depth of 2, but this can be changed.
 */
public class SpellChecker implements SpellCheckerInterface {
	
	/** Maximum depth for DFS */
	final int MAX_DEPTH = 2;
	
	/** Correctly spelled words */ //TODO fix for memory
	private Map<String, String> originalWords;

	/** Holds list of characters which are to be tried when replacing a given character
	 * in the given word to correct */
	private Chars charsMapping;
	
	/** Used for matching phonemes */
	private PhoneticManager phoneticManager;
	
	/** Enables or disables phonetic matching */
	private boolean enablePhoneticMatching;
	
	
	
	/**
	 * @param phoneticMatching	if true, it allow phonetic matching
	 */
	public SpellChecker(boolean phoneticMatching) {
		enablePhoneticMatching= phoneticMatching;
		originalWords= new HashMap<String,String>();
		charsMapping= new Chars();
		phoneticManager= new PhoneticManager();
	}
	
	
	/** Build dictionary from given dictionary file.
	 * The file must contain each word on a separate line 
	 * 
	 * @param path	path and name of dictionary file
	 * @throws FileNotFoundException	if dictionary file not found
	 */
	public void buildDictionary(String path) throws FileNotFoundException {
		Scanner scanner=new Scanner(new File(path));
		
		while (scanner.hasNext()){
			String crtWord= scanner.next();
			originalWords.put(crtWord, crtWord);
		}
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
	 * @return		true if the word to be tested is in dictionary
	 */
	public boolean isCorrectWord(String word){
		return originalWords.containsKey("word");
	}

	
	/**
	 * @param word	misspelled word to start generating words from
	 * @return		list of possible corrections
	 */
	public Collection<String> correctWord(String misspelledWord){
		Set<String> possibleCorrections = new HashSet<String>();

		if (originalWords.isEmpty()) {
			return possibleCorrections;
		}

		if (originalWords.containsKey(misspelledWord)){
			possibleCorrections.add(misspelledWord);
			return possibleCorrections;
		}
		
		int crtDepth= 1;
		String separatorString= "|";
		Stack<String> stack=new Stack<String>();
		stack.push(misspelledWord);
		stack.push(separatorString);
		
		
		if (enablePhoneticMatching){
			possibleCorrections= getPhoneticMatches(misspelledWord);
		}
		
		//modified DFS - non recursive to save stack and gain some speed
		while(!stack.isEmpty()){
			String crtWord= stack.pop();
			
			if (crtWord==separatorString){
				if (stack.isEmpty()) break;
				
				crtWord= stack.pop();//get elem before sep
				stack.push(separatorString);//put sep back in
			}
			
			//2 consecutive separators -> drop one level
			if (crtWord==separatorString) {
				crtDepth--;
				continue;
			}
			
			if (originalWords.containsKey(crtWord)){
				if (isPercentMatch(misspelledWord, crtWord)){
					possibleCorrections.add(crtWord);
				}
				continue; //optional
			}
			
			//add some calculations on probabilities for taking branches
//			if (!takeCurrentBranch(misspelledWord, crtWord)){
//				continue;
//			}
			
			if (crtDepth==MAX_DEPTH +1){
				continue;
			}
			
			//TODO rank results from most significant to least
			List<String> l= generateMisspelledWords(crtWord);
			stack.addAll(l);
			stack.push(separatorString);
			crtDepth++;
		}

		stack=null;
		
		return possibleCorrections;
	}
	
	
	/** Retrieves phonetic matches with the help of a phonetic manager.
	 * The matches are then checked if they are valid dictionary words.
	 * 
	 * Feel free to override to create your own rules for accepting 
	 * phonemes
	 * 
	 * @param word	word string for which to get phonetic matches
	 * @return		set of phonetic matches which are valid words
	 */
	public Set<String> getPhoneticMatches(String word) {
		Set<String> phoneticMatches = new HashSet<String>();
		
		//only if they're in map/dic
		phoneticManager.generatePhoneticMatches(word, 0, phoneticMatches);

		Iterator<String> itr = phoneticMatches.iterator();
		String crtWord = "";
		 while(itr.hasNext()){
			 crtWord = (String)itr.next();
			 if(!originalWords.containsKey(crtWord)){
				 itr.remove();
			 } 
		 }
		 return phoneticMatches;
	}
	
	
	/** Checks if generated word matches original word for some threshold.
	 * 
	 * Feel free to override to create your own rules for accepting 
	 * a word as correct.
	 * 
	 * @param original
	 * @param generated
	 * @return
	 */
	public boolean isPercentMatch(String original, String generated) {
		final int MAX_DIFFERENCE=2;
		//if a word is length 1 (ie I, a) don't allow long words)
		if (original.length()==1 && generated.length()>2) return false;
		if (generated.length()==1 && original.length()>2) return false;
		
		//if original is a substring of generated allow any length of generated 
		if (generated.contains(original)) return true;
		
		Map <Character, Character> originalMap = new HashMap <Character, Character>();
		int counter=0;
		
		for (int i=0; i<original.length(); i++){
			originalMap.put(original.charAt(i), original.charAt(i));
		}
		
		for (int i=0; i<generated.length(); i++){
			if (originalMap.containsKey(generated.charAt(i))){
				counter++;
			}
		}
		
		//insert fancy statistical matching code in here
		if ((generated.length() - counter) < MAX_DIFFERENCE +0) return true;
		
		
		return false;
	}
	
}

