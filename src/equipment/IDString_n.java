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
public class IDString_n extends ID_auto {
    
    String[] dbCols, tblCols;
    int[] tblColWidth;
    int i0;

    /**
     *
     * @param the_connection
     * @param size font size
     * @param tbl DB table
     * @param db_fields DB fileds (the first field – primary key)
     * @param tbl_cols JTable columns
     * @param tbl_col_with width of column of JTable
     * @param id_auto_increment
     */
    public IDString_n (ConnectionEquipment the_connection, int size, String tbl, String[] db_fields, String[] tbl_cols, int[] tbl_col_with, boolean id_auto_increment) {
        super(the_connection, size, tbl);
        dbCols = db_fields;
        tblCols = tbl_cols;
        tblColWidth = tbl_col_with;
        i0 = id_auto_increment ? 1 : 0;
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
	    setUpdateDelete();
            setInsert();
            setSelect_filter();
	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    protected void setSelect_filter() {
        StringBuilder sb1;
        int l;
        l = dbCols.length;
        sb1 = new StringBuilder("SELECT ");
        for (int i = 0; i < l; i++) {
            sb1.append(dbCols[i]);
            if (i < l-1) {
                sb1.append(", ");
            }
        }
        sb1.append(" FROM ").append(table_name).append(" WHERE ");
        for (int i = 0; i < l; i++) {
            sb1.append(dbCols[i]).append(" LIKE ? ");
            if (i < l-1) {
                sb1.append("OR ");
            }
        }
        sb1.append(" ORDER BY LENGTH(").append(dbCols[0]).append("), ").append(dbCols[0]);
        select = sb1.toString();
    }
    
    @Override
    protected void setPrepared() {
        String txt;
        txt = "%" + tfSearch.getText() + "%";
        try {
            for (int i = 1; i <= dbCols.length; i++) {
                preparedSelect.setString(i, txt);
            }
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    protected void setUpdateDelete() {
        StringBuilder sb1;
        int l;
        l = dbCols.length;
        sb1 = new StringBuilder("UPDATE ");
        sb1.append(table_name).append(" SET ");
        for (int i = 1; i < l; i++) {
            sb1.append(dbCols[i]).append(" = ?");
            if (i < l-1) {
                sb1.append(", ");
            }
        }
        sb1.append(" WHERE ").append(dbCols[0]).append(" = ?");
        update = sb1.toString();
        delete = "DELETE FROM " + table_name + " WHERE " + dbCols[0] + " = ?";
    }
    
    @Override
    protected void setInsert(){
        StringBuilder sb1, sb2;
        int l;
        l = dbCols.length;
        sb1 = new StringBuilder("INSERT INTO " + table_name + " (");
        sb2 = new StringBuilder(" VALUES (");
        for (int i = i0; i < l; i++) {
            sb1.append(dbCols[i]);
            sb2.append("?");
            if (i < l-1) {
                sb1.append(", ");
                sb2.append(", ");
            } else {
                sb1.append(")");
                sb2.append(")");
                sb1.append(sb2);
            }
        }
        insert = sb1.toString();
//        System.out.println(insert);
    }
    
    @Override
    protected void createTable() {
	tableModel = new DefaultTableModel(tblCols, 0);
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
    protected void setColumnsWidths() {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(tblColWidth[i]);
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
                for (int i = i0; i < tblCols.length; i++) {
                    preparedInsert.setString(i, String.valueOf(table.getValueAt(row, i)));
                }
		if (preparedInsert.executeUpdate() == 1) {
		    filter();
		}
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	    }
	} else {
            if (table.getRowCount() == 0) {
                tableModel.addRow(dbCols);
            } else {
                JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
            }
	}
    }

    @Override
    protected void update() {
	int row, i;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(update);
		}
                for (i = 1; i < dbCols.length; i++) {
                    preparedUpdate.setString(i, String.valueOf(table.getValueAt(row, i)));
                }
                if (i0 == 0) {
                    preparedUpdate.setString(i, (String) table.getValueAt(row, 0));                                 } else {
                    preparedUpdate.setInt(i, Integer.parseInt(String.valueOf(table.getValueAt(row, 0))));
                }
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
                if (i0 == 0) {
                    preparedDelete.setString(1, (String) table.getValueAt(row, 0));
                } else {
                    preparedDelete.setInt(1, Integer.parseInt(String.valueOf(table.getValueAt(row, 0))));
                }
		
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
