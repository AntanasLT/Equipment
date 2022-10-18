/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import datum.Datum;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.Year;
import javax.swing.JFrame;
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
public class Ataskaita_liftai extends JFrame implements ActionListener {


    static final String SELECT_ARGIA = "SELECT pr.ID, pr.Irenginys, pr.Laikas, pab.Laikas, TIMESTAMPDIFF(MINUTE, pr.Laikas, pab.Laikas) FROM Darbai pr INNER JOIN Darbai pab WHERE pr.Laikas LIKE ? AND pr.Sistema = 7 AND (pr.IDpr != 0 AND pr.IDpr = pab.IDpr OR pr.IDpr = 0 AND pr.ID = pab.IDpr) AND pr.Busena = 6 AND pab.Busena = 3 ORDER BY pr.Irenginys, pr.Laikas";
    static final String SELECT_AFIKSI = "SELECT pr.ID, pr.Irenginys, pr.Laikas, pab.Laikas, TIMESTAMPDIFF(MINUTE, pr.Laikas, pab.Laikas) FROM Darbai pr INNER JOIN Darbai pab WHERE pr.Laikas LIKE ? AND pr.Sistema = 7 AND pr.ID = pab.IDpr AND pr.Busena = 6 AND pab.Busena = 2";    
    static final String ID = "ID";
    static final String IRENGINYS = "Irenginys";
    static final String LAIKAS0 = "Nuo";
    static final String LAIKAS1 = "Iki";
    static final String PRASTOVA = "Valandos";
    private static final String TEX_START = "\\documentclass[a4paper,12pt]{article}\n" +
 "\\usepackage[left=5mm, top=5mm, bottom=5mm, right=5mm]{geometry}\n"
	    + "\\usepackage[utf8x]{inputenc}\n" +
"\\usepackage[lithuanian]{babel}\n" +
"\\usepackage[L7x]{fontenc}\n" +
 "\\usepackage{graphicx}\n" + "\\usepackage{multirow}\n"
	    + "\\usepackage{supertabular}\n"
	    + "\n" +
 "\\begin{document}\n "
	    + "\\centering\n \\ Liftų prastovos \\\\\\ \n Ataskaitos data: \\today \n \\\\ \\vspace{1em} \n \\tabletail{\\hline}\n"
	    + "\\begin{supertabular}{|p{1.2cm}|p{7.5cm}|p{4cm}|p{4cm}|p{1cm}|} \\hline\n"
	    + "\\multirow{2}{*}{ID} & \\multirow{2}{*}{Įrenginys} & \\multicolumn{2}{c|}{Gedimas} & \\multirow{2}{*}{Val.} \\\\\\cline{3-4}"
	    + "& & " + LAIKAS0 + " & " + LAIKAS1 + " & \\\\\\hline";
    private static final String TEX_END = "\n\\end{supertabular}\n"
	    + "\\end{document}\n";
    private static final String PDF_FILE = "Prastovos";
    private static final String PDF_DIR = "/home/a/dasDokument/dasSchreiben/dieAusschreibung/meine/dieFahrstuhle/2021/Ivairus/";
    

    PreparedStatement preparedSelect;

    
    int fontsize;
    Font font;
    ConnectionEquipment connection;
    DefaultTableModel tableModel;
    JTable table;
    JScrollPane spTable;
    JPanel panelTop;
    JLabelLeft lMessage;
    JMyTextField tfMonth;
    JMyButton btClear, btPDF, btSelect;
	
    public Ataskaita_liftai(ConnectionEquipment the_connection, int size) {
	fontsize = size;
	connection = the_connection;
//	init();
    }
    
