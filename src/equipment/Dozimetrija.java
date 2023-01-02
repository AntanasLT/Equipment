/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipment;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author a
 */
public class Dozimetrija extends IDString_n {
    
    String[] cols;

    
    public Dozimetrija(ConnectionEquipment the_connection, int size, String tbl, String[] db_fields, String[] tbl_cols, int[] tbl_col_with, boolean id_auto_increment) {
        super(the_connection, size, tbl, db_fields, tbl_cols, tbl_col_with, id_auto_increment);
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
        sb1.append(", Pavadinimas, Vieta ");
        sb1.append(" FROM ").append(table_name).append(", Introskopai WHERE Introskopas = Nr AND (");
        for (int i = 0; i < l; i++) {
            sb1.append(dbCols[i]).append(" LIKE ? ");
            if (i < l-1) {
                sb1.append("OR ");
            }
        }
        sb1.append(") ORDER BY LENGTH(").append(dbCols[0]).append("), ").append(dbCols[0]);
        select = sb1.toString();
    }
    
    @Override
    protected void createTable() {
        int n;
        n = tblCols.length;
        cols = new String[n + 2];
        System.arraycopy(tblCols, 0, cols, 0, n); // Αντιγραφή tblCols στο cols
        cols[n] = "Pavadinimas";
        cols[n+1] = "Vieta";
	tableModel = new DefaultTableModel(cols, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setAutoCreateRowSorter(true);
        table.addMouseListener(this);
//	table.getSelectionModel().addListSelectionListener(this);
	setColumnsWidths();
	tableModel.setRowCount(1);
	scrTable = new JScrollPane(table);
    }
    
    @Override
    protected void setColumnsWidths() {
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            table.getColumnModel().getColumn(i).setPreferredWidth(tblColWidth[i]);
//        }
    }
    
    
    
    
}
