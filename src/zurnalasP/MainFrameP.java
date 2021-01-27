package zurnalasP;

import equipment.*;
import javax.swing.JMenuBar;

/**
 *
 * @author a
 */
public class MainFrameP extends MainFrame {

    WorksP panelWorks;
    AboutP frAboutP;
//    int fontsize;
 
    protected MainFrameP(String host, int size) {
	super(host, size);
 	panelWorks = new WorksP(connection, fontsize);
	tabbedpane.addTab("Darbai", panelWorks);
    }

    @Override
    protected void showAbout() {
	if (frAboutP == null) {
	    frAboutP = new AboutP();
	    frAboutP.setSize(200, 100);
	    frAboutP.setTitle("Apie");
	    frAboutP.setResizable(false);
	    frAboutP.setLocation(100, 100);
	} else {
	    frAboutP.setVisible(true);
	}
    }

    @Override
    protected void connect_Equipment() {
	if (connection == null) {
	    connection = connect("Equipment");
	}
	if (panelWorks == null) {
	    panelWorks = new WorksP(connection, fontsize);
	}
	
//	if (panelWorks != null & connection != null) {
//	    panelWorks.setConnection(connection);
//	    panelWorks.init_components();
//	}
//	renewTypes();
    }

    @Override
    protected void disconnect() {
	if (connection != null) {
	    labelMessage.setText(connection.disconnect());
	    connection = null;
	    if (panelWorks != null) {
		panelWorks.disconnect();
		tabbedpane.remove(panelWorks);
	    }
//	    password = "";
//	    dialogPassword.dispose();
//	    panelOutlays.disconnect();
	} else {
	    labelMessage.setText("Nuo duomenų bazės atsijungta");
	}
    }


    @Override
    protected JMenuBar menuBar() {
	menu_bar = new JMenuBar();

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
        
	menuTabs = new JMyMenu("Kortelės", fontsize);
	menuItemWorks = new JMyCheckBoxMenuItem("Darbai", fontsize);
	menuItemWorks.addActionListener(this);
	menuItemWorks.setActionCommand("works");
	menuItemWorks.setSelected(true);
	menuTabs.add(menuItemWorks);
	menu_bar.add(menuTabs);
        
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

}
