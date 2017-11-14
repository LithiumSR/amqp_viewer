package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import core.RabbitReceive;
import gui.CoreGui;

public class MyActionListener implements ActionListener {
	private CoreGui cg;
	
	public MyActionListener(CoreGui cg) {
		this.cg=cg;
	}

	private RabbitReceive rr;
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("start")) {
			saveConfig();
			cg.bt.setEnabled(false);
			cg.stop.setEnabled(true);
			rr=new RabbitReceive(cg.queue.getText(),cg.uri.getText(),cg);
			Thread t1=new Thread(rr);
			t1.start();


		}
		else if (e.getActionCommand().equals("stop")) {
			rr.interruptReceiver();
			cg.stop.setEnabled(false);
			cg.bt.setEnabled(true);

		}
	}
	
	public void saveConfig(){

        Properties prop = new Properties();
        OutputStream output= null;
        prop.put("uri", cg.uri.getText());
        prop.put("queue",cg.queue.getText());
        prop.put("durable", Boolean.toString(cg.durable.isSelected()));
        prop.put("exclusive", Boolean.toString(cg.exclusive.isSelected()));
        prop.put("autodelete", Boolean.toString(cg.autoDelete.isSelected()));
        
        try {

            output = new FileOutputStream("amqp_viewer_config.cfg");
            prop.store(output, "Config file for AMQP Basic Viewer");
            output.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
