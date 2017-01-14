import javax.swing.text.*;
import java.awt.Color;

public class SyntaxHighlighterDoc extends DefaultStyledDocument {
    private Theme theme;

    protected void insert(int offset, DefaultStyledDocument.ElementSpec[] data) throws BadLocationException {
        super.insert(offset, data);
        System.out.println("Insert (" + offset + ", " + data.length);
    }

    //this one gets used by the system
    protected void insertUpdate(AbstractDocument.DefaultDocumentEvent evt, AttributeSet set) {
        System.out.println("insertUpdate");
        int offset = ((evt.getOffset() == 0)? 0 : evt.getOffset() - 1), length = evt.getLength();
        System.out.println("off: "+offset+", len: "+length+", doc:"+this.getLength());
        SimpleAttributeSet attrs = new SimpleAttributeSet(set);
        attrs.addAttribute(AbstractDocument.ElementNameAttribute, "testing");
        StyleConstants.setForeground(attrs, Color.red);
        super.insertUpdate(evt, attrs);
        // try {
        //     this.insert(offset, new DefaultStyledDocument.ElementSpec[] {
        //         elemSpec(attrs, "", "", length)
        //     });
        // } catch(BadLocationException e) {
        //     System.out.println("Error");
        //     e.printStackTrace();
        // }
    }

    private DefaultStyledDocument.ElementSpec elemSpec(AttributeSet set, String type, String dir, int len) {
        DefaultStyledDocument.ElementSpec result;
        short t = DefaultStyledDocument.ElementSpec.ContentType;
        short d = DefaultStyledDocument.ElementSpec.OriginateDirection;
        if(type.equals("start")) {t = DefaultStyledDocument.ElementSpec.StartTagType;}
        else if(type.equals("end")) {t = DefaultStyledDocument.ElementSpec.EndTagType;}
        if(dir.equals("fracture")) {t = DefaultStyledDocument.ElementSpec.JoinFractureDirection;}
        else if(dir.equals("next")) {t = DefaultStyledDocument.ElementSpec.JoinNextDirection;}
        else if(dir.equals("prev")) {t = DefaultStyledDocument.ElementSpec.JoinPreviousDirection;}
        result = new DefaultStyledDocument.ElementSpec(set, t, len);
        result.setDirection(d);
        return result;
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
