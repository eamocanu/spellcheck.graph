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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author eamocanu
 * Maps qwerty keys to neighbouring keys.
 */
public class Chars {

	static Map<Character, List<Character>> neighbours;
	public final static int NUM_LETTERS=26;
	
	//kbd top row
	Character [] qs= {'w','a','1','2'};
	Character [] ws= {'q','e','s','a','2','3'};
	Character [] es= {'w','r','s','d','3','4'};
	Character [] rs= {'e','t','d','f','4','5'};
	Character [] ts= {'r','y','g','f','5','6'};
	Character [] ys= {'t','u','g','h','7','6'};
	Character [] us= {'y','i','h','j','7','8'};
	Character [] is= {'u','o','j','k','8','9'};
	Character [] os= {'i','p','k','l','9','0'};
	Character [] ps= {'o','[','0','-','l',';'};
	
	//kbd mid row
	Character [] as= {'q','s','z','w'};
	Character [] ss= {'a','d','w','e','x','z'};
	Character [] ds= {'s','f','e','r','x','c'};
	Character [] fs= {'d','g','r','t','c','v'};
	Character [] gs= {'f','h','t','y','v','b'};
	Character [] hs= {'g','j','y','u','b','n'};
	Character [] js= {'h','k','u','i','n','m'};
	Character [] ks= {'j','l','i','o','m',','};
	Character [] ls= {'k',';','o','p',',','.'};
	
	//kbd bot row
	Character [] zs= {'a','s','x'};
	Character [] xs= {'z','c','s','d'};
	Character [] cs= {'x','v','d','f'};
	Character [] vs= {'c','b','f','g'};
	Character [] bs= {'v','n','g','h'};
	Character [] ns= {'b','m','h','j'};
	Character [] ms= {'n',',','j','k'};
	

	public Chars() {
		neighbours = new HashMap<Character, List<Character>>();
		init();
	}
	

	private void init() {
		add('q',qs);
		add('w',ws);
		add('e',es);
		add('r',rs);
		add('t',ts);
		add('y',ys);
		add('u',us);
		add('i',is);
		add('o',os);
		add('p',ps);
		
		add('a',as);
		add('s',ss);
		add('d',ds);
		add('f',fs);
		add('g',gs);
		add('h',hs);
		add('j',js);
		add('k',ks);
		add('l',ls);
		
		add('z',zs);
		add('x',xs);
		add('c',cs);
		add('v',vs);
		add('b',bs);
		add('n',ns);
		add('m',ms);
	}

	
	private void add(char ch, Character [] a){
		List <Character> l= Arrays.asList(a);
		neighbours.put(ch, l);
	}
	

	/** Get neighbouring keys for a given key.
	 * 
	 * @param ch	the corresponding character for key on qwerty keyboard 
	 * 				for which to get the neighbouring key characters
	 * @return		list of neighbouring key characters
	 */
	public List<Character> getCharsFor(char ch) {
		if (neighbours.containsKey(ch)){
			return neighbours.get(ch);
		}
		return Collections.EMPTY_LIST;
	}

}
