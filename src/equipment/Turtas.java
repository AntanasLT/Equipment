/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author a
 */
public class Turtas extends Darbai {

    private static final String SELECT_ALL = "SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas, v.Pavadinimas, i.Pozymis, i.Pastaba, i.Data, k.Pavadinimas FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID LEFT JOIN Vietos v ON i.Vieta = v.ID LEFT JOIN Veiklos k ON i.Veikla = k.ID ORDER BY i.IT";
    private static final String SELECT = "SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas, v.Pavadinimas, i.Pozymis, i.Pastaba, i.Data, k.Pavadinimas FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID LEFT JOIN Vietos v ON i.Vieta = v.ID LEFT JOIN Veiklos k ON i.Veikla = k.ID";
    private static final String PREPARE_INSERT = "INSERT INTO Irenginiai (IT, Nr, Pavadinimas, Sistema, Data, Vieta, Pozymis, Pastaba, Veikla) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String PREPARE_UPDATE = "UPDATE Irenginiai SET IT = ?, Nr = ?, Pavadinimas = ?, Sistema = ?, Data = ?, Vieta = ?, Pozymis = ?, Pastaba = ?, Veikla = ? WHERE ID = ?";
    private static final String TEX_START = "\\documentclass[a4paper,12pt]{article}\n" +
 "\\usepackage[left=5mm, top=5mm, bottom=5mm, right=5mm]{geometry}\n"
	    + "\\usepackage[utf8x]{inputenc}\n" +
"\\usepackage[lithuanian]{babel}\n" +
"\\usepackage[L7x]{fontenc}\n" +
"\\usepackage{graphicx}\n" +
 "\\usepackage{supertabular}\n"
	    + "\n" +
 "\\begin{document}\n \\tabletail{\\hline}\n"
	    + "\\begin{supertabular}{|p{4.2cm}|p{4.2cm}|p{4.2cm}|p{4.2cm}|} \\hline\n";
    private static final String TEX_END = "\n\\end{supertabular}\n"
	    + "\\end{document}";
    private static final String BARCODES_FILE = "Barcodes";
    private static final String IT_CSV = "IT.csv";
    private static final int TEX_COL_COUNT = 4;
//    private static final String ID = "ID (auto)";
    private static final String IT = "IT";
    private static final String NR = "Nr";
//    private static final String PAVADINIMAS = "Pavadinimas";
    private static final String SISTEMA = "Sistema";
    private static final String VIETA = "Vieta";
    private static final String POZYMIS = "⊠□";
    private static final String PASTABA = "Pastaba";
    private static final String DATA = "Data";
    private static final String VEIKLA = "Veikla";
    private static final String TABLE = "Irenginiai ";
    private static final String PAVADINIMAS = "Pavadinimas";
    static final String ID = "ID (auto)";
    
    private JCheckBox chLocation, chMark, chIT, chNr, chName;
    private JLabelRechts lMark, lIT;
    protected JLabelRechts lName, lLocation, lNr;
    private JMyComboBox cbCode;
    protected JMyComboBox cbLocations;
    private JTextField fMark, fIT, fNr;
    private JTextArea ta_Message;
    protected JPanel pButtons;
    protected JMyButton btInsert, btEdit, btDelete;

//    protected DefaultTableModel tableModel;
//    protected PreparedStatement preparedUpdate, preparedInsert, preparedFilter;
    
    protected String[][] locations, codes;

    protected Turtas(ConnectionEquipment connection, int size) {
	super(connection, size);
	fontsize = size;
    }

