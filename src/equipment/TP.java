/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import datum.Datum;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author a
 */
public class TP extends Darbai {

    private static final String PREPARE_SELECT_ALL = "SELECT tp.ID, tp.Data, s.Pavadinimas, tpr.Pavadinimas, tp.Pastaba FROM TP tp LEFT JOIN Sistemos s ON tp.Sistema = s.ID LEFT JOIN TPrusys tpr ON tp.TP = tpr.ID ORDER BY tp.Data DESC LIMIT 50";
    private static final String PREPARE_INSERT = "INSERT INTO TP (Data, Sistema, TP, Pastaba) VALUES (?, ?, ?, ?)";
    private static final String PREPARE_UPDATE = "UPDATE TP SET Data = ?, Sistema = ?, TP = ?, Pastaba = ? WHERE ID = ?";
    private static final String SELECT = "SELECT tp.ID, tp.Data, s.Pavadinimas, tpr.Pavadinimas, tp.Pastaba FROM TP tp LEFT JOIN Sistemos s ON tp.Sistema = s.ID LEFT JOIN TPrusys tpr ON tp.TP = tpr.ID";
    static final String DATE = "Data";
    static final String SYSTEM = "Sistema";
    static final String TP = "Rūšis";
    static final String NOTE = "Pastaba";
    static final String FOLDER = "Ivairus";
    

//    private DefaultTableModel tableModelTP;

    private JMyComboBox cbTPtype;
    private JCheckBox chTPtype;
    
    String[][] tptypes;

    protected TP(ConnectionEquipment connection, String server, int size) {
	super(connection, server, size);
    }

    @Override
    protected void setConstants(){
	select = PREPARE_SELECT_ALL;
        insert = PREPARE_INSERT;
        tableTooltip = "";
    }


    @Override
    protected void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
        date = new Datum();
        table_columns = new String[]{ID, DATE, SYSTEM, TP, NOTE};
        table_column_width = new int[]{4*fontsize, 8*fontsize, 8*fontsize, 8*fontsize, 70*fontsize};
	if (connection != null) {
	    setLayout(new BorderLayout());
            setConstants();
	    getTPtypes();
	    getSystems();
	    createTable();
	    createPanelInput();
	    add(pInput, BorderLayout.NORTH);
	    add(lMessage, BorderLayout.SOUTH);
	    add(scrTable, BorderLayout.CENTER);
	    setVisible(true);
//	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    protected void createPanelInput() {
	pInput = new JPanel(new BorderLayout());
	createPanelFields();
	pInput.add(pFields, BorderLayout.NORTH);
	createPanelFilterButtons();
	pInput.add(pnFIlterButtons, BorderLayout.SOUTH);
	lMessage = new JLabelLeft(fontsize);
    }

