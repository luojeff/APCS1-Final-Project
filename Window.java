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
    private JMenu submenu1, submenu2;
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

	// Submenus in menubar
	menu1 = new JMenu("File");
	menu2 = new JMenu("Edit");
	menu4 = new JMenu("Properties");
	menu3 = new JMenu("Help");
	submenu1 = new JMenu("Change Font");
	submenu2 = new JMenu("Change Font Size");
	menuBar.add(menu1);
	menuBar.add(menu2);
	menuBar.add(menu4);
	menuBar.add(menu3);
	menu4.add(submenu1);
	menu4.add(submenu2);
	
	/*
	 * ArrayList<JMenuItem> menu1list = new ArrayList<JMenuItem>();
	 * ArrayList<JMenuItem> menu2list = new ArrayList<JMenuItem>();
	 * ArrayList<JMenuItem> menu3list = new ArrayList<JMenuItem>();
	 */

	// Items in submenu
	JMenuItem m1i1 = new JMenuItem("Open");
	JMenuItem m1i2 = new JMenuItem("Save");
	JMenuItem m1i3 = new JMenuItem("Save As");
	JMenuItem m1i4 = new JMenuItem("Exit");
	JMenuItem m2i1 = new JMenuItem("Copy All");
	JMenuItem m2i2 = new JMenuItem("Paste");
	JMenuItem m3i1 = new JMenuItem("About");

	// Font selections for submenu
	JMenuItem m4s1i1 = new JMenuItem("Consolas");

	// Font sizes for submenu
	JMenuItem m4s2i1 = new JMenuItem("12");
	JMenuItem m4s2i2 = new JMenuItem("18");
	
	menu1.add(m1i1);
	menu1.add(m1i2);
	menu1.add(m1i3);
	menu1.add(m1i4);
	menu2.add(m2i1);
	menu2.add(m2i2);
	menu3.add(m3i1);
	submenu1.add(m4s1i1);
	submenu2.add(m4s2i1);
	submenu2.add(m4s2i2);

	m1i1.addActionListener(this);
	m1i2.addActionListener(this);
	m1i3.addActionListener(this);
	m1i4.addActionListener(this);
	m2i1.addActionListener(this);
	m2i2.addActionListener(this);
	m3i1.addActionListener(this);
	m4s1i1.addActionListener(this);
	m4s2i1.addActionListener(this);
	
	m1i1.setActionCommand("OPEN");
	m1i2.setActionCommand("SAVE");
	m1i3.setActionCommand("SAVEAS");
	m1i4.setActionCommand("QUIT");
	m2i1.setActionCommand("COPY");
	m2i2.setActionCommand("PASTE");
	m3i1.setActionCommand("ABOUT");
	m4s1i1.setActionCommand("FONT");
	m4s2i1.setActionCommand("OTHER");
	
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
	    FileExplorer fe1 = new FileExplorer(true, editField);
	    fe1.revealExplorer();
	    editField.setText(fe1.getContents());
	    break;
	case "SAVEAS":
	    FileExplorer fe2 = new FileExplorer(false, editField);
	    fe2.revealExplorer();
	    break;
	case "SAVE":
	    FileExplorer fe3 = new FileExplorer(false, editField);
	    fe3.revealExplorer();
	case "FONT":
	    editField.changeFont("Arial", 16);
	    editField.addFontStyle(true, true, "INSERT_STYLE");
	    editField.revalidate();
	    break;
	}
    }
}
