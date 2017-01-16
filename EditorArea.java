import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 * Main component for displaying the area for editing text. Receives actions and
 * allows for interaction. Also has more appealing text font and size
 */
public class EditorArea extends JPanel {
	private String fontName;
	private int fontSize;
	private JTextPane ePane;
	private StyledDocument doc;
	private SimpleAttributeSet attrs;
	private Font font;
	private TestFilter tf;
	private LinePanel lineNums;
	private HTMLVisualizer visuals;
	private FileExplorer fe;
	private FileUpdateChecker checker;

	public EditorArea(String fontName, int fontSize) {
		super(new BorderLayout());
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.lineNums = lineNums;

		doc = new DefaultStyledDocument();
		ePane = new JTextPane(doc);
		tf = new TestFilter();

		font = new Font(fontName, Font.PLAIN, fontSize);
		ePane.setFont(font);

		/*
		 * - - - - - KEYBIND INPUT/ACTION(MAP) FORMAT - - - - -
		 * ePane.getInputMap().put(KeyStroke.getKeyStroke("A"),"keyA");
		 * ePane.getActionMap().put("keyA", new KeyAction("A")); Registers the
		 * key "A" and advances to KeyAction class for action
		 */
		ePane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), "keyCopy");
		ePane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK), "emacs-up");
		ePane.getActionMap().put("keyCopy", new KeyAction("copy"));
		ePane.getActionMap().put("emacs-up", new KeyAction("emacs-up"));

		this.add(ePane);

		doc.addDocumentListener(new TestListener());
		((AbstractDocument) doc).setDocumentFilter(tf);

		attrs = new SimpleAttributeSet();
		// Tests
		// System.out.println(JEditorPane.getEditorKitClassNameForContentType("x-code/html"));
		ePane.setCharacterAttributes(attrs, true);
		ePane.revalidate();
	}

	public String getText() {
		return ePane.getText();
	}

	public void setVisuals(HTMLVisualizer visuals) {
		this.visuals = visuals;
	}

	public void setFileExplorer(FileExplorer fe) {
		this.fe = fe;
	}

	public void setFileUpdateChecker(FileUpdateChecker checker) {
		this.checker = checker;
	}

	public void setText(String contents) {
		ePane.setText(contents);
	}

	public void setLinePanel(LinePanel linePanel) {
		lineNums = linePanel;
	}

	public void appendText(String contents) {
		ePane.setText(ePane.getText() + contents);
	}

	public void changeFont(String fontname, int size) {
		fontSize = size;
		font = new Font(fontname, Font.PLAIN, fontSize);
		ePane.setFont(font);
		lineNums.changeFont(fontname, fontSize);
	}

	public int getFontSize() {
		return fontSize;
	}

	public String getCurrentFont() {
		return font.getFontName();
	}

	public void addFontStyle(Boolean italics, Boolean bold, String style) {
		if (italics) {
			attrs.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
		}
		if (bold) {
			attrs.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
		}
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
	public class KeyAction extends AbstractAction {
		private String sequence;

		public KeyAction(String sequence) {
			this.sequence = sequence;
		}

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

	private class TestListener implements DocumentListener {
		public void insertUpdate(DocumentEvent evt) {
			/*
			 * System.out.print(evt.getType().toString() + " " + evt.getLength()
			 * + "; "); Element root =
			 * evt.getDocument().getDefaultRootElement();
			 * System.out.println(root.getName() + "  has  " +
			 * root.getElementCount() + "; " + evt.getLength());
			 */

			lineNums.update();
			try {
				fe.setRead(false);
				if (checker.isFileSet()) {
					fe.setAutoOverwrite(true, checker.returnFile());
					if (fe.revealExplorer()) {
						visuals.updateVisualizer(fe.getFileName());
						fe.disableAutoOverwrite();
						checker.init(new File(fe.getFileName()), getText());
						System.out.println("File saved to: " + fe.getFileName());
					}
				} else {
					visuals.displayNone();
				}
			} catch (NullPointerException e) {
				System.out.println("Save or open an HTML file to view visually!");
			}
		}

		public void removeUpdate(DocumentEvent evt) {
			lineNums.update();
		}

		public void changedUpdate(DocumentEvent evt) {
		}
	}
}