    @Override
    protected void createPanelFields() {
	pFields = new JPanel(new GridBagLayout());
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.insets = new Insets(0, 0, 5, 5);

// Μια πρώτη σειρά
	gbc.gridx = 0;
	gbc.gridy = 0;
//	gbc.weightx = 0;
	lDate = new JLabelRechts("Data:", fontsize);
	pFields.add(lDate, gbc);

	gbc.gridx = 1;
//	gbc.weightx = 0;
	tfDate = new JMyTextField(date.getToday(), 10, fontsize);
        tfDate.setToolTipText("Dukart spragt – šiandiena");
	tfDate.addMouseListener(this);
        tfDate.setToolTipText(date.getWeekday());
	pFields.add(tfDate, gbc);

	gbc.gridx = 2;
//	gbc.weightx = 0;
	lSystem = new JLabelRechts("Sistema", fontsize);
	pFields.add(lSystem, gbc);

	gbc.gridx = 3;
//	gbc.weightx = 0.5;
	cbIrenginys = new JMyComboBox(systems[1], fontsize);
	pFields.add(cbIrenginys, gbc);
//
	gbc.gridx = 4;
//	gbc.weightx = 0;
	lWork = new JLabelRechts("Darbas", fontsize);
	pFields.add(lWork, gbc);

	gbc.gridx = 5;
//	gbc.weightx = 0.5;
	cbTPtype = new JMyComboBox(tptypes[1], fontsize);
	pFields.add(cbTPtype, gbc);
//
// Η δεύτερη σειρά
	gbc.gridy = 1;

	gbc.gridx = 0;
	lFilters = new JLabelRechts("Filtrai:", fontsize);
	lFilters.addMouseListener(this);
	pFields.add(lFilters, gbc);

	gbc.gridx = 1;
	chDate = new JCheckBox();
	pFields.add(chDate, gbc);

	gbc.gridx = 3;
	chSystem = new JCheckBox();
	pFields.add(chSystem, gbc);

	gbc.gridx = 5;
	chTPtype = new JCheckBox();
	pFields.add(chTPtype, gbc);

// Η τρίτη σειρά
	gbc.gridy = 2;
	gbc.gridx = 0;
	gbc.weightx = 0;
	createPanelMessages();
	pFields.add(pMessage, gbc);

	gbc.gridx = 1;
	gbc.weightx = 0.5;
	gbc.gridwidth = 4;
	taMessage = new JMyTextArea_monospaced(15, 30, fontsize);
	taMessage.addMouseListener(this);
	taMessage.setLineWrap(true);
	taMessage.setWrapStyleWord(true);
	taMessage.setToolTipText("Dvigubas spragtelėjimas ištrina tekstą iš šio lauko");
	scrMessage = new JScrollPane(taMessage);
	pFields.add(scrMessage, gbc);

	gbc.gridx = 5;
	gbc.gridwidth = 1;
	gbc.weightx = 0;
	createPanelEditButtons();
	pFields.add(pEditButtons, gbc);

    }

    @Override
    protected void createPanelEditButtons() {
	pEditButtons = new JPanel(new GridLayout(2, 1, 2, 5));
	btChange = new JMyButton("Išsaugoti", fontsize);
	btChange.setActionCommand("update");
	btChange.addActionListener(this);
	bAdd = new JMyButton("Naujas", fontsize);
	bAdd.setActionCommand("insert");
	bAdd.addActionListener(this);
	pEditButtons.add(btChange);
	pEditButtons.add(bAdd);
// 	if (user.equals("Antanas") || user.equals("ak")) {
//	    bDelete = new JMyButton("Pašalinti");
//	    bDelete.setActionCommand("delete");
//	    bDelete.addActionListener(this);
//	    pEditButtons.add(bDelete);
//	}
    }

