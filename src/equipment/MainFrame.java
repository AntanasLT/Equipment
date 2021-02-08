/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package equipment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.READ;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import zurnalasP.Help;

/**
 *
 * @author a
 */
public class MainFrame extends JFrame implements ActionListener{
    

    String the_host, password, username;

    public ConnectionEquipment connection;
    Message message;

    public JTabbedPane tabbedpane;
    Irangos_tipai panelEquipmentTypes;
    Sistemos panelSystems;
    Vartotojai panelUsers;
    Darbu_rusys panelWorktypes;
    Darbai panelWorks;
    Turtas panelIT;
    TP panelTP;
    Vietos panelLocations;
    Generatoriai panelGeneratoriai;
    Introskopai panelIntroskopai;
    Gen_busenos panelGen_busenos;
    Busenos panelStates;
    Veiklos panelVeiklos;
    Help frHelp;
    protected About frAbout;
    
// –––––––––––––––––––––––   
    public JMenuBar menu_bar;
// –––––––––––––––––––––––   
    public JMyMenu menuDatabase;
    public JMyMenuItem menuItem_connect, menItem_disconnect;
// –––––––––––––––––––––––
    public JMyMenu menuTabs, menuRSC;
//    JMenuItem dasMenuePunkt_dieKoerperangaben;
    public JMyCheckBoxMenuItem menuItemUsers, menuItemSystems, menuItemIT, menuItemWorks, menuItemStates, menuItemWorktypes, menuItemEquipmentTypes, menuItemTP, menuItemLocations,            menuItemGenerators, menuItemGeneratorStates, menuItemIntroscopes,
            
            menuItemCodes,
	    
	    menuItemContracts, menuItemAccounts,
            menuItemPartners, menuItemBudget, menuItemOrders;
// –––––––––––––––––––––––
    public JMyMenu menuIT;
    public JMyMenu menuIreginiai;
    public JMyMenuItem menuItemBarcode;
// –––––––––––––––––––––––    
    public JMyMenu menuHelp;
    public JMyMenuItem menuItemHelp, menuItemAbout;
// –––––––––––––––––––––––
    
    DialogPassword dialogPassword;
    protected JLabelLeft labelMessage;
// –––––––––––––––––––––––
    public int fontsize;
// –––––––––––––––––––––––

    protected MainFrame(String host, int size) {
        fontsize = size;
        the_host = host;
	connection = null;
	tabbedpane = new JMyTabbedPane(fontsize);
	init();
//        panelWorks = new Works(connection);
//	tabbedpane.addTab("Darbai", panelWorks);
	add(tabbedpane, BorderLayout.CENTER);
	add(labelMessage, BorderLayout.SOUTH);
//	panelOutlays = new Accounts(connection);
    }
	
