/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Irenginiai extends Sistemos {

    private static final String SELECT_ALL = "SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas, v.Pavadinimas, i.Pozymis, i.Pastaba, i.Data, k.Pavadinimas FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID LEFT JOIN Vietos v ON i.Vieta = v.ID LEFT JOIN Veiklos k ON i.Veikla = k.ID ORDER BY i.IT";
    private static final String SELECT = "SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas, v.Pavadinimas, i.Pozymis, i.Pastaba, i.Data, k.Pavadinimas FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID LEFT JOIN Vietos v ON i.Vieta = v.ID LEFT JOIN Veiklos k ON i.Veikla = k.ID";
    private static final String PREPARE_INSERT = "INSERT INTO Irenginiai (IT, Nr, Pavadinimas, Sistema, Data, Vieta, Pozymis, Pastaba, Veikla) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String PREPARE_UPDATE = "UPDATE Irenginiai SET IT = ?, Nr = ?, Pavadinimas = ?, Sistema = ?, Data = ?, Vieta = ?, Pozymis = ?, Pastaba = ?, Veikla = ? WHERE ID = ?";
//    private static final String PREPARE_DELETE = "DELETE FROM Irenginiai WHERE ID = ?";
    private static final String ID = "ID (auto)";
    private static final String IT = "IT";
    private static final String NR = "Nr";
    private static final String PAVADINIMAS = "Pavadinimas";
    private static final String SISTEMA = "Sistema";
    private static final String VIETA = "Vieta";
    private static final String POZYMIS = "⊠□";
    private static final String PASTABA = "Pastaba";
    private static final String DATA = "Data";
    private static final String VEIKLA = "Veikla";
    
    private JCheckBox chLocation, chMark, chIT, chNr, chName;
    private JLabelRechts lName, lLocation, lNr, lMark, lIT;
    private JMyComboBox cbLocations, cbCode;
    private JTextField fMark, fIT, fNr;
    private JTextArea ta_Message;

    private DefaultTableModel tableModel;
    private PreparedStatement preparedUpdate, preparedInsert, preparedFilter;
    
    String[][] locations, codes;

    protected Irenginiai(ConnectionEquipment connection, int size) {
	super(connection, size);
        fontsize = size;
	init();
    }

    private void init() {
	if (connection != null) {
	    setLayout(new BorderLayout());
	    createTable();
	    createPanelInput();
	    add(pInput, BorderLayout.NORTH);
	    add(scrPaneTable, BorderLayout.CENTER);
	    setVisible(true);
	    filter_all();
	} else {
	    JOptionPane.showMessageDialog(this, "No connection!", "Error!", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    protected void createPanelInput() {
	getLocations();
        getCodes();
	pInput = new JPanel(new BorderLayout());
	createPanelFields();
	pInput.add(pFields, BorderLayout.NORTH);
	createPanelButtons();
	pInput.add(pButtons, BorderLayout.CENTER);
	lMessage = new JLabelRechts(fontsize);
    }

    private void createTable() {
	tableModel = new DefaultTableModel(new Object[]{ID, IT, NR, PAVADINIMAS, SISTEMA, VIETA, POZYMIS, PASTABA, DATA, VEIKLA}, 0);
	table = new JTable(tableModel);
	table.setAutoCreateRowSorter(true);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setDefaultEditor(Object.class, null);
	table.addMouseListener(this);
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
		case PAVADINIMAS:
		    dieSpalte.setPreferredWidth(300);
		    break;
		case ID:
		    dieSpalte.setPreferredWidth(20);
		    break;
		case POZYMIS:
		    dieSpalte.setPreferredWidth(13);
		    break;
	    }
	}
    }
    
    private void getLocations(){
	try {
	    locations = connection.getLocations();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    private void getCodes(){
	try {
	    codes = connection.getCodes();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    

    @Override
    protected void createPanelFields() {
        pFields = new JPanel(new GridBagLayout());
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
//		panelInput.setBackground(Color.lightGray);
	gbc.insets = new Insets(0, 0, 5, 5);

// Μια πρώτη σειρά
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0.2;
        lDate = new JLabelRechts("Data:", fontsize);
	pFields.add(lDate, gbc);

	gbc.gridx = 1;
	gbc.weightx = 0;
	tfDate = new JMyTextField(date.getToday(), 8, fontsize);
	tfDate.addMouseListener(this);
	tfDate.setToolTipText("Dvigubas spragtelėjimas šiandienos datai");
	pFields.add(tfDate, gbc);

	gbc.gridx = 2;
	gbc.weightx = 0;
	lSystem = new JLabelRechts("Sistema", fontsize);
	pFields.add(lSystem, gbc);

	gbc.gridx = 3;
	gbc.weightx = 0;
	cbSystem = new JMyComboBox(systems[1], fontsize);
	pFields.add(cbSystem, gbc);
//
	gbc.gridx = 4;
	gbc.weightx = 0;
	lName = new JLabelRechts("Pavadinimas", fontsize);
	pFields.add(lName, gbc);

	gbc.gridx = 5;
	gbc.weightx = 0.6;
	fName = new JMyTextField(12, fontsize);
	fName.addMouseListener(this);
	pFields.add(fName, gbc);
//
	gbc.gridx = 6;
	gbc.weightx = 0;
	lLocation = new JLabelRechts("Vieta", fontsize);
	pFields.add(lLocation, gbc);

	gbc.gridx = 7;
	gbc.weightx = 0;
	cbLocations = new JMyComboBox(locations[1], fontsize);
	pFields.add(cbLocations, gbc);
//
	gbc.gridx = 8;
	gbc.weightx = 0;
	lMark = new JLabelRechts("Žyma", fontsize);
	pFields.add(lMark, gbc);

	gbc.gridx = 9;
	gbc.weightx = 0;
	fMark = new JMyTextField(1, fontsize);
	pFields.add(fMark, gbc);
//
	gbc.gridx = 10;
	gbc.weightx = 0;
	lIT = new JLabelRechts("IT", fontsize);
	pFields.add(lIT, gbc);

	gbc.gridx = 11;
	gbc.weightx = 0;
	fIT = new JMyTextField(7, fontsize);
	pFields.add(fIT, gbc);
//
	gbc.gridx = 12;
	gbc.weightx = 0;
	lNr = new JLabelRechts("Nr.", fontsize);
	pFields.add(lNr, gbc);

	gbc.gridx = 13;
	gbc.weightx = 0.2;
	fNr = new JMyTextField(7, fontsize);
	pFields.add(fNr, gbc);
//
	gbc.gridx = 14;
	gbc.weightx = 0;
	pFields.add(new JLabelRechts("Veikla", fontsize), gbc);

	gbc.gridx = 15;
	gbc.weightx = 0;
	cbCode = new JMyComboBox(codes[1], fontsize);
	pFields.add(cbCode, gbc);
// Η δεύτερη σειρά
	gbc.gridy = 1;
	gbc.weightx = 0;
	gbc.gridx = 0;
	lFilters = new JLabelRechts("Filtrai:", fontsize);
	lFilters.addMouseListener(this);
	pFields.add(lFilters, gbc);
	
	gbc.gridx = 1;
	chDate = new JCheckBox();
	pFields.add(chDate, gbc);

	gbc.gridx = 3;
	chSystem = new JCheckBox();
	pFields.add(chSystem, gbc);

	gbc.gridx = 5;
	chName = new JCheckBox();
	pFields.add(chName, gbc);

	gbc.gridx = 7;
	chLocation = new JCheckBox();
	pFields.add(chLocation, gbc);

	gbc.gridx = 9;
	chMark = new JCheckBox();
	pFields.add(chMark, gbc);
	
	gbc.gridx = 11;
	chIT = new JCheckBox();
	pFields.add(chIT, gbc);
	
	gbc.gridx = 13;
	chNr = new JCheckBox();
	pFields.add(chNr, gbc);
	
// Η τρίτη σειρά
	gbc.gridy = 2;
	gbc.gridx = 0;
	gbc.weightx = 0;
	createPanelMessages();
	pFields.add(pMessage, gbc);

	gbc.gridx = 1;
//	gbc.weightx = 1;
        gbc.gridwidth = 15;
	ta_Message = new JMyTextArea(3, 40, fontsize);
	ta_Message.addMouseListener(this);
	ta_Message.setFocusAccelerator('A');
	ta_Message.setLineWrap(true);
	ta_Message.setWrapStyleWord(true);
//	ta_Message.setToolTipText("Dvigubas spragtelėjimas ištrina tekstą iš šio lauko");
	scrPaneMessage = new JScrollPane(ta_Message);
	pFields.add(scrPaneMessage, gbc);
        
	
    }

//    private void createPanel_Messages() {
//	pMessage = new JPanel(new GridLayout(2, 1));
//	lMessage = new JLabelRechts("Aprašymas");
//	ch_Message = new JCheckBox();
//	pMessage.add(lMessage);
//	pMessage.add(ch_Message);
//    }
//

    private void filter() {
        if (chDate.isSelected() || chSystem.isSelected() || chName.isSelected() || chLocation.isSelected() || chMark.isSelected() || chIT.isSelected() || chNr.isSelected()) {
            filter_by();
        } else {
            filter_all();
        }
    }

    protected void filter_all() {
	Object[] row;
	int i, colcount;
	tableModel.setRowCount(0);
	ResultSet resultset;
	try {
	    resultset = connection.executeQuery(SELECT_ALL);
	    colcount = tableModel.getColumnCount();
	    row = new Object[colcount];
	    while (resultset.next()) {
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

    private void filter_by() {
	int i, colcount;
        Object[] row;
	StringBuilder sb;
	tableModel.setRowCount(0);
        ResultSet resultset;
	try {
	    sb = prepareFilter();
	    preparedFilter = connection.prepareStatement(sb.toString());
	    preparedFilter_setPrepared(sb);
	    resultset = preparedFilter.executeQuery();
	    colcount = tableModel.getColumnCount();
	    row = new Object[colcount];
	    while( resultset.next() ){
		for (i = 0; i <= colcount - 1; i++) {
		    row[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(row);
	    }
	    resultset.close();
	    preparedFilter.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }

//SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas, v.Pavadinimas, i.Pastaba, i.Pozymis, i.Data FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID LEFT JOIN Vietos v ON i.Vieta = v.Pavadinimas
    private StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(SELECT);
        sb.append(" WHERE");
        if (chDate.isSelected()) {
            sb.append(" i.Data LIKE ?");
	}
        if (chSystem.isSelected()) {
            appendAND(sb);
            sb.append(" i.Sistema = ?");
        }
        if (chName.isSelected()) {
            appendAND(sb);
            sb.append(" i.Pavadinimas LIKE ?");
        }
        if (chLocation.isSelected()) {
            appendAND(sb);
            sb.append(" i.Vieta = ?");
        }
        if (chMark.isSelected()) {
            appendAND(sb);
            sb.append(" i.Pozymis LIKE ?");
        }
        if (chIT.isSelected()) {
            appendAND(sb);
            sb.append(" i.IT LIKE ?");
        }
        if (chNr.isSelected()) {
            appendAND(sb);
            sb.append(" i.Nr LIKE ?");
        }
	if (chMessage.isSelected()) {
	    appendAND(sb);
            sb.append(" i.Pastaba LIKE ?");
	System.out.println(sb.toString());
        }
	sb.append(" ORDER BY i.IT");
	return sb;
    }
    
//SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas, v.Pavadinimas, i.Pastaba, i.Pozymis, i.Data FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID LEFT JOIN Vietos v ON i.Vieta = v.Pavadinimas
    private void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n;
	n = 0;
//System.out.println(sb.toString());
	i = sb.indexOf(" i.Data LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, (String) tfDate.getText());
	}
	i = sb.indexOf(" i.Sistema = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));	    
	}
	i = sb.indexOf(" i.Pavadinimas LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, (String) fName.getText());
	}
	i = sb.indexOf(" i.Vieta = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(locations[0][cbLocations.getSelectedIndex()]));
	}
	i = sb.indexOf(" i.Pozymis LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, (String) fMark.getText());;
	}
	i = sb.indexOf(" i.IT LIKE ?");
	if (i >= 0) {
 	    n++;
	    preparedFilter.setString(n, (String) fIT.getText());;
	}
	i = sb.indexOf(" i.Nr LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, (String) ta_Message.getText());
	}
	i = sb.indexOf(" i.Pastaba LIKE ?");
	if (i >= 0) {
 	    n++;
	    preparedFilter.setString(n, (String) ta_Message.getText());;
	}
	
    }

//UPDATE Irenginiai SET IT = ?, Nr = ?, Pavadinimas = ?, Sistema = ?, Data = ?, Vieta = ?, Pozymis = ?, Pastaba = ? WHERE ID = ?    
    private void update() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
		}
// IT, Nr, Pavadinimas, Sistema, ID
		preparedUpdate.setString(1, fIT.getText());
		preparedUpdate.setString(2, fNr.getText());
		preparedUpdate.setString(3, fName.getText());
		preparedUpdate.setInt(4, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));
                preparedUpdate.setString(5, tfDate.getText());
		preparedUpdate.setInt(6, Integer.valueOf(locations[0][cbLocations.getSelectedIndex()]));
		preparedUpdate.setString(7, fMark.getText());
		preparedUpdate.setString(8, ta_Message.getText());
		preparedUpdate.setInt(9, Integer.valueOf(codes[0][cbCode.getSelectedIndex()]));
		preparedUpdate.setInt(10, (Integer) table.getValueAt(row, tableModel.findColumn(ID)));
		
		if (preparedUpdate.executeUpdate() == 1) {
		    filter_all();
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	} else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
//	taMessage.requestFocus();
    }

    private void insert() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
//	    systems_id = getSystemID((String) table.getValueAt(row, 4));
//	    if (systems_id >= 0) {
	    try {
		if (preparedInsert == null) {
		    preparedInsert = connection.prepareStatement(PREPARE_INSERT);
		}
		// IT, Nr, Pavadinimas, Sistema
		preparedInsert.setString(1, table.getValueAt(row, 1).toString());
		preparedInsert.setString(2, table.getValueAt(row, 2).toString());
		preparedInsert.setString(3, table.getValueAt(row, 3).toString());
		preparedInsert.setInt(4, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));
		if (preparedInsert.executeUpdate() == 1) {
		    filter_all();
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.getErrorCode(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
//	    } else {
//		JOptionPane.showMessageDialog(this, "Nėra tokios sistemos.", "Klaida!!", JOptionPane.ERROR_MESSAGE);
//	    }
	}  else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);     
	}
    }

//    private void delete() {
//	int row;
//	row = table.getSelectedRow();
//	if (row >= 0) {
//	    try {
//		if (preparedDelete == null) {
//		    preparedDelete = connection.prepareStatement(PREPARE_DELETE);
//		}
//// ID, IT, Nr, Pavadinimas, Sistema
//		preparedDelete.setInt(1, (int) table.getValueAt(row, 0));
//		if (preparedDelete.execute()) {
//		    filter();
//		}
//	    } catch (SQLException ex) {
//		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
//	    }
//	}  else {
//	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
//	}
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "update":
		update();
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
	selectedRow = table.getSelectedRow();
 	if (selectedRow >= 0) {
            tfDate.setText(table.getValueAt(selectedRow, tableModel.findColumn(DATA)).toString());
	    setComboBoxItem(cbSystem, systems[1], (String) table.getValueAt(selectedRow, tableModel.findColumn(SISTEMA)));
            fName.setText(table.getValueAt(selectedRow, tableModel.findColumn(PAVADINIMAS)).toString());
	    setComboBoxItem(cbLocations, locations[1], (String) table.getValueAt(selectedRow, tableModel.findColumn(VIETA)));
            fMark.setText(table.getValueAt(selectedRow, tableModel.findColumn(POZYMIS)).toString());
            fIT.setText(table.getValueAt(selectedRow, tableModel.findColumn(IT)).toString());
            fNr.setText(table.getValueAt(selectedRow, tableModel.findColumn(NR)).toString());
            setComboBoxItem(cbCode, codes[1], (String) table.getValueAt(selectedRow, tableModel.findColumn(VEIKLA)));
	    ta_Message.setText(table.getValueAt(selectedRow, tableModel.findColumn(PASTABA)).toString());
        }
    }



    
    
}
//	tableModel = new DefaultTableModel(new Object[]{"", "Datum", "<html>Fett<br>%</hmtl>", "<html>Muskeln<br>%</html>", "<html>Wasser<br>%</html>", "<html>Knochen<br>kg</html>", "<html>Masse<br>kg</html>", "<html>Energie0<br>kcal</html>", "<html>Energie1<br>kcal</html>", "<html>Bauch<br>cm</html>", "<html>Oberarm<br>cm</html>", "<html>Unterarm<br>cm</html>", "<html>Ober-<br>schenkel<br>cm</html>", "<html>Unter-<br>schenkel<br>cm</html>", "<html>Brust<br>cm</html>", "<html>Fettmasse<br>kg</html>", "<html>Muskel-<br>masse<br>kg</html>"}, 0);