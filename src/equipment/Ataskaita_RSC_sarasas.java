/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipment;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
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
import static equipment.Ataskaita_RSC_matavimai.FONT;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Ataskaita_RSC_sarasas extends Ataskaita_RSC_matavimai {
    
    private static final String SELECT = "SELECT IFNULL (i.Pavadinimas, '–'), IFNULL (i.Nr, '–'), IFNULL (i.Vamzdziu_skc, '–') AS skc, g.Pavadinimas, g.Nr, g.Vamzdzio_pavad, g.Vamzdzio_nr, b.Pavadinimas FROM Introskopai i RIGHT JOIN JSG g ON g.Introskopas = i.Nr RIGHT JOIN Gen_busenos b ON g.Busena = b.ID WHERE (b.Pavadinimas LIKE '%augom%' OR b.Pavadinimas LIKE '%audojam%') AND g.Nr IS NOT NULL ORDER BY i.Pavadinimas, i.Nr";
    private static final String[] PDF_TABLE_HEADER = new String[] {"Prietaiso pavadinimas", "Prietaiso gamyklinis numeris", "Vamzdžių skč.", "Generatoriaus pavadinimas", "Generatoriaus gamyklinis numeris", "Vamzdžio pavadinimas", "Vamzdžio gamyklinis numeris", "Prietaiso būklė (naudojamas/saugomas)"};
    private static final String DIR = "RSC";
    private static final String FILENAME = "Sarasas";
    private static final String PDF_DIR = "dasDokument/dasSchreiben/derStrahlenschutz/dieAngaben/";
    private static final String PRIEDAS = "Duomenų apie jonizuojančiosios spinduliuotės šaltinius ir\n" +
"darbuotojus, dirbančius su jonizuojančiosios spinduliuo-\n" +
"tės šaltiniais, pateikimo Valstybės jonizuojančiosios spin-\n" +
"duliuotės šaltinių ir darbuotojų apšvitos registrui tvarkos\n" +
"aprašo 7 priedas";
    private static final String IMONE = "\n \n \n \n\n VALSTYBĖS ĮMONĖS LIETUVOS ORO UOSTŲ VILNIAUS FILIALAS \n Įmonės kodas 303316259, Rodūnios kelias 10A, Vilnius, tel. 8 5 273 9326, faks. 8 5 232 9122, e-paštas: info@vno.lt \n \n";
    private static final String ANTRASTE = "METINĖS INVENTORIZACIJOS DUOMENYS \n APIE TURIMUS JONIZUOJANČIOSIOS SPINDULIUOTĖS GENERATORIUS IR DALELIŲ GREITINTUVUS \n \n";
    private static final String LICENCIJA = "Veiklai išduota licencija: 0630 \t \t \t \t Padalinio pavadinimas: Terminalas, Rodūnios kelias 2, Vilnius) \n";
    private static final String ATSAKINGAS = "Asmuo, atsakingas už radiacinę saugą: \t Jevgenij Ivanov ( 868737912, j.ivanov@ltou.lt) \n";
//    private static final String FONT = "/home/a/.fonts/PFHandbookProRegular.ttf";
    private static final String ISVADOS = "" ;
    private static final String TIKRINO = "Duomenis surašė Inžinerijos ir eksploatacijos skyriaus inžinierius-automatikas \t \t Antanas Kvietkauskas";
    private static final String[] TABLE_HEADER = new String[] {"Eil. Nr.", "Pavadinimas","Introsk. gam. Nr.", "Vamzdžiai", "Generatorius", "Generat. Nr.", "Vamzdis", "Vamzdžio Nr.", "Būsena"};
    
    String licencija, atsakingas;
    
    public Ataskaita_RSC_sarasas(ConnectionEquipment the_connection, int size) {
        super(the_connection, size);
    }

    @Override
    protected void set_constants() {
        select = SELECT;
        pdf_table_header = PDF_TABLE_HEADER;
        dir = DIR;
        filename = FILENAME;
        pdf_dir = PDF_DIR;
        priedas = PRIEDAS;
        imone = IMONE;
        antraste = ANTRASTE;
        licencija = LICENCIJA;
        atsakingas = ATSAKINGAS;
        isvados = ISVADOS;
        tikrino = TIKRINO;
        table_header = TABLE_HEADER;
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
        panelTop.add(new JLabelRechts("Teikiami", fontsize), gbc);        
        tfData = new JMyTextField("20", fontsize);
	gbc.weightx = 0.25;
	gbc.gridx = 5;
        panelTop.add(tfData, gbc);
	gbc.gridx = 6;
	gbc.weightx = 0;
        panelTop.add(new JLabelRechts("metų duomenys", fontsize), gbc);        
       

// Η τρύτη σειρά
        create_Buttons();
    }

    @Override
    protected void setColumnsWidths() {
	TableColumn column;
	for (int i = 0; i < table.getColumnCount(); i++) {
	    column = table.getColumnModel().getColumn(i);
	    column.setPreferredWidth(100);
	    switch (i) {
                case 0:
		    column.setPreferredWidth(50);
		    break;                    
		case 1:
//		    column.setMaxWidth(100);
		    column.setPreferredWidth(300);
		    break;
		case 3:
//                    column.setMaxWidth(60);
		    column.setPreferredWidth(50);
		    break;
//		case 4:
////                    column.setMaxWidth(60);
//		    column.setPreferredWidth(200);
//		    break;
	    }
	}
    }
    
    @Override
    protected void showResults() {
	ResultSet resultset;
	resultset = null;
        try {
            preparedSelect = connection.prepareStatement(select);
//            preparedSelect.setString(1, "%" + tfData.getText() + "%");
            resultset = preparedSelect.executeQuery();
            fillTable(resultset, "Inventorizacijos duomenys");
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
    }
    
    @Override
    protected void create_pdf() {
        PdfWriter pdfwriter;
        Table pdftable;
        Cell cell;
        Paragraph par;
//        AreaBreak ab = new AreaBreak();
        String pdf_name, copy_to;
        Datum date;
	int colcount, rowcount, row, col;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        final FontSet set = new FontSet();
        date = new Datum();
        pdf_name = filename + "_" + date.getDate() + ".pdf";
        WriterProperties wp = new WriterProperties();
        wp.addXmpMetadata();
        wp.setPdfVersion(PdfVersion.PDF_1_7);
        try {
            pdfwriter = new PdfWriter(dir + System.getProperty("file.separator") + pdf_name, wp);
            PdfDocument pdfDoc = new PdfDocument(pdfwriter);
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
            PdfDocumentInfo info = pdfDoc.getDocumentInfo();
            info.setTitle("Dozimetrinių matavimų protokolas");
            info.setAuthor("Antanas Kvietkauskas");
            info.setSubject("Radiacinė sauga (DVS 7.35)");
            Document doc = new Document(pdfDoc);
            set.addFont(System.getProperty("user.home") + System.getProperty("file.separator") + FONT.replace('/', System.getProperty("file.separator").charAt(0)));
            
            doc.setFontProvider(new FontProvider(set));
            doc.setProperty(Property.FONT, new String[]{""});
            par = new Paragraph(imone);
            par.setTextAlignment(TextAlignment.CENTER);//.setSpacingRatio(2F);
            par.setCharacterSpacing(1F);
            par.add(antraste);
//            par.setFixedPosition(20, 500, 800);
            par.add(tfDVSData.getText()).add("\t").add("Nr. ").add(tfDVSNr.getText()).add("\n Vilnius \n");
            par.setMultipliedLeading(0.9F);
            doc.add(par);
            par = new Paragraph(priedas).setMultipliedLeading(0.9F);
//            par.setMultipliedLeading(5);
            par.setSpacingRatio(1F).setFontSize(10);
            par.setFixedPosition(560, 520, 260);
            doc.add(par);
            par = new Paragraph(licencija);
            par.setTextAlignment(TextAlignment.LEFT);
            par.add(atsakingas).add("Teikiami ").add(tfData.getText()).add(" metų duomenys:");
            doc.add(par);
            rowcount = table.getRowCount();
            colcount = tableModel.getColumnCount();
            pdftable = new Table(new float[]{150, 150, 30, 89, 89, 89, 89, 89});
            pdftable.setWidth(100);
            pdftable.setFixedLayout();
            pdftable.setTextAlignment(TextAlignment.CENTER);
            for (int i = 0; i < pdf_table_header.length; i++) {
                cell = new Cell(1, 1).add(new Paragraph(pdf_table_header[i]));
                cell.setTextAlignment(TextAlignment.CENTER);
                pdftable.addCell(cell);
            } 
//            for (col = 1; col <= colcount; col++) {
//                cell = new Cell(1, 1).add(new Paragraph(String.valueOf(col)));
//                cell.setTextAlignment(TextAlignment.CENTER).setFontSize(10);
//                pdftable.addCell(cell);
//            }
            for (row = 0; row < rowcount - 1; row++) {
//                pdftable.addCell(String.valueOf(row + 1));
                for (col = 1; col < colcount; col++) {
                    pdftable.addCell(String.valueOf(table.getValueAt(row, col))).setTextAlignment(TextAlignment.LEFT);
                }
            }
            doc.add(pdftable);
            par = new Paragraph(isvados).addTabStops().add(tikrino);
            doc.add(par);
            doc.close();
            copy_to = System.getProperty("user.home") + System.getProperty("file.separator") + pdf_dir.concat(tfData.getText().concat("/")).replace('/', System.getProperty("file.separator").charAt(0)) +  pdf_name;
            if (JOptionPane.showConfirmDialog(this, pdf_name + " kopijuoti į " + copy_to + " ?", "Failas", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                copy_to = JOptionPane.showInputDialog(this, "Failo vardas", copy_to);
                Files.copy(Paths.get(dir + System.getProperty("file.separator") + pdf_name), Paths.get(copy_to), StandardCopyOption.REPLACE_EXISTING); 
            }
            if (JOptionPane.showConfirmDialog(this, "Rodyti?", "Rodymas", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                openFile(dir, System.getProperty("file.separator") + pdf_name);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex, "Klaida", JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getDefaultCursor());
    }
    
    
}
