/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zurnalasP;

import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 *
 * @author a
 */
public class Help extends JFrame {
       
    JScrollPane scrpane;
    JEditorPane editorPane;
    URL helpUrl;
//    JTextPane tpane;
//    Style s, def, regular, italic, bold;
//    
//    String[] initString = {"Aprašymas\nAprašymas\nAprašymas\nAprašymas\n", "ita\t", "bold", "H1"};
//    String[] initStyles = {"regular", "italic", "bold", "h1"};
    
    public Help () {
        init();
    }
    
    private void init() {
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        helpUrl =  Help.class.getResource("Aprasymas.html");
        editorPane.setContentType("text/html;charset=UTF-8");
        if (helpUrl != null) {
            try {
                editorPane.setPage(helpUrl); 
            } catch (IOException e) {
                System.out.println("Attempted to read a bad URL: " + helpUrl);
            }
        }
        scrpane = new JScrollPane(editorPane);
        add(scrpane);
//        pack();
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setVisible(true);
    }
    
//    private void init() {
//        tpane = new JTextPane();
//        StyledDocument doc = tpane.getStyledDocument();
//        tpane.setEditable(false);
//        addStylesToDocument(doc);
//        try {
//            for (int i = 0; i < initString.length; i++) {
//                doc.insertString(doc.getLength(), initString[i], doc.getStyle(initStyles[i]));
//            }
//        } catch (BadLocationException ble) {
//            System.err.println("Couldn't insert initial text into text pane.");
//        }
//        scrpane = new JScrollPane(tpane);
//        add(scrpane);
//        pack();
////	addWindowListener(new WindowAdapter() {
////	    @Override
////	    public void windowClosing(WindowEvent e) {
////	    }
////	});
//        
//        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
//        setVisible(true);
//    }

//    protected void addStylesToDocument(StyledDocument doc) {
//        def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
//        regular = doc.addStyle("regular", def);
//        StyleConstants.setFontFamily(def, "SansSerif");
//        
//        s = doc.addStyle("italic", regular);
//        StyleConstants.setItalic(s, true);
//        
//        s = doc.addStyle("bold", regular);
//        StyleConstants.setBold(s, true);        
//        
//        s = doc.addStyle("h1", regular);
//        StyleConstants.setFontSize(s, 16);
//        
//    }
    
    
//    @Override
//    public void actionPerformed(ActionEvent ae) {
//        
//    }

    
    
}
