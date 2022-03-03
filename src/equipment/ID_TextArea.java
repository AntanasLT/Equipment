/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import datum.Datum;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author a
 */
public class ID_TextArea extends ID_auto implements MouseListener {
    
    JMyTextArea taText;
    JScrollPane scrPane;
    
    String taField;
    String[] dbCols, tblCols;
    Dimension taSize;
    int[] tblColWidth;
    int i0;
    
    private static final String FOLDER = "Ivairus";

    /**
     *
     * @param the_connection
     * @param size font size
     * @param tbl DB table
     * @param db_fields DB fileds (the first field – primary key)
     * @param tbl_cols JTable columns
     * @param tbl_col_with width of column of JTable
     * @param id_auto_increment
     * @param textArea_field
     * @param texAreaSize
     */
    public ID_TextArea (ConnectionEquipment the_connection, int size, String tbl, String[] db_fields, String[] tbl_cols, int[] tbl_col_with, boolean id_auto_increment, String textArea_field, Dimension texAreaSize) {
        super(the_connection, size, tbl);
        dbCols = db_fields;
        tblCols = tbl_cols;
        tblColWidth = tbl_col_with;
        i0 = id_auto_increment ? 1 : 0;
        taField = textArea_field;
        taSize = texAreaSize;
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
    protected void setSelect_limit() {
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
        sb1.append(" FROM ").append(table_name);
        sb1.append(" ORDER BY LENGTH(").append(dbCols[0]).append("), ").append(dbCols[0]).append(" LIMIT 50");
        select = sb1.toString();
    }
    
    @Override
    protected void createPanelButtons() {
	chSearch = new JMyCheckBox("Paieška:", true, fontsize);
        pButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        pButtons.add(chSearch);
        taText = new JMyTextArea(taSize.height, taSize.width, fontsize);
        taText.addMouseListener(this);
        taText.setToolTipText("Μπορεί να ανοίγει το επιλεγμένο αρχείο, που βρίσκεται στον φάκελο Ivairus");
        taText.setLineWrap(true);
        taText.setWrapStyleWord(true);
        scrPane = new JScrollPane(taText);
        scrPane.setMinimumSize(taSize);
	pButtons.add(scrPane);	
        btFilter = new JMyButton("Rodyti", fontsize);
        btFilter.setMnemonic('R');
	btFilter.setActionCommand("filter");
	btFilter.addActionListener(this);
 	pButtons.add(btFilter);
        pButtons.add(Box.createHorizontalStrut(20));
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
    
    @Override
    protected void setPrepared() {
        String txt;
        txt = "%" + taText.getText() + "%";
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
        table.addMouseListener(this);
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
                    if (dbCols[i].equals(taField)) {
                        preparedInsert.setString(i, taText.getText());
                    } else {
                        preparedInsert.setString(i, String.valueOf(table.getValueAt(row, i)));
                    }
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
                    if (dbCols[i].equals(taField)) {
                        preparedUpdate.setString(i, taText.getText());
                    } else {
                        preparedUpdate.setString(i, String.valueOf(table.getValueAt(row, i)));
                    }
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

    protected void openFile(String folder, String filename) {
        try {
            if (filename.startsWith("http") || filename.contains("www")) {
                Desktop.getDesktop().browse(new URL(filename).toURI());
            } else {
                File file = new File(folder, filename);
                if (file.exists()) {
//                    if (filename.endsWith("txt")) {
//                        Desktop.getDesktop().edit(file);
//                    }
                    Desktop.getDesktop().open(file);
                } else {
                    throw new IOException(filename + ": nėra!");
                }
            }
        } catch (IOException | URISyntaxException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        int col, row;
        if (me.getComponent().equals(table)) {
            col = table.getSelectedColumn();
            row = table.getSelectedRow();
            taText.setText((String) table.getValueAt(table.getSelectedRow(), tableModel.findColumn(taField)));
            if (me.getButton() == 2 && col == tableModel.findColumn("Data")) {
                Datum date = new Datum();
                table.setValueAt(date.getDate(), row, col);
            }
        }
        if ((me.getComponent().equals(taText)) & me.getButton() == 3) {
            openFile(FOLDER, taText.getSelectedText());
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    
}
