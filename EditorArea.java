import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
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
    private StyledDocument doc;
    private SimpleAttributeSet attrs;
    private Font font;
    private TestFilter tf;

    public EditorArea() {
	super(new BorderLayout());

	doc = new DefaultStyledDocument();
	ePane = new JTextPane(doc);
	font = new Font("Consolas", Font.PLAIN, 18);
	tf = new TestFilter();
	ePane.setFont(font);

	/* - - - - - KEYBIND INPUT/ACTION(MAP) FORMAT - - - - -
	 * ePane.getInputMap().put(KeyStroke.getKeyStroke("A"),"keyA");
	 * ePane.getActionMap().put("keyA", new KeyAction("A"));
	 * Registers the key "A" and advances to KeyAction class for action
	 */ 
	ePane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), "keyCopy");
	ePane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK), "emacs-up");
	ePane.getActionMap().put("keyCopy", new KeyAction("copy"));
	ePane.getActionMap().put("emacs-up", new KeyAction("emacs-up"));

	this.add(ePane);

	doc.addDocumentListener(new TestListener());
	((AbstractDocument)doc).setDocumentFilter(tf);
	
	attrs = new SimpleAttributeSet();
	//trs.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
	//trs.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);	
		
	// Tests
	// System.out.println(JEditorPane.getEditorKitClassNameForContentType("x-code/html"));
	
	ePane.setCharacterAttributes(attrs, true);
	ePane.revalidate();
    }

    public String getText() {
	return ePane.getText();
    }

    public void insertAS(){
	try {
	    doc.insertString(1, "Something", attrs);
	} catch(BadLocationException e){
	    e.printStackTrace();
	}
    }
	
    public JTextPane getEditor(){
	return ePane;
    }

    public void setText(String contents) {
	ePane.setText(contents);
    }

    public void appendText(String contents) {
	ePane.setText(ePane.getText() + contents);
    }

    public void changeFont(String fontname, int size){
	font = new Font(fontname, Font.PLAIN, size);
	ePane.revalidate();
    }

    public void addFontStyle(Boolean italics, Boolean bold, String style){
        attrs.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
	attrs.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.green);
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
	    switch (sequence) {
	    case "copy":
		System.out.println("Copied (REPLACE)");
		break;
	    case "emacs-up":
		System.out.println("Cursor should move up");
		break;
	    }
	}
    }

	
    private static class TestListener implements DocumentListener {
	public void insertUpdate(DocumentEvent evt) {
	    System.out.print(evt.getType().toString() + " " + evt.getLength()
			     + "; "); 
	    Element root = evt.getDocument().getDefaultRootElement();
	    System.out.println(root.getName() + "  has  " +
			       root.getElementCount() + "; " + evt.getLength());
	    
	}

	public void removeUpdate(DocumentEvent evt) {
	}

	public void changedUpdate(DocumentEvent evt) {
	    System.out.println(evt.getLength());
	}
    }
}
