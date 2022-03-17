/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import datum.Datum;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Ataskaita_RSC extends Ataskaita_liftai {


    private static final String TEX_0 = "\\documentclass[a4paper,12pt]{article}\n" +
"\\usepackage[left=10mm, top=30mm, bottom=20mm, right=10mm, landscape]{geometry}\n" +
"\\usepackage[utf8]{inputenc}\n" +
"\\usepackage[lithuanian]{babel}\n" +
"\\usepackage[L7x]{fontenc}\n" +
"\\usepackage[unicode=true,,]{hyperref}\n" +
"\\usepackage{soulutf8}\n" +
"\\usepackage{supertabular}\n" +
"\\usepackage{import}\n" +
"\n" +
"\\hypersetup{pdftitle={Jonizuojančiosios spinduliuotės generatorių metinės inventorizacijos duomenys},pdfauthor={Antanas Kvietkauskas},pdfcreator={kile},pdfproducer={Antanas Kvietkauskas},pdfkeywords={JSŠ, Jonizuojančiosios spinduliuotės generatoriai, metinė inventorizacija},pdfsubject={}}\n" +
"\\date{\today}" + 
"\\begin{document}\n" +
"  \\setlength{\\leftskip}{21cm} \\setlength\\parindent{0cm}\n" +
"  {\\small Radiologinių incidentų ir avarijų prevencijos ir padarinių likvidavimo tvarkos \\\\2 Priedas}\n" +
"  \\begin{center}\n" +
"	  \\textbf{VALSTYBĖS ĮMONĖS LIETUVOS ORO UOSTŲ VILNIAUS FILIALAS\\\\\n" +
"	  Įmonės kodas 303316259, Rodūnios kelias 10A, Vilnius, tel. 8 5 273 9326, faks. 8 5 232 9122, e-paštas: info@vno.lt\\\\ \\vspace{1em}\n" +
"	  DOZIMETRINIŲ MATAVIMŲ PROTOKOLAS}\\\\ \\vspace{1em}";
    private static final String TEX_1 = "Vilnius\n" +
"  \\end{center}\n" +
"  \\vspace{1em}\n" +
"  \\setlength{\\leftskip}{0cm}\n" +
"Įrenginio buvimo adresas: keleivių terminalas (Rodūnios kelias 2, Vilnius)\\\\\n" +
"Matavimai atlikti prietaisu (tipas, gamykl. Nr.): ";
    private static final String TEX_2 = "patikra atlikta ";
    private static final String TEX_3 = "patikros liudijimas Nr. ";
    private static final String TEX_4 = "Matavimų data:  ";
    private static final String TEX_TABLE_HEAD = "  \\begin{longtable}{|p{0.03\\textwidth}|p{0.15\\textwidth}|p{0.12\\textwidth}|p{0.1\\textwidth}|p{0.1\\textwidth}|p{0.05\\textwidth}|p{0.05\\textwidth}|p{0.05\\textwidth}|p{0.05\\textwidth}|p{0.05\\textwidth}|p{0.05\\textwidth}|} \\hline\n" +
"    Eil. Nr. & Įrenginio pavadinimas & Įrenginio gamyklinis numeris & Darbo režimas / radionuklidas, aktyvumas & Įrenginio buvimo vieta & \\multicolumn{6}{|c|}{Matavimų rezultatai nSv/h} \\\\ \\cline{6-11} \\endfirsthead\n" +
"\n" +
"	\\hline \\multicolumn{1}{|c|}{1} & \\multicolumn{1}{c|}{2} & \\multicolumn{1}{c|}{3} & \\multicolumn{1}{c|}{4} & \\multicolumn{1}{c|}{5} & \\multicolumn{1}{c|}{6} & \\multicolumn{1}{c|}{7} & \\multicolumn{1}{c|}{8} & \\multicolumn{1}{c|}{9} & \\multicolumn{1}{c|}{10} & \\multicolumn{1}{c|}{11} \\\\\\hline \\endhead\n" +
"\n" +
"	\\hline\n" +
"	\\endfoot\n" +
"	\\hline\n" +
"	\\endlastfoot\n" +
"&&&&& Operato-riaus darbo vieta & Prieš tunelį & Už tunelio & Prietaiso paviršiuje & Prie generatoriaus & 1~m atstumu nuo įreng.  \\\\\\hline % Generator is on the side without door; Login: 004/94866. For 7085A:main menu 15937, service menu 14123, setup menu 94866. Die Messwerte im Abstand von 10 cm vom Gerätegehäuse dürfen eine Grenze von 2 µSv/h (0,2 mrem/h) nicht überschreiten.\n" +
"\\multicolumn{1}{|c|}{1} & \\multicolumn{1}{c|}{2} & \\multicolumn{1}{c|}{3} & \\multicolumn{1}{c|}{4} & \\multicolumn{1}{c|}{5} & \\multicolumn{1}{c|}{6} & \\multicolumn{1}{c|}{7} & \\multicolumn{1}{c|}{8} & \\multicolumn{1}{c|}{9} & \\multicolumn{1}{c|}{10} & \\multicolumn{1}{c|}{11} \\\\\\hline";
    private static final String TEX_TABLE_END = "\\end{longtable}\n" +
"\n" +
"\\textbf{Išvados}: \\\\\n" +
"1) introskopai tinka naudojimui, darbuotojų ir gyventojų radiacinė sauga užtikrinama;\\\\";
    private static final String TEX_END = "\\vspace{1cm}\n" +
"\n" +
"Tikrino: Inžinerijos ir eksploatacijos skyriaus inžinierius-automatikas \\hspace{1cm} \\rule{8cm}{0.4pt} \\hfill Antanas Kvietkauskas\n" +
"\n" +
"\\end{document}";
    private static final String SELECT = "SELECT i.Pavadinimas, i.Nr, i.Rezimas, v.Pavadinimas, d.Operat, d.Pries, d.Uz, d.Virsuj, d.Generat, d.metras FROM Introskopai i LEFT JOIN JSGvietos v ON i.Vieta = v.Pavadinimas LEFT JOIN Dozimetrija d ON i.Nr = d.Introskopas WHERE d.Data = ?";
    private static final String PDF_FILE = "Matavimai";
    private static final String PDF_DIR = "/home/a/dasDokument/dasSchreiben/derStrahlenschutz/dieAngaben/2022/";    
    
    GridBagConstraints gbc;
    
    JMyButton btPrint;
    JScrollPane scrTa;
    JMyTextArea taIsvados;
    JMyTextField tfDVSData, tfDVSNr, tfDozim, tfDozimData, tfPatikrNr, tfData;
    JPanel panelButtons;

    public Ataskaita_RSC(ConnectionEquipment the_connection, int size) {
        super(the_connection, size);
    }

    @Override
    protected void init_components() {
	setLayout(new BorderLayout());
	createTopPanel();
	add(panelTop, BorderLayout.NORTH);
	createTable();
	add(spTable, BorderLayout.CENTER);
	lMessage = new JLabelLeft(fontsize);
	add(lMessage, BorderLayout.SOUTH);
	setVisible(true);
//	tfMonth.requestFocus();
//	tfMonth.setCaretPosition(5);
    }

    @Override
    protected void createTopPanel() {
	panelTop = new JPanel(new GridBagLayout());
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.insets = new Insets(0, 0, 5, 5);
// Η πρώτη σειρά
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0;
        panelTop.add(new JLabelRechts("DVS data:", fontsize), gbc);        
        tfDVSData = new JMyTextField("20--", fontsize);
	gbc.weightx = 0.25;
	gbc.gridx = 1;
        panelTop.add(tfDVSData, gbc);
        
	gbc.gridx = 2;
	gbc.weightx = 0;
        panelTop.add(new JLabelRechts("DVS Nr:", fontsize), gbc);        
        tfDVSNr = new JMyTextField(" Nr. 6R-", fontsize);
        tfDVSNr.setToolTipText("7.35 Radiacinės saugos dokumentai");
	gbc.weightx = 0.25;
	gbc.gridx = 3;
        panelTop.add(tfDVSNr, gbc);
        
	gbc.gridx = 4;
	gbc.weightx = 0;
        panelTop.add(new JLabelRechts("Dozimetras:", fontsize), gbc);        
        tfDozim = new JMyTextField("DKS-AT 1121, Nr. 44129", fontsize);
	gbc.weightx = 0.25;
	gbc.gridx = 5;
        panelTop.add(tfDozim, gbc);
        
	gbc.gridx = 6;
	gbc.weightx = 0;
        panelTop.add(new JLabelRechts("Patikros data:", fontsize), gbc);        
        tfDozimData = new JMyTextField("20--", fontsize);
	gbc.weightx = 0.25;
	gbc.gridx = 7;
        panelTop.add(tfDozimData, gbc);
        
	gbc.gridx = 8;
	gbc.weightx = 0;
        panelTop.add(new JLabelRechts("Patikros Nr.:", fontsize), gbc);        
        tfPatikrNr = new JMyTextField(fontsize);
	gbc.weightx = 0.25;
	gbc.gridx = 9;
        panelTop.add(tfPatikrNr, gbc);
        
	gbc.gridx = 10;
	gbc.weightx = 0;
        panelTop.add(new JLabelRechts("Data:", fontsize), gbc);        
        tfData = new JMyTextField("20--", fontsize);
	gbc.weightx = 0.25;
	gbc.gridx = 11;
        panelTop.add(tfData, gbc);
        
// Η δεύτερη σειρά
	gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 12;
        taIsvados = new JMyTextArea(4, 80, fontsize);
        taIsvados.setLineWrap(true);
        taIsvados.setWrapStyleWord(true);
        taIsvados.setMinimumSize(new Dimension(4, 80));
        taIsvados.setToolTipText("Generator is on the side without door; Login: 004/94866. For 7085A:main menu 15937, service menu 14123, setup menu 94866.");
        panelTop.add(new JScrollPane(taIsvados), gbc);

// Η τρύτη σειρά
        panelButtons = new JPanel();
        btSelect = new JMyButton("Rodyti", fontsize);
	btSelect.setActionCommand("select");
	btSelect.addActionListener(this);
	panelButtons.add(btSelect);
	btPDF = new JMyButton("PDF", fontsize);
	btPDF.setActionCommand("pdf");
	btPDF.addActionListener(this);
	panelButtons.add(btPDF);
        btPrint = new JMyButton("Spausdinti", fontsize);
	btPrint.setActionCommand("print");
	btPrint.addActionListener(this);
	panelButtons.add(btPrint);
	gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 12;
        panelTop.add(panelButtons, gbc);
        
    }

    @Override
    protected void createTable() {
	tableModel = new DefaultTableModel(new Object[]{"Eil. Nr.","Pavadinimas", "Nr.", "Režimas", "Vieta", "Operat.", "Prieš tun.", "Už tun.", "Viršuje", "Prie generat.", "1 m"}, 0);
	table = new JTable(tableModel);
	table.setFont(font);
	table.getTableHeader().setFont(font);
//	table.setDefaultEditor(Object.class, null);
//        table.addMouseListener(this);
//        table.setToolTipText("Dvigubas spragtelėjimas išfiltruoja susijusius įrašus");
	table.setAutoCreateRowSorter(true);
	setColumnsWidths();
//	table.getSelectionModel().addListSelectionListener(this);
//	tfMonth.requestFocus();
	spTable = new JScrollPane(table);
    }

    @Override
    protected void setColumnsWidths() {
	TableColumn column;
	for (int i = 0; i < table.getColumnCount(); i++) {
	    column = table.getColumnModel().getColumn(i);
//	    column.setPreferredWidth(200);
	    switch (i) {
		case 1:
//		    column.setMaxWidth(100);
		    column.setPreferredWidth(300);
		    break;
		case 3:
//                    column.setMaxWidth(60);
		    column.setPreferredWidth(200);
		    break;
		case 4:
//                    column.setMaxWidth(60);
		    column.setPreferredWidth(200);
		    break;
	    }
	}
    }

    @Override
    protected void showResults() {
	ResultSet resultset;
	resultset = null;
        
	if (tfDozimData.getText().length() == 10) {
	    try {
		preparedSelect = connection.prepareStatement(SELECT);
		preparedSelect.setString(1, tfDozimData.getText());
		resultset = preparedSelect.executeQuery();
		fillTable(resultset, "Dozimetrinių matavimų protokolas");
		preparedSelect.close();
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	    }
            if (resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
                }
            }
	}  else {
                JOptionPane.showMessageDialog(this, "Ίσως η ημερομηνία δεν είναι σωστή" + tfDozimData.getText().length(), "Problema!", JOptionPane.ERROR_MESSAGE);
            }
    }
    
    @Override
    protected void fillTable(ResultSet rs, String caption) throws SQLException {
	int i, n, colcount;
	Object[] row;
	colcount = tableModel.getColumnCount();
	row = new Object[colcount];
        n = 1;
	while (rs.next()) {
            for (i = 1; i < colcount; i++) {
                row[i] = rs.getObject(i);
            }
            row[0] = n;
            n++;
            tableModel.addRow(row);
	}
    }
    
    
    @Override
    protected void create_pdf() {
	StringBuilder sb;
        String pdf_name, pdf_pathname;
        Runtime r;
        Process pr;
        Datum date;
	int res, colcount, rowcount, row, col;
        date = new Datum();
        pdf_name = PDF_FILE + date.getDate();
	String[] run_pdflatex = {"pdflatex", "-synctex=1", "-interaction=nonstopmode", pdf_name.concat(".tex")};
	res = -1;
	rowcount = table.getRowCount();
	colcount = tableModel.getColumnCount();
	sb = new StringBuilder(TEX_0);
        r = Runtime.getRuntime();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            for (row = 0; row < rowcount; row++) {
                for (col = 0; col < colcount - 1; col++) {
                    sb.append(table.getValueAt(row, col)).append(" & ");
                }
                sb.append(table.getValueAt(row, col));
                sb.append("\\\\\\hline\n");
            }
//		sb.append(TEX_END);
            saveFile(pdf_name.concat(".tex"), sb.toString());
            pr = r.exec(run_pdflatex);
            pr.waitFor();
//		pr.waitFor(3, TimeUnit.SECONDS);
            res = pr.exitValue();
            pr.destroy();

        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        }
        pdf_pathname = PDF_DIR +  pdf_name + ".pdf";
        if (JOptionPane.showConfirmDialog(this, pdf_name + " rezultatas: " + String.valueOf(res) + ". Kopijuoti į " + PDF_DIR + " ?", "Failas", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                pdf_pathname = JOptionPane.showInputDialog(this, "Failo vardas", pdf_pathname);
                Files.copy(Paths.get(pdf_name + ".pdf"), Paths.get(pdf_pathname));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex, "Klaida", JOptionPane.ERROR_MESSAGE);
            }
        }
        delete_temp_files();
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	String command;
	command = ae.getActionCommand();
	switch (command) {
	    case "select":
		showResults();
		break;
	    case "print":
//		print_file();
		break;
	    case "pdf":
		create_pdf();
		break;
	}
    }

}
