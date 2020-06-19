/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author a
 */
public class Devices extends JPanel implements ActionListener, ListSelectionListener {

    static final String SELECT_ALL = "SELECT i.ID, i.IT, i.Nr, i.Pavadinimas, s.Pavadinimas FROM Irenginiai i LEFT JOIN Sistemos s ON i.Sistema = s.ID ORDER BY i.Sistema";
    static final String DELETE = "DELETE FROM Irenginiai WHERE ID = ";
    static final String INSERT = "INSERT INTO Irenginiai (ID, IT, Nr, Pavadinimas, Sistema) VALUES ('";
    static final String UPDATE_0 = "UPDATE Irenginiai SET IT = '";
    static final String UPDATE_NR = "', Nr = '";
    static final String UPDATE_PAVADINIMAS = "', Pavadinimas = '";
    static final String UPDATE_SISTEMA = "', Sistema = ";
    static final String UPDATE_FINISH = " WHERE ID = ";
    static final String ID = "ID";
    static final String IT = "IT";
    static final String NR = "Nr";
    static final String PAVADINIMAS = "Pavadinimas";
    static final String SISTEMA = "Sistema";

    ConnectionEquipment connection;
    DefaultTableModel tableModel;
    JMyButton buttonDelete, buttonAdd, buttonChange, buttonFilter;
    JPanel panel;
    JScrollPane scrollpane;
    JTable table;
    JRadioButton radioButton1;


    public Devices(ConnectionEquipment the_connection) {
	connection = the_connection;
	init();
    }

    private void init() {
//	if (connection != null) {
	setLayout(new BorderLayout());
	createTable();
	createButtons();
	add(panel, BorderLayout.NORTH);
	add(scrollpane, BorderLayout.CENTER);
	setVisible(true);
//	} else {
//	    JOptionPane.showMessageDialog(this, "No connection!", "Error!", JOptionPane.ERROR_MESSAGE);
//        }
    }

    private void createButtons() {
	panel = new JPanel();
	buttonChange = new JMyButton("Išsaugoti");
	buttonChange.setActionCommand("update");
	buttonChange.addActionListener(this);
	buttonAdd = new JMyButton("Pridėti");
	buttonAdd.setActionCommand("insert");
	buttonAdd.addActionListener(this);
	buttonDelete = new JMyButton("Pašalinti");
	buttonDelete.setActionCommand("delete");
	buttonDelete.addActionListener(this);
	buttonFilter = new JMyButton("Filtruoti");
	buttonFilter.setActionCommand("filter");
	buttonFilter.addActionListener(this);
	panel.add(buttonFilter);
	panel.add(buttonChange);
	panel.add(buttonAdd);
	panel.add(buttonDelete);
    }

   
    private void createTable() {
	tableModel = new DefaultTableModel(new Object[]{ID, IT, NR, PAVADINIMAS, SISTEMA}, 0);
	table = new JTable(tableModel);
	table.setAutoCreateRowSorter(true);
	table.getSelectionModel().addListSelectionListener(this);
//	setztSpaltenbreiten();
//	setzt_dieUeberschriften();
	scrollpane = new JScrollPane(table);
    }

//        private void setztSpaltenbreiten() {
//	TableColumn column;
//	column = null;
//	    for (int i = 0; i < table.getColumnCount(); i++) {
//		column = table.getColumnModel().getColumn(i);
//                if (tableModel.getColumnName(i).equalsIgnoreCase(ID)) {
//                    column.setPreferredWidth(10);
//                } else if (tableModel.getColumnName(i).equals(PASTABOS)) {
//                    column.setPreferredWidth(300);
//                } else
//                    column.setPreferredWidth(50);
//	    }
//    }
	
//    private void setzt_dieUeberschriften(){
//	table.getTableHeader().setPreferredSize(new Dimension(table.getWidth(), 60));
//        table.getColumnModel().getColumn(1).setHeaderValue("<html>Der<br>Typ</html>");
//    }
	

    private void filter(String query){
        Object[] row;
	int i, colcount;
	tableModel.setRowCount(0);
        ResultSet resultset;
	try {
            resultset = connection.executeQuery(query);
	    colcount = tableModel.getColumnCount();
//            System.out.println(colcount);
	    row = new Object[colcount];
	    while( resultset.next() ){
		for (i = 0; i <= colcount - 1; i++) {
//                    System.out.print(i); 
		    row[i] = resultset.getObject(i + 1);
//                    System.out.println(row[i]);
		}
		tableModel.addRow(row);
	    }
	    resultset.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Λάθος!", JOptionPane.ERROR_MESSAGE);
	}

    }
	
    
    private void update() {
	int row;
	StringBuilder statement;
	row = table.getSelectedRow();
	if (row >= 0) {
	    statement = new StringBuilder(UPDATE_0);
	    statement.append(table.getValueAt(row, 1)).
                    append(UPDATE_NR).append(table.getValueAt(row, 2)).
                    append(UPDATE_PAVADINIMAS).append(table.getValueAt(row, 3)).
//                    append(UPDATE_SISTEMA).append(table.getValueAt(row, 4)).
                    append(UPDATE_FINISH).append(table.getValueAt(row, 0));
	    try {
		if (connection.executeUpdate(statement.toString()) == 1) {
		    filter(SELECT_ALL);
		};
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Λάθος!!", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }
    
    
    
    private void insert() {
	int row;
	StringBuilder statement;
	row = table.getSelectedRow();
	if (row >= 0) {
	    statement = new StringBuilder(INSERT);
	    statement.append(table.getValueAt(row, 1)).append("', '").
                    append(table.getValueAt(row, 2)).append("', '").
                    append(table.getValueAt(row, 3)).append("', ").
                    append(table.getValueAt(row, 4)).append(")");
	    try {
		if (connection.executeUpdate(statement.toString()) == 1) {
		    filter(SELECT_ALL);
		};
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Λάθος!!", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    private void delete() {
	int [] rows;
        int l, i;
	StringBuilder statement;
	rows = table.getSelectedRows();
        l = rows.length;
	if (l >= 0) {
	    statement = new StringBuilder(DELETE);	    
            for (i = 1; i <= l; i++) {
                statement.append(table.getValueAt(rows[i-1], 0));
                if (i < l) {
                    statement.append(" OR ID = ");
                }
            }
	    try {
		if (connection.executeUpdate(statement.toString()) == 1) {
		    filter(SELECT_ALL);
                    System.out.println();
		};
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Λάθος!!", JOptionPane.ERROR_MESSAGE);
	    }
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
		filter(SELECT_ALL);
		break;
	    case "insert":
		insert();
		break;	
	    case "delete":
		delete();
		filter(SELECT_ALL);
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