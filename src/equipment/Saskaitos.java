/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import datum.Datum;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author a
 */
public class Saskaitos extends Liftu_darbai {

    private static final String SELECT = "SELECT s.NR, s.Data, s.Ivesta, s.DVSNr, k.Pavadinimas, sut.Pavadinimas, s.BiudKodas, b.Pavadinimas, s.UzsNr, s.UzsData, s.UzsSuma, s.UzsPastabos, s.Suma, s.Prekes, s.Paslaugos, s.IT, s.Pastabos, s.Failas, s.Failas2, s.Failas3, s.Filialas  FROM Saskaitos s INNER JOIN Biudzetas b ON s.BiudKodas = b.Kodas INNER JOIN Sutartys sut ON s.DVSNr = sut.RegNr INNER JOIN Kontrahentai k ON sut.Kontrahentas = k.ID "; // συνολικά 18
    private static final String INSERT = "INSERT INTO Saskaitos (NR, Data, Ivesta, DVSNr, BiudKodas, UzsNr, UzsData, UzsSuma, UzsPastabos, Suma, Prekes, Paslaugos, IT, Pastabos, Failas, Failas2, Failas3, Filialas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // συνολικά 15
    private static final String UPDATE = "UPDATE Saskaitos SET Data = ?, Ivesta = ?, DVSNr = ?, BiudKodas = ?, NR = ?, UzsData = ?, UzsSuma = ?, UzsPastabos = ?, Suma = ?, Prekes = ?, Paslaugos = ?, IT = ?, Pastabos = ?, Failas = ?, Failas2 = ?, Failas3 = ?, Filialas = ? WHERE Uzsnr = ?";
    private static final String FOLDER1 = "Saskaitos";

//"Nr.", "Data", "DVS Nr.", "Kontrahentas", "Sutarties pavadinimas", "Biud. kodas", "Biud. pavadinimas", "Užs. Nr.", "Užs. data", "Užs. suma", "Užs. pastabos", "Sąsk. suma", "Prekės", "Paslaugos", "IT", "Pastabos sąskaitai", "Failas", "Filialas"

    JMyCheckBox chNr, chIvesta, chBiuKodas, chBiuPavad, chKontrahentas, chSutNr, chSutPavad, chUzsNr, chUzsData, chIT, chUzsPast, chFilialas;
    JMyComboBox cbBiuKodai, cbBiuPavad, cbSutNr, cbSutPavad;
    JMyTextField tfNr, tfIvesta, tfKontrah, tfUzsNr, tfUzsData, tfUzsSuma,tfSuma, tfPrekes, tfPasl, tfIT, tfFailas, tfFailas2, tfFailas3, tfFilialas, tfUzsPast;
    
    String[][] biudzetas, kontrahentai; //[Kodas][Pavadinimas], 
    String [][] sutartys; //[{DVSNr}{Pavadinimas}{Kontrahentas (Kontrahentai.Pavadinimas)}][VIPISNR]
    
//    NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);

    protected Saskaitos(ConnectionEquipment connection, int size) {
	super(connection, size);
    }

    @Override
    protected void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
        date = new Datum();
        
        table_columns = new String[]{"Nr.", "Data", "Ivesta", "DVS Nr.", "Kontrahentas", "Sutarties pavadinimas", "Biud. kodas", "Biud. pavadinimas", "Užs. Nr.", "Užs. data", "Užs. suma", "Užs. pastabos", "Sąsk. suma", "Prekės", "Paslaugos", "IT", "Pastabos sąskaitai", "Failas", "Failas", "Failas", "Filialas"};
        table_column_width = new int[]{4*fontsize, 3*fontsize, 3*fontsize, 3*fontsize, 6*fontsize, 25*fontsize, 3*fontsize, 12*fontsize, 10*fontsize, 3*fontsize, 2*fontsize, 6*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 4*fontsize, 4*fontsize, fontsize, fontsize, fontsize, fontsize};
	if (connection != null) {
	    setLayout(new BorderLayout());
            getKontrahentai();
//            System.out.println(Arrays.toString(kontrahentai[1]));
            getSutartys();
            getBiudzetas();
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
	select = SELECT + "WHERE DATEDIFF(CURDATE(), s.Data) < 188 ORDER BY s.Data DESC";
        insert = INSERT ;
        tableTooltip = "";
    }

