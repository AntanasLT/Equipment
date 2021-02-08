/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author a
 */
public class Introskopai extends Sistemos {

    private static final String SELECT_ALL = "SELECT i.Pavadinimas, i.Nr, i.Vamzdziu_skc, b.Pavadinimas FROM Introskopai i LEFT JOIN Gen_busenos b ON i.Busena = b.ID ORDER BY i.Nr";
    private static final String PREPARE_SELECT = "SELECT i.Pavadinimas, i.Nr, i.Vamzdziu_skc, b.Pavadinimas FROM Introskopai i LEFT JOIN Gen_busenos b ON i.Busena = b.ID";
    private static final String PREPARE_INSERT = "";
    private static final String PREPARE_UPDATE = "";
    
    protected static final String NR = "Nr.";
    protected static final String BUSENA = "Būsena";
    private static final String VAMZDZIU_SKC = "Vamzdžių skč.";

    JPanel pSearch;
    JMyTextField fSearch;
    JMyButton btSearch;
    JCheckBox chState;
    
    PreparedStatement preparedSelect;

    String[][] gen_states;
    String prepare_select;

    public Introskopai(ConnectionEquipment connection, int size) {
	super(connection, size);
	init();
    }

    @Override
    protected void setSelectQuery() {
	select = SELECT_ALL;
	prepare_select = PREPARE_SELECT;
    }

    @Override
    protected final void init() {
	if (connection != null) {
	    getGenStates();
	    setLayout(new BorderLayout());
	    createTable();
	    createPanelSearch();
	    add(pInput, BorderLayout.NORTH);
	    add(scrPaneTable, BorderLayout.CENTER);
	    setVisible(true);
	    setSelectQuery();
	    filter(select);
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }
    

    @Override
    protected void createTable() {
	tableModel = new DefaultTableModel(new Object[]{PAVADINIMAS, NR, VAMZDZIU_SKC, BUSENA}, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setAutoCreateRowSorter(true);
	table.getSelectionModel().addListSelectionListener(this);
	tableModel.setRowCount(1);
	scrPaneTable = new JScrollPane(table);
    }

    protected void createPanelSearch() {
	createPanelButtons();
	pSearch = new JPanel();
	cbState = new JMyComboBox(gen_states[1], fontsize);
	pInput = new JPanel(new BorderLayout());
	pSearch.add(new JLabelRechts("Paieška:", fontsize));
	fSearch = new JMyTextField(20, fontsize);
	fSearch.addActionListener(this);
	pSearch.add(fSearch);
	pSearch.add(cbState);
	chState = new JCheckBox();
	pSearch.add(chState);
	pInput.add(pSearch, BorderLayout.NORTH);
	pInput.add(pButtons, BorderLayout.SOUTH);
    }

    protected void getGenStates() {
	try {
	    gen_states = connection.getList("Gen_busenos");
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    

//ELECT i.Pavadinimas, i.Nr, i.Vamzdziu_skc, b.Pavadinimas FROM Introskopai i LEFT JOIN Gen_busenos b ON i.Busena = b.ID
    @Override
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(prepare_select);
        if (!fSearch.getText().isEmpty() || chState.isSelected()) {
	    sb.append(" WHERE");
	    if (chState.isSelected()) {
		sb.append(" i.Busena = ?");
	    }
	    if (!fSearch.getText().isEmpty()) {
		appendAND(sb);
		sb.append(" i.Pavadinimas LIKE ? OR i.Nr LIKE ?");
	    }
	 }
	sb.append(" ORDER BY i.Nr");
//	System.out.println(sb.toString());
	return sb;
    }
    
    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n;
	String zw;
	n = 0;
	i = sb.indexOf(" i.Busena = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(gen_states[0][cbState.getSelectedIndex()]));
	}
	i = sb.indexOf(" i.Pavadinimas LIKE ? OR i.Nr LIKE ?");
	if (i >= 0) {
	    zw = "%".concat(fSearch.getText()).concat("%");
	    n++;
	    preparedFilter.setString(n, zw);
	    n++;
	    preparedFilter.setString(n, zw);
	}
    }
    
    
//    protected void filter() {
//	if (chState.isSelected()) {
//	    search(Integer.parseInt(gen_states[0][cbState.getSelectedIndex()]));
//	} else {
//	    search();
//	}
//    }
//    
//    protected void search() {
//	if (fSearch.getText().isEmpty()) {
//	    filter(select);
//	} else {
//
//	}
//    }
//    
//    protected void search(int state) {
//	int i, colcount;
//	Object[] row;
//	StringBuilder sb;
//	tableModel.setRowCount(0);
//	ResultSet resultset;
//	sb = new StringBuilder(PREPARE_SELECT);
//	try {
//	    if (fSearch.getText().isEmpty()) {
//		sb.append("i.Busena = ?");
//		preparedSelect = connection.prepareStatement(sb.toString());
//		preparedSelect.setInt(1, state);
//	    }
//	    else {
//
//	    }
//	    
//	    resultset = preparedSelect.executeQuery();
//	    colcount = tableModel.getColumnCount();
//	    row = new Object[colcount];
//	    while( resultset.next() ){
//		for (i = 0; i <= colcount - 1; i++) {
//		    row[i] = resultset.getObject(i + 1);
//		}
//		tableModel.addRow(row);
//	    }
//	    resultset.close();
//	    preparedSelect.close();
//	} catch (SQLException ex) {
//	    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
//	}
//	
//    }
//   

//    private void update() {
//	int row;
//	row = table.getSelectedRow();
//	if (row >= 0) {
//	    try {
//		if (preparedUpdate == null) {
//		    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
//		}
//// IT, Nr, Pavadinimas, Sistema, ID
//		preparedUpdate.setString(1, (String) table.getValueAt(row, 1));
//		preparedUpdate.setInt(2, (int) table.getValueAt(row, 0));
//		if (preparedUpdate.executeUpdate() == 1) {
//		    filter(select);
//		}
//	    } catch (SQLException ex) {
//		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
//	    }
//	} else {
//	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
//	}
//    }
//
//    private void insert() {
//	int row;
//	row = table.getSelectedRow();
//	if (row >= 0) {
//	    try {
//		if (preparedInsert == null) {
//		    preparedInsert = connection.prepareStatement(PREPARE_INSERT);
//		}
//		// ID, Pavadinimas
//		preparedInsert.setString(1, (String) table.getValueAt(row, 1));
//		if (preparedInsert.executeUpdate() == 1) {
//		    filter(select);
//		}
//	    } catch (SQLException ex) {
//		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
//	    }
//	} else {
//	    JOptionPane.showMessageDialog(this, "Nepažymėta eilutė", "Klaida!!", JOptionPane.ERROR_MESSAGE);
//	}
//    }
//

    
    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "update":
//		update();
		filter(select);
		break;	
	    case "filter":
		filter();
		break;
	    case "insert":
//		insert();
		break;	
//	    case "delete":
//		delete();
//		filter();
//		break;	
	}
	
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
	selectedRow = table.getSelectedRow();
	if (selectedRow >= 0) {
	    setComboBoxItem(cbState, gen_states[1], (String) table.getValueAt(selectedRow, tableModel.findColumn(BUSENA)));
	}
    }