    private void init() {
	password = "";
	username = "";
	setLayout(new BorderLayout());
	labelMessage = new JLabelLeft(fontsize);
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		disconnect();
		System.exit(0);
	    }
	});
	connect_Equipment();
    }
	
    protected JMenuBar menuBar() {
	menu_bar = new JMenuBar();
//  _______________Duombazė_______________
	menuDatabase = new JMyMenu("Duombazė", fontsize);
	menuItem_connect = new JMyMenuItem("Prisijungti", fontsize);
	menuItem_connect.addActionListener(this);
	menuItem_connect.setActionCommand("connect");
	menItem_disconnect = new JMyMenuItem("Atsijungti", fontsize);
	menItem_disconnect.setActionCommand("disconnect");
	menItem_disconnect.addActionListener(this);
	menuDatabase.add(menuItem_connect);
	menuDatabase.add(menItem_disconnect);
	menu_bar.add(menuDatabase);
//	menuData.addSeparator();

//  _______________Kortelės_______________
	menuTabs = new JMyMenu("Kortelės", fontsize);
        menuItemWorks = new JMyCheckBoxMenuItem("Darbai", fontsize);
	menuItemWorks.addActionListener(this);
	menuItemWorks.setActionCommand("works");
	menuTabs.add(menuItemWorks);
	menuItemUsers = new JMyCheckBoxMenuItem("Vartotojai", fontsize);
	menuItemUsers.addActionListener(this);
	menuItemUsers.setActionCommand("users");
	menuTabs.add(menuItemUsers);	
	menuItemSystems = new JMyCheckBoxMenuItem("Sistemos", fontsize);
	menuItemSystems.addActionListener(this);
	menuItemSystems.setActionCommand("systems");
	menuTabs.add(menuItemSystems);
        menuItemWorktypes = new JMyCheckBoxMenuItem("Darbų rūšys", fontsize);
        menuItemWorktypes.addActionListener(this);
        menuItemWorktypes.setActionCommand("worktypes");
        menuTabs.add(menuItemWorktypes);
        menuItemEquipmentTypes = new JMyCheckBoxMenuItem("Įrangos tipai", fontsize);
        menuItemEquipmentTypes.addActionListener(this);
        menuItemEquipmentTypes.setActionCommand("equipmenttypes");
        menuTabs.add(menuItemEquipmentTypes);
        menuItemStates = new JMyCheckBoxMenuItem("Būsenos", fontsize);
        menuItemStates.addActionListener(this);
        menuItemStates.setActionCommand("states");
	menuTabs.add(menuItemStates);
	menuItemLocations = new JMyCheckBoxMenuItem("Vietos", fontsize);
	menuItemLocations.addActionListener(this);
        menuItemLocations.setActionCommand("locations");
        menuTabs.add(menuItemLocations);
	
// _____________________________
	menuTabs.addSeparator();
        menuRSC = new JMyMenu("RSC", fontsize);
	menuItemGenerators = new JMyCheckBoxMenuItem("Generatoriai", fontsize);
	menuItemGenerators.addActionListener(this);
	menuItemGenerators.setActionCommand("generators");
        menuRSC.add(menuItemGenerators);
	menuItemIntroscopes = new JMyCheckBoxMenuItem("Introskopai", fontsize);
	menuItemIntroscopes.addActionListener(this);
	menuItemIntroscopes.setActionCommand("introscopes");
        menuRSC.add(menuItemIntroscopes);
	menuItemGeneratorStates = new JMyCheckBoxMenuItem("Generatorių būsenos", fontsize);
	menuItemGeneratorStates.addActionListener(this);
	menuItemGeneratorStates.setActionCommand("generatorStates");
        menuRSC.add(menuItemGeneratorStates);
        menuTabs.add(menuRSC);
// _______________Turtas_______________
	menuTabs.addSeparator();
	menuIT = new JMyMenu("Turtas", fontsize);
	menuItemIT = new JMyCheckBoxMenuItem("IT", fontsize);
	menuItemIT.addActionListener(this);
	menuItemIT.setActionCommand("it");
        menuIT.add(menuItemIT);
	menuItemCodes = new JMyCheckBoxMenuItem("Veiklos kodai", fontsize);
	menuItemCodes.addActionListener(this);
        menuItemCodes.setActionCommand("codes");
        menuIT.add(menuItemCodes);
	menuItemBarcode = new JMyMenuItem("Brūkšniai kodai", fontsize);
	menuItemBarcode.addActionListener(this);
	menuItemBarcode.setActionCommand("barcodes");
	menuIT.add(menuItemBarcode);
	menuTabs.add(menuIT);
// _____________________________
	menuTabs.addSeparator();
        menuItemTP = new JMyCheckBoxMenuItem("TP", fontsize);
        menuItemTP.addActionListener(this);
        menuItemTP.setActionCommand("tp");
        menuTabs.add(menuItemTP);
	
	menu_bar.add(menuTabs);
   
// __________Pagalba___________________
        menuHelp = new JMyMenu("Pagalba", fontsize);
        menuItemHelp = new JMyMenuItem("Aprašymas", fontsize);
        menuItemHelp.addActionListener(this);
        menuItemHelp.setActionCommand("help");
        menuHelp.add(menuItemHelp);
        menuItemAbout = new JMyMenuItem("Versija", fontsize);
        menuItemAbout.addActionListener(this);
        menuItemAbout.setActionCommand("about");
        menuHelp.add(menuItemAbout);

        menu_bar.add(menuHelp);

	return menu_bar;
    }
