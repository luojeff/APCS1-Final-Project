import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class TestFilter extends DocumentFilter {
    public TestFilter() {
        super();
    }

    public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
        System.out.println("invoked");
	    SimpleAttributeSet attrs = new SimpleAttributeSet(attr);
        DefaultStyledDocument doc = (DefaultStyledDocument)fb.getDocument();
        Element context = doc.getCharacterElement(offset);
	    attrs.removeAttribute(AbstractDocument.ElementNameAttribute);
	    attrs.addAttribute(AbstractDocument.ElementNameAttribute, "customName");
	    super.replace(fb, offset, length, str, attrs);
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

    public String[][] parseInsertion(String context, String insertion) {
        ArrayList<String[]> pieces = new ArrayList<String[]>();
        int i = 0;
        while(i < insertion.length()) {
            i++;
        }
        return pieces.toArray(new String[0][]);
    }

    /*public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
	System.out.println("insert");
      }*/
}
