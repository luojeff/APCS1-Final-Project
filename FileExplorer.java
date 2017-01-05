import java.io.BufferedReader;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

/**
 * Opens a file explorer which allows for the user to either choose a file and
 * copy the contents into the text component or choose a directory to save a
 * file into
 */
public class FileExplorer {

    private File file;
    private String contents = "";
    private Boolean read;
    private EditorArea textField;

    public FileExplorer(Boolean read, EditorArea textField) {
	this.read = read;
	this.textField = textField;
    }

    public void revealExplorer() {
	// JDialog.setDefaultLookAndFeelDecorated(false);
	JFileChooser fileChooser = new JFileChooser();
	if (read) {
	    int val = fileChooser.showOpenDialog(null);
	    if (val == JFileChooser.APPROVE_OPTION) {
		file = fileChooser.getSelectedFile();
		readFile(file);
	    }
	} else {
	    int val = fileChooser.showSaveDialog(null);
	    if (val == JFileChooser.APPROVE_OPTION) {
		file = fileChooser.getSelectedFile();
		if (!file.exists()) {
		    writeFile(file, textField.getText());
		} else {
		    int response = JOptionPane.showConfirmDialog(null, "Do you want to overwrite an existing file?",
								 "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.YES_OPTION) {
			writeFile(file, textField.getText());
		    }
		}
	    }
	}
    }

    public String getContents() {
	return contents;
    }

    public void readFile(File file) {
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(file));
	    String line = reader.readLine();
	    while (line != null) {
		contents += line + '\n';
		line = reader.readLine();
	    }
	    reader.close();
	} catch (FileNotFoundException e) {
	    System.out.println("File not found!");
	} catch (IOException e) {
	    System.out.println("Error in parsing file!");
	}
    }

    public void writeFile(File newFile, String content) {
	try {
	    if (!newFile.exists()) {
		newFile.createNewFile();
	    }
	    BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
	    writer.write(content);
	    writer.close();
	} catch (IOException e) {
	    System.out.println("Error in parsing file!");
	}
    }
}
