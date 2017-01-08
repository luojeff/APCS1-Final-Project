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
    private Boolean read, autoOverwrite;
    private EditorArea textField;
    private String fileName;
    JFileChooser fileChooser;

    public FileExplorer(EditorArea textField) {
	this.textField = textField;
    }

    public void revealExplorer() {
	fileChooser = new JFileChooser();
	if (read) {
	    int val = fileChooser.showOpenDialog(null);
	    if (val == JFileChooser.APPROVE_OPTION) {
		readFile();
	    }
	} else {
	    int val = fileChooser.showSaveDialog(null);
	    if (val == JFileChooser.APPROVE_OPTION) {
		writeFile();
	    }
	}
    }

    public void readFile(){
	file = fileChooser.getSelectedFile();
        readContents(file);
	fileName = file.getAbsolutePath();
    }

    public void writeFile(){
	if (autoOverwrite) {
	    File current = new File(fileName);
	    writeContents(current, textField.getText());
	    fileName = file.getAbsolutePath();
	} else {
	    file = fileChooser.getSelectedFile();
	    if (!file.exists()){
		writeContents(file, textField.getText());
		fileName = file.getAbsolutePath();
	    } else {
		int response = JOptionPane.showConfirmDialog(null, "Do you want to overwrite an existing file?",
							     "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
		    writeContents(file, textField.getText());
		}
	    }
	}
    }

    public void setRead(boolean b){
	read = b;
    }

    // True for Save, False for Save As
    public void setAutoOverwrite(boolean b){
	autoOverwrite = b;
    }

    public String getContents() {
	return contents;
    }

    public String getFileName() {
	return fileName;
    }
    
    public void readContents(File file) {
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

    public void writeContents(File newFile, String content) {
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
