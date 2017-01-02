import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

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
        DefaultStyledDocument doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new TestFilter());
        ((AbstractDocument)doc).addDocumentListener(new TestListener());
        ePane.setDocument(doc);
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
    * Creates a hashmap where actions are stored by name
    * and includes all the supported ones from the textComponent
    */
    private HashMap<Object, Action> createActionTable(JTextComponent textComponent) {
        HashMap<Object, Action> actions = new HashMap<Object, Action>();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
        return actions;
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

    private static class TestListener implements DocumentListener {
        public void insertUpdate(DocumentEvent evt) {
            System.out.print(evt.getType().toString() + " " + evt.getLength() + "; ");
            Element root = evt.getDocument().getDefaultRootElement();
            if(evt.getDocument() instanceof DefaultStyledDocument) {
                System.out.print(" is a DefaultStyledDocument; ");
            }
            System.out.println(root.getName() + "  has  " + root.getElementCount());
        }
        public void removeUpdate(DocumentEvent evt) {}
        public void changedUpdate(DocumentEvent evt) {}
    }
}
