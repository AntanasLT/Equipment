/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Font;
import javax.swing.JTextArea;

/**
 *
 * @author a
 */
class JMyTextArea_monospaced extends JTextArea {
    
    public JMyTextArea_monospaced(int rows, int cols, int fontsize) {
        setRows(rows);
        setColumns(cols);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontsize));        
    }
    
    
}
