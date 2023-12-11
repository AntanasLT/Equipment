/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import datum.Datum;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author a
 */
public class Darbai extends JPanel implements ActionListener, MouseListener {

    private static final String PREPARE_SELECT_ALL = "SELECT d.ID, d.IDpr, v.Pavadinimas, d.Laikas, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Pavadinimas, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID ORDER BY d.ID DESC LIMIT 50";
    private static final String PREPARE_SELECT = "SELECT d.ID, d.IDpr, v.Pavadinimas, d.Laikas, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Pavadinimas, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID ";
    private static final String PREPARE_INFO = "SELECT d.ID, d.IDpr, v.Pavadinimas, d.Laikas, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Pavadinimas, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID WHERE dt.ID = 6 ORDER BY d.ID DESC LIMIT 50";
    private static final String PREPARE_UNFINISHED = "SELECT d.ID, d.IDpr, v.Pavadinimas, d.Laikas, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Pavadinimas, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID WHERE d.Baigta = FALSE ORDER BY d.ID DESC";
//    private static final String PREPARE_UNFINISHED_ID = "SELECT d.ID, d.IDpr, v.Pavadinimas, d.Laikas, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Pavadinimas, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID WHERE d.Baigta = FALSE AND (d.ID = ? OR d.IDpr = ?) ORDER BY d.ID DESC";
    private static final String PREPARE_UPDATE = "UPDATE Darbai SET Laikas = ?, IDPr = ?,  Sistema = ?, Irenginys = ?, Darbas = ?, Busena = ?, Pastabos = ?, Baigta = ? WHERE ID = ?";
    private static final String PREPARE_FINISHED = "UPDATE Darbai SET Laikas = Laikas, Baigta = TRUE WHERE ID = ? OR IDpr = ?";
    private static final String PREPARE_INSERT = "INSERT INTO Darbai (IDPr, Vartotojas, Laikas, Sistema, Irenginys, Darbas, Busena, Pastabos, Baigta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    private static final String PREPARE_SELECT = "";
    private static final String PREPARE_DELETE = "DELETE FROM Darbai WHERE ID = ?";
    protected static final String ID = "ID";
    private static final String ID_PR = "Tąsa";
    private static final String USER = "Vartotojas";
    private static final String DATA = "Laikas";
    private static final String SISTEMA = "Sistema";
    private static final String IRENGINYS = "Irenginys";
    private static final String DARBAS = "Darbas";
    private static final String BUSENA = "Būsena";
    private static final String APRASYMAS = "Aprašymas";
    private static final int BAIGTA = 3;
    private static final int SUSIPAZINAU = 5;
    protected static String LIMIT = "50";
    private static final String TABLE_TOOLTIP = "Dvigubas spragtelėjimas išfiltruoja susijusius įrašus";
    private static final String BASIC_FILE_DIR = "Zurnalas/";


    ConnectionEquipment connection;
    Datum date;
    protected DefaultTableModel tableModel, tableModelElevators;
    GridBagConstraints gbc;
    JCheckBox chDate, chSystem, chDevice, chWorktype;
    private JCheckBox chState;
    JLabelLeft lMessage;
    JLabelRechts lDate, lDevice, lSystem, lWork, lIDpr, lFilters;
    protected JMyButton bDelete, bAdd, btChange, btAcknowl, btFilter, btUnfinished, btAll, btInfo;
    protected JMyCheckBox chMessage;
    JMyComboBox cbWorktype, cbState;
    JMyComboBox cbIrenginys;
    protected JPanel pInput, pnFIlterButtons, pFields;
    JPanel pEditButtons, pMessage;
    protected PreparedStatement preparedUpdate, preparedInsert, preparedSelectAll, preparedDelete, preparedFilter, preparedUpdateFinish;
//    JRadioButton radioButton1;
    JScrollPane scrTable, scrMessage;
    JTable table, tableElevators;
    JMyTextField tfDate, tfName, tfIDpr, tfID;
    JMyTextArea_monospaced taMessage;
    JMyMenuItem menuItemLiftai;
    JMyPopupMenu popupLiftai;
    JOptionPane elevatorSelectionPane;

