import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Toolkit;
import java.awt.datatransfer.*;

public class Window extends JFrame implements ActionListener {
    private String initTitle = "Text Editor";
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
	this.setTitle(initTitle + " | New");
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
	menu3 = new JMenu("Help");
	menu4 = new JMenu("Properties");

	menuBar.add(menu1);
	menuBar.add(menu2);
	menuBar.add(menu4);
	menuBar.add(menu3);

	// Items in submenus
	JMenuItem m1i1 = new JMenuItem("Open");
	JMenuItem m1i2 = new JMenuItem("Save");
	JMenuItem m1i3 = new JMenuItem("Save As");
	JMenuItem m1i4 = new JMenuItem("Exit");
	JMenuItem m1i5 = new JMenuItem("New");
	
	JMenuItem m2i1 = new JMenuItem("Copy All");
	JMenuItem m2i2 = new JMenuItem("Paste");
	
	JMenuItem m3i1 = new JMenuItem("About");
	
	submenu1 = new JMenu("Change Font");
	submenu2 = new JMenu("Change Font Size");
	
	// Font selections for submenu in submenu
	JMenuItem m4s1i1 = new JMenuItem("Monospaced");
	JMenuItem m4s1i2 = new JMenuItem("Consolas");
	JMenuItem m4s1i3 = new JMenuItem("Arial");

	// Font sizes for submenu
	JMenuItem m4s2i1 = new JMenuItem("12");
	JMenuItem m4s2i2 = new JMenuItem("14");
	JMenuItem m4s2i3 = new JMenuItem("16");
	JMenuItem m4s2i4 = new JMenuItem("18");
	JMenuItem m4s2i5 = new JMenuItem("20");
	JMenuItem m4s2i6 = new JMenuItem("22");

	menu1.add(m1i5);
	menu1.add(m1i1);
	menu1.add(m1i2);
	menu1.add(m1i3);
	menu1.add(m1i4);
	menu2.add(m2i1);
	menu2.add(m2i2);
	menu3.add(m3i1);
	menu4.add(submenu1);
	menu4.add(submenu2);
	submenu1.add(m4s1i1);
	submenu1.add(m4s1i2);
	submenu1.add(m4s1i3);
	submenu2.add(m4s2i1);
	submenu2.add(m4s2i2);
	submenu2.add(m4s2i3);
	submenu2.add(m4s2i4);
	submenu2.add(m4s2i5);
	submenu2.add(m4s2i6);

	m1i1.addActionListener(this);
	m1i2.addActionListener(this);
	m1i3.addActionListener(this);
	m1i4.addActionListener(this);
	m1i5.addActionListener(this);
	m2i1.addActionListener(this);
	m2i2.addActionListener(this);
	m3i1.addActionListener(this);
	m4s1i1.addActionListener(this);
	m4s1i2.addActionListener(this);
	m4s1i3.addActionListener(this);
	m4s2i1.addActionListener(this);
	m4s2i2.addActionListener(this);
        m4s2i3.addActionListener(this);
	m4s2i4.addActionListener(this);
        m4s2i5.addActionListener(this);
	m4s2i6.addActionListener(this);
	
	m1i1.setActionCommand("OPEN");
	m1i2.setActionCommand("SAVE");
	m1i3.setActionCommand("SAVEAS");
	m1i4.setActionCommand("QUIT");
	m1i5.setActionCommand("NEW");
	m2i1.setActionCommand("COPY");
	m2i2.setActionCommand("PASTE");
	m3i1.setActionCommand("ABOUT");
	m4s1i1.setActionCommand("FONT-MONO");
	m4s1i2.setActionCommand("FONT-CONSOLAS");
	m4s1i3.setActionCommand("FONT-ARIAL");
	m4s2i1.setActionCommand("12");
	m4s2i2.setActionCommand("14");
	m4s2i3.setActionCommand("16");
	m4s2i4.setActionCommand("18");
	m4s2i5.setActionCommand("20");
	m4s2i6.setActionCommand("22");
	
	editField = new EditorArea("Consolas", 18);
	label = new JLabel("Save as: ");
	label.setVisible(false);

	menuPanel.add(menuBar);
	menuPanel.add(label);

	scroll = new JScrollPane(editField);
	scroll.getVerticalScrollBar().setUnitIncrement(18);
	textPanel.add(scroll, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
	Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
	String event = e.getActionCommand();
	FileExplorer fe = new FileExplorer(editField);
	
	switch (event) {
	case "NEW":
	    fe.setRead(false);
	    fe.setAutoOverwrite(false);
	    fe.revealExplorer();
	    setTitle(initTitle + " | New");
	    editField.setText("");
	    break;
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
	case "FONT-MONO":
	    editField.changeFont("Monospaced", editField.getFontSize());
	    break;
	case "FONT-CONSOLAS":
	    editField.changeFont("Consolas", editField.getFontSize());
	    break;
	case "FONT-ARIAL":
	    editField.changeFont("Arial", editField.getFontSize());
	    break;
	case "12":
	    editField.changeFont(editField.getCurrentFont(), 12);
	    break;
	case "14":
	    editField.changeFont(editField.getCurrentFont(), 14);
	    break;
	case "16":
	    editField.changeFont(editField.getCurrentFont(), 16);
	    break;
	case "18":
	    editField.changeFont(editField.getCurrentFont(), 18);
	    break;
	case "20":
	    editField.changeFont(editField.getCurrentFont(), 20);
	    break;
	case "22":
	    editField.changeFont(editField.getCurrentFont(), 22);
	    break;
	}

	if(event.equals("SAVE") || event.equals("SAVEAS") || event.equals("OPEN")){
	    if (event.equals("OPEN")) {
		fe.setRead(true);
		fe.revealExplorer();
		editField.setText(fe.getContents());
		this.setTitle(initTitle + " | " + fe.getFileName());
	    } else if (event.equals("SAVEAS")){
		fe.setRead(false);
		fe.setAutoOverwrite(false);
		fe.revealExplorer();
		this.setTitle(initTitle + " | " + fe.getFileName());
	    } else {
	        fe.setRead(false);
		fe.setAutoOverwrite(true);
		fe.revealExplorer();
		this.setTitle(initTitle + " | " + fe.getFileName());
	    }
	}
    }
}

