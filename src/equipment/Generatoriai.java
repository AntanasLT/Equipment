/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Generatoriai extends Turtas {

    static final String SELECT_ALL = "SELECT g.Nr, g.Pavadinimas, g.Vamzdzio_pavad, g.Vamzdzio_nr, g.Data, v.Pavadinimas, b.Busena FROM JSG g LEFT JOIN Gen_busenos b ON g.Busena = b.ID LEFT JOIN Vietos v ON g.Vieta = v.ID ORDER BY g.Nr";
    static final String SEARCH = "SELECT g.Nr, g.Pavadinimas, g.Vamzdzio_pavad, g.Vamzdzio_nr, g.Data, v.Pavadinimas, b.Busena FROM JSG g LEFT JOIN Gen_busenos b ON g.Busena = b.ID LEFT JOIN Vietos v ON g.Vieta = v.ID";
    static final String PREPARE_INSERT = "INSERT INTO JSG (Vieta, Pavadinimas, Data, Vamzdzio_pavad, Vamzdzio_nr, Busena, Nr) VALUES (?, ?, ?, ?, ?, ?, ?)";
    static final String PREPARE_UPDATE = "UPDATE JSG SET Vieta = ?, Pavadinimas = ?, Data = ?, Vamzdzio_pavad = ?, Vamzdzio_nr = ?, Busena = ? WHERE Nr = ?";

    static final String VIETA = "Vieta";
    static final String NR = "Nr";
    static final String PAVADINIMAS = "Pavadinimas";
    static final String DATA = "Data";
    static final String VAMZDZIO_PAVAD = "Vamzdis";
    static final String VAMZDZIO_NR = "Vamzdžio nr.";
    static final String BUSENA = "Būsena";
    
    private JLabelRechts lTubeNr, lTubeName;
    private JTextField fNr, fTubeNr, fTubeName;
    private JCheckBox chNr, chName, chTubeNr, chTubeName, chState, chLocation;
    private JMyComboBox cbState;

    
    private String[][] states;


    public Generatoriai(ConnectionEquipment connection, int size) {
	super(connection, size);
	init();
    }

    private void init() {
	if (connection != null) {
 	    setLayout(new BorderLayout());
	    createTable();
	    createPanelInput();
	    add(pInput, BorderLayout.NORTH);
//	    createTopPanel();
//	    add(panelTop, BorderLayout.NORTH);
	    add(scrPaneTable, BorderLayout.CENTER);
	    setVisible(true);
	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }
    

    @Override
    protected void createPanelFields() {
        pFields = new JPanel(new GridBagLayout());
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
//		panelInput.setBackground(Color.lightGray);
	gbc.insets = new Insets(0, 0, 5, 5);

// Μια πρώτη σειρά
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0;
	lNr = new JLabelRechts(NR, fontsize);
	pFields.add(lNr, gbc);

	gbc.gridx = 1;
	gbc.weightx = 0;
	fNr = new JMyTextField(7, fontsize);
	pFields.add(fNr, gbc);
        
	gbc.gridx = 2;
	gbc.weightx = 0;
	lName = new JLabelRechts(PAVADINIMAS, fontsize);
	pFields.add(lName, gbc);

	gbc.gridx = 3;
	gbc.weightx = 0;
	fName = new JMyTextField(12, fontsize);
	fName.addMouseListener(this);
	pFields.add(fName, gbc);
        
	gbc.gridx = 4;
	gbc.weightx = 0;
	lTubeName = new JLabelRechts(VAMZDZIO_PAVAD, fontsize);
	pFields.add(lTubeName, gbc);

	gbc.gridx = 5;
	gbc.weightx = 0;
        fTubeName = new JMyTextField(12, fontsize);
        pFields.add(fTubeName, gbc);
        
	gbc.gridx = 6;
	gbc.weightx = 0;
	lTubeNr = new JLabelRechts(VAMZDZIO_NR, fontsize);
	pFields.add(lTubeNr, gbc);

	gbc.gridx = 7;
	gbc.weightx = 0;
	fTubeNr = new JMyTextField(12, fontsize);
	fTubeNr.addMouseListener(this);
	pFields.add(fTubeNr, gbc);

	gbc.gridx = 8;
	gbc.weightx = 0;
	lDate = new JLabelRechts(DATA, fontsize);
	pFields.add(lDate, gbc);

	gbc.gridx = 9;
	gbc.weightx = 0;
	tfDate = new JMyTextField(date.getToday(), 8, fontsize);
	tfDate.addMouseListener(this);
	tfDate.setToolTipText("Dvigubas spragtelėjimas šiandienos datai");
	pFields.add(tfDate, gbc);

	gbc.gridx = 10;
	gbc.weightx = 0;
	lLocation = new JLabelRechts(VIETA, fontsize);
	pFields.add(lLocation, gbc);

	gbc.gridx = 11;
	gbc.weightx = 0;
	cbLocations = new JMyComboBox(locations[1], fontsize);
	pFields.add(cbLocations, gbc);

	gbc.gridx = 12;
	gbc.weightx = 0;
	pFields.add(new JLabelRechts(BUSENA, fontsize), gbc);

	gbc.gridx = 13;
	gbc.weightx = 0;
	cbState= new JMyComboBox(states[1], fontsize);
	pFields.add(cbState, gbc);
// Η δεύτερη σειρά
	gbc.gridy = 1;
	gbc.weightx = 0;
	gbc.gridx = 0;
	lFilters = new JLabelRechts("Filtrai:", fontsize);
	lFilters.addMouseListener(this);
	pFields.add(lFilters, gbc);
	
	gbc.gridx = 1;
	chNr = new JCheckBox();
	pFields.add(chNr, gbc);

	gbc.gridx = 3;
	chName = new JCheckBox();
	pFields.add(chName, gbc);

	gbc.gridx = 5;
	chTubeNr = new JCheckBox();
	pFields.add(chTubeNr, gbc);

	gbc.gridx = 7;
	chTubeName = new JCheckBox();
	pFields.add(chTubeName, gbc);

	gbc.gridx = 9;
	chDate = new JCheckBox();
	pFields.add(chDate, gbc);
	
	gbc.gridx = 11;
	chLocation = new JCheckBox();
	pFields.add(chLocation, gbc);
	
	gbc.gridx = 13;
	chState = new JCheckBox();
	pFields.add(chState, gbc);
	
    }


//    private void createTopPanel(){
//	createPanelButtons();
//	panelTop = new JPanel(new GridLayout(2, 1));
//	panelSearch = new JPanel();
//	lSearch = new JLabelRechts("Paieška:", fontsize);
//	panelSearch.add(lSearch);
//	fSearch = new JTextField(50);
//	panelSearch.add(fSearch);
//	panelTop.add(pButtons);
//	panelTop.add(panelSearch);
//    }

    @Override
       protected void getStates() {
	try {
	    states = connection.getStates_of_generators();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    protected void createTable() {
	tableModel = new DefaultTableModel(new Object[]{NR, PAVADINIMAS, VAMZDZIO_PAVAD, VAMZDZIO_NR, DATA, VIETA, BUSENA}, 0);
	table = new JTable(tableModel);
        table.setFont(font);
        table.getTableHeader().setFont(font);
	table.setAutoCreateRowSorter(true);
	table.getSelectionModel().addListSelectionListener(this);
	setColumnsWidths();
	tableModel.setRowCount(1);
//	setzt_dieUeberschriften();
	scrPaneTable = new JScrollPane(table);
    }

    private void setColumnsWidths() {
	TableColumn dieSpalte;
	dieSpalte = null;
	    for (int i = 0; i < table.getColumnCount(); i++) {
		dieSpalte = table.getColumnModel().getColumn(i);
		switch (tableModel.getColumnName(i)) {
//		    case PAVADINIMAS:
//			dieSpalte.setPreferredWidth(100);
//			break;
		    case DATA:
			dieSpalte.setPreferredWidth(100);
			break;
		}
	    }
    }
	
//    private void setzt_dieUeberschriften(){
//	table.getTableHeader().setPreferredSize(new Dimension(table.getWidth(), 60));
//        table.getColumnModel().getColumn(1).setHeaderValue("<html>Der<br>Typ</html>");
//    }
	
// SELECT g.Nr, g.Pavadinimas, g.Vamzdzio_pavad, g.Vamzdzio_nr, g.Date, v.Pavadinimas, b.Busena FROM JSG g LEFT JOIN Gen_busenos b ON g.Busena = b.ID LEFT JOIN Vietos v ON g.Vieta = v.ID WHERE g.Nr LIKE ? OR g.Pavadinimas LIKE ? OR g.Vamzdzio_pavad LIKE ? OR g.Vamzdzio_nr LIKE ? OR g.Data LIKE ? ORDER BY g.Nr

    private void filter() {
    if (chDate.isSelected() || chNr.isSelected() || chName.isSelected() || chLocation.isSelected() || chTubeName.isSelected() || chTubeNr.isSelected() || chState.isSelected()) {
        filter_by();
    } else {
        filter_all(SELECT_ALL);
    }
}

// SELECT g.Nr, g.Pavadinimas, g.Vamzdzio_pavad, g.Vamzdzio_nr, g.Date, v.Pavadinimas, b.Busena FROM JSG g LEFT JOIN Gen_busenos b ON g.Busena = b.ID LEFT JOIN Vietos v ON g.Vieta = v.ID
//     WHERE g.Nr LIKE ? OR g.Pavadinimas LIKE ? OR g.Vamzdzio_pavad LIKE ? OR g.Vamzdzio_nr LIKE ? OR g.Data LIKE ? OR g.Vieta = ? OR g.Busena = ?
    @Override
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(SEARCH);
        sb.append(" WHERE");
        if (chNr.isSelected()) {
            sb.append(" g.Nr LIKE ?");
	}
        if (chName.isSelected()) {
            appendAND(sb);
            sb.append(" g.Pavadinimas LIKE ?");
        }
        if (chTubeName.isSelected()) {
            appendAND(sb);
            sb.append(" g.Vamzdzio_pavad LIKE ?");
        }
        if (chTubeNr.isSelected()) {
            appendAND(sb);
	    sb.append(" g.Vamzdzio_nr LIKE ?");
        }
        if (chDate.isSelected()) {
            appendAND(sb);
            sb.append(" g.Data LIKE ?");
        }
        if (chLocation.isSelected()) {
            appendAND(sb);
            sb.append(" g.Vieta = ?");
        }
        if (chState.isSelected()) {
            appendAND(sb);
            sb.append(" g.Busena = ?");
        }
	sb.append(" ORDER BY g.Nr");
	return sb;
//	    sb.append(" i.Pozymis LIKE CONVERT (? USING utf8mb4) COLLATE utf8mb4_bin");
    }

    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n;
	n = 0;
	i = sb.indexOf(" g.Nr LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, fNr.getText());
	}
	i = sb.indexOf(" g.Pavadinimas LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, fName.getText());	    
	}
	i = sb.indexOf(" g.Vamzdzio_pavad LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, fTubeName.getText());
	}
	i = sb.indexOf(" g.Vamzdzio_nr LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, fTubeNr.getText());
	}
	i = sb.indexOf(" g.Data LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, tfDate.getText());
	}
	i = sb.indexOf(" g.Vieta = ?");
	if (i >= 0) {
 	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(locations[0][cbLocations.getSelectedIndex()]));
	}
	i = sb.indexOf(" g.Busena = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.valueOf(states[0][cbState.getSelectedIndex()]));
	}
    }

	
// UPDATE JSG SET Vieta = ?, Pavadinimas = ?, Data = ?, Vamzdzio_pavad = ?, Vamzdzio_nr = ?, Busena = ? WHERE Nr = ?
   private void update() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedUpdate == null) {
		    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
		}
		preparedUpdate.setInt(1, Integer.valueOf(locations[0][cbLocations.getSelectedIndex()]));
		preparedUpdate.setString(2, fName.getText());
		preparedUpdate.setString(3, tfDate.getText());
		preparedUpdate.setString(4, fTubeName.getText());
		preparedUpdate.setString(5, fTubeNr.getText());
                preparedUpdate.setInt(6, Integer.valueOf(states[0][cbState.getSelectedIndex()]));
		preparedUpdate.setString(7, fNr.getText());
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

