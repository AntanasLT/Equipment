/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package equipment;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import zurnalasP.Help;

/**
 *
 * @author a
 */
public class MainFrame extends JFrame implements ActionListener{
    

    String the_host, password, username;

    public ConnectionEquipment connection;
//    Message message;

    public JTabbedPane tabbedpane;
    
    ID_auto panelKontrahentai;
    ID_noauto panelStates, panelLocations, panelGen_busenos, panelWorktypes, panelJSG_vietos,panelEquipmentTypes, panelTPrusys, panelUsers, panelVeiklos;
    IDString panelPotinkliai;
    IDString_n panelSystems, panelIntroskopai, panelLiftai;
    ID_TextArea panelDarbeliai, panelDarbeliai_baigtieji, panelRSCdarbai;
    Biudzetas panelBiudzetas;
    Dozimetrija panelDozimetrija;
    Sutartys panelSutartys;
    Saskaitos panelSaskaitos;

    Darbai panelWorks;
    Turtas panelIT;
    TP panelTP;
    Ataskaita_liftai fr_ataskaitos_liftai;
    Ataskaita_RSC_matavimai fr_matavimu_protokolas;
    Ataskaita_RSC_sarasas fr_RSC_sarasas;
    Liftu_darbai panelLiftu_darbai;
    Tinklai panelAdresai;
    JSG panelJSG;
    Help frHelp;
    protected About frAbout;
    
// –––––––––––––––––––––––   
    public JMenuBar menu_bar;
// –––––––––––––––––––––––   
    public JMyMenuItem miConnect, miDisconnect, miBarcode, miExportIT, miLifu_prastovos, miDoziMatav, miInventorizacija, miCloseAll, miPlatus, miAtstata, miHelp, miHelp_biudzetas, miAbout;
// –––––––––––––––––––––––
    public JMyMenu menuTabs, menuRSC, menuAtaskRSC, menuPagalbines, menuDatabase, menuIT, menuBuhalterija, menuIreginiai, menuAtaskaitos, menuLiftai, menuTinklai, menuVaizdas, menuHelp;
//    JMenuItem dasMenuePunkt_dieKoerperangaben;
    public JMyCheckBoxMenuItem mcbWorks,  
            mcbLiftai, mcbLiftu_darbai, 
            mcbAdresai, mcbPotinkliai, 
            mcbGenerators, mcbGeneratorStates, mcbIntroscopes, mcbDozimetrija, mcbJSG_vietos, mcbRSCdarbai,
            mcbIT, mcbVeiklos, mcbDarbeliai, mcbDarbeliai_baigtieji, 
            mcbUsers, mcbSystems, mcbWorktypes, mcbEquipmentTypes, mcbStates, mcbLocations,
            mcbBiudzetas, mcbIslaidos, mcbSaskaitos, mcbSutartys, mcbKontrahentai, 
            mcbTP, mcbTPrusys;
// –––––––––––––––––––––––
    
    DialogPassword dialogPassword;
    protected JLabelLeft labelMessage;
// –––––––––––––––––––––––
    public int fontsize;
    private Dimension size0;
// –––––––––––––––––––––––

    protected MainFrame(String host, int size) {
        fontsize = size;
        the_host = host;
	connection = null;
//	panelOutlays = new Accounts(connection);
    }
	
