import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class TestFilter extends DocumentFilter {
    public static Object charState = new Object();

    public Theme theme;

    public TestFilter() {
        super();
        try {theme = Theme.fromFile("default_theme.txt");}
        catch(Exception e) {theme = new Theme("tag-* color 0 0 255");}
    }

    public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
	System.out.println("invoked");
	SimpleAttributeSet attrs = new SimpleAttributeSet(attr);
        DefaultStyledDocument doc = (DefaultStyledDocument)fb.getDocument();
        Element context = doc.getCharacterElement((offset == 0)? offset : (offset - 1));
        replaceAll(fb, offset, length, parseInsertion(getContext(context), str), attr);
	//attrs.removeAttribute(AbstractDocument.ElementNameAttribute);
	//attrs.addAttribute(AbstractDocument.ElementNameAttribute, "customName");
	//super.replace(fb, offset, length, str, attrs);
        /*if(offset % 2 == 0) {
	  StyleConstants.setBold(attrs, true);
	  StyleConstants.setForeground(attrs, Color.red);
	  super.replace(fb, offset, length, str, attrs);
	  } else {
	  StyleConstants.setBold(attrs, false);
	  StyleConstants.setForeground(attrs, Color.black);
	  super.replace(fb, offset, length, str, attrs);
	  }*/
	/*
	  int ind = EditorArea.getEditor().indexOf(keyWord);
	  if(ind != 0){
	  StyleConstants.setBold(bold, true);
	  super.replace(fb, ind, keyWord.length(), keyWord, bold);
	  }
	*/
    }

    /**
    * Usually called when the setText() method is invoked. In this
    * program, that only happens when a new document is opened, so
    * it is assumed that the previous context should be reset to text
    */
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        String[][] pieces = parseInsertion("", string); //ignore previous style information
        for(String[] piece : pieces) {
            SimpleAttributeSet named = new SimpleAttributeSet(attr);
            named.addAttribute(AbstractDocument.ElementNameAttribute, piece[0]);
            named.addAttributes(theme.getStyle(piece[0]));
            super.insertString(fb, offset, piece[1], named);
            offset += piece[1].length();
        }
    }

    /**
     * deprecated, here for legacy purposes. charState is no longer used.
     */
    public String getContext(AttributeSet set) {
        if(set.isDefined(charState)) {return (String)(set.getAttribute(charState));}
        return "";
    }

    /**
     * converts the name of the element to a context name
     */
    public String getContext(Element elem) {
        if(elem.getName().equals("content")) {return "";}
        return elem.getName();
    }

    /**
     * Inserts each "piece" of the 2d array individually, making a new element each time
     * Also pads newlines for auto-indentation
     * @param pieces A 2d string array in the format returned by parseInsertion
     */
    public void replaceAll(FilterBypass fb, int offset, int length, String[][] pieces, AttributeSet set) throws BadLocationException {
        if(pieces.length == 1 && pieces[0][1].equals("\n")) { //indentation logic
            int indent = ((SyntaxHighlighterDoc)fb.getDocument()).getIndentation(offset);
            while(indent > 0) {
                pieces[0][1] += " ";
                indent--;
            }
        }
        for(String[] piece : pieces) {
            SimpleAttributeSet named = new SimpleAttributeSet(set);
            //System.out.println("Name of \"" + piece[1] + "\" is " + set.getAttribute(AbstractDocument.ElementNameAttribute));
            named.removeAttribute(AbstractDocument.ElementNameAttribute);
            named.addAttribute(AbstractDocument.ElementNameAttribute, piece[0]);
            named.addAttributes(theme.getStyle(piece[0]));
            super.replace(fb, offset, length, piece[1], named);
        }
    }

    /**
     * based on the context, returns and modifies
     * an attributeset to have syntax highlighting.
     */
    public AttributeSet getStyle(String context, AttributeSet parent) {
        SimpleAttributeSet style = new SimpleAttributeSet(parent);
        switch(context) {
            case "":
                StyleConstants.setForeground(style, Color.black);
            break;
            case "tag-start":
                StyleConstants.setForeground(style, Color.blue);
                break;
            case "tag-name":
                StyleConstants.setForeground(style, Color.blue);
                break;
            case "attribute-name":
                StyleConstants.setForeground(style, Color.magenta);
                break;
            case "attribute-separator":
                StyleConstants.setForeground(style, new Color(144, 19, 156));
                break;
            case "attribute-value":
                StyleConstants.setForeground(style, new Color(27, 127, 209));
                break;
            case "attribute-value-quoted":
                StyleConstants.setForeground(style, new Color(27, 127, 209));
                break;
            case "html-comment":
                StyleConstants.setForeground(style, new Color(100, 100, 100));
                break;
            case "tag-end":
                StyleConstants.setForeground(style, Color.blue);
        }
        return style;
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
                current = current.substring(1);
                previous = current;
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
     * based on the context it was inserted into and contents
     * of the current context
     */
    public String determineState(String previous, String content, char c) {
        if(previous.equals("")) {
            if(c == '<') {return "tag-start";}
        }
        if(previous.equals("tag-start")) {
            if(c == '>') {return "tag-end";}
            else if(Character.isWhitespace(c)) {return "tag-start";}
            else {return "tag-name";}
        }
        if(previous.equals("tag-name")) {
            if(Character.isWhitespace(c)) {return "tag-content";}
            else if(c == '>') {return "tag-end";}
            else if((content + c).equals("!--")) {return "!html-comment";}
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
            if(c == '"') {return "attribute-value-quoted";}
            if(c == '>') {return "tag-end";}
            if(c == ' ') {return "attribute-name";}
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
        if(previous.equals("html-comment")) {
            if((content + c).endsWith("-->")) {return "html-comment-end";}
            return "html-comment";
        }
        if(previous.equals("html-comment-end")) {
            if(c == '<') {return "tag-start";}
        }
        return "";
    }

    /*public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
      System.out.println("insert");
      }*/
}
