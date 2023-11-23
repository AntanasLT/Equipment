/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipment;

import datum.Datum;
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
    
    Datum date;
    
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
	    add(pInput, BorderLayout.NORTH);
	    add(scrTable, BorderLayout.CENTER);
	    setVisible(true);
            setDate_to_search_field();
	    setUpdateDelete();
            setInsert();
	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    
    @Override
    protected void setPrepared() {
        if (chSearch.isSelected()) {
            try {
                for (int i = 1; i <= dbCols.length; i++) {
                    preparedSelect.setString(i, "%" + tfSearch.getText() + "%");
                }
            } catch (SQLException ex) {
               JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }
    
    @Override
    protected void setSelect_filter() {
        StringBuilder sb1;
        int l;
        String txt, the_date;
        l = dbCols.length;
        sb1 = new StringBuilder("SELECT ");
        for (int i = 0; i < l; i++) {
            sb1.append("b.").append(dbCols[i]);
            if (i < l-1) {
                sb1.append(", ");
            }
        }
        txt = tfSearch.getText();
        if (chSearch.isSelected()) {
            if (txt.startsWith("202")) {
                sb1.append(", ROUND(SUM(s.Suma)), ROUND(SUM(s.Suma/M*100)) FROM Biudzetas b INNER JOIN Saskaitos s ON b.Kodas = s.BiudKodas WHERE b.Metai LIKE '%").append(txt).append("%' AND b.Metai = SUBSTR(s.Data, 1, 4) AND ("); //AND Skyrius = 'VNO_440'
            } else {
                sb1.append(", 0, 0 FROM Biudzetas b WHERE (");
                
//                sb1.append(", ROUND(SUM(Suma)), ROUND(SUM(Suma/M*100)) FROM Biudzetas, Saskaitos WHERE ").append(" Metai = SUBSTR(Data, 1, 4) AND BiudKodas = Kodas AND (");
            }
            for (int i = 0; i < l; i++) {
                sb1.append("b.").append(dbCols[i]).append(" LIKE ? ");
                if (i < l-1) {
                    sb1.append("OR ");
                }
            }
            sb1.append(") GROUP BY b.Kodas, b.Metai");
        } else {
            if (txt.startsWith("202")) {
                sb1.append(", 0, 0 FROM Biudzetas b WHERE b.Metai LIKE '%").append(txt).append("%' ");
            } else {
                sb1.append(", 0, 0 FROM Biudzetas b");
            }
        }
//System.out.println(sb1);
        select = sb1.toString();
    }
    
    private void setDate_to_search_field() {
        date = new Datum();
        tfSearch.setText(date.getYear());
    }

    @Override
    protected void filter() {
	int i, db_colcount, tbl_colcount, row_count;
        Float sum_all, percent_avg, value;
        Float[] sum;
        Object[] row;
        Object obj;
        ResultSet resultset;
	tableModel.setRowCount(0);
	try {
            setSelect_filter();
            preparedSelect = connection.prepareStatement(select);
            setPrepared();
            resultset = preparedSelect.executeQuery();
            db_colcount = dbCols.length;
            tbl_colcount = tableModel.getColumnCount();
            sum = new Float[tbl_colcount];
            sum_all = 0F; percent_avg = 0F; row_count = 0;
            for (i = 0; i < db_colcount; i++) {
                sum[i] = 0F;
            }
            row = new Object[tbl_colcount];
            while (resultset.next() ){
                for (i = 0; i < db_colcount+2; i++) {       // 2 στήλες ακόμα για ROUND(SUM(Suma)), ROUND(SUM(Suma/M*100))
                    row[i] = resultset.getObject(i + 1);
                    sum[i] = (i>1) & (i<15) & row[i] instanceof Float ? sum[i] + Float.valueOf(String.valueOf(row[i])) : 0F;
                    switch (i) {
                        case 17:
                            sum_all = sum_all + Float.valueOf(String.valueOf(row[i]));
                            break;
                        case 18:
                            percent_avg = percent_avg + Float.valueOf(String.valueOf(row[i]));
                            break;
                    }
                }
                tableModel.addRow(row);
                row_count++;
            }
            resultset.close();
            for (i = 0; i < db_colcount; i++) {
                row[i] = sum[i];
            }
            row[0] = ""; row[16] = ""; row[15] = row_count; 
            row[1] = "Sumos: "; 
            row[17] = sum_all;
            row[18] = row_count > 0 ? percent_avg / row_count : "";
            tableModel.addRow(row);
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	}
    }

    
    @Override
    protected void insert() {
	float value, value0, sum;
        int[] rows;
        String s;
	rows = table.getSelectedRows();
        value0 = 0; 
        value = value0;
        if (rows.length > 0) {
            for (int row : rows) {
                try {
                    if (preparedInsert == null) {
                        preparedInsert = connection.prepareStatement(insert);
                    }
    //System.out.println(insert);
                    preparedInsert.setString(1, table.getValueAt(row, tableModel.findColumn("Kodas")).toString());
                    preparedInsert.setString(2, table.getValueAt(row, tableModel.findColumn("Pavadinimas")).toString());
                    s = table.getValueAt(row, tableModel.findColumn("Metams")).toString().replace(',', '.');           // Metams
                    sum = s.isEmpty() ? 0 : Float.parseFloat(s);
                     if (sum > 0) {
                        value0 = sum / 12;
                        for (int i = tableModel.findColumn("Sausis"); i <= tableModel.findColumn("Metams"); i++) {
                            preparedInsert.setFloat(i+1, value0);
                        }
                        preparedInsert.setFloat(15, sum);

                    } else {
                        sum = 0F;
                        for (int i = tableModel.findColumn("Sausis"); i < tableModel.findColumn("Metams"); i++) {
                            s = table.getValueAt(row, i).toString().replace(',', '.');
//                            value0 = value != 0 ? value : value0;
                            value = Float.parseFloat(s);
//                            value = value == 0 ? value0 : value;
                            preparedInsert.setFloat(i+1, value);
                            sum = sum + value;
                        }
                        preparedInsert.setFloat(15, sum);
                    } 
                    preparedInsert.setString(16, (String) table.getValueAt(row, tableModel.findColumn("Skyrius")));
                    preparedInsert.setString(17, (String) table.getValueAt(row, tableModel.findColumn("Metai")));
                    if (preparedInsert.executeUpdate() == 1) {
                        filter();
                    }
                } catch (SQLException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}        
    }
    @Override
    protected void setUpdateDelete() {
        StringBuilder sb1;
        int dbColCount;
        dbColCount = dbCols.length;
        sb1 = new StringBuilder("UPDATE ");
        sb1.append(table_name).append(" SET ");
        for (int i = 1; i < dbColCount-1; i++) {
            sb1.append(dbCols[i]).append(" = ?");
            if (i < dbColCount-2) {
                sb1.append(", ");
            }
        }
        sb1.append(" WHERE Kodas = ? AND Metai = ?");
        update = sb1.toString();
//        System.out.println(update);
        delete = "DELETE FROM " + table_name + " WHERE Kodas = ? AND Metai = ?";
    }
    
    @Override
    protected void delete() {
	int[] rows;
	rows = table.getSelectedRows();
        try {
            if (preparedDelete == null) {
                preparedDelete = connection.prepareStatement(delete);
            }
            for (int row : rows) {
                preparedDelete.setString(1, (String) table.getValueAt(row, tableModel.findColumn("Kodas")));
                preparedDelete.setString(2, (String) table.getValueAt(row, tableModel.findColumn("Metai")));
                preparedDelete.execute();
            }
            filter();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    protected void update() {
        Float value, sum;
        String s;
	int row, i;
	row = table.getSelectedRow();
	if (row >= 0) {
            sum = 0F;
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(update);
		}
                preparedUpdate.setString(1, (String) table.getValueAt(row, tableModel.findColumn("Pavadinimas")));
                for (i = tableModel.findColumn("Sausis"); i < tableModel.findColumn("Metams"); i++) {                                            //14 – M(etams)
                    s = table.getValueAt(row, i).toString();
                    value = s.isEmpty() ? 0 : Float.valueOf(s);
                    preparedUpdate.setFloat(i, value);
                    sum = sum + value;
                }
                s = table.getValueAt(row, 14).toString();
                value = s.isEmpty() ? sum : Float.valueOf(s);
                preparedUpdate.setString(14, String.valueOf(value));
                preparedUpdate.setString(15, String.valueOf(table.getValueAt(row, tableModel.findColumn("Skyrius"))));
                preparedUpdate.setString(16, String.valueOf(table.getValueAt(row, tableModel.findColumn("Kodas"))));     //Kodas
		preparedUpdate.setString(17, String.valueOf(table.getValueAt(row, tableModel.findColumn("Metai"))));    //Metai
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