    @Override
    protected void createPanelFields() {
	pFields = new JPanel(new GridBagLayout());
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.insets = new Insets(0, 0, 5, 5);
        
// Μια πρώτη σειρά

//"Nr.", "Data", "DVS Nr.", "Kontrahentas", "Sutarties pavadinimas", "Biud. kodas", "Biud. pavadinimas", "Sąsk. suma", "Prekės", "Paslaugos"
        gbc.gridwidth = 1;
        gbc.weightx = 0;
	gbc.gridy = 0;
	gbc.gridx = 0;
        tfNr = new JMyTextField(6, fontsize);
//        tfNr.setToolTipText("Sąskaitos numeris");
        pFields.add(tfNr, gbc);
        
	gbc.gridx = 1;
        gbc.weightx = 0;
        tfDate = new JMyTextField(6, fontsize);
        tfDate.addMouseListener(this);
//        tfDate.setToolTipText("Sąskaitos data");
        pFields.add(tfDate, gbc);
        
	gbc.gridx = 2;
        gbc.weightx = 0;
        tfKontrah = new JMyTextField(10, fontsize);
        pFields.add(tfKontrah, gbc);
        
	gbc.gridx = 3;
        gbc.weightx = 0;
        cbSutNr = new JMyComboBox(sutartys[0], fontsize); //sutartys[{DVSNr}{Pavadinimas}{Kontrahentas (Kontrahentai.Pavadinimas)}][VIPISNR]
        cbSutNr.addItemListener(event -> {
            int n = cbSutNr.getSelectedIndex();
            int p = cbSutPavad.getSelectedIndex();
                tfKontrah.setText(getKontrahID(sutartys[2][n]));
            if (p != n) {
                cbSutPavad.setSelectedIndex(n);
            }
        });
        pFields.add(cbSutNr, gbc);

	gbc.gridx = 4;
        gbc.weightx = 0;
        cbSutPavad = new JMyComboBox(sutartys[1], fontsize); //sutartys[{DVSNr}{Pavadinimas}{Kontrahentas (Kontrahentai.Pavadinimas)}][VIPISNR]
        cbSutPavad.addItemListener(event -> {
            int n = cbSutNr.getSelectedIndex();
            int p = cbSutPavad.getSelectedIndex();
            if (p != n) {
                cbSutNr.setSelectedIndex(p);
            }
        });
        pFields.add(cbSutPavad, gbc);
        
	gbc.gridx = 5;
        gbc.weightx = 0;
	cbBiuKodai = new JMyComboBox(biudzetas[0], fontsize); //[Kodas][Pavadinimas]
        cbBiuKodai.addItemListener(event -> {
            int p = cbBiuPavad.getSelectedIndex();
            int k = cbBiuKodai.getSelectedIndex();
            if (p != k) {
                cbBiuPavad.setSelectedIndex(k);
            }
        });
	pFields.add(cbBiuKodai, gbc);
        
	gbc.gridx = 6;
        gbc.weightx = 0;
	cbBiuPavad = new JMyComboBox(biudzetas[1], fontsize); //[Kodas][Pavadinimas]
        cbBiuPavad.addItemListener(event -> {
            int p = cbBiuPavad.getSelectedIndex();
            int k = cbBiuKodai.getSelectedIndex();
            if (p != k) {
                cbBiuKodai.setSelectedIndex(p);
            }
        });
	pFields.add(cbBiuPavad, gbc);        

	gbc.gridx = 7;
        gbc.weightx = 0.25;
        tfSuma = new JMyTextField(4, fontsize);
//        tfSuma.setToolTipText("Sąskaitos suma");
        pFields.add(tfSuma, gbc);
        
	gbc.gridx = 8;
        gbc.weightx = 0.25;
        tfPrekes = new JMyTextField(4, fontsize);
//        tfPrekes.setToolTipText("Prekėms");
        pFields.add(tfPrekes, gbc);
        
	gbc.gridx = 9;
        gbc.weightx = 0.5;
        tfPasl = new JMyTextField(4, fontsize);
//        tfPasl.setToolTipText("Paslaugoms");
        pFields.add(tfPasl, gbc);
        
// Η δεύτερη σειρά

//"Nr.", "Data", "DVS Nr.", "Kontrahentas", "Sutarties pavadinimas", "Biud. kodas", "Biud. pavadinimas", "Sąsk. suma", "Prekės", "Paslaugos"	gbc.gridy = 1;
        gbc.gridy = 1;
	gbc.gridx = 0;
        chNr = new JMyCheckBox("Sąskaitos numeris", false, fontsize);
	pFields.add(chNr, gbc);
        
	gbc.gridx = 1;
        chDate = new JMyCheckBox("Sąskaitos data", false, fontsize);
	pFields.add(chDate, gbc);
        
	gbc.gridx = 2;
        chKontrahentas = new JMyCheckBox("Kontrahentas", false, fontsize);
	pFields.add(chKontrahentas, gbc);
        
	gbc.gridx = 3;
	chSutNr = new JMyCheckBox("Sut. Nr.", false, fontsize);
	pFields.add(chSutNr, gbc);

	gbc.gridx = 4;
	chSutPavad = new JMyCheckBox("Sut. pavad.", false, fontsize);
	pFields.add(chSutPavad, gbc);
        
        gbc.gridx = 5;
        chBiuKodas = new JMyCheckBox("B. kodas", false, fontsize);
        pFields.add(chBiuKodas, gbc);
        
	gbc.gridx = 6;
//	gbc.gridwidth = 4;
	chBiuPavad = new JMyCheckBox("Biudžeto pavadinimas", false, fontsize);
	pFields.add(chBiuPavad, gbc);
        
        gbc.gridx = 7;
        pFields.add(new JLabelLeft("Suma", fontsize), gbc);
        
        gbc.gridx = 8;
        pFields.add(new JLabelLeft("Prekės", fontsize), gbc);
        
        gbc.gridx = 9;
        pFields.add(new JLabelLeft("Paslaugos", fontsize), gbc);

// Η τρίτη σειρά

//"Užs. Nr.", "Užs. data", "Užs. suma", "Užs. pastabos", "IT", "Pastabos sąskaitai", "Failas", "Filialas"
	gbc.gridwidth = 1;
	gbc.gridy = 2;
	gbc.gridx = 0;
        tfUzsNr = new JMyTextField(6, fontsize);
        pFields.add(tfUzsNr, gbc);
        
	gbc.gridx = 1;
        tfUzsData = new JMyTextField(6, fontsize);
        tfUzsData.addMouseListener(this);
        pFields.add(tfUzsData, gbc);

	gbc.gridx = 2;
        tfUzsSuma = new JMyTextField(4, fontsize);
//        tfUzsSuma.setToolTipText("Užsakymo suma");
        pFields.add(tfUzsSuma, gbc);
        
	gbc.gridx = 3;
        tfFilialas = new JMyTextField(2, fontsize);
        pFields.add(tfFilialas, gbc);
        
	gbc.gridx = 4;
        tfIT = new JMyTextField(4, fontsize);
//        tfIT.setToolTipText("IT");
        pFields.add(tfIT, gbc);
        
        gbc.gridx = 5;
        gbc.weightx = 0;
        gbc.gridwidth = 2;
        tfFailas = new JMyTextField(30, fontsize);
        tfFailas.setToolTipText("Με το δεξιό πλήκτρο του ποντικιού μπορείτε να ανοίξετε το επιλεγμένο αρχείο.");
        tfFailas.addMouseListener(this);
        pFields.add(tfFailas, gbc);

        gbc.gridx = 7;
        gbc.weightx = 0;
        tfFailas2 = new JMyTextField(30, fontsize);
        tfFailas2.addMouseListener(this);
        pFields.add(tfFailas2, gbc);

        gbc.gridx = 9;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        tfFailas3 = new JMyTextField(30, fontsize);
        tfFailas3.addMouseListener(this);
        pFields.add(tfFailas3, gbc);
        

// Η τέταρτη σειρά
//        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 1;
	gbc.gridy = 3;
	gbc.gridx = 0;   
        gbc.weightx = 0;
        chUzsNr = new JMyCheckBox("Užs. Nr.", false, fontsize);
        pFields.add(chUzsNr, gbc);
	gbc.gridx = 1;        
        chUzsData = new JMyCheckBox("Užs. data", false, fontsize);
        pFields.add(chUzsData, gbc);
        gbc.gridx = 2;
        pFields.add(new JLabelLeft("Užs. suma", fontsize), gbc);
	gbc.gridx = 3;        
        chFilialas = new JMyCheckBox("Filialas", false, fontsize);
        pFields.add(chFilialas, gbc);
	gbc.gridx = 4;   
        chIT = new JMyCheckBox("IT", false, fontsize);
        pFields.add(chIT, gbc);
        gbc.gridx = 5;
//        gbc.gridwidth = 6;
//        gbc.anchor = GridBagConstraints.CENTER;
        pFields.add(new JLabelLeft("Failai", fontsize), gbc);
 
// Η πέμτη σειρά
	gbc.gridy = 4;
	gbc.gridx = 0; 
        tfIvesta = new JMyTextField(10, fontsize);
        tfIvesta.setToolTipText("Sąskaitos pateikimo VIPIS data");
        tfIvesta.addMouseListener(this);
        chIvesta = new JMyCheckBox("Įvedimo data ", false, fontsize);
        pFields.add(tfIvesta, gbc);
	gbc.gridx = 1; 
        pFields.add(chIvesta, gbc);
        
	gbc.gridx = 2; 
        gbc.gridwidth = 7;
        tfUzsPast = new JMyTextField(100, fontsize);
        pFields.add(tfUzsPast, gbc);
	gbc.gridx = 9;        
        gbc.gridwidth = 1;
        chUzsPast = new JMyCheckBox("Užs. past.", false, fontsize);
        pFields.add(chUzsPast, gbc);
       
// Η έκτη σειρά
// "Nr.", "Data", "DVS Nr.", "Kontrahentas", "Sutarties pavadinimas", "Biud. kodas", "Biud. pavadinimas", "Sąsk. suma", "Prekės", "Paslaugos"	gbc.gridy = 4;
	gbc.gridy = 5;
	gbc.gridx = 0;
	gbc.gridwidth = 1;
	createPanelEditButtons();
	pFields.add(pEditButtons, gbc);

	gbc.gridx = 1;
	gbc.gridwidth = 8;
	taMessage = new JMyTextArea_monospaced(5, 30, fontsize);
	taMessage.setLineWrap(true);
	taMessage.setWrapStyleWord(true);
	scrMessage = new JScrollPane(taMessage);
        scrMessage.setMinimumSize(new Dimension(500, 55));
	pFields.add(scrMessage, gbc);

	gbc.gridx = 9;
	gbc.gridwidth = 1;
	createPanelMessages();
	pFields.add(pMessage, gbc);
    }

