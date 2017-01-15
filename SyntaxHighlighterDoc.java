import javax.swing.text.*;
import javax.swing.text.AbstractDocument.BranchElement;
import javax.swing.text.AbstractDocument.LeafElement;
import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;

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
        super.insertUpdate(evt, set);
        //colorLine(getParagraphElement(offset), set);
        // try {
        //     this.insert(offset, new DefaultStyledDocument.ElementSpec[] {
        //         elemSpec(attrs, "", "", length)
        //     });
        // } catch(BadLocationException e) {
        //     System.out.println("Error");
        //     e.printStackTrace();
        // }
    }

    public void colorLine(Element l, AttributeSet set) {
        AbstractDocument.BranchElement line = (AbstractDocument.BranchElement)l;
        SimpleAttributeSet attrs = new SimpleAttributeSet(set);
        attrs.removeAttribute(AbstractDocument.ElementNameAttribute);
        attrs.addAttribute(AbstractDocument.ElementNameAttribute, "custom");
        Random rng = new Random();
        StyleConstants.setForeground(attrs, new Color(rng.nextInt(100), rng.nextInt(100), rng.nextInt(100)));
        int start = line.getStartOffset();
        int end = line.getEndOffset();
        int lineLength = end - start;
        if(end > this.getLength()) {end--;}
        Element named = createLeafElement(line, attrs, start, end);
        line.replace(0, line.getElementCount() - 1, new Element[] {named});
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

    private void parseLine(BranchElement line) {
        /*
        get each element of the line --> $1
        for each element:
            get its contents as a string
            run string through a modified parseInsertions
        create an array of elements based on those results --> $2
        if the two arrays not the same:
            use line.replace(0, $1.length, $2)
        */
    }

    /**
    * Checks if two sets of elements are equivalent in value.
    * Has no care for object references.
    */
    private boolean same(Element[] e1, Element[] e2) {
        if(e1.length != e2.length) {return false;}
        for(int i = 0; i < e1.length; i++) {
            if(!e1[i].getAttributes().isEqual(e2[i].getAttributes())) {
                return false;
            }
        }
        return true;
    }

    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        //super.insertString(offset, str, a);
        //System.out.println("inserString("+offset+","+str+")");
        Element context = this.getCharacterElement((offset == 0)? offset : offset - 1);
        insertAll(offset, parseInsertion(getContext(context), str), a);
    }

    public void insertAll(int offset, String[][] pieces, AttributeSet set) throws BadLocationException {
        for(String[] piece : pieces) {
            SimpleAttributeSet named = new SimpleAttributeSet(set);
            named.removeAttribute(AbstractDocument.ElementNameAttribute);
            named.addAttribute(AbstractDocument.ElementNameAttribute, piece[0]);
            named.addAttributes(theme.getStyle(piece[0]));
            super.insertString(offset, piece[1], named);
            offset += piece[1].length();
        }
    }

    public String getContext(Element elem) {
        if(elem.getName().equals("content")) {return "";}
        return elem.getName();
    }

    /**
     * Assigns contexts to the different structures within the string.
     * Also goes through the string and breaks it up into syntax structures.
     * @param context   the context that the insertion was inserted into
     * @param insertion the string to parse
     * @return a 2d string array where each String[] is of length two.
     *         the first string is the context, and the second string
     *         is the thing that context is describing. Example:<br>
     *         <code>{context, string}</code>
     */
    public String[][] parseInsertion(String context, String insertion) {
        ArrayList<String[]> pieces = new ArrayList<String[]>();
        String previous = context, current = "", building = "";
        int i = 0;
        while(i < insertion.length()) {
            current = determineState(previous, insertion.charAt(i));
            //System.out.print("  Determined '" + current + "' from '" + insertion.charAt(i) + "' in '" + previous + "'");
            if(previous.equals(current)) {
                //System.out.println(";  Continuing \"" + building + "\" in '" + current + "' with '" + insertion.charAt(i));
                building += insertion.substring(i, i+1);
            } else {
                if(building.length() > 0) {
                    String[] p = {current, building};
                    pieces.add(p);
                }
                building = insertion.substring(i, i+1);
                previous = current;
                //System.out.println(";  changed to state " + current);
            }
            i++;
        }
        String[] p = {current, building};
        pieces.add(p);
        //String[] s = {"", insertion};
        //pieces.add(s);
        return pieces.toArray(new String[0][]);
    }

    /**
     * determines the context that this character should have
     * based on the context it was inserted into
     */
    public String determineState(String previous, char c) {
        if(previous.equals("")) {
            if(c == '<') {return "tag-start";}
        }
        if(previous.equals("tag-start")) {
            if(c == '>') {return "tag-end";}
            else if(c == '!') {return "tag-special";}
            else if(Character.isWhitespace(c)) {return "tag-start";}
            else {return "tag-name";}
        }
        if(previous.equals("tag-special")) {
            if(c == '-') {return "tag-special-comment-partial";}
            return "tag-name";
        }
        if(previous.equals("tag-special-comment-partial")) {
            if(c == '-') {return "html-comment";}
            return "tag-name";
        }
        if(previous.equals("tag-name")) {
            if(Character.isWhitespace(c)) {return "attribute-name";}
            else if(c == '>') {return "tag-end";}
            else {return "tag-name";}
        }
        if(previous.equals("attribute-name")) {
            if(c == '>') {return "tag-end";}
            else if(c == '=') {return "attribute-separator";}
            else {return "attribute-name";}
        }
        if(previous.equals("attribute-separator")) {
            if(c == '>') {return "tag-end";}
            if(c == '"') {return "attribute-value-quoted";}
            return "attribute-value";
        }
        if(previous.equals("attribute-value")) {
            if(c == '"') {return "attribute-value-quoted";}
            if(c == '>') {return "tag-end";}
            if(c == ' ') {return "attribute-name";}
            return "attribute-value";
        }
        if(previous.equals("attribute-value-quoted")) {
            if(c == '"') {return "attribute-value";}
            return "attribute-value-quoted";
        }
        if(previous.equals("tag-end")) {
            if(c == '<') {return "tag-start";}
        }
        if(previous.equals("html-comment")) {
            if(c == '-') {return "html-comment-1";}
            return "html-comment";
        }
        if(previous.equals("html-comment-1")) {
            if(c == '-') {return "html-comment-2";}
            return "html-comment";
        }
        if(previous.equals("html-comment-2")) {
            if(c == '>') {return "tag-end";}
            return "html-comment";
        }
        return "";
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
