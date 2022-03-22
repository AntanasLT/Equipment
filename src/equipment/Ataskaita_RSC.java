/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.TextAlignment;
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

    private static final String SELECT = "SELECT i.Pavadinimas, i.Nr, i.Rezimas, v.Pavadinimas, d.Operat, d.Pries, d.Uz, d.Virsuj, d.Generat, d.metras FROM Introskopai i LEFT JOIN JSGvietos v ON i.Vieta = v.Pavadinimas LEFT JOIN Dozimetrija d ON i.Nr = d.Introskopas WHERE d.Data LIKE ?";
    private static final String[] PDF_TABLE_HEADER = new String[] {"Eil. Nr.", "Pavadinimas", "Gamyklinis numeris", "Darbo režimas", "Vieta", "Operat. darbo vieta", "Prieš tunelį", "Už tunelio", "Įrenginio viršuje", "Prie generatoriaus", "1 m atstumu nuo įrenginio"};
    private static final String PDF_FILE = "RSC/Matavimai";
    private static final String PDF_DIR = "/home/a/dasDokument/dasSchreiben/derStrahlenschutz/dieAngaben/2022/";
    private static final String PRIEDAS = "Radiologinių incidentų ir avarijų prevencijos ir padarinių likvidavimo tvarkos \n 2 Priedas";
    private static final String IMONE = "\n \n \n \n VALSTYBĖS ĮMONĖS LIETUVOS ORO UOSTŲ VILNIAUS FILIALAS \n Įmonės kodas 303316259, Rodūnios kelias 10A, Vilnius, tel. 8 5 273 9326, faks. 8 5 232 9122, e-paštas: info@vno.lt \n \n DOZIMETRINIŲ MATAVIMŲ PROTOKOLAS \n \n";
    private static final String ADRESAS = "Įrenginio buvimo adresas: keleivių terminalas (Rodūnios kelias 2, Vilnius) \n";
    private static final String DOZIM_PAVAD = "Matavimai atlikti prietaisu (tipas, gamykl. Nr.): ";
    private static final String DOZIM_PATIKROS_DATA = ", patikra atlikta ";
    private static final String DOZIM_PATIKROS_NR = ", patikros liudijimo Nr. ";
    private static final String MATAVIMU_DATA = "Matavimų data: ";
    private static final String FONT = "/home/a/.fonts/Palem-nm.ttf";
