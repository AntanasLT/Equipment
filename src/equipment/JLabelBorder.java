/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package equipment;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author a
 */
public class JLabelBorder extends JLabel {
	public JLabelBorder(String derText){
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		setText(derText);
		
	}
	
}
