/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author a
 */
public class ID_noauto extends ID_auto {

    public ID_noauto(ConnectionEquipment the_connection, int size, String tbl) {
        super(the_connection, size, tbl);
    }

    @Override
    protected void setInsert(){
        insert = "INSERT INTO " + table_name + " (ID, Pavadinimas) VALUES (?, ?)" ;
    }
    
    
//    @Override
//    protected void setPrepared() {
//        String txt;
//        txt = "%" + tfSearch.getText() + "%";
//        try {        
////            preparedSelect.setInt(1, Integer.parseInt(tfSearch.getText()));
//            preparedSelect.setString(1, txt);
//        } catch (SQLException ex) {
//           JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    
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
		preparedInsert.setInt(1, Integer.valueOf((String) table.getValueAt(row, 0)));
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

    
}
