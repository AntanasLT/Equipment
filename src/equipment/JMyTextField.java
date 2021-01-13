/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Font;
import javax.swing.JTextField;

/**
 *
 * @author a
 */
class JMyTextField extends JTextField {

    public JMyTextField(int fontsize) {
        setFont(new Font("Arial", Font.PLAIN, fontsize));        
    }

    public JMyTextField(String text, int fontsize) {
        setFont(new Font("Arial", Font.PLAIN, fontsize));    
        setText(text);
    }    
    
    public JMyTextField(int cols, int fontsize) {
        setColumns(cols);
        setFont(new Font("Arial", Font.PLAIN, fontsize));        
    }
    
    
    public JMyTextField(String text, int cols, int fontsize) {
        setText(text);
        setColumns(cols);
        setFont(new Font("Arial", Font.PLAIN, fontsize));        
    }
    
}
