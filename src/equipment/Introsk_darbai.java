/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.Font;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Introsk_darbai extends Darbai {

    private static final String SELECT_ALL = "SELECT d.ID, d.Data, d.Nr, i.Pavadinimas, i.IT, v.Pavadinimas, d.Pastaba FROM Introsk_darbai d LEFT JOIN Introskopai i ON d.Nr = i.Nr LEFT JOIN Introsk_vietos v ON d.Vieta = v.ID GROUP BY d.Nr";
    private static final String INSERT = "INSERT INTO TP (Data, Sistema, TP, Pastaba) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE TP SET Data = ?, Sistema = ?, TP = ?, Pastaba = ? WHERE ID = ?";
    private static final String SELECT0 = "SELECT d.ID, d.Data, d.Nr, i.Pavadinimas, i.IT, v.Pavadinimas, d.Pastaba FROM Introsk_darbai d LEFT JOIN Introskopai i ON d.Nr = i.Nr LEFT JOIN Introsk_vietos v ON d.Vieta = v.ID";
//    private static final String SELECT1 = " WHERE d.Nr  LIKE ?";
//    private static final String SELECT2 = " GROUP BY d.Nr";
    
//    private static final String ID = "ID";
    private static final String DATA = "Data";
    private static final String NR = "Introsk. Nr.";
    private static final String PAVAD = "Introsk. pavad.";
    private static final String IT = "IT";
//    private static final String VIETA = "Vieta";
//    private static final String PASTABA = "Pastaba";

    private DefaultTableModel tableModelTP;
    private PreparedStatement preparedUpdateTP, preparedInsertTP;

    private JMyComboBox cbTPtype;
    private JCheckBox chTPtype;
    
    private PreparedStatement preparedFilterTP;

    String[][] tptypes;

    protected Introsk_darbai(ConnectionEquipment connection, String server, int size) {
	super(connection, server, size);
        init_this();
    }


    private void init_this() {
        font = new Font("Arial", Font.PLAIN, fontsize);
        table_columns = new String[]{ID, DATA, NR, PAVAD, IT};
	if (connection != null) {
	    setLayout(new BorderLayout());
	    getTPtypes();
	    getSystems();
	    createTable();
	    createPanelInput();
	    add(pInput, BorderLayout.NORTH);
	    add(scrTable, BorderLayout.CENTER);
	    add(lMessage, BorderLayout.SOUTH);
	    add(scrTable, BorderLayout.CENTER);
	    setVisible(true);
//	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "No connection!", "Error!", JOptionPane.ERROR_MESSAGE);
	}
    }

//    private void createTable() {
//	tableModelTP = new DefaultTableModel(new Object[]{ID, DATA, NR, PAVAD, IT}, 1);
//	table = new JTable(tableModelTP);
//        table.setFont(font);
//        table.getTableHeader().setFont(font);
//	table.setAutoCreateRowSorter(false);
//	table.setDefaultEditor(Object.class, null);
//        table.addMouseListener(this);
//	table.getSelectionModel().addListSelectionListener(this);
//	setColumnsWidths();
//	tableModelTP.setRowCount(1);
////	setzt_dieUeberschriften();
//	scrTable = new JScrollPane(table);
//    }

    @Override
    protected void setColumnsWidths() {
	TableColumn column;
//	column = null;
	for (int i = 0; i < table.getColumnCount(); i++) {
	    column = table.getColumnModel().getColumn(i);
	    if (tableModelTP.getColumnName(i).equals(ID)) {
		column.setMaxWidth(60);
		column.setPreferredWidth(50);
	    } else if (tableModelTP.getColumnName(i).equals(IT)) {
		column.setPreferredWidth(200);
	    } else {
                    column.setMaxWidth(200);
                    column.setPreferredWidth(100);
		}
//	    switch (i) {
//		case 0:
//		    dieSpalte.setPreferredWidth(20);
//		    break;
//		case 3:
//		    dieSpalte.setPreferredWidth(500);
//		    break;
//	    }
	}
    }

//    private void setzt_dieUeberschriften(){
//	table.getTableHeader().setPreferredSize(new Dimension(table.getWidth(), 60));
//        table.getColumnModel().getColumn(1).setHeaderValue("<html>Der<br>Typ</html>");
//    }
    @Override
    protected void createPanelInput() {
	pInput = new JPanel(new BorderLayout());
	createPanelFields();
	pInput.add(pFields, BorderLayout.NORTH);
	createPanelFilterButtons();
	pInput.add(pnFIlterButtons, BorderLayout.SOUTH);
	lMessage = new JLabelLeft(fontsize);
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
//	gbc.weightx = 0;
	lDate = new JLabelRechts("Data:", fontsize);
	pFields.add(lDate, gbc);

	gbc.gridx = 1;
//	gbc.weightx = 0;
	tfDate = new JMyTextField(date.getToday(), 10, fontsize);
	tfDate.addMouseListener(this);
        tfDate.setToolTipText(date.getWeekday());
	pFields.add(tfDate, gbc);

	gbc.gridx = 2;
//	gbc.weightx = 0;
	lSystem = new JLabelRechts("Sistema", fontsize);
	pFields.add(lSystem, gbc);

	gbc.gridx = 3;
//	gbc.weightx = 0.5;
	cbIrenginys = new JMyComboBox(systems[1], fontsize);
	pFields.add(cbIrenginys, gbc);
//
	gbc.gridx = 4;
//	gbc.weightx = 0;
	lWork = new JLabelRechts("Darbas", fontsize);
	pFields.add(lWork, gbc);

	gbc.gridx = 5;
//	gbc.weightx = 0.5;
	cbTPtype = new JMyComboBox(tptypes[1], fontsize);
	pFields.add(cbTPtype, gbc);
//
// Η δεύτερη σειρά
	gbc.gridy = 1;

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
	chTPtype = new JCheckBox();
	pFields.add(chTPtype, gbc);

// Η τρίτη σειρά
	gbc.gridy = 2;
	gbc.gridx = 0;
	gbc.weightx = 0;
	createPanelMessages();
	pFields.add(pMessage, gbc);

	gbc.gridx = 1;
	gbc.weightx = 0.5;
	gbc.gridwidth = 4;
	taMessage = new JMyTextArea_monospaced(15, 30, fontsize);
	taMessage.addMouseListener(this);
	taMessage.setLineWrap(true);
	taMessage.setWrapStyleWord(true);
	taMessage.setToolTipText("Dvigubas spragtelėjimas ištrina tekstą iš šio lauko");
	scrMessage = new JScrollPane(taMessage);
	pFields.add(scrMessage, gbc);

	gbc.gridx = 5;
	gbc.gridwidth = 1;
	gbc.weightx = 0;
	createPanelEditButtons();
	pFields.add(pEditButtons, gbc);

    }

    @Override
    protected void createPanelEditButtons() {
	pEditButtons = new JPanel(new GridLayout(4, 1));
	btChange = new JMyButton("Išsaugoti", fontsize);
	btChange.setActionCommand("update");
	btChange.addActionListener(this);
	bAdd = new JMyButton("Naujas", fontsize);
	bAdd.setActionCommand("insert");
	bAdd.addActionListener(this);
	pEditButtons.add(btChange);
	pEditButtons.add(bAdd);
// 	if (user.equals("Antanas") || user.equals("ak")) {
//	    bDelete = new JMyButton("Pašalinti");
//	    bDelete.setActionCommand("delete");
//	    bDelete.addActionListener(this);
//	    pEditButtons.add(bDelete);
//	}
    }

    @Override
    protected void createPanelFilterButtons() {
	pnFIlterButtons = new JPanel();
	btAll = new JMyButton("Naujausieji", fontsize);
	btAll.setActionCommand("all");
	btAll.addActionListener(this);
	pnFIlterButtons.add(btAll);
	btFilter = new JMyButton("Filtruoti", fontsize);
	btFilter.setActionCommand("filter");
	btFilter.addActionListener(this);
	pnFIlterButtons.add(btFilter);
    }

//    private int getSystemID(String system) {
//	int i, n, id;
//	boolean found;
//	i = 0;
//	id = -1;
//	found = false;
//	n = systems.length;
//	while (i <= n & !found) {
//	    if (systems[1][i].equals(system)) {
//		found = true;
//		id = Integer.valueOf(systems[0][i]);
//	    } 
//	    else {
//		i++;
//	    }
//	}
//	return id;
//    }

    private void getTPtypes() {
	try {
	    tptypes = connection.getTPtypes();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    
//    private int getTPID(String tp) {
//	int i, n, id;
//	boolean found;
//	i = 0;
//	id = -1;
//	found = false;
//	n = tptypes.length;
//	while (i <= n & !found) {
//	    if (tptypes[1][i].equals(tp)) {
//		found = true;
//		id = Integer.valueOf(tptypes[0][i]);
//	    } 
//	    else {
//		i++;
//	    }
//	}
//	return id;
//	
//    }
//    
    @Override
    protected void filter() {
	Object[] the_row;
	int i, colcount;
	tableModelTP.setRowCount(0);
	StringBuilder sb;
	ResultSet resultset;
	try {
	    sb = prepareFilter();
	    preparedFilterTP = connection.prepareStatement(sb.toString());
	    preparedFilter_setPrepared(sb);
	    resultset = preparedFilterTP.executeQuery();
	    colcount = tableModelTP.getColumnCount();
	    the_row = new Object[colcount];
	    while (resultset.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    the_row[i] = resultset.getObject(i + 1);
		}
		tableModelTP.addRow(the_row);
	    }
	    resultset.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

// SELECT tp.ID, tp.Data, s.Pavadinimas, tpr.Pavadinimas, tp.Pastaba FROM TP tp LEFT JOIN Sistemos s ON tp.Sistema = s.ID LEFT JOIN TPrusys tpr ON tp.TP = tpr.ID
    @Override
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(SELECT0);
	if (chDate.isSelected() || chSystem.isSelected() || chTPtype.isSelected() || chMessage.isSelected()) {
	    sb.append(" WHERE");
            if (chDate.isSelected()) {
		sb.append(" tp.Data LIKE ?");
            }
            if (chSystem.isSelected()) {
                appendAND(sb);
		sb.append(" tp.Sistema = ?");
            }
	    if (chTPtype.isSelected()) {
                appendAND(sb);
		sb.append(" tp.TP = ?");
            }
	    if (chMessage.isSelected()) {
                appendAND(sb);
		sb.append(" tp.Pastaba LIKE ?");
            }
	}
	sb.append(" ORDER BY tp.Data DESC");
//	System.out.println(sb.toString());
	return sb;
    }
    
    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n;
	n = 0;
	i = sb.indexOf(" tp.Data LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilterTP.setString(n, tfDate.getText());
	}
	i = sb.indexOf(" tp.Sistema = ?");
	if (i >= 0) {
	    n++;
	    preparedFilterTP.setInt(n, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));	    
	}
	i = sb.indexOf(" tp.TP = ?");
	if (i >= 0) {
	    n++;
	    preparedFilterTP.setInt(n, Integer.parseInt(tptypes[0][cbTPtype.getSelectedIndex()]));
	}
	i = sb.indexOf(" tp.Pastaba LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilterTP.setString(n, taMessage.getText());
	}

    }

     
    protected void filter_all() {
        Object[] the_row;
	int i, colcount;
	tableModelTP.setRowCount(0);
	ResultSet resultset;
	try {
	    resultset = connection.executeQuery(SELECT_ALL);
	    colcount = tableModelTP.getColumnCount();
	    the_row = new Object[colcount];
	    while (resultset.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    the_row[i] = resultset.getObject(i + 1);
		}
		tableModelTP.addRow(the_row);
	    }
	    resultset.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}

    }

