/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author a
 */
public class JMyButton extends JButton {
    public JMyButton(String text, int size) {
        setFont(new Font("Arial", Font.PLAIN, size));
        setText(text);
    }
    
}
