package dados;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import application.Application;
import application.Main;


public class Delivery extends Thread  {
	
	private Iterator<Cliente> destinos;
	private Grupo grupo; 
	private Mensagem mensagem;
	
	
	
	

	public Iterator<Cliente> getDestinos() {
		return destinos;
	}




	public void setDestinos(Iterator<Cliente> destinos) {
		this.destinos = destinos;
	}




	public Grupo getGrupo() {
		return grupo;
	}




	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}




	public Mensagem getMensagem() {
		return mensagem;
	}




	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}




	public Delivery(Iterator<Cliente> d, Grupo g,Mensagem m) {
		this.destinos = d;
		this.grupo = g;
		this.mensagem = m;
	}
	
	
	

	@Override
	public void run() {
		
		try {
			
			
			while(destinos.hasNext()) {

				Cliente c = (Cliente) destinos.next();
				if(!c.getAddr().equals(Main.localhost)) {
					int porta = 7010;
					
					DatagramSocket serverSocket;
					serverSocket = new DatagramSocket(porta);
					byte[] buffer = new byte[1024];
					InetAddress destiny = InetAddress.getByName(c.getAddr());
					String payload = "type: men\nbody: {\"grupo\":\""+grupo.getNome()+"\",\"origem\":\""+Main.localhost+"\",\"body\":\""+ mensagem.getBody()+"\",\"tempo\":\""+mensagem.getTime()+"\"}";
					buffer = payload.getBytes(StandardCharsets.UTF_8);
					DatagramPacket sendPacket = new DatagramPacket(buffer,buffer.length,destiny,7000);
					serverSocket.send(sendPacket);
					serverSocket.close();
				}
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
