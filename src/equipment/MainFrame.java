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

/**
 *
 * @author a
 */
public class MainFrame extends JFrame implements ActionListener{
    

    String password, username;

    ConnectionEquipment connection;
    Message message;

    JTabbedPane tabbedpane;
    Accounts panelOutlays;
    EquipmentTypes panelEquipmentTypes;
    Systems panelSystems;
    Worktypes panelWorktypes;
    Works panelWorks;
    Devices panelDevices;
    
// –––––––––––––––––––––––   
    JMenuBar menu_bar;
// –––––––––––––––––––––––   
    JMyMenu menuDatabase;
    JMyMenuItem menuItem_connect, menItem_disconnect;
// –––––––––––––––––––––––
    JMyMenu menuData;
//    JMenuItem dasMenuePunkt_dieKoerperangaben;
    JMyCheckBoxMenuItem menuItemSystems, menuItemDevices, menuItemBudget, menuItemWorks, 
            menuItemWorktypes, menuItemEquipmentTypes, menuItemContracts, menuItemAccounts,
            menuItemPartners, menuItemOrders;
// –––––––––––––––––––––––
    
    DialogPassword dialogPassword;
    JLabelLeft labelMessage;
// –––––––––––––––––––––––

    public MainFrame() {
	connection = null;
	tabbedpane = new JMyTabbedPane();
        panelWorks = new Works(connection);
//	panelOutlays = new Accounts(connection);
	init();
    }
	
    private void init() {
	password = "";
	username = "";
	setLayout(new BorderLayout());
	labelMessage = new JLabelLeft();
	tabbedpane.addTab("Darbai", panelWorks);
	add(tabbedpane, BorderLayout.CENTER);
	add(labelMessage, BorderLayout.SOUTH);
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		disconnect();
		System.exit(0);
	    }
	});
	connect_Equipment();
    }
	
    public JMenuBar menuBar() {
	menu_bar = new JMenuBar();

	menuDatabase = new JMyMenu("Duombazė");
	menuItem_connect = new JMyMenuItem("Prisijungti");
	menuItem_connect.addActionListener(this);
	menuItem_connect.setActionCommand("connect");
	menItem_disconnect = new JMyMenuItem("Atsijungti");
	menItem_disconnect.setActionCommand("disconnect");
	menItem_disconnect.addActionListener(this);
	menuDatabase.add(menuItem_connect);
	menuDatabase.add(menItem_disconnect);
	menu_bar.add(menuDatabase);
//	menuData.addSeparator();
	menuData = new JMyMenu("Lentelės");
	menuItemSystems = new JMyCheckBoxMenuItem("Sistemos");
	menuItemSystems.addActionListener(this);
	menuItemSystems.setActionCommand("systems");
	menuData.add(menuItemSystems);
	menuItemDevices = new JMyCheckBoxMenuItem("Įrenginiai");
	menuItemDevices.addActionListener(this);
	menuItemDevices.setActionCommand("devices");
	menuData.add(menuItemDevices);
//	menuItemBudget = new JMyCheckBoxMenuItem("Biudžetas");
//	menuItemBudget.addActionListener(this);
//	menuItemBudget.setActionCommand("budget");
//	menuData.add(menuItemBudget);
        menuItemWorks = new JMyCheckBoxMenuItem("Darbai");
        menuItemWorks.addActionListener(this);
        menuItemWorks.setActionCommand("works");
        menuData.add(menuItemWorks);
        menuItemWorktypes = new JMyCheckBoxMenuItem("Darbų rūšys");
        menuItemWorktypes.addActionListener(this);
        menuItemWorktypes.setActionCommand("worktypes");
        menuData.add(menuItemWorktypes);
        menuItemEquipmentTypes = new JMyCheckBoxMenuItem("Įrangos tipai");
        menuItemEquipmentTypes.addActionListener(this);
        menuItemEquipmentTypes.setActionCommand("equipmenttypes");
        menuData.add(menuItemEquipmentTypes);
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
	menu_bar.add(menuData);

	return menu_bar;
    }

