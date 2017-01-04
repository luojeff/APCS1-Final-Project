import javax.swing.text.*;

public class TestFilter extends DocumentFilter {
    public TestFilter() {
        super();
    }

    public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
        System.out.println("invoked");
	SimpleAttributeSet bold = new SimpleAttributeSet(attr);
        if(offset > 10 && offset < 15) {
            StyleConstants.setBold(bold, true);
            super.replace(fb, offset, length, str, bold);
        } else {
            super.replace(fb, offset, length, str, attr);
        }
    }
}
