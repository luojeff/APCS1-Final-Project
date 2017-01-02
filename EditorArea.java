import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Main component for displaying the area for editing text. Receives actions and
 * allows for interaction. Also has more appealing text font and size
 */
public class EditorArea extends JPanel {
    JEditorPane ePane;
    static SimpleAttributeSet attrs;

    public EditorArea() {
	super(new BorderLayout());

	ePane = new JEditorPane("text/html_code", "");
	ePane.setDocument(new DefaultStyledDocument());
	ePane.setFont(new Font("Consolas", Font.PLAIN, 18));

	// Demo: ePane.getInputMap().put(KeyStroke.getKeyStroke("A"),"keyA");
	// ePane.getActionMap().put("keyA", new KeyAction("A"));
	// -- registers the key "A" and advances to KeyAction class for action
	ePane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), "keyCopy");
	ePane.getActionMap().put("keyCopy", new KeyAction("copy"));

	this.add(ePane);

	Document doc = ePane.getDocument();
	doc.addDocumentListener(new TestListener());
	attrs = new SimpleAttributeSet();
		
	ePane.revalidate();

	// Tests
	// System.out.println(JEditorPane.getEditorKitClassNameForContentType("x-code/html"));
    }

    public String getText() {
	return ePane.getText();
    }

    public void setText(String contents) {
	ePane.setText(contents);
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
	    this.updateText();
	}

	private void updateText() {
	    attrs.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
	}

	public void removeUpdate(DocumentEvent evt) {
	}

	public void changedUpdate(DocumentEvent evt) {
	    System.out.println(evt.getLength());
	}
    }
}