// Gemeinsamme Verfahren
    private ConnectionEquipment connect(String database) {
	if (password.equals("") || connection == null) {
	    if (dialogPassword != null) {
		dialogPassword.dispose();
	    }
//	    dialogPassword = new DialogPassword(this);
//	    dialogPassword.pack();
//	    dialogPassword.setVisible(true);
	    password = "a";
	    username = "ak";
//	    password = dialogPassword.bekommeKennwort()[1];
//	    username = dialogPassword.bekommeKennwort()[0];
	}
	connection = new ConnectionEquipment(database, username);
	try {
	    labelMessage.setText(connection.connect(password));
	} catch (ClassNotFoundException | InstantiationException | SQLException | IllegalAccessException ex) {
	    connection = null;
	    password = "";
	    labelMessage.setText(ex.getMessage());
	}
	return connection;
    }

    private void disconnect() {
	if (connection != null) {
	    labelMessage.setText(connection.disconnect());
	    connection = null;
	    password = "";
//	    dialogPassword.dispose();
//	    panelOutlays.disconnect();
            
	} else {
	    labelMessage.setText("Es gibt keine Anbindung");
	}
    }

    public void setzt_dieMeldung(String dieMeldung) {
	labelMessage.setText(dieMeldung);
    }

    /**
     * Es wird an die Haushaltsdatenbank (biudzetas8) angebunden.
     * @param eineDatenbank der Name der Datenbank
     * @param einDateiname der Pfad zu die Aus-/Einfuhrdatei
     * @param eineTabelle der Name der Haupttabelle
     * @param eineGruppentabelle der Name der Tabelle, die Gruppen enthält
     * @param eineGruppenID der Name des Gruppen ID Feldes
     * @param einGruppenname der Name des Gruppennamenfeldes
     * @param einID
     * @param Ausfuhrfelder
     */
    private void connect_Equipment() {
	if (connection == null) {
	    connection = connect("Equipment");
	}
	panelWorks.setConnection(connection);
//	renewTypes();
    }

    private void einpflegt_dieAuswahlGruppe() {
	try {
	    panelOutlays.renewTypes();
	} catch (SQLException ex) {
	    labelMessage.setText(ex.getMessage());
	}
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
	JOptionPane.showMessageDialog(this, dieMeldung, "Fehler!", JOptionPane.ERROR_MESSAGE);
    }

    
    private void loescht_dat(String einDateiname) {
        int loeschen;
        Path dieDatei;
        dieDatei = Paths.get(einDateiname);
	if (Files.exists(dieDatei)) {
            loeschen = JOptionPane.showConfirmDialog(this, einDateiname.concat(" löschen?"));
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
	    panelEquipmentTypes = new EquipmentTypes(connection);
	    tabbedpane.addTab("Įrangos tipai", panelEquipmentTypes);
	}
	if (panelEquipmentTypes != null & !menuItemEquipmentTypes.isSelected()) {
	    tabbedpane.remove(panelEquipmentTypes);
	    panelEquipmentTypes = null;
	}
    }
    
    private void showSystems() {
	if (panelSystems == null & menuItemSystems.isSelected()) {
	    panelSystems = new Systems(connection);
	    tabbedpane.addTab("Sistemos", panelSystems);
	}
	if (panelSystems != null & !menuItemSystems.isSelected()) {
	    tabbedpane.remove(panelSystems);
	    panelSystems = null;
	}
    }
    
    private void showWorks() {
	if (panelWorks == null & menuItemWorks.isSelected()) {
	    panelWorks = new Works(connection);
	    tabbedpane.addTab("Darbai", panelWorks);
	}
	if (panelWorks != null & !menuItemWorks.isSelected()) {
	    tabbedpane.remove(panelWorks);
	    panelWorks = null;
	}
    }
    
    private void showWorktypes() {
	if (panelWorktypes == null & menuItemWorktypes.isSelected()) {
	    panelWorktypes = new Worktypes(connection);
	    tabbedpane.addTab("Darbų rūšys", panelWorktypes);
	}
	if (panelWorktypes != null & !menuItemWorktypes.isSelected()) {
	    tabbedpane.remove(panelWorktypes);
	    panelWorktypes = null;
	}
    }

    private void showDevices() {
	if (panelDevices == null & menuItemDevices.isSelected()) {
	    panelDevices = new Devices(connection);
	    tabbedpane.addTab("Įrenginiai", panelDevices);
	}
	if (panelDevices != null & !menuItemDevices.isSelected()) {
	    tabbedpane.remove(panelDevices);
	    panelDevices = null;
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
	    case "systems":
		connect_Equipment();
		showSystems();
		break;		
	    case "worktypes":
		connect_Equipment();
		showWorktypes();
		break;		
	    case "works":
		connect_Equipment();
		showWorks();
		break;		
	    case "devices":
		connect_Equipment();
		showDevices();
		break;		
            case "equipmenttypes":
		connect_Equipment();
		showEquipmentTypes();
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
