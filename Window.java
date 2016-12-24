import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {
    private JMenuBar menuBar;
    private JMenu menu;
    private Editor textField;
    private Container pane;

    public Window(){
        this.setTitle("Text Editor");
        this.setSize(1200,800);
	this.setLocation(150,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	pane = this.getContentPane();

	JPanel top = new JPanel(new BorderLayout());
	JPanel bottom = new JPanel(new BorderLayout());
	JPanel both = new JPanel();
	both.setLayout(new BoxLayout(both, BoxLayout.PAGE_AXIS));
	both.add(top);
	both.add(bottom);
	pane.add(both);

	// Creates a menubar on the top which can contain the common
	// functions of most editors (File, Edit, Options, Help, etc...)
	menuBar = new JMenuBar();
	menu = new JMenu("Edit");
	menuBar.add(menu);
	
	JMenuItem menui1 = new JMenuItem("Copy");
	JMenuItem menui2 = new JMenuItem("Paste");
	menu.add(menui1);
	menu.add(menui2);
	
	textField = new Editor();

	top.add(menuBar);
	bottom.add(textField);
    }
}
