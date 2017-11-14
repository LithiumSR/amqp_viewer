package core;

import com.rabbitmq.client.*;

import gui.CoreGui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

public class RabbitReceive implements Runnable{
    private String QUEUE_NAME = "MSI_Info";
    private Channel channel;
    private CoreGui cg;
    private String uri;
    private String consumerTag;
    private Connection connection;

    public RabbitReceive(String queue,String uri, CoreGui cg){
        System.out.println("[RabbitMQ] Setup flow has started...");
        QUEUE_NAME=queue;
        this.cg=cg;
        this.uri=uri;
        ConnectionFactory factory = new ConnectionFactory();
        try {
			factory.setUri(uri);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, cg.durable.isSelected(), cg.exclusive.isSelected(), cg.autoDelete.isSelected(), null);
            System.out.println("[RabbitMQ] Waiting for messages...");
            
		} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException | IOException | TimeoutException e1) {
			JOptionPane.showMessageDialog(null,"Something went wrong.\n Check your connection and make sure the settings are valid for you AMQP server :(","Error",JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			cg.bt.setEnabled(true);
			cg.stop.setEnabled(false);
		}


    }

    @Override
    public void run() {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [RabbitMQ] Received '" + message + "'");
                cg.area.append(message+"\n");
                fileOP(message);
            }
        };
        try {
            consumerTag=channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
			cg.bt.setEnabled(true);
			cg.stop.setEnabled(false);
        }
        System.out.println("ciao");
    }
    
    public void interruptReceiver() {
    	try {
			channel.basicCancel(consumerTag);
			channel.close();
	    	connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    	
    }


    //Write messages on file
    public synchronized void fileOP(String text){
        File fl=new File(cg.queue.getText()+".txt");
        if(!fl.exists()) try {
            fl.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long sizeInBytes=fl.length();
        long sizeInMb = sizeInBytes/ (1024 * 1024);

        if (sizeInMb> 20){
            fl.delete();
            try {
                fl.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter fw=new FileWriter(fl,true);
            fw.append(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
