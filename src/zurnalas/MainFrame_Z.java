package zurnalas;

import equipment.*;
import javax.swing.JMenuBar;

/**
 *
 * @author a
 */
public class MainFrame_Z extends MainFrame {

    Works_Z panelWorks;

    protected MainFrame_Z(String host) {
	super(host);
	panelWorks = new Works_Z(connection);
	tabbedpane.addTab("Darbai", panelWorks);

    }

    @Override
    protected void connect_Equipment() {
	if (connection == null) {
	    connection = connect("Equipment");
	}
	if (panelWorks != null) {
	    panelWorks.setConnection(connection);
	}
//	renewTypes();
    }

    @Override
    protected void disconnect() {
	if (connection != null) {
	    labelMessage.setText(connection.disconnect());
	    connection = null;
	    if (panelWorks != null) {
		panelWorks.disconnect();
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
	return menu_bar;
    }

}
