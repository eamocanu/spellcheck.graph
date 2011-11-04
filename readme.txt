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



Runtime

Runtime is O(n^depth), where n is word length that is being checked
Memory O(depth*m), where m is the number of generated words 1 character apart from their parent



UI version 

Shows parts of the graph of generated words, 
when a correct node is generated, it is stored and later a path is created from the
found node back to the original incorrectly spelled word.



Changes this version

1: Replaced BFS with modified DFS to allow to computer virtually infinitely many words without running out of memory.

2: Added phonetic recognition (useful mostly for slang and text speak)

3: Added some primitive probability matching for some branches to define goodness of fit for given misspelled word



TODOs

TODO: Account for plurals. Tricky since not all words that end in s are plurals. Might need another list just of nouns
TODO: Better probability matching (utility functions, etc)
TODO: Rank results from most significat to least
TODO: Speed up generateMisspelledWords(String)
TODO: Speed up generatePhoneticMatches(..)



Further Optimizations
When generating new words preallocate all memory for 1 level in the tree at once then fill it up with corresponding chars from original string.

Tried optimizing new word generation with StringBuffer and CharSequence, but they did not improve runtime. See stress test section of this document.



Stress Test Data
All times are in ms. Data obtained from StressTestMain.java

1:
buffer + [] not preallocated

TOOK 22015
turn trust true through run try 

TOOK 22000
turn trust true through run try 

TOOK 22094
turn trust true through run try 

---------------------------------------
2:
strings + linked list

TOOK 24234
turn trust true through run try 

TOOK 24344
turn trust true through run try 

TOOK 24796
turn trust true through run try 

---------------------------------------
3:
strings + preallocd []

TOOK 22734
turn trust true through run try 

TOOK 22828
turn trust true through run try 

TOOK 22797
turn trust true through run try 

---------------------------------------
4:
buffer + preallocated []

TOOK 21703
turn trust true through run try 

TOOK 21532
turn trust true through run try 

TOOK 21578
turn trust true through run try 

---------------------------------------
5:
buffer + char sequence + preallocated []

TOOK 22172
turn trust true through run try 

TOOK 22109
turn trust true through run try 

TOOK 22031
turn trust true through run try 


Even optimized with buffer and char sequences, the speed improvement is not great. I removed such optimizations. 
Currently it uses #3.