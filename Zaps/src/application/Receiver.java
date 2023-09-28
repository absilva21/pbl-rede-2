package application;
import java.net.*;
import java.io.*;
import dados.*;
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
				
				String mensagem = new String(receivePacket.getData());
				String origem = new String(receivePacket.getSocketAddress().toString());
				int port = receivePacket.getPort();
				Cliente c = new Cliente(origem,port);
				MensagemIn m = new MensagemIn(mensagem,0,c);
				Application.novaMensagem(m);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
	}
	
}
