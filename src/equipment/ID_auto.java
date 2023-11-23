/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
public class ID_auto extends JPanel implements ActionListener, MouseListener {
    
    private static final String HTML_START = "<!DOCTYPE html>\n" +
"<!--\n" +
"To change this license header, choose License Headers in Project Properties.\n" +
"To change this template file, choose Tools | Templates\n" +
"and open the template in the editor.\n" +
"-->\n" +
"<html>\n" +
"<style>\n" +
"p {\n" +
"  text-align: justify;\n" +
"  font-family: \"Arial\";\n" +
"}\n" +
"\n" +
"table, th, td {\n" +
"  border-collapse: collapse;\n" +
"  border: 1px solid; \n" +
"}\n" +
"</style>" +
"    <head>\n" +
"        <title>Darbeliai</title>\n" +
"        <meta charset=\"UTF-8\">\n" +
"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"    </head>\n" +
"    <body>\n" +
"       <table border=\"1\">\n";
    private static final String HTML_END = "</table>\n    </body>\n</html>";
    private static final String HTML_FILE = "Ivairus/Darbeliai.html";
    private static final String FOLDER = "";
    
    Font font;
    ConnectionEquipment connection;
    protected PreparedStatement preparedUpdate, preparedInsert, preparedSelect, preparedDelete;
    
    
    int fontsize;
    String select, delete, update, insert, table_name, id_name, folder;

    DefaultTableModel tableModel;
    JMyButton btInsert, btEdit, btDelete, btFilter;
    JMyCheckBox chSearch;
    JMyPopupMenu menuPop;
    JMyMenuItem miHtml;
    JMyTextField tfSearch;
    JPanel pInput;
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
        folder = FOLDER;
//	init();
    }

     public void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
	if (connection != null) {
	    setLayout(new BorderLayout());
            createPopup();
	    createTable();
            table.setComponentPopupMenu(menuPop);
            table.add(menuPop);
	    createPanelButtons();
	    add(pInput, BorderLayout.NORTH);
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
        table.addMouseListener(this);
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
        pInput = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        pInput.add(chSearch);
        tfSearch = new JMyTextField(20, fontsize);
        tfSearch.setActionCommand("filter");
	tfSearch.addActionListener(this);
	pInput.add(tfSearch);	
        btFilter = new JMyButton("Rodyti", fontsize);
        btFilter.setToolTipText("50 paskutiniųjų");
        btFilter.setMnemonic('R');
	btFilter.setActionCommand("filter");
	btFilter.addActionListener(this);
 	pInput.add(btFilter);
        pInput.add(Box.createHorizontalStrut(100));
	btEdit = new JMyButton("Pakeisti", fontsize);
        btEdit.setMnemonic('P');
 	btEdit.addActionListener(this);
	btEdit.setActionCommand("update");
	pInput.add(btEdit);
	btInsert = new JMyButton("Naujas", fontsize);
        btInsert.setMnemonic('N');
	btInsert.addActionListener(this);
	btInsert.setActionCommand("insert");
	pInput.add(btInsert);
	btDelete = new JMyButton("Šalinti", fontsize);
	btDelete.addActionListener(this);
	btDelete.setActionCommand("delete");
	pInput.add(btDelete);
    }
    
    protected void setColumnsWidths() {
	TableColumn dieSpalte;
//	dieSpalte = null;
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
	int i, n, colcount;
        Object[] the_row;
	tableModel.setRowCount(0);
        ResultSet resultset;
        n = 0;
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
            the_row = new Object[colcount];
            while (resultset.next() ){
                for (i = 0; i <= colcount - 1; i++) {
                    the_row[i] = resultset.getObject(i + 1);
                }
                tableModel.addRow(the_row);
                n++;
            }
            for (i = 1; i <= colcount - 1; i++) {
                the_row[i] = "";
            }
            the_row[0] = "Iš viso:";
            the_row[1] = n;
            tableModel.addRow(the_row);
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
		preparedUpdate.setInt(2, Integer.parseInt(String.valueOf(table.getValueAt(row, 0))));
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
	int[] rows;
	rows = table.getSelectedRows();
        try {
            if (preparedDelete == null) {
                preparedDelete = connection.prepareStatement(delete);
            }
            for (int row : rows) {
                preparedDelete.setInt(1, (int) table.getValueAt(row, 0));
                preparedDelete.execute();
            }
            if (preparedDelete.execute()) {
                filter();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
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
    
    protected void createPopup() {
        miHtml = new JMyMenuItem("Kurti html", fontsize);
        miHtml.addActionListener(this);
        miHtml.setActionCommand("html");
        menuPop = new JMyPopupMenu(fontsize);
        menuPop.add(miHtml);
    }
    
    protected void create_html() {
        StringBuilder sb;
        sb = new StringBuilder(HTML_START);
        sb.append("\n<tr>\n");
            for (int col = 0; col < table.getColumnCount(); col++) {
                sb.append("<th>").append(String.valueOf(table.getColumnName(col))).append("</thja>\n");
            }
        sb.append("\n</tr>\n");
        for (int row : table.getSelectedRows()) {
            sb.append("<tr>\n");
            for (int col = 0; col < table.getColumnCount(); col++) {
                sb.append("<td>").append(String.valueOf(table.getValueAt(row, col))).append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append(HTML_END);
        saveFile(HTML_FILE, sb.toString());
    }

    private void saveFile(String filename, String text) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(text);
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (JOptionPane.showConfirmDialog(this,  "Failas " + System.getProperty("user.dir") + System.getProperty("file.separator") + filename + " sukurtas. Rodyti?", "Sukurtas failas", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                openFile(folder, filename);
            }
        }    
    }
    
    protected void openFile(String folder, String filename) {
        if (filename == null) {          
            filename = "";
        }
        try {
            if (filename.startsWith("http") || filename.contains("www")) {
                Desktop.getDesktop().browse(new URL(filename).toURI());
            } else if (filename.endsWith("html")) {
                filename = "file://" + System.getProperty("user.dir") + System.getProperty("file.separator") + filename;
                Desktop.getDesktop().browse((new URL(filename).toURI()));
            } else {
                if (!filename.isEmpty()){
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
            }
        } catch (IOException | URISyntaxException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
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
	    case "html":
                create_html();
		break;	
	}
    }

    @Override
    public void mouseClicked(MouseEvent me) {
//        if (me.getComponent().equals(table) & me.getButton() == 3) {
//            openFile(folder, (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()));
//        }
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
