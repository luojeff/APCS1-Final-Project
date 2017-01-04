import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Toolkit;
import java.awt.datatransfer.*;

public class Window extends JFrame implements ActionListener {
    private JPanel menuPanel, textPanel, mainPanel;
    private JLabel label;
    private JMenuBar menuBar;
    private JMenu menu1, menu2, menu3, menu4;
    private JScrollPane scroll;
    private EditorArea editField;
    private Container pane;

    /*
     * Sets up the main window with two panels. One panel contains the menu bar,
     * the other the text editing region. Several components utilize an
     * ActionListener
     */
    public Window() {
	this.setTitle("Text Editor");
	this.setSize(1200, 800);
	this.setLocation(300, 100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	pane = this.getContentPane();

	menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	textPanel = new JPanel(new BorderLayout());
	mainPanel = new JPanel();

	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
	mainPanel.add(menuPanel);
	mainPanel.add(textPanel);
	pane.add(mainPanel);

	// Creates a menubar on the top which can contain the common
	// functions of most editors (File, Edit, Options, Help, etc...)
	menuBar = new JMenuBar();
	menu1 = new JMenu("File");
	menu2 = new JMenu("Edit");
	menu4 = new JMenu("Properties");
	menu3 = new JMenu("Help");
	menuBar.add(menu1);
	menuBar.add(menu2);
	menuBar.add(menu4);
	menuBar.add(menu3);
	
	/*
	 * ArrayList<JMenuItem> menu1list = new ArrayList<JMenuItem>();
	 * ArrayList<JMenuItem> menu2list = new ArrayList<JMenuItem>();
	 * ArrayList<JMenuItem> menu3list = new ArrayList<JMenuItem>();
	 */

	JMenuItem m1i1 = new JMenuItem("Open");
	JMenuItem m1i2 = new JMenuItem("Save");
	JMenuItem m1i3 = new JMenuItem("Save As");
	JMenuItem m1i4 = new JMenuItem("Exit");
	JMenuItem m2i1 = new JMenuItem("Copy All");
	JMenuItem m2i2 = new JMenuItem("Paste");
	JMenuItem m3i1 = new JMenuItem("About");
	JMenuItem m4i1 = new JMenuItem("Appearance");
	
	menu1.add(m1i1);
	menu1.add(m1i2);
	menu1.add(m1i3);
	menu1.add(m1i4);
	menu2.add(m2i1);
	menu2.add(m2i2);
	menu3.add(m3i1);
	menu4.add(m4i1);

	m1i1.addActionListener(this);
	m1i2.addActionListener(this);
	m1i3.addActionListener(this);
	m1i4.addActionListener(this);
	m2i1.addActionListener(this);
	m2i2.addActionListener(this);
	m3i1.addActionListener(this);
	m4i1.addActionListener(this);

	m1i1.setActionCommand("OPEN");
	m1i2.setActionCommand("SAVE");
	m1i3.setActionCommand("SAVEAS");
	m1i4.setActionCommand("QUIT");
	m2i1.setActionCommand("COPY");
	m2i2.setActionCommand("PASTE");
	m3i1.setActionCommand("ABOUT");
	m4i1.setActionCommand("FONT");

	editField = new EditorArea();
	label = new JLabel("Save as: ");
	label.setVisible(false);

	menuPanel.add(menuBar);
	menuPanel.add(label);

	scroll = new JScrollPane(editField);
	scroll.getVerticalScrollBar().setUnitIncrement(18);
	textPanel.add(scroll, BorderLayout.CENTER);
	scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void actionPerformed(ActionEvent e) {
	Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
	String event = e.getActionCommand();
	
	switch (event) {
	case "QUIT":
	    System.exit(0);
	    break;
	case "COPY":
	    StringSelection str = new StringSelection(editField.getText());
	    clipBoard.setContents(str, null);
	    break;
	case "PASTE":
	    Transferable t = clipBoard.getContents(this);
	    try {
		editField.appendText((String) t.getTransferData(DataFlavor.stringFlavor));
	    } catch (Exception ex){
		System.out.println("No flavor!");
	    }
	    break;
	case "OPEN":
	    FileExplorer fe1 = new FileExplorer(true);
	    fe1.revealExplorer();
	    editField.setText(fe1.getContents());
	    break;
	case "SAVEAS":
	    FileExplorer fe2 = new FileExplorer(false);
	    fe2.revealExplorer();
	    break;
	case "FONT":
	    editField.changeFont("Arial", 16);
	    editField.addFontStyle(true, true, "INSERT_STYLE");
	    editField.insertAS();
	    textPanel.revalidate();
	    break;
	}
    }

    /**
     * Opens a file explorer which allows for the user to either choose a file
     * and copy the contents into the text component or choose a directory to
     * save a file into
     */
    public class FileExplorer {

	private File file;
	private String contents = "";
	private Boolean read;

	public FileExplorer(Boolean read) {
	    this.read = read;
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
			writeFile(file, editField.getText());
		    } else {
			int response = JOptionPane.showConfirmDialog(null, "Do you want to overwrite an existing file?",
								     "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.YES_OPTION) {
			    writeFile(file, editField.getText());
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
}