    protected void init() {
        font = new Font("Arial", Font.PLAIN, fontsize);
	if (connection != null) {
	    init_components();
//	addWindowListener(new WindowAdapter() {
//	    @Override
//	    public void windowClosing(WindowEvent e) {
//		closeResultSet();
//		System.exit(0);
//	    }
//	});
	} else {
	    JOptionPane.showMessageDialog(this, "Nerisijungta!", "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }
    
    protected void init_components() {
	setLayout(new BorderLayout());
	createTopPanel();
	add(panelTop, BorderLayout.NORTH);
	createTable();
	add(spTable, BorderLayout.CENTER);
	lMessage = new JLabelLeft(fontsize);
	add(lMessage, BorderLayout.SOUTH);
	setVisible(true);
	tfMonth.requestFocus();
	tfMonth.setCaretPosition(5);
    }

    protected void createTopPanel() {
	panelTop = new JPanel();
	btClear = new JMyButton("Išvalyti", fontsize);
	btClear.setActionCommand("clear");
	btClear.addActionListener(this);
	panelTop.add(btClear);
	tfMonth = new JMyTextField(String.valueOf(Year.now().getValue()) + "-%", 8, fontsize);
	panelTop.add(tfMonth);
	btSelect = new JMyButton("Rodyti", fontsize);
	btSelect.setActionCommand("select");
	btSelect.addActionListener(this);
	panelTop.add(btSelect);
	btPDF = new JMyButton("PDF", fontsize);
	btPDF.setActionCommand("pdf");
	btPDF.addActionListener(this);
	panelTop.add(btPDF);
    }

    protected void createTable() {
	tableModel = new DefaultTableModel(new Object[]{ID, IRENGINYS, LAIKAS0, LAIKAS1, PRASTOVA}, 0);
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

    protected void setColumnsWidths() {
	TableColumn column;
	for (int i = 0; i < table.getColumnCount(); i++) {
	    column = table.getColumnModel().getColumn(i);
//	    column.setPreferredWidth(200);
	    switch (tableModel.getColumnName(i)) {
		case ID:
		    column.setMaxWidth(100);
		    column.setPreferredWidth(50);
		    break;
		case IRENGINYS:
//                    column.setMaxWidth(60);
		    column.setPreferredWidth(300);
		    break;
	    }
	}
    }

    protected void showResults() {
	ResultSet resultset;
	resultset = null;
	if (tfMonth.getText().length() == 8) {
	    try {
		preparedSelect = connection.prepareStatement(SELECT_ARGIA);
		preparedSelect.setString(1, tfMonth.getText());
		resultset = preparedSelect.executeQuery();
		fillTable(resultset, "Prastovos");
		preparedSelect.close();
		preparedSelect = connection.prepareStatement(SELECT_AFIKSI);
		preparedSelect.setString(1, tfMonth.getText());
		resultset = preparedSelect.executeQuery();
		fillTable(resultset, "Atvykimas");
		preparedSelect.close();
	    } catch (SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	    }
	    tfMonth.requestFocus();
	    tfMonth.setCaretPosition(5);
	if (resultset != null) {
	    try {
		resultset.close();
	    } catch (SQLException ex) {
	JOptionPane.showMessageDialog(this, ex.toString(), "Problema!", JOptionPane.ERROR_MESSAGE);
	    }
	}
	    
	}
    }
    
    protected void fillTable(ResultSet rs, String caption) throws SQLException {
	int i, colcount;
        String month;
        float sum_LF, sum_ES;
        DecimalFormat df;
	Object[] row;
        df = new DecimalFormat("0.0");
	sum_LF = 0; sum_ES = sum_LF;
	colcount = tableModel.getColumnCount();
	row = new Object[colcount];
	row[0] = "";
	row[1] = caption;
	for (i = 2; i < colcount; i++) {
		row[i] = "";
	    }
	tableModel.addRow(row);	
	while (rs.next()) {
		for (i = 0; i <= colcount - 1; i++) {
		    row[i] = rs.getObject(i + 1);
                    if (i == colcount - 1) {
                        if (String.valueOf(row[1]).contains("ES")) {
                            sum_ES = sum_ES + Float.parseFloat(String.valueOf(row[i]))/60;
                        } else {
                            sum_LF = sum_LF + Float.parseFloat(String.valueOf(row[i]))/60;
                        }
                        row[i] = df.format(Float.parseFloat(String.valueOf(row[i]))/60);
                     }
		}
		tableModel.addRow(row);
	    }
	    for (i = 0; i <= colcount - 3; i++) {
		row[i] = "";
	    }
            month = tfMonth.getText().substring(0, 7) + " mėn.)";
	    row[i] = "Eskalatoriai (val.): " + month;
	    row[i + 1] = df.format(sum_ES);
	    tableModel.addRow(row);            
	    row[i] = "Liftai (val.): " + month;
	    row[i + 1] = df.format(sum_LF);
	    tableModel.addRow(row);
    }
    

    protected void clearTable() {
	tableModel.setRowCount(0);
    }
    
    protected void create_pdf() {
	StringBuilder sb;
        String pdf_name, pdf_pathname;
        Runtime r;
        Process pr;
        Datum date;
	int res, colcount, rowcount, row, col;
        date = new Datum();
        pdf_name = PDF_FILE + date.getMonth_before();
	String[] run_pdflatex = {"pdflatex", "-synctex=1", "-interaction=nonstopmode", pdf_name.concat(".tex")};
	res = -1;
	rowcount = table.getRowCount();
	colcount = tableModel.getColumnCount();
	sb = new StringBuilder(TEX_START);
	if (JOptionPane.showConfirmDialog(this, "Sukurti pdf iš lentelės duomenų?", "Sukurti pdf?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
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
		sb.append(TEX_END);
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
    }

    protected void saveFile(String filename, String text) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(text);
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void delete_temp_files(){
        Runtime r;
        Process pr;
	String[] del_cmd = {"rm", "-f", "*ps"};
	r = Runtime.getRuntime();
	try {
	    pr = r.exec(del_cmd);
            pr.waitFor();
//	    pr.waitFor(1000, TimeUnit.MILLISECONDS);
	    JOptionPane.showMessageDialog(this, pr.exitValue());
	    pr.destroy();
	} catch (IOException | InterruptedException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	}
    }

    protected void openFile(String folder, String filename) {
        if (filename != null) {
            try {
                if (filename.startsWith("http") || filename.contains("www")) {
                    Desktop.getDesktop().browse(new URL(filename).toURI());
                } else {
                    File file = new File(folder, filename);
                    if (file.exists()) {
    //                    if (filename.endsWith("txt")) {
    //                        Desktop.getDesktop().edit(file);
    //                    }
                        Desktop.getDesktop().open(file);
                    } else {
                        throw new IOException(filename + ": nėra!");
                    }
                }
            } catch (IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    @Override
    public void actionPerformed(ActionEvent ae) {
	String command;
	command = ae.getActionCommand();
	switch (command) {
	    case "select":
		showResults();
		break;
	    case "clear":
		clearTable();
		break;
	    case "pdf":
		create_pdf();
		break;
	}
    }

}
