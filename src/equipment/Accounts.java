/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Accounts extends JPanel implements ActionListener, MouseListener {

    static final String FILES_FOLDER = "../files/";
    static final int MAX_LINE_COUNT = 100, MAX_EQUIPMENT = 100;
    static final String DIE_ABFRAGE_KOERPERDATEN = "SELECT KMuskelPr, KMasse, KEnergie1 FROM Koerperdaten ORDER BY KDatum DESC LIMIT 1";

    
    ConnectionEquipment myConnection;

    String[][] types;
    String[][] equipment;
    boolean is_fromDB;
    int[] currentID;
    int typeCount, equipmentCount, fontsize;

    JScrollPane scrollPane;
    JPanel panelInput, panelButtons, panelFields;
    DefaultTableModel tableModel;
    JTable table;
    
    JLabelRechts labelMessage;
    JLabelRechts labelDate, labelAccount_date, labelAccount_getted, labelEquipment, labelOutlays, labelNotes, labelSystem;
    JTextField textFieldDate, textFieldOutlays, textFieldAccount, textField_getted;
    JTextArea textAreaNotes;
    JMyComboBox comboBoxTypes, comboBoxEquipment, comboBoxSystem;

//    JLabelRechts dasLabeldieMasse;

    JMyButton buttonDelete, buttonAdd, buttonChange, buttonFilter, buttonApply;

//    JCheckBox dasKontrollkaestchen_ausfuehrlich;

    GridBagConstraints gbc;
    

    public Accounts(ConnectionEquipment the_connection, int size) {
        fontsize = size;
	myConnection = the_connection;
	panelInput = new JPanel(new BorderLayout());
	panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelFields = new JPanel(new GridBagLayout());

	labelMessage = new JLabelRechts(fontsize);
	labelMessage.addMouseListener(this);
	
	labelAccount_date = new JLabelRechts("Ο Λογαριασμός:", fontsize);
	textFieldAccount = new JTextField(8);

	labelDate = new JLabelRechts("Η ημερομηνία:", fontsize);
	textFieldDate = new JTextField();
	
	labelSystem = new JLabelRechts("Ένα σύστημα:", fontsize);
	textFieldDate = new JTextField(8);
	
	labelAccount_getted = new JLabelRechts("Πήρα:", fontsize);
	textField_getted = new JTextField(8);
	
	labelOutlays = new JLabelRechts("Σύνολο:", fontsize);
	textFieldOutlays = new JTextField();
	
	labelNotes = new JLabelRechts("Η σημείοση:", fontsize);
	textAreaNotes = new JTextArea(2, 60);

	labelEquipment = new JLabelRechts("Το μηχάνημα:", fontsize);

        comboBoxEquipment = new JMyComboBox(size);
        
        comboBoxSystem = new JMyComboBox(size);
        
        comboBoxTypes = new JMyComboBox(size);
                
	scrollPane = new JScrollPane(table);
	tableModel = new DefaultTableModel(new Object[]{"ID", "Η υμερομηνία", "Το σύστημα", "Ο τύπος", "Το μηχάνημα", "Τα έχοδα", "Η σημείοση"}, 0);
	table = new JTable(tableModel);
        
	init();
    }

    private void init() {
	is_fromDB = false;
	setLayout(new BorderLayout());
	createPanelInput();
	add(panelInput, BorderLayout.NORTH);
	add(scrollPane, BorderLayout.CENTER);
	add(labelMessage, BorderLayout.SOUTH);
	setTableColumnWidth();
	table.getSelectionModel().addListSelectionListener(new ListSelectionListenerImpl());
	setVisible(true);
    }
    
    private void createPanelInput(){
        createPanelFields();
        panelInput.add(panelFields, BorderLayout.NORTH);
        createPanelButtons();
        panelInput.add(panelButtons, BorderLayout.SOUTH);
    }

    private void createPanelFields() {
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
//		panelInput.setBackground(Color.lightGray);
	gbc.insets = new Insets(0, 0, 5, 5);

//First row
	gbc.weightx = 0;
	gbc.gridx = 0;
	gbc.gridy = 0;
//	gbc.gridwidth = 3;
//	gbc.fill = GridBagConstraints.EAST;
	panelFields.add(labelDate, gbc);
	
	gbc.gridx = 1;
	gbc.weightx = 0.5;
	panelFields.add(textFieldDate, gbc);
	
	gbc.gridx = 2;
	gbc.weightx = 0;
	panelFields.add(labelSystem, gbc);
	
	gbc.gridx = 3;
	gbc.weightx = 0.5;
	panelFields.add(comboBoxSystem, gbc);	

	gbc.gridx = 4;
	gbc.weightx = 0;
	panelFields.add(labelAccount_date, gbc);
	
	gbc.gridx = 5;
	gbc.weightx = 0.5;
	panelFields.add(comboBoxTypes, gbc);	
	
	gbc.gridx = 6;
	gbc.weightx = 0;
	panelFields.add(labelEquipment, gbc);
	
	gbc.gridx = 7;
	gbc.weightx = 0.5;
	panelFields.add(comboBoxEquipment, gbc);
	
// Second row
	gbc.gridy = 1;
	gbc.gridx = 0;
	panelFields.add(labelOutlays, gbc);
	
	gbc.gridx = 1;
	panelFields.add(textFieldOutlays, gbc);
	
	
        
// Η τρίτη σειρά
	gbc.gridy = 2;
	gbc.gridx = 0;
	panelFields.add(labelNotes, gbc);
	gbc.gridx = 1;
	gbc.gridwidth = 8;
	panelFields.add(textAreaNotes, gbc);


    }

    private void createPanelButtons() {

	buttonAdd = new JMyButton("Προσθήκω", fontsize);
	buttonAdd.setMnemonic(KeyEvent.VK_P);
	buttonAdd.setActionCommand("Add");
	buttonAdd.addActionListener(this);
	panelButtons.add(buttonAdd);

	buttonDelete = new JMyButton("Αφαιρώ", fontsize);
	buttonDelete.setActionCommand("Delete");
	buttonDelete.addActionListener(this);
	panelButtons.add(buttonDelete);

	buttonChange = new JMyButton("Αλλάζω", fontsize);
	buttonChange.setActionCommand("Change");
	buttonChange.addActionListener(this);
	panelButtons.add(buttonChange);

	buttonFilter = new JMyButton("Φιλτράρω", fontsize);
	buttonFilter.setActionCommand("filtern");
	buttonFilter.setMnemonic(KeyEvent.VK_F);
	buttonFilter.addActionListener(this);
	panelButtons.add(buttonFilter);
	
	buttonApply = new JMyButton("Καταχωρώ", fontsize);
	buttonApply.setMnemonic(KeyEvent.VK_I);
	buttonApply.setActionCommand("Apply");
	buttonApply.addActionListener(this);
	panelButtons.add(buttonApply);

    }

    protected int bekommtID() {
	int x;
	x = table.getSelectedRow();
	return currentID[x];
    }

    protected String bekomme_ausgewaehltesDatum() {
	int x;
	x = table.getSelectedRow();
	if (x >= 0) {
	    return String.valueOf(table.getValueAt(x, 0));
	} else {
	    return "";
	}

    }

    protected String getDate() {
	return textFieldDate.getText();
    }

    private void setTableColumnWidth() {
	TableColumn dieSpalte;
	dieSpalte = null;
	for (int i = 0; i < table.getColumnCount(); i++) {
	    dieSpalte = table.getColumnModel().getColumn(i);
	    switch (i) {
//		case 0:
//		    dieSpalte.setPreferredWidth(10);
//		    break;
		case 0:
		    dieSpalte.setPreferredWidth(50);
		    break;		    
		case 6:
		    dieSpalte.setPreferredWidth(400);
		    break;
	    }
	}
    }

    private void bekommt_dieGerichte() throws SQLException{
	if (myConnection != null) {
	    types = myConnection.getEquipmentTypes();
	    typeCount = types[0].length;
        }
    }

    public void setConnection(ConnectionEquipment connection) {
	myConnection = connection;
    }

    public void disconnect() {
	myConnection = null;
    }

    public void renewTypes() throws SQLException {
	comboBoxTypes.removeAllItems();
	if (myConnection != null & comboBoxTypes.getItemCount() == 0) {
	    bekommt_dieGerichte();
	    for (int i = 0; i < types[0].length; i++) {
		comboBoxTypes.addItem(types[1][i]);
	    }
	}
    }

    private String getQuery() {
	StringBuilder query;
	query = new StringBuilder("SELECT VID, VdieZeit, GdieBenennung, VdieMenge, VdieEiweisse, VdieFette, VdieKohlenhydrate FROM dieVerzerrung, dasGericht WHERE VdasGericht=GID ");
//	if (!field_date.getText().isEmpty()) {
//	    query.append(" AND VdieZeit LIKE '").append(field_date.getText()).append("%'");
//	}
//	if (!dasFeldZeit.getText().isEmpty()) {
//	    query.append(" AND VdieZeit LIKE '% ").append(dasFeldZeit.getText()).append("%'");
//	}
//	if (!field_search.getText().isEmpty()) {
//	    query.append(" AND VdasGericht='").append(types[0][dieAuswahlGericht.getSelectedIndex()]).append("'");
//	}
//        query.append(" ORDER BY VdieZeit ");
	return query.toString();
    }
    
    protected String bekommt_dieEinfuegungsabfrage(){
	int i, n;
        StringBuilder dieAbfrage;
	n = table.getRowCount();
	dieAbfrage = new StringBuilder("INSERT INTO dieVerzerrung (VdieZeit, VdasGericht, VdieMenge, VdieEiweisse, VdieFette, VdieKohlenhydrate) VALUES(");
	try {
	    for (i = 0; i < n; i++) {
		dieAbfrage.append("'");
		dieAbfrage.append(table.getValueAt(i, 0));
		dieAbfrage.append("', '").append(types[0][getTypeID((String) table.getValueAt(i, 1))]);
		dieAbfrage.append("', '").append(table.getValueAt(i, 2));
		dieAbfrage.append("', '").append(table.getValueAt(i, 3));
		dieAbfrage.append("', '").append(table.getValueAt(i, 4));
		dieAbfrage.append("', '").append(table.getValueAt(i, 5));
		if (i < n - 1) {
		    dieAbfrage.append("'), (");
		} else {
		    dieAbfrage.append("')");
		}
	    }
	} catch (NumberFormatException ex) {
	    JOptionPane.showMessageDialog(this, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
	}
//        JOptionPane.showMessageDialog(this, query.toString(), "Info", JOptionPane.INFORMATION_MESSAGE);
	return dieAbfrage.toString();
    }
    
    
    private void setData() {
//        Object[] dieZeile;
//	int dieZeilenzahl;
//	float m, e, f, k, en;
//        dieZeilenzahl = 0;
//	dieEiweisse = 0;
//	dieMasse = 0;
//	dieFette = 0;
//	dieKohlenhydrate = 0;
//	dieEnergie = 0;
//	currentID = new int[MAX_LINE_COUNT];
//	tableModel.setRowCount(0);
//        if (myConnection != null){
//            try {
//		dieTage = myConnection.bekomme_die_anzahl_der_tage(field_date.getText(), "dieVerzerrung");
//                int i, dieSpaltenzahl;
//                ResultSet derResultatsatz;
//                derResultatsatz = myConnection.executeQuery(getQuery());
//		dieSpaltenzahl = tableModel.getColumnCount();
//                dieZeile = new Object[dieSpaltenzahl];
//                while( derResultatsatz.next() ){
//		    for (i = 0; i < 6; i++) {
//			dieZeile[i] = derResultatsatz.getObject(i + 2);
//		    }
//		    m = (Float) derResultatsatz.getObject(4);
//		    e = (Float) derResultatsatz.getObject(5);
//		    f = (Float) derResultatsatz.getObject(6);
//		    k = (Float) derResultatsatz.getObject(7);
//		    en = Math.round(4.1 * (e + k) + 9.3 * f);
//		    dieZeile[6] = String.valueOf(en);
//		    dieMasse = dieMasse + m;
//		    dieEiweisse = dieEiweisse + e;
//		    dieFette = dieFette + f;
//		    dieKohlenhydrate = dieKohlenhydrate + k;
//		    dieEnergie = dieEnergie + en;
//		    if (dieZeilenzahl < MAX_LINE_COUNT & dasKontrollkaestchen_ausfuehrlich.isSelected()) {
//			tableModel.addRow(dieZeile);
//			currentID[dieZeilenzahl] = (int) derResultatsatz.getObject(1);
//		    }
//		    dieZeilenzahl++;
//		}
//		derResultatsatz.close();
//		if (dieZeilenzahl > 0) {
//		    fuegt_denGesamtverbrauch_hinzu(dieZeile, dieZeilenzahl);
//		    fuegt_denTagesverbrauch_hinzu(dieZeile);
//		    fuegt_dieGesamtbilanz_hinzu(dieZeile);
//		    fuegt_dieTagesbilanz_hinzu(dieZeile);
//		    fuegt_dieBilanznormen_hinzu(dieZeile);
//		    fuegt_Bilanznormen_hinzu(dieZeile);
//		    if (dieZeilenzahl >= MAX_LINE_COUNT & dasKontrollkaestchen_ausfuehrlich.isSelected()) {
//			fuegt_Begrenzungshinweise_hinzu(dieSpaltenzahl);
//		    }
//		}
//		is_fromDB = true;
//	    } catch (SQLException | NullPointerException ex) {
//                showErrorMessage(ex.toString());
//            }
//
//        } else {
//            JOptionPane.showMessageDialog(this, "Es gibt keine Anbindung!", "Fehler!", JOptionPane.ERROR_MESSAGE);
//        }
    }
    
    private void fuegt_Begrenzungshinweise_hinzu(int dieSpaltenzahl) {
        Object[] dieZeile;
	dieZeile = new Object[dieSpaltenzahl];
	    dieZeile[0] = String.valueOf(MAX_LINE_COUNT);
	    dieZeile[1] = "Einträge wird dargestellt, andere wurden verbergt.";
	    dieZeile[2] = null;
	    dieZeile[3] = null;
	    dieZeile[4] = null;
	    dieZeile[5] = null;
	    dieZeile[6] = null;
	tableModel.addRow(dieZeile);
    }
    
    private String bekommt_denGruppennamen(String dieGruppenID) {
	int i;
	boolean gefunden;
	i = 0;
	gefunden = false;
	while (i < typeCount & !gefunden) {
	    if (types[0][i].equals(dieGruppenID)) {
		gefunden = true;
	    }
	    i++;
	}
	return types[1][i - 1];
    }
    
    private int getTypeID(String derGruppenname) {
        int i;
        boolean gefunden;
        i = 0;
        gefunden = false;
	while (i < typeCount & !gefunden) {
	    if (types[1][i].contains(derGruppenname)) {
                gefunden = true;
            }
            i++;
        }
	return i - 1; //dieGruppen[0][i - 1];
    }
    
    private String getDeleteQuery(int[] die_ausgewaehlteZeilen) {
        int i, n;
        StringBuilder dieAbfrage;
        n = die_ausgewaehlteZeilen.length;
	dieAbfrage = new StringBuilder("DELETE FROM dieVerzerrung WHERE VID = ");
        for (i = 0; i < n; i++){
	    dieAbfrage.append(currentID[die_ausgewaehlteZeilen[i]]);
            if (n-i > 1){
		dieAbfrage.append(" OR VID = ");
            }
        }
        return dieAbfrage.toString();
    }
    
    private String getUpdateQuery(int die_ausgewaehlteZeile) {
        StringBuilder dieAbfrage;
	dieAbfrage = new StringBuilder("UPDATE dieVerzerrung SET VdieZeit = '");
	dieAbfrage.append(table.getValueAt(die_ausgewaehlteZeile, 0)).append("',VdasGericht = '").append(types[0][getTypeID((String) table.getValueAt(die_ausgewaehlteZeile, 1))]).append("', VdieMenge = ").append(table.getValueAt(die_ausgewaehlteZeile, 2)).append(", VdieEiweisse = ").append(table.getValueAt(die_ausgewaehlteZeile, 3)).append(", VdieFette = ").append(table.getValueAt(die_ausgewaehlteZeile, 4)).append(", VdieKohlenhydrate = ").append(table.getValueAt(die_ausgewaehlteZeile, 5)).append(" WHERE VID = ").append(currentID[die_ausgewaehlteZeile]);
            //.append("' WHERE pid = ").append(table.getValueAt(die_ausgewaehlteZeile[i],0));
        return dieAbfrage.toString();
    }    
    
    private int updateDB(String dieAbfrage) {
        int n;
        n = 0;
//        try {
//            n = myConnection.setzt_dieDaten(query);
//        } catch (SQLException ex) {
//            showErrorMessage(ex.toString());
//        }
        return n;
    }
	
    private void insertRow() {
	Object[] dieZeile;
	String dieZeit;
//	dieZeit = dasFeldZeit.getText();
//	if (!dieZeit.contains(":") & dieZeit.length() == 4) {
//	    dieZeit = dieZeit.substring(0,2).concat(":").concat(dieZeit.substring(2));
//	    dasFeldZeit.setText(dieZeit);
//	}
//	if (is_fromDB) {
//	    tableModel.setRowCount(0);
//	    is_fromDB = false;
//	}
//	dieZeile = new Object[tableModel.getColumnCount()];
//	if (field_date.getText().isEmpty() || dieZeit.isEmpty()) {
//	    showErrorMessage("Es ist kein Datum oder Zeit eingegeben!");
//	} else {
//	    dieZeile[0] = field_date.getText().concat(" ").concat(dieZeit);
//	    dieZeile[1] = dieAuswahlGericht.getSelectedItem();
//	    dieZeile[2] = dasFeldMenge.getText();
////	    dieZeile[3] = String.valueOf(Float.parseFloat(equipment[0]) * dieGerichtsmasse);
////	    dieZeile[4] = String.valueOf(Float.parseFloat(equipment[1]) * dieGerichtsmasse);
////	    dieZeile[5] = String.valueOf(Float.parseFloat(equipment[2]) * dieGerichtsmasse);
////	    dieZeile[6] = String.valueOf(Float.parseFloat(equipment[4]) * dieGerichtsmasse);
//	    tableModel.addRow(dieZeile);
//	    dasFeldEiweisse.setText(null);
//	    dasFeldFette.setText(null);
//	    dasFeldKohlenhydrate.setText(null);
//	    dasFeldKalorien.setText(null);
//	    field_search.setText(null);
//	    dasFeldMenge.setText(null);
//	    field_search.requestFocus();
//	}
    }
    
    private void insert_intoDB() {
//        if (myConnection == null){
//            JOptionPane.showMessageDialog(this, "Es gibt keine Anbindung!", "Fehler!", JOptionPane.ERROR_MESSAGE);
//        } else {
//            try {
//		labelMessage.setText(String.valueOf(myConnection.setzt_dieDaten(bekommt_dieEinfuegungsabfrage())).concat(" Datensätze wurden zur Datenbang zugefügt"));
////                JOptionPane.showMessageDialog(this, String.valueOf(myConnection.setzt_dieDaten(bekommt_dieEinfuegungsabfrage())).concat(" Datensätze wurden zur Datenbang zugefügt"), "Das Message", JOptionPane.INFORMATION_MESSAGE);
//		tableModel.setRowCount(0);
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(this, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
//            }
//        }
    }
    
    private void vor() {
//	field_date.setText(date.heutigesDatum());
//	if (dasFeldZeit.getText().isEmpty()) {
//	    dasFeldZeit.setText(date.gegenwaertigeUhrzeit());
//	}
//	dasFeldZeit.setText(date.gegenwaertigeUhrzeit(Integer.parseInt(dasFeldVor.getText())));
	
    }

    private void delete_fromDB() {
	if (currentID == null) {
	    deleteRow();
	} else {
	    if (updateDB(getDeleteQuery(table.getSelectedRows())) > 0) {
		deleteRow();
	    }
	}
    }
    
    private void deleteRow() {
	int i, n;
	int[] die_ausgewaehlteZeilen;
	die_ausgewaehlteZeilen = table.getSelectedRows();
	n = die_ausgewaehlteZeilen.length;
	for (i = n - 1; i >= 0; i--) {
	    tableModel.removeRow(die_ausgewaehlteZeilen[i]);
	}
	JOptionPane.showMessageDialog(this, String.valueOf(n));
	
    }
    
    /**
     * 
     * @param derDateiname Pfad zu die dieKoerperdaten.csv
     * @return [0] – die Masse <br> [1] – die Muskelmasse
     */
    private float[] fileChannel(String derDateiname) {
	float[] m;
	float x;
	int gelesen;
	Scanner sc;
	FileChannel fc;
	ByteBuffer bb;
	bb = ByteBuffer.allocate(64);
	m = new float[3];
//	Path dieDatei = FileSystems.getDefault().getPath(FILES_FOLDER, derDateiname);
//	try {
//	    fc = FileChannel.open(dieDatei, READ);
//	    fc.position(fc.size()-64);
//	    do {
//		gelesen = fc.read(bb);
//	    } while (gelesen != -1 && bb.hasRemaining());
//	    sc = new Scanner(new String(bb.array(), Charset.forName("UTF-8"))).useDelimiter("\t");
//	    gelesen = 0;
//	    while (sc.hasNext() && gelesen < 10) {
//		gelesen++;
//		if (sc.hasNextFloat()) {
//		    x = sc.nextFloat();
//		    if (gelesen == 8) {
//			m[2] = x;
//		    }
//		    if (gelesen == 6) {
//			m[0] = x;
//		    }
//		    if (gelesen == 9) {
//			m[1] = x;
//		    }
//		} else {
//		    sc.next();
//		}
//	    }
//	} catch (IOException ex) {
//	    showErrorMessage(ex.toString());
//	}
	return m;
    }
    
    private void showErrorMessage(String message) {
	JOptionPane.showMessageDialog(this, message, "Klaida!", JOptionPane.ERROR_MESSAGE);
    }

    private float calculate(String derText) {
	int i, anzahl;
	float x, y;
	String [] ss;
	ss = derText.split(" ");
	anzahl = ss.length;
	y = 0;
	try {
	    y = Float.parseFloat(ss[0]);
	    for (i = 0; i + 2 < anzahl; i += 2) {
		x = Float.parseFloat(ss[i + 2]);
		switch (ss[i + 1]) {
		    case "/":
			y /= x;
			break;
		    case "*":
			y *= x;
			break;
		    case "+":
			y += x;
			break;
		    case "-":
			y -= x;
			break;
		    default:
			showErrorMessage("Es wird nur + - / * berechnet.");
		}
	    }
            y = Math.round(y);
	} catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
	    showErrorMessage(ex.toString());
	}
	return y;
    }

    private float round_by(Float x, int n) {
        return (float) (Math.round(x*Math.pow(10, n))/Math.pow(10, n));
    }

    @Override
    public void mouseClicked(MouseEvent me) {
	String derText = labelMessage.getText();
	if (me.getComponent().equals(labelMessage) & derText.length() > 100) {
	    Message derBericht = new Message(derText, 30, 90);
            derBericht.setVisible(true);
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

    private class ListSelectionListenerImpl implements ListSelectionListener {

	public ListSelectionListenerImpl() {
	}

	@Override
	public void valueChanged(ListSelectionEvent lse) {
//	    int zeile;
//	    zeile = table.getSelectedRow();
//	    if (zeile >= 0) {
//		if (String.valueOf(tableModel.getValueAt(zeile, 0)).length() > 15) {
//		    field_date.setText(String.valueOf(tableModel.getValueAt(zeile, 0)).substring(0, 10));
//		    dasFeldZeit.setText(String.valueOf(tableModel.getValueAt(zeile, 0)).substring(11, 16));
//		    dieAuswahlGericht.setSelectedItem(String.valueOf(tableModel.getValueAt(zeile, 1)));
//		    dasFeldMenge.setText(String.valueOf(tableModel.getValueAt(zeile, 2)));
//		}
//	    }
	}
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "Types":
		
		break;
	}
	
    }


    
    
}
//    private void setzt_dieUeberschriften(){
//        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
//        table.getColumnModel().getColumn(1).setHeaderValue("<html>Der<br>Typ</html>");
//        table.getColumnModel().getColumn(2).setHeaderValue("<html>Die<br>Sammlung</html>");
//        table.getColumnModel().getColumn(3).setHeaderValue("<html>Das<br>Thema</html>");
//        table.getColumnModel().getColumn(6).setHeaderValue("<html>Das<br>Jahr</html>");
//        table.getColumnModel().getColumn(7).setHeaderValue("<html>Der<br>Autor</html>");
//        table.getColumnModel().getColumn(8).setHeaderValue("<html>Die<br>Sprache</html>");
//        table.getColumnModel().getColumn(9).setHeaderValue("<html>Der<br>Kommentar</html>");
//    }

/*
  CharBuffer c;
  Reader f = null;
  int a;
  this.setCursor( WAIT_CURSOR );  
  c = CharBuffer.allocate( 100000 );
  a = 0;
  try
  {
    f = new FileReader( eineDatei ); 
    a = f.read( c );
  }
  catch ( IOException e ) 
  { 
    jLabel.setText( e.toString() ); 
    jLabel.setForeground( Color.RED );
  } 
  jLabel.setText( String.valueOf( a ) );
  jLabel.setBackground( Color.getColor( "Label.background"));                                          
  jLabel.setForeground( Color.black );
  try { f.close(); } catch ( Exception e ) { } 
  c.rewind(); 
  jTextArea.setBackground( Color.getColor( "Label.background"));                                          
  jTextArea.setForeground( Color.black );
  if ( anschliessen ) {
        jTextArea.append( c.toString().substring( 0, a ) );
    }
  else {
        jTextArea.setText( c.toString().substring( 0, a ) );
    } 
  jTextArea.setCaretPosition(0);
  this.setCursor( DEFAULT_CURSOR );

*/