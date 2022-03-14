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
import java.sql.PreparedStatement;
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
public class Sutartys extends TP {

    private static final String SELECT = "SELECT s.VIPISNR, s.RegNr, s.RegData, s.Iki, s.Pavadinimas, k.Pavadinimas, s.Verte, s.Prekes, s.Paslaugos, s.Pastabos, s.Renge FROM Sutartys s INNER JOIN Kontrahentai k ON s.Kontrahentas = k.ID";
    private static final String INSERT = "INSERT INTO Sutartys (VIPISNR, RegNr, RegData, Iki, Pavadinimas, Kontrahentas, Verte, Prekes, Paslaugos, Pastabos, Renge) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Sutartys SET RegNr = ?, RegData = ?, Iki = ?, Pavadinimas = ?, Kontrahentas = ?, Verte = ?, Prekes = ?, Paslaugos = ?, Pastabos = ?, Renge = ? WHERE VIPISNR = ?";
    private static final String TABLE_TOOLTIP = "";

    JCheckBox chKontrah, chVIPIS, chDVS, chDataIki, chRenge;
    JMyCheckBox chPavad, chAprasymas;
    JMyComboBox cbKontrah;
    JMyTextArea taPavad;
    JMyTextField tfVIPIS, tfDVS, tfDataIki, tfKontrah, tfVerte, tfPrekem, tfPaslaugom, tfRenge;
    JScrollPane scrPavad;
    
    
    String[][] kontrahentai;
    
//    String[]elev_nr, elev_nr_and_locations;

    protected Sutartys(ConnectionEquipment connection, int size) {
	super(connection, size);
    }

