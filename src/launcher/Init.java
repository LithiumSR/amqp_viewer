package launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.CoreGui;

public class Init {

	public static void main(String[] args) {
		//Change look and feel 
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		
			e.printStackTrace();
		}
		
		//Load config files
		File config= new File("amqp_viewer_config.cfg");
		if (!config.exists()) try {
			config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				}
		Properties prop = new Properties();
		InputStream input= null;
		try {
			input = new FileInputStream("amqp_viewer_config.cfg");
			prop.load(input);
			input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					}
		
		CoreGui cg=new CoreGui(prop);

	}

}
