import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Toolkit;
import java.awt.datatransfer.*;

public class Window extends JFrame implements ActionListener {
    private String initTitle = "Text Editor";
    private JPanel menuPanel, textPanel, visualizerPanel, mainPanel;
    private JSplitPane bottomPanel;
    private LinePanel linePanel;
    private JMenuBar menuBar;
    private JMenu menu1, menu2, menu3, menu4;
    private JMenu submenu1, submenu2;
    private JMenuItem m1i1, m1i2, m1i3, m1i4, m1i5;
    private JMenuItem m2i1, m2i2, m2i3, m2i4, m2i5;
    private JMenuItem m3i1, m3i2, m3i3, m3i4, m3i5;
    private JMenuItem m4i1, m4i2, m4i3, m4i4, m4i5;
    private JMenuItem m5i1, m5i2, m5i3, m5i4, m5i5;
    private JMenuItem m4s1i1, m4s1i2, m4s1i3;
    private JMenuItem m4s2i1, m4s2i2, m4s2i3, m4s2i4, m4s2i5, m4s2i6;
    private JScrollPane scrollPane;
    private EditorArea editField;
    private Container pane;
    private FileUpdateChecker checker;
    private JEditorPane visualizer;
    private Dimension ssDimension;

    /*
     * Sets up the main window with two panels. One panel contains the menu bar,
     * the other the text editing region. Several components utilize an
     * ActionListener
     */
    public Window() {
	setup();	
	setMenuBar();
	setActionCommands();
	if(checker != null){
	    setupVisualizer(checker.returnFileName());
	} else {
	    setupVisualizer("none");
	}
    }

    public void setup(){	
	this.setTitle(initTitle + " | New");
	this.setSize(1300, 800);
	this.setResizable(true); // CAN DISABLE
	this.setLocation(400, 100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	pane = this.getContentPane();

	menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	textPanel = new JPanel(new BorderLayout());
	visualizerPanel = new JPanel(new BorderLayout());
	bottomPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textPanel, visualizerPanel);
	
	Rectangle rec = this.getBounds();
	ssDimension = new Dimension(rec.width / 3, rec.height);
	textPanel.setMinimumSize(ssDimension);
	visualizerPanel.setMinimumSize(ssDimension);
     
	bottomPanel.setDividerLocation(rec.width / 2);
	bottomPanel.setOneTouchExpandable(true);
	mainPanel = new JPanel();
	mainPanel.setLayout(new BorderLayout());
	mainPanel.add(menuPanel, BorderLayout.NORTH);
	mainPanel.add(bottomPanel, BorderLayout.CENTER);
	pane.add(mainPanel);

	// Creates a menubar on the top which can contain the common
	// functions of most editors (File, Edit, Options, Help, etc...)	
	
	editField = new EditorArea("Monospaced", 18);
	linePanel = new LinePanel(editField);
	linePanel.changeFont(editField.getCurrentFont(), editField.getFontSize());
	editField.setLinePanel(linePanel);
	
	textPanel.add(editField, BorderLayout.CENTER);
	textPanel.add(linePanel, BorderLayout.WEST);	

	scrollPane = new JScrollPane(textPanel);
	scrollPane.getVerticalScrollBar().setUnitIncrement(18);
	bottomPanel.add(scrollPane);
    }

    public void setMenuBar(){
	menuBar = new JMenuBar();
	
	menuPanel.add(menuBar);

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
	m1i1 = new JMenuItem("Open");
	m1i2 = new JMenuItem("Save");
	m1i3 = new JMenuItem("Save As");
	m1i4 = new JMenuItem("Exit");
	m1i5 = new JMenuItem("New");
	m2i1 = new JMenuItem("Copy All");
	m2i2 = new JMenuItem("Paste");
	
	m3i1 = new JMenuItem("About");
	
	submenu1 = new JMenu("Change Font");
	submenu2 = new JMenu("Change Font Size");
	
	// Font selections for submenu in submenu
	m4s1i1 = new JMenuItem("Monospaced");
	m4s1i2 = new JMenuItem("Consolas");
	m4s1i3 = new JMenuItem("Arial");

	// Font sizes for submenu
	m4s2i1 = new JMenuItem("12");
	m4s2i2 = new JMenuItem("14");
	m4s2i3 = new JMenuItem("16");
	m4s2i4 = new JMenuItem("18");
	m4s2i5 = new JMenuItem("20");
	m4s2i6 = new JMenuItem("22");

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
    }

    public void setActionCommands(){
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
    }

    public void setupVisualizer(String fileName){
	visualizer = new JEditorPane();
	visualizer.setEditable(false);
	JScrollPane htmlScroller = new JScrollPane(visualizer);
        htmlScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	
        updateVisualizer(fileName);
	visualizerPanel.add(visualizer);
    }

    public void updateVisualizer(String fileName){	
	try{
	    visualizer.setPage((new File(fileName)).toURI().toURL());
	} catch (IOException e){
	    System.out.println("Can't retrieve HTML!");
	}
	System.out.println("Sucess to theere");
	visualizerPanel.revalidate();
    }
    
    public void actionPerformed(ActionEvent e) {
	Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
	String event = e.getActionCommand();
	FileExplorer fe = new FileExplorer(editField);	
	
	switch (event) {
	case "NEW":
	    if(checker != null && checker.isTextChanged(editField.getText())){
		fe.setRead(false);
		fe.revealExplorer();
		setTitle(initTitle + " | New");
		editField.setText("");
	    } else {
		setTitle(initTitle + " | New");
		editField.setText("");
	    }
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

		if(fe.getFileName() != null){
		    editField.setText(fe.getContents());
		    checker = new FileUpdateChecker(new File(fe.getFileName()), editField.getText());
		    this.setTitle(initTitle + " | " + fe.getFileName());
		    System.out.println("Current File: " + fe.getFileName());
		    updateVisualizer(fe.getFileName());
		}
	    } else if (event.equals("SAVEAS")){				
		fe.setRead(false);
		fe.revealExplorer();
		
		checker = new FileUpdateChecker(new File(fe.getFileName()), editField.getText());
		this.setTitle(initTitle + " | " + fe.getFileName());
		System.out.println("Current File: " + fe.getFileName());
		updateVisualizer(fe.getFileName());
	    } else {
	        fe.setRead(false);

		if(checker == null){
		    fe.revealExplorer();
		    checker = new FileUpdateChecker(new File(fe.getFileName()), editField.getText());
		    this.setTitle(initTitle + " | " + fe.getFileName());
		    System.out.println("Current File: " + fe.getFileName());

		    updateVisualizer(fe.getFileName());
		} else if(checker.isTextChanged(editField.getText())) {
		    fe.setAutoOverwrite(true, checker.returnFile());
		    fe.revealExplorer();   
		    this.setTitle(initTitle + " | " + checker.returnFileName());
		    System.out.println("Current File: " + checker.returnFileName());
		    updateVisualizer(checker.returnFileName());
		}	
	    }
	}
    }
}
