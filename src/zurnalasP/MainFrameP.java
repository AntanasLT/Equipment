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
// 	panelWorks = new WorksP(connection, fontsize);
//	showWorks();
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
	    panelWorks = new WorksP(connection, server, fontsize);
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
	miConnect = new JMyMenuItem("Prisijungti", fontsize);
	miConnect.addActionListener(this);
	miConnect.setActionCommand("connect");
	miDisconnect = new JMyMenuItem("Atsijungti", fontsize);
	miDisconnect.setActionCommand("disconnect");
	miDisconnect.addActionListener(this);
	menuDatabase.add(miConnect);
	menuDatabase.add(miDisconnect);
	menu_bar.add(menuDatabase);
        
	menuTabs = new JMyMenu("Kortelės", fontsize);
	mcbWorks = new JMyCheckBoxMenuItem("Darbai", fontsize);
	mcbWorks.addActionListener(this);
	mcbWorks.setActionCommand("works");
	mcbWorks.setSelected(true);
	menuTabs.add(mcbWorks);
        mcbLiftai = new JMyCheckBoxMenuItem("Liftai", fontsize);
        mcbLiftai.addActionListener(this);
        mcbLiftai.setActionCommand("liftai");
	menuTabs.add(mcbLiftai);
        mcbTP = new JMyCheckBoxMenuItem("TP", fontsize);
        mcbTP.addActionListener(this);
        mcbTP.setActionCommand("tp");
        menuTabs.add(mcbTP);
	menu_bar.add(menuTabs);
        
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

        menuHelp = new JMyMenu("Pagalba", fontsize);
        miHelp = new JMyMenuItem("Aprašymas", fontsize);
        miHelp.addActionListener(this);
        miHelp.setActionCommand("help");
        menuHelp.add(miHelp);
	miAbout = new JMyMenuItem("Versija", fontsize);
	miAbout.addActionListener(this);
	miAbout.setActionCommand("about");
	menuHelp.add(miAbout);

	menu_bar.add(menuHelp);
        
        
	return menu_bar;
    }

}
