import javax.swing.text.*;

public class SyntaxHighlighterDoc extends DefaultStyledDocument {
    protected void insert(int offset, DefaultStyledDocument.ElementSpec[] data) throws BadLocationException {
        super.insert(offset, data);
        System.out.println("Insert (" + offset + ", " + data.length);
    }

    protected void insertUpdate(AbstractDocument.DefaultDocumentEvent evt, AttributeSet set) {
	System.out.println("insertUpdate");
	super.insertUpdate(evt, set);
    }

    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);
        System.out.println("inserString("+offset+","+str+")");
    }

    public SyntaxHighlighterDoc() {
	    System.out.println("Empty constructor");
    }

    public SyntaxHighlighterDoc(AbstractDocument.Content c, StyleContext styles) {
        super(c, styles);
        System.out.println("Content and styles");
    }

    public SyntaxHighlighterDoc(StyleContext styles) {
        super(styles);
        System.out.println("Just styles");
    }
}
