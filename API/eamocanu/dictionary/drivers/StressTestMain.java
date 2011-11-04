package eamocanu.dictionary.drivers;
import java.io.FileNotFoundException;
import java.util.Collection;

import eamocanu.dictionary.SpellChecker;

/**
 * @author Adrian
 * 
 * Stress test driver
 */
public class StressTestMain {

	public static void main(String[] args) {
		SpellChecker spellChecker= new SpellChecker(true);

		try {
			spellChecker.buildDictionary("sample dictionary/1000new.txt");

			Collection<String> ls=null;
			long startTime= System.currentTimeMillis();
			for (int i=0; i<1000; i++){
				ls = spellChecker.correctWord("tru");
			}
			long endTime= System.currentTimeMillis();
			System.out.println("TOOK "+ (endTime-startTime));
//			ls = spellChecker.correctWord("ckecker");
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
