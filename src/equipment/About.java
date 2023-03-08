/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author a
 */
public class About extends JFrame {
       
    JScrollPane scrpane;
    JTextPane tpane;
    Style s, def, regular;

    String[] initString = {"Versija 2.11.7\n", "2023-03-08"};
    String[] initStyles = {"caption", "regular"}; //
    
    public About () {
        init();
    }
    
    private void init() {
	tpane = new JTextPane();
	StyledDocument doc = tpane.getStyledDocument();
	tpane.setEditable(false);
	addStylesToDocument(doc);
	try {
	    for (int i = 0; i < initString.length; i++) {
		doc.insertString(doc.getLength(), initString[i], doc.getStyle(initStyles[i]));
	    }
	} catch (BadLocationException ble) {
	    System.err.println("Couldn't insert initial text into text pane.");
	}
	scrpane = new JScrollPane(tpane);
	add(scrpane);
	pack();
//	addWindowListener(new WindowAdapter() {
//	    @Override
//	    public void windowClosing(WindowEvent e) {
//	    }
//	});

	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	setVisible(true);
    }

    protected void addStylesToDocument(StyledDocument doc) {
	def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
	regular = doc.addStyle("regular", def);
	StyleConstants.setFontFamily(def, "SansSerif");

	s = doc.addStyle("italic", regular);
	StyleConstants.setItalic(s, true);

	s = doc.addStyle("bold", regular);
	StyleConstants.setBold(s, true);

	s = doc.addStyle("caption", regular);
	StyleConstants.setFontSize(s, 18);
	StyleConstants.setBold(s, true);

    }
    
    
    
    
}
