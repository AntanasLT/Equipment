/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Font;
import javax.swing.JTabbedPane;

/**
 *
 * @author a
 */
public class JMyTabbedPane extends JTabbedPane {
    public JMyTabbedPane(int size){
        setFont(new Font("Arial", Font.PLAIN, size));
    }
}
