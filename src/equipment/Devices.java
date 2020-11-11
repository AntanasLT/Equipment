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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Devices extends Systems {

    private static final String SELECT_ALL = "SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID ORDER BY i.ID";
    private static final String PREPARE_INSERT = "INSERT INTO Irenginiai (IT, Nr, Pavadinimas, Sistema) VALUES (?, ?, ?, ?)";
    private static final String PREPARE_UPDATE = "UPDATE Irenginiai SET IT = ?, Nr = ?, Pavadinimas = ?, Sistema = ? WHERE ID = ?";
    private static final String PREPARE_DELETE = "DELETE FROM Irenginiai WHERE ID = ?";
    private static final String ID = "ID (auto)";
    private static final String IT = "IT";
    private static final String NR = "Nr";
    private static final String PAVADINIMAS = "Pavadinimas";
    private static final String SISTEMA = "Sistema";

    private DefaultTableModel tableModel;
    private PreparedStatement preparedUpdate, preparedInsert, preparedSelectAll, preparedDelete;
    protected Devices(ConnectionEquipment connection) {
	super(connection);
	init();
    }

    private void init() {
	if (connection != null) {
	    setLayout(new BorderLayout());
	    createTable();
	    createPanelButtons();
	    add(pButtons, BorderLayout.NORTH);
	    add(scrPaneTable, BorderLayout.CENTER);
	    setVisible(true);
	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "No connection!", "Error!", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void createTable() {
	tableModel = new DefaultTableModel(new Object[]{ID, IT, NR, PAVADINIMAS, SISTEMA}, 0);
	table = new JTable(tableModel);
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
	    switch (i) {
		case 0:
		    dieSpalte.setPreferredWidth(20);
		    break;
		case 3:
		    dieSpalte.setPreferredWidth(500);
		    break;
	    }
	}
    }

//    private void setzt_dieUeberschriften(){
//	table.getTableHeader().setPreferredSize(new Dimension(table.getWidth(), 60));
//        table.getColumnModel().getColumn(1).setHeaderValue("<html>Der<br>Typ</html>");
//    }
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
    
    protected void filter() {
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

    private void update() {
	int row, systems_id;
	row = table.getSelectedRow();
	if (row >= 0) {
	    systems_id = getSystemID((String) table.getValueAt(row, 4));	    
	    if (systems_id >= 0) {
		try {
		    if (preparedUpdate == null) {
			preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
		    }
    // IT, Nr, Pavadinimas, Sistema, ID
		    preparedUpdate.setString(1, (String) table.getValueAt(row, 1));
		    preparedUpdate.setString(2, (String) table.getValueAt(row, 2));
		    preparedUpdate.setString(3, (String) table.getValueAt(row, 3));
		    preparedUpdate.setInt(4, systems_id);
		    preparedUpdate.setInt(5, (int) table.getValueAt(row, 0));
		    if (preparedUpdate.executeUpdate() == 1) {
			filter();
		    }
		} catch (SQLException ex) {
		    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
		}
	    } else {
		JOptionPane.showMessageDialog(this, "Nėra tokios sistemos.", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }   
	} else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void insert() {
	int row, systems_id;
	row = table.getSelectedRow();
	if (row >= 0) {
	    systems_id = getSystemID((String) table.getValueAt(row, 4));	    
	    if (systems_id >= 0) {
		try {
		    if (preparedInsert == null) {
			preparedInsert = connection.prepareStatement(PREPARE_INSERT);
		    }
		    // IT, Nr, Pavadinimas, Sistema
		    preparedInsert.setString(1, table.getValueAt(row, 1).toString());
		    preparedInsert.setString(2, table.getValueAt(row, 2).toString());
		    preparedInsert.setString(3, table.getValueAt(row, 3).toString());
		    preparedInsert.setInt(4, systems_id);
		    if (preparedInsert.executeUpdate() == 1) {
			filter();
		    }
		} catch (SQLException ex) {
		    JOptionPane.showMessageDialog(this, ex.getErrorCode(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
		}
	    } else {
		JOptionPane.showMessageDialog(this, "Nėra tokios sistemos.", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    } 
	}  else {
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
// ID, IT, Nr, Pavadinimas, Sistema
		preparedDelete.setInt(1, (int) table.getValueAt(row, 0));
		if (preparedDelete.execute()) {
		    filter();
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	}  else {
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