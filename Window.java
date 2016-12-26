import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Window extends JFrame implements ActionListener {
    private JTextField textField;
    private JPanel menuPanel, textPanel, mainPanel;
    private JLabel label;
    private JMenuBar menuBar;
    private JMenu menu1, menu2, menu3;
    private JScrollPane scroll;
    private EditorArea editField;
    private Container pane;
    
    /*Sets up the main window with two panels. One panel contains
      the menu bar, the other the text editing region. Several components
      utilize an ActionListener
    */
    public Window(){
        this.setTitle("Text Editor");
        this.setSize(1200,800);
	this.setLocation(300,100);
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
	menu3 = new JMenu("Help");
	menuBar.add(menu1);
	menuBar.add(menu2);
	menuBar.add(menu3);

	/*
	ArrayList<JMenuItem> menu1list = new ArrayList<JMenuItem>();
	ArrayList<JMenuItem> menu2list = new ArrayList<JMenuItem>();
	ArrayList<JMenuItem> menu3list = new ArrayList<JMenuItem>();
	*/

	JMenuItem m1i1 = new JMenuItem("Open");
	JMenuItem m1i2 = new JMenuItem("Exit");
	JMenuItem m2i1 = new JMenuItem("Copy");
	JMenuItem m2i2 = new JMenuItem("Paste");
	JMenuItem m3i1 = new JMenuItem("About");
	menu1.add(m1i1);
	menu1.add(m1i2);
	menu2.add(m2i1);
	menu2.add(m2i2);
	menu3.add(m3i1);
	
	m1i1.addActionListener(this);
	m1i2.addActionListener(this);
	m2i1.addActionListener(this);
	m1i1.setActionCommand("OPEN");
	m1i2.setActionCommand("QUIT");
	m2i1.setActionCommand("READ");
		
	editField = new EditorArea();
	textField = new JTextField(20);
	textField.setVisible(false);
	label = new JLabel("Save as: ");
	label.setVisible(false);
     
	menuPanel.add(menuBar);
	menuPanel.add(label);
	menuPanel.add(textField);
	
	scroll = new JScrollPane(editField);
	scroll.getVerticalScrollBar().setUnitIncrement(18);
	textPanel.add(scroll, BorderLayout.CENTER);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void actionPerformed(ActionEvent e){
	String event = e.getActionCommand();
	switch(event){
	case "QUIT":
	    System.exit(0);
	    break;
        case "READ":
	    System.out.println(editField.getText());
	    break;
	case "OPEN":
	    revealFileExplorer(true);
	    break;
	}
    }

    /**
     *Pops up a file explorer dialog that the user can use
     *to find files to open.
     *@param read true if you want to read a file, false if you want to write to a file
     */
    public void revealFileExplorer(Boolean read){
	JFileChooser fChooser = new JFileChooser();
	int returnVal = fChooser.showOpenDialog(null);
	String contents = "";
	File chosenFile;
	
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    chosenFile = fChooser.getSelectedFile();
	    if(read == true){
		try{
		    BufferedReader reader = new BufferedReader(new FileReader(chosenFile));
		    String line = reader.readLine();
		    while(line != null){
			contents += line + '\n';
			line = reader.readLine();
		    }
		    editField.setText(contents);
		} catch(FileNotFoundException e){
		    System.out.println("File not found!");
		} catch(IOException e){
		    System.out.println("Error in parsing file!");
		}
	    } else {
		// Include code to write to a file
	    }
        }
    }
}
