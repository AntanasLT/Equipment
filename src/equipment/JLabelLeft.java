/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Font;
import javax.swing.JLabel;

/**
 *
 * @author a
 */
public class JLabelLeft extends JLabel {

    public JLabelLeft(String derText, int size) {
        setFont(new Font("Arial", Font.PLAIN, size));
        setText(derText);
    }
    
    public JLabelLeft(int size) {
        setFont(new Font("Arial", Font.PLAIN, size));
    }    
}
