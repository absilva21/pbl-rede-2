package application;
import java.net.*;
import java.util.Iterator;
import java.io.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dados.Cliente;
import dados.Grupo;
import dados.Mensagem;


//representa o receptor de mensagens
public class Receiver extends Thread {
	
	public Receiver() {
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public  void run() {
		int porta = 7000;
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(porta);
			
			while(true) {
				byte[] receiveData = new byte[2048];
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				
				serverSocket.receive(receivePacket);
				
				String payload = new String(receivePacket.getData());
				
				String[] payloadArray = payload.split("\n");
				System.out.println(payloadArray[1]);
				int inicioJson = payloadArray[1].indexOf('{');
				int fimJson = payloadArray[1].indexOf('}')+1;
				String jsonBody = payloadArray[1].substring(inicioJson, fimJson);
				
			
				
				JSONParser parser = new JSONParser(); 
				
				
				try {
					JSONObject json = (JSONObject) parser.parse(jsonBody);
					String mensagem = json.get("body").toString();
					String origem = new String(receivePacket.getSocketAddress().toString());;
					Grupo viewGroup = null;
					viewGroup = Application.grupos.get(Application.grupoView-1);
					viewGroup.addMessage(new Mensagem(mensagem,0,new Cliente(origem)));
					Iterator<Mensagem> i = viewGroup.getMensagens().iterator();
					
					for(int j = 0; j<50;j++) {
						System.out.println("");
					}
					
					 System.out.println("     "+viewGroup.getNome());
					
					    while(i.hasNext()) {
					    	Mensagem m = i.next();
					    	
					    	if(m.getSource().getAddr().equals(Application.localhost)) {
					    		Mensagem out = (Mensagem) m;
					    		System.out.println(" \n                  você"+": \n                  		"+out.getBody()+" "+out.getTime()+"\n");
					    	}else {
					    		Mensagem IN = (Mensagem) m;
					    		System.out.println(" \n"+IN.getSource().getAddr()+": \n		"+IN.getBody()+" "+IN.getTime()+"\n");
					    		
					    	}
					    	
					    	
					    }
					    
					    System.out.println("\ndigite uma mensageem para o grupo ou ENTER para sair:");
					
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
