/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author a
 */
public class DialogPassword extends JDialog implements ActionListener, PropertyChangeListener {

    private final String DER_KNOPFTEXT1 = "Taip";
    private final String DER_KNOPFTEXT2 = "Ne";

    private final JPasswordField dasKennwortFeld;
    private JTextField dasBenutzerFeld;
    private JOptionPane optionPane;

    /**
     * Returns null if the typed string was invalid; otherwise, returns the
     * string as the user entered it.
     *
     * @return
     */
    /**
     * Creates the reusable dialog.
     *
     * @param aFrame
     */
    public DialogPassword(Frame aFrame) {
        super(aFrame, true);
        setTitle("Slaptažodis");
        dasKennwortFeld = new JPasswordField(20);
        dasBenutzerFeld = new JTextField(20);
        Object[] derBericht = {"Naudotojas:", dasBenutzerFeld, "Slaptažodis:", dasKennwortFeld};
        Object[] dieKnopfnamen = {DER_KNOPFTEXT1, DER_KNOPFTEXT2}; //Create an array specifying the number of dialog buttons and their text.
        optionPane = new JOptionPane(derBericht, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, dieKnopfnamen, dieKnopfnamen[0]); //Create the JOptionPane.
        setContentPane(optionPane); //Make this dialog display it.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); //Handle window closing correctly.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                /*
             * Instead of directly closing the window, we're going to change the JOptionPane's value property.
                 */
                optionPane.setValue(JOptionPane.CLOSED_OPTION);
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                dasBenutzerFeld.requestFocusInWindow();
            }
        }); //Ensure the text field always gets the first focus.
        dasKennwortFeld.addActionListener(this); //Register an event handler that puts the text into the option pane.
        optionPane.addPropertyChangeListener(this);  //Register an event handler that reacts to option pane state changes.
    }

    /**
     * This method handles events for the text field.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(DER_KNOPFTEXT1);
    }

    /**
     * This method reacts to state changes in the option pane.
     *
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
//        if (optionPane.getValue().equals(JOptionPane.NO_OPTION)) {
        if (isVisible() && (e.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object derWert = optionPane.getValue();
            if (derWert == JOptionPane.UNINITIALIZED_VALUE) {
                return; //ignore reset
            }
        }
        if (!String.valueOf(optionPane.getValue()).equals(DER_KNOPFTEXT1)) {
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE); //Reset the JOptionPane's value.If you don't do this, then if the user presses the same button next time, no property change event will be fired.
            dasKennwortFeld.setText("");
        }
        setVisible(false);
    }

    /**
     * This method clears the dialog and hides it.
     *
     * @return
     */
//    public void clearAndHide() {
//        dasKennwortFeld.setText(null); 
//        setVisible(false); 
//    }
    protected String[] bekommeKennwort() {
        String[] res;
        res = new String[2];
        res[0] = dasBenutzerFeld.getText();
        res[1] = String.valueOf(dasKennwortFeld.getPassword());
        return res;
    }
}