//	menuItemBudget = new JMyCheckBoxMenuItem("Biudžetas");
//	menuItemBudget.addActionListener(this);
//	menuItemBudget.setActionCommand("budget");
//	menuData.add(menuItemBudget);
//        menuItemPartners = new JMyCheckBoxMenuItem("Kontrahentai");
//        menuItemPartners.addActionListener(this);
//        menuItemPartners.setActionCommand("partners");
//        menuData.add(menuItemPartners);
//        menuItemAccounts = new JMyCheckBoxMenuItem("Sąskaitos");
//        menuItemAccounts.addActionListener(this);
//        menuItemAccounts.setActionCommand("accounts");
//        menuData.add(menuItemAccounts);
//        menuItemContracts = new JMyCheckBoxMenuItem("Sutartys");
//        menuItemContracts.addActionListener(this);
//        menuItemContracts.setActionCommand("works");
//        menuData.add(menuItemContracts);
//        menuItemOrders = new JMyCheckBoxMenuItem("Užsakymai");
//        menuItemOrders.addActionListener(this);
//        menuItemOrders.setActionCommand("orders");
//        menuData.add(menuItemOrders);

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
	} catch (ClassNotFoundException | InstantiationException | SQLException | IllegalAccessException ex) {
	    connection = null;
	    password = "";
	    labelMessage.setText(ex.getMessage());
	}
	return connection;
    }

