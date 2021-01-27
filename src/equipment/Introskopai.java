/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Introskopai extends Sistemos {

    static final String SELECT_ALL = "SELECT i.Pavadinimas, i.Nr, i.Vamzdziu_skc, g.Pavadinimas, g.Nr, g.Vamzdzio_nr, g.Vamzdzio_pavad, g.Nr, b.Busena FROM JSG g LEFT JOIN Introskopai i ON g.Vieta = i.Nr LEFT JOIN Gen_busenos b ON g.Busena = b.ID ORDER BY i.Nr";
    static final String SEARCH = "SELECT i.Pavadinimas, i.Nr, i.Vamzdziu_skc, g.Pavadinimas, g.Nr, g.Vamzdzio_nr, g.Vamzdzio_pavad, g.Nr, b.Busena FROM JSG g LEFT JOIN Introskopai i ON g.Vieta = i.Nr LEFT JOIN Gen_busenos b ON g.Busena = b.ID WHERE i.Pavadinimas LIKE ? OR i.Nr LIEK ? OR g.Pavadinimas LIKE ? OR g.Nr LIKE ? OR g.Vamzdzio_pavad LIKE ? OR g.Vamzdzio_nr LIKE ? ORDER BY i.Nr";
    static final String PREPARE_INSERT = "INSERT INTO JSG (Vieta, Nr, Pavadinimas, Data, Vamzdzio_pavad, Vamzdzio_nr, Busena) VALUES (?, ?, ?, ?, ?, ?, ?)";
    static final String PREPARE_UPDATE = "UPDATE JSG SET Vieta = ?, Nr = ?, Pavadinimas = ?, Data = ?, Vamzdzio_pavad = ?, Vamzdzio_nr = ?, Busena = ? WHERE Nr = ?";

    static final String VIETA = "Vieta";
    static final String NR = "Nr";
    static final String PAVADINIMAS = "Pavadinimas";
    static final String DATA = "Data";
    static final String VAMZDZIO_PAVAD = "Vamzdis";
    static final String VAMZDZIO_NR = "Vamzdžio nr.";
    static final String BUSENA = "Būsena";

    private DefaultTableModel tableModel;
    private PreparedStatement preparedUpdate, preparedInsert, preparedDelete;
    
    private JPanel panelSearch, panelTop;
    private JLabelRechts lSearch;
    private JTextField fSearch;

    private PreparedStatement preparedFilter;


    public Introskopai(ConnectionEquipment connection, int size) {
	super(connection, size);
	init();
    }

    private void init() {
	if (connection != null) {
	    setLayout(new BorderLayout());
	    createTable();
//	    createTopPanel();
	    add(panelTop, BorderLayout.NORTH);
	    add(scrPaneTable, BorderLayout.CENTER);
	    setVisible(true);
	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
//    private void createTopPanel(){
//	createPanelButtons();
//	panelTop = new JPanel(new GridLayout(2, 1));
//	panelSearch = new JPanel();
//	lSearch = new JLabelRechts("Paieška:", fontsize);
//	panelSearch.add(lSearch);
//	fSearch = new JTextField(50);
//	panelSearch.add(fSearch);
//	panelTop.add(pButtons);
//	panelTop.add(panelSearch);
//    }

    @Override
    protected void createPanelInput() {
//	getLocations();
//        getCodes();
	pInput = new JPanel(new BorderLayout());
	createPanelFields();
	pInput.add(pFields, BorderLayout.NORTH);
	createPanelButtons();
	pInput.add(pButtons, BorderLayout.CENTER);
	lMessage = new JLabelRechts(fontsize);
    }

 
//    @Override
//    protected void createPanelFields() {
//        pFields = new JPanel(new GridBagLayout());
//	gbc = new GridBagConstraints();
//	gbc.fill = GridBagConstraints.HORIZONTAL;
////		panelInput.setBackground(Color.lightGray);
//	gbc.insets = new Insets(0, 0, 5, 5);
//
//// Μια πρώτη σειρά
//	gbc.gridx = 0;
//	gbc.gridy = 0;
//	gbc.weightx = 0.2;
//        lDate = new JLabelRechts("Data:", fontsize);
//	pFields.add(lDate, gbc);
//
//	gbc.gridx = 1;
//	gbc.weightx = 0;
//	tfDate = new JMyTextField(date.getToday(), 8, fontsize);
//	tfDate.addMouseListener(this);
//	tfDate.setToolTipText("Dvigubas spragtelėjimas šiandienos datai");
//	pFields.add(tfDate, gbc);
//
//	gbc.gridx = 2;
//	gbc.weightx = 0;
//	lSystem = new JLabelRechts("Sistema", fontsize);
//	pFields.add(lSystem, gbc);
//
//	gbc.gridx = 3;
//	gbc.weightx = 0;
//	cbSystem = new JMyComboBox(systems[1], fontsize);
//	pFields.add(cbSystem, gbc);
////
//	gbc.gridx = 4;
//	gbc.weightx = 0;
//	lName = new JLabelRechts("Pavadinimas", fontsize);
//	pFields.add(lName, gbc);
//
//	gbc.gridx = 5;
//	gbc.weightx = 0.6;
//	fName = new JMyTextField(12, fontsize);
//	fName.addMouseListener(this);
//	pFields.add(fName, gbc);
////
//	gbc.gridx = 6;
//	gbc.weightx = 0;
//	lLocation = new JLabelRechts("Vieta", fontsize);
//	pFields.add(lLocation, gbc);
//
//	gbc.gridx = 7;
//	gbc.weightx = 0;
//	cbLocations = new JMyComboBox(locations[1], fontsize);
//	pFields.add(cbLocations, gbc);
////
//	gbc.gridx = 8;
//	gbc.weightx = 0;
//	lMark = new JLabelRechts("Žyma", fontsize);
//	pFields.add(lMark, gbc);
//
//	gbc.gridx = 9;
//	gbc.weightx = 0;
//	fMark = new JMyTextField(1, fontsize);
//	pFields.add(fMark, gbc);
////
//	gbc.gridx = 10;
//	gbc.weightx = 0;
//	lIT = new JLabelRechts("IT", fontsize);
//	pFields.add(lIT, gbc);
//
//	gbc.gridx = 11;
//	gbc.weightx = 0;
//	fIT = new JMyTextField(7, fontsize);
//	pFields.add(fIT, gbc);
////
//	gbc.gridx = 12;
//	gbc.weightx = 0;
//	lNr = new JLabelRechts("Nr.", fontsize);
//	pFields.add(lNr, gbc);
//
//	gbc.gridx = 13;
//	gbc.weightx = 0.2;
//	fNr = new JMyTextField(7, fontsize);
//	pFields.add(fNr, gbc);
////
//	gbc.gridx = 14;
//	gbc.weightx = 0;
//	pFields.add(new JLabelRechts("Veikla", fontsize), gbc);
//
//	gbc.gridx = 15;
//	gbc.weightx = 0;
//	cbCode = new JMyComboBox(codes[1], fontsize);
//	pFields.add(cbCode, gbc);
//// Η δεύτερη σειρά
//	gbc.gridy = 1;
//	gbc.weightx = 0;
//	gbc.gridx = 0;
//	lFilters = new JLabelRechts("Filtrai:", fontsize);
//	lFilters.addMouseListener(this);
//	pFields.add(lFilters, gbc);
//	
//	gbc.gridx = 1;
//	chDate = new JCheckBox();
//	pFields.add(chDate, gbc);
//
//	gbc.gridx = 3;
//	chSystem = new JCheckBox();
//	pFields.add(chSystem, gbc);
//
//	gbc.gridx = 5;
//	chName = new JCheckBox();
//	pFields.add(chName, gbc);
//
//	gbc.gridx = 7;
//	chLocation = new JCheckBox();
//	pFields.add(chLocation, gbc);
//
//	gbc.gridx = 9;
//	chMark = new JCheckBox();
//	pFields.add(chMark, gbc);
//	
//	gbc.gridx = 11;
//	chIT = new JCheckBox();
//	pFields.add(chIT, gbc);
//	
//	gbc.gridx = 13;
//	chNr = new JCheckBox();
//	pFields.add(chNr, gbc);
//	
//// Η τρίτη σειρά
//	gbc.gridy = 2;
//	gbc.gridx = 0;
//	gbc.weightx = 0;
//	createPanelMessages();
//	pFields.add(pMessage, gbc);
//
//	gbc.gridx = 1;
////	gbc.weightx = 1;
//        gbc.gridwidth = 15;
//	ta_Message = new JMyTextArea(3, 40, fontsize);
//	ta_Message.addMouseListener(this);
//	ta_Message.setFocusAccelerator('A');
//	ta_Message.setLineWrap(true);
//	ta_Message.setWrapStyleWord(true);
////	ta_Message.setToolTipText("Dvigubas spragtelėjimas ištrina tekstą iš šio lauko");
//	scrPaneMessage = new JScrollPane(ta_Message);
//	pFields.add(scrPaneMessage, gbc);
//        
//	
//    }

    
    private void createTable() {
	tableModel = new DefaultTableModel(new Object[]{VIETA, NR, PAVADINIMAS, DATA, VAMZDZIO_PAVAD, VAMZDZIO_NR, BUSENA}, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setAutoCreateRowSorter(true);
	table.getSelectionModel().addListSelectionListener(this);
	setColumnsWidths();
	tableModel.setRowCount(1);
//	setzt_dieUeberschriften();
	scrPaneTable = new JScrollPane(table);
    }

    private void setColumnsWidths() {
	TableColumn dieSpalte;
	dieSpalte = null;
	    for (int i = 0; i < table.getColumnCount(); i++) {
		dieSpalte = table.getColumnModel().getColumn(i);
		switch (tableModel.getColumnName(i)) {
//		    case PAVADINIMAS:
//			dieSpalte.setPreferredWidth(100);
//			break;
		    case DATA:
			dieSpalte.setPreferredWidth(100);
			break;
		}
	    }
    }
	
//    private void setzt_dieUeberschriften(){
//	table.getTableHeader().setPreferredSize(new Dimension(table.getWidth(), 60));
//        table.getColumnModel().getColumn(1).setHeaderValue("<html>Der<br>Typ</html>");
//    }
	
// SELECT Nr, Pavadinimas, Vamzdis, Data FROM JSG WHERE Nr LIKE ? OR Pavadinimas LIKE ? OR Vamzdis LIKE ? OR Data LIKE ?
    private void filter() {
        Object[] row;
	int i, colcount;
	String toSearch;
	tableModel.setRowCount(0);
        ResultSet resultset;
	try {
	    if (fSearch.getText().isEmpty()) {
		resultset = connection.executeQuery(SELECT_ALL);
	    } else {
		toSearch = fSearch.getText();
		preparedFilter = connection.prepareStatement(SEARCH);
		preparedFilter.setString(1, toSearch);
		preparedFilter.setString(2, toSearch);
		preparedFilter.setString(3, toSearch);
		preparedFilter.setString(4, toSearch);
		resultset = preparedFilter.executeQuery();
	    }
	    colcount = tableModel.getColumnCount();
	    row = new Object[colcount];
	    while( resultset.next() ){
		for (i = 0; i <= colcount - 1; i++) {
		    row[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(row);
	    }
	    resultset.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}

    }
	
// UPDATE JSG SET Pavadinimas = ?, Vamzdis = ?, Data = ? WHERE Nr = ?    
   private void update() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
		}
		preparedUpdate.setString(1, (String) table.getValueAt(row, 1));
		preparedUpdate.setString(2, (String) table.getValueAt(row, 2));
		preparedUpdate.setString(3, (String) table.getValueAt(row, 3));
		preparedUpdate.setString(4, (String) table.getValueAt(row, 0));
		if (preparedUpdate.executeUpdate() == 1) {
		    filter();
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	} else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void insert() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedInsert == null) {
		    preparedInsert = connection.prepareStatement(PREPARE_INSERT);
		}
		// ID, Pavadinimas
		preparedInsert.setString(1, (String) table.getValueAt(row, 1));
		if (preparedInsert.executeUpdate() == 1) {
		    filter();
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	} else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "update":
		update();
		filter();
		break;	
	    case "filter":
		filter();
		break;
	    case "insert":
		insert();
		break;	
//	    case "delete":
//		delete();
//		filter();
//		break;	
	}
	
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
//	int zeile, spalte;
//	zeile = table.getSelectedRow();
//	spalte = table.getSelectedColumn();
//	if (lse.getValueIsAdjusting()) {
//	    table.setValueAt((String.valueOf(table.getValueAt(zeile, spalte)).replace(",", ".")), zeile, spalte);
//	}
    }



    
    
}
//	tableModel = new DefaultTableModel(new Object[]{"", "Datum", "<html>Fett<br>%</hmtl>", "<html>Muskeln<br>%</html>", "<html>Wasser<br>%</html>", "<html>Knochen<br>kg</html>", "<html>Masse<br>kg</html>", "<html>Energie0<br>kcal</html>", "<html>Energie1<br>kcal</html>", "<html>Bauch<br>cm</html>", "<html>Oberarm<br>cm</html>", "<html>Unterarm<br>cm</html>", "<html>Ober-<br>schenkel<br>cm</html>", "<html>Unter-<br>schenkel<br>cm</html>", "<html>Brust<br>cm</html>", "<html>Fettmasse<br>kg</html>", "<html>Muskel-<br>masse<br>kg</html>"}, 0);