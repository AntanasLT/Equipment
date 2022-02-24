/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author a
 */
public class IDString extends ID_auto {
    
    String colName1, colName2, dbField1, dbField2;

    /**
     *
     * @param the_connection connection to the DB
     * @param size fontsize
     * @param tbl DB table name
     * @param dbf1 DB table name fieldname 1
     * @param dbf2 DB table name fieldname 2
     * @param s1 name of first column of JTable
     * @param s2 name of second column of JTable
     */
    public IDString(ConnectionEquipment the_connection, int size, String tbl, String dbf1, String dbf2, String s1, String s2) {
        super(the_connection, size, tbl);
        colName1 = s1;
        colName2 = s2;
        dbField1 = dbf1;
        dbField2 = dbf2;
//        init();
    }

    @Override
    public void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
	if (connection != null) {
	    setLayout(new BorderLayout());
	    createTable();
	    createPanelButtons();
	    add(pButtons, BorderLayout.NORTH);
	    add(scrTable, BorderLayout.CENTER);
	    setVisible(true);
            setSelect();
            setInsert();
	    setUpdateDelete();
	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    protected void createTable() {
	tableModel = new DefaultTableModel(new Object[]{colName1, colName2}, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setAutoCreateRowSorter(true);
//	table.getSelectionModel().addListSelectionListener(this);
	setColumnsWidths();
	tableModel.setRowCount(1);
	scrTable = new JScrollPane(table);
    }

    @Override
    protected void setSelect() {
	select = "SELECT " + dbField1 + ", " + dbField2 + " FROM " + table_name + " WHERE " + dbField1 + " LIKE ? OR " + dbField2 + " LIKE ? " + " ORDER BY LENGTH(" + dbField1 + "), " + dbField1;        
    }
    
    @Override
    protected void setInsert() {
        insert = "INSERT INTO " + table_name + " (" + dbField1 + dbField2 + ") VALUES (?, ?)";
    }
    
    @Override
    protected void setUpdateDelete(){
        update = "UPDATE " + table_name + " SET " + dbField2 + " = ? WHERE " + dbField1 + " = ?";
        delete = "DELETE FROM " + table_name + "WHERE " + dbField1 + " = ?";
    }

    @Override
    protected void setPrepared() {
        String txt;
        txt = "%" + tfSearch.getText() + "%";
        try {        
            preparedSelect.setString(1, txt);
            preparedSelect.setString(2, txt);
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    @Override
    protected void insert() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedInsert == null) {
		    preparedInsert = connection.prepareStatement(insert);
		}
		// ID, Pavadinimas
		preparedInsert.setString(1, (String) table.getValueAt(row, 0));
		preparedInsert.setString(2, (String) table.getValueAt(row, 1));
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
    protected void update() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(update);
		}
// IT, Nr, Pavadinimas, Sistema, ID
		preparedUpdate.setString(1, (String) table.getValueAt(row, 1));
		preparedUpdate.setString(2, (String) table.getValueAt(row, 0));
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
    
    @Override
    protected void delete() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedDelete == null) {
		    preparedDelete = connection.prepareStatement(delete);
		}
// ID, Pavadinimas
		preparedDelete.setString(1, (String) table.getValueAt(row, 0));
		if (preparedDelete.execute()) {
		    filter();
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	} else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
}
