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
    
    public EditorArea(){
        super(new BorderLayout());
        
        ePane = new JEditorPane("text/html_code", "");
        ePane.setFont(new Font("Consolas", Font.PLAIN, 18));

        // Demo: ePane.getInputMap().put(KeyStroke.getKeyStroke("A"),"keyA");
        // ePane.getActionMap().put("keyA", new KeyAction("A"));
        ePane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK),"keyCopy");
        ePane.getActionMap().put("keyCopy", new KeyAction("copy"));
        
        this.add(ePane);
        //Tests
        //System.out.println(JEditorPane.getEditorKitClassNameForContentType("x-code/html"));
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
    public static class KeyAction extends AbstractAction {
        private String sequence;
        
        public KeyAction(String sequence){
            this.sequence = sequence;
        };

        public void actionPerformed(ActionEvent e){ 
            if(sequence.equals("copy")){
                System.out.println("Highlighted text copied!");
            }
        }
    }
}
