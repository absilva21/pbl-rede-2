package dados;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import application.Application;


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
			
			DatagramSocket serverSocket;

			serverSocket = new DatagramSocket(porta);
			
			while(destinos.hasNext()) {
				Cliente c = (Cliente) destinos.next();
				byte[] buffer = new byte[1024];
				InetAddress destiny = InetAddress.getByName(c.getAddr());
				String payload = "type: men\nbody: {\"grupo\":\""+grupo.getNome()+"\",\"origem\":\""+Application.localhost+"\",\"body\":\""+ mensagem+"\"}";
				buffer = payload.getBytes(StandardCharsets.UTF_8);;
				DatagramPacket sendPacket = new DatagramPacket(buffer,buffer.length,destiny,7000);
				serverSocket.send(sendPacket);
			}
			
			Grupo g = Application.grupos.get(Application.grupoView-1);
			g.addMessage(new Mensagem(mensagem,0,new Cliente(Application.localhost)));
			serverSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