//    protected ConnectionEquipment connect(String database) {
//        password = "i--Logic15325";
//        username = "Antanas";
//	connection = new ConnectionEquipment(the_host, database, username);
//	try {
//	    labelMessage.setText(connection.connect(password));
//	} catch (ClassNotFoundException | InstantiationException | SQLException | IllegalAccessException ex) {
//	    connection = null;
//	    password = "";
//	    labelMessage.setText(ex.getMessage());
//	}
//	return connection;
//    }
    
    
    protected void disconnect() {
	if (connection != null) {
	    labelMessage.setText(connection.disconnect());
	    connection = null;
	} else {
	    labelMessage.setText("Nuo duomenų bazės atsijungta");
	}
	if (panelWorks != null) {
	    panelWorks.disconnect();
	    menuItemWorks.setSelected(false);
	    tabbedpane.remove(panelWorks);
	    showWorks();
	}
	if (panelIT != null) {
	    panelIT.disconnect();
	    menuItemIT.setSelected(false);
	    tabbedpane.remove(panelIT);
	    showIT();
	}
	if (panelEquipmentTypes != null) {
	    panelEquipmentTypes.disconnect();
	    menuItemEquipmentTypes.setSelected(false);
	    tabbedpane.remove(panelEquipmentTypes);
	    showEquipmentTypes();
	}
	if (panelSystems != null) {
	    panelSystems.disconnect();
	    menuItemSystems.setSelected(false);
	    tabbedpane.remove(panelSystems);
	    showSystems();
	}
	if (panelUsers != null) {
	    panelUsers.disconnect();
	    menuItemUsers.setSelected(false);
	    tabbedpane.remove(panelUsers);
	    showUsers();
	}
	if (panelStates != null) {
	    panelStates.disconnect();
	    menuItemStates.setSelected(false);
	    tabbedpane.remove(panelStates);
	    showSystems();
	}
	if (panelWorktypes != null) {
	    panelWorktypes.disconnect();
	    menuItemWorktypes.setSelected(false);
	    tabbedpane.remove(panelWorktypes);
	    showWorktypes();
	}
	menuTabs.setVisible(false);
	password = "";
//	    dialogPassword.dispose();
//	    panelOutlays.disconnect();
            
    }

    public void setzt_dieMeldung(String dieMeldung) {
	labelMessage.setText(dieMeldung);
    }

    protected void connect_Equipment() {
	if (menuTabs != null) {
	    menuTabs.setVisible(true);
	}
	if (connection == null) {
	    connection = connect("Equipment");
	}
	if (panelIT != null) {
	    panelIT.setConnection(connection);
	}
	if (panelEquipmentTypes != null) {
	    panelEquipmentTypes.setConnection(connection);
	}
	if (panelSystems != null) {
	    panelSystems.setConnection(connection);
	}
	if (panelUsers != null) {
	    panelUsers.setConnection(connection);
	}
	if (panelWorktypes != null) {
	    panelWorktypes.setConnection(connection);
	}
	if (panelWorks != null) {
	    panelWorks.setConnection(connection);
	}
	if (panelLocations != null) {
	    panelLocations.setConnection(connection);
	}

//	renewTypes();
    }

    private String bekommt_letztenDatensatz(String einDateiname) {
	final int DIEZEILELZENGE = 64;
	int gelesen;
	String zk;
	Path dieDatei;
	FileChannel derDateiKanal;
	ByteBuffer derBytePuffer;
	zk = null;
	derBytePuffer = ByteBuffer.allocate(DIEZEILELZENGE);
	dieDatei = FileSystems.getDefault().getPath(einDateiname);
	try {
	    derDateiKanal = FileChannel.open(dieDatei, READ);
	    derDateiKanal.position(derDateiKanal.size() - DIEZEILELZENGE);
	    do {
		gelesen = derDateiKanal.read(derBytePuffer);
	    } while (gelesen != -1 && derBytePuffer.hasRemaining());
	    zk = new String(derBytePuffer.array(), Charset.forName("UTF-8"));
	    zk = zk.substring(zk.indexOf("\n") + 1);
	} catch (IOException ex) {
	    setzt_dieMeldung(labelMessage.getText().concat("; ").concat(ex.toString()));
	}
	return zk;
    }
    
    private String bekommt_erstenDatensatz(String einDateiname) {
	int x;
	StringBuilder sb;
	Reader r;
	sb = new StringBuilder(einDateiname).append(": \n");
	try {
	    r = new FileReader(einDateiname);
	    do {
		x = r.read();
		sb.append((char) x);
	    } while (x != -1 && x != 10);
	} catch (FileNotFoundException ex) {
	    zeigt_dieFehlermeldung(ex.toString());
	} catch (IOException ex) {
	    zeigt_dieFehlermeldung(ex.toString());
	}
	return sb.toString();
    }
    
    private void zeigt_dieFehlermeldung(String dieMeldung) {
	JOptionPane.showMessageDialog(this, dieMeldung, "Klaida!", JOptionPane.ERROR_MESSAGE);
    }

    
    private void loescht_dat(String einDateiname) {
        int loeschen;
        Path dieDatei;
        dieDatei = Paths.get(einDateiname);
	if (Files.exists(dieDatei)) {
            loeschen = JOptionPane.showConfirmDialog(this, einDateiname.concat(" šalinti?"));
            if (loeschen == JOptionPane.YES_OPTION) {
                try {
                    Files.delete(dieDatei);
                } catch (IOException ex) {
                    zeigt_dieFehlermeldung(ex.toString());
                }
            }
	}
    }
    
    private void showEquipmentTypes() {
	if (panelEquipmentTypes == null & menuItemEquipmentTypes.isSelected()) {
	    panelEquipmentTypes = new Irangos_tipai(connection, fontsize);
	    tabbedpane.addTab("Įrangos tipai", panelEquipmentTypes);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelEquipmentTypes != null & !menuItemEquipmentTypes.isSelected()) {
	    tabbedpane.remove(panelEquipmentTypes);
	    panelEquipmentTypes = null;
	}
    }
    
    private void showSystems() {
	if (panelSystems == null & menuItemSystems.isSelected()) {
	    panelSystems = new Sistemos(connection, fontsize);
	    tabbedpane.addTab("Sistemos", panelSystems);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelSystems != null & !menuItemSystems.isSelected()) {
	    tabbedpane.remove(panelSystems);
	    panelSystems = null;
	}
    }

    private void showStates() {
	if (panelStates == null & menuItemStates.isSelected()) {
	    panelStates = new Busenos(connection, fontsize);
	    tabbedpane.addTab("Būsenos", panelStates);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelStates != null & !menuItemStates.isSelected()) {
	    tabbedpane.remove(panelStates);
	    panelStates = null;
	}
    }
    
    private void showUsers() {
	if (panelUsers == null & menuItemUsers.isSelected()) {
	    panelUsers = new Vartotojai(connection, fontsize);
	    tabbedpane.addTab("Vartotojai", panelUsers);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelUsers != null & !menuItemUsers.isSelected()) {
	    tabbedpane.remove(panelUsers);
	    panelUsers = null;
	}
    }

    protected void showWorks() {
	if (panelWorks == null & menuItemWorks.isSelected()) {
	    panelWorks = new Darbai(connection, fontsize);
	    tabbedpane.addTab("Darbai", panelWorks);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelWorks != null & !menuItemWorks.isSelected()) {
	    tabbedpane.remove(panelWorks);
	    panelWorks = null;
	}
    }
    
    private void showWorktypes() {
	if (panelWorktypes == null & menuItemWorktypes.isSelected()) {
	    panelWorktypes = new Darbu_rusys(connection, fontsize);
	    tabbedpane.addTab("Darbų rūšys", panelWorktypes);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelWorktypes != null & !menuItemWorktypes.isSelected()) {
	    tabbedpane.remove(panelWorktypes);
	    panelWorktypes = null;
	}
    }

    private void showIT() {
	if (panelIT != null & !menuItemIT.isSelected()) {
	    tabbedpane.remove(panelIT);
	    panelIT = null;
	}
	if (panelIT == null & menuItemIT.isSelected()) {
	    panelIT = new Turtas(connection, fontsize);
	    tabbedpane.addTab("IT", panelIT);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
    }
    
    private void showTP() {
	if (panelTP == null & menuItemTP.isSelected()) {
	    panelTP = new TP(connection, fontsize);
	    tabbedpane.addTab("TP", panelTP);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelTP != null & !menuItemTP.isSelected()) {
	    tabbedpane.remove(panelTP);
	    panelTP = null;
	}
    }
    
    
    private void showLocations() {
	if (panelLocations == null & menuItemLocations.isSelected()) {
	    panelLocations = new Vietos(connection, fontsize);
	    tabbedpane.addTab("Vietos", panelLocations);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelLocations != null & !menuItemLocations.isSelected()) {
	    tabbedpane.remove(panelLocations);
	    panelLocations = null;
	}
    }
    
    private void showGenerators() {
	if (panelGeneratoriai == null & menuItemGenerators.isSelected()) {
	    panelGeneratoriai = new Generatoriai(connection, fontsize);
	    tabbedpane.addTab("Generatoriai", panelGeneratoriai);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelGeneratoriai != null & !menuItemGenerators.isSelected()) {
	    tabbedpane.remove(panelGeneratoriai);
	    panelGeneratoriai = null;
	}
    }
    
    private void showIntroscopes() {
	if (panelIntroskopai == null & menuItemIntroscopes.isSelected()) {
	    panelIntroskopai = new Introskopai(connection, fontsize);
	    tabbedpane.addTab("Introskopai", panelIntroskopai);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelIntroskopai != null & !menuItemIntroscopes.isSelected()) {
	    tabbedpane.remove(panelIntroskopai);
	    panelIntroskopai = null;
	}
    }    
    
    private void showGeneratorStates() {
	if (panelGen_busenos == null & menuItemGeneratorStates.isSelected()) {
	    panelGen_busenos = new Gen_busenos(connection, fontsize);
	    tabbedpane.addTab("Generatorių būsenos", panelGen_busenos);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelGen_busenos != null & !menuItemGeneratorStates.isSelected()) {
	    tabbedpane.remove(panelGen_busenos);
	    panelGen_busenos = null;
	}
    }    
    
    private void showCodes() {
	if (panelVeiklos == null & menuItemCodes.isSelected()) {
	    panelVeiklos = new Veiklos(connection, fontsize);
	    tabbedpane.addTab("Veiklos", panelVeiklos);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelVeiklos != null & !menuItemCodes.isSelected()) {
	    tabbedpane.remove(panelVeiklos);
	    panelVeiklos = null;
	}
    }
    
    
    private void showHelp() {
	if (frHelp == null) {
	    frHelp = new Help();
	    frHelp.setSize(600, 800);
	    frHelp.setTitle("Aprašymas");
	}
	else {
	    frHelp.setVisible(true);
	}        
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
    
    @Override
    public void actionPerformed(ActionEvent e) {
	String command;
	command = e.getActionCommand();
	switch (command) {
	    case "connect":
		connect_Equipment();
		break;
	    case "disconnect":
		disconnect();
		break;
	    case "users":
		connect_Equipment();
		showUsers();
		break;
	    case "systems":
		connect_Equipment();
		showSystems();
		break;		
	    case "states":
		connect_Equipment();
		showStates();
		break;		
	    case "worktypes":
		connect_Equipment();
		showWorktypes();
		break;		
	    case "works":
		connect_Equipment();
		showWorks();
		break;		
	    case "it":
		connect_Equipment();
		showIT();
		break;		
            case "equipmenttypes":
		connect_Equipment();
		showEquipmentTypes();
		break;
	    case "tp":
		connect_Equipment();
		showTP();
		break;		
	    case "locations":
		connect_Equipment();
		showLocations();
		break;		
	    case "generators":
		connect_Equipment();
		showGenerators();
		break;
	    case "introscopes":
		connect_Equipment();
		showIntroscopes();
		break;		
	    case "generatorStates":
		connect_Equipment();
		showGeneratorStates();
		break;                
	    case "codes":
		connect_Equipment();
		showCodes();
		break;		
	    case "barcodes":
		connect_Equipment();
		menuItemIT.setSelected(true);
		showIT();
		panelIT.create_barcodes_file();
		break;		
	    case "help":
		showHelp();
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
