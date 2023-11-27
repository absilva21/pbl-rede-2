package application;
import java.net.*;
import java.util.Iterator;
import java.io.*;

import org.json.simple.JSONArray;
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
				String[] tipoArray = payloadArray[0].split(" ");
				int inicioJson = payloadArray[1].indexOf('{');
				int fimJson = payloadArray[1].lastIndexOf('}')+1;
				String jsonBody = payloadArray[1].substring(inicioJson, fimJson);
				
			
				
				JSONParser parser = new JSONParser(); 
			
				
				try {
					JSONObject json = (JSONObject) parser.parse(jsonBody);
					String tipo = tipoArray[1];
					
					
					if(tipo.equals("com")) {
						
						System.out.println("\n"+json.toString());
						String tipoCom = (String)json.get("com");
						
						if(tipoCom.equals("add")) {
							JSONObject grupoJson = (JSONObject) json.get("body");
							String nomeGrupo = (String) grupoJson.get("nome");
							String admGrupo = (String) grupoJson.get("adm");
							Grupo novoGrupo = new Grupo(nomeGrupo,admGrupo);
							JSONArray mens = (JSONArray)  grupoJson.get("mensagens");
							JSONArray partici =  (JSONArray) grupoJson.get("clientes");
							
							for(int i=0;i<partici.size();i++) {
								JSONObject par = (JSONObject) partici.get(i);
								String addr = (String) par.get("addr");
								String nome = (String) par.get("nome");
								Cliente c = new Cliente(addr,nome);
								novoGrupo.addClient(c);	
							}
							
							for(int i = 0;i<mens.size();i++) {
								JSONObject mensa = (JSONObject) mens.get(i);
								String bodyMen = (String) mensa.get("body");
								String tempoMen = (String) mensa.get("temp");
								String addrOri = (String) mensa.get("origem");
								String nomeOri = (String) mensa.get("nomeOrigem");
								int tempo = Integer.parseInt(tempoMen);
								Cliente c = new Cliente(addrOri,nomeOri);
								
								Mensagem men = new Mensagem(bodyMen,tempo,c);
								novoGrupo.addMessage(men);
								
							}
							
							synchronized(this) {
								Main.grupos.add(novoGrupo);
							}
							
						}
					}
					
					if(tipo.equals("men")) {
						String mensagem = json.get("body").toString();
						String origem = json.get("origem").toString();
						String destino = json.get("grupo").toString();
						String tempoString = (String) json.get("tempo");
						int tempo = Integer.parseInt(tempoString);
						Grupo grupoDestino = grupoExiste(destino);
						Grupo viewGroup = null;
						
						if(grupoDestino!=null) {
							
							boolean participante = grupoDestino.isPart(origem);
							
							if(Main.grupoView>0&&participante) {
								viewGroup = Main.grupos.get(Main.grupoView-1);
								if(viewGroup.getNome().equals(grupoDestino.getNome())) {
									
									viewGroup.addMessage(new Mensagem(mensagem,tempo,viewGroup.searchClient(origem)));
									Iterator<Mensagem> i = viewGroup.getMensagens().iterator();
									
									for(int j = 0; j<50;j++) {
										System.out.println("");
									}
									
									 System.out.println("     "+viewGroup.getNome());
									
									    while(i.hasNext()) {
									    	Mensagem m = i.next();
									    	
									    	if(m.getSource().getAddr().equals(Main.localhost)) {
									    		Mensagem out = (Mensagem) m;
									    		System.out.println(" \n                  você"+": \n                  		"+out.getBody()+" "+out.getTime()+"\n");
									    	}else {
									    		Mensagem IN = (Mensagem) m;
									    		System.out.println(" \n"+IN.getSource().getAddr()+": \n		"+IN.getBody()+" "+IN.getTime()+"\n");
									    		
									    	}
									    	
									    	
									    }
									    
									    System.out.println("\ndigite uma mensageem para o grupo ou ENTER para sair:");
									
								}else {
									grupoDestino.addMessage(new Mensagem(mensagem,Main.clock,grupoDestino.searchClient(origem)));
								}
							}else {
								if(participante) {
									grupoDestino.addMessage(new Mensagem(mensagem,Main.clock,grupoDestino.searchClient(origem)));
								}
							}
						}
					}
					
					
					
					
					
					
					
					
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
	
	public Grupo grupoExiste(String grupo) {
		
		Grupo result = null;
		Iterator<Grupo> it = Main.grupos.iterator();
		
		while(it.hasNext()) {
			Grupo g = (Grupo) it.next();
			if(g.getNome().equals(grupo)) {
				result = g;
				break;
			}
		}
		
	
		return result;
		
	}
	
}
