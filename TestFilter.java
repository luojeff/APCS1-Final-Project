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
        Element context = doc.getCharacterElement(offset);
        replaceAll(fb, offset, length, parseInsertion(getContext(attr), str), attr);
	    /*attrs.removeAttribute(AbstractDocument.ElementNameAttribute);
	    attrs.addAttribute(AbstractDocument.ElementNameAttribute, "customName");
	    super.replace(fb, offset, length, str, attrs);*/
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

    public void replaceAll(FilterBypass fb, int offset, int length, String[][] pieces, AttributeSet set) throws BadLocationException {
        for(String[] piece : pieces) {
            super.replace(fb, offset, length, piece[1], getStyle(piece[0], set));
        }
    }

    public AttributeSet getStyle(String context, AttributeSet parent) {
        return parent;
    }

    public String[][] parseInsertion(String context, String insertion) {
        ArrayList<String[]> pieces = new ArrayList<String[]>();
        int i = 0;
        while(i < insertion.length()) {
            i++;
        }
        String[] s = {"", insertion};
        pieces.add(s);
        return pieces.toArray(new String[0][]);
    }

    /*public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
	System.out.println("insert");
      }*/
}