    @Override
    public void mouseClicked(MouseEvent me) {
//	if (me.getComponent().equals(tfDate) & me.getClickCount() == 2) {
//	    tfDate.setText(date.getToday());
//	}
//        if (me.getClickCount() == 4) {
//            if (me.getComponent().equals(fName)) {
//                fName.setText("%%");
//            }
//            if (me.getComponent().equals(fTubeName)) {
//                fTubeName.setText("%%");
//            }
//            if (me.getComponent().equals(fTubeNr)) {
//                fTubeNr.setText("%%");
//            }
//        }
//	if (me.getComponent().equals(lFilters)) {
//	    clearCheckboxes();
//        }
    }


    
    
}
//	tableModel = new DefaultTableModel(new Object[]{"", "Datum", "<html>Fett<br>%</hmtl>", "<html>Muskeln<br>%</html>", "<html>Wasser<br>%</html>", "<html>Knochen<br>kg</html>", "<html>Masse<br>kg</html>", "<html>Energie0<br>kcal</html>", "<html>Energie1<br>kcal</html>", "<html>Bauch<br>cm</html>", "<html>Oberarm<br>cm</html>", "<html>Unterarm<br>cm</html>", "<html>Ober-<br>schenkel<br>cm</html>", "<html>Unter-<br>schenkel<br>cm</html>", "<html>Brust<br>cm</html>", "<html>Fettmasse<br>kg</html>", "<html>Muskel-<br>masse<br>kg</html>"}, 0);