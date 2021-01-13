/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zurnalasS;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author a
 */
public class ZurnalaS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int h, h1, l, size;

//		try {
//			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
//				if ("Nimbus".equals(info.getName())){
//					UIManager.setLookAndFeel(info.getClassName());
//				}
//			}
//		} catch (Exception e) {
//		}
//		SynthLookAndFeel laf = new SynthLookAndFeel();
//		try {
//			laf.load(MainFrame.class.getResourceAsStream("derStil.xml"), MainFrame.class);
//			UIManager.setLookAndFeel(laf);
//		} catch (Exception e) {
//		}

//		try {
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
//		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {		}
        if (args[1] == null) {
            size = 12;
        } else {
            size = Integer.getInteger(args[1]);
        }
        MainFrameS mainFrame = new MainFrameS(args[0], size);
        mainFrame.setDefaultCloseOperation(MainFrameS.EXIT_ON_CLOSE);
        h = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
//	    h1 = h > 1280 ? 1000 : Math.round(h/3*2);
//	    l = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//            l = l > 1600 ? 1600 : l;
        l = 1024;
        h1 = 800;
        mainFrame.setSize(l, h1);
        mainFrame.setMinimumSize(new Dimension(l, h1/2));
        mainFrame.setJMenuBar(mainFrame.menuBar());
        mainFrame.setTitle("Žurnalas");
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("/home/a/Programme/java/berufliche/Equipment/zurnalas.png"));
        mainFrame.setVisible(true);
    }

}
