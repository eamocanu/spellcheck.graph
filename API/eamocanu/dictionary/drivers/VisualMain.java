package eamocanu.dictionary.drivers;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import eamocanu.dictionary.SpellChecker;


/**
 * @author amocanu
 * Test driver. 
 * 
 * Type in some letters and see real time spell suggestions.
 * The longer the word the slower the suggestions appear. The fix is to move
 * the suggestion generating to a new thread.
 */
public class VisualMain extends JPanel {
	private static final long serialVersionUID = 6364349005495411834L;
	private JTextField dataInputField;
	private JLabel correctOutputField;
	private SpellChecker spellChecker;
	
	
	public VisualMain(){

		try {
			spellChecker= new SpellChecker(true);
			spellChecker.buildDictionary("sample dictionary/1000new.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		init();
		this.add(dataInputField);
		this.add(correctOutputField);
		this.setSize(500, 500);
		setPreferredSize(new Dimension(500,500));
		setMinimumSize(new Dimension(500,500));
		
		
	}
	
	
	public void init(){
		dataInputField= new JTextField("                   ");
		correctOutputField= new JLabel("                   ");
		
		dataInputField.setSize(100, 20);
		correctOutputField.setSize(100, 20);
		
		dataInputField.addActionListener(new WordCorrectorListener());
		dataInputField.addKeyListener(new KeyWordListener());
	}
	
	class KeyWordListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			String text = dataInputField.getText();
			if (text.length()<2) return;
			
			Collection<String> words = spellChecker.correctWord(text.trim());
			correctOutputField.setText(printList(words));

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			
		}
		
	}
	
	
	class WordCorrectorListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
//			if e is arrow keys => return
			
			String text = dataInputField.getText();
			if (text.trim().length()<2) return;

			Collection<String> words = spellChecker.correctWord(text.trim());
			correctOutputField.setText(printList(words));
		}
		
	}
	
	
	private String printList(Collection<?> l){
		String s="";
		
		for (Object n:l){
			s+=(n.toString()+" ");
		}
		System.out.println();
		
		return s;
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame= new JFrame();
		
		VisualMain panel = new VisualMain(); 
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.show();
		frame.setSize(500, 500);
	}



	private void showWindow(JFrame frame) {
		JPanel mainPanel = new JPanel();
		JScrollPane js= new JScrollPane(mainPanel);
//		mainPanel.add(jgraph);
		js.setPreferredSize(new Dimension(450, 110));

		frame.getContentPane().add(js);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