    private void getBiudzetas() {
        try {        
            biudzetas = connection.getList("Biudzetas", "Kodas", "Pavadinimas", "Pavadinimas");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        }       
    }
    
//[{DVSNr}{Pavadinimas}{Kontrahentas (Kontrahentai.Pavadinimas)}][VIPISNR]
    private void getSutartys() {
        try {        
            sutartys = connection.getList("Sutartys", new String[]{"RegNr", "Pavadinimas", "Kontrahentas", "VIPISNR"}, "Pavadinimas", "Iki > CURDATE()");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void getKontrahentai() {
        try {        
            kontrahentai = connection.getList("Kontrahentai", "ID", "Pavadinimas", "Pavadinimas");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    


// SELECT s.NR, s.Data, s.DVSNr, k.Pavadinimas, sut.Pavadinimas, s.BiudKodas, b.Pavadinimas, s.UzsNr, s.UzsData, s.UzsSuma, s.UzsPastabos, s.Suma, s.Prekes, s.Paslaugos, s.IT, s.Pastabos, s.Failas, s.Filialas  FROM Saskaitos s INNER JOIN Biudzetas b ON s.BiudKodas = b.Kodas INNER JOIN Sutartys sut ON s.DVSNr = sut.RegNr INNER JOIN Kontrahentai k ON sut.Kontrahentas = k.ID
    @Override
    protected StringBuilder prepareFilter() {
	StringBuilder sb;
	sb = new StringBuilder(SELECT);
	if (chNr.isSelected() || chDate.isSelected() || chKontrahentas.isSelected() || chSutNr.isSelected() || chSutPavad.isSelected() || chBiuKodas.isSelected() || chBiuPavad.isSelected() || chUzsNr.isSelected() || chUzsData.isSelected() || chFilialas.isSelected() || chIT.isSelected() || chUzsPast.isSelected() || chMessage.isSelected() || chIvesta.isSelected()) {
	    sb.append(" WHERE");
            if (chNr.isSelected()) {
		sb.append(" s.NR LIKE ?");
            }
            if (chDate.isSelected()) {
                appendAND(sb);
		sb.append(" s.Data LIKE ?");
            }
            if (chKontrahentas.isSelected()) {
                appendAND(sb);
		sb.append(" k.Pavadinimas LIKE ?");
            }
            if (chSutNr.isSelected()) {
                appendAND(sb);
		sb.append(" s.DVSNr = ?");
            }
            if (chSutPavad.isSelected()) {
                appendAND(sb);
		sb.append(" sut.Pavadinimas = ?");
            }
            if (chBiuKodas.isSelected()) {
                appendAND(sb);
		sb.append(" s.BiudKodas = ?");
            }
            if (chBiuPavad.isSelected()) {
                appendAND(sb);
		sb.append(" b.Pavadinimas = ?");
            }
            if (chUzsNr.isSelected()) {
                appendAND(sb);
		sb.append(" s.UzsNr LIKE ?");
            }
            if (chUzsData.isSelected()) {
                appendAND(sb);
		sb.append(" s.UzsData LIKE ?");
            }
            if (chFilialas.isSelected()) {
                appendAND(sb);
		sb.append(" s.Filialas LIKE ?");
            }
            if (chIT.isSelected()) {
                appendAND(sb);
		sb.append(" s.IT LIKE ?");
            }
            if (chUzsPast.isSelected()) {
                appendAND(sb);
		sb.append(" s.UzsPastabos LIKE ?");
            }
            if (chMessage.isSelected()) {
                appendAND(sb);
		sb.append(" s.Pastabos LIKE ?");
            }
            if (chIvesta.isSelected()) {
                appendAND(sb);
		sb.append(" s.Ivesta LIKE ?");
            }
	}
        sb.append(" ORDER BY s.Data");
//	System.out.println(sb.toString());
	return sb;
    }
    
// Integer.valueOf(tptypes[0][cbTPtype.getSelectedIndex()])  cbKontrah.getSelectedIndex()
    @Override
    protected void preparedFilter_setPrepared(StringBuilder sb) throws SQLException {
	int n;
	n = 0;
        if (chNr.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfNr.getText() + "%");
        }
        if (chDate.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfDate.getText() + "%");
        }
        if (chKontrahentas.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfKontrah.getText() + "%");
        }
        if (chSutNr.isSelected()) {
            n++;
            preparedFilter.setString(n, (String) cbSutNr.getSelectedItem());
        }
        if (chSutPavad.isSelected()) {
            n++;
            preparedFilter.setString(n, (String) cbSutPavad.getSelectedItem());
        }
        if (chBiuKodas.isSelected()) {
            n++;
            preparedFilter.setString(n, (String) cbBiuKodai.getSelectedItem());
        }
        if (chBiuPavad.isSelected()) {
            n++;
            preparedFilter.setString(n, (String) cbBiuPavad.getSelectedItem());
        }
        if (chUzsNr.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfUzsNr.getText() + "%");
        }
        if (chUzsData.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfUzsData.getText() + "%");
        }
        if (chFilialas.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfFilialas.getText() + "%");
        }
        if (chIT.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfIT.getText() + "%");
        }
        if (chUzsPast.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfUzsPast.getText() + "%");
        }
        if (chMessage.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + taMessage.getText() + "%");
        }
        if (chIvesta.isSelected()) {
            n++;
            preparedFilter.setString(n, "%" + tfIvesta.getText() + "%");
        }
