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
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class TP extends Darbai {

    private static final String SELECT_ALL = "SELECT tp.ID, tp.Data, s.Pavadinimas, tpr.Pavadinimas, tp.Pastaba FROM TP tp LEFT JOIN Sistemos s ON tp.Sistema = s.ID LEFT JOIN TPrusys tpr ON tp.TP = tpr.ID ORDER BY tp.Data DESC LIMIT 100";
    private static final String INSERT = "INSERT INTO TP (Data, Sistema, TP, Pastaba) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE TP SET Data = ?, Sistema = ?, TP = ?, Pastaba = ? WHERE ID = ?";
    private static final String SELECT = "SELECT tp.ID, tp.Data, s.Pavadinimas, tpr.Pavadinimas, tp.Pastaba FROM TP tp LEFT JOIN Sistemos s ON tp.Sistema = s.ID LEFT JOIN TPrusys tpr ON tp.TP = tpr.ID";
//    private static final String PREPARE_DELETE = "DELETE FROM Irenginiai WHERE ID = ?";
    private static final String ID = "ID";
    private static final String DATE = "Data";
    private static final String SYSTEM = "Sistema";
    private static final String TP = "Rūšis";
    private static final String NOTE = "Pastaba";

    private DefaultTableModel tableModel;
    private PreparedStatement preparedUpdate, preparedInsert;

    private JMyComboBox cbTPtype;
    private JCheckBox chTPtype;
    
    private PreparedStatement preparedFilter;

    String[][] tptypes;

    protected TP(ConnectionEquipment connection, int size) {
	super(connection, size);
//	init_components();
    }


    @Override
    protected void init_components() {
	if (connection != null) {
	    setLayout(new BorderLayout());
	    getTPtypes();
	    getSystems();
	    createTable();
	    createPanelInput();
	    add(pInput, BorderLayout.NORTH);
	    add(scrPaneTable, BorderLayout.CENTER);
	    add(lMessage, BorderLayout.SOUTH);
	    add(scrPaneTable, BorderLayout.CENTER);
	    setVisible(true);
//	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "No connection!", "Error!", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void createTable() {
	tableModel = new DefaultTableModel(new Object[]{ID, DATE, SYSTEM, TP, NOTE}, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setAutoCreateRowSorter(false);
	table.setDefaultEditor(Object.class, null);
        table.addMouseListener(this);
	table.getSelectionModel().addListSelectionListener(this);
	setColumnsWidths();
	tableModel.setRowCount(1);
//	setzt_dieUeberschriften();
	scrPaneTable = new JScrollPane(table);
    }

    private void setColumnsWidths() {
	TableColumn column;
	column = null;
	for (int i = 0; i < table.getColumnCount(); i++) {
	    column = table.getColumnModel().getColumn(i);
	    if (tableModel.getColumnName(i).equals(ID)) {
		column.setMaxWidth(60);
		column.setPreferredWidth(50);
	    } else if (tableModel.getColumnName(i).equals(NOTE)) {
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
	lMessage = new JLabelRechts(fontsize);
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
	cbSystem = new JMyComboBox(systems[1], fontsize);
	pFields.add(cbSystem, gbc);
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
	taMessage = new JMyTextArea(15, 30, fontsize);
	taMessage.addMouseListener(this);
	taMessage.setLineWrap(true);
	taMessage.setWrapStyleWord(true);
	taMessage.setToolTipText("Dvigubas spragtelėjimas ištrina tekstą iš šio lauko");
	scrPaneMessage = new JScrollPane(taMessage);
	pFields.add(scrPaneMessage, gbc);

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

    protected void createPanelMessages() {
	pMessage = new JPanel(new GridLayout(2, 1));
	lMessage = new JLabelRechts("Aprašymas", fontsize);
	chMessage = new JCheckBox();
	pMessage.add(lMessage);
	pMessage.add(chMessage);
    }

    private int getSystemID(String system) {
	int i, n, id;
	boolean found;
	i = 0;
	id = -1;
	found = false;
	n = systems.length;
	while (i <= n & !found) {
	    if (systems[1][i].equals(system)) {
		found = true;
		id = Integer.valueOf(systems[0][i]);
	    } 
	    else {
		i++;
	    }
	}
	return id;
    }

    private void getTPtypes() {
	try {
	    tptypes = connection.getTPtypes();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    
    private int getTPID(String tp) {
	int i, n, id;
	boolean found;
	i = 0;
	id = -1;
	found = false;
	n = tptypes.length;
	while (i <= n & !found) {
	    if (tptypes[1][i].equals(tp)) {
		found = true;
		id = Integer.valueOf(tptypes[0][i]);
	    } 
	    else {
		i++;
	    }
	}
	return id;
	
    }
    
    protected void filter() {
	Object[] row;
	int i, colcount;
	tableModel.setRowCount(0);
	StringBuilder sb;
	ResultSet resultset;
	try {
	    sb = prepareFilter();
	    preparedFilter = connection.prepareStatement(sb.toString());
	    preparedFilter_setPrepared(sb);
	    resultset = preparedFilter.executeQuery();
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

// SELECT tp.ID, tp.Data, s.Pavadinimas, tpr.Pavadinimas, tp.Pastaba FROM TP tp LEFT JOIN Sistemos s ON tp.Sistema = s.ID LEFT JOIN TPrusys tpr ON tp.TP = tpr.ID
    @Override
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(SELECT);
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
	int i, n, idpr, id;
	n = 0;
	i = sb.indexOf(" tp.Data LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, tfDate.getText());
	}
	i = sb.indexOf(" tp.Sistema = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));	    
	}
	i = sb.indexOf(" tp.TP = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(tptypes[0][cbTPtype.getSelectedIndex()]));
	}
	i = sb.indexOf(" tp.Pastaba LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, taMessage.getText());
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

//UPDATE TP SET Data = ?, Sistema = ?, TP = ?, Pastaba = ? WHERE ID = ?
    private void update() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
            try {
                if (preparedUpdate == null) {
                    preparedUpdate = connection.prepareStatement(UPDATE);
                }
                preparedUpdate.setString(1, tfDate.getText());
                preparedUpdate.setInt(2, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));
                preparedUpdate.setInt(3, Integer.valueOf(tptypes[0][cbTPtype.getSelectedIndex()]));
                preparedUpdate.setString(4, taMessage.getText());
                preparedUpdate.setInt(5, (int) table.getValueAt(row, 0));
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

// INSERT INTO TP (Data, Sistema, TP, Pastaba) VALUES (?, ?, ?, ?)
    private void insert() {
        try {
            if (preparedInsert == null) {
                preparedInsert = connection.prepareStatement(INSERT);
            }
            preparedInsert.setString(1, tfDate.getText());
//             System.out.println(systems[0][1]);
//           System.out.println(cbSystem.getSelectedIndex());
            preparedInsert.setInt(2, Integer.valueOf(systems[0][cbSystem.getSelectedIndex()]));
            preparedInsert.setInt(3, Integer.valueOf(tptypes[0][cbTPtype.getSelectedIndex()]));
            preparedInsert.setString(4, taMessage.getText());
             if (preparedInsert.executeUpdate() == 1) {
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

    @Override
    public void valueChanged(ListSelectionEvent lse) {
	selectedRow = table.getSelectedRow();
 	if (selectedRow >= 0) {
            tfDate.setText(table.getValueAt(selectedRow, 1).toString());
	    setComboBoxItem(cbSystem, systems[1], (String) table.getValueAt(selectedRow, 2));
	    setComboBoxItem(cbTPtype, tptypes[1], (String) table.getValueAt(selectedRow, 3));
	    taMessage.setText((String) table.getValueAt(selectedRow, 4));
            tfDate.setToolTipText(date.getWeekday(tfDate.getText())); //tfDate.repaint();
        }
    }



    
    
}
//	int zeile, spalte;
//	zeile = table.getSelectedRow();
//	spalte = table.getSelectedColumn();
//	if (lse.getValueIsAdjusting()) {
//	    table.setValueAt((String.valueOf(table.getValueAt(zeile, spalte)).replace(",", ".")), zeile, spalte);
//	}


//	tableModel = new DefaultTableModel(new Object[]{"", "Datum", "<html>Fett<br>%</hmtl>", "<html>Muskeln<br>%</html>", "<html>Wasser<br>%</html>", "<html>Knochen<br>kg</html>", "<html>Masse<br>kg</html>", "<html>Energie0<br>kcal</html>", "<html>Energie1<br>kcal</html>", "<html>Bauch<br>cm</html>", "<html>Oberarm<br>cm</html>", "<html>Unterarm<br>cm</html>", "<html>Ober-<br>schenkel<br>cm</html>", "<html>Unter-<br>schenkel<br>cm</html>", "<html>Brust<br>cm</html>", "<html>Fettmasse<br>kg</html>", "<html>Muskel-<br>masse<br>kg</html>"}, 0);