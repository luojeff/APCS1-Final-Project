import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.GraphicsEnvironment;
import java.text.DecimalFormat;

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
	private FileExplorer fe;
	private HTMLVisualizer visuals;
	private Rectangle dimensions;
	private double splitPaneRatio;
	private ArrayList<JMenu> menuArrayList;
	private FileUpdateChecker checker;

	/*
	 * Sets up the main window with two panels. One panel contains the menu bar,
	 * the other the text editing region. Several components utilize an
	 * ActionListener
	 */
	public Window(FileUpdateChecker checker) {
		this.checker = checker;
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
		this.addComponentListener(new WindowListener());

		pane = this.getContentPane();

		menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		textPanel = new JPanel(new BorderLayout());
		visualizerPanel = new JPanel(new BorderLayout());
		bottomPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textPanel, visualizerPanel);
		dimensions = this.getBounds();

		textPanel.setMinimumSize(new Dimension(dimensions.width / 3, dimensions.height));
		visualizerPanel.setMinimumSize(new Dimension(dimensions.width / 3, dimensions.height));

		bottomPanel.setDividerLocation(dimensions.width / 2);
		splitPaneRatio = 1 / 2.0;
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

		fe = new FileExplorer(editField);
		checker.init(null, "");
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

		AbstractButton[] menu1Options = { new JMenuItem("New"), new JMenuItem("Open"), new JMenuItem("Save"),
				new JMenuItem("Save As"), new JMenuItem("Exit"), };

		AbstractButton[] menu2Options = { new JMenuItem("Cut"), new JMenuItem("Copy"), new JMenuItem("Paste"),
				new JMenuItem("Clear"), };

		AbstractButton[] menu3Options = { new JCheckBoxMenuItem("Show HTML Visualizer", true),
				new JCheckBoxMenuItem("Enable Auto-save", true), new JMenu("Change Font"),
				new JMenu("Change Font Size") };

		// Items in submenus
		JMenuItem[] fonts = { new JMenuItem("Arial"), new JMenuItem("Calibri"), new JMenuItem("Cambria"),
				new JMenuItem("Consolas"), new JMenuItem("Courier New"), new JMenuItem("Lucida Sans"),
				new JMenuItem("Monospaced"), new JMenuItem("Serif"), new JMenuItem("Times New Roman"),
				new JMenuItem("Trebuchet MS"), new JMenuItem("Verdana") };

		JMenuItem[] fontSizes = { new JMenuItem("12"), new JMenuItem("14"), new JMenuItem("16"), new JMenuItem("18"),
				new JMenuItem("20"), new JMenuItem("22"), new JMenuItem("24"), new JMenuItem("26"), new JMenuItem("28"),
				new JMenuItem("36"), new JMenuItem("48"), new JMenuItem("72") };

		for (JMenuItem font : fonts) {
			menu3Options[2].add(font);
			font.addActionListener(this);
		}

		for (JMenuItem fontSize : fontSizes) {
			menu3Options[3].add(fontSize);
			fontSize.addActionListener(this);
		}

		JMenuItem[] menu4Options = { new JMenuItem("About") };

		AbstractButton[][] menus = { menu1Options, menu2Options, menu3Options, menu4Options };

		int count = 0;
		for (AbstractButton[] menuOps : menus) {
			for (AbstractButton ab : menuOps) {
				if (ab instanceof JMenuItem) {
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
		if (checker.isFileSet()) {
			visuals.setupVisualizer(checker.returnFileName());
		} else {
			visuals.setupVisualizer("none");
		}
		editField.setVisuals(visuals);
		visuals.displayNone();
		editField.setFileExplorer(fe);
		editField.setFileUpdateChecker(checker);
	}

	public void actionPerformed(ActionEvent e) {
		Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		String event = e.getActionCommand();

		switch (event) {
		case "New":
			if (checker.isFileSet()) {
				fe.setRead(false);
				if (checker.isTextChanged(editField.getText())) {
					if (fe.revealExplorer()) {
						// When a file is set and HAS changed from original save

						setTitle(initTitle + " | New");
						editField.setText("");
						checker.init(null, "");
						fe.setFile(null);
						visuals.displayNone();
					}
				} else {
					// When a file is set, though hasn't changed from original
					// save

					setTitle(initTitle + " | New");
					editField.setText("");
					checker.init(null, "");
					fe.setFile(null);
					visuals.displayNone();
				}
			} else {
				// When no file is set; purely from scratch

				setTitle(initTitle + " | New");
				editField.setText("");
				checker.init(null, "");
				fe.setFile(null);
				visuals.displayNone();
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
			if (event.equals("Open")) {
				if (checker.isFileSet() && checker.isTextChanged(editField.getText())) {
					// If currently set file has changed from original state.
					// It will ask you to save your file before choosing a new
					// one.

					fe.setRead(false);
					if (fe.revealExplorer()) {	
						fe.disableAutoOverwrite();
						checker.init(new File(fe.getFileName()), editField.getText());
						System.out.println("File saved to: " + fe.getFileName());
					}
				}
				fe.setRead(true);
				if (fe.revealExplorer()) {
					checker.init(new File(fe.getFileName()), fe.getContents());
					editField.setText(fe.getContents());
					this.setTitle(initTitle + " | " + fe.getFileName());
					System.out.println("Current File: " + fe.getFileName());
					visuals.updateVisualizer(fe.getFileName());
				}
			} else if (event.equals("Save As")) {
				fe.disableAutoOverwrite();
				fe.setRead(false);
				if (fe.revealExplorer()) {
					checker.init(new File(fe.getFileName()), editField.getText());
					this.setTitle(initTitle + " | " + fe.getFileName());
					System.out.println("File saved to: " + fe.getFileName());
					visuals.updateVisualizer(fe.getFileName());
				}
			} else if (event.equals("Save")) {
				fe.setRead(false);
				if (checker.isFileSet()) {
					if (checker.isTextChanged(editField.getText())) {
						fe.setAutoOverwrite(true, checker.returnFile());
						if (fe.revealExplorer()) {
							checker.init(new File(fe.getFileName()), editField.getText());
							this.setTitle(initTitle + " | " + fe.getFileName());
							visuals.updateVisualizer(fe.getFileName());
							fe.disableAutoOverwrite();

							System.out.println("File saved to: " + fe.getFileName());
						}
					} else {
						System.out.println("File has not changed. No save");
					}
				} else {
					System.out.println("FileExplorer is null");
					if (fe.revealExplorer()) {
						checker.init(new File(fe.getFileName()), editField.getText());
						this.setTitle(initTitle + " | " + fe.getFileName());
						visuals.updateVisualizer(fe.getFileName());
					}
				}
			} else if (event.equals("Show HTML Visualizer")) {
				if (!menuArrayList.get(2).getItem(0).isSelected()) {
					bottomPanel.getRightComponent().setVisible(false);
					bottomPanel.setDividerLocation(1.0d);
				} else {
					bottomPanel.getRightComponent().setVisible(true);
					bottomPanel.setDividerLocation(splitPaneRatio);
				}
			} else if (event.equals("Enable Auto-save")) {
				if (!menuArrayList.get(2).getItem(1).isSelected()) {
					editField.setAutoSaveFeature(false);
				} else {
					editField.setAutoSaveFeature(true);
				}
			} else if (event.matches("\\d+")) {
				System.out.println(event);
				editField.changeFont(editField.getCurrentFont(), Integer.parseInt(event));
			} else if (Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
					.contains(event)) {
				editField.changeFont(event, editField.getFontSize());
			} else {
				System.out.println("No defined action for event: " + event);
			}
			break;
		}
	}

	public class WindowListener implements ComponentListener {
		public void componentHidden(ComponentEvent arg0) {
		}

		public void componentMoved(ComponentEvent arg0) {
		}

		public void componentShown(ComponentEvent arg0) {
		}

		public void componentResized(ComponentEvent arg0) {
			if (!menuArrayList.get(2).getItem(0).isSelected()) {
				bottomPanel.getRightComponent().setVisible(false);
				bottomPanel.setDividerLocation(1.0d);
			} else {
				bottomPanel.getRightComponent().setVisible(true);
				bottomPanel.setDividerLocation(splitPaneRatio);
			}
		}

	}
}