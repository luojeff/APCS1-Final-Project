import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class TestFilter extends DocumentFilter {
    public static Object charState = new Object();

    public TestFilter() {
        super();
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

    public String getContext(AttributeSet set) {
        if(set.isDefined(charState)) {return (String)(set.getAttribute(charState));}
        return "";
    }

    public String getContext(Element elem) {
        if(elem.getName().equals("content")) {return "";}
        return elem.getName();
    }

    public void replaceAll(FilterBypass fb, int offset, int length, String[][] pieces, AttributeSet set) throws BadLocationException {
        for(String[] piece : pieces) {
            SimpleAttributeSet named = new SimpleAttributeSet(set);
            //System.out.println("Name of \"" + piece[1] + "\" is " + set.getAttribute(AbstractDocument.ElementNameAttribute));
            named.removeAttribute(AbstractDocument.ElementNameAttribute);
            named.addAttribute(AbstractDocument.ElementNameAttribute, piece[0]);
            super.replace(fb, offset, length, piece[1], getStyle(piece[0], named));
        }
    }

    public AttributeSet getStyle(String context, AttributeSet parent) {
        SimpleAttributeSet style = new SimpleAttributeSet(parent);
        switch(context) {
            case "":
                StyleConstants.setForeground(style, Color.black);
            break;
            case "tag":
                StyleConstants.setForeground(style, Color.blue);
                break;
            case "attribute-name":
                StyleConstants.setForeground(style, Color.magenta);
                break;
            case "attribute-value":
                StyleConstants.setForeground(style, Color.cyan);
                break;
            case "attribute-value-quoted":
                StyleConstants.setForeground(style, Color.cyan);
                break;
        }
        return style;
    }

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

    public String determineState(String previous, char c) {
        if(previous.equals("")) {
            if(c == '<') {return "tag";}
        }
        if(previous.equals("tag")) {
            if(c == '>') {return "tag-end";}
            else if(c == ' ' || c == '\t' || c == '\n') {return "attribute-name";}
            else {return "tag";}
        }
        if(previous.equals("attribute-name")) {
            if(c == '>') {return "tag-end";}
            else if(c == '=') {return "attribute-value";}
            else {return "attribute-name";}
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
        return "";
    }

    /*public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
	System.out.println("insert");
      }*/
}
