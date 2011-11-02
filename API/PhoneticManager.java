/**
 * Experimental spell check API
 *
 * Created by Adrian M in 2010
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



import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 */

/**
 * @author amocanu
 * Maps sounds of letter groups to phonemes. 
 * 
 * From testing, this is useful only when "text speak" is present in the dictionary
 * If it generates other valid words that are not in the dictionary, this should
 * be disabled.
 */
public class PhoneticManager {
	private Map <String,List<String>> phoneticMapppings = new HashMap <String,List<String>>();
	
	
	public PhoneticManager(){
		addMappings();
	}
		
	
	/** Creates phonetic mappings */
	private void addMappings(){
		add(phoneticMapppings, "oo", "u");
		add(phoneticMapppings, "k", "c");
		add(phoneticMapppings, "y", "i");
		add(phoneticMapppings, "o", "oh", "ou");
		
		add(phoneticMapppings, "u", "oo", "ough", "ue");
		add(phoneticMapppings, "c", "k", "s");
		add(phoneticMapppings, "i", "y");
		add(phoneticMapppings, "oh", "o");
		
		add(phoneticMapppings, "sa", "ca");
		add(phoneticMapppings, "ee", "e", "ea");
		add(phoneticMapppings, "ea", "ee");
		add(phoneticMapppings, "uh", "a");
		add(phoneticMapppings, "ah", "a");
		add(phoneticMapppings, "aw", "o", "a", "oh");
		add(phoneticMapppings, "a", "uh", "ah");
//		add(phoneticMapppings, "ch", "tch");
//		add(phoneticMapppings, "sad", "cad");
		add(phoneticMapppings, "t", "th");
		add(phoneticMapppings, "ou", "o","u");
		
	}
	
	
	private void add(Map <String,List<String>> phoneticMapppings, String key, String... values){
		phoneticMapppings.put(key, Arrays.asList(values));
	}

	/** This method is slow: it is recursive and concatenates String objects.
	 * FIXME
	 * @param s				original string which is to have phonetic matches replaced
	 * @param startIndex	start index in string where replacement starts
	 * @param generations	stored results
	 */
	public void generatePhoneticMatches(String s, int startIndex, Set<String> generations){

		for (int i=startIndex; i<s.length(); i++){
			String prefix= s.substring(0,i);
			String suffix= s.substring(i+1, s.length());
			
			List<String> matches = getPhoneticMatchesFromMap(""+s.charAt(i));
			//replace 1 char w *
			for (String match: matches){
				String newS= prefix +match +suffix;
				generations.add(newS);
				generatePhoneticMatches(newS, i+ match.length(), generations);
			}
			
			if (i+2 > s.length()) continue;
			
			String suffix2= s.substring(i+2, s.length());
			
			matches= getPhoneticMatchesFromMap(s.substring(i,i+2));
			//replace 2 chars w *
			for (String match: matches){
				String newS= prefix +match +suffix2;
				generations.add(newS);
				generatePhoneticMatches(newS, i+ match.length(), generations);
			}
			
		}
		
		
	}
	

	private <T> List<T> getPhoneticMatchesFromMap(String pattern){
		if (phoneticMapppings.containsKey(pattern)){
			return (List<T>) phoneticMapppings.get(pattern);
		}
		
		return Collections.emptyList();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PhoneticManager m=new PhoneticManager();
		
		String w="seayuh";
		Set<String> r= new HashSet<String>();
		m.generatePhoneticMatches(w, 0, r);
		m.printList(r);
	}
	
	
	private void printList(Collection<?> l){
		for (Object n:l){
			System.out.print(n.toString()+" ");
		}
		System.out.println();
	}

}