//    private static final String FONT = "/home/a/.fonts/PFHandbookProRegular.ttf";
    private static final String ISVADOS = "Išvada: įrenginys tinka naudojimui, darbuotojų ir gyventojų radiacinė sauga užtikrinama.\n";
    private static final String TIKRINO = "Tikrino: Inžinerijos ir eksploatacijos skyriaus inžinierius-automatikas \t \t Antanas Kvietkauskas";
    
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
//        tfData.requestFocus();
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
        taIsvados.setText(ISVADOS);
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
        try {
            preparedSelect = connection.prepareStatement(SELECT);
            preparedSelect.setString(1, "%" + tfData.getText() + "%");
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
//        
//	if (tfData.getText().length() == 10) {
//	}  else {
//                JOptionPane.showMessageDialog(this, "Ίσως η ημερομηνία δεν είναι σωστή: " + tfData.getText(), "Problema!", JOptionPane.ERROR_MESSAGE);
//            }
    }
    
    @Override
    protected void fillTable(ResultSet rs, String caption) throws SQLException {
	int i, n, colcount;
	Object[] row;
        tableModel.setRowCount(0);
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
        PdfWriter pdfwriter;
        Table pdftable;
        Cell cell;
        Paragraph par;
//        AreaBreak ab = new AreaBreak();
        String pdf_name, pdf_pathname;
        Datum date;
	int colcount, rowcount, row, col;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        final FontSet set = new FontSet();
        date = new Datum();
        pdf_name = PDF_FILE + date.getDate() + ".pdf";
        WriterProperties wp = new WriterProperties();
        wp.setPdfVersion(PdfVersion.PDF_2_0);
        try {
            pdfwriter = new PdfWriter(pdf_name, wp);
            PdfDocument pdfDoc = new PdfDocument(pdfwriter);
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
            Document doc = new Document(pdfDoc);
            set.addFont(FONT);
            doc.setFontProvider(new FontProvider(set));
            doc.setProperty(Property.FONT, new String[]{""});
            par = new Paragraph(IMONE);
            par.setTextAlignment(TextAlignment.CENTER);//.setSpacingRatio(2F);
            par.setCharacterSpacing(1F);
//            par.setFixedPosition(20, 500, 800);
            par.add(tfDVSData.getText()).add("\t").add("Nr. ").add(tfDVSNr.getText()).add("\n Vilnius \n");
            par.setMultipliedLeading(0.9F);
            doc.add(par);
            par = new Paragraph(PRIEDAS).setMultipliedLeading(0.9F);
//            par.setMultipliedLeading(5);
            par.setSpacingRatio(1F).setFontSize(10);
            par.setFixedPosition(600, 520, 220);
            doc.add(par);
            par = new Paragraph(ADRESAS);
            par.setTextAlignment(TextAlignment.LEFT);
            par.add(DOZIM_PAVAD).add(tfDozim.getText()).add(DOZIM_PATIKROS_DATA).add(tfDozimData.getText()).add(DOZIM_PATIKROS_NR).add(tfPatikrNr.getText()).add("\n");
            par.add(MATAVIMU_DATA).add(tfData.getText());
            doc.add(par);
            rowcount = table.getRowCount();
            colcount = tableModel.getColumnCount();
            pdftable = new Table(new float[]{25, 150, 80, 80, 140, 50, 50 ,50, 50, 50, 50});
            pdftable.setWidth(100);
            pdftable.setFixedLayout();
            pdftable.setTextAlignment(TextAlignment.CENTER);
            for (int i = 0; i < 5; i++) {
                cell = new Cell(2, 1).add(new Paragraph(PDF_TABLE_HEADER[i]));
                cell.setTextAlignment(TextAlignment.CENTER);
                pdftable.addCell(cell);
            } 
                cell = new Cell(1, 6).add(new Paragraph("Matavimų rezultatai µSv/h"));
                cell.setTextAlignment(TextAlignment.CENTER);
                pdftable.addCell(cell);
            for (int i = 5; i < PDF_TABLE_HEADER.length; i++) {
                cell = new Cell(1, 1).add(new Paragraph(PDF_TABLE_HEADER[i]));
                cell.setTextAlignment(TextAlignment.CENTER);
                pdftable.addCell(cell);
            } 
            for (col = 1; col <= colcount; col++) {
                cell = new Cell(1, 1).add(new Paragraph(String.valueOf(col)));
                cell.setTextAlignment(TextAlignment.CENTER).setFontSize(10);
                pdftable.addCell(cell);
            }
            for (row = 0; row < rowcount; row++) {
                pdftable.addCell(String.valueOf(row + 1));
                for (col = 1; col < colcount; col++) {
                    pdftable.addCell(String.valueOf(table.getValueAt(row, col))).setTextAlignment(TextAlignment.LEFT);
                }
            }
            doc.add(pdftable);
            par = new Paragraph(ISVADOS).addTabStops().add(TIKRINO);
            doc.add(par);
            doc.close();
            pdf_pathname = PDF_DIR +  pdf_name + ".pdf";
            if (JOptionPane.showConfirmDialog(this, pdf_name + " kopijuoti į " + PDF_DIR + " ?", "Failas", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                pdf_pathname = JOptionPane.showInputDialog(this, "Failo vardas", pdf_pathname);
                Files.copy(Paths.get(pdf_name + ".pdf"), Paths.get(pdf_pathname));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex, "Klaida", JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    
    
//    @Override
//    protected void create_pdf() {
//	StringBuilder sb;
//        String pdf_name, pdf_pathname;
//        Runtime r;
//        Process pr;
//        Datum date;
//	int res, colcount, rowcount, row, col;
//        date = new Datum();
//        pdf_name = PDF_FILE + date.getDate();
//	String[] run_pdflatex = {"pdflatex", "-synctex=1", "-interaction=nonstopmode", pdf_name.concat(".tex")};
//	res = -1;
//	rowcount = table.getRowCount();
//	colcount = tableModel.getColumnCount();
//	sb = new StringBuilder(TEX_0);
//        r = Runtime.getRuntime();
//        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        try {
//            for (row = 0; row < rowcount; row++) {
//                for (col = 0; col < colcount - 1; col++) {
//                    sb.append(table.getValueAt(row, col)).append(" & ");
//                }
//                sb.append(table.getValueAt(row, col));
//                sb.append("\\\\\\hline\n");
//            }
////		sb.append(TEX_END);
//            saveFile(pdf_name.concat(".tex"), sb.toString());
//            pr = r.exec(run_pdflatex);
//            pr.waitFor();
////		pr.waitFor(3, TimeUnit.SECONDS);
//            res = pr.exitValue();
//            pr.destroy();
//
//        } catch (IOException | InterruptedException ex) {
//            JOptionPane.showMessageDialog(this, ex.toString(), "Klaida!", JOptionPane.ERROR_MESSAGE);
//        }
//        pdf_pathname = PDF_DIR +  pdf_name + ".pdf";
//        if (JOptionPane.showConfirmDialog(this, pdf_name + " rezultatas: " + String.valueOf(res) + ". Kopijuoti į " + PDF_DIR + " ?", "Failas", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//            try {
//                pdf_pathname = JOptionPane.showInputDialog(this, "Failo vardas", pdf_pathname);
//                Files.copy(Paths.get(pdf_name + ".pdf"), Paths.get(pdf_pathname));
//            } catch (IOException ex) {
//                JOptionPane.showMessageDialog(this, ex, "Klaida", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//        delete_temp_files();
//        setCursor(Cursor.getDefaultCursor());
//    }

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
