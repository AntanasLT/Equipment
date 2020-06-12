/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class A extends JPanel implements ActionListener {

    static final String DIE_ABFRAGE_ALLES = "SELECT GeschirrID, Geschirrname, Geschirrmasse FROM Geschirr ORDER BY Geschirrname";
    static final String DIE_ABFRAGE_0 = "SELECT GeschirrID, Geschirrname, Geschirrmasse FROM Geschirr WHERE Geschirrname LIKE '%";
    static final String DIE_ANWEISUNG_ENTFERNEN = "DELETE FROM Geschirr WHERE GeschirrID = ";
    static final String DIE_ANWEISUNG_EIFUEGEN = "INSERT INTO Geschirr (Geschirrname, Geschirrmasse) VALUES (";
    static final String DIE_ANWEISUNG_AENDERN = "UPDATE Geschirr SET ";

    MyConnection dieAnbindung;
    DefaultTableModel dasTabellenmodel;
    JButton derKnopfEntfernen, derKnopfEinfuegen, derKnopfAendern, derKnopfSuchen;
    JPanel dieTaefelung;
    JScrollPane dieRolltaefelung;
    JTable dieTabelle;
    JTextField dasFeld_suchen;


    public A(MyConnection eineAnbindung) {
	dieAnbindung = eineAnbindung;
	dasTabellenmodel = new DefaultTableModel(new Object[]{"ID", "Geschirr", "Masse"}, 0);
	anfangen();
    }

    private void anfangen() {
	if (dieAnbindung != null) {
	setLayout(new BorderLayout());
	erstellt_dieTabelle();
	erstellt_dieTaefelungKnoepfe();
	add(dieTaefelung, BorderLayout.NORTH);
	add(dieRolltaefelung, BorderLayout.CENTER);
	setVisible(true);
	} else {
            JOptionPane.showMessageDialog(this, "Es gibt keine Anbindung!", "Fehler!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void erstellt_dieTaefelungKnoepfe() {
	dieTaefelung = new JPanel();
	dasFeld_suchen = new JTextField(20);
	dasFeld_suchen.setActionCommand("suchen");
	dasFeld_suchen.addActionListener(this);
	derKnopfAendern = new JButton("Ändern");
	derKnopfAendern.setActionCommand("aendern");
	derKnopfAendern.addActionListener(this);
	derKnopfEinfuegen = new JButton("Einfügen");
	derKnopfEinfuegen.setActionCommand("einfuegen");
	derKnopfEinfuegen.addActionListener(this);	
	derKnopfEntfernen = new JButton("Entfernen");
	derKnopfEntfernen.setActionCommand("entfernen");
	derKnopfEntfernen.addActionListener(this);
	derKnopfSuchen = new JButton("Suchen");
	derKnopfSuchen.setActionCommand("suchen");
	derKnopfSuchen.addActionListener(this);	
	dieTaefelung.add(dasFeld_suchen);
	dieTaefelung.add(derKnopfSuchen);
	dieTaefelung.add(derKnopfAendern);
	dieTaefelung.add(derKnopfEinfuegen);
	dieTaefelung.add(derKnopfEntfernen);
    }

    
    private void erstellt_dieTabelle() {
	dasTabellenmodel = new DefaultTableModel(new Object[]{"ID", "Geschirr", "Masse"}, 1);
	dieTabelle = new JTable(dasTabellenmodel);
	dieTabelle.setAutoCreateRowSorter(true);
	setztSpaltenbreiten();
	dieRolltaefelung = new JScrollPane(dieTabelle);
    }

        private void setztSpaltenbreiten() {
	TableColumn dieSpalte;
	dieSpalte = null;
	for (int i = 0; i < dieTabelle.getColumnCount(); i++) {
	    dieSpalte = dieTabelle.getColumnModel().getColumn(i);
	    switch (i) {
		case 0:
		    dieSpalte.setPreferredWidth(1);
		    break;
		case 1:
		    dieSpalte.setPreferredWidth(800);
		    break;
	    }
	}
    }

    private void stellt_Daten_dar(ResultSet derResultatsatz){
        Object[] dieZeile;
	int i, dieSpaltenzahl;
	dasTabellenmodel.setRowCount(0);
	try {
	    dieSpaltenzahl = dasTabellenmodel.getColumnCount();
	    dieZeile = new Object[dieSpaltenzahl];
	    while( derResultatsatz.next() ){
		for (i = 0; i < dieSpaltenzahl; i++) {
		    dieZeile[i] = derResultatsatz.getObject(i + 1);
		}
		dasTabellenmodel.addRow(dieZeile);
	    }
	    for (i = 0; i < dieSpaltenzahl; i++) {
		dieZeile[i] = "";
	    }
	    dasTabellenmodel.addRow(dieZeile);
	    derResultatsatz.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Fehler!", JOptionPane.ERROR_MESSAGE);
	}

    }
	
    
    private void aendert() {
//	int dieZeile;
//	StringBuilder dieAnweisung;
//	dieZeile = dieTabelle.getSelectedRow();
//	dieAnweisung = new StringBuilder(DIE_ANWEISUNG_AENDERN);
//	dieAnweisung.append("Geschirrname = '").append(dieTabelle.getValueAt(dieZeile, 1)).append("', Geschirrmasse = ").append(dieTabelle.getValueAt(dieZeile, 2)).append(" WHERE GeschirrID = ").append(dieTabelle.getValueAt(dieZeile, 0));
//	try {
//	    dieAnbindung.setzt_dieDaten(dieAnweisung.toString());
//	} catch (SQLException ex) {
//	    JOptionPane.showMessageDialog(this, ex.toString(), "Fehler!", JOptionPane.ERROR_MESSAGE);
//	}
    }
    
    private void sucht() {
	String einName;
	ResultSet dasResultat;
	einName = dasFeld_suchen.getText();
	    try {
		if (einName.isEmpty()) {
		    dasResultat = dieAnbindung.executeQuery(DIE_ABFRAGE_ALLES);
		} else {
		    dasResultat = dieAnbindung.executeQuery(DIE_ABFRAGE_0.concat(einName).concat("%'"));
		}
		stellt_Daten_dar(dasResultat);
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Fehler!", JOptionPane.ERROR_MESSAGE);
	    }	    
    }
    
    private void fuegt_ein() {
//	StringBuilder dieAnweisung;
//	dieAnweisung = new StringBuilder(DIE_ANWEISUNG_EIFUEGEN);
//	dieAnweisung.append("'").append(dieTabelle.getValueAt(dieTabelle.getSelectedRow(), 1)).append("', ").append(dieTabelle.getValueAt(dieTabelle.getSelectedRow(), 2)).append(")");
//	try {
//	    dieAnbindung.setzt_dieDaten(dieAnweisung.toString());
//	} catch (SQLException ex) {
//	    JOptionPane.showMessageDialog(this, ex.toString(), "Fehler!", JOptionPane.ERROR_MESSAGE);
//	}
    }

    private void entfernt() {
//	int dieZeile, id;
//	dieZeile = dieTabelle.getSelectedRow();
//	id =  (int) dieTabelle.getValueAt(dieZeile, 0);
//	if (id > 0) {
//	    try {
//		if (dieAnbindung.setzt_dieDaten(DIE_ANWEISUNG_ENTFERNEN.concat(String.valueOf(id))) == 1) {
//		    dasTabellenmodel.removeRow(dieZeile);
//		}
//	    } catch (SQLException ex) {
//		JOptionPane.showMessageDialog(this, ex.toString(), "Fehler!", JOptionPane.ERROR_MESSAGE);
//	    }
//	}
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "aendern":
		aendert();
		break;	
	    case "suchen":
		sucht();
		break;
	    case "einfuegen":
		fuegt_ein();
		break;	
	    case "entfernen":
		entfernt();
		break;	
	}
	
    }



    
    
}