    @Override
    protected void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
        date = new Datum();
        table_columns = new String[]{"VIPIS Nr.", "DVS Nr.", "Reg. data", "Iki", "Pavadinimas", "Kontrahentas", "Vertė", "Prekėm", "Paslaugom", "Aprašymas", "Rengė"};
        table_column_width = new int[]{4*fontsize, 5*fontsize, 3*fontsize, 3*fontsize, 30*fontsize, 8*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 10*fontsize, 6*fontsize };
	if (connection != null) {
	    setLayout(new BorderLayout());
            getKontrahentai();
            setConstants();
	    createTable();
	    createPanelInput();
	    add(pInput, BorderLayout.NORTH);
	    add(scrTable, BorderLayout.CENTER);
	    add(lMessage, BorderLayout.SOUTH);
            setConstants();
	    setVisible(true);
//	    filter();
	} else {
	    JOptionPane.showMessageDialog(this, "Neprisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    protected void setConstants(){
	select = SELECT + " ORDER BY s.RegData, k.Pavadinimas, s.Pavadinimas";
        insert = INSERT ;
        tableTooltip = TABLE_TOOLTIP;
    }

    @Override
    protected void createPanelFields() {
	pFields = new JPanel(new GridBagLayout());
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.insets = new Insets(0, 0, 5, 5);

// Μια πρώτη σειρά
	gbc.gridy = 0;
        
	gbc.gridx = 0;
        gbc.weightx = 0.2;
        pFields.add(new JLabelRechts("VIPIS Nr.:", fontsize), gbc);
	gbc.gridx = 1;
        tfVIPIS = new JMyTextField(8, fontsize);
        pFields.add(tfVIPIS, gbc);
        
	gbc.gridx = 2;
        pFields.add(new JLabelRechts("DVS Nr.:", fontsize), gbc);
	gbc.gridx = 3;
        tfDVS = new JMyTextField(8, fontsize);
        pFields.add(tfDVS, gbc);
        
	gbc.gridx = 4;
	pFields.add(new JLabelRechts("Reg. data:", fontsize), gbc);
	gbc.gridx = 5;
//	tfDate = new JMyTextField(date.getToday(), 10, fontsize);
        tfDate = new JMyTextField(8, fontsize);
        pFields.add(tfDate, gbc);
        
	gbc.gridx = 6;
	pFields.add(new JLabelRechts("Galioja iki:", fontsize), gbc);
	gbc.gridx = 7;
        tfDataIki = new JMyTextField(8, fontsize);
	tfDataIki.addMouseListener(this);
        pFields.add(tfDataIki, gbc);

//	gbc.gridx = 8;
//	pFields.add(new JLabelRechts("Pavadinimas:", fontsize), gbc);
//	gbc.gridx = 9;
//	tfPavad = new JMyTextField(30, fontsize);
//	pFields.add(tfPavad, gbc);

// Η δεύτερη σειρά
	gbc.gridy = 1;
	gbc.gridx = 0;
	lFilters = new JLabelRechts("Filtrai:", fontsize);
	lFilters.addMouseListener(this);
	pFields.add(lFilters, gbc);

	gbc.gridx = 1;
        chVIPIS = new JCheckBox();
	pFields.add(chVIPIS, gbc);
        
	gbc.gridx = 3;
        chDVS = new JCheckBox();
	pFields.add(chDVS, gbc);
        
	gbc.gridx = 5;
	chDate = new JCheckBox();
	pFields.add(chDate, gbc);

	gbc.gridx = 7;
	chDataIki = new JCheckBox();
	pFields.add(chDataIki, gbc);
        

// Η τρίτη σειρά
	gbc.gridy = 2;
	gbc.gridx = 0;
	pFields.add(new JLabelRechts("Kontrahentas:", fontsize), gbc);
	gbc.gridx = 1;
	cbKontrah = new JMyComboBox(kontrahentai[1], fontsize);
	pFields.add(cbKontrah, gbc);

	gbc.gridx = 2;
        pFields.add(new JLabelRechts("Vertė:", fontsize), gbc);
	gbc.gridx = 3;
        tfVerte = new JMyTextField(6, fontsize);
        pFields.add(tfVerte, gbc);
        
	gbc.gridx = 4;
	pFields.add(new JLabelRechts("Prekėms:", fontsize), gbc);
	gbc.gridx = 5;
        tfPrekem = new JMyTextField(6, fontsize);
        pFields.add(tfPrekem, gbc);
        
	gbc.gridx = 6;
	pFields.add(new JLabelRechts("Paslaugoms:", fontsize), gbc);
	gbc.gridx = 7;
        tfPaslaugom = new JMyTextField(6, fontsize);
        pFields.add(tfPaslaugom, gbc);

	gbc.gridx = 8;
	pFields.add(new JLabelRechts("Rengė:", fontsize), gbc);
	gbc.gridx = 9;
	tfRenge = new JMyTextField(20, fontsize);
	pFields.add(tfRenge, gbc);
// Η τέταρτη σειρά
	gbc.gridy = 3;
	gbc.gridx = 0;
	lFilters = new JLabelRechts("Filtrai:", fontsize);
	lFilters.addMouseListener(this);
	pFields.add(lFilters, gbc);

	gbc.gridx = 1;
        chKontrah = new JCheckBox();
	pFields.add(chKontrah, gbc);
        
        gbc.gridx = 9;
        chRenge = new JCheckBox();
        pFields.add(chRenge, gbc);

// Η πέμπτη σειρά
	gbc.gridy = 4;
	gbc.gridx = 0;
	gbc.gridwidth = 1;
//	gbc.weightx = 0;
	createPanelEditButtons();
	pFields.add(pEditButtons, gbc);

	gbc.gridx = 1;
	gbc.gridwidth = 6;
	taMessage = new JMyTextArea_monospaced(3, 30, fontsize);
        taMessage.setToolTipText("Με το δεξιό πλήκτρο του ποντικιού ανοίξτε το επιλεγμένο αρχείο");
	taMessage.setLineWrap(true);
	taMessage.setWrapStyleWord(true);
        taMessage.addMouseListener(this);
	scrMessage = new JScrollPane(taMessage);
	pFields.add(scrMessage, gbc);

//	gbc.gridx = 6;
//	gbc.gridwidth = 1;
//	createPanelMessages();
//	pFields.add(pMessage, gbc);
        
	gbc.gridx = 7;
	gbc.gridwidth = 3;
        taPavad = new JMyTextArea(3, 30, fontsize);
        taPavad.setLineWrap(true);
        taPavad.setWrapStyleWord(true);
        scrPavad = new JScrollPane(taPavad);
        pFields.add(scrPavad, gbc);
// Η έκτη σειρά
        gbc.anchor = GridBagConstraints.CENTER;
	gbc.gridy = 5;
	gbc.gridx = 1;
	gbc.gridwidth = 6;
        chAprasymas = new JMyCheckBox("Aprašymas", false, fontsize);
        pFields.add(chAprasymas, gbc);
        
	gbc.gridx = 7;
	gbc.gridwidth = 3;
        chPavad = new JMyCheckBox("Pavadinimas", false, fontsize);
        pFields.add(chPavad, gbc);
        
    }

    private void getKontrahentai() {
        try {        
            kontrahentai = connection.getList("Kontrahentai", "ID", "Pavadinimas", "Pavadinimas");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        }
    }


// SELECT s.VIPISNR, s.RegNr, s.RegData, s.Iki, s.Pavadinimas, k.Pavadiniams, s.Verte, s.Prekes, s.Paslaugos, s.Pastabos, s.Renge FROM Sutartys s INNER JOIN Kontrahentai k ON s.Kotrahentas = k.Pavadinimas
    @Override
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(SELECT);
	if (chVIPIS.isSelected() || chDVS.isSelected() || chDataIki.isSelected() || chDate.isSelected() || chPavad.isSelected() || chKontrah.isSelected() || chAprasymas.isSelected() || chRenge.isSelected()) {
	    sb.append(" WHERE");
            if (chVIPIS.isSelected()) {
		sb.append(" s.VIPISNR LIKE ?");
            }
            if (chDVS.isSelected()) {
                appendAND(sb);
		sb.append(" s.RegNr LIKE ?");
            }
            if (chDate.isSelected()) {
                appendAND(sb);
		sb.append(" s.RegData LIKE ?");
            }
            if (chDataIki.isSelected()) {
                appendAND(sb);
		sb.append(" s.Iki LIKE ?");
            }
            if (chPavad.isSelected()) {
                appendAND(sb);
		sb.append(" s.Pavadinimas LIKE ?");
            }
            if (chKontrah.isSelected()) {
                appendAND(sb);
		sb.append(" s.Kontrahentas = ?");
            }
            if (chRenge.isSelected()) {
                appendAND(sb);
		sb.append(" s.Renge LIKE ?");
            }
	    if (chAprasymas.isSelected()) {
                appendAND(sb);
		sb.append(" s.Pastabos LIKE ?");
            }
	}
        sb.append(" ORDER BY s.RegData, k.Pavadinimas, s.Pavadinimas");
//	System.out.println(sb.toString());
	return sb;
    }
    
// Integer.valueOf(tptypes[0][cbTPtype.getSelectedIndex()])  cbKontrah.getSelectedIndex()
    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int n;
	n = 0;
        if (chVIPIS.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfVIPIS.getText() + "%");
        }
        if (chDVS.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfDVS.getText() + "%");
        }
        if (chDate.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfDate.getText() + "%");
        }
        if (chDataIki.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfDataIki.getText() + "%");
        }
        if (chPavad.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + taPavad.getText() + "%");
        }
        if (chKontrah.isSelected()) {
            n++;
            preparedFilter.setInt(n, Integer.valueOf(kontrahentai[0][cbKontrah.getSelectedIndex()]));
        }
        if (chRenge.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfRenge.getText() + "%");
        }
        if (chAprasymas.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + taMessage.getText() + "%");
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

