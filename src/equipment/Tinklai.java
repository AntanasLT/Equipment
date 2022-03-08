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
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author a
 */
public class Tinklai extends Turtas {

    private static final String PREPARE_SELECT_0 = "SELECT IP, Free, MAC, Device, Location, User, Password, Room, Plint, Switch, Port, Pastaba FROM Tinklai WHERE";
//    private static final String PREPARE_SELECT_0 = "SELECT Nr, Pavadinimas, Vamzdziu_skc, IT, Pastaba FROM Introskopai ";
//    private static final String PREPARE_SELECT_1 = "WHERE IP LIKE ? OR MAC LIKE ? OR Device LIKE ? OR Location LIKE ? OR User LIKE ? OR Password LIKE ? OR Room LIKE ? OR Plint LIKE ? OR Plint LIKE ? OR Switch LIKE ? OR Port LIKE ? OR Pastaba LIKE ? ";
    private static final String PREPARE_INSERT = "INSERT INTO Tinklai (IP, Free, MAC, Device, Location, User, Password, Room, Plint, Switch, Port, Pastaba) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String PREPARE_UPDATE = "UPDATE Tinklai SET Free = ?, MAC = ?, Device = ?, Location = ?, User = ?, Password = ?, Room = ?, Plint = ?, Switch = ?, Port = ?, Pastaba = ? WHERE IP = ?";
//    private static final String SELECT_POTINKLIAI = "SELECT IP, VLAN FROM Potinkliai ORDER BY IP";

    private static final int FIELDS_COUNT = 12;
    
    private static final String TABLE = "Tinklai";
    protected static final String IP = "IP";
    protected static final String FREE = "?";
    private static final String MAC = "MAC";
    private static final String DEVICE = "Įrenginys";
    private static final String LOCATION = "Vieta";
    private static final String USER = "Vartotojas";
    private static final String PASSWORD = "Slaptažodis";
    private static final String ROOM = "Serverinė";
    private static final String PLINT = "Lizdas";
    private static final String SWITCH = "Switch";
    private static final String PORT = "Portas";
    private static final String PASTABA = "Pastaba";

    JPanel pSearch1, pSearch, pSearch2;
    JMyTextField fSearch, fFree;
    JMyButton btSearch;
    JMyComboBox cbSubnets;
    JMyCheckBox chSearch, chNetworks, chFree;
    
    PreparedStatement preparedSelect;

    String[] networks;
    int fields_count;

    public Tinklai(ConnectionEquipment connection, int size) {
	super(connection, size);
    }

    @Override
    protected void setConstants() {
//	select = PREPARE_SELECT;
        insert = PREPARE_INSERT;
        delete = "DELETE FROM " + TABLE + " WHERE IP = ?";
        fields_count = FIELDS_COUNT;
   }

