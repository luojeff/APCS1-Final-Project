import javax.swing.*;
import javax.swing.text.*;
import java.io.*;

public class HTMLVisualizer extends JPanel {
	private JEditorPane visualizer;
	private JPanel visualizerPanel;
	private JScrollPane htmlScroller;

	public HTMLVisualizer(JPanel visualizerPanel) {
		this.visualizerPanel = visualizerPanel;
	}

	public void setupVisualizer(String fileName) {
		visualizer = new JEditorPane();
		visualizer.setEditable(false);
		visualizer.setContentType("text/html");
		htmlScroller = new JScrollPane(visualizer);
		htmlScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		visualizerPanel.add(visualizer);
	}

	public void updateVisualizer(String fileName) {
		try {
			Document doc = visualizer.getDocument();
			doc.putProperty(Document.StreamDescriptionProperty, null);
			System.out.println("Attempted Run");
			visualizer.setPage((new File(fileName)).toURI().toURL());
		} catch (IOException e) {
			System.out.println("Can't retrieve HTML!");
		}
	}
	
	public void displayNone(){
		visualizer.setText("<h1>Open or save an HTML file to view!</h1>");
	}
}
