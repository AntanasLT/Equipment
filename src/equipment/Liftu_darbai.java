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
public class Liftu_darbai extends TP {

    private static final String PREPARE_SELECT_ALL = "SELECT d.ID, d.Data, d.RegNr, l.Vieta, d.Darbas FROM Liftu_darbai d INNER JOIN Liftai l ON d.RegNr = l.RegNr ORDER BY d.Data DESC LIMIT 50";
    private static final String PREPARE_INSERT = "INSERT INTO Liftu_darbai (Data, RegNr, Darbas) VALUES (?, ?, ?)";
    private static final String PREPARE_UPDATE = "UPDATE Liftu_darbai SET Data = ?, Darbas = ?, RegNr = ? WHERE ID = ?";
    private static final String SELECT = "SELECT d.ID, d.Data, d.RegNr, l.Vieta, d.Darbas FROM Liftu_darbai d INNER JOIN Liftai l ON d.RegNr = l.RegNr ";
    private static final String FOLDER = "Liftai";


    private JCheckBox chElevator;
    
    String[][] elevators;
    String[]elev_nr, elev_nr_and_locations;

    protected Liftu_darbai(ConnectionEquipment connection, int size) {
	super(connection, size);
    }

    @Override
    protected void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
        date = new Datum();
        table_columns = new String[]{"ID","Data", "Reg. Nr.", "Vieta", "Darbas"};
        table_column_width = new int[]{4*fontsize, 7*fontsize, 7*fontsize, 16*fontsize, 66*fontsize};
	if (connection != null) {
	    setLayout(new BorderLayout());
            getElev_nr_and_locations();
            setConstants();
	    createTable();
	    createPanelInput();
	    add(pInput, BorderLayout.NORTH);
	    add(scrTable, BorderLayout.CENTER);
	    add(lMessage, BorderLayout.SOUTH);
	    setVisible(true);
//	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    protected void setConstants(){
	select = PREPARE_SELECT_ALL;
        insert = PREPARE_INSERT ;
        tableTooltip = "";
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
	tfDate.addMouseListener(this);
        tfDate.setToolTipText(date.getWeekday());
	pFields.add(tfDate, gbc);

	gbc.gridx = 2;
//	gbc.weightx = 0;
	lSystem = new JLabelRechts("Įrenginys", fontsize);
	pFields.add(lSystem, gbc);

	gbc.gridx = 3;
//	gbc.weightx = 0.5;
	cbIrenginys = new JMyComboBox(elev_nr_and_locations, fontsize);
	pFields.add(cbIrenginys, gbc);
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
	chElevator = new JCheckBox();
	pFields.add(chElevator, gbc);

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
	taMessage.setLineWrap(true);
	taMessage.setWrapStyleWord(true);
        taMessage.addMouseListener(this);
	scrMessage = new JScrollPane(taMessage);
	pFields.add(scrMessage, gbc);

	gbc.gridx = 5;
	gbc.gridwidth = 1;
	gbc.weightx = 0;
	createPanelEditButtons();
	pFields.add(pEditButtons, gbc);

    }

//    @Override
//    protected void createPanelMessages() {
//	pMessage = new JPanel(new GridLayout(2, 1));
//	lMessage = new JLabelRechts("Darbas", fontsize);
//	chMessage = new JCheckBox();
//	pMessage.add(lMessage);
//	pMessage.add(chMessage);
//    }


    @Override
    protected void filter() {
	Object[] row;
	int i, colcount;
	tableModel.setRowCount(0);
	StringBuilder sb;
	ResultSet resultset;
	try {
	    sb = prepareFilter();
	    preparedFilter = connection.prepareStatement(sb.toString());
	    preparedFilter_setPrepared(sb);
	    resultset = preparedFilter.executeQuery();
	    colcount = tableModel.getColumnCount();
	    row = new Object[colcount];
	    while (resultset.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    row[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(row);
	    }
	    resultset.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

// SELECT d.ID, d.RegNr, l.Vieta, d.Data, d.Darbas FROM Liftu_darbai d INNER JOIN Liftai l ON d.RegNr = l.RegNr 
    @Override
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(SELECT);
	if (chDate.isSelected() || chElevator.isSelected() || chMessage.isSelected()) {
	    sb.append(" WHERE");
            if (chDate.isSelected()) {
		sb.append(" d.Data LIKE ?");
            }
            if (chElevator.isSelected()) {
                appendAND(sb);
		sb.append(" d.RegNr LIKE ?");
            }
	    if (chMessage.isSelected()) {
                appendAND(sb);
		sb.append(" d.Darbas LIKE ?");
            }
	}
	sb.append(" ORDER BY d.Data DESC");
//	System.out.println(sb.toString());
	return sb;
    }
    
    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int i, n;
	n = 0;
	i = sb.indexOf(" d.Data LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, tfDate.getText());
	}
	i = sb.indexOf(" d.RegNr LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, elevators[cbIrenginys.getSelectedIndex()][0]);
	}
	i = sb.indexOf(" d.Darbas LIKE ?");
	if (i >= 0) {
	    n++;
	    preparedFilter.setString(n, taMessage.getText());
	}

    }
    
    private void getElev_nr_and_locations() {
        int i;
        i = 0;
        elev_nr_and_locations = null;
        elev_nr = null;
        try {
            elevators = connection.getElevators();
            elev_nr_and_locations = new String[elevators.length];
            elev_nr = new String[elevators.length];
            for (String[] elevator : elevators) {
                elev_nr[i] = elevator[0];
                elev_nr_and_locations[i] = elevator[1] + " " + elevator[0];
                i++;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        }
    }

     
    @Override
    protected void filter(String query) {
        Object[] row;
	int i, colcount;
	tableModel.setRowCount(0);
	ResultSet resultset;
	try {
	    resultset = connection.executeQuery(query);
	    colcount = tableModel.getColumnCount();
	    row = new Object[colcount];
	    while (resultset.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    row[i] = resultset.getObject(i + 1);
		}
		tableModel.addRow(row);
	    }
	    resultset.close();
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}

    }

// UPDATE Liftu_darbai SET Data = ?, Darbas = ?, RegNr = ? WHERE ID = ?
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
                preparedUpdate.setString(2, taMessage.getText());
                preparedUpdate.setString(3, elevators[cbIrenginys.getSelectedIndex()][0]);
                preparedUpdate.setString(4, String.valueOf(table.getValueAt(row, 0)));
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

// INSERT INTO Liftu_darbai (Data, RegNr, Darbas) VALUES (?, ?, ?)
    protected void insert() {
        try {
            if (preparedInsert == null) {
                preparedInsert = connection.prepareStatement(insert);
            }
            preparedInsert.setString(1, tfDate.getText());
            preparedInsert.setString(2, elevators[cbIrenginys.getSelectedIndex()][0]);
            preparedInsert.setString(3, taMessage.getText());
//            System.out.println(elevators[cbIrenginys.getSelectedIndex()][0]);
             if (preparedInsert.executeUpdate() == 1) {
                filter();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
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
        if (me.getComponent().equals(taMessage) & me.getButton() == 3) {
            openFile(FOLDER, taMessage.getSelectedText());
        }
	if (me.getComponent().equals(table)) {
            the_row = table.getSelectedRow();
            if (the_row >= 0) {
                tfDate.setText(table.getValueAt(the_row, 1).toString());
                taMessage.setText((String) table.getValueAt(the_row, 4));
                setComboBoxItem(cbIrenginys, elev_nr, (String) (table.getValueAt(the_row, 2)));
            }
	}
    }



    
    
}
