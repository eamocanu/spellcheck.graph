/** Copyright (c) 2011, Adrian M
    All rights reserved.
    
Copyright (c) 2001-2009, JGraph Ltd
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list 
of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this 
list of conditions and the following disclaimer in the documentation and/or 
other materials provided with the distribution.
Neither the name of JGraph Ltd nor the names of its contributors may be used 
to endorse or promote products derived from this software without specific prior written permission.
Termination for Patent Action. This License shall terminate
automatically, as will all licenses assigned to you by the copyright
holders of this software, and you may no longer exercise any of the
rights granted to you by this license as of the date you commence an
action, including a cross-claim or counterclaim, against the
copyright holders of this software or any licensee of this software
alleging that any part of the JGraph, JGraphX and/or mxGraph software
libraries infringe a patent. This termination provision shall not
apply for an action alleging patent infringement by combinations of
this software with other software or hardware.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package eamocanu.dictionary.drivers;

import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jgraph.layout.demo.JGraphLayoutPanel;

import eamocanu.dictionary.graph.VisualSpellChecker;
import eamocanu.dictionary.graph.WordGraph;

/**
 * @author Adrian
 * 
 * Rushed test driver to illustrate graphical representation 
 * how the word was corrected.
 */
public class WordGraphMain {
	private static WordGraph grf;
	
	
	
	public static void main(String[] args) {
		VisualSpellChecker graphSpellChecker= new VisualSpellChecker();

		try {
			graphSpellChecker.buildDictionary("sample dictionary/1000new.txt");

			String wd="ale";
			graphSpellChecker.correctWord(wd);
			JFrame frame= new JFrame();

			final JGraphLayoutPanel layoutPanel = new JGraphLayoutPanel();
			grf= graphSpellChecker.getGraph();
			
			if (grf!=null){
				SwingUtilities.invokeLater( new Runnable() { 
					public void run() {
						layoutPanel.setGraphData(grf.getGraph());
						layoutPanel.start();
					}
				});
				frame.getContentPane().add(layoutPanel);

				frame.show();
				frame.setSize(500, 500);
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
