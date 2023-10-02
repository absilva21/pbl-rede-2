package application;
import java.net.*;
import java.io.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//representa o receptor de mensagens
public class Receiver extends Thread {
	
	public Receiver() {
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public void run() {
		int porta = 7000;
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(porta);
			
			System.out.println("aguardando mensagens...");
			while(true) {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				
				serverSocket.receive(receivePacket);
				
				String payload = new String(receivePacket.getData());
				
				String[] payloadArray = payload.split("\n");
				String[] typeArray = payloadArray[0].split(" ");
				String[] bodyArray = payloadArray[1].split(" ");
				
				JSONParser parser = new JSONParser(); 
				
				try {
					JSONObject json = (JSONObject) parser.parse(bodyArray[1]);
					String mensagem = json.get("body").toString();
					String origem = new String(receivePacket.getSocketAddress().toString());;
					Application.novaMensagem(mensagem,0,origem);
				}catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
	}
	
}
