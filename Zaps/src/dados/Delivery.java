package dados;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.json.simple.JSONArray;

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
				if(!c.getAddr().equals(Application.main.localhost)) {
					int porta = 7010;
					int[] relogio = mensagem.getTime();
					JSONArray relogioJson = new JSONArray();
					for(int i = 0; i<relogio.length;i++) {
						String valor = Integer.toString(relogio[i]);
						relogioJson.add(valor);
					}
					DatagramSocket serverSocket;
					serverSocket = new DatagramSocket(porta);
					byte[] buffer = new byte[1024];
					InetAddress destiny = InetAddress.getByName(c.getAddr());
					String payload = "type: men\nbody: {\"grupo\":\""+grupo.getNome()+"\",\"origem\":\""+Application.main.localhost+"\",\"body\":\""+ mensagem.getBody()+"\",\"tempo\":"+relogioJson.toJSONString()+",\"id\":\""+mensagem.getSource().getId()+"\",\"idm\""+mensagem.getIdLocal()+"\"}";
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
