import javax.swing.text.*;
import java.awt.*;

public class TestFilter extends DocumentFilter {
    public TestFilter() {
        super();
    }

    public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
        System.out.println("invoked");
	SimpleAttributeSet attrs = new SimpleAttributeSet(attr);

	String keyWord = "class";
	
        if(offset % 2 == 0) {
            StyleConstants.setBold(attrs, true);
	    StyleConstants.setForeground(attrs, Color.red);
            super.replace(fb, offset, length, str, attrs);
        } else {
	    StyleConstants.setBold(attrs, false);
	    StyleConstants.setForeground(attrs, Color.black);
            super.replace(fb, offset, length, str, attrs);
	    
        }

	/*

	int ind = EditorArea.getEditor().indexOf(keyWord);
	if(ind != 0){
	    StyleConstants.setBold(bold, true);
	    super.replace(fb, ind, keyWord.length(), keyWord, bold);
	}
	*/
    }
}
