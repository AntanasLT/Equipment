/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import datum.Datum;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author a
 */
public class JSG extends TP {

    private static final String SELECT = "SELECT g.Nr, g.Pavadinimas, g.Data, g.Vamzdzio_pavad, g.Vamzdzio_nr, b.Pavadinimas, g.Introskopas, i.Pavadinimas, v.Pavadinimas, g.Yra, g.Failai, g.Gautas, g.Idetas, g.Isimtas, g.Sunaikintas FROM JSG g LEFT JOIN Gen_busenos b ON g.Busena = b.ID LEFT JOIN Introskopai i ON g.Introskopas = i.Nr LEFT JOIN JSGvietos v ON g.Vieta = v.ID";
    private static final String INSERT = "INSERT INTO JSG (Nr, Pavadinimas, Data, Vamzdzio_pavad, Vamzdzio_nr, Busena, Introskopas, Vieta, Yra, Failai, Gautas, Idetas, Isimtas, Sunaikintas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE JSG SET Pavadinimas = ?, Data = ?, Vamzdzio_pavad = ?, Vamzdzio_nr = ?, Busena = ?, Introskopas = ?, Vieta = ?, Yra = ?, Failai = ?, Gautas = ?, Idetas = ?, Isimtas = ?, Sunaikintas = ? WHERE Nr = ?";
    private static final String FOLDER_RSC = "RSC";
//    private static final String INSERT_INTO_RSCDARBAI = "INSERT INTO RSCdarbai (Data, Vamzdis, IntrNr, DVS, Pastaba) VALUES (?, ?, ?, ?, ?)";

    protected JMyComboBox cbVieta, cbBusena, cbInroskNr;
    private JMyTextField tfJSGNr, tfPavad, tfVamzdzioNr, tfVamzdzio_pavad, tfIntroskPavad,tfYra, tfGautas, tfIdetas, tfIsimas, tfSunaikintas, tfSearch;
    private JMyTextArea taFailai;
    private JMyButton btSearch;
    private JScrollPane scrPane;
    
    String[][] vietos, busenos, introskopai;
    
    String search;
    
    String filtras;

    protected JSG(ConnectionEquipment connection, int size) {
	super(connection, size);
    }

    @Override
    protected void setConstants(){
	select = SELECT + " ORDER BY g.Introskopas";//LENGTH(g.Vamzdzio_nr), g.Vamzdzio_nr";
        insert = INSERT;
        tableTooltip = "";
        search = SELECT + " WHERE g.Nr LIKE ? OR g.Pavadinimas LIKE ? OR g.Data LIKE ? OR g.Vamzdzio_pavad LIKE ? OR g.Vamzdzio_nr LIKE ? OR g.Introskopas LIKE ? ORDER BY g.Introskopas";
    }