    @Override
    protected void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
        table_columns = new String[]{ID, IT, NR, PAVADINIMAS, SISTEMA, VIETA, POZYMIS, PASTABA, DATA, VEIKLA};
        table_column_width = new int[]{4*fontsize, 8*fontsize, 8*fontsize, 25*fontsize, 8*fontsize, 8*fontsize, 2*fontsize, 16*fontsize, 8*fontsize, 12*fontsize};
	if (connection != null) {
	    setLayout(new BorderLayout());
	    setConstants();
	    createTable();
	    createPanelInput();
	    add(pInput, BorderLayout.NORTH);
	    add(scrTable, BorderLayout.CENTER);
	    setVisible(true);
	    filter_all(SELECT_ALL);
	} else {
	    JOptionPane.showMessageDialog(this, "No connection!", "Error!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    @Override
    protected void setConstants() {
	select = SELECT_ALL;
        delete = "DELETE FROM " + TABLE + "WHERE ID = ?";
        tableTooltip = "";
    }

    @Override
    protected void createPanelInput() {
	pInput = new JPanel(new BorderLayout());
	createPanelFields();
	pInput.add(pFields, BorderLayout.NORTH);
	createPanelButtons();
	pInput.add(pButtons, BorderLayout.CENTER);
	lMessage = new JLabelLeft(fontsize);
    }

    protected void createPanelButtons() {
	pButtons = new JPanel();
	super.btFilter = new JMyButton("Rodyti", fontsize);
        btFilter.setMnemonic('R');
	super.btFilter.setActionCommand("filter");
	super.btFilter.addActionListener(this);
 	pButtons.add(super.btFilter);
	btEdit = new JMyButton("Pakeisti", fontsize);
        btEdit.setMnemonic('P');
 	btEdit.addActionListener(this);
	btEdit.setActionCommand("update");
	pButtons.add(btEdit);
	btInsert = new JMyButton("Naujas", fontsize);
        btInsert.setMnemonic('N');
	btInsert.addActionListener(this);
	btInsert.setActionCommand("insert");
	pButtons.add(btInsert);
	btDelete = new JMyButton("Šalinti", fontsize);
	btDelete.addActionListener(this);
	btDelete.setActionCommand("delete");
	pButtons.add(btDelete);
    }
    
//    protected void createTable() {
//	tableModel = new DefaultTableModel(new Object[]{ID, IT, NR, PAVADINIMAS, SISTEMA, VIETA, POZYMIS, PASTABA, DATA, VEIKLA}, 0);
//	table = new JTable(tableModel);
//	table.setAutoCreateRowSorter(true);
//        table.setFont(font);
//        table.getTableHeader().setFont(font);
//	table.setDefaultEditor(Object.class, null);
//	table.addMouseListener(this);
//	table.getSelectionModel().addListSelectionListener(this);
//	setColumnsWidths();
//	tableModel.setRowCount(1);
////	setzt_dieUeberschriften();
//	scrTable = new JScrollPane(table);
//    }
//
//    private void setColumnsWidths() {
//	TableColumn dieSpalte;
//	dieSpalte = null;
//	for (int i = 0; i < table.getColumnCount(); i++) {
//	    dieSpalte = table.getColumnModel().getColumn(i);
//	    switch (tableModel.getColumnName(i)) {
//		case PAVADINIMAS:
//		    dieSpalte.setPreferredWidth(300);
//		    break;
//		case ID:
//		    dieSpalte.setPreferredWidth(20);
//		    break;
//		case POZYMIS:
//		    dieSpalte.setPreferredWidth(13);
//		    break;
//	    }
//	}
//    }
    
    private void getLocations() {
	try {
	    locations = connection.getList("Vietos", "ID", "Pavadinimas", "Pavadinimas");
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    private void getCodes(){
	try {
	    codes = connection.getList("Veiklos", "ID", "Pavadinimas", "Pavadinimas");
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    

    @Override
    protected void createPanelFields() {
	getLocations();
	getSystems();
	getCodes();
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
	tfDate = new JMyTextField(8, fontsize);
	tfDate.addMouseListener(this);
	tfDate.setToolTipText("Dvigubas spragtelėjimas šiandienos datai");
	pFields.add(tfDate, gbc);

	gbc.gridx = 2;
	gbc.weightx = 0;
	lSystem = new JLabelRechts("Sistema", fontsize);
	pFields.add(lSystem, gbc);

	gbc.gridx = 3;
	gbc.weightx = 0;
	cbIrenginys = new JMyComboBox(systems[1], fontsize);
	pFields.add(cbIrenginys, gbc);
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
	scrMessage = new JScrollPane(ta_Message);
	pFields.add(scrMessage, gbc);
        
	
    }

//    private void createPanel_Messages() {
//	pMessage = new JPanel(new GridLayout(2, 1));
//	lMessage = new JLabelRechts("Aprašymas");
//	ch_Message = new JCheckBox();
//	pMessage.add(lMessage);
//	pMessage.add(ch_Message);
//    }
//

    @Override
    protected void filter() {
        if (chDate.isSelected() || chSystem.isSelected() || chName.isSelected() || chLocation.isSelected() || chMark.isSelected() || chIT.isSelected() || chNr.isSelected()) {
            filter_by();
        } else {
            filter_all(SELECT_ALL);
        }
    }

    protected void filter_all(String query) {
	Object[] row;
	int i, colcount;
	tableModel.setRowCount(0);
	ResultSet resultset;
	try {
	    resultset = connection.executeQuery(query);
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

    protected void filter_by() {
	int i, colcount;
        Object[] the_row;
	StringBuilder sb;
	tableModel.setRowCount(0);
        ResultSet resultset;
	try {
	    sb = prepareFilter();
	    preparedFilter = connection.prepareStatement(sb.toString());
	    preparedFilter_setPrepared(sb);
	    resultset = preparedFilter.executeQuery();
	    colcount = tableModel.getColumnCount();
	    the_row = new Object[colcount];
	    while( resultset.next() ){
		for (i = 0; i <= colcount - 1; i++) {
		    the_row[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(the_row);
	    }
	    resultset.close();
	    preparedFilter.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }

//SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas, v.Pavadinimas, i.Pastaba, i.Pozymis, i.Data FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID LEFT JOIN Vietos v ON i.Vieta = v.Pavadinimas
    @Override
    protected StringBuilder prepareFilter() {
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
	    sb.append(" i.Pozymis LIKE CONVERT (? USING utf8mb4) COLLATE utf8mb4_bin");
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
        }
	sb.append(" ORDER BY i.IT");
	return sb;
    }
    
//SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas, v.Pavadinimas, i.Pastaba, i.Pozymis, i.Data FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID LEFT JOIN Vietos v ON i.Vieta = v.Pavadinimas
    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n;
	n = 0;
	i = sb.indexOf(" i.Data LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, tfDate.getText());
	}
	i = sb.indexOf(" i.Sistema = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));	    
	}
	i = sb.indexOf(" i.Pavadinimas LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, fName.getText());
	}
	i = sb.indexOf(" i.Vieta = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.parseInt(locations[0][cbLocations.getSelectedIndex()]));
	}
	i = sb.indexOf(" i.Pozymis LIKE");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, fMark.getText());
	}
	i = sb.indexOf(" i.IT LIKE ?");
	if (i >= 0) {
 	    n++;
	    preparedFilter.setString(n, fIT.getText());
	}
	i = sb.indexOf(" i.Nr LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, ta_Message.getText());
	}
	i = sb.indexOf(" i.Pastaba LIKE ?");
	if (i >= 0) {
 	    n++;
	    preparedFilter.setString(n, ta_Message.getText());
	}
	
    }

//    
    @Override
    protected void update() {
	int the_row;
	the_row = table.getSelectedRow();
	if (the_row >= 0) {
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
		}
		preparedUpdate.setString(1, fIT.getText());
		preparedUpdate.setString(2, fNr.getText());
		preparedUpdate.setString(3, fName.getText());
		preparedUpdate.setInt(4, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));
                preparedUpdate.setString(5, tfDate.getText());
		preparedUpdate.setInt(6, Integer.parseInt(locations[0][cbLocations.getSelectedIndex()]));
		preparedUpdate.setString(7, fMark.getText());
		preparedUpdate.setString(8, ta_Message.getText());
		preparedUpdate.setInt(9, Integer.parseInt(codes[0][cbCode.getSelectedIndex()]));
		preparedUpdate.setInt(10, (Integer) table.getValueAt(the_row, tableModel.findColumn(ID)));
		
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

// INSERT INTO Irenginiai (IT, Nr, Pavadinimas, Sistema, Data, Vieta, Pozymis, Pastaba, Veikla) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    protected void insert() {
	int the_row;
	the_row = table.getSelectedRow();
	if (the_row >= 0) {
	    try {
		if (preparedInsert == null) {
		    preparedInsert = connection.prepareStatement(PREPARE_INSERT);
		}
		preparedInsert.setString(1, fIT.getText());
		preparedInsert.setString(2, fNr.getText());
		preparedInsert.setString(3, fName.getText());
		preparedInsert.setInt(4, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));
                preparedInsert.setString(5, tfDate.getText());
		preparedInsert.setInt(6, Integer.parseInt(locations[0][cbLocations.getSelectedIndex()]));
		preparedInsert.setString(7, fMark.getText());
		preparedInsert.setString(8, ta_Message.getText());
		preparedInsert.setInt(9, Integer.parseInt(codes[0][cbCode.getSelectedIndex()]));
                if (preparedInsert.executeUpdate() == 1) {
		    filter();
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.getErrorCode(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	}  else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);     
	}
    }

    private void delete_temp_files(){
        Runtime r;
        Process pr;
	String[] del_cmd = {"rm", "-f", "*ps"};
	r = Runtime.getRuntime();
	try {
	    pr = r.exec(del_cmd);
            pr.waitFor();
//	    pr.waitFor(1000, TimeUnit.MILLISECONDS);
	    JOptionPane.showMessageDialog(this, pr.exitValue());
	    pr.destroy();
	} catch (IOException | InterruptedException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     *
     */
    public void create_barcodes_file() {
	String[] run_pdflatex = {"pdflatex", "-synctex=1", "-interaction=nonstopmode", BARCODES_FILE.concat(".tex")};
	String[] ps2eps_cmd = {"ps2eps", "-f", "zw.ps"};
	String[] barcode_cmd;
	String[] name, note;
	StringBuilder sb;
        Runtime r;
        Process pr;
	int res, col, rows, i, j;
	name = new String[TEX_COL_COUNT];
	note = new String[TEX_COL_COUNT];
	barcode_cmd = new String[5];
	barcode_cmd[0] = "barcode";
	barcode_cmd[1] = "-b";
	barcode_cmd[3] = "-o";
	res = -1;
	col = 0;
	rows = table.getRowCount();
	sb = new StringBuilder(TEX_START);
	if (JOptionPane.showConfirmDialog(this, "Sukurti brūkšninių kodų pdf?", "Sukurti pdf?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
	    r = Runtime.getRuntime();
	    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    try {
		for (i = 0; i < rows; i++) {
		    barcode_cmd[2] = (String) table.getValueAt(i, 1);
		    name[col] = (String) table.getValueAt(i, 3);
		    note[col] = (String) table.getValueAt(i, 7);
		    barcode_cmd[4] = barcode_cmd[2].concat(".ps");
		    pr = r.exec(barcode_cmd);
                    pr.waitFor();
//		    pr.waitFor(200, TimeUnit.MILLISECONDS);
		    res = pr.exitValue();
		    if (res == 0) {
			ps2eps_cmd[2] = barcode_cmd[4];
			pr = r.exec(ps2eps_cmd);
                        pr.waitFor();
//			pr.waitFor(200, TimeUnit.MILLISECONDS);

			res = pr.exitValue();
		    }
		    if (res == 0) {
			sb.append("Vilniaus oro uostas \\newline \\includegraphics[scale=0.9]{").append(barcode_cmd[2]).append(".eps}").append(" \\newline ").append(name[col]).append(" \\newline \\footnotesize {").append(note[col]).append("}");
			if (col < TEX_COL_COUNT - 1) {
			    sb.append(" & ");
			    col++;
			} else {
			    sb.append("\\\\\\hline\n");
			    col = 0;
			}
		    }
		}
		for (j = i; j <= i + 1; j++) {
		    sb.append(" & ");
		}
		sb.append("\\\\\\hline\n");
		sb.append(TEX_END);
		saveFile(BARCODES_FILE.concat(".tex"), sb.toString());
		pr = r.exec(run_pdflatex);
		pr.waitFor();
//		pr.waitFor(3, TimeUnit.SECONDS);
		res = pr.exitValue();
		pr.destroy();
	    } catch (IOException | InterruptedException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	    }
	    JOptionPane.showMessageDialog(this, "Failas " + System.getProperty("user.dir") + BARCODES_FILE + ".pdf: " + String.valueOf(res));
	    delete_temp_files();
	    setCursor(Cursor.getDefaultCursor());
	}
    }

     
    public void exportIT() {
        String filename, s;
        StringBuilder sb;
        filename = JOptionPane.showInputDialog(this, "CSV failo vardas", IT_CSV);
        sb = new StringBuilder("ID\tIT\tNr\tPavadinimas\tSistema\tVieta\tŽyma\tPastaba\tData\tVeikla\n");
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                s = String.valueOf(table.getValueAt(i, j));
                s = s.replaceAll("\n", "");
                sb.append(s).append("\t");
            }
            sb.append("\n");
        }
        saveFile(filename, sb.toString());
    }

   
    
    protected void delete() {
	int the_row;
	the_row = table.getSelectedRow();
	if (the_row >= 0) {
	    try {
		if (preparedDelete == null) {
		    preparedDelete = connection.prepareStatement(delete);
		}
// ID, Pavadinimas
		preparedDelete.setInt(1, (int) table.getValueAt(the_row, 0));
		if (preparedDelete.execute()) {
		    filter(select);
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	} else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    protected void filter(String query) {
	Object[] the_row;
	int i, colcount;
	tableModel.setRowCount(0);
	ResultSet resultset;
	try {
	    resultset = connection.executeQuery(query);
	    colcount = tableModel.getColumnCount();
	    the_row = new Object[colcount];
	    while (resultset.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    the_row[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(the_row);
	    }
	    resultset.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }
	

    
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
	    case "delete":
		delete();
		filter();
		break;
	}
	
    }



    @Override
    public void mouseClicked(MouseEvent me) {
	if (me.getComponent().equals(table)){
            the_row = table.getSelectedRow();
            if (the_row >= 0) {
                tfDate.setText(table.getValueAt(the_row, tableModel.findColumn(DATA)).toString());
                setComboBoxItem(cbIrenginys, systems[1], (String) table.getValueAt(the_row, tableModel.findColumn(SISTEMA)));
                fName.setText(table.getValueAt(the_row, tableModel.findColumn(PAVADINIMAS)).toString());
                setComboBoxItem(cbLocations, locations[1], (String) table.getValueAt(the_row, tableModel.findColumn(VIETA)));
                fMark.setText(table.getValueAt(the_row, tableModel.findColumn(POZYMIS)).toString());
                fIT.setText(table.getValueAt(the_row, tableModel.findColumn(IT)).toString());
                fNr.setText(table.getValueAt(the_row, tableModel.findColumn(NR)).toString());
                setComboBoxItem(cbCode, codes[1], (String) table.getValueAt(the_row, tableModel.findColumn(VEIKLA)));
                ta_Message.setText(table.getValueAt(the_row, tableModel.findColumn(PASTABA)).toString());
            }
        }
    }
     
}
