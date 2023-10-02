package dados;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;


public class Delivery extends Thread  {
	
	private Iterator<Cliente> destinos;
	private Grupo grupo; 
	private String mensagem;
	
	
	
	

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




	public String getMensagem() {
		return mensagem;
	}




	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}




	public Delivery(Iterator<Cliente> d, Grupo g,String m) {
		this.destinos = d;
		this.grupo = g;
		this.mensagem = m;
	}
	
	
	

	@Override
	public void run() {
		
		try {
			int porta = 7010;
			;
			DatagramSocket serverSocket;
			InetAddress localhost;
			serverSocket = new DatagramSocket(porta);
			localhost = InetAddress.getLocalHost();
			while(destinos.hasNext()) {
				Cliente c = (Cliente) destinos.next();
				byte[] buffer = new byte[1024];
				InetAddress destiny = InetAddress.getByName(c.getAddr());
				String payload = "type: men\nbody: {\"grupo\":\""+grupo.getNome()+"\",\"origem\":\""+localhost+"\",\"body\":\""+ mensagem+"\"}";
				buffer = payload.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(buffer,buffer.length,destiny,7000);
				serverSocket.send(sendPacket);
			}
			
			serverSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}