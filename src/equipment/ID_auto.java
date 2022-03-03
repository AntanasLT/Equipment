/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Antanas Kvietkauskas
 */
public class ID_auto extends JPanel implements ActionListener {
    
    Font font;
    ConnectionEquipment connection;
    protected PreparedStatement preparedUpdate, preparedInsert, preparedSelect, preparedDelete;
    
    
    int fontsize;
    String select, delete, update, insert, table_name, id_name;

    DefaultTableModel tableModel;
    JMyButton btInsert, btEdit, btDelete, btFilter;
    JMyCheckBox chSearch;
    JMyTextField tfSearch;
    JPanel pButtons;
    JScrollPane scrTable;
    JTable table;
            
    /**
     *
     * @param the_connection connection to the DB
     * @param size fontsize
     * @param tbl database table
     */
    protected ID_auto(ConnectionEquipment the_connection, int size, String tbl) {
        fontsize = size;
	connection = the_connection;
        table_name = tbl;
        id_name = this.getClass().getSimpleName();
//	init();
    }

     public void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
	if (connection != null) {
	    setLayout(new BorderLayout());
	    createTable();
	    createPanelButtons();
	    add(pButtons, BorderLayout.NORTH);
	    add(scrTable, BorderLayout.CENTER);
	    setVisible(true);
	    setUpdateDelete();
            setInsert();
	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }
     protected void setSelect_filter() {
	select = "SELECT ID, Pavadinimas FROM " + table_name + " WHERE Pavadinimas LIKE ? ORDER BY LENGTH(ID), ID";
     }

     protected void setSelect_limit() {
	select = "SELECT ID, Pavadinimas FROM " + table_name + " ORDER BY LENGTH(ID), ID LIMIT 50";
     }
     
     
     protected void setUpdateDelete(){
        update = "UPDATE " + table_name + " SET Pavadinimas = ? WHERE ID = ?";
        delete = "DELETE FROM " + table_name + " WHERE ID = ?";
    }
    
    protected void setInsert(){
        insert = "INSERT INTO " + table_name + " (Pavadinimas) VALUES (?)" ;
    }
    
    protected void createTable() {
	tableModel = new DefaultTableModel(new Object[]{id_name, "Pavadinimas"}, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setAutoCreateRowSorter(true);
//	table.getSelectionModel().addListSelectionListener(this);
	setColumnsWidths();
	tableModel.setRowCount(1);
	scrTable = new JScrollPane(table);
    }
    
    protected void createPanelButtons() {
	chSearch = new JMyCheckBox("Paieška:", true, fontsize);
        pButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        pButtons.add(chSearch);
        tfSearch = new JMyTextField(20, fontsize);
        tfSearch.setActionCommand("filter");
	tfSearch.addActionListener(this);
	pButtons.add(tfSearch);	
        btFilter = new JMyButton("Rodyti", fontsize);
        btFilter.setToolTipText("50 paskutiniųjų");
        btFilter.setMnemonic('R');
	btFilter.setActionCommand("filter");
	btFilter.addActionListener(this);
 	pButtons.add(btFilter);
        pButtons.add(Box.createHorizontalStrut(100));
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
    
    protected void setColumnsWidths() {
	TableColumn dieSpalte;
	dieSpalte = null;
	    for (int i = 0; i < table.getColumnCount(); i++) {
		dieSpalte = table.getColumnModel().getColumn(i);
		switch (i) {
		    case 0:
			dieSpalte.setPreferredWidth(20);
			break;
		    case 1:
			dieSpalte.setPreferredWidth(800);
			break;
		}
	    }
    }
	
    protected void filter() {
	int i, colcount;
        Object[] row;
	tableModel.setRowCount(0);
        ResultSet resultset;
	try {
            if (chSearch.isSelected()) {
                setSelect_filter();
                preparedSelect = connection.prepareStatement(select);
                setPrepared();
                resultset = preparedSelect.executeQuery();
            } else {
                setSelect_limit();
                resultset = connection.executeQuery(select);
            }
            colcount = tableModel.getColumnCount();
            row = new Object[colcount];
            while (resultset.next() ){
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

    protected void setPrepared() {
        try {        
            preparedSelect.setString(1, "%" + tfSearch.getText() + "%");
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    protected void insert() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedInsert == null) {
		    preparedInsert = connection.prepareStatement(insert);
		}
		// ID, Pavadinimas
		preparedInsert.setString(1, (String) table.getValueAt(row, 1));
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

    protected void update() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(update);
		}
		preparedUpdate.setString(1, (String) table.getValueAt(row, 1));
		preparedUpdate.setInt(2, Integer.valueOf(String.valueOf(table.getValueAt(row, 0))));
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

    protected void delete() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedDelete == null) {
		    preparedDelete = connection.prepareStatement(delete);
		}
// ID, Pavadinimas
		preparedDelete.setInt(1, (int) table.getValueAt(row, 0));
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
    
    protected void enableButtons(boolean enabled) {
        btDelete.setEnabled(enabled);
        btEdit.setEnabled(enabled);
        btFilter.setEnabled(enabled);
        btInsert.setEnabled(enabled);
    }

    public void setConnection(ConnectionEquipment the_connection) {
        connection = the_connection;
        enableButtons(true);
    }

    public void disconnect() {
	connection = null;
        enableButtons(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "update":
		update();
		filter();
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

    
}
