import javax.swing.text.*;

public class TestFilter extends DocumentFilter {
    public TestFilter() {
        super();
    }

    public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
        System.out.println("invoked");
        if(offset % 3 == 0) {
            SimpleAttributeSet bold = new SimpleAttributeSet(attr);
            StyleConstants.setBold(bold, true);
            super.insertString(fb, offset, str, bold);
        } else {
            super.insertString(fb, offset, str, attr);
        }
    }
}