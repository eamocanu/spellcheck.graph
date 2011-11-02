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

package eamocanu.dictionary;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Set;

/**
 * @author Adrian
 *
 */
public interface SpellCheckerInterface {
	
	boolean isCorrectWord(String word);
	Collection<String> correctWord(String misspelledWord);
	Set<String> getPhoneticMatches(String word);
	boolean isPercentMatch(String original, String generated);
	void buildDictionary(String path) throws FileNotFoundException;
	
}
