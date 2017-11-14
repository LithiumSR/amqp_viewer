package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import listeners.MyActionListener;

public class CoreGui extends JFrame {
	public JTextArea area;
	public JButton bt;
	public JTextField uri;
	public JTextField queue;
	private JLabel label;
	private JLabel label2;
	public JButton stop;
	public JCheckBox durable;
	public JCheckBox exclusive;
	public JCheckBox autoDelete;
	
	public CoreGui(Properties prop) {
		super("AMQP Basic Viewer");
		//Create UI elements
		area=new JTextArea(300,400);
		bt=new JButton("Start receiver");
		uri=new JTextField();
		uri.setColumns(30);
		queue=new JTextField();
		label2=new JLabel("Queue name:");
		stop=new JButton("Interrupt");
		durable=new JCheckBox("Durable");
		exclusive=new JCheckBox("Exclusive");
		autoDelete=new JCheckBox("Auto-Delete");
		stop.setEnabled(false);
		label=new JLabel("AMQP URI: ");
		JScrollPane scroll = new JScrollPane ();
		
		//Change text and load settings
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setViewportView(area);
		area.setEditable(false);
		queue.setColumns(26);
		uri.setText(prop.getProperty("uri"));
		queue.setText(prop.getProperty("queue"));
		if (prop.containsKey("durable") && prop.getProperty("durable").equals("true")) durable.setSelected(true);
		if (prop.containsKey("exclusive") && prop.getProperty("exclusive").equals("true")) exclusive.setSelected(true);
		if (prop.containsKey("autodelete") && prop.getProperty("autodelete").equals("true")) autoDelete.setSelected(true);

		//Add ActionListener and ActionCommand to buttons
		MyActionListener mal=new MyActionListener(this);
		bt.setActionCommand("start");
		stop.setActionCommand("stop");
		bt.addActionListener(mal);
		stop.addActionListener(mal);

		
		//Populate panels
		JPanel jpl = new JPanel(new BorderLayout());
		JPanel topPnl = new JPanel(new GridLayout(3,1));
		JPanel firstRow = new JPanel(new FlowLayout());
		JPanel secondRow = new JPanel(new FlowLayout());
		JPanel thirdRow = new JPanel(new FlowLayout());
		JPanel botPnl = new JPanel(new FlowLayout());
		firstRow.add(label);
		firstRow.add(uri);
		secondRow.add(label2);
		secondRow.add(queue);
		thirdRow.add(durable);
		thirdRow.add(exclusive);
		thirdRow.add(autoDelete);
		topPnl.setBorder(BorderFactory.createTitledBorder("Config"));
		topPnl.add(firstRow);
		topPnl.add(secondRow);
		topPnl.add(thirdRow);
		botPnl.add(bt);
		botPnl.add(stop);
		jpl.add(topPnl,BorderLayout.NORTH);
		jpl.add(scroll,BorderLayout.CENTER);
		jpl.add(botPnl,BorderLayout.SOUTH);
		
		//Change JFrame settings
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(jpl);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setSize(600, 600);
		this.setResizable(false);
	}
}
