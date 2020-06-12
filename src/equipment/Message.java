/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author a
 */
public final class Message extends JDialog {// implements ActionListener{

    private boolean geaendert;

//    private JButton derKnopf_aendern;
//    private JButton derKnopf_abbrechen;
    private JScrollPane dieTaeffelung;
    private JTextArea derTextBereich;
    private JPanel dasFuellelement;
    
    public Message(){
	anfangen(30, 90);
    }
    
    public Message(String derText, int eineBreite, int eineHoehe) {
        anfangen(eineBreite, eineHoehe);
        setzt_denText(derText);
    }
    
    private void anfangen(int eineBreite, int eineHoehe) {
//        derKnopf_aendern = new JButton("Ändern");
//        derKnopf_aendern.addActionListener(this);
//        derKnopf_aendern.setActionCommand("speichern");
//        derKnopf_abbrechen = new JButton("Abbrechen");
//        derKnopf_abbrechen.addActionListener(this);
//        derKnopf_abbrechen.setActionCommand("abbrechen");        
        setLayout(new BorderLayout());
	derTextBereich = new JTextArea(eineBreite, eineHoehe);
	derTextBereich.setLineWrap(true);
	derTextBereich.setDragEnabled(true);
        dieTaeffelung = new JScrollPane(derTextBereich);
        dasFuellelement = new JPanel();
//        dasFuellelement.add(derKnopf_aendern);
//        dasFuellelement.add(derKnopf_abbrechen);
        add(dasFuellelement, BorderLayout.SOUTH);
        add(dieTaeffelung, BorderLayout.CENTER);
        geaendert = false;
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                geaendert = false;
	    }
        });

        this.pack();
    }

    public String bekommt_denText(){
        return derTextBereich.getText();
    }

    public void setzt_denText(String einText) {
        derTextBereich.setText(einText);
    }

    public boolean ist_geaendert(){
        return geaendert;
    }

    public void setzt_denNamen(String derName) {
	this.setName(derName);
    }

//    @Override
//    public void actionPerformed(ActionEvent ae) {
//        String derBefehl;
//        derBefehl = ae.getActionCommand();
//        switch (derBefehl) {
//            case "speichern":
//                geaendert = true;
//                this.setVisible(false);
//                break;
//            case "abbrechen":
//                geaendert = false;
//                this.setVisible(false);
//                break;
//        }
//    }
    
    
    
}