    @Override
    protected void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
        date = new Datum();
        table_columns = new String[]{"Gen. Nr.", "Pavadinimas", "Data", "Vamzdis", "Vamzdžio Nr.", "Būsena", "Introskopo Nr.", "Introskopo pavad.", "Vieta", "Yra", "Failai", "Gautas", "Įdėtas", "Išimtas", "Sunaikintas"};
        table_column_width = new int[]{2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, fontsize, 2*fontsize, 2*fontsize, fontsize, fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize};
	if (connection != null) {
	    setLayout(new BorderLayout());
            setConstants();
	    getJSGvietos();
	    getJSGbusenos();
            getIntroskopai();
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
	gbc.weightx = 0;
	pFields.add(new JLabelRechts("Generatorius:", fontsize), gbc);
	gbc.weightx = 0.1;
	gbc.gridx = 1;
	tfJSGNr = new JMyTextField(10, fontsize);
        tfJSGNr.setToolTipText("Numeris");
	pFields.add(tfJSGNr, gbc);

	gbc.gridx = 2;
        tfPavad = new JMyTextField(10, fontsize);
        tfPavad.setToolTipText("Pavadinimas");
	pFields.add(tfPavad, gbc);
        
	gbc.gridx = 3;
        tfDate = new JMyTextField(10, fontsize);
        tfDate.setToolTipText("Pagaminimo data");
	pFields.add(tfDate, gbc);

	gbc.gridx = 4;
	gbc.weightx = 0;
	pFields.add(new JLabelRechts("Vamzdis:", fontsize), gbc);
	gbc.gridx = 5;
	gbc.weightx = 0.2;
        tfVamzdzio_pavad = new JMyTextField(10, fontsize);
        tfVamzdzio_pavad.setToolTipText("Pavadinimas");
        pFields.add(tfVamzdzio_pavad, gbc);
        
	gbc.gridx = 6;
        tfVamzdzioNr = new JMyTextField(10, fontsize);
        tfVamzdzioNr.setToolTipText("Numeris");
        pFields.add(tfVamzdzioNr, gbc);
	
	gbc.gridx = 7;
	gbc.weightx = 0;
	pFields.add(new JLabelRechts("Būsena:", fontsize), gbc);
	gbc.gridx = 8;
	gbc.weightx = 0;
        cbBusena = new JMyComboBox(busenos[1], fontsize);
        pFields.add(cbBusena, gbc);
        
	gbc.gridx = 9;
	gbc.weightx = 0;
	pFields.add(new JLabelRechts("Introskopas:", fontsize), gbc);
	gbc.gridx = 10;
	gbc.weightx = 0;
        cbInroskNr = new JMyComboBox(introskopai[0], fontsize);
        cbInroskNr.addItemListener(event -> {
            tfIntroskPavad.setText(introskopai[1][cbInroskNr.getSelectedIndex()]);
        });
        pFields.add(cbInroskNr, gbc);
        
	gbc.gridx = 11;
        tfIntroskPavad = new JMyTextField(10, fontsize);
	gbc.weightx = 0.2;
        tfIntroskPavad.setEditable(false);
        pFields.add(tfIntroskPavad, gbc);
        
	gbc.gridx = 12;
	gbc.weightx = 0;
	pFields.add(new JLabelRechts("Vieta:", fontsize), gbc);
	gbc.gridx = 13;
//	gbc.weightx = 0.1;
        cbVieta = new JMyComboBox(vietos[1], fontsize);
        pFields.add(cbVieta, gbc);
// Η δεύτερη σειρά
	gbc.gridy = 1;
	gbc.gridx = 0;
        pFields.add(new JLabelRechts("Failai:", fontsize), gbc);
	gbc.gridx = 1;
        gbc.gridwidth = 5;
        taFailai = new JMyTextArea(3, 10, fontsize);
        taFailai.setLineWrap(true);
        taFailai.setWrapStyleWord(true);
        taFailai.addMouseListener(this);
        scrPane = new JScrollPane(taFailai);
//        scrPane.setPreferredSize(new Dimension(200, 50));
        scrPane.setMinimumSize(new Dimension(200, 40));
        pFields.add(scrPane, gbc);
        
        
        gbc.gridwidth = 1;
	gbc.gridx = 6;
        tfYra = new JMyTextField(1, fontsize);
        tfYra.setToolTipText("T/N");
	gbc.weightx = 0;
        pFields.add(tfYra, gbc);
        
	gbc.gridx = 7;
        gbc.gridwidth = 1;
        pFields.add(new JLabelRechts("Gautas:", fontsize), gbc);
        tfGautas = new JMyTextField(10, fontsize);
        tfGautas.addMouseListener(this);
	gbc.gridx = 8;
        pFields.add(tfGautas, gbc);
        
	gbc.gridx = 9;
        pFields.add(new JLabelRechts("Įdėtas, išimtas:", fontsize), gbc);
        tfIdetas= new JMyTextField(10, fontsize);
        tfIdetas.addMouseListener(this);
	gbc.gridx = 10;
        pFields.add(tfIdetas, gbc);

//	gbc.gridx = 11;
//        pFields.add(new JLabelRechts("Isimtas:", fontsize), gbc);
        tfIsimas = new JMyTextField(10, fontsize);
        tfIsimas.addMouseListener(this);
	gbc.gridx = 11;
        pFields.add(tfIsimas, gbc);
        
	gbc.gridx = 12;
        pFields.add(new JLabelRechts("Sunaikintas:", fontsize), gbc);
        tfSunaikintas= new JMyTextField(10, fontsize);
        tfSunaikintas.addMouseListener(this);
	gbc.gridx = 13;
        pFields.add(tfSunaikintas, gbc);
    }


