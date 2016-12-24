import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {
    private JMenu menu;
    private Editor textField;
    private Container pane;

    public Window(){
	this.setTitle("Text Editor");
	this.setSize(1200,800);
	this.setLocation(150,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	pane = this.getContentPane();

	GridLayout g = new GridLayout(2,2);
	// For sake of simplicity, can be changed later
        pane.setLayout(g);

	menu = new JMenu("Menubar");
	JMenuItem mi = new JMenuItem("File");
	menu.add(mi);
	
	textField = new Editor();

	pane.add(menu);
	pane.add(textField);
    }
}
