import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.GraphicsEnvironment;

public class Window extends JFrame implements ActionListener {
    private String initTitle = "Text Editor";
    private JPanel menuPanel, textPanel, visualizerPanel, mainPanel;
    private JSplitPane bottomPanel;
    private LinePanel linePanel;
    private JMenuBar menuBar;
    private JMenu menu1, menu2, menu3, menu4;
    private JScrollPane scrollPane;
    private EditorArea editField;
    private Container pane;
    private FileUpdateChecker checker;
    private FileExplorer fe;
    private HTMLVisualizer visuals;
    private Dimension ssDimension;
    private ArrayList<JMenu> menuArrayList;

    /*
     * Sets up the main window with two panels. One panel contains the menu bar,
     * the other the text editing region. Several components utilize an
     * ActionListener
     */
    public Window() {
	setup();
	setMenuBar();
	setupVisuals();
    }

    private void setup() {
	this.setTitle(initTitle + " | New");
	this.setSize(1300, 800);
	this.setResizable(true);
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

	fe = new FileExplorer(editField);
	scrollPane = new JScrollPane(textPanel);
	scrollPane.getVerticalScrollBar().setUnitIncrement(18);
	bottomPanel.add(scrollPane);
    }

    private void setMenuBar() {
	menuBar = new JMenuBar();
	menuPanel.add(menuBar);

	// Submenus in menubar
	menu1 = new JMenu("File");
	menu2 = new JMenu("Edit");
	menu3 = new JMenu("Properties");
	menu4 = new JMenu("Help");

	menuArrayList = new ArrayList<JMenu>();
	menuArrayList.add(menu1);
	menuArrayList.add(menu2);
	menuArrayList.add(menu3);
	menuArrayList.add(menu4);

	for (JMenu menu : menuArrayList) {
	    menuBar.add(menu);
	}

	AbstractButton[] menu1Options = { 
	    new JMenuItem("New") ,
	    new JMenuItem("Open"), 
	    new JMenuItem("Save"), 
	    new JMenuItem("Save As"),
	    new JMenuItem("Exit"),
	};
		
	AbstractButton[] menu2Options = {
	    new JMenuItem("Cut"),
	    new JMenuItem("Copy"),
	    new JMenuItem("Paste"),
	    new JMenuItem("Clear"),
	};
		
	AbstractButton[] menu3Options = { 
	    new JCheckBoxMenuItem("Show HTML Visualizer"),
	    new JMenu("Change Font"),
	    new JMenu("Change Font Size") 
	};
		
	// Items in submenus
	JMenuItem[] fonts = {
	    new JMenuItem("Arial"),
	    new JMenuItem("Calibri"),
	    new JMenuItem("Cambria"),
	    new JMenuItem("Consolas"),
	    new JMenuItem("Courier New"),
	    new JMenuItem("Lucida Sans"),
	    new JMenuItem("Monospaced"),
	    new JMenuItem("Serif"),
	    new JMenuItem("Times New Roman"),
	    new JMenuItem("Trebuchet MS"),
	    new JMenuItem("Verdana")
	};
		
	JMenuItem[] fontSizes = { 
	    new JMenuItem("12"), 
	    new JMenuItem("14"), 
	    new JMenuItem("16"), 
	    new JMenuItem("18"), 
	    new JMenuItem("20"), 
	    new JMenuItem("22"),
	    new JMenuItem("24"), 
	    new JMenuItem("26"), 
	    new JMenuItem("28"), 
	    new JMenuItem("36"), 
	    new JMenuItem("48"), 
	    new JMenuItem("72")
	};
		
	for(JMenuItem font : fonts){
	    menu3Options[1].add(font);
	    font.addActionListener(this);
	} 
		
	for(JMenuItem fontSize : fontSizes){
	    menu3Options[2].add(fontSize);
	    fontSize.addActionListener(this);
	}

	JMenuItem[] menu4Options = { 
	    new JMenuItem("About")
	};

	AbstractButton[][] menus = { 
	    menu1Options, 
	    menu2Options, 
	    menu3Options, 
	    menu4Options
	};

	int count = 0;
	for (AbstractButton[] menuOps : menus) {
	    for(AbstractButton ab : menuOps){
		if(ab instanceof JMenuItem){
		    menuArrayList.get(count).add(ab);
		    ab.addActionListener(this);
		    ab.setActionCommand(ab.getText());
		}
	    }
	    count++;
	}
    }

    private void setupVisuals() {
	visuals = new HTMLVisualizer(visualizerPanel);
	if (checker != null) {
	    visuals.setupVisualizer(checker.returnFileName());
	} else {
	    visuals.setupVisualizer("none");
	}
	editField.setVisuals(visuals);
	editField.setFileExplorer(fe);
    }

    public void actionPerformed(ActionEvent e) {
	Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
	String event = e.getActionCommand();

	switch (event) {
	case "New":
	    if (checker != null && checker.isTextChanged(editField.getText())) {
		fe.setRead(false);
		fe.revealExplorer();
		setTitle(initTitle + " | New");
		editField.setText("");
		checker = new FileUpdateChecker(new File("none"), "");
	    } else {
		setTitle(initTitle + " | New");
		editField.setText("");
	    }
	    break;
	case "Exit":
	    System.exit(0);
	    break;
	case "Copy":
	    StringSelection str = new StringSelection(editField.getText());
	    clipBoard.setContents(str, null);
	    break;
	case "Paste":
	    Transferable t = clipBoard.getContents(this);
	    try {
		editField.appendText((String) t.getTransferData(DataFlavor.stringFlavor));
	    } catch (Exception ex) {
		System.out.println("No flavor!");
	    }
	    break;
	default:
	    if (Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()).contains(event)){
		editField.changeFont(event, editField.getFontSize());
	    }
	    if (event.matches("\\d+")){
		System.out.println(event);
		editField.changeFont(editField.getCurrentFont(), Integer.parseInt(event));
	    }
	    if (event.equals("Open") || event.equals("Save") || event.equals("Save As")){
		if (event.equals("Open")) {
		    fe.setRead(true);
		    fe.revealExplorer();

		    if (fe.getFileName() != null) {
			System.out.println(fe.getContents());
			editField.setText(fe.getContents());
			checker = new FileUpdateChecker(new File(fe.getFileName()), editField.getText());
			this.setTitle(initTitle + " | " + fe.getFileName());
			System.out.println("Current File: " + fe.getFileName());
			visuals.updateVisualizer(fe.getFileName());
		    }
		} else if (event.equals("Save As")) {
		    fe.setRead(false);
		    fe.revealExplorer();

		    checker = new FileUpdateChecker(new File(fe.getFileName()), editField.getText());
		    this.setTitle(initTitle + " | " + fe.getFileName());
		    System.out.println("Current File: " + fe.getFileName());
		    visuals.updateVisualizer(fe.getFileName());
		} else {
		    fe.setRead(false);

		    if (checker == null) {
			fe.revealExplorer();
			checker = new FileUpdateChecker(new File(fe.getFileName()), editField.getText());
			this.setTitle(initTitle + " | " + fe.getFileName());
			System.out.println("Current File: " + fe.getFileName());
			visuals.updateVisualizer(fe.getFileName());
		    } else if (checker.isTextChanged(editField.getText())) {
			fe.setAutoOverwrite(true, checker.returnFile());
			fe.revealExplorer();
			this.setTitle(initTitle + " | " + fe.getFileName());
			System.out.println("Current File: " + checker.returnFileName());
			visuals.updateVisualizer(fe.getFileName());
		    }
		}
	    }
	    break;
	}
    }
}
