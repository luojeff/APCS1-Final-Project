import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** 
 * Main component for displaying the area for editing text.
 * Receives actions and allows for interaction. Also has
 * more appealing text font and size
 */
public class EditorArea extends JPanel implements KeyListener {
    JEditorPane ePane;
    
    public EditorArea(){
	super(new BorderLayout());
	
	ePane = new JEditorPane();
	ePane.setFont(new Font("Consolas", Font.PLAIN, 18));
	ePane.addKeyListener(this);
	
	this.add(ePane);
    }

    public void keyTyped(KeyEvent e){
	// System.out.println(e.getKeyChar());
    }

    public void keyPressed(KeyEvent e){
    }
    
    public void keyReleased(KeyEvent e){
    }

    public String getText(){
	return ePane.getText();
    }

    public void setText(String contents){
	ePane.setText(contents);
    }
}
