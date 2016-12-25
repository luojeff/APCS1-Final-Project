import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** 
 * Main component for displaying the area for editing text.
 * Receives actions and allows for interaction.
 */
public class Editor extends JEditorPane implements ActionListener {
    public Editor(){
	setFont(new Font("Arial", Font.PLAIN, 18));
	//setLineWrap(true);
    }
    
    public void actionPerformed(ActionEvent e){
    }
}
