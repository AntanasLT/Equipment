package zurnalasP;

import equipment.*;
import javax.swing.JMenuBar;

/**
 *
 * @author a
 */
public class MainFrameP extends MainFrame {

    WorksP panelWorks;

    protected MainFrameP(String host) {
	super(host);
	panelWorks = new WorksP(connection);
	tabbedpane.addTab("Darbai", panelWorks);

    }

    @Override
    protected void connect_Equipment() {
	if (connection == null) {
	    connection = connect("Equipment");
	}
	if (panelWorks == null) {
	    panelWorks = new WorksP(connection);
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
        
	menuData = new JMyMenu("Kortelės");
	menuItemWorks = new JMyCheckBoxMenuItem("Darbai");
	menuItemWorks.addActionListener(this);
	menuItemWorks.setActionCommand("works");
	menuItemWorks.setSelected(true);
	menuData.add(menuItemWorks);
	menu_bar.add(menuData);
        
        menuHelp = new JMyMenu("Pagalba");
        menuItemHelp = new JMyMenuItem("Aprašymas");
        menuItemHelp.addActionListener(this);
        menuItemHelp.setActionCommand("help");
        menuHelp.add(menuItemHelp);
	menuItemAbout = new JMyMenuItem("Versija");
	menuItemAbout.addActionListener(this);
	menuItemAbout.setActionCommand("about");
	menuHelp.add(menuItemAbout);

	menu_bar.add(menuHelp);
        
        
	return menu_bar;
    }

}
