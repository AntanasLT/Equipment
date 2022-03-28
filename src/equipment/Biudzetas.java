/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Antanas Kvietkauskas
 */
public class Biudzetas extends IDString_n {
    
    public Biudzetas(ConnectionEquipment the_connection, int size, String tbl, String[] db_fields, String[] tbl_cols, int[] tbl_col_with) {
        super(the_connection, size, tbl, db_fields, tbl_cols, tbl_col_with, false);
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
    protected void filter() {
	int i, colcount;
        Object[] row;
        Object value;
	tableModel.setRowCount(0);
        ResultSet resultset;
	try {
            if (chSearch.isSelected()) {
                preparedSelect = connection.prepareStatement(select);
                setPrepared();
                resultset = preparedSelect.executeQuery();
                colcount = dbCols.length;
                row = new Object[tableModel.getColumnCount()];
                while (resultset.next() ){
                    for (i = 0; i < colcount; i++) {
                        row[i] = resultset.getObject(i + 1);
                    }
                    tableModel.addRow(row);
                }
                resultset.close();
            }
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }

    
    @Override
    protected void insert() {
	float value, value0;
        int row;
        String s;
	row = table.getSelectedRow();
        value0 = 0; value = value0;
	if (row >= 0) {
	    try {
		if (preparedInsert == null) {
		    preparedInsert = connection.prepareStatement(insert);
		}
//System.out.println(insert);
                preparedInsert.setString(1, table.getValueAt(row, 0).toString());
                preparedInsert.setString(2, table.getValueAt(row, 1).toString());
                value = Float.parseFloat(table.getValueAt(row, 14).toString());
                if (value > 0){
                    value0 = value / 12;
                    for (int i = 2; i <= 14; i++) {
                        preparedInsert.setFloat(i+1, value0);
                    }
                    preparedInsert.setFloat(15, value);
                    
                } else {
                    for (int i = 2; i <= 14; i++) {
                        s = table.getValueAt(row, i).toString();
                        value0 = value != 0 ? value : value0;
                        value = Float.parseFloat(s);
                        value = value == 0 ? value0 : value;
                        preparedInsert.setFloat(i+1, value);
                    }
                }
                preparedInsert.setString(16, (String) table.getValueAt(row, tblCols.length - 2));
                preparedInsert.setString(17, (String) table.getValueAt(row, tblCols.length - 1));
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
        Float value;
        String s;
	int row, i;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(update);
		}
                preparedUpdate.setString(1, (String) table.getValueAt(row, 1));
                for (i = 2; i <= 14; i++) {
                    s = table.getValueAt(row, i).toString();
                    value = s.isEmpty() ? 0 : Float.parseFloat(s);
                    preparedUpdate.setFloat(i, value);
                }
                preparedUpdate.setString(15, String.valueOf(table.getValueAt(row, 15)));
                preparedUpdate.setString(16, String.valueOf(table.getValueAt(row, 16)));
		preparedUpdate.setString(17, String.valueOf(table.getValueAt(row, 0)));
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

}
