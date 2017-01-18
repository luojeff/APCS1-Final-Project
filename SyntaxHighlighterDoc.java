import javax.swing.text.*;
import javax.swing.text.AbstractDocument.BranchElement;
import javax.swing.text.AbstractDocument.LeafElement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentEvent.*;
import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;
import java.util.Enumeration;

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
        BranchElement line = (BranchElement)getParagraphElement(offset);
        //if(offset > 8) {reParse((BranchElement)getParagraphElement(offset));}
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

    public int getStart(Element[] elems) {return elems[0].getStartOffset();}
    public int getEnd(Element[] elems)   {return elems[elems.length - 1].getStartOffset();}
    public String getText(Element e) {
        try {
            return getText(e.getStartOffset(), e.getEndOffset() - e.getStartOffset());
        } catch(Exception err) {
            return null;
        }
    }
    public int getIndentation(int offset) {
        BranchElement line = (BranchElement)getParagraphElement(offset);
        String text = getText(line);
        if(text != null) {
            int spaces = 0;
            while(spaces < text.length()) {
                if(text.charAt(spaces) == ' ') {spaces++;}
                else if(text.charAt(spaces) == '\t') {spaces += 4;}
                else {return spaces;}
            }
        }
        return 0;
    }
    public void setTheme(Theme newTheme) {
        this.theme = newTheme;
        ((TestFilter)getDocumentFilter()).theme = newTheme;
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

    private void reParse(BranchElement line) {
        /*
        get each element of the line --> $1
        for each element:
            get its contents as a string
            run string through a modified parseInsertions
        create an array of elements based on those results --> $2
        if the two arrays not the same:
            use line.replace(0, $1.length, $2)
        */
        Element[] before = new Element[line.getChildCount()];
        for(int i = 0; i < before.length; i++) {
            before[i] = line.getElement(i);
        }
        String[][] beforeRefine = unParse(before);
        String toRefine = "";
        try {
            toRefine = getText(line.getStartOffset(), line.getEndOffset() - line.getStartOffset());
        } catch(Exception err) {
            for(String[] row : beforeRefine) {
                toRefine += row[1];
            }
        }
        if(getLength() < line.getEndOffset()) { //acount for EOF element on last line
            toRefine = toRefine.substring(0, toRefine.length() - 1);
            before = subarray(before, 0, before.length - 1);
            beforeRefine = unParse(before);
        }
        String[][] afterRefine = refineHTML(beforeRefine[0][0], toRefine);
        System.out.println(same(beforeRefine, afterRefine));
        if(!same(beforeRefine, afterRefine)) {
            printArr2d(beforeRefine);
            printArr2d(afterRefine);
            Element[] after = createLeaves(afterRefine, line.getStartOffset(), line);
            System.out.println("Before-end: " + before[before.length - 1].getEndOffset() + "\tAfter-end: " + after[after.length - 1].getEndOffset());
            //line.replace(0, beforeRefine.length, after);
        }
    }

    private LeafElement[] createLeaves(String[][] contexts, int offset, BranchElement parent) {
        LeafElement[] result = new LeafElement[contexts.length];
        for(int i = 0; i < result.length; i++) {
            SimpleAttributeSet set = theme.getStyle(contexts[i][0]);
            set.addAttribute(AbstractDocument.ElementNameAttribute, contexts[i][0]);
            result[i] = new LeafElement(parent, set, offset, offset + contexts[i][1].length());
            offset += contexts[i][1].length();
        }
        return result;
    }

    private String[][] unParse(Element[] e) {
        String[][] result = new String[e.length][2];
        for(int i = 0; i < result.length; i++) {
            result[i][0] = getContext(e[i]);
            int start = e[i].getStartOffset(),
                end   = e[i].getEndOffset();
            try {
                result[i][1] = getText(start, end - start);
            } catch(Exception err) {
                result[i][1] = "";
            }
        }
        return result;
    }

    private Element[] subarray(Element[] arr, int start, int end) {
        Element[] result = new Element[end - start];
        for(int i = start; i < end; i++) {
            result[i - start] = arr[i];
        }
        return result;
    }

    private String[][] refineHTML(String firstState, String content) {
        ArrayList<String[]> pieces = new ArrayList<String[]>();
        String current = firstState, next, building = "";
        for(int i = 0; i < content.length(); i++) {
            next = determineContext(current, building, content.charAt(i));
            if(next.equals(current)) {
                building += content.charAt(i);
            } else {
                if(building.length() > 0) {
                    pieces.add(new String[] {current, building});
                }
                building = content.substring(i, i + 1);
                current = next;
            }
        }
        pieces.add(new String[] {current, building}); //add the last piece
        return pieces.toArray(new String[0][]);
    }

    private String determineContext(String current, String content, char c) {
        if(current.equals("tag-start")) {
            if(content.length() == 0) {
                if(c == '<') {return "tag-start";}
                return ""; //its just text, not actually tag start
            }
            if(Character.isWhitespace(c)) {return "error-whitespaceInTagName";}
            return "tag-name";
        }
        return determineState(current, content, c);
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

    private boolean same(String[][] p1, String[][] p2) {
        if(p1.length != p2.length) {return false;}
        for(int i = 0; i < p1.length; i++) {
            if(!p1[i][0].equals(p2[i][0]) || !p1[i][1].equals(p2[i][1])) {
                return false;
            }
        }
        return true;
    }

    public static void printArr2d(String[][] arr) {
        String str = "";
        for(String[] row : arr) {
            str += "[";
            for(int i = 0; i < row.length; i++) {
                if(i + 1 == row.length) {
                    str += "\"" + row[i] + "\"";
                } else {
                    str += "\"" + row[i] + "\", ";
                }
            }
            str += "]\n";
        }
        System.out.println(str);
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
            current = determineState(previous, building, insertion.charAt(i));
            if(current.startsWith("!")) {
                current = current.substring(1); //get rid of !
                previous = current; //change the context without making a new element
            }
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
    public String determineState(String previous, String content, char c) {
        if(previous.equals("")) {
            if(c == '<') {return "tag-start";}
        }
        if(previous.equals("tag-start")) {
            if(c == '>') {return "tag-end";}
            else if(Character.isWhitespace(c)) {return "error-whitespaceInTagName";}
            else {return "tag-name";}
        }
        if(previous.equals("tag-name")) {
            if(Character.isWhitespace(c)) {return "tag-content";}
            else if(c == '>') {return "tag-end";}
            else if((content + c).equals("!--")) {return "!comment-html";}
            else {return "tag-name";}
        }
        if(previous.equals("tag-content")) {
            if(c == '/') {return "tag-end-almost";}
            else if(Character.isWhitespace(c)) {return "tag-content";}
            else {return "attribute-name";}
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
            if(c == '"') {return "error-illegalQuote";}
            if(c == '>') {return "tag-end";}
            if(c == ' ') {return "tag-content";}
            return "attribute-value";
        }
        if(previous.equals("attribute-value-quoted")) {
            if(c == '"') {return "attribute-value";}
            return "attribute-value-quoted";
        }
        if(previous.equals("tag-end-almost")) {
            if(c == '>') {return "tag-end";}
            return "tag-end-almost";
        }
        if(previous.equals("tag-end")) {
            if(c == '<') {return "tag-start";}
        }
        if(previous.equals("comment-html")) {
            if(content.endsWith("--") && c == '>') {return "tag-end";}
            return "comment-html";
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
