import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** 
 * Main component for displaying the area for editing text.
 * Receives actions and allows for interaction. Also has
 * more appealing text font and size
 */
public class EditorArea extends JPanel {
    JEditorPane ePane;
    InputMap inMap;
    ActionMap acMap;
    
    public EditorArea(){
	super(new BorderLayout());
	
	ePane = new JEditorPane();
	ePane.setFont(new Font("Consolas", Font.PLAIN, 18));

	inMap = ePane.getInputMap(ePane.WHEN_FOCUSED);
	acMap = ePane.getActionMap();
	
	inMap.put(KeyStroke.getKeyStroke("f"),"pressed");
	acMap.put("pressed",new KeyAction("pressed"));
	
	this.add(ePane);
    }

    public String getText(){
	return ePane.getText();
    }

    public void setText(String contents){
	ePane.setText(contents);
    }

    /**
     *Action command for when a certain
     *key (or binding) is pressed. 
     */
    public class KeyAction extends AbstractAction {
	private String action;
	
	public KeyAction(String action){
	    this.action = action;
	};

	public void actionPerformed(ActionEvent e){
	    if(e.equals("pressed")){
		System.out.println("Pressed F key!");
	    }
	}
    }
}
