/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zurnalas;

import equipment.*;
import datum.Datum;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author a
 */
public class Works_Z extends Works {

    public Works_Z(ConnectionEquipment connection) {
	super(connection);
    }

//    private void createPanelInput(){
//        pInput = new JPanel(new BorderLayout());
//        createPanelFields();
//        pInput.add(pFields, BorderLayout.NORTH);
//        createPanelButtons();
//        pInput.add(pButtons, BorderLayout.SOUTH);
//        lMessage = new JLabelRechts();
////        updateComboBoxes();
//    }

    @Override
    protected void createPanelButtons() {
	pButtons = new JPanel();
	bChange = new JMyButton("Išsaugoti");
	bChange.setActionCommand("update");
	bChange.addActionListener(this);
	bAdd = new JMyButton("Naujas");
	bAdd.setActionCommand("insert");
	bAdd.addActionListener(this);
//	bDelete = new JMyButton("Pašalinti");
//	bDelete.setActionCommand("delete");
//	bDelete.addActionListener(this);
	bFilter = new JMyButton("Filtruoti");
	bFilter.setActionCommand("filter");
	bFilter.addActionListener(this);
	pButtons.add(bFilter);
	pButtons.add(bChange);
	pButtons.add(bAdd);
//	pButtons.add(bDelete);
    }

}
