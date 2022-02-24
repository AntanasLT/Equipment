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

    /**
     *
     * @param fontsize
     */
    public JMyComboBox(int fontsize){
	setFont(new Font("Arial", Font.PLAIN, fontsize));
    }
    
    /**
     *
     * @param list
     * @param fontsize
     */
    public JMyComboBox(Object[] list, int fontsize) {
	super(list);
	setFont(new Font("Arial", Font.PLAIN, fontsize));
    }

      
}