    @Override
    protected void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
        table_columns = new String[]{IP, FREE, MAC, DEVICE, LOCATION, USER, PASSWORD, ROOM, PLINT, SWITCH, PORT, PASTABA};
        table_column_width = new int[]{10*fontsize, fontsize, 5*fontsize, 20*fontsize, 10*fontsize, 6*fontsize, 8*fontsize, 6*fontsize, 4*fontsize, 10*fontsize, 3*fontsize, 40*fontsize};
	if (connection != null) {
            getNetworks();
	    setLayout(new BorderLayout());
	    createTable();
//            setColumnsWidths();
            createPanelButtons();
	    createPanelSearch();
	    add(pInput, BorderLayout.NORTH);
	    add(scrTable, BorderLayout.CENTER);
	    setVisible(true);
	    setConstants();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    @Override
    protected void createTable() {
	tableModel = new DefaultTableModel(table_columns, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
//	table.setDefaultEditor(Object.class, null);
        table.addMouseListener(this);
//        table.setToolTipText("Dvigubas spragtelėjimas išfiltruoja susijusius įrašus");
	table.setAutoCreateRowSorter(true);
	setColumnsWidths();
	scrTable = new JScrollPane(table);
    }


    protected void createPanelSearch() {
	pSearch1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
	pInput = new JPanel(new BorderLayout(2, 10));
	chSearch = new JMyCheckBox("Paieška:", false, fontsize);
	pSearch1.add(chSearch);
	fSearch = new JMyTextField(20, fontsize);
	fSearch.addActionListener(this);
	pSearch1.add(fSearch);
        pSearch1.add(Box.createHorizontalStrut(20));
        chNetworks = new JMyCheckBox("Potinklis:", true, fontsize);
        pSearch1.add(chNetworks);
        cbSubnets = new JMyComboBox(networks, fontsize);
        pSearch1.add(cbSubnets);
        pSearch1.add(Box.createHorizontalStrut(20));
        chFree = new JMyCheckBox("Laisvas (T/N):", false, fontsize);
        pSearch1.add(chFree);
        fFree = new JMyTextField(2, fontsize);
        pSearch1.add(fFree);
        pSearch1.add(Box.createHorizontalStrut(20));
        pSearch1.add(pButtons);
        pSearch = new JPanel(new BorderLayout());
        pSearch.add(pSearch1, BorderLayout.NORTH);
        taMessage = new JMyTextArea_monospaced(2, 50, fontsize);
        taMessage.addMouseListener(this);
        taMessage.setLineWrap(true);
        taMessage.setWrapStyleWord(true);
        scrMessage = new JScrollPane(taMessage);
        pSearch.add(scrMessage, BorderLayout.CENTER);
	pInput.add(pSearch, BorderLayout.NORTH);
//	pInput.add(pButtons, BorderLayout.EAST);
    }

    
    
    @Override
    protected void filter() {
	int i, colcount;
        Object[] row;
	StringBuilder sb;
	tableModel.setRowCount(0);
        ResultSet resultset;
	try {
	    sb = prepareFilter();
            if (sb != null) {
                preparedFilter = connection.prepareStatement(sb.toString());
                preparedFilter_setPrepared(sb);
                resultset = preparedFilter.executeQuery();
                colcount = tableModel.getColumnCount();
                row = new Object[colcount];
                while (resultset.next() ){
                    for (i = 0; i <= colcount - 1; i++) {
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

// IP LIKE ? OR MAC LIKE ? OR Device LIKE ? OR Location LIKE ? OR User LIKE ? OR Password LIKE ? OR Room LIKE ? OR Plint LIKE ? OR Plint LIKE ? OR Switch LIKE ? OR Port LIKE ? OR Pastaba LIKE ? 
    @Override
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
        sb = null;
        if (chSearch.isSelected() || chNetworks.isSelected() || chFree.isSelected()) {
            sb = new StringBuilder(PREPARE_SELECT_0);
            if (chSearch.isSelected()) {
                sb.append(" IP LIKE ? OR MAC LIKE ? OR Device LIKE ? OR Location LIKE ? OR User LIKE ? OR Password LIKE ? OR Room LIKE ? OR Plint LIKE ? OR Plint LIKE ? OR Switch LIKE ? OR Port LIKE ? OR Pastaba LIKE ? ");
            } else {
                if (chNetworks.isSelected()) {
                    sb.append(" IP LIKE ? ");
                }
                if (chFree.isSelected()) {
                    appendAND(sb);
                    sb.append(" Free LIKE ? ");
                }          
            }
            sb.append("ORDER BY LENGTH(IP), IP");
        }
//        System.out.println(sb);
        return sb;
    }
    
    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n;
        String filter;
	n = 0;
        if (chSearch.isSelected()) {
            filter = "%" + fSearch.getText() + "%";
            for (i = 1; i <= 12; i++) {
                preparedFilter.setString(i, filter);
            }
            
        } else {
            i = sb.indexOf(" IP LIKE ?");
            if (i >= 0 && chNetworks.isSelected()) {
                n++;
                preparedFilter.setString(n, "%" + ((String) cbSubnets.getSelectedItem()) +"%");
            }
            i = sb.indexOf(" Free LIKE ?");
            if (i >= 0) {
                n++;
                preparedFilter.setString(n, "%".concat(fFree.getText().concat("%")));
            }
        }
    }
    
    
// UPDATE Tinklai SET Free = ?, MAC = ?, Device = ?, Location = ?, User = ?, Password = ?, Room = ?, Plint = ?, Switch = ?, Port = ?, Pastaba = ? WHERE IP = ?
    @Override
    protected void update() {
	int row_selected, i;
        String pastaba;
	row_selected = table.getSelectedRow();
	if (row_selected >= 0) {
	    try {
                pastaba = !table.isFocusOwner() ? taMessage.getText() : (String) table.getValueAt(row_selected, 11);
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
		}
                for (i = 1; i < FIELDS_COUNT-1; i++) {
                    preparedUpdate.setString(i, (String) table.getValueAt(row_selected, i));
                }
                preparedUpdate.setString(11, pastaba);
                preparedUpdate.setString(12, (String) table.getValueAt(row_selected, 0));
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

// INSERT INTO Tinklai (IP, Free, MAC, Device, Location, User, Password, Room, Plint, Switch, Port, Pastaba) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    @Override
    protected void insert() {
	int row_selected, i;
        String pastaba;
	row_selected = table.getSelectedRow();
	if (row_selected >= 0) {
	    try {
		if (preparedInsert == null) {
		    preparedInsert = connection.prepareStatement(PREPARE_INSERT);
		}
                for (i = 1; i < FIELDS_COUNT; i++) {
                    preparedInsert.setString(i, (String) table.getValueAt(row_selected, i-1));
                }
                pastaba = taMessage.hasFocus() ? taMessage.getText() : (String) table.getValueAt(row_selected, 11);
                preparedInsert.setString(12, pastaba);
		if (preparedInsert.executeUpdate() == 1) {
		    filter();
		} else {
                    JOptionPane.showMessageDialog(this, "Neįrašyta (gal toks adresas jau yra?)", "Klaida!!", JOptionPane.ERROR_MESSAGE);
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
	int row_selected;
	row_selected = table.getSelectedRow();
	if (row_selected >= 0) {
	    try {
		if (preparedDelete == null) {
		    preparedDelete = connection.prepareStatement(delete);
		}
		preparedDelete.setString(1, (String) table.getValueAt(row_selected, 0));
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

    @Override
    protected void setComboBoxItem(JComboBox cb, String[] s, String name) {
	int i, n;
	boolean found;
	i = 0;
	found = false;
	n = cb.getItemCount();
	while (i <= n & !found) {
	    if (s[i].equals(name)) {
		found = true;
		cb.setSelectedIndex(i);
	    }
	    i++;
	}
    }

    private void getNetworks() {
        try {
            networks = connection.getNetworks();
        } catch (SQLException ex) {
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
	}
	
    }


   @Override
    public void mouseClicked(MouseEvent me) {
	if (me.getComponent().equals(table)){
            row = table.getSelectedRow();
            if (row >= 0) {
                taMessage.setText(String.valueOf(table.getValueAt(row, tableModel.findColumn(PASTABA))));
            }
        }
	if (me.getComponent().equals(taMessage) & me.getButton() == 3){
            openFile("", taMessage.getSelectedText());
        }
        
    }
            
}