    public void init() {
	password = "";
	username = "";
        UIManager.put("OptionPane.cancelButtonText", "Atšaukti");
        UIManager.put("OptionPane.okButtonText", "Gerai");
	setLayout(new BorderLayout());
	tabbedpane = new JMyTabbedPane(fontsize);
//	init();
//        panelWorks = new Works(connection);
//	tabbedpane.addTab("Darbai", panelWorks);
	add(tabbedpane, BorderLayout.CENTER);
	labelMessage = new JLabelLeft(fontsize);
	add(labelMessage, BorderLayout.SOUTH);
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		disconnect();
		System.exit(0);
	    }
	});
	connect_Equipment();
	javax.swing.UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.PLAIN, fontsize)));
	size0 = this.getBounds().getSize();
    }
    
    protected JMenuBar menuBar() {
	menu_bar = new JMenuBar();
//  _______________Duombazė_______________
	menuDatabase = new JMyMenu("Duombazė", fontsize);
	miConnect = new JMyMenuItem("Prisijungti", fontsize);
	miConnect.addActionListener(this);
	miConnect.setActionCommand("connect");
	miDisconnect = new JMyMenuItem("Atsijungti", fontsize);
	miDisconnect.setActionCommand("disconnect");
        miDisconnect.setName("disconnect");
	miDisconnect.addActionListener(this);
	menuDatabase.add(miConnect);
	menuDatabase.add(miDisconnect);
        menuDatabase.setName("Duombazė");
	menu_bar.add(menuDatabase);
//	menuData.addSeparator();

//  _______________Kortelės_______________
	menuTabs = new JMyMenu("Kortelės", fontsize);
        mcbWorks = new JMyCheckBoxMenuItem("Darbai", fontsize);
        mcbWorks.setName("Darbai");
	mcbWorks.addActionListener(this);
	mcbWorks.setActionCommand("works");
        menuTabs.setName("Kortelės");
	menuTabs.add(mcbWorks);
	
// _______________Liftai_______________
//	menuTabs.addSeparator();
        menuLiftai = new JMyMenu("Liftai", fontsize);
        mcbLiftai = new JMyCheckBoxMenuItem("Liftai", fontsize);
        mcbLiftai.addActionListener(this);
        mcbLiftai.setActionCommand("liftai");
        menuLiftai.add(mcbLiftai);
        mcbLiftu_darbai = new JMyCheckBoxMenuItem("Liftų darbai", fontsize);
        mcbLiftu_darbai.addActionListener(this);
        mcbLiftu_darbai.setActionCommand("liftu_darbai");
        menuLiftai.add(mcbLiftu_darbai);
	menuTabs.add(menuLiftai);
        
// _______________Tinklai_______________
        menuTinklai = new JMyMenu("Tinklai", fontsize);
        mcbAdresai = new JMyCheckBoxMenuItem("Adresai", fontsize);
        mcbAdresai.addActionListener(this);
        mcbAdresai.setActionCommand("adresai");
        menuTinklai.add(mcbAdresai);
        mcbPotinkliai = new JMyCheckBoxMenuItem("Potinkliai", fontsize);
        mcbPotinkliai.addActionListener(this);
        mcbPotinkliai.setActionCommand("potinkliai");
        menuTinklai.add(mcbPotinkliai);
	menuTabs.add(menuTinklai);
        
// _______________RSC______________
//	menuTabs.addSeparator();
        menuRSC = new JMyMenu("RSC", fontsize);
	mcbGenerators = new JMyCheckBoxMenuItem("JSG", fontsize);
	mcbGenerators.addActionListener(this);
	mcbGenerators.setActionCommand("generators");
        menuRSC.add(mcbGenerators);
	mcbIntroscopes = new JMyCheckBoxMenuItem("Introskopai", fontsize);
	mcbIntroscopes.addActionListener(this);
	mcbIntroscopes.setActionCommand("introscopes");
        menuRSC.add(mcbIntroscopes);
	mcbDozimetrija = new JMyCheckBoxMenuItem("Dozimetrija", fontsize);
	mcbDozimetrija.addActionListener(this);
	mcbDozimetrija.setActionCommand("dozimetrija");
        menuRSC.add(mcbDozimetrija);
	mcbRSCdarbai = new JMyCheckBoxMenuItem("Darbai", fontsize);
	mcbRSCdarbai.addActionListener(this);
	mcbRSCdarbai.setActionCommand("rsc_darbai");
        menuRSC.add(mcbRSCdarbai);
	mcbGeneratorStates = new JMyCheckBoxMenuItem("JSG būsenos", fontsize);
	mcbGeneratorStates.addActionListener(this);
	mcbGeneratorStates.setActionCommand("generatorStates");
        menuRSC.add(mcbGeneratorStates);
	mcbJSG_vietos = new JMyCheckBoxMenuItem("JSG vietos", fontsize);
	mcbJSG_vietos.addActionListener(this);
	mcbJSG_vietos.setActionCommand("jsg_vietos");
        menuRSC.add(mcbJSG_vietos);
        
        menuTabs.add(menuRSC);
// _______________Turtas_______________
//	menuTabs.addSeparator();
	menuIT = new JMyMenu("Turtas", fontsize);
	mcbIT = new JMyCheckBoxMenuItem("IT", fontsize);
	mcbIT.addActionListener(this);
	mcbIT.setActionCommand("it");
        menuIT.add(mcbIT);
	mcbVeiklos = new JMyCheckBoxMenuItem("Veiklos kodai", fontsize);
	mcbVeiklos.addActionListener(this);
        mcbVeiklos.setActionCommand("veiklos");
        menuIT.add(mcbVeiklos);
	miBarcode = new JMyMenuItem("Brūkšniai kodai", fontsize);
	miBarcode.addActionListener(this);
	miBarcode.setActionCommand("barcodes");
	menuIT.add(miBarcode);
	miExportIT = new JMyMenuItem("Į failą", fontsize);
	miExportIT.addActionListener(this);
	miExportIT.setActionCommand("exportIT");
	menuIT.add(miExportIT);
	menuTabs.add(menuIT);
// _______________Buhalterija_______________
        menuBuhalterija = new JMyMenu("Buhalterija", fontsize);
        mcbBiudzetas = new JMyCheckBoxMenuItem("Biudžetas", fontsize);
	mcbBiudzetas.addActionListener(this);
	mcbBiudzetas.setActionCommand("biudzetas");
        menuBuhalterija.add(mcbBiudzetas);        
        mcbIslaidos = new JMyCheckBoxMenuItem("Išlaidos", fontsize);
	mcbIslaidos.addActionListener(this);
	mcbIslaidos.setActionCommand("islaidos");
        menuBuhalterija.add(mcbIslaidos);
        mcbSaskaitos = new JMyCheckBoxMenuItem("Sąskaitos", fontsize);
	mcbSaskaitos.addActionListener(this);
	mcbSaskaitos.setActionCommand("saskaitos");
        menuBuhalterija.add(mcbSaskaitos);
        mcbSutartys = new JMyCheckBoxMenuItem("Sutartys", fontsize);
	mcbSutartys.addActionListener(this);
	mcbSutartys.setActionCommand("sutartys");
        menuBuhalterija.add(mcbSutartys);
        mcbKontrahentai = new JMyCheckBoxMenuItem("Kontrahentai", fontsize);
	mcbKontrahentai.addActionListener(this);
	mcbKontrahentai.setActionCommand("kontrahentai");
        menuBuhalterija.add(mcbKontrahentai);
        menuTabs.add(menuBuhalterija);
        
// ______________TP_______________
//	menuTabs.addSeparator();
        mcbTP = new JMyCheckBoxMenuItem("TP", fontsize);
        mcbTP.addActionListener(this);
        mcbTP.setActionCommand("tp");
        menuTabs.add(mcbTP);

// ______________Pagalbinės________________
	menuTabs.addSeparator();
        menuPagalbines = new JMyMenu("Pagalbinės", fontsize);
	mcbUsers = new JMyCheckBoxMenuItem("Vartotojai", fontsize);
	mcbUsers.addActionListener(this);
	mcbUsers.setActionCommand("users");
	menuPagalbines.add(mcbUsers);	
	mcbSystems = new JMyCheckBoxMenuItem("Sistemos", fontsize);
	mcbSystems.addActionListener(this);
	mcbSystems.setActionCommand("systems");
	menuPagalbines.add(mcbSystems);
        mcbWorktypes = new JMyCheckBoxMenuItem("Darbų rūšys", fontsize);
        mcbWorktypes.addActionListener(this);
        mcbWorktypes.setActionCommand("worktypes");
        menuPagalbines.add(mcbWorktypes);
        mcbEquipmentTypes = new JMyCheckBoxMenuItem("Įrangos tipai", fontsize);
        mcbEquipmentTypes.addActionListener(this);
        mcbEquipmentTypes.setActionCommand("equipmenttypes");
        menuPagalbines.add(mcbEquipmentTypes);
        mcbStates = new JMyCheckBoxMenuItem("Būsenos", fontsize);
        mcbStates.addActionListener(this);
        mcbStates.setActionCommand("states");
	menuPagalbines.add(mcbStates);
	mcbLocations = new JMyCheckBoxMenuItem("Vietos", fontsize);
        menuPagalbines.add(mcbLocations);
	mcbTPrusys = new JMyCheckBoxMenuItem("TP rūšys", fontsize);
	mcbTPrusys.addActionListener(this);
        mcbTPrusys.setActionCommand("tprusys");
        menuPagalbines.add(mcbTPrusys);        
        menuPagalbines.addSeparator();
        mcbDarbeliai = new JMyCheckBoxMenuItem("Darbeliai", fontsize);
	mcbDarbeliai.addActionListener(this);
        mcbDarbeliai.setActionCommand("darbeliai");
        menuPagalbines.add(mcbDarbeliai);        
        mcbDarbeliai_baigtieji = new JMyCheckBoxMenuItem("Darbeliai baigtieji", fontsize);
	mcbDarbeliai_baigtieji.addActionListener(this);
        mcbDarbeliai_baigtieji.setActionCommand("darbeliai_baigtieji");
        menuPagalbines.add(mcbDarbeliai_baigtieji);        
        menuTabs.add(menuPagalbines);
        menuTabs.addSeparator();
        miCloseAll = new JMyMenuItem("Uždaryti visas", fontsize);
        miCloseAll.addActionListener(this);
        miCloseAll.setActionCommand("closeAll");
        menuTabs.add(miCloseAll);
	menu_bar.add(menuTabs);
        
// _______________ Ataskaitos _______________
	menuAtaskaitos = new JMyMenu("Ataskaitos", fontsize);
	menuLiftai = new JMyMenu("Liftai, eskalatoriai", fontsize);
	miLifu_prastovos = new JMyMenuItem("Liftai ...", fontsize);
	miLifu_prastovos.addActionListener(this);
	miLifu_prastovos.setActionCommand("prastovos");
	menuLiftai.add(miLifu_prastovos);
	menuAtaskaitos.add(menuLiftai);
	miDoziMatav = new JMyMenuItem("Dozimetriniai matavimai", fontsize);
	miDoziMatav.addActionListener(this);
	miDoziMatav.setActionCommand("dozes");
	miInventorizacija = new JMyMenuItem("Inventorizacija", fontsize);
	miInventorizacija.addActionListener(this);
	miInventorizacija.setActionCommand("inventorizacija");
        menuAtaskRSC = new JMyMenu("RSC", fontsize);
        menuAtaskRSC.add(miDoziMatav);
        menuAtaskRSC.add(miInventorizacija);
	menuAtaskaitos.add(menuAtaskRSC);
        menuAtaskaitos.setName("Ataskaitos");
	menu_bar.add(menuAtaskaitos);

// _______________ Langas _______________
        menuVaizdas = new JMyMenu("Vaizdas", fontsize);
        miPlatus = new JMyMenuItem("Ekrano pločio", fontsize);
        miPlatus.addActionListener(this);
        miPlatus.setActionCommand("platus");
        menuVaizdas.add(miPlatus);
        miAtstata = new JMyMenuItem("Pločio atstata", fontsize);
        miAtstata.addActionListener(this);
        miAtstata.setActionCommand("atstata");
        menuVaizdas.add(miAtstata);
        menuVaizdas.setName("Vaizdas");
	menu_bar.add(menuVaizdas);
   
// __________Pagalba___________________
        menuHelp = new JMyMenu("Pagalba", fontsize);
        miHelp = new JMyMenuItem("Aprašymas", fontsize);
        miHelp.addActionListener(this);
        miHelp.setActionCommand("help");
        menuHelp.add(miHelp);
        miHelp_biudzetas = new JMyMenuItem("Biudžetas", fontsize);
        miHelp_biudzetas.addActionListener(this);
        miHelp_biudzetas.setActionCommand("help_biudzetas");
        menuHelp.add(miHelp_biudzetas);
        miAbout = new JMyMenuItem("Versija", fontsize);
        miAbout.addActionListener(this);
        miAbout.setActionCommand("about");
        menuHelp.add(miAbout);
        menuHelp.setName("Pagalba");
        menu_bar.add(menuHelp);

	return menu_bar;
    }