//            preparedFilter.setInt(n, Integer.valueOf(kontrahentai[0][cbKontrah.getSelectedIndex()]));
    }
    
    @Override
    protected void filter() {
	Object[] the_row;
	int i, colcount;
        float saskSuma;
	tableModel.setRowCount(0);
	StringBuilder sb;
	ResultSet resultset;
        saskSuma = 0;
	try {
	    sb = prepareFilter();
	    preparedFilter = connection.prepareStatement(sb.toString());
	    preparedFilter_setPrepared(sb);
	    resultset = preparedFilter.executeQuery();
	    colcount = tableModel.getColumnCount();
	    the_row = new Object[colcount];
	    while (resultset.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    the_row[i] = resultset.getObject(i + 1);
                    if (i == 12) {
                        saskSuma = saskSuma + Float.parseFloat(String.valueOf(the_row[i]));
                    }
		}
		tableModel.addRow(the_row);
	    }
	    resultset.close();
            for (i = 0; i <= colcount - 1; i++) {
                the_row[i] = "";
                if (i == 12) {
                    the_row[i] = saskSuma;
                }
            }
            tableModel.addRow(the_row);
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    @Override
    protected void filter(String query) {
        Object[] the_row;
	int i, colcount;
        float saskSuma;
	tableModel.setRowCount(0);
	ResultSet resultset;
        saskSuma = 0;
	try {
	    resultset = connection.executeQuery(query);
	    colcount = tableModel.getColumnCount();
	    the_row = new Object[colcount];
	    while (resultset.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    the_row[i] = resultset.getObject(i + 1);
                    if (i == 12) {
                        saskSuma = saskSuma + Float.parseFloat(String.valueOf(the_row[i]));
                    }
		}
		tableModel.addRow(the_row);
	    }
	    resultset.close();
            for (i = 0; i <= colcount - 1; i++) {
                the_row[i] = "";
                if (i == 12) {
                    the_row[i] = saskSuma;
                }
            }
            tableModel.addRow(the_row);
	} catch (SQLException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}

    }

