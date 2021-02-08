/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class Sistemos extends Darbai {

    private static final String SELECT_ALL = "SELECT ID, Pavadinimas FROM Sistemos ORDER BY Pavadinimas";
    private static final String PREPARE_DELETE = "DELETE FROM Sistemos WHERE ID = ?";
    private static final String PREPARE_INSERT = "INSERT INTO Sistemos (Pavadinimas) VALUES (?)";
    private static final String PREPARE_UPDATE = "UPDATE Sistemos SET Pavadinimas = ? WHERE ID = ?";

//    protected DefaultTableModel tableModel;
//    private PreparedStatement preparedUpdate, preparedInsert, preparedDelete;
    protected JPanel pButtons;
    protected JMyButton btInsert, btEdit;

    protected static final String PAVADINIMAS = "Pavadinimas";
    protected static final String ID = "ID";
    
    protected String select;

    public Sistemos(ConnectionEquipment connection, int size) {
	super(connection, size);
//	init();
    }

    @Override
    protected void init() {
	if (connection != null) {
	    setLayout(new BorderLayout());
	    createTable();
	    createPanelButtons();
	    add(pButtons, BorderLayout.NORTH);
	    add(scrPaneTable, BorderLayout.CENTER);
	    setVisible(true);
	    setSelectQuery();
	    filter(select);
	} else {
	    JOptionPane.showMessageDialog(this, "No connection!", "Error!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    protected void setSelectQuery(){
	select = SELECT_ALL;
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
    }
    
    protected void createTable() {
	tableModel = new DefaultTableModel(new Object[]{ID, PAVADINIMAS}, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setAutoCreateRowSorter(true);
	table.getSelectionModel().addListSelectionListener(this);
	setColumnsWidths();
//	setzt_dieUeberschriften();
	scrPaneTable = new JScrollPane(table);
    }

    private void setColumnsWidths() {
    TableColumn dieSpalte;
    dieSpalte = null;
        for (int i = 0; i < table.getColumnCount(); i++) {
            dieSpalte = table.getColumnModel().getColumn(i);
	    switch (tableModel.getColumnName(i)) {
		case ID:
                    dieSpalte.setPreferredWidth(20);
                    break;
		case PAVADINIMAS:
                    dieSpalte.setPreferredWidth(800);
                    break;
            }
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
	
    
    private void update() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
		}
// IT, Nr, Pavadinimas, Sistema, ID
		preparedUpdate.setString(1, (String) table.getValueAt(row, 1));
		preparedUpdate.setInt(2, (int) table.getValueAt(row, 0));
		if (preparedUpdate.executeUpdate() == 1) {
		    filter(select);
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
		    filter(select);
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	} else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void delete() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedDelete == null) {
		    preparedDelete = connection.prepareStatement(PREPARE_DELETE);
		}
// ID, Pavadinimas
		preparedDelete.setInt(1, (int) table.getValueAt(row, 0));
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
    
    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "update":
		update();
		break;	
	    case "filter":
		filter(select);
		break;
	    case "insert":
		insert();
		break;	
//	    case "delete":
//		delete();
//		filter(SELECT);
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