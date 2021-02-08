/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package equipment;

import java.sql.SQLException;

/**
 *
 * @author a
 */
public class MainFrame_test extends MainFrame {

    protected MainFrame_test(String host, int size) {
	super(host, size);
    }

    @Override
    protected ConnectionEquipment connect(String database) {
	password = "i--Logic15325";
	username = "Antanas";
	connection = new ConnectionEquipment(the_host, database, username);
	try {
	    labelMessage.setText(connection.connect(password));
	} catch (ClassNotFoundException | InstantiationException | SQLException | IllegalAccessException ex) {
	    connection = null;
	    password = "";
	    labelMessage.setText(ex.getMessage());
	}
	return connection;
    }

    
    
}