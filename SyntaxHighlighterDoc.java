import javax.swing.text.*;

public class SyntaxHighlighterDoc extends DefaultStyledDocument {
    protected void insert(int offset, DefaultStyledDocument.ElementSpec[] data){
        System.out.println("Insert (" + offset + ", " + data.length);
    }

    public SyntaxHighlighterDoc() {
	System.out.println("Empty constructor");
    }

    public SyntaxHighlighterDoc(AbstractDocument.Content c, StyleContext styles) {
        System.out.println("Content and styles");
    }

    public SyntaxHighlighterDoc(StyleContext styles) {
        System.out.println("Just styles");
    }
}
