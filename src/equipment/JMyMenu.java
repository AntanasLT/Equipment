/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Font;
import javax.swing.JMenu;

/**
 *
 * @author a
 */
public class JMyMenu extends JMenu {
    public JMyMenu(String derText, int size) {
        setFont(new Font("Arial", Font.PLAIN, size));
        setText(derText);
    }
}