// INSERT INTO JSG (Vieta, Pavadinimas, Data, Vamzdzio_pavad, Vamzdzio_nr, Busena, Nr) VALUES (?, ?, ?, ?, ?, ?, ?)
   private void insert() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
	    try {
		if (preparedInsert == null) {
		    preparedInsert = connection.prepareStatement(PREPARE_INSERT);
		}
                preparedInsert.setInt(1, Integer.valueOf(locations[0][cbLocations.getSelectedIndex()]));
		preparedInsert.setString(2, fName.getText());
		preparedInsert.setString(3, tfDate.getText());
		preparedInsert.setString(4, fTubeNr.getText());
		preparedInsert.setString(5, fTubeName.getText());
                preparedInsert.setInt(6, Integer.valueOf(states[0][cbState.getSelectedIndex()]));
		preparedInsert.setString(7, fNr.getText());
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
            fNr.setText(table.getValueAt(selectedRow, tableModel.findColumn(NR)).toString());
            fName.setText(table.getValueAt(selectedRow, tableModel.findColumn(PAVADINIMAS)).toString());
            fTubeNr.setText(table.getValueAt(selectedRow, tableModel.findColumn(VAMZDZIO_NR)).toString());
            fTubeName.setText(table.getValueAt(selectedRow, tableModel.findColumn(VAMZDZIO_PAVAD)).toString());
            tfDate.setText(table.getValueAt(selectedRow, tableModel.findColumn(DATA)).toString());
	    setComboBoxItem(cbLocations, locations[1], (String) table.getValueAt(selectedRow, tableModel.findColumn(VIETA)));
	    setComboBoxItem(cbState, states[1], (String) table.getValueAt(selectedRow, tableModel.findColumn(BUSENA)));
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
	if (me.getComponent().equals(tfDate) & me.getClickCount() == 2) {
	    tfDate.setText(date.getToday());
	}
        if (me.getClickCount() == 4) {
            if (me.getComponent().equals(fName)) {
                fName.setText("%%");
            }
            if (me.getComponent().equals(fTubeName)) {
                fTubeName.setText("%%");
            }
            if (me.getComponent().equals(fTubeNr)) {
                fTubeNr.setText("%%");
            }
        }
//	if (me.getComponent().equals(lFilters)) {
//	    clearCheckboxes();
//        }
    }


    
    
}
//	tableModel = new DefaultTableModel(new Object[]{"", "Datum", "<html>Fett<br>%</hmtl>", "<html>Muskeln<br>%</html>", "<html>Wasser<br>%</html>", "<html>Knochen<br>kg</html>", "<html>Masse<br>kg</html>", "<html>Energie0<br>kcal</html>", "<html>Energie1<br>kcal</html>", "<html>Bauch<br>cm</html>", "<html>Oberarm<br>cm</html>", "<html>Unterarm<br>cm</html>", "<html>Ober-<br>schenkel<br>cm</html>", "<html>Unter-<br>schenkel<br>cm</html>", "<html>Brust<br>cm</html>", "<html>Fettmasse<br>kg</html>", "<html>Muskel-<br>masse<br>kg</html>"}, 0);