/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Font;
import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author a
 */
public class JMyCheckBoxMenuItem extends JCheckBoxMenuItem {
    public JMyCheckBoxMenuItem(String text) {
        setFont(new Font("Arial", Font.PLAIN, 12));
        setText(text);
        
    }
    
}
