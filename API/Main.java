import java.io.FileNotFoundException;
import java.util.Collection;

/**
 * @author Adrian
 * 
 * Test driver
 */
public class Main {

	public static void main(String[] args) {
		Dictionary d= new Dictionary();

		try {
			d.buildDictionary("sample dictionary/1000new.txt");

			Collection<String> ls;
			ls = d.correctWord("govrnmet");
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
