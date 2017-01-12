import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.util.regex.*;

/**
 * Panel that displays line numbers
 */
public class LinePanel extends JPanel {
    private JTextPane numTextPane;
    private JPanel numPanel;
    private Font font;
    private int fontSize;
    private EditorArea editorArea;
    private Window w;
    private int numReturns;
    private SimpleAttributeSet attrs;

    public LinePanel() {
	super(new BorderLayout());

	numTextPane = new JTextPane();
	numTextPane.setEditable(false);
	
        numPanel = new JPanel(new BorderLayout());
	numPanel.add(numTextPane);

	attrs = new SimpleAttributeSet();
	attrs.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.GRAY);
	numTextPane.setCharacterAttributes(attrs, true);
	
	this.add(numTextPane, BorderLayout.EAST);
    }
    
    public LinePanel(EditorArea editorArea) {
        this();
	this.editorArea = editorArea;
	update();
    }

    public void changeFont(String fontname, int size){
	fontSize = size;
	font = new Font(fontname, Font.PLAIN, size);
	numTextPane.setFont(font);
    }

    public static int countOccurrences(String content, String s){
	int i = 0;
	Pattern pattern = Pattern.compile(s);
	Matcher matcher = pattern.matcher(content);
	while (matcher.find()) {
	    i++;
	}
        return i;
    }

    public void update(){
	int numReturns = countOccurrences(editorArea.getText(), "\n");
	
        numTextPane.setText("1");
	for(int i=2; i<numReturns+2; i++){
	    numTextPane.setText(numTextPane.getText() + '\n' + Integer.toString(i));
	}
    }
}
