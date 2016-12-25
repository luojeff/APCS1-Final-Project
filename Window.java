import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {
    private JMenuBar menuBar;
    private JMenu menu1, menu2, menu3;
    private Editor editField;
    private Container pane;

    public Window(){
        this.setTitle("Text Editor");
        this.setSize(1200,800);
	this.setLocation(300,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	pane = this.getContentPane();

	JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JPanel bottom = new JPanel(new BorderLayout());
	JPanel both = new JPanel();
	both.setLayout(new BoxLayout(both, BoxLayout.PAGE_AXIS));
	both.add(top);
	both.add(bottom);
	pane.add(both);

	// Creates a menubar on the top which can contain the common
	// functions of most editors (File, Edit, Options, Help, etc...)
	menuBar = new JMenuBar();
	menu1 = new JMenu("File");
	menu2 = new JMenu("Edit");
	menu3 = new JMenu("Help");
	menuBar.add(menu1);
	menuBar.add(menu2);
	menuBar.add(menu3);

	JMenuItem m1i1 = new JMenuItem("Save");
	JMenuItem m1i2 = new JMenuItem("Exit");
	JMenuItem m2i1 = new JMenuItem("Copy");
	JMenuItem m2i2 = new JMenuItem("Paste");
	JMenuItem m3i1 = new JMenuItem("About");
	
	menu1.add(m1i1);
	menu1.add(m1i2);
	menu2.add(m2i1);
	menu2.add(m2i2);
	menu3.add(m3i1);
	
	editField = new Editor();

	top.add(menuBar);
	bottom.add(editField);
    }
}
