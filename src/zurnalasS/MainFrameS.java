package zurnalasS;

import equipment.*;
import javax.swing.JMenuBar;

/**
 *
 * @author a
 */
public class MainFrameS extends MainFrame {

    WorkS panelWorks;
    int fontsize;

    protected MainFrameS(String host, int size) {
	super(host, size);
//	panelWorks = new WorkS(connection);
//	tabbedpane.addTab("Darbai", panelWorks);

    }

    @Override
    protected void connect_Equipment() {
	if (menuTabs != null) {
	    menuTabs.setVisible(true);
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
	    panelWorks = new WorkS(connection, fontsize);
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
        menuTabs = new JMyMenu("Lentelės", fontsize);
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
//	menuItemDevices = new JMyCheckBoxMenuItem("Įrenginiai");
//	menuItemDevices.addActionListener(this);
//	menuItemDevices.setActionCommand("devices");
//	menuData.add(menuItemDevices);
//	menuItemBudget = new JMyCheckBoxMenuItem("Biudžetas");
//	menuItemBudget.addActionListener(this);
//	menuItemBudget.setActionCommand("budget");
//	menuData.add(menuItemBudget);
        menuItemWorktypes = new JMyCheckBoxMenuItem("Darbų rūšys", fontsize);
        menuItemWorktypes.addActionListener(this);
        menuItemWorktypes.setActionCommand("worktypes");
        menuTabs.add(menuItemWorktypes);
//        menuItemEquipmentTypes = new JMyCheckBoxMenuItem("Įrangos tipai");
//        menuItemEquipmentTypes.addActionListener(this);
//        menuItemEquipmentTypes.setActionCommand("equipmenttypes");
//        menuData.add(menuItemEquipmentTypes);
        menuItemStates = new JMyCheckBoxMenuItem("Būsenos", fontsize);
        menuItemStates.addActionListener(this);
        menuItemStates.setActionCommand("states");
        menuTabs.add(menuItemStates);
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
