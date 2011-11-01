Experimental spell checker. 

Creates a dictionary from a file with words. Each word on a separate line.
When a given word is given to check if it is correct, the current algorithm
builds a graph of generations and checks to see which new nodes lead to words
which exist in the dictionary. It generates for a maximum depth of 2.
If such word nodes are found, they are stored in a list and returned as possible
corrections.
In the UI version which shows parts of the graph of generated words, 
when a correct node is generated, it is stored and later a path is created from the
found node back to the original incorrectly spelled word.

The algorithm employed is BFS, but for lower memory consumption DFS would be better
because I can make it discard visited nodes when memory is low.

If you have lots of memory you can change it to generate 2 levels for all words in the
dictionary and then check if the word to correct is in the graph. Store the 
generations in a map for O(1) expected loop up time.


TODO replace string w char [] for fixed allocation
TODO: optimize for speed by using more space: if now O(n) mem, to make it 10-20% faster,
use O(n*m) memory (where m is avg word length) by generating for the original words
new words w space )ie" cat ->  at, c t, and ca . This way I eliminate the one loop 
that right now puts every letter surrounding the key in the place of space 
TODO: Account for plurals
TODO: take into account frequency and phonetic structure of the word
