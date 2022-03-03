/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import datum.Datum;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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


    static final String SELECT_ARGIA = "SELECT pr.ID, pr.Irenginys, pr.Laikas, pab.Laikas, TIMESTAMPDIFF(MINUTE, pr.Laikas, pab.Laikas) FROM Darbai pr INNER JOIN Darbai pab WHERE pr.Laikas LIKE ? AND pr.Sistema = 7 AND (pr.IDpr != 0 AND pr.IDpr = pab.IDpr OR pr.IDpr = 0 AND pr.ID = pab.IDpr) AND pr.Busena = 6 AND pab.Busena = 3";
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
    

    PreparedStatement preparedSelect;

    
    int fontsize;
    Font font;
    ConnectionEquipment connection;
    DefaultTableModel tableModel;
    JTable table;
    JScrollPane spTable;
    JPanel panelButtons;
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
	createPanelButtons();
	add(panelButtons, BorderLayout.NORTH);
	createTable();
	add(spTable, BorderLayout.CENTER);
	lMessage = new JLabelLeft(fontsize);
	add(lMessage, BorderLayout.SOUTH);
	setVisible(true);
	tfMonth.requestFocus();
	tfMonth.setCaretPosition(5);
    }

    protected void createPanelButtons() {
	panelButtons = new JPanel();
	btClear = new JMyButton("Išvalyti", fontsize);
	btClear.setActionCommand("clear");
	btClear.addActionListener(this);
	panelButtons.add(btClear);
	tfMonth = new JMyTextField(String.valueOf(Year.now().getValue()) + "-%", 8, fontsize);
	panelButtons.add(tfMonth);
	btSelect = new JMyButton("Rodyti", fontsize);
	btSelect.setActionCommand("select");
	btSelect.addActionListener(this);
	panelButtons.add(btSelect);
	btPDF = new JMyButton("PDF", fontsize);
	btPDF.setActionCommand("pdf");
	btPDF.addActionListener(this);
	panelButtons.add(btPDF);
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
        float sum;
        DecimalFormat df;
	Object[] row;
        df = new DecimalFormat("0.0");
	sum = 0;
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
                        sum = sum + Float.parseFloat(String.valueOf(row[i]))/60;
                        row[i] = df.format(Float.parseFloat(String.valueOf(row[i]))/60);
                     }
		}
		tableModel.addRow(row);
		
	    }
	    for (i = 0; i <= colcount - 3; i++) {
		row[i] = "";
	    }
	    row[i] = "Suma (" + tfMonth.getText().substring(0, 7) + " (val.))";
	    row[i + 1] = df.format(sum);
	    tableModel.addRow(row);
    }
    

    private void clearTable() {
	tableModel.setRowCount(0);
    }
    
    private void create_pdf() {
	StringBuilder sb;
        String pdf, pdf_to;
        Runtime r;
        Process pr;
        Datum date;
	int res, colcount, rowcount, row, col;
        date = new Datum();
        pdf = PDF_FILE + date.getMonth_before();
	String[] run_pdflatex = {"pdflatex", "-synctex=1", "-interaction=nonstopmode", pdf.concat(".tex")};
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
		saveFile(pdf.concat(".tex"), sb.toString());
		pr = r.exec(run_pdflatex);
		pr.waitFor();
//		pr.waitFor(3, TimeUnit.SECONDS);
		res = pr.exitValue();
		pr.destroy();

	    } catch (IOException | InterruptedException ex) {
		JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
	    }
            pdf_to = "/home/a/dasDokument/dasSchreiben/dieAusschreibung/meine/dieFahrstuhle/2021/Ivairus/" + pdf + ".pdf";
	    if (JOptionPane.showConfirmDialog(this, pdf + " rezultatas: " + String.valueOf(res) + ". Kopijuoti į /home/a/dasDokument/dasSchreiben/dieAusschreibung/meine/dieFahrstuhle/2021/Ivairus/ ?", "Failas", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    pdf_to = JOptionPane.showInputDialog(this, "Failo vardas", pdf_to);
                    Files.copy(Paths.get(pdf + ".pdf"), Paths.get(pdf_to));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, ex, "Klaida", JOptionPane.ERROR_MESSAGE);
                }
            }
	    delete_temp_files();
	    setCursor(Cursor.getDefaultCursor());
	}
    }

    private void saveFile(String filename, String text) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(text);
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void delete_temp_files(){
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
