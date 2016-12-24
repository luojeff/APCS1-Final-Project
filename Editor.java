import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** 
 * Main component for displaying the area for editing text.
 * Receives actions and allows for interaction.
 */
public class Editor extends JTextArea implements ActionListener {

    public Editor(){
	super(10,50);
    }
    
    public void actionPerformed(ActionEvent e){
    }
}