    @Override
    protected void createPanelFilterButtons() {
	pnFIlterButtons = new JPanel();
        tfSearch = new JMyTextField(10, fontsize);
        tfSearch.addActionListener(this);
        tfSearch.setActionCommand("search");
        pnFIlterButtons.add(tfSearch);
        btSearch = new JMyButton("Paieška", fontsize);
        btSearch.setActionCommand("search");
        btSearch.addActionListener(this);
	pnFIlterButtons.add(btSearch);
        btAll = new JMyButton("Visi", fontsize);
	btAll.setActionCommand("all");
	btAll.addActionListener(this);
	pnFIlterButtons.add(btAll);
	btFilter = new JMyButton("Turimi", fontsize);
	btFilter.setActionCommand("filter");
	btFilter.addActionListener(this);
	pnFIlterButtons.add(btFilter);
	btChange = new JMyButton("Išsaugoti", fontsize);
	btChange.setActionCommand("update");
	btChange.addActionListener(this);
	bAdd = new JMyButton("Naujas", fontsize);
	bAdd.setActionCommand("insert");
	bAdd.addActionListener(this);
	pnFIlterButtons.add(btChange);
	pnFIlterButtons.add(bAdd);
    }

 
    private void getJSGvietos() {
	try {
	    vietos = connection.getList("JSGvietos", "ID", "Pavadinimas", "ID");
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    private void getJSGbusenos() {       
	try {
	    busenos = connection.getList("Gen_busenos", "ID", "Pavadinimas", "Pavadinimas");
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    private void getIntroskopai() {
	try {
	    introskopai = connection.getList("Introskopai", "Nr", "Pavadinimas", "Nr");
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
	}
    }    
    
// SELECT g.Nr, g.Pavadinimas, g.Data, g.Vamzdzio_pavad, g.Vamzdzio_nr, b.Pavadinimas, g.Introskopas, i.Pavadinimas, v.Pavadinimas FROM JSG g INNER JOIN Gen_busenos b ON g.Busena = b.ID INNER JOIN Introskopai i ON g.Introskopas = i.Nr INNER JOIN JSGvietos v ON g.Vieta = v.ID
    @Override
    protected StringBuilder prepareFilter() {
        StringBuilder ret;
        ret = new StringBuilder(SELECT);
        switch (filtras) {
            case "Y":
                ret = new StringBuilder(SELECT).append(" WHERE g.Yra = ? ORDER BY g.Introskopas");//LENGTH(g.Vamzdzio_nr), g.Vamzdzio_nr");
                break;
            case "F":
                ret =  new StringBuilder(SELECT).append(" WHERE g.Nr = ? ORDER BY g.Introskopas");//LENGTH(g.Vamzdzio_nr), g.Vamzdzio_nr");
                break;
            case "P":
                ret = new StringBuilder(search);
                break;
        }
        return ret;
    }
    
    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
        switch (filtras) {
           case "Y":
                preparedFilter.setString(1, "T"); 
                break;
            case "F":
                preparedFilter.setString(1, tfJSGNr.getText());
                break;
            case "P":
                 for (int i = 1; i <= 6; i++) {
                     preparedFilter.setString(i, "%" + tfSearch.getText() + "%");
                 }
                break;
            default:
                throw new AssertionError();
        }
    }
     
     
// UPDATE JSG SET Pavadinimas = ?, Data = ?, Vamzdzio_pavad = ?, Vamzdzio_nr = ?, Busena = ?, Introskopas = ?, Vieta = ?, Yra = ?, Failai = ?, Gautas = ?, Idetas = ?, Isimtas = ?, Sunaikintas = ? WHERE Nr = ?
    @Override
    protected void update() {
	int selected_row;
	selected_row = table.getSelectedRow();
	if (selected_row >= 0) {
            try {
                if (preparedUpdate == null) {
                    preparedUpdate = connection.prepareStatement(UPDATE);
                }
                preparedUpdate.setString(1, tfPavad.getText());
                preparedUpdate.setString(2, tfDate.getText());
                preparedUpdate.setString(3, tfVamzdzio_pavad.getText());
                preparedUpdate.setString(4, tfVamzdzioNr.getText());
                preparedUpdate.setInt(5, Integer.parseInt(busenos[0][cbBusena.getSelectedIndex()]));
                preparedUpdate.setString(6, introskopai[0][cbInroskNr.getSelectedIndex()]);
                preparedUpdate.setInt(7, Integer.parseInt(vietos[0][cbVieta.getSelectedIndex()]));
                preparedUpdate.setString(8, tfYra.getText());
                preparedUpdate.setString(9, taFailai.getText());
                preparedUpdate.setString(10, tfGautas.getText());
                preparedUpdate.setString(11, tfIdetas.getText());
                preparedUpdate.setString(12, tfIsimas.getText());
                preparedUpdate.setString(13, tfSunaikintas.getText());
                preparedUpdate.setString(14, tfJSGNr.getText());
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

// INSERT INTO JSG (Nr, Pavadinimas, Data, Vamzdzio_pavad, Vamzdzio_nr, Busena, Introskopas, Vieta, Yra) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    private void insert() {
        try {
            if (preparedInsert == null) {
                preparedInsert = connection.prepareStatement(insert);
            }
                preparedInsert.setString(1, tfJSGNr.getText());
                preparedInsert.setString(2, tfPavad.getText());
                preparedInsert.setString(3, tfDate.getText());
                preparedInsert.setString(4, tfVamzdzio_pavad.getText());
                preparedInsert.setString(5, tfVamzdzioNr.getText());
                preparedInsert.setInt(6, Integer.parseInt(busenos[0][cbBusena.getSelectedIndex()]));
                preparedInsert.setString(7, introskopai[0][cbInroskNr.getSelectedIndex()]);
                preparedInsert.setInt(8, Integer.parseInt(vietos[0][cbVieta.getSelectedIndex()]));
                preparedInsert.setString(9, tfYra.getText());
                preparedInsert.setString(10, taFailai.getText());
                preparedInsert.setString(11, get_NULL_tested(tfGautas.getText()));
                preparedInsert.setString(12, get_NULL_tested(tfIdetas.getText()));
                preparedInsert.setString(13, get_NULL_tested(tfIsimas.getText()));
                preparedInsert.setString(14, get_NULL_tested(tfSunaikintas.getText()));
             if (preparedInsert.executeUpdate() == 1) {
                filter(select);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected String get_NULL_tested(Object obj) {
        String txt;
        txt = String.valueOf(obj);
        if (txt.isEmpty() || obj == null) {
            txt = null;
        }
        return txt;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
	String derBefehl;
	derBefehl = e.getActionCommand();
	switch (derBefehl) {
	    case "all":
		filter(select);
		break;
	    case "search":
                filtras = "P";
		filter();
		break;
	    case "update":
                filtras = "F";
		update();
		break;	
	    case "filter":
                filtras = "Y";
		filter();
		break;
	    case "insert":
		insert();
		break;	
	}
	
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        Component component;
        component = me.getComponent();
        if (me.getButton() == 3) {            
            if (component.equals(taFailai)) {
                openFile(FOLDER_RSC, taFailai.getSelectedText());
            }
        }
	if (component.equals(table)) {
            the_row = table.getSelectedRow();
            if (the_row >= 0) {
                tfJSGNr.setText((String) table.getValueAt(the_row, 0));
                tfPavad.setText((String) table.getValueAt(the_row, 1));
                tfDate.setText(String.valueOf(table.getValueAt(the_row, 2)));
                tfVamzdzio_pavad.setText((String) table.getValueAt(the_row, 3));
                tfVamzdzioNr.setText((String) table.getValueAt(the_row, 4));
                setComboBoxItem(cbBusena, busenos[1], (String) table.getValueAt(the_row, 5));
                setComboBoxItem(cbInroskNr, introskopai[0], (String) table.getValueAt(the_row, 6));
                tfIntroskPavad.setText((String) table.getValueAt(the_row, 7));
                setComboBoxItem(cbVieta, vietos[1], (String) table.getValueAt(the_row, 8));
                tfYra.setText((String) table.getValueAt(the_row, 9));
                taFailai.setText((String) table.getValueAt(the_row, 10));
                tfGautas.setText(String.valueOf(table.getValueAt(the_row, 11)));
                tfIdetas.setText(String.valueOf(table.getValueAt(the_row, 12)));
                tfIsimas.setText(String.valueOf(table.getValueAt(the_row, 13)));
                tfSunaikintas.setText(String.valueOf(table.getValueAt(the_row, 14)));
            }
	}
    }


    
    
}

//	tableModel = new DefaultTableModel(new Object[]{"", "Datum", "<html>Fett<br>%</hmtl>", "<html>Muskeln<br>%</html>", "<html>Wasser<br>%</html>", "<html>Knochen<br>kg</html>", "<html>Masse<br>kg</html>", "<html>Energie0<br>kcal</html>", "<html>Energie1<br>kcal</html>", "<html>Bauch<br>cm</html>", "<html>Oberarm<br>cm</html>", "<html>Unterarm<br>cm</html>", "<html>Ober-<br>schenkel<br>cm</html>", "<html>Unter-<br>schenkel<br>cm</html>", "<html>Brust<br>cm</html>", "<html>Fettmasse<br>kg</html>", "<html>Muskel-<br>masse<br>kg</html>"}, 0);