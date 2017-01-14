import javax.swing.text.*;

public class SyntaxHighlighterDoc extends DefaultStyledDocument {
    private Theme theme;

    protected void insert(int offset, DefaultStyledDocument.ElementSpec[] data) throws BadLocationException {
        super.insert(offset, data);
        System.out.println("Insert (" + offset + ", " + data.length);
    }

    //this one gets used by the system
    protected void insertUpdate(AbstractDocument.DefaultDocumentEvent evt, AttributeSet set) {
        System.out.println("insertUpdate");
        super.insertUpdate(evt, set);
    }

    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);
        System.out.println("inserString("+offset+","+str+")");
    }

    public SyntaxHighlighterDoc() {
        super();
        constructor();
	    //System.out.println("Empty constructor");
    }

    public SyntaxHighlighterDoc(AbstractDocument.Content c, StyleContext styles) {
        super(c, styles);
        constructor();
        System.out.println("Content and styles");
    }

    public SyntaxHighlighterDoc(StyleContext styles) {
        super(styles);
        constructor();
        System.out.println("Just styles");
    }

    private void constructor() {
        try {
            theme = Theme.fromFile("default_theme.txt");
        } catch(Exception e) {
            theme = new Theme("tag-* color 0 0 255");
        }
    }
}