// UPDATE Saskaitos SET Data = ?, DVSNr = ?, BiudKodas = ?, UzsNr = ?, UzsData = ?, UzsSuma = ?, UzsPastabos = ?, Suma = ?, Prekes = ?, Paslaugos = ?, IT = ?, Pastabos = ?, Failas = ?, Filialas = ? WHERE NR = ?
    @Override
    protected void update() {
	if (table.getSelectedRow() >= 0) {
            try {
                if (preparedUpdate == null) {
                    preparedUpdate = connection.prepareStatement(UPDATE);
                }
            preparedUpdate.setString(1, get_NULL_tested(tfDate.getText()));
            preparedUpdate.setString(2, get_NULL_tested(tfIvesta.getText()));
            preparedUpdate.setString(3, (String) cbSutNr.getSelectedItem());
            preparedUpdate.setString(4, (String) cbBiuKodai.getSelectedItem());
            preparedUpdate.setString(5, tfNr.getText());
            preparedUpdate.setString(6, tfUzsData.getText());
            preparedUpdate.setFloat(7, Float.parseFloat(replaceComma(tfUzsSuma.getText())));
//            preparedUpdate.setFloat(6, Float.valueOf(tfUzsSuma.getText()));
            preparedUpdate.setString(8, tfUzsPast.getText());
            preparedUpdate.setFloat(9, Float.parseFloat(replaceComma(tfSuma.getText())));
            preparedUpdate.setFloat(10, Float.parseFloat(replaceComma(tfPrekes.getText())));
            preparedUpdate.setFloat(11, Float.parseFloat(replaceComma(tfPasl.getText())));
            preparedUpdate.setString(12, tfIT.getText());
            preparedUpdate.setString(13, taMessage.getText());
            preparedUpdate.setString(14, tfFailas.getText());
            preparedUpdate.setString(15, tfFailas2.getText());
            preparedUpdate.setString(16, tfFailas3.getText());
            preparedUpdate.setString(17, tfFilialas.getText());
            preparedUpdate.setString(18, tfUzsNr.getText());
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

// NR, Data, Ivesta, DVSNr, BiudKodas, UzsNr, UzsData, UzsSuma, UzsPastabos, Suma, Prekes, Paslaugos, IT, Pastabos, Failas, Failas2, Failas3, Filialas
    @Override
    protected void insert() {
        float suma, prekes, pasl;
        suma = tfSuma.getText().equals("") ? 0 : Float.parseFloat(replaceComma(tfSuma.getText()));
        prekes = tfPrekes.getText().equals("") ? 0 : Float.parseFloat(replaceComma(tfPrekes.getText()));
        pasl = tfPasl.getText().equals("") ? 0 : Float.parseFloat(replaceComma(tfPasl.getText()));
        try {
            if (preparedInsert == null) {
                preparedInsert = connection.prepareStatement(insert);
            }
            preparedInsert.setString(1, tfNr.getText());
            preparedInsert.setString(2, get_NULL_tested(tfDate.getText()));
            preparedInsert.setString(3, get_NULL_tested(tfIvesta.getText()));
            preparedInsert.setString(4, (String) cbSutNr.getSelectedItem());
            preparedInsert.setString(5, (String) cbBiuKodai.getSelectedItem());
            preparedInsert.setString(6, tfUzsNr.getText());
            preparedInsert.setString(7, tfUzsData.getText());
            preparedInsert.setFloat(8, Float.parseFloat(replaceComma(tfUzsSuma.getText())));
            preparedInsert.setString(9, tfUzsPast.getText());
            preparedInsert.setFloat(10, suma);
            preparedInsert.setFloat(11, prekes);
            preparedInsert.setFloat(12, pasl);
            preparedInsert.setString(13, tfIT.getText());
            preparedInsert.setString(14, taMessage.getText());
            preparedInsert.setString(15, tfFailas.getText());
            preparedInsert.setString(16, tfFailas2.getText());
            preparedInsert.setString(17, tfFailas3.getText());
            preparedInsert.setString(18, tfFilialas.getText());
            if (preparedInsert.executeUpdate() == 1) {
                filter();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getKontrahID(String id) {
        int n;
        boolean found;
        n = 0;
        found = false;
        while (n < kontrahentai[0].length && !found) {
            found = kontrahentai[0][n].equals(id);
            n++;
        }
        return kontrahentai[1][n-1];
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
        if ((me.getComponent().equals(tfFailas)) & me.getButton() == 3) {
            openFile(FOLDER1, tfFailas.getSelectedText());
        }
        if ((me.getComponent().equals(tfFailas2)) & me.getButton() == 3) {
            openFile(FOLDER1, tfFailas2.getText());
        }
        if ((me.getComponent().equals(tfFailas3)) & me.getButton() == 3) {
            openFile(FOLDER1, tfFailas3.getText());
        }
	if (me.getComponent().equals(table)) {
            the_row = table.getSelectedRow();
            if (the_row >= 0) {
                tfNr.setText((String) table.getValueAt(the_row, 0));
                tfDate.setText(String.valueOf(table.getValueAt(the_row, 1)));
                tfIvesta.setText(String.valueOf(table.getValueAt(the_row, 2)));
                tfKontrah.setText((String) (table.getValueAt(the_row, 4)));
                setComboBoxItem(cbSutNr, sutartys[0], (String) (table.getValueAt(the_row, 3)));
//                cbSutPavad.setSelectedIndex(cbSutNr.getSelectedIndex());
                setComboBoxItem(cbBiuKodai, biudzetas[0], (String) (table.getValueAt(the_row, 6)));
//                cbBiuPavad.setSelectedIndex(cbBiuKodai.getSelectedIndex());
                tfUzsNr.setText((String) table.getValueAt(the_row, 8));
                tfUzsData.setText(String.valueOf(table.getValueAt(the_row, 9)));
                tfUzsSuma.setText(String.valueOf(table.getValueAt(the_row, 10)));
                tfUzsPast.setText((String) table.getValueAt(the_row, 11));
                tfSuma.setText(String.valueOf(table.getValueAt(the_row, 12)));
                tfPrekes.setText(String.valueOf(table.getValueAt(the_row, 13)));
                tfPasl.setText(String.valueOf(table.getValueAt(the_row, 14)));
                tfIT.setText((String) table.getValueAt(the_row, 15));
                taMessage.setText((String) table.getValueAt(the_row, 16));
                tfFailas.setText((String) table.getValueAt(the_row, 17));
                tfFailas2.setText((String) table.getValueAt(the_row, 18));
                tfFailas3.setText((String) table.getValueAt(the_row, 19));
                tfFilialas.setText((String) table.getValueAt(the_row, 20));
            }
	}
        if ( me.getButton() == 3) {
            if (me.getComponent().equals(tfIvesta)) tfIvesta.setText(date.getToday());
            if (me.getComponent().equals(tfDate)) tfDate.setText(date.getToday());
            if (me.getComponent().equals(tfUzsData)) tfUzsData.setText(date.getToday());
        }
    }

    
}
