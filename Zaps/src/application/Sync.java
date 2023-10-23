package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dados.Cliente;
import dados.Grupo;
import dados.Mensagem;


//uma thread que envia dados ou comandos para que os nó sincronizem
/*
 add - avisa um nó que ele foi adicionado a um grupo
 up - pede a um nó os dados da mensagens contidas em um grupo
 */

public class Sync extends Thread {
	
	private String destino;
	private String tipo;
	private Object mensagem;
	
	
	

	public String getDestino() {
		return destino;
	}



	public void setDestino(String destino) {
		this.destino = destino;
	}



	public String getTipo() {
		return tipo;
	}



	public void setTipo(String tipo) {
		this.tipo = tipo;
	}



	public Object getMensagem() {
		return mensagem;
	}



	public void setMensagem(Object mensagem) {
		this.mensagem = mensagem;
	}

	public Sync(String d, String t, Object m) {
		this.destino = d;
		this.tipo = t;
		this.mensagem = m;
	}
	
	@Override
	public void run() {
		try {
			int porta = 7010;
			
			DatagramSocket serverSocket;

			serverSocket = new DatagramSocket(porta);
			
			JSONObject body = new JSONObject();
			
			
			//mapear as informações em json
			if(this.mensagem instanceof Grupo) {
				
				Grupo g = (Grupo) this.mensagem;
				JSONArray mensagens = new JSONArray();
				JSONArray clientes = new JSONArray();
				Iterator<Mensagem> i = g.getMensagens().iterator();
				Iterator<Cliente> j = g.getClientes().iterator(); 
				
				body.put("adm",g.getAdm());
				
				body.put("nome", g.getNome());
				
				while(i.hasNext()) {
					Mensagem m = (Mensagem) i.next();
					JSONObject jsonMensagem = new JSONObject();
					jsonMensagem.put("origem", m.getSource().getAddr());
					jsonMensagem.put("nomeOrigem", m.getSource().getNome());
					jsonMensagem.put("body", m.getBody());
					mensagens.add(jsonMensagem);
				}
				
				while(j.hasNext()) {
					Cliente c = (Cliente) j.next();
					JSONObject jsonCliente = new JSONObject();
					jsonCliente.put("addr", c.getAddr());
					jsonCliente.put("nome", c.getNome());
					clientes.add(jsonCliente);
				}
				
				body.put("mensagens", mensagens);
				body.put("clientes", clientes);
			}
			
			JSONObject json = new JSONObject();
			json.put("com", this.tipo);
			json.put("body", body);
			
			String pacote = "type: com\nbody: "+json.toJSONString();
			
			byte[] buffer = new byte[1024];
			
			buffer = pacote.getBytes(StandardCharsets.UTF_8);
			InetAddress destiny = InetAddress.getByName(this.destino);
			DatagramPacket sendPacket = new DatagramPacket(buffer,buffer.length,destiny,7000);
			
			serverSocket.send(sendPacket);
			
			serverSocket.close();
			
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	

}
