package eamocanu.dictionary.drivers;
import java.io.FileNotFoundException;
import java.util.Collection;

import eamocanu.dictionary.SpellChecker;

/**
 * @author Adrian
 * 
 * Test driver
 */
public class Main {

	public static void main(String[] args) {
		SpellChecker spellChecker= new SpellChecker(true);

		try {
			spellChecker.buildDictionary("sample dictionary/1000new.txt");

			Collection<String> ls;
			ls = spellChecker.correctWord("tru");
			printList(ls);		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void printList(Collection<?> l){
		for (Object n:l){
			System.out.print(n.toString()+" ");
		}
		System.out.println();
	}
}