    @Override
    protected void createPanelFilterButtons() {
	pnFIlterButtons = new JPanel();
	btAll = new JMyButton("Naujausieji", fontsize);
	btAll.setActionCommand("all");
        btAll.setToolTipText("Limit 100");
        btAll.setToolTipText(LIMIT + " paskutiniųjų");
	btAll.addActionListener(this);
	pnFIlterButtons.add(btAll);
	btFilter = new JMyButton("Filtruoti", fontsize);
	btFilter.setActionCommand("filter");
	btFilter.addActionListener(this);
	pnFIlterButtons.add(btFilter);
    }

 
    private void getTPtypes() {
	try {
	    tptypes = connection.getTPtypes();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }

     
// SELECT tp.ID, tp.Data, s.Pavadinimas, tpr.Pavadinimas, tp.Pastaba FROM TP tp LEFT JOIN Sistemos s ON tp.Sistema = s.ID LEFT JOIN TPrusys tpr ON tp.TP = tpr.ID
    @Override
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(SELECT);
	if (chDate.isSelected() || chSystem.isSelected() || chTPtype.isSelected() || chMessage.isSelected()) {
	    sb.append(" WHERE ");
            if (chDate.isSelected()) {
		sb.append(" tp.Data LIKE ?");
            }
            if (chSystem.isSelected()) {
                appendAND(sb);
		sb.append(" tp.Sistema = ?");
            }
	    if (chTPtype.isSelected()) {
                appendAND(sb);
		sb.append(" tp.TP = ?");
            }
	    if (chMessage.isSelected()) {
                appendAND(sb);
		sb.append(" tp.Pastaba LIKE ?");
            }
	}
	sb.append(" ORDER BY tp.Data DESC LIMIT 100");
//	System.out.println(sb.toString());
	return sb;
    }
    
    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n;
	n = 0;
	i = sb.indexOf(" tp.Data LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, "%" + tfDate.getText() + "%");
	}
	i = sb.indexOf(" tp.Sistema = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));	    
	}
	i = sb.indexOf(" tp.TP = ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setInt(n, Integer.parseInt(tptypes[0][cbTPtype.getSelectedIndex()]));
	}
	i = sb.indexOf(" tp.Pastaba LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, "%" + taMessage.getText() + "%");
	}

    }

     
    protected void filter(String query) {
        Object[] row;
	int i, n, colcount;
	tableModel.setRowCount(0);
	ResultSet resultset;
        n = 0;
	try {
	    resultset = connection.executeQuery(query);
	    colcount = tableModel.getColumnCount();
	    row = new Object[colcount];
	    while (resultset.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    row[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(row);
                n++;
	    }
	    resultset.close();
            tableModel.addRow((Object[]) summary_row(n, colcount, true));
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}

    }

//UPDATE TP SET Data = ?, Sistema = ?, TP = ?, Pastaba = ? WHERE ID = ?
    @Override
    protected void update() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
            try {
                if (preparedUpdate == null) {
                    preparedUpdate = connection.prepareStatement(PREPARE_UPDATE);
                }
                preparedUpdate.setString(1, tfDate.getText());
                preparedUpdate.setInt(2, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));
                preparedUpdate.setInt(3, Integer.parseInt(tptypes[0][cbTPtype.getSelectedIndex()]));
                preparedUpdate.setString(4, taMessage.getText());
                preparedUpdate.setInt(5, (int) table.getValueAt(row, 0));
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

// INSERT INTO TP (Data, Sistema, TP, Pastaba) VALUES (?, ?, ?, ?)
    private void insert() {
        try {
            if (preparedInsert == null) {
                preparedInsert = connection.prepareStatement(insert);
            }
            preparedInsert.setString(1, tfDate.getText());
//             System.out.println(systems[0][1]);
//           System.out.println(cbIrenginys.getSelectedIndex());
            preparedInsert.setInt(2, Integer.parseInt(systems[0][cbIrenginys.getSelectedIndex()]));
            preparedInsert.setInt(3, Integer.parseInt(tptypes[0][cbTPtype.getSelectedIndex()]));
            preparedInsert.setString(4, taMessage.getText());
             if (preparedInsert.executeUpdate() == 1) {
                filter();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getErrorCode(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "all":
		filter(select);
		break;
	    case "update":
		update();
		break;	
	    case "filter":
		filter();
		break;
	    case "insert":
		insert();
		break;	
	}
	
    }

    @Override
    public void mouseClicked(MouseEvent me) {
	if (me.getComponent().equals(tfDate) & me.getClickCount() == 2) {
	    setCurrTime();
	}
	if (me.getComponent().equals(table)) {
            the_row = table.getSelectedRow();
            if (the_row >= 0) {
                tfDate.setText(table.getValueAt(the_row, 1).toString());
                setComboBoxItem(cbIrenginys, systems[1], (String) table.getValueAt(the_row, 2));
                setComboBoxItem(cbTPtype, tptypes[1], (String) table.getValueAt(the_row, 3));
                taMessage.setText((String) table.getValueAt(the_row, 4));
                tfDate.setToolTipText(date.getWeekday(tfDate.getText())); //tfDate.repaint();
            }
	} else {
            if (me.getComponent().equals(taMessage) & me.getButton() == 3) {
                openFile(FOLDER, taMessage.getSelectedText());
            }
        }
        
    }


    
    
}

//	tableModel = new DefaultTableModel(new Object[]{"", "Datum", "<html>Fett<br>%</hmtl>", "<html>Muskeln<br>%</html>", "<html>Wasser<br>%</html>", "<html>Knochen<br>kg</html>", "<html>Masse<br>kg</html>", "<html>Energie0<br>kcal</html>", "<html>Energie1<br>kcal</html>", "<html>Bauch<br>cm</html>", "<html>Oberarm<br>cm</html>", "<html>Unterarm<br>cm</html>", "<html>Ober-<br>schenkel<br>cm</html>", "<html>Unter-<br>schenkel<br>cm</html>", "<html>Brust<br>cm</html>", "<html>Fettmasse<br>kg</html>", "<html>Muskel-<br>masse<br>kg</html>"}, 0);