/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import datum.Datum;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Works extends JPanel implements ActionListener, ListSelectionListener {

    private static final String SELECT_ALL = "SELECT d.ID, d.IDpr, v.Vardas, d.Data, sist.Pavadinimas, i.Pavadinimas, dt.Pavadinimas, d.Pastabos FROM Darbai d LEFT join Sistemos sist ON d.Sistema = sist.ID LEFT join Irenginiai i ON d.Irenginys = i.Pavadinimas LEFT join Darbotipis dt ON d.Darbas = dt.ID LEFT JOIN Vartotojai v ON  d.IDpr = v.ID  ORDER BY Data";
    private static final String DELETE = "DELETE FROM Darbai WHERE ID = ";
    private static final String INSERT = "INSERT INTO Darbai (Data, Sistema, Irenginys, Darbas, Pastabos) VALUES ('";
    private static final String UPDATE_0DATA = "UPDATE Darbai SET Data = '";
    private static final String UPDATE_SISTEMA = "', Sistema = ";
    private static final String UPDATE_IRENGINYS = ", Irenginys = ";
    private static final String UPDATE_DARBAS = ", Darbas = ";
    private static final String UPDATE_PASTABOS = ", Pastabos = '";
    private static final String UPDATE_FINISH = "' WHERE ID = ";
    private static final String ID = "ID";
    private static final String ID_PR = "ID_PR";
    private static final String USER = "Vartotojas";
    private static final String DATA = "Data";
    private static final String SISTEMA = "Sistema";
    private static final String IRENGINYS = "Irenginys";
    private static final String DARBAS = "Darbas";
    private static final String APRASYMAS = "Aprašymas";

    private DefaultTableModel tableModel;
    ConnectionEquipment connection;
    JPanel pInput, pButtons, pFields;
    JScrollPane sPaneTable, sPaneMessage;
    JTable table;
    JMyButton bDelete, bAdd, bChange, bFilter;
    JRadioButton radioButton1;
    JLabelRechts lMessage, lDate, lEquipment, lSystem, lWorkType, lIDpr;
    JTextField tfDate, tfEquipment, tfIDpr;
    JTextArea taMessage;
    JMyComboBox cbSystem, cbWorkType;
    GridBagConstraints gbc;
    Datum date;
    String[][] systems, worktypes;



    public Works(ConnectionEquipment the_connection) {
	connection = the_connection;
	init();
    }

    private void init() {
	if (connection != null) {
        date = new Datum();
	setLayout(new BorderLayout());
	getSystems();
	    getWorktypes();
	createTable();
	createPanelInput();
	add(pInput, BorderLayout.NORTH);
	add(sPaneTable, BorderLayout.CENTER);
	add(lMessage, BorderLayout.SOUTH);
	setVisible(true);
	} else {
	    JOptionPane.showMessageDialog(this, "Nerisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void createPanelInput(){
        pInput = new JPanel(new BorderLayout());
        createPanelFields();
        pInput.add(pFields, BorderLayout.NORTH);
        createPanelButtons();
        pInput.add(pButtons, BorderLayout.SOUTH);
        lMessage = new JLabelRechts();
//        updateComboBoxes();
    }

    protected void createPanelButtons() {
	pButtons = new JPanel();
	bChange = new JMyButton("Išsaugoti");
	bChange.setActionCommand("update");
	bChange.addActionListener(this);
	bAdd = new JMyButton("Naujas");
	bAdd.setActionCommand("insert");
	bAdd.addActionListener(this);
	bDelete = new JMyButton("Pašalinti");
	bDelete.setActionCommand("delete");
	bDelete.addActionListener(this);
	bFilter = new JMyButton("Filtruoti");
	bFilter.setActionCommand("filter");
	bFilter.addActionListener(this);
	pButtons.add(bFilter);
	pButtons.add(bChange);
	pButtons.add(bAdd);
	pButtons.add(bDelete);
    }

    private void createPanelFields() {
        pFields = new JPanel(new GridBagLayout());
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
//		panelInput.setBackground(Color.lightGray);
	gbc.insets = new Insets(0, 0, 5, 5);

//First row
//	gbc.weightx = 0;
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0;
//	gbc.gridwidth = 3;
//	gbc.fill = GridBagConstraints.EAST;
        lDate = new JLabelRechts("Data:");
	pFields.add(lDate, gbc);

	gbc.gridx = 1;
	gbc.weightx = 0;
        tfDate = new JTextField(date.heutigesDatum(), 10);
	pFields.add(tfDate, gbc);

	gbc.gridx = 2;
	gbc.weightx = 0;
	lSystem = new JLabelRechts("Sistema");
	pFields.add(lSystem, gbc);

	gbc.gridx = 3;
	gbc.weightx = 0.5;
	cbSystem = new JMyComboBox(systems[1]);
	pFields.add(cbSystem, gbc);
//
	gbc.gridx = 4;
	gbc.weightx = 0;
        lEquipment = new JLabelRechts("Įrenginys");
	pFields.add(lEquipment, gbc);

	gbc.gridx = 5;
	gbc.weightx = 0.5;
        tfEquipment = new JTextField(20);
	pFields.add(tfEquipment, gbc);
//
	gbc.gridx = 6;
	gbc.weightx = 0;
        lWorkType = new JLabelRechts("Darbas");
	pFields.add(lWorkType, gbc);

	gbc.gridx = 7;
	gbc.weightx = 0.5;
	cbWorkType = new JMyComboBox(worktypes[1]);
	pFields.add(cbWorkType, gbc);
//
	gbc.gridx = 8;
	gbc.weightx = 0;
	lIDpr = new JLabelRechts("ID prad.");
	pFields.add(lIDpr, gbc);

	gbc.gridx = 9;
	gbc.weightx = 0;
	tfIDpr = new JTextField(6);
	pFields.add(tfIDpr, gbc);//
//// Second row
	gbc.gridy = 1;
	gbc.gridx = 0;
	gbc.weightx = 0;
        lMessage = new JLabelRechts("Aprašymas");
	pFields.add(lMessage, gbc);

	gbc.gridx = 1;
//	gbc.weightx = 0.5;
        gbc.gridwidth = 9;
        taMessage = new JTextArea(5, 50);
        sPaneMessage = new JScrollPane(taMessage);
	pFields.add(sPaneMessage, gbc);
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
    protected void setConnection(ConnectionEquipment the_connection) {
	connection = the_connection;
    }

    protected void disconnect() {
	connection = null;
    }

    private void getSystems() {
	try {
	    systems = connection.getSystems();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    private void getWorktypes() {
	try {
	    worktypes = connection.getWorkTypes();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void createTable() {
	tableModel = new DefaultTableModel(new Object[]{ID, ID_PR, USER, DATA, SISTEMA, IRENGINYS, DARBAS, APRASYMAS}, 0);
	table = new JTable(tableModel);
	table.setAutoCreateRowSorter(true);
	table.getSelectionModel().addListSelectionListener(this);
	setztSpaltenbreiten();
//	setzt_dieUeberschriften();
	sPaneTable = new JScrollPane(table);
    }

        private void setztSpaltenbreiten() {
	TableColumn column;
	column = null;
	    for (int i = 0; i < table.getColumnCount(); i++) {
		column = table.getColumnModel().getColumn(i);
		if (tableModel.getColumnName(i).equals(ID) || tableModel.getColumnName(i).equals(ID_PR)) {
                    column.setMaxWidth(50);
                    column.setPreferredWidth(20);
                } else if (tableModel.getColumnName(i).equals(APRASYMAS)) {
                    column.setPreferredWidth(500);
                } else
                    column.setMaxWidth(200);
                    column.setPreferredWidth(100);
   	    }
    }

//    private void setzt_dieUeberschriften(){
//	table.getTableHeader().setPreferredSize(new Dimension(table.getWidth(), 60));
//        table.getColumnModel().getColumn(1).setHeaderValue("<html>Der<br>Typ</html>");
//    }


    protected void filter(String query) {
        Object[] row;
	int i, colcount;
	tableModel.setRowCount(0);
        ResultSet resultset;
	try {
            resultset = connection.executeQuery(query);
	    colcount = tableModel.getColumnCount();
//            System.out.println();
	    row = new Object[colcount];
	    while( resultset.next() ){
		for (i = 0; i <= colcount - 1; i++) {
//                    System.out.print(i);
		    row[i] = resultset.getObject(i + 1);
//                    System.out.println(row[i]);
		}
		tableModel.addRow(row);
	    }
	    resultset.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}

    }


    private void update() {
	int row;
	StringBuilder statement;
	row = table.getSelectedRow();
	if (row >= 0) {
	    statement = new StringBuilder(UPDATE_0DATA);
	    statement.append(table.getValueAt(row, 1)).
                    append(UPDATE_SISTEMA).append(table.getValueAt(row, 2)).
                    append(UPDATE_IRENGINYS).append(table.getValueAt(row, 3)).
                    append(UPDATE_DARBAS).append(table.getValueAt(row, 4)).
                    append(UPDATE_PASTABOS).append(table.getValueAt(row, 5)).
                    append(UPDATE_FINISH).append(table.getValueAt(row, 0));
	    try {
		if (connection.executeUpdate(statement.toString()) == 1) {
		    filter(SELECT_ALL);
		};
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Λάθος!!", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }



    private void insert() {
	int row;
	StringBuilder statement;
	row = table.getSelectedRow();
	if (row >= 0) {
	    statement = new StringBuilder(INSERT);
	    statement.append(table.getValueAt(row, 1)).append("', ").
                    append(table.getValueAt(row, 2)).append(", ").
                    append(table.getValueAt(row, 3)).append(", ").
                    append(table.getValueAt(row, 4)).append(", '").
                    append(table.getValueAt(row, 5)).append("')");
	    try {
		if (connection.executeUpdate(statement.toString()) == 1) {
		    filter(SELECT_ALL);
		};
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Λάθος!!", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    protected void delete() {
	int [] rows;
        int l, i;
	StringBuilder statement;
	rows = table.getSelectedRows();
        l = rows.length;
	if (l >= 0) {
	    statement = new StringBuilder(DELETE);
            for (i = 1; i <= l; i++) {
                statement.append(table.getValueAt(rows[i-1], 0));
                if (i < l) {
                    statement.append(" OR ID = ");
                }
            }
	    try {
		if (connection.executeUpdate(statement.toString()) == 1) {
		    filter(SELECT_ALL);
                    System.out.println();
		};
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    private String getString(int row, int col) {
	Object o;
	o = tableModel.getValueAt(row, col);
	if (o != null) {
	    return o.toString();
	} else {
	    return "";
	}
    }
    
    private void setItem(JComboBox cb, String[] s, String name) {
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
    
    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "update":
		update();
		break;
	    case "filter":
		filter(SELECT_ALL);
		break;
	    case "insert":
		insert();
		break;
	    case "delete":
		delete();
		filter(SELECT_ALL);
		break;
	}

    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
	int the_row;
	the_row = table.getSelectedRow();
	if (the_row >= 0) {
	    tfIDpr.setText(getString(the_row, 1));
	    tfDate.setText(getString(the_row, 3));
	    setItem(cbSystem, systems[1], getString(the_row, 4));
	    tfEquipment.setText(getString(the_row, 5));
	    setItem(cbWorkType, worktypes[1], getString(the_row, 6));
	    taMessage.setText(getString(the_row, 7));
//	    if (String.valueOf(tableModel.getValueAt(the_row, 0)).length() > 15) {
//		field_date.setText(String.valueOf(tableModel.getValueAt(the_row, 0)).substring(0, 10));
//		dasFeldZeit.setText(String.valueOf(tableModel.getValueAt(the_row, 0)).substring(11, 16));
//		dieAuswahlGericht.setSelectedItem(String.valueOf(tableModel.getValueAt(the_row, 1)));
//		dasFeldMenge.setText(String.valueOf(tableModel.getValueAt(the_row, 2)));
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





}
//	tableModel = new DefaultTableModel(new Object[]{"", "Datum", "<html>Fett<br>%</hmtl>", "<html>Muskeln<br>%</html>", "<html>Wasser<br>%</html>", "<html>Knochen<br>kg</html>", "<html>Masse<br>kg</html>", "<html>Energie0<br>kcal</html>", "<html>Energie1<br>kcal</html>", "<html>Bauch<br>cm</html>", "<html>Oberarm<br>cm</html>", "<html>Unterarm<br>cm</html>", "<html>Ober-<br>schenkel<br>cm</html>", "<html>Unter-<br>schenkel<br>cm</html>", "<html>Brust<br>cm</html>", "<html>Fettmasse<br>kg</html>", "<html>Muskel-<br>masse<br>kg</html>"}, 0);