// _________ Οι γενικοί μεθόδοι ___________
    
    protected ConnectionEquipment connect(String database) {
	if (password.equals("") || connection == null) {
	    if (dialogPassword != null) {
		dialogPassword.dispose();
	    }
	    dialogPassword = new DialogPassword(this);
	    dialogPassword.pack();
	    dialogPassword.setVisible(true);
//	    password = "i--Logic15325";
//	    username = "Antanas";
	    password = dialogPassword.bekommeKennwort()[1];
	    username = dialogPassword.bekommeKennwort()[0];
	}
	connection = new ConnectionEquipment(the_host, database, username);
	try {
	    labelMessage.setText(connection.connect(password));
	} catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException | InstantiationException | SQLException | IllegalAccessException ex) {
	    connection = null;
	    password = "";
	    labelMessage.setText(ex.getMessage());
	}
	return connection;
    }

    protected void disconnect() {
	if (connection != null) {
	    labelMessage.setText(connection.disconnect());
	    connection = null;
	}
	    labelMessage.setText("Nuo duomenų bazės atsijungta");
//        else {
//	}
//	if (panelWorks != null) {
//	    panelWorks.disconnect();
//	    mcbWorks.setSelected(false);
//	    tabbedpane.remove(panelWorks);
//	    showWorks();
//	}
//	if (panelIT != null) {
//	    panelIT.disconnect();
//	    mcbIT.setSelected(false);
//	    tabbedpane.remove(panelIT);
//	    showIT();
//	}
//	if (panelEquipmentTypes != null) {
//	    panelEquipmentTypes.disconnect();
//	    mcbEquipmentTypes.setSelected(false);
//	    tabbedpane.remove(panelEquipmentTypes);
//	}
//	if (panelSystems != null) {
//	    panelSystems.disconnect();
//	    mcbSystems.setSelected(false);
//	    tabbedpane.remove(panelSystems);
//	}
//	if (panelUsers != null) {
//	    panelUsers.disconnect();
//	    mcbUsers.setSelected(false);
//	    tabbedpane.remove(panelUsers);
//	}
//	if (panelStates != null) {
//	    panelStates.disconnect();
//	    mcbStates.setSelected(false);
//	    tabbedpane.remove(panelStates);
//	}
//	if (panelWorktypes != null) {
//	    panelWorktypes.disconnect();
//	    mcbWorktypes.setSelected(false);
//	    tabbedpane.remove(panelWorktypes);
//	}
//	menuTabs.setVisible(false);
	password = "";
        closeAll();
    }

    public void setzt_dieMeldung(String dieMeldung) {
	labelMessage.setText(dieMeldung);
    }

    protected void connect_Equipment() {
//	if (menuTabs != null) {
//	    menuTabs.setVisible(true);
//	}
	if (connection == null) {
	    connection = connect("Equipment");
	}
//	if (panelIT != null) {
//	    panelIT.setConnection(connection);
//	}
//	if (panelEquipmentTypes != null) {
//	    panelEquipmentTypes.setConnection(connection);
//	}
//	if (panelSystems != null) {
//	    panelSystems.setConnection(connection);
//	}
//	if (panelUsers != null) {
//	    panelUsers.setConnection(connection);
//	}
//	if (panelWorktypes != null) {
//	    panelWorktypes.setConnection(connection);
//	}
//	if (panelWorks != null) {
//	    panelWorks.setConnection(connection);
//	}
//	if (panelLocations != null) {
//	    panelLocations.setConnection(connection);
//	}
    }

   
    public void showWorks() {
	if (mcbWorks.isSelected()) {
            if (panelWorks == null) {
                panelWorks = new Darbai(connection, fontsize);
                panelWorks.init();                
            } else {
                panelWorks.setVisible(true);
            }
	    tabbedpane.addTab("Darbai", panelWorks);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            panelWorks.setConnection(connection);
	}
	if (panelWorks != null & !mcbWorks.isSelected()) {
	    tabbedpane.remove(panelWorks);
	    panelWorks = null;
	}
    }
    
    private void showIT() {
	if (mcbIT.isSelected()) {
            if (panelIT == null) {
                panelIT = new Turtas(connection, fontsize);
                panelIT.init();
            } else {
                panelIT.setVisible(true);
            }
	    tabbedpane.addTab("IT", panelIT);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            panelIT.setConnection(connection);
	}
	if (panelIT != null & !mcbIT.isSelected()) {
	    tabbedpane.remove(panelIT);
	    panelIT = null;
	}        
    }
    
    private void showTP() {
	if (mcbTP.isSelected()) {
            if (panelTP == null) {
                panelTP = new TP(connection, fontsize);
                panelTP.init();               
            } else {
                panelTP.setVisible(true);
            }
	    tabbedpane.addTab("TP", panelTP);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            panelTP.setConnection(connection);
	}
	if (panelTP != null & !mcbTP.isSelected()) {
	    tabbedpane.remove(panelTP);
	    panelTP = null;
	}
    }
    
    private void showSaskaitos() {
	if (mcbSaskaitos.isSelected()) {
            if (panelSaskaitos == null) {
                panelSaskaitos = new Saskaitos(connection, fontsize);
                panelSaskaitos.init();                
            } else {
                panelSaskaitos.setVisible(true);
            }
	    tabbedpane.addTab("Saskaitos", panelSaskaitos);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            panelSaskaitos.setConnection(connection);
	}
	if (panelSaskaitos != null & !mcbSaskaitos.isSelected()) {
	    tabbedpane.remove(panelSaskaitos);
	    panelSaskaitos = null;
	}
    }
        
    private void showSutartys() {
	if (mcbSutartys.isSelected()) {
            if (panelSutartys == null) {
                panelSutartys = new Sutartys(connection, fontsize);
                panelSutartys.init();              
            } else {
                panelSutartys.setVisible(true);
            }
	    tabbedpane.addTab("Sutartys", panelSutartys);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            panelSutartys.setConnection(connection);
	}
	if (panelSutartys != null & !mcbSutartys.isSelected()) {
	    tabbedpane.remove(panelSutartys);
	    panelSutartys = null;
	}
        
    }

    
    /**
     *
     * @param tab JPanel to create
     * @param menuItem JMyCheckBoxMenuItem 
     * @param dbTable database table
     * @param tabName name of created tab
     */
    private ID_auto createTab_ID_auto(ID_auto tab, JMyCheckBoxMenuItem menuItem, String dbTable, String tabName) {
        if (menuItem.isSelected()) {
            if (tab == null) {
                tab = new ID_auto(connection, fontsize, dbTable);
                tab.init();               
            } else {
                tab.setVisible(true);
            }
            tabbedpane.addTab(tabName, tab);
            tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            tab.setConnection(connection);
        }
        if (!menuItem.isSelected()) {
            tabbedpane.remove(tab);
            tab = null;
        }
        return tab;
    }
    
    /**
     *
     * @param tab JPanel to create
     * @param menuItem JMyCheckBoxMenuItem 
     * @param dbTable database table
     * @param tabName name of created tab
     */
    private ID_noauto createTab_ID_noauto(ID_noauto tab, JMyCheckBoxMenuItem menuItem, String dbTable, String tabName) {
        if (menuItem.isSelected()) {
            if (tab == null) {
                tab = new ID_noauto(connection, fontsize, dbTable);
                tab.init();               
            } else {
                tab.setVisible(true);
            }
            tabbedpane.addTab(tabName, tab);
            tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            tab.setConnection(connection);
        }
        if (!menuItem.isSelected()) {
            tabbedpane.remove(tab);
            tab = null;
        }
        return tab;
    }
    
    private Biudzetas createTabBiudzetas(Biudzetas tab, JMyCheckBoxMenuItem menuItem, String dbTable, String tabName, String[] dbFields, String[] tbl_cols, int[] col_with) {
        if (menuItem.isSelected()) {
            if (tab == null) {
                tab = new Biudzetas(connection, fontsize, dbTable, dbFields, tbl_cols, col_with);
                tab.init();               
            } else {
                tab.setVisible(true);
            }
            tabbedpane.addTab(tabName, tab);
            tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            tab.setConnection(connection);
        }
        if (!menuItem.isSelected()) {
            tabbedpane.remove(tab);
            tab = null;
        }
        return tab;
        
    }
    
    private IDString createTab_IDString(IDString tab, JMyCheckBoxMenuItem menuItem, String dbTable, String tabName, String dbField1, String dbField2, String col1, String col2) {
        if (menuItem.isSelected()) {
            if (tab == null) {
                tab = new IDString(connection, fontsize, dbTable, dbField1, dbField2, col1, col2);
                tab.init();               
            } else {
                tab.setVisible(true);
            }
            tabbedpane.addTab(tabName, tab);
            tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            tab.setConnection(connection);
        }
        if (!menuItem.isSelected()) {
            tabbedpane.remove(tab);
            tab = null;
        }
        return tab;
    }

    private IDString_n createTab_IDString_n(IDString_n tab, JMyCheckBoxMenuItem menuItem, String dbTable, String tabName, String[] dbFields, String[] tbl_cols, int[] col_with, boolean id_auto_increment) {
        if (menuItem.isSelected()) {
            if (tab == null) {
                tab = new IDString_n(connection, fontsize, dbTable, dbFields, tbl_cols, col_with, id_auto_increment);
                tab.init();               
            } else {
                tab.setVisible(true);
            }
            tabbedpane.addTab(tabName, tab);
            tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            tab.setConnection(connection);
        }
        if (!menuItem.isSelected()) {
            tabbedpane.remove(tab);
            tab = null;
        }
        return tab;
    }
    
    private Dozimetrija createDozimetrija(Dozimetrija tab, JMyCheckBoxMenuItem menuItem, String dbTable, String tabName, String[] dbFields, String[] tbl_cols, int[] col_with, boolean id_auto_increment) {
        if (menuItem.isSelected()) {
            if (tab == null) {
                tab = new Dozimetrija(connection, fontsize, dbTable, dbFields, tbl_cols, col_with, id_auto_increment);
                tab.init();               
            } else {
                tab.setVisible(true);
            }
            tabbedpane.addTab(tabName, tab);
            tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            tab.setConnection(connection);
        }
        if (!menuItem.isSelected()) {
            tabbedpane.remove(tab);
            tab = null;
        }
        return tab;
    }
    
    
    private ID_TextArea createTab_IDTextArea(ID_TextArea tab, JMyCheckBoxMenuItem menuItem, String dbTable, String tabName, String[] dbFields, String[] tbl_cols, int[] col_with, boolean id_auto_increment, String taField, String docFolder) {
        if (menuItem.isSelected()) {
            if (tab == null) {
                tab = new ID_TextArea(connection, fontsize, dbTable, dbFields, tbl_cols, col_with, id_auto_increment, taField, new Dimension(70, 10), docFolder);
                tab.init();                
            } else {
                tab.setVisible(true);               
            }
            tabbedpane.addTab(tabName, tab);
            tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
            tab.setConnection(connection);
        }
        if (!menuItem.isSelected()) {
            tabbedpane.remove(tab);
            tab = null;
        }
        return tab;
    }
    

    protected void showHelp(String html_file) {
	    frHelp = new Help(html_file);
	    frHelp.setSize(600, 800);
	    frHelp.setTitle("Aprašymas");
//	if (frHelp == null) {
//	}
//	else {
//	    frHelp.setVisible(true);
//	}        
    }

    
    protected void showAbout() {
	if (frAbout == null) {
	    frAbout = new About();
	    frAbout.setSize(200, 100);
	    frAbout.setTitle("Apie");
	    frAbout.setResizable(false);
	    frAbout.setLocation(100, 100);
	}
	else {
	    frAbout.setVisible(true);
	}
    }

    private void showLiftu_ataskaitos() {
	if (fr_ataskaitos_liftai == null) {
	    fr_ataskaitos_liftai = new Ataskaita_liftai(connection, fontsize);
            fr_ataskaitos_liftai.init();
	    fr_ataskaitos_liftai.setSize(800, 600);
	    fr_ataskaitos_liftai.setTitle("Liftų prastovos ataskaita");
	} else {
	    fr_ataskaitos_liftai.setVisible(true);
	}
    }
    
    private void showLiftu_darbai() {
	if (panelLiftu_darbai == null & mcbLiftu_darbai.isSelected()) {
	    panelLiftu_darbai = new Liftu_darbai(connection, fontsize);
            panelLiftu_darbai.init();
	    tabbedpane.addTab("Liftų darbai", panelLiftu_darbai);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
        panelLiftu_darbai.setConnection(connection);
	if (panelLiftu_darbai != null & !mcbLiftu_darbai.isSelected()) {
	    tabbedpane.remove(panelLiftu_darbai);
	    panelLiftu_darbai = null;
	}
    }
    
    private void showAdresai() {
	if (panelAdresai == null & mcbAdresai.isSelected()) {
	    panelAdresai = new Tinklai(connection, fontsize);
            panelAdresai.init();
	    tabbedpane.addTab("Adresai", panelAdresai);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
        panelAdresai.setConnection(connection);
	if (panelAdresai != null & !mcbAdresai.isSelected()) {
	    tabbedpane.remove(panelAdresai);
	    panelAdresai = null;
	}
    }

    private void showJSG() {
	if (panelJSG == null & mcbGenerators.isSelected()) {
	    panelJSG = new JSG(connection, fontsize);
            panelJSG.init();
	    tabbedpane.addTab("JSG", panelJSG);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
        panelJSG.setConnection(connection);
	if (panelJSG != null & !mcbGenerators.isSelected()) {
	    tabbedpane.remove(panelJSG);
	    panelJSG = null;
	}
    }
    
    private void showMatavimuProtokolas() {
	if (fr_matavimu_protokolas == null) {
	    fr_matavimu_protokolas = new Ataskaita_RSC_matavimai(connection, fontsize);
            fr_matavimu_protokolas.init();
	    fr_matavimu_protokolas.setSize(1000, 800);
	    fr_matavimu_protokolas.setTitle("Dozimetrinių matavimų protokolas");
	} else {
	    fr_matavimu_protokolas.setVisible(true);
	}
        fr_matavimu_protokolas.tfData.requestFocusInWindow();
    }

    private void showRSCInventorizacija() {
	if (fr_RSC_sarasas == null) {
	    fr_RSC_sarasas = new Ataskaita_RSC_sarasas(connection, fontsize);
            fr_RSC_sarasas.init();
	    fr_RSC_sarasas.setSize(1000, 800);
	    fr_RSC_sarasas.setTitle("Inventorizacijos protokolas");
	} else {
	    fr_RSC_sarasas.setVisible(true);
	}
        fr_RSC_sarasas.tfData.requestFocusInWindow();
    }

    private void exportIT() {
        mcbIT.setSelected(true);
        showIT();
        panelIT.exportIT();
    }
    
    private void setWindow(boolean set_wide) {
	int w, w0;
	w = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	w0 = getWidth();
	if (set_wide) {
	    if (w0 < w - 50) {
		size0 = getSize();
	    }

	    setSize(w, getHeight());
	} else {
	    setSize(size0);
        }
    }
    
    private void closeAll() {
        int compCount;
        Component component;
        for (int i = 0; i < menuTabs.getMenuComponentCount(); i++) {
            Component comp = menuTabs.getMenuComponent(i);      
            if (comp instanceof JMyCheckBoxMenuItem) {
                ((JMyCheckBoxMenuItem) comp).setSelected(false);
            } else {
                if (comp instanceof JMyMenu) {
                    for (int j = 0; j < ((JMyMenu) comp).getMenuComponentCount(); j++) {
                        Component comp1 = ((JMyMenu) comp).getMenuComponent(j);
                        if (comp1 instanceof JMyCheckBoxMenuItem) {
                            ((JMyCheckBoxMenuItem) comp1).setSelected(false);
                        } 
                    }
                    
                }
            }
        }
        compCount = tabbedpane.getComponentCount();
        for (int i = 0; i < compCount; i++) {
            component = tabbedpane.getComponent(0);
            if (component instanceof JPanel) {
                tabbedpane.remove(component);
                panelWorks = null;
            }
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
	String command;
	command = e.getActionCommand();
	switch (command) {
//  Duombazė
	    case "connect":
		connect_Equipment();
		break;
	    case "disconnect":
		disconnect();
		break;
//  Kortelės                
	    case "works":
		connect_Equipment();
		showWorks();
		break;		
//      Liftai
            case "liftai":
		connect_Equipment();
		panelLiftai = createTab_IDString_n(panelLiftai, mcbLiftai, "Liftai", "Liftai", new String[]{"RegNr", "Pavadinimas", "GamNr", "Vieta", "DTP", "PTP", "Busena"}, new String[]{"Reg. Nr.", "Pavadinimas", "Gam. Nr.", "Vieta", "DTP", "PTP", "Būsena"}, new int[]{3*fontsize, 18*fontsize, 3*fontsize, 12*fontsize, 2*fontsize, 2*fontsize, 30*fontsize}, false );
		break;		
	    case "liftu_darbai":
		connect_Equipment();
		showLiftu_darbai();
		break;		
//      Tinklai
            case "adresai":
		connect_Equipment();
 		showAdresai();
		break;		
	    case "potinkliai":
		connect_Equipment();
 		panelPotinkliai = createTab_IDString(panelPotinkliai, mcbPotinkliai, "Potinkliai", "Potinkliai", "IP", "VLAN", "IP", "VLAN");
		break;		
//      RSC
            case "generators":
		connect_Equipment();
                showJSG();
		break;
	    case "introscopes":
		connect_Equipment();
                panelIntroskopai = createTab_IDString_n(panelIntroskopai, mcbIntroscopes, "Introskopai", "Introskopai", new String[]{"Nr", "Pavadinimas", "Vamzdziu_skc", "IT", "Vieta", "Rezimas", "Pastaba", "Yra"}, new String[]{"Nr", "Pavadinimas", "Vamzdžių skaičius", "IT", "Vieta", "Režimas", "Pastaba", "Yra"}, new int[]{100, 200, 50, 100, 200, 100, 600, 30}, false);
		break;		
	    case "rsc_darbai":
		connect_Equipment();
                panelRSCdarbai = createTab_IDTextArea(panelRSCdarbai, mcbRSCdarbai, "RSCdarbai", "Introskopų darbai", new String[]{"ID", "Data", "Vamzdis", "IntrNr", "DVS", "Pastaba"}, new String[]{"ID", "Data", "Vamzdis", "Introsk. Nr.", "DVS Nr.", "Pastaba"}, new int[]{10, 60, 150, 150, 100, 300}, true, "Pastaba", "RSC");
		break;		
	    case "dozimetrija":
		connect_Equipment();
                panelDozimetrija = createDozimetrija(panelDozimetrija, mcbDozimetrija, "Dozimetrija", "Dozimetrija", new String[]{"ID", "Data", "Introskopas", "Operat", "Pries", "Uz", "Virsuj", "Generat", "metras"}, new String[]{"ID", "Data", "Nr.", "Prie operat.", "Prieš tun.", "Už tun.", "Viršuj", "Prie generat.", "Už metro"}, new int[]{1*fontsize, 1*fontsize, 3*fontsize, 1*fontsize, 1*fontsize, 1*fontsize, 1*fontsize, 1*fontsize, 1*fontsize}, true);
		break;		
	    case "generatorStates":
		connect_Equipment();
                panelGen_busenos = createTab_ID_noauto(panelGen_busenos, mcbGeneratorStates, "Gen_busenos", "Generatorių būsenos");
		break;
	    case "jsg_vietos":
		connect_Equipment();
                panelJSG_vietos = createTab_ID_noauto(panelJSG_vietos, mcbJSG_vietos, "JSGvietos", "Introskopų vietos");
		break;
//      IT
            case "it":
		connect_Equipment();
		showIT();
		break;	
            case "veiklos":
		connect_Equipment();
		panelVeiklos = createTab_ID_noauto(panelVeiklos, mcbVeiklos, "Veiklos", "Veiklos");
		break;                
	    case "exportIT":
		connect_Equipment();
 		exportIT();
		break;		
	    case "barcodes":
		connect_Equipment();
		mcbIT.setSelected(true);
		showIT();
		panelIT.create_barcodes_file();
		break;		
//      Buhalterija
            case "biudzetas":
//                setWindow(true);
                panelBiudzetas = createTabBiudzetas(panelBiudzetas, mcbBiudzetas, "Biudzetas", "Biudžetas", new String[]{"Kodas", "Pavadinimas", "Sau", "Vas", "Kov", "Bal", "Geg", "Bir", "Lie", "Rgp", "Rgs", "Spa", "Lap", "Gru", "M", "Skyrius", "Metai"}, new String[]{"Kodas", "Pavadinimas", "Sausis", "Vasaris", "Kovas", "Balandis", "Gegužė", "Birželis", "Liepa", "Rugpjūtis", "Rugsėjis", "Spalis", "Lapkritis", "Gruodis", "Metams", "Skyrius", "Metai", "Faktas €", "Faktas %"}, new int[]{2*fontsize, 15*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, 2*fontsize, fontsize, fontsize, fontsize, fontsize} );
		break;
            case "islaidos":
//                setWindow(true);
                JOptionPane.showMessageDialog(this, "Bus apibendrinanti lentelė iš sąskaitų lentelės užklausos");
		break;
            case "saskaitos":
//                setWindow(true);
                showSaskaitos();
		break;
            case "sutartys":
//                setWindow(true);
                showSutartys();
		break;
            case "kontrahentai":
//                setWindow(true);
		connect_Equipment();
		panelKontrahentai = createTab_ID_auto(panelKontrahentai, mcbKontrahentai, "Kontrahentai", "Kontrahentai");
		break;                
//      Pagalbinės
            case "users":
		connect_Equipment();
		panelUsers = createTab_ID_noauto(panelUsers, mcbUsers, "Vartotojai", "Vartotojai");
		break;
	    case "systems":
		connect_Equipment();
		panelSystems = createTab_IDString_n(panelSystems, mcbSystems, "Sistemos", "Sistemos", new String[]{"ID", "Pavadinimas", "IT"}, new String[]{"ID", "Pavadinimas", "IT"}, new int[]{100, 800, 100}, false);
		break;		
	    case "worktypes":
		connect_Equipment();
		panelWorktypes = createTab_ID_noauto(panelWorktypes, mcbWorktypes, "Darbotipis", "Darbų rūšys");
		break;		
           case "equipmenttypes":
		connect_Equipment();
		panelEquipmentTypes = createTab_ID_noauto(panelEquipmentTypes, mcbEquipmentTypes, "IrangosTipai", "Įrangos tipai");
		break;
 	    case "states":
		connect_Equipment();
		panelStates = createTab_ID_noauto(panelStates, mcbStates, "Busenos", "Darbų būsenos");
		break;		
	    case "locations":
		connect_Equipment();
		panelLocations = createTab_ID_noauto(panelLocations, mcbLocations, "Vietos", "Vietos");
		break;		
	    case "tprusys":
		connect_Equipment();
		panelTPrusys = createTab_ID_noauto(panelTPrusys, mcbTPrusys, "TPrusys", "TP rūšys");
		break;             
	    case "darbeliai":
		connect_Equipment();
		panelDarbeliai = createTab_IDTextArea(panelDarbeliai, mcbDarbeliai, "Darbeliai", "Darbeliai", new String[]{"ID", "Data", "Data_baigta", "Darbas", "Baigtas"}, new String[]{"ID", "Data", "Užbaigta", "Darbas", "Baigtas"}, new int[]{fontsize, 2*fontsize, 2*fontsize, 70*fontsize, fontsize}, true, "Darbas", "Ivairus");
		break;	
	    case "darbeliai_baigtieji":
		connect_Equipment();
		panelDarbeliai_baigtieji = createTab_IDTextArea(panelDarbeliai_baigtieji, mcbDarbeliai_baigtieji, "Darbeliai_baigtieji", "Darbeliai baigtieji", new String[]{"ID", "Data", "Data_baigta", "Darbas", "Baigtas"}, new String[]{"ID", "Data", "Užbaigta", "Darbas", "Baigtas"}, new int[]{fontsize, 2*fontsize, 2*fontsize, 70*fontsize, fontsize}, true, "Darbas", "Ivairus");
		break;                
	    case "closeAll":
		closeAll();
		break;
                
//      TP
            case "tp":
		connect_Equipment();
		showTP();
		break;
//  Ataskaitos
            case "prastovos":
		connect_Equipment();
		showLiftu_ataskaitos();
		break;
            case "dozes":
		connect_Equipment();
		showMatavimuProtokolas();
		break;
            case "inventorizacija":
		connect_Equipment();
		showRSCInventorizacija();
		break;
//  Vaizdas
            case "platus":
                setWindow(true);
                break;
            case "atstata":
                setWindow(false);
                break;//  Pagalba
            case "help":
		showHelp("Aprasymas.html");
		break;
            case "help_biudzetas":
		showHelp("Biudzetas.html");
		break;            
	    case "about":
		showAbout();
		break;
	}
    }

}
    
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

  try { f.close(); } catch ( Exception e ) { }
  c.rewind();

––––––––––

  Reader f = null;
  String einwort, daswort_masse, daswort_dieenergie, daswort_diemuskelmasse, daswort_dieberechnungsmasse;
  String [] a;
  StringBuilder d;
  StringTokenizer st;
  d = new StringBuilder();
//  a = {"" ""};
  a = new String [4];
  daswort_masse = "DieGesamtmasse"; daswort_dieenergie = "DieEnergie";
  daswort_diemuskelmasse = "DieMuskelmasse"; daswort_dieberechnungsmasse = "DieBerechnungsmasse";
  try
  {
    f = new FileReader( "../Dateien/dieErnaehrung.dat" );
    for ( x=0; (x = f.read()) != -1 ; ) {
      d = d.append( (char) x );
    }
  }
  catch ( IOException e ) {
    a[0] = e.toString();
  }
  finally {
    try { f.close(); } catch ( Exception e ) { }


     */
