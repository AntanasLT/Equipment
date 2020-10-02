/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Font;
import javax.swing.JComboBox;

/**
 *
 * @author a
 */
public class JMyComboBox extends JComboBox {
    public JMyComboBox(){
	setFont(new Font("Arial", Font.PLAIN, 12));
    }
    
    public JMyComboBox(Object[] o) {
	super(o);
	setFont(new Font("Arial", Font.PLAIN, 12));
    }
       
}
