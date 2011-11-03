Experimental spell checker v0.2

Creates a dictionary from a file with words. Each word on a separate line.
When a given word is given to check if it is correct, the current algorithm
builds a graph of generations and checks to see which new nodes lead to words
which exist in the dictionary. It generates for a maximum depth of 2. However
this can be changed to be higher based on probability that some branches 
may lead to better words.

If generated word nodes are found in the dictionary, they are stored in a 
list and returned as possible corrections.

If you wish to add in your own probability matching, override isPercentMatch(String, String) in SpellChecker.java
For phonetic matches, override generatePhoneticMatches(..) in PhoneticManager.java



UI version 

Shows parts of the graph of generated words, 
when a correct node is generated, it is stored and later a path is created from the
found node back to the original incorrectly spelled word.



Changes this version

1: Replaced BFS with modified DFS to allow to computer virtually infinitely many words without running out of memory.

2: Added phonetic recognition (useful mostly for slang and text speak)

3: Added some primitive probability matching for some branches to define goodness of fit for given misspelled word



TODOs

TODO: Account for plurals
TODO: Better probability matching
TODO: Rank results from most significat to least
TODO: Speed up generateMisspelledWords(String)
TODO: Speed up generatePhoneticMatches(..)