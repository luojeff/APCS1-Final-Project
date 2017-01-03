import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 * Main component for displaying the area for editing text. Receives actions and
 * allows for interaction. Also has more appealing text font and size
 */
public class EditorArea extends JPanel {
    private JTextPane ePane;
    private SimpleAttributeSet attrs;
    private Font font;

    public EditorArea() {
	super(new BorderLayout());

	StyledDocument doc = new DefaultStyledDocument();
	ePane = new JTextPane(doc);
	font = new Font("Consolas", Font.PLAIN, 18);
	ePane.setFont(font);

	// Demo: ePane.getInputMap().put(KeyStroke.getKeyStroke("A"),"keyA");
	// ePane.getActionMap().put("keyA", new KeyAction("A"));
	// -- registers the key "A" and advances to KeyAction class for action
	ePane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), "keyCopy");
	ePane.getActionMap().put("keyCopy", new KeyAction("copy"));

	this.add(ePane);

	//Document doc = ePane.getDocument();
	doc.addDocumentListener(new TestListener());
	attrs = new SimpleAttributeSet();
	attrs.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
	attrs.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);
	ePane.setCharacterAttributes(attrs, true);
		
	// Tests
	// System.out.println(JEditorPane.getEditorKitClassNameForContentType("x-code/html"));
	ePane.revalidate();
    }

    public String getText() {
	return ePane.getText();
>>>>>>> p-testing
    }
	
    public JTextPane getEditor(){
	return ePane;
    }

    public void setText(String contents) {
	ePane.setText(contents);
    }

    public void changeFont(String fontname, int size){
	font = new Font(fontname, Font.PLAIN, size);
    }
    
    /**
     * Creates a hashmap where actions are stored by name and includes all the
     * supported ones from the textComponent
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

>>>>>>> p-testing
    /**
     * Action command for when a certain key (or binding) is pressed.
     */
    public static class KeyAction extends AbstractAction {
	private String sequence;

	public KeyAction(String sequence) {
	    this.sequence = sequence;
	};

	public void actionPerformed(ActionEvent e) {
	    if (sequence.equals("copy")) {
		System.out.println("Highlighted text copied!");
	    }
	}
    }

	
    private static class TestListener implements DocumentListener {
	public void insertUpdate(DocumentEvent evt) {
	    /*
	     * System.out.print(evt.getType().toString() + " " + evt.getLength()
	     * + "; "); Element root =
	     * evt.getDocument().getDefaultRootElement();
	     * System.out.println(root.getName() + "  has  " +
	     * root.getElementCount());
	     */

	    System.out.println(evt.getLength());
	}

	public void removeUpdate(DocumentEvent evt) {
	}

	public void changedUpdate(DocumentEvent evt) {
	    System.out.println(evt.getLength());
	}
    }
}
