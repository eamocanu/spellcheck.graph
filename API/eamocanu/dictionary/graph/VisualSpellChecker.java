/**
 * Experimental spell check API
 *
 * Created by Adrian M in 2011
 *
 * Downloaded from https://github.com/eamocanu/spellcheck.graph/downloads
 * Git repository https://github.com/eamocanu/spellcheck.graph
 */

/* Released under the LGPL license. I have no choice bc it uses LGPL graphing libraries.
Copyright (c) 2011, Adrian M
All rights reserved.
................
*/

package eamocanu.dictionary.graph;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Set;

import eamocanu.dictionary.SpellCheckerInterface;

/**
 * @author Adrian
 * 
 * Displays the generated graph of words.
 */
public class VisualSpellChecker implements SpellCheckerInterface {

	/* (non-Javadoc)
	 * @see eamocanu.dictionary.SpellCheckerInterface#buildDictionary(java.lang.String)
	 */
	@Override
	public void buildDictionary(String path) throws FileNotFoundException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eamocanu.dictionary.SpellCheckerInterface#correctWord(java.lang.String)
	 */
	@Override
	public Collection<String> correctWord(String misspelledWord) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eamocanu.dictionary.SpellCheckerInterface#getPhoneticMatches(java.lang.String)
	 */
	@Override
	public Set<String> getPhoneticMatches(String word) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eamocanu.dictionary.SpellCheckerInterface#isCorrectWord(java.lang.String)
	 */
	@Override
	public boolean isCorrectWord(String word) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see eamocanu.dictionary.SpellCheckerInterface#isPercentMatch(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isPercentMatch(String original, String generated) {
		// TODO Auto-generated method stub
		return false;
	}

}
