package application;
import java.net.*;
import java.io.*;

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
			byte[] receiveData = new byte[1024];
			System.out.println("aguardando mensagens...");
			while(true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				serverSocket.receive(receivePacket);
				
				String mensagem = new String(receivePacket.getData());
				System.out.println("\nchegou");
				Application.menssagens.add(mensagem);
				Application.novaMensagem();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
	}
	
}
