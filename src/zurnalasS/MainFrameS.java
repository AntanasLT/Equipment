package zurnalasS;

import equipment.*;
import javax.swing.JMenuBar;

/**
 *
 * @author a
 */
public class MainFrameS extends MainFrame {

    WorkS panelWorks;

    protected MainFrameS(String host) {
	super(host);
//	panelWorks = new WorkS(connection);
//	tabbedpane.addTab("Darbai", panelWorks);

    }

    @Override
    protected void connect_Equipment() {
	if (menuData != null) {
	    menuData.setVisible(true);
	}
	if (connection == null) {
	    connection = connect("Equipment");
	}
//	if (panelWorks != null & connection != null) {
//	    panelWorks.setConnection(connection);
//	    panelWorks.init_components();
//	}
    }

    @Override
    protected void showWorks() {
	if (panelWorks == null & menuItemWorks.isSelected()) {
	    panelWorks = new WorkS(connection);
	    tabbedpane.addTab("Darbai", panelWorks);
	    tabbedpane.setSelectedIndex(tabbedpane.getTabCount() - 1);
	}
	if (panelWorks != null & !menuItemWorks.isSelected()) {
	    tabbedpane.remove(panelWorks);
	    panelWorks = null;
	}
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
        menuData = new JMyMenu("Lentelės");
        menuItemWorks = new JMyCheckBoxMenuItem("Darbai");
	menuItemWorks.addActionListener(this);
	menuItemWorks.setActionCommand("works");
	menuData.add(menuItemWorks);
	menuItemUsers = new JMyCheckBoxMenuItem("Vartotojai");
	menuItemUsers.addActionListener(this);
	menuItemUsers.setActionCommand("users");
	menuData.add(menuItemUsers);	
	menuItemSystems = new JMyCheckBoxMenuItem("Sistemos");
	menuItemSystems.addActionListener(this);
	menuItemSystems.setActionCommand("systems");
	menuData.add(menuItemSystems);
//	menuItemDevices = new JMyCheckBoxMenuItem("Įrenginiai");
//	menuItemDevices.addActionListener(this);
//	menuItemDevices.setActionCommand("devices");
//	menuData.add(menuItemDevices);
//	menuItemBudget = new JMyCheckBoxMenuItem("Biudžetas");
//	menuItemBudget.addActionListener(this);
//	menuItemBudget.setActionCommand("budget");
//	menuData.add(menuItemBudget);
        menuItemWorktypes = new JMyCheckBoxMenuItem("Darbų rūšys");
        menuItemWorktypes.addActionListener(this);
        menuItemWorktypes.setActionCommand("worktypes");
        menuData.add(menuItemWorktypes);
//        menuItemEquipmentTypes = new JMyCheckBoxMenuItem("Įrangos tipai");
//        menuItemEquipmentTypes.addActionListener(this);
//        menuItemEquipmentTypes.setActionCommand("equipmenttypes");
//        menuData.add(menuItemEquipmentTypes);
        menuItemStates = new JMyCheckBoxMenuItem("Būsenos");
        menuItemStates.addActionListener(this);
        menuItemStates.setActionCommand("states");
        menuData.add(menuItemStates);
	menu_bar.add(menuData);

	menuHelp = new JMyMenu("Pagalba");
	menuItemHelp = new JMyMenuItem("Aprašymas");
	menuItemHelp.addActionListener(this);
	menuItemHelp.setActionCommand("help");
	menuHelp.add(menuItemHelp);
	menu_bar.add(menuHelp);

	return menu_bar;
    }

}