//UPDATE TP SET Data = ?, Sistema = ?, TP = ?, Pastaba = ? WHERE ID = ?
    @Override
    protected void update() {
	int the_row;
	the_row = table.getSelectedRow();
	if (the_row >= 0) {
            try {
                if (preparedUpdateTP == null) {
                    preparedUpdateTP = connection.prepareStatement(UPDATE);
                }
                preparedUpdateTP.setString(1, tfDate.getText());
                preparedUpdateTP.setInt(2, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));
                preparedUpdateTP.setInt(3, Integer.parseInt(tptypes[0][cbTPtype.getSelectedIndex()]));
                preparedUpdateTP.setString(4, taMessage.getText());
                preparedUpdateTP.setInt(5, (int) table.getValueAt(the_row, 0));
                if (preparedUpdateTP.executeUpdate() == 1) {
                    filter();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
            }
	} else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

// INSERT INTO TP (Data, Sistema, TP, Pastaba) VALUES (?, ?, ?, ?)
    private void insert() {
        try {
            if (preparedInsertTP == null) {
                preparedInsertTP = connection.prepareStatement(INSERT);
            }
            preparedInsertTP.setString(1, tfDate.getText());
//             System.out.println(systems[0][1]);
//           System.out.println(cbIrenginys.getSelectedIndex());
            preparedInsertTP.setInt(2, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));
            preparedInsertTP.setInt(3, Integer.parseInt(tptypes[0][cbTPtype.getSelectedIndex()]));
            preparedInsertTP.setString(4, taMessage.getText());
             if (preparedInsertTP.executeUpdate() == 1) {
                filter();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getErrorCode(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
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
	    case "all":
		filter_all();
		break;
	    case "update":
		update();
		break;	
	    case "filter":
		filter();
		break;
	    case "insert":
		insert();
		break;	
	}
	
    }

}