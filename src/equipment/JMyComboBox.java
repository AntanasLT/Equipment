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
    public JMyComboBox(int size){
	setFont(new Font("Arial", Font.PLAIN, size));
    }
    
    public JMyComboBox(Object[] o, int size) {
	super(o);
	setFont(new Font("Arial", Font.PLAIN, size));
    }
       
}
