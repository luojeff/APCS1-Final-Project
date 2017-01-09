import java.awt.*;
import javax.swing.*;
import javax.swing.text.StyledDocument;

/**
 * Panel that displays line numbers
 */
public class LinePanel extends JPanel {
    private int[] lineNumsArray;
    private JTextPane numTextPane;
    private JPanel numPanel;
    private Font font;
    private int fontSize;

    public LinePanel() {
	super(new BorderLayout());
	
	numTextPane = new JTextPane();
	numTextPane.setEditable(false);
	
        numPanel = new JPanel(new BorderLayout());
	numPanel.add(numTextPane);
	
	this.add(numTextPane, BorderLayout.EAST);
        
	lineNumsArray = new int[30];

	for(int i = 0; i < lineNumsArray.length; i++){
	    lineNumsArray[i] = i+1;
	}

	for(int i : lineNumsArray){
	    if(!numTextPane.getText().equals("")){
		numTextPane.setText(numTextPane.getText() + '\n' + Integer.toString(i));
	    } else {
		numTextPane.setText("1");
	    }
	}
    }

    public void changeFont(String fontname, int size){
	fontSize = size;
	font = new Font(fontname, Font.PLAIN, size);
	numTextPane.setFont(font);
    }
}