    int the_row, fontsize;
    int[] table_column_width;
    String user, message, server;
    String[][] systems, users;
    private String[][] states, worktypes;
    String[] table_columns;    
    Font font;
    protected String select, delete, insert, tableTooltip;
 


    protected Darbai(ConnectionEquipment the_connection, String the_server, int size) {
        fontsize = size;
	connection = the_connection;
	server = the_server;
    }

    protected void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
        table_columns = new String[]{ID, ID_PR, USER, DATA, SISTEMA, IRENGINYS, DARBAS, BUSENA, APRASYMAS};
        table_column_width = new int[]{4*fontsize, 4*fontsize, 8*fontsize, 12*fontsize, 7*fontsize, 16*fontsize, 8*fontsize, 8*fontsize, 41*fontsize};
	if (connection != null) {
	    try {
		users = connection.get_users();
                user = connection.get_username();
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	    }
	    date = new Datum();
            setLayout(new BorderLayout());
            getSystems();
            getWorktypes();
            setConstants();
            getStates();
            createTable();
            createPanelInput();
            add(pInput, BorderLayout.NORTH);
            add(scrTable, BorderLayout.CENTER);
            add(lMessage, BorderLayout.SOUTH);
            setVisible(true);
	} else {
	    JOptionPane.showMessageDialog(this, "Nerisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    protected void setConstants(){
	select = PREPARE_SELECT_ALL;
        insert = PREPARE_INSERT;
        tableTooltip = TABLE_TOOLTIP;
    }
    

    protected void createPanelInput() {
        pInput = new JPanel(new BorderLayout());
        createPanelFields();
        pInput.add(pFields, BorderLayout.NORTH);
        createPanelFilterButtons();
        pInput.add(pnFIlterButtons, BorderLayout.SOUTH);
        lMessage = new JLabelLeft(fontsize);
    }

    protected void createPanelMessages() {
	pMessage = new JPanel(new GridLayout(2, 1, 2, 2));
//	lMessage = new JLabelLeft("Aprašymas", fontsize);
	chMessage = new JMyCheckBox("Aprašymas", false, fontsize);
//	pMessage.add(lMessage);
	pMessage.add(chMessage);
    }

    protected void createPanelFilterButtons() {
	pnFIlterButtons = new JPanel();
	btAll = new JMyButton("Naujausieji", fontsize);
	btAll.setActionCommand("all");
        btAll.setToolTipText(LIMIT + " naujausiųjų");
	btAll.addActionListener(this);
	pnFIlterButtons.add(btAll);
	btUnfinished = new JMyButton("Nebaigtieji", fontsize);
        btUnfinished.setForeground(Color.RED);
	btUnfinished.setActionCommand("unfinished");
	btUnfinished.addActionListener(this);
	pnFIlterButtons.add(btUnfinished);
	btInfo = new JMyButton("Info", fontsize);
        btInfo.setForeground(Color.MAGENTA);
	btInfo.setActionCommand("info");
	btInfo.addActionListener(this);
	pnFIlterButtons.add(btInfo);
	btFilter = new JMyButton("Filtruoti", fontsize);
	btFilter.setActionCommand("filter");
	btFilter.addActionListener(this);
	pnFIlterButtons.add(btFilter);
    }
    
    protected void createPanelEditButtons() {
	pEditButtons = new JPanel(new GridLayout(3, 1, 0, 10));
	btChange = new JMyButton("Išsaugoti", fontsize);
        btChange.setForeground(Color.ORANGE);
        btChange.setToolTipText("Išsaugomi pakeitimai");
	btChange.setActionCommand("update");
	btChange.addActionListener(this);
	bAdd = new JMyButton("Naujas", fontsize);
        bAdd.setToolTipText("Sukuriamas naujas įrašas iš laukuose esančių duomenų");
        bAdd.setForeground(Color.BLUE);
	bAdd.setActionCommand("insert");
	bAdd.addActionListener(this);
	btAcknowl = new JMyButton("Susipažinau", fontsize);
        btAcknowl.setForeground(Color.GREEN);
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

// Η πρώτη σειρά
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0;
	lDate = new JLabelRechts("Laikas:", fontsize);
	pFields.add(lDate, gbc);

	gbc.gridx = 1;
	gbc.weightx = 0;
	tfDate = new JMyTextField("", 14, fontsize);
	setCurrTime();
	tfDate.addMouseListener(this);
	tfDate.setToolTipText("Dukart spragt – dabartinis laikas");
	pFields.add(tfDate, gbc);

	gbc.gridx = 2;
	gbc.weightx = 0;
	lSystem = new JLabelRechts("Sistema", fontsize);
	pFields.add(lSystem, gbc);

	gbc.gridx = 3;
	gbc.weightx = 0.5;
	cbIrenginys = new JMyComboBox(systems[1], fontsize);
	pFields.add(cbIrenginys, gbc);
//
	gbc.gridx = 4;
	gbc.weightx = 0;
	lDevice = new JLabelRechts("Įrenginys", fontsize);
	pFields.add(lDevice, gbc);
        
	gbc.gridx = 5;
	gbc.weightx = 0.5;
	tfName = new JMyTextField(15, fontsize);
	tfName.setToolTipText("Šiam laukui yra kontekstinis meniu (liftų sąrašas)");
	tfName.addMouseListener(this);
	pFields.add(tfName, gbc);
        popupLiftai = new JMyPopupMenu(fontsize);
        menuItemLiftai = new JMyMenuItem("Liftų sąrašas", fontsize);
        menuItemLiftai.setActionCommand("liftu_sarasas");
        menuItemLiftai.addActionListener(this);
        popupLiftai.add(menuItemLiftai);
	tfName.setComponentPopupMenu(popupLiftai);
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
//	chDate.setToolTipText("<html>Galimi datos lauko šablonai paieškai:<br> 2020% – visi 2020 m. įrašai;<br> 2020-10% – visi 2020 m. 10 mėn. įrašai;<br> %-10-% – visi visų 10-ojo mėn. įrašai.</html>");
	pFields.add(chDate, gbc);

	gbc.gridx = 3;
	chSystem = new JCheckBox();
	pFields.add(chSystem, gbc);

	gbc.gridx = 5;
	chDevice = new JCheckBox();
	chDevice.addActionListener(this);
	chDevice.setActionCommand("chDevice");
//	chDevice.setToolTipText("Galimi paieškos šablonai. Pvz.: %onitor% – bus ieškoma visų įrašų, turinčių „onitor“");
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
        gbc.gridwidth = 10;
	taMessage = new JMyTextArea_monospaced(10, 45, fontsize);
//	taMessage.setFocusAccelerator('A');
        taMessage.addMouseListener(this);
	taMessage.setLineWrap(true);
	taMessage.setWrapStyleWord(true);
//        taMessage.setToolTipText("Dvigubas spragtelėjimas ištrina tekstą iš šio lauko");
	scrMessage = new JScrollPane(taMessage);
	pFields.add(scrMessage, gbc);
	
//	gbc.gridx = 12;
//	gbc.gridwidth = 2;
	gbc.gridx = 11;
	gbc.gridwidth = 1;
	createPanelEditButtons();
	pFields.add(pEditButtons, gbc);
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
	    systems = connection.getList("Sistemos", "ID", "Pavadinimas", "Pavadinimas");
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void getWorktypes() {
	try {
	    worktypes = connection.getList("Darbotipis", "ID", "Pavadinimas", "Pavadinimas");
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    protected void getStates() {
	try {
	    states = connection.getList("Busenos", "ID", "Pavadinimas", "Pavadinimas");
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    protected String replaceComma(String s){
        return s.replace(',', '.');
    }

    protected void createTable() {
	tableModel = new DefaultTableModel(table_columns, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setDefaultEditor(Object.class, null);
        table.addMouseListener(this);
        if (!tableTooltip.isEmpty()) {
            table.setToolTipText(tableTooltip);
        }
	table.setAutoCreateRowSorter(true);
	setColumnsWidths();
	scrTable = new JScrollPane(table);
    }

    protected void setColumnsWidths() {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(table_column_width[i]);
            }
    }
    
    /**
     *
     * @param row_limit Αριθμός των σειρών
     * @param colcount Αριθμός των στηλών
     * @param limit Προσαρμογή του ορίου
     * @return
     */
    protected Object summary_row(int row_limit, int colcount, boolean limit) {
        Object[] therow;
        therow = new Object[colcount];
        for (int i = 1; i <= colcount - 1; i++) {
            therow[i] = "";
        }
        therow[0] = "Iš viso:";
        therow[1] = row_limit;
        if (row_limit >= 50 & limit) {
            therow[3] = "(rib. 50)";
        }
        return therow;
    }
    

    private void filter_all() {
        Object[] therow;
	int i, n, colcount;
	tableModel.setRowCount(0);
        ResultSet resultset;
        n = 0;
	try {
	    if (preparedSelectAll == null) {
		preparedSelectAll = connection.prepareStatement(select);
	    }
	    resultset = preparedSelectAll.executeQuery();
	    colcount = tableModel.getColumnCount();
	    therow = new Object[colcount];
	    while( resultset.next() ){
		for (i = 0; i <= colcount - 1; i++) {
		    therow[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(therow);
                n++;
	    }
            tableModel.addRow((Object[]) summary_row(n, colcount, true));
	    resultset.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    private void filter_unfinished() {
        Object[] row;
	int i, n, colcount;
	tableModel.setRowCount(0);
        ResultSet resultset;
        n = 0;
	try {
		preparedFilter = connection.prepareStatement(PREPARE_UNFINISHED);
		resultset = preparedFilter.executeQuery();
	    colcount = tableModel.getColumnCount();
	    row = new Object[colcount];
	    while( resultset.next() ){
		for (i = 0; i <= colcount - 1; i++) {
		    row[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(row);
                n++;
	    }
	    resultset.close();
            tableModel.addRow((Object[]) summary_row(n, colcount, true));
	    preparedFilter.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    private void filter_info() {
        Object[] row;
	int i, n, colcount;
	tableModel.setRowCount(0);
        ResultSet resultset;
        n = 0;
	try {
		preparedFilter = connection.prepareStatement(PREPARE_INFO);
		resultset = preparedFilter.executeQuery();
	    colcount = tableModel.getColumnCount();
	    row = new Object[colcount];
	    while( resultset.next() ){
		for (i = 0; i <= colcount - 1; i++) {
		    row[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(row);
                n++;
	    }
	    resultset.close();
            tableModel.addRow((Object[]) summary_row(n, colcount, true));
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }
    

    protected void filter() {
	int i, n, colcount;
        Object[] row;
	StringBuilder sb;
	tableModel.setRowCount(0);
        ResultSet resultset;
        n = 0;
	try {
	    sb = prepareFilter();
            preparedFilter = connection.prepareStatement(sb.toString());
            preparedFilter_setPrepared(sb);
            resultset = preparedFilter.executeQuery();
            colcount = tableModel.getColumnCount();
            row = new Object[colcount];
            while (resultset.next() ){
                for (i = 0; i <= colcount - 1; i++) {
                    row[i] = resultset.getObject(i + 1);
                }
                tableModel.addRow(row);
                n++;
            }
            resultset.close();
            tableModel.addRow((Object[]) summary_row(n, colcount, false));
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }

//SELECT d.ID, d.IDpr, v.Pavadinimas, d.Laikas, sist.Pavadinimas, d.Irenginys, dt.Pavadinimas, b.Pavadinimas, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.Vartotojas = v.ID LEFT JOIN Busenos b ON d.Busena = b.ID
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(PREPARE_SELECT);
        if (chDevice != null & chState != null & chWorktype != null & chMessage != null) {
            if (chDate.isSelected() || chDevice.isSelected() || chState.isSelected() || chSystem.isSelected() || chWorktype.isSelected() || chMessage.isSelected()) {
                sb.append(" WHERE");
                if (chDate.isSelected()) {
		    sb.append(" d.Laikas LIKE ?");
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
                    sb.append(" WHERE d.Sistema = -1");
                    JOptionPane.showMessageDialog(this, "Neįvestas nė vienas filtravimo kriterijus", "Nepavyko", JOptionPane.INFORMATION_MESSAGE);
                } 
            }
        }
	sb.append(" ORDER BY d.ID DESC");
	return sb;
    }
    
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n, idpr, id;
	n = 0;
        if (!tfIDpr.getText().isEmpty()) {
            idpr = Integer.parseInt(tfIDpr.getText()); 
        } else {
            idpr = 0;
        }
        if (!tfID.getText().isEmpty()) {
            id = Integer.parseInt(tfID.getText()); 
        } else {
            id = 0;
        }
	i = sb.indexOf(" d.Laikas LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, "%".concat(tfDate.getText().concat("%")));
	}
	i = sb.indexOf(" d.Sistema = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));	    
	}
	i = sb.indexOf(" d.Irenginys LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, "%".concat(tfName.getText().concat("%")));
	}
	i = sb.indexOf(" d.Darbas = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.parseInt(worktypes[0][cbWorktype.getSelectedIndex()]));
	}
	i = sb.indexOf(" d.Busena = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.parseInt(states[0][cbState.getSelectedIndex()]));
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
	    preparedFilter.setString(n, "%".concat(taMessage.getText().concat("%")));
	}
	
    }

    protected String getWildcardedTxt(JTextField tf) {
	return "%" + tf.getText() + "%";
    }


    protected void setLikeFilter() {
	if (chDevice.isSelected()) {
	    if (!tfName.getText().contains("%")) {
		tfName.setText("%");
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
        String idTasa, time, state_name;
        id = get_userid_by_name(connection.get_username());
        if (id < 0) {
            JOptionPane.showMessageDialog(this, "Tokio vartotojo nėra.", "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
        idTasa = tfIDpr.getText();
//        iD = tfID.getText();
        state_name = (String) cbState.getSelectedItem();
        if (!state_name.equals("Užregistruota")) {
            idTasa = JOptionPane.showInputDialog(this, "Tęsiamo (pradinio) įrašo nr. ", idTasa);
        } else {
            idTasa = "0";
        }
        if (idTasa != null) {           
            time = JOptionPane.showInputDialog(this, "Laikas. ", tfDate.getText());
            if (time != null) {
                try {
                    idpr = get_int(idTasa);
                    state = Integer.parseInt(states[0][cbState.getSelectedIndex()]);
                    if (preparedInsert == null) {
                        preparedInsert = connection.prepareStatement(PREPARE_INSERT);
                    }
        //    (IDPr, Vartotojas, Data, Sistema, Irenginys, Darbas, Pavadinimas, Pastabos, Baigtas)
                    preparedInsert.setInt(1, idpr);
                    preparedInsert.setInt(2, id);
                    preparedInsert.setString(3, time);
                    preparedInsert.setInt(4, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));
		    preparedInsert.setString(5, tfName.getText());
                    preparedInsert.setInt(6, Integer.parseInt(worktypes[0][cbWorktype.getSelectedIndex()]));
                    preparedInsert.setInt(7, Integer.parseInt(states[0][cbState.getSelectedIndex()]));
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
                    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida (insert)!", JOptionPane.ERROR_MESSAGE);
                }            
            }
        }
    }

    private void update_acknowledge() {
	int id, idpr;
//        String newmessage;
        id = get_userid_by_name(connection.get_username());
        if (id < 0) {
            JOptionPane.showMessageDialog(this, "Tokio vartotojo nėra.", "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
	try {
	    idpr = get_int(tfID.getText(), tfIDpr.getText());
//            newmessage = taMessage.getText();
	    if (preparedInsert == null) {
		preparedInsert = connection.prepareStatement(PREPARE_INSERT);
	    }
//(IDPr, Vartotojas, Data, Sistema, Irenginys, Darbas, Pavadinimas, Pastabos, Baigta)
	    preparedInsert.setInt(1, get_int(tfID.getText()));
	    preparedInsert.setInt(2, id);
	    preparedInsert.setString(3, date.getToday() + " " + date.getTime());
	    preparedInsert.setInt(4, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));
	    preparedInsert.setString(5, tfName.getText());
	    preparedInsert.setInt(6, Integer.parseInt(worktypes[0][cbWorktype.getSelectedIndex()]));
	    preparedInsert.setInt(7, SUSIPAZINAU);
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
    
//    (Data, IDpr, Sistema, Irenginys, Darbas, Pavadinimas, Pastabos, Baigta, ID)
    protected void update() {
	int row, state, idpr;
	boolean clossed;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		idpr = get_int(tfIDpr.getText());
		state = Integer.parseInt(states[0][cbState.getSelectedIndex()]);
		clossed = state == BAIGTA;
                if (preparedUpdate == null) {
                    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
                }
                preparedUpdate.setString(1, tfDate.getText());
                preparedUpdate.setInt(2, idpr);
                preparedUpdate.setInt(3, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));
		preparedUpdate.setString(4, tfName.getText());
                preparedUpdate.setInt(5, Integer.parseInt(worktypes[0][cbWorktype.getSelectedIndex()]));
                preparedUpdate.setInt(6, state);
                preparedUpdate.setString(7, taMessage.getText());
                preparedUpdate.setBoolean(8, clossed);
                preparedUpdate.setInt(9, (int) table.getValueAt(row, 0));
                preparedUpdate.executeUpdate();    
		if (clossed) {
		    update_finish(idpr);
		}
                filter();
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	}
        else {
            JOptionPane.showMessageDialog(this, "Nepažymėta keičiama eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }    
        
    }
    
    private void update_finish(int idpr) {
	if (idpr > 0) {
	    try {
		if (preparedUpdateFinish == null) {
		    preparedUpdateFinish = connection.prepareStatement(PREPARE_FINISHED);
		}
                preparedUpdateFinish.setInt(1, idpr);
		preparedUpdateFinish.setInt(2, idpr);
		preparedUpdateFinish.executeUpdate();
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida (update_finish)!", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    private int get_int(String str) {
	if (str.isEmpty()) {
	    return 0;
	} else {
	    return Integer.parseInt(str);
	}
    }
    
    private int get_int(String idstr, String idtasastr) {
	int id, idtasa;
	if (idtasastr.isEmpty()) {
	    idtasa = 0;
	} else {
	    idtasa = Integer.parseInt(idtasastr);
	}
	id = Integer.parseInt(idstr);
	id = idtasa > 0 ? idtasa : id;
	return id;
    }
    

    private void delete() {
	int r;
	r = table.getSelectedRow();
	if (r >= 0) {
	    try {
		if (preparedDelete == null) {
		    preparedDelete = connection.prepareStatement(PREPARE_DELETE);
		}
// ID, IT, Nr, Pavadinimas, Sistema
		preparedDelete.setInt(1, (int) table.getValueAt(r, 0));
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

    private String getTableCellString(int row, int col) {
	Object o;
	o = tableModel.getValueAt(row, col);
	if (o != null) {
	    return o.toString();
	} else {
	    return "";
	}
    }

    protected void setComboBoxItem(JComboBox cb, String[] cbItems, String item) {
	int i, n;
	boolean found;
	i = 0;
	found = false;
	n = cb.getItemCount();
	cb.setEnabled(false);
	while (i < n & !found) {
	    if (cbItems[i].equals(item)) {
		found = true;
		cb.setEnabled(true);
		cb.setSelectedIndex(i);
	    }
	    i++;
	}
	if (!found) {
	    cb.setSelectedIndex(n - 1);
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
            id = Integer.parseInt(users[0][i]);
        }
        else {
            id = -1;
        }
	return id;
    }

    protected void setCurrTime() {
	tfDate.setText(date.getToday() + " " + date.getTime());

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
        btChange.setEnabled(enabled);
    }


    public void saveFile(String filename, String text) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(text);
            writer.close();
            JOptionPane.showMessageDialog(this, "Įrašyta į failą " + filename);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showElevators() {
        JScrollPane spTable;
        String SELECT = "SELECT RegNr, Vieta, Pavadinimas FROM Liftai";
        String NR = "Reg. Nr.";
        String VIETA = "Vieta";
        String PAVADINIMAS = "Pavadinimas";
	ResultSet resultset;
	resultset = null;
        elevatorSelectionPane = new JOptionPane(); 
	tableModelElevators = new DefaultTableModel(new Object[]{NR, VIETA, PAVADINIMAS}, 0);
	tableElevators = new JTable(tableModelElevators);
	tableElevators.setFont(font);
	tableElevators.getTableHeader().setFont(font);
	tableElevators.setDefaultEditor(Object.class, null);
        tableElevators.addMouseListener(this);
	tableElevators.setAutoCreateRowSorter(true);
	spTable = new JScrollPane(tableElevators);
	    try {
                resultset = connection.executeQuery(SELECT);
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	    }
	if (resultset != null) {
	    try {
                fillTable(resultset);
		resultset.close();
	    } catch (SQLException ex) {
	JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	    }
	}
        if (JOptionPane.showConfirmDialog(this, spTable, "Parinkimas", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            tableElevators.getSelectionModel().addListSelectionListener(elevListSelectionListener());
	    tfName.setText(String.valueOf(elevatorSelectionPane.getValue()));
        }
        
    }

    private void fillTable(ResultSet rs) throws SQLException {
	int i, colcount;
	Object[] row_data;
	colcount = tableModelElevators.getColumnCount();
	row_data = new Object[colcount];
	while (rs.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    row_data[i] = rs.getObject(i + 1);
		}
		tableModelElevators.addRow(row_data);
        }
    }

    private void setSystem() {
        cbIrenginys.setSelectedItem("Liftai");
    }
    
    protected void openFile(String folder, String filename) {
	if (filename != null) {
            try {
                if (filename.startsWith("http") || filename.contains("www")) {
                    Desktop.getDesktop().browse(new URL(filename).toURI());
                } else {
                    File file = new File(folder, filename);
                    if (file.exists()) {
                        Desktop.getDesktop().open(file);
                    } else {
			if (get_remote_file(folder, filename)) {
			    openFile(folder, filename);
			} else {
			    throw new IOException(filename + ": nėra!");
			}
		    }
                }
            } catch (IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean get_remote_file(String folder, String filename) {
//        Path path_from, path_to;
//        path_from = Paths.get(files[0]);
//        path_to = Paths.get("/home/a/zw/zw");
	boolean rez;
	Cursor cursor0;
	UserInfo uin;
	String msg;
//	UserInfo ui;
	JSch ssh;
	rez = false;
	Session session = null;
	Channel channel = null;
	ChannelSftp sftp;
//	Vector<ChannelSftp.LsEntry> list;
	msg = "Atnaujinta";
	cursor0 = getCursor();
	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	try {
	    ssh = new JSch();
	    session = ssh.getSession("pamain", server, 22);
	    session.setConfig("StrictHostKeyChecking", "no");
	    uin = new MyUserInfo();
	    session.setUserInfo(uin);
	    session.setPassword("Terp 38-2021".getBytes());
	    session.connect();
	    channel = session.openChannel("sftp");
	    channel.connect();
	    channel.run();
	    sftp = (ChannelSftp) channel;
	    sftp.cd(BASIC_FILE_DIR + folder);
	    sftp.get(filename, folder);
	    rez = true;

	} catch (JSchException | SftpException e) {
	    msg = "Neatnaujinta!";
	    JOptionPane.showMessageDialog(this, e.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	} finally {
	    if (channel != null) {
		channel.disconnect();
	    }
	    if (session != null) {
		session.disconnect();
	    }
	    JOptionPane.showMessageDialog(this, msg, "Informacija", JOptionPane.INFORMATION_MESSAGE);
	    super.setCursor(cursor0);
	}
//
//        if (Files.exists(path_from)) {
//            try {
//                Files.copy(path_from, path_to);
//            } catch (IOException ex) {
//
//            }
//        }
	return rez;
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
	String command;
	command = e.getActionCommand();
	switch (command) {
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
	    case "liftu_sarasas":
		showElevators();
                setSystem();
		break;
	}

    }

    private ListSelectionListener elevListSelectionListener() {
        int row_nr;
        row_nr = tableElevators.getSelectedRow();
        if (row_nr >= 0) {
            elevatorSelectionPane.setValue(tableElevators.getValueAt(row_nr, 0) + " " + tableElevators.getValueAt(row_nr, 1));
        }
        return null;
    }


    @Override
    public void mouseClicked(MouseEvent me) {
	if (me.getComponent().equals(tfDate) & me.getClickCount() == 2) {
	    setCurrTime();
	}
        if (me.getComponent().equals(tfID)) {
          tfIDpr.setText(tfID.getText());
        }
//	if (me.getComponent().equals(tfIDpr)) {
//          tfIDpr.setText("");
//        }
	if (me.getComponent().equals(table) & me.getClickCount() == 2) {
	    filter();
        }
	if (me.getComponent().equals(table)) {
	the_row = table.getSelectedRow();
            if (the_row >= 0) {
                if (!user.equals(table.getValueAt(the_row, 2))) {
                    enableChangeButton(false);
                } else {
                    enableChangeButton(true);
                }
                message = getTableCellString(the_row, 8);
                tfID.setText(getTableCellString(the_row, 0));
                tfIDpr.setText(getTableCellString(the_row, 1));
                tfDate.setText(getTableCellString(the_row, 3));
                setComboBoxItem(cbIrenginys, systems[1], getTableCellString(the_row, 4));
		tfName.setText(getTableCellString(the_row, 5));
                setComboBoxItem(cbWorktype, worktypes[1], getTableCellString(the_row, 6));
                setComboBoxItem(cbState, states[1], getTableCellString(the_row, 7));
                taMessage.setText(message);
            }
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

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

	@Override
	public String getPassphrase() {
	    return null;
	}

	@Override
	public String getPassword() {
	    return null;
	}

	@Override
	public boolean promptPassphrase(String arg0) {
	    return false;
	}

	@Override
	public boolean promptPassword(String arg0) {
	    return false;
	}

	@Override
	public boolean promptYesNo(String arg0) {
	    return false;
	}

	@Override
	public void showMessage(String arg0) {
	}

	@Override
	public String[] promptKeyboardInteractive(String arg0, String arg1,
		String arg2, String[] arg3, boolean[] arg4) {
	    return null;
	}
    }


}
