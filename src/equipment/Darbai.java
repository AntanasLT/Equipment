/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import datum.Datum;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Darbai extends JPanel implements ActionListener, ListSelectionListener, MouseListener {

    private static final String PREPARE_SELECT_ALL = "SELECT d.ID, d.IDpr, v.Vardas, d.Data, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Busena, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID ORDER BY d.ID DESC LIMIT 50";
    private static final String PREPARE_SELECT = "SELECT d.ID, d.IDpr, v.Vardas, d.Data, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Busena, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID";
    private static final String PREPARE_INFO = "SELECT d.ID, d.IDpr, v.Vardas, d.Data, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Busena, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID WHERE dt.ID = 6 ORDER BY d.ID DESC";
//    private static final String PREPARE_INFO_ID = "SELECT d.ID, d.IDpr, v.Vardas, d.Data, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Busena, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID WHERE d.Busena = 5 AND (d.ID = ? OR d.IDpr = ?) ORDER BY d.ID";
    private static final String PREPARE_UNFINISHED = "SELECT d.ID, d.IDpr, v.Vardas, d.Data, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Busena, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID WHERE d.Baigta = FALSE ORDER BY d.ID DESC";
    private static final String PREPARE_UNFINISHED_ID = "SELECT d.ID, d.IDpr, v.Vardas, d.Data, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Busena, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID WHERE d.Baigta = FALSE AND (d.ID = ? OR d.IDpr = ?) ORDER BY d.ID DESC";
    private static final String PREPARE_UPDATE = "UPDATE Darbai SET Data = ?, IDPr = ?,  Sistema = ?, Irenginys = ?, Darbas = ?, Busena = ?, Pastabos = ?, Baigta = ? WHERE ID = ?";
    private static final String PREPARE_UPDATE_FINISH = "UPDATE Darbai SET Baigta = TRUE WHERE ID = ? OR IDpr = ?";
    private static final String PREPARE_INSERT = "INSERT INTO Darbai (IDPr, Vartotojas, Data, Sistema, Irenginys, Darbas, Busena, Pastabos, Baigta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    private static final String PREPARE_SELECT = "";
    private static final String PREPARE_DELETE = "DELETE FROM Darbai WHERE ID = ?";
    private static final String ID = "ID";
    private static final String ID_PR = "Tąsa";
    private static final String USER = "Vartotojas";
    private static final String DATA = "Data";
    private static final String SISTEMA = "Sistema";
    private static final String IRENGINYS = "Irenginys";
    private static final String DARBAS = "Darbas";
    private static final String BUSENA = "Būsena";
    private static final String APRASYMAS = "Aprašymas";
    private static final int BAIGTA = 3;
    private static final int SUSIPAZINAU = 5;


    ConnectionEquipment connection;
    Datum date;
    private DefaultTableModel tableModel;
    GridBagConstraints gbc;
    JCheckBox chDate, chSystem, chDevice, chWorktype, chState, chMessage;
    JLabelRechts lMessage, lDate, lDevice, lSystem, lWork, lIDpr, lFilters;
    protected JMyButton bDelete, bAdd, btChange, btAcknowl, btFilter, btUnfinished, btAll, btInfo;
    private JMyComboBox cbWorktype;
    JMyComboBox cbSystem, cbState;
    protected JPanel pInput, pnFIlterButtons, pFields;
    JPanel pEditButtons, pMessage;
    private PreparedStatement preparedUpdate, preparedInsert, preparedSelectAll, preparedDelete, preparedFilter, preparedUpdateFinish;
    JRadioButton radioButton1;
    JScrollPane scrPaneTable, scrPaneMessage;
    JTable table;
    JMyTextField tfDate, fName, tfIDpr, tfID;
    JMyTextArea taMessage;

    int selectedRow, fontsize;
    String user, message;
    String[][] systems, worktypes, users, states;
    Font font;
 


    public Darbai(ConnectionEquipment the_connection, int size) {
        fontsize = size;
	connection = the_connection;
	init();
    }

    private void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
	if (connection != null) {
	    try {
		users = connection.get_users();
                user = connection.get_username();
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	    }
	    date = new Datum();
	    init_components();
	} else {
	    JOptionPane.showMessageDialog(this, "Nerisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    protected void init_components() {
	setLayout(new BorderLayout());
	getSystems();
	getWorktypes();
	getStates();
	createTable();
	createPanelInput();
	add(pInput, BorderLayout.NORTH);
	add(scrPaneTable, BorderLayout.CENTER);
	add(lMessage, BorderLayout.SOUTH);
	setVisible(true);
    }

    protected void createPanelInput() {
        pInput = new JPanel(new BorderLayout());
        createPanelFields();
        pInput.add(pFields, BorderLayout.NORTH);
        createPanelFilterButtons();
        pInput.add(pnFIlterButtons, BorderLayout.SOUTH);
        lMessage = new JLabelRechts(fontsize);
//        updateComboBoxes();
    }

    protected void createPanelMessages() {
	pMessage = new JPanel(new GridLayout(2, 1));
	lMessage = new JLabelRechts("Aprašymas", fontsize);
	chMessage = new JCheckBox();
	pMessage.add(lMessage);
	pMessage.add(chMessage);
    }

    protected void createPanelFilterButtons() {
	pnFIlterButtons = new JPanel();
	btAll = new JMyButton("Naujausieji", fontsize);
	btAll.setActionCommand("all");
	btAll.addActionListener(this);
	pnFIlterButtons.add(btAll);
	btUnfinished = new JMyButton("Nebaigtieji", fontsize);
	btUnfinished.setActionCommand("unfinished");
	btUnfinished.addActionListener(this);
	pnFIlterButtons.add(btUnfinished);
	btInfo = new JMyButton("Info", fontsize);
	btInfo.setActionCommand("info");
	btInfo.addActionListener(this);
	pnFIlterButtons.add(btInfo);
	btFilter = new JMyButton("Filtruoti", fontsize);
	btFilter.setActionCommand("filter");
	btFilter.addActionListener(this);
	pnFIlterButtons.add(btFilter);
    }
    
    protected void createPanelEditButtons() {
	pEditButtons = new JPanel(new GridLayout(3, 1));
	btChange = new JMyButton("Išsaugoti", fontsize);
	btChange.setActionCommand("update");
	btChange.addActionListener(this);
	bAdd = new JMyButton("Naujas", fontsize);
	bAdd.setActionCommand("insert");
	bAdd.addActionListener(this);
	btAcknowl = new JMyButton("Susipažinau", fontsize);
	btAcknowl.setActionCommand("acknowledge");
	btAcknowl.addActionListener(this);
	pEditButtons.add(btChange);
	pEditButtons.add(bAdd);
        pEditButtons.add(btAcknowl);
// 	if (user.equals("Antanas") || user.equals("ak")) {
//	    bDelete = new JMyButton("Pašalinti");
//	    bDelete.setActionCommand("delete");
//	    bDelete.addActionListener(this);
//	    pEditButtons.add(bDelete);
//	}
   }

    protected void createPanelFields() {
        pFields = new JPanel(new GridBagLayout());
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
//		panelInput.setBackground(Color.lightGray);
	gbc.insets = new Insets(0, 0, 5, 5);

// Μια πρώτη σειρά
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0;
        lDate = new JLabelRechts("Data:", fontsize);
	pFields.add(lDate, gbc);

	gbc.gridx = 1;
	gbc.weightx = 0;
	tfDate = new JMyTextField(date.getToday(), 10, fontsize);
	tfDate.addMouseListener(this);
	tfDate.setToolTipText("Dvigubas spragtelėjimas šiandienos datai");
	pFields.add(tfDate, gbc);

	gbc.gridx = 2;
	gbc.weightx = 0;
	lSystem = new JLabelRechts("Sistema", fontsize);
	pFields.add(lSystem, gbc);

	gbc.gridx = 3;
	gbc.weightx = 0.5;
	cbSystem = new JMyComboBox(systems[1], fontsize);
	pFields.add(cbSystem, gbc);
//
	gbc.gridx = 4;
	gbc.weightx = 0;
	lDevice = new JLabelRechts("Įrenginys", fontsize);
	pFields.add(lDevice, gbc);

	gbc.gridx = 5;
	gbc.weightx = 0.5;
	fName = new JMyTextField(15, fontsize);
	fName.addMouseListener(this);
	pFields.add(fName, gbc);
//
	gbc.gridx = 6;
	gbc.weightx = 0;
	lWork = new JLabelRechts("Darbas", fontsize);
	pFields.add(lWork, gbc);

	gbc.gridx = 7;
	gbc.weightx = 0.5;
	cbWorktype = new JMyComboBox(worktypes[1], fontsize);
	pFields.add(cbWorktype, gbc);
//
	gbc.gridx = 8;
	gbc.weightx = 0;
	lWork = new JLabelRechts("Būsena", fontsize);
	pFields.add(lWork, gbc);

	gbc.gridx = 9;
	gbc.weightx = 0;
	cbState = new JMyComboBox(states[1], fontsize);
	pFields.add(cbState, gbc);
//
	gbc.gridx = 10;
	gbc.weightx = 0;
	lIDpr = new JLabelRechts("Tąsa", fontsize);
	pFields.add(lIDpr, gbc);

	gbc.gridx = 11;
	gbc.weightx = 0;
	tfIDpr = new JMyTextField(4, fontsize);
	tfIDpr.addMouseListener(this);
	pFields.add(tfIDpr, gbc);
// Η δεύτερη σειρά
	gbc.gridy = 1;

	gbc.gridx = 0;
	lFilters = new JLabelRechts("Filtrai:", fontsize);
	lFilters.setToolTipText("Sąlyga IR");
	lFilters.addMouseListener(this);
	pFields.add(lFilters, gbc);
	
	gbc.gridx = 1;
	chDate = new JCheckBox();
	chDate.setToolTipText("<html>Galimi datos lauko šablonai paieškai:<br> 2020% – visi 2020 m. įrašai;<br> 2020-10% – visi 2020 m. 10 mėn. įrašai;<br> %-10-% – visi visų 10-ojo mėn. įrašai.</html>");
	pFields.add(chDate, gbc);

	gbc.gridx = 3;
	chSystem = new JCheckBox();
	pFields.add(chSystem, gbc);

	gbc.gridx = 5;
	chDevice = new JCheckBox();
	chDevice.addActionListener(this);
	chDevice.setActionCommand("chDevice");
	chDevice.setToolTipText("Galimi paieškos šablonai. Pvz.: %onitor% – bus ieškoma visų įrašų, turinčių „onitor“");
	pFields.add(chDevice, gbc);

	gbc.gridx = 7;
	chWorktype = new JCheckBox();
	pFields.add(chWorktype, gbc);

	gbc.gridx = 9;
	chState = new JCheckBox();
	pFields.add(chState, gbc);

	gbc.gridx = 10;
	gbc.weightx = 0;
	lIDpr = new JLabelRechts("ID", fontsize);
	pFields.add(lIDpr, gbc);

	gbc.gridx = 11;
	gbc.weightx = 0;
	tfID = new JMyTextField(4, fontsize);
        tfID.setEnabled(false);
        tfID.addMouseListener(this);
	pFields.add(tfID, gbc);
// Η τρίτη σειρά
	gbc.gridy = 2;
	gbc.gridx = 0;
	gbc.weightx = 0;
	createPanelMessages();
	pFields.add(pMessage, gbc);

	gbc.gridx = 1;
//	gbc.weightx = 0.5;
        gbc.gridwidth = 9;
	taMessage = new JMyTextArea(10, 45, fontsize);
	taMessage.setFocusAccelerator('A');
        taMessage.addMouseListener(this);
	taMessage.setLineWrap(true);
	taMessage.setWrapStyleWord(true);
//        taMessage.setToolTipText("Dvigubas spragtelėjimas ištrina tekstą iš šio lauko");
	scrPaneMessage = new JScrollPane(taMessage);
	pFields.add(scrPaneMessage, gbc);
	
	gbc.gridx = 12;
	gbc.gridwidth = 2;
	gbc.gridx = 10;
	gbc.gridwidth = 2;
	createPanelEditButtons();
	pFields.add(pEditButtons, gbc);
//
//
//
//// Η τρίτη σειρά
//	gbc.gridy = 2;
//	gbc.gridx = 0;
//	pFields.add(labelNotes, gbc);
//	gbc.gridx = 1;
//	gbc.gridwidth = 8;
//	pFields.add(textAreaNotes, gbc);
//
//
    }

    public void setConnection(ConnectionEquipment the_connection) {
	    connection = the_connection;
	    enableButtons(true);
//	if (connection == null) {
//	    connection = the_connection;
//	}
    }

    public void disconnect() {
	connection = null;
//	enableButtons(false);
    }

    protected void getSystems() {
	try {
	    systems = connection.getSystems();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    protected void getWorktypes() {
	try {
	    worktypes = connection.getWorkTypes();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    protected void getStates() {
	try {
	    states = connection.getStates();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void createTable() {
	tableModel = new DefaultTableModel(new Object[]{ID, ID_PR, USER, DATA, SISTEMA, IRENGINYS, DARBAS, BUSENA, APRASYMAS}, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setDefaultEditor(Object.class, null);
        table.addMouseListener(this);
        table.setToolTipText("Dvigubas spragtelėjimas išfiltruoja susijusius įrašus");
//	table.setAutoCreateRowSorter(true);
	table.getSelectionModel().addListSelectionListener(this);
	setColumnsWidths();
//	setzt_dieUeberschriften();
	scrPaneTable = new JScrollPane(table);
    }

    private void setColumnsWidths() {
	TableColumn column;
	    for (int i = 0; i < table.getColumnCount(); i++) {
		column = table.getColumnModel().getColumn(i);
		if (tableModel.getColumnName(i).equals(BUSENA) || tableModel.getColumnName(i).equals(DARBAS) || tableModel.getColumnName(i).equals(USER)) {
		    column.setMaxWidth(120);
		    column.setPreferredWidth(100);
		} else	if (tableModel.getColumnName(i).equals(ID) || tableModel.getColumnName(i).equals(ID_PR)) {
		    column.setMaxWidth(60);
		    column.setPreferredWidth(50);
		} else if (tableModel.getColumnName(i).equals(SISTEMA) || tableModel.getColumnName(i).equals(IRENGINYS)) {
		    column.setMaxWidth(300);
		    column.setPreferredWidth(150);
		}
		//		if (tableModel.getColumnName(i).equals(IRENGINYS) || tableModel.getColumnName(i).equals(SISTEMA)) {
		//		    column.setMaxWidth(100);
		//		    column.setPreferredWidth(80);
		//		}
		else if (tableModel.getColumnName(i).equals(APRASYMAS)) {
                    column.setPreferredWidth(500);
		} else {
                    column.setMaxWidth(200);
                    column.setPreferredWidth(100);
		}
   	    }
    }

//    private void setzt_dieUeberschriften(){
//	table.getTableHeader().setPreferredSize(new Dimension(table.getWidth(), 60));
//        table.getColumnModel().getColumn(1).setHeaderValue("<html>Der<br>Typ</html>");
//    }

    private void filter_all() {
        Object[] row;
	int i, colcount;
	tableModel.setRowCount(0);
        ResultSet resultset;
	try {
	    if (preparedSelectAll == null) {
		preparedSelectAll = connection.prepareStatement(PREPARE_SELECT_ALL);
	    }
	    resultset = preparedSelectAll.executeQuery();
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
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    protected void filter_unfinished() {
        Object[] row;
	boolean idpr_ismepty;
	int i, colcount;
	tableModel.setRowCount(0);
        ResultSet resultset;
	idpr_ismepty = tfIDpr.getText().isEmpty();
	try {
	    if (idpr_ismepty) {
		preparedFilter = connection.prepareStatement(PREPARE_UNFINISHED);
		resultset = preparedFilter.executeQuery();
	    }
	    else {
		i = Integer.valueOf(tfIDpr.getText());
		preparedFilter = connection.prepareStatement(PREPARE_UNFINISHED_ID);
		preparedFilter.setInt(1, i);
		preparedFilter.setInt(2, i);
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
	    preparedFilter.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    protected void filter_info() {
        Object[] row;
//	boolean idpr_ismepty;
	int i, colcount;
	tableModel.setRowCount(0);
        ResultSet resultset;
//	idpr_ismepty = tfIDpr.getText().isEmpty();
	try {
//	    if (idpr_ismepty) {
		preparedFilter = connection.prepareStatement(PREPARE_INFO);
		resultset = preparedFilter.executeQuery();
//	    }
//	    else {
//		i = Integer.valueOf(tfID.getText());
//		preparedFilter = connection.prepareStatement(PREPARE_INFO_ID);
//		preparedFilter.setInt(1, i);
//		preparedFilter.setInt(2, i);
//		resultset = preparedFilter.executeQuery();
//	    }
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
    

    private void filter() {
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

//SELECT d.ID, d.IDpr, v.Vardas, d.Data, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Busena, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID
    private StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(PREPARE_SELECT);
        if (chDevice != null & chState != null & chWorktype != null & chMessage != null) {
            if (chDate.isSelected() || chDevice.isSelected() || chState.isSelected() || chSystem.isSelected() || chWorktype.isSelected() || chMessage.isSelected()) {
                sb.append(" WHERE");
                if (chDate.isSelected()) {
                    sb.append(" d.Data LIKE ?");
                }
                if (chSystem.isSelected()) {
                    appendAND(sb);
                    sb.append(" d.Sistema = ?");
                }
                if (chDevice.isSelected()) {
                    appendAND(sb);
                    sb.append(" d.Irenginys LIKE ?");
                }
                if (chWorktype.isSelected()) {
                    appendAND(sb);
                    sb.append(" d.Darbas = ?");
                }
                if (chState.isSelected()) {
                    appendAND(sb);
                    sb.append(" d.Busena = ?");
                }
                if (chMessage.isSelected()) {
                    appendAND(sb);
                    sb.append(" d.Pastabos LIKE ?");
                }
            } else {
                if (!tfIDpr.getText().isEmpty()) {
                    sb.append(" WHERE d.ID = ? OR d.IDpr = ?");                      
                } else {
                    sb.append(" WHERE d.Sistema != 0");
                } 
            }
        }
	sb.append(" ORDER BY d.ID DESC");
//	System.out.println(sb.toString());
	return sb;
    }
    
    private void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n, idpr, id;
	n = 0;
        if (!tfIDpr.getText().isEmpty()) {
            idpr = Integer.valueOf((String) tfIDpr.getText()); 
        } else {
            idpr = 0;
        }
        if (!tfID.getText().isEmpty()) {
            id = Integer.valueOf((String) tfID.getText()); 
        } else {
            id = 0;
        }
	i = sb.indexOf(" d.Data LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, (String) tfDate.getText());
	}
	i = sb.indexOf(" d.Sistema = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));	    
	}
	i = sb.indexOf(" d.Irenginys LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, (String) fName.getText());
	}
	i = sb.indexOf(" d.Darbas = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(worktypes[0][cbWorktype.getSelectedIndex()]));
	}
	i = sb.indexOf(" d.Busena = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(states[0][cbState.getSelectedIndex()]));
	}
	i = sb.indexOf(" d.ID = ?");
	if (i >= 0) {
 	    n++;
            if (idpr > 0) {
                preparedFilter.setInt(n, idpr);	                
                n++;
                preparedFilter.setInt(n, idpr);
            } else {
                preparedFilter.setInt(n, id);
                n++;
                preparedFilter.setInt(n, id);
            }
	}
	i = sb.indexOf(" d.Pastabos LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, (String) taMessage.getText());
	}
	
    }

//    private void clearCheckboxes() {
//	chDate.setSelected(false);
//	chDevice.setSelected(false);
//	chState.setSelected(false);
//	chSystem.setSelected(false);
//	chWorktype.setSelected(false);
//    }

    protected void setLikeFilter() {
	if (chDevice.isSelected()) {
	    if (!fName.getText().contains("%")) {
		fName.setText("%");
	    }
	}
    }

    protected StringBuilder appendAND(StringBuilder sb) {
	if (sb.indexOf("?") >= 0) {
	    sb.append(" AND");
	}
	return sb;
    }
    
    private void insert() {
	int id, state, idpr;
	boolean clossed;
        id = get_userid_by_name(connection.get_username());
        if (id < 0) {
            JOptionPane.showMessageDialog(this, "Tokio vartotojo nėra.", "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
	try {
	    idpr = get_int(tfIDpr.getText());
	    state = Integer.valueOf(states[0][cbState.getSelectedIndex()]);
	    if (preparedInsert == null) {
		preparedInsert = connection.prepareStatement(PREPARE_INSERT);
	    }
//    (IDPr, Vartotojas, Data, Sistema, Irenginys, Darbas, Busena, Pastabos, Baigtas)
	    preparedInsert.setInt(1, get_int(tfIDpr.getText()));
	    preparedInsert.setInt(2, id);
	    preparedInsert.setString(3, tfDate.getText());
	    preparedInsert.setInt(4, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));
	    preparedInsert.setString(5, fName.getText());
	    preparedInsert.setInt(6, Integer.valueOf(worktypes[0][cbWorktype.getSelectedIndex()]));
	    preparedInsert.setInt(7, Integer.valueOf(states[0][cbState.getSelectedIndex()]));
	    clossed = state == BAIGTA;
	    preparedInsert.setString(8, taMessage.getText());
	    preparedInsert.setBoolean(9, clossed);
	    if (preparedInsert.executeUpdate() == 1) {
		filter_all();
	    }
	    if (clossed) {
		update_finish(idpr);
	    }
	} catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
//	}
    }

    private void update_acknowledge() {
	int id, idpr;
        String newmessage;
        id = get_userid_by_name(connection.get_username());
        if (id < 0) {
            JOptionPane.showMessageDialog(this, "Tokio vartotojo nėra.", "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
	try {
	    idpr = get_int(tfID.getText(), tfIDpr.getText());
            newmessage = taMessage.getText();
	    if (preparedInsert == null) {
		preparedInsert = connection.prepareStatement(PREPARE_INSERT);
	    }
//(IDPr, Vartotojas, Data, Sistema, Irenginys, Darbas, Busena, Pastabos, Baigta)
	    preparedInsert.setInt(1, get_int(tfID.getText()));
	    preparedInsert.setInt(2, id);
	    preparedInsert.setString(3, date.getToday());
	    preparedInsert.setInt(4, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));
	    preparedInsert.setString(5, fName.getText());
	    preparedInsert.setInt(6, Integer.valueOf(worktypes[0][cbWorktype.getSelectedIndex()]));
	    preparedInsert.setInt(7, SUSIPAZINAU);
            if (message.equals(newmessage)) {
                message = "";
            } else {
                message = newmessage;
            }
	    preparedInsert.setString(8, message);
	    preparedInsert.setBoolean(9, true);
	    if (preparedInsert.executeUpdate() == 1) {
		filter_all();
	    }
	    update_finish(idpr);
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
        
    }
    
//    (Data, IDpr, Sistema, Irenginys, Darbas, Busena, Pastabos, Baigta, ID)
    private void update() {
	int row, state, idpr;
	boolean clossed;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		idpr = get_int(tfIDpr.getText());
		state = Integer.valueOf(states[0][cbState.getSelectedIndex()]);
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
		};
		preparedUpdate.setString(1, tfDate.getText());
		preparedUpdate.setInt(2, idpr);
		preparedUpdate.setInt(3, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));
		preparedUpdate.setString(4, fName.getText());
		preparedUpdate.setInt(5, Integer.valueOf(worktypes[0][cbWorktype.getSelectedIndex()]));
		preparedUpdate.setInt(6, state);
		preparedUpdate.setString(7, taMessage.getText());
		clossed = state == BAIGTA;
		preparedUpdate.setBoolean(8, clossed);
		preparedUpdate.setInt(9, (int) table.getValueAt(row, 0));
		if (preparedUpdate.executeUpdate() == 1) {
		    filter_all();
		};
		if (clossed) {
		    update_finish(idpr);
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }
    
    private void update_finish(int idpr) {
	if (idpr > 0) {
	    try {
		if (preparedUpdateFinish == null) {
		    preparedUpdateFinish = connection.prepareStatement(PREPARE_UPDATE_FINISH);
		}
//		System.out.println(idpr);
		preparedUpdateFinish.setInt(1, idpr);
		preparedUpdateFinish.setInt(2, idpr);
		preparedUpdateFinish.executeUpdate();
//		System.out.println(preparedUpdateFinish.executeUpdate());
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    private int get_int(String str) {
	if (str.isEmpty()) {
	    return 0;
	} else {
	    return Integer.valueOf(str);
	}
    }
    
    private int get_int(String idstr, String idtasastr) {
	int id, idtasa;
	if (idtasastr.isEmpty()) {
	    idtasa = 0;
	} else {
	    idtasa = Integer.valueOf(idtasastr);
	}
	id = Integer.valueOf(idstr);
	id = idtasa > 0 ? idtasa : id;
	return id;
    }
    

    private void delete() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedDelete == null) {
		    preparedDelete = connection.prepareStatement(PREPARE_DELETE);
		}
// ID, IT, Nr, Pavadinimas, Sistema
		preparedDelete.setInt(1, (int) table.getValueAt(row, 0));
		if (preparedDelete.execute()) {
		    filter_all();
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	}  else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
//	int [] rows;
//        int l, i;
//	StringBuilder statement;
//	rows = table.getSelectedRows();
//        l = rows.length;
//	if (l >= 0) {
//	    statement = new StringBuilder(DELETE);
//            for (i = 1; i <= l; i++) {
//                statement.append(table.getValueAt(rows[i-1], 0));
//                if (i < l) {
//                    statement.append(" OR ID = ");
//                }
//            }
//	    try {
//		if (connection.executeUpdate(statement.toString()) == 1) {
//		    filter_all();
//                    System.out.println();
//		};
//	    } catch (SQLException ex) {
//		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
//	    }
//	}

    private String getTableCellString(int row, int col) {
	Object o;
	o = tableModel.getValueAt(row, col);
	if (o != null) {
	    return o.toString();
	} else {
	    return "";
	}
    }

    protected void setComboBoxItem(JComboBox cb, String[] s, String name) {
	int i, n;
	boolean found;
	i = 0;
	found = false;
	n = cb.getItemCount();
	while (i <= n & !found) {
	    if (s[i].equals(name)) {
		found = true;
		cb.setSelectedIndex(i);
	    }
	    i++;
	}
    }
    
    private int get_userid_by_name(String name) {
	int i, n, id;
	boolean found;
	i = 0;
	found = false;
	n = users[1].length;
	while (i < n & !found) {
	    if (users[1][i].equals(name)) {
		found = true;
	    }
	    else {
		i++;
	    }
	}
        if (found) {
            id = Integer.valueOf(users[0][i]);
        }
        else {
            id = -1;
        }
	return id;

    }

    private void enableButtons(boolean enabled) {
	if (bAdd != null){
	    bAdd.setEnabled(enabled);
	}
	if (btChange != null){
	    btChange.setEnabled(enabled);	    
	}
	if (btUnfinished != null) {
	    btUnfinished.setEnabled(enabled);
	}
	if (btFilter != null) {
	    btFilter.setEnabled(enabled);
	}
//	btFilter.setEnabled(enabled);
    }

    private void enableChangeButton(boolean enabled) {
//        tfDate.setEnabled(enabled);
//        tfEquipment.setEnabled(enabled);
//        tfIDpr.setEnabled(enabled);
//        taMessage.setEnabled(enabled);
//        cbState.setEnabled(enabled);
//        cbSystem.setEnabled(enabled);
//        cbWorkType.setEnabled(enabled);
        btChange.setEnabled(enabled);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "update":
		update();
		break;
	    case "all":
		filter_all();
		break;
	    case "unfinished":
		filter_unfinished();
		break;		
	    case "info":
		filter_info();
		break;
	    case "filter":
		filter();
		break;
	    case "acknowledge":
		update_acknowledge();
		break;
	    case "insert":
		insert();
		break;
	    case "delete":
		delete();
		filter_all();
		break;
//	    case "chDevice":
//		setLikeFilter();
//		break;
	}

    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
	selectedRow = table.getSelectedRow();
	if (selectedRow >= 0) {
	    if (!user.equals((String) table.getValueAt(selectedRow, 2))) {
		enableChangeButton(false);
	    } else {
                enableChangeButton(true);
	    }
            message = getTableCellString(selectedRow, 8);
            tfID.setText(getTableCellString(selectedRow, 0));
	    tfIDpr.setText(getTableCellString(selectedRow, 1));
	    tfDate.setText(getTableCellString(selectedRow, 3));
	    setComboBoxItem(cbSystem, systems[1], getTableCellString(selectedRow, 4));
	    fName.setText(getTableCellString(selectedRow, 5));
	    setComboBoxItem(cbWorktype, worktypes[1], getTableCellString(selectedRow, 6));
	    setComboBoxItem(cbState, states[1], getTableCellString(selectedRow, 7));
	    taMessage.setText(message);
	    }
	}
//	int zeile, spalte;
//	zeile = table.getSelectedRow();
//	spalte = table.getSelectedColumn();
//	if (lse.getValueIsAdjusting()) {
//	    table.setValueAt((String.valueOf(table.getValueAt(zeile, spalte)).replace(",", ".")), zeile, spalte);
//	}
//    }

//    private void updateComboBoxes() {
//    }
    @Override
    public void mouseClicked(MouseEvent me) {
	if (me.getComponent().equals(tfDate) & me.getClickCount() == 2) {
	    tfDate.setText(date.getToday());
	}
        if (me.getComponent().equals(tfID)) {
          tfIDpr.setText(tfID.getText());
        }
	if (me.getComponent().equals(fName) & me.getClickCount() == 4) {
	    fName.setText("%%");
	}
	if (me.getComponent().equals(tfIDpr)) {
          tfIDpr.setText("");
        }
//	if (me.getComponent().equals(lFilters)) {
//	    clearCheckboxes();
//        }
 	if (me.getComponent().equals(taMessage)) {
//	    if (me.getClickCount() == 2) {
//		taMessage.setText("");
//	    }
	    if (me.getClickCount() == 4) {
		taMessage.setText("%%");
	    }	    
	}
	if (me.getComponent().equals(table) & me.getClickCount() == 2) {
	    filter();
        }	
        
    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

}
