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
public class JLabelRechts extends JLabel {

    public JLabelRechts(String derText) {
        setFont(new Font("Arial", Font.PLAIN, 12));
        setHorizontalAlignment(JLabel.RIGHT);
        setText(derText);
    }
    
    public JLabelRechts() {
        setFont(new Font("Arial", Font.PLAIN, 12));
        setHorizontalAlignment(JLabel.RIGHT);
    }    
}