// UPDATE Sutartys SET RegNr = ?, RegData = ?, Iki = ?, Pavadinimas = ?, Kontrahentas = ?, Verte = ?, Prekes = ?, Paslaugos = ?, Pastabos = ?, Renge = ? WHERE VIPISNR = ?
    @Override
    protected void update() {
	int row;
	row = table.getSelectedRow();
	if (row >= 0) {
            try {
                if (preparedUpdate == null) {
                    preparedUpdate = connection.prepareStatement(UPDATE);
                }
                setPrepared(preparedUpdate, false);
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
    
    protected void setPrepared(PreparedStatement ps, Boolean insert) {
        int i;
        i = insert? 1 : 0;
        try {
            ps.setString(11, tfVIPIS.getText());
            ps.setString(i + 1, tfDVS.getText());
            ps.setString(i + 2, tfDate.getText());
            ps.setString(i + 3, tfDataIki.getText());
            ps.setString(i + 4, taPavad.getText());
            ps.setInt(i+ 5, Integer.parseInt(kontrahentai[0][cbKontrah.getSelectedIndex()]));
            ps.setString(i + 6, tfVerte.getText());
            ps.setString(i + 7, tfPrekem.getText());
            ps.setString(i + 8, tfPaslaugom.getText());
            ps.setString(i + 9, taMessage.getText());
            ps.setString(i + 10, tfRenge.getText());
            if (insert) {
                ps.setString(1, tfVIPIS.getText());
            } else {
                ps.setString(11, tfVIPIS.getText());
            }
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
    }

// INSERT INTO Sutartys (VIPISNR, RegNr, RegData, Iki, Pavadinimas, Kontrahentas, Verte, Prekes, Paslaugos, Pastabos, Renge) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    protected void insert() {
        try {
            if (preparedInsert == null) {
                preparedInsert = connection.prepareStatement(insert);
            }
            setPrepared(preparedInsert, true);
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
	if (me.getComponent().equals(table)) {
            row = table.getSelectedRow();
            if (row >= 0) {
                tfVIPIS.setText((String) table.getValueAt(row, 0));
                tfDVS.setText((String) table.getValueAt(row, 1));
                tfDate.setText(table.getValueAt(row, 2).toString());
                tfDataIki.setText(table.getValueAt(row, 3).toString());
                taPavad.setText((String) table.getValueAt(row, 4));
                setComboBoxItem(cbKontrah, kontrahentai[1], (String) (table.getValueAt(row, 5)));
                tfVerte.setText(table.getValueAt(row, 6).toString());
                tfPrekem.setText(table.getValueAt(row, 7).toString());
                tfPaslaugom.setText(table.getValueAt(row, 8).toString());
                taMessage.setText(table.getValueAt(row, 9).toString());
                tfRenge.setText((String) table.getValueAt(row, 10));
            }
	}
	if (me.getComponent().equals(taMessage) & me.getButton() == 3){
            openFile("Sutartys", taMessage.getSelectedText());
        }
    }



    
    
}
