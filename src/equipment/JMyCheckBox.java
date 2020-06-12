/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Font;
import javax.swing.JCheckBox;

/**
 *
 * @author a
 */
public class JMyCheckBox extends JCheckBox {
    public JMyCheckBox(String text, boolean checked) {
        setFont(new Font("Arial", Font.PLAIN, 12));
        setText(text);
        setSelected(checked);
    }
}
