package application;
import java.net.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
						Long tipoComLong = (Long) json.get("com");
						int tipoComInt = tipoComLong.intValue();
						
						switch(tipoComInt) {
							case 1:
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
									Long idLong = (Long) par.get("id");
									int id = idLong.intValue();
									Cliente c = new Cliente(addr,nome);
									c.setId(id);
									novoGrupo.addClient(c);	
								}
								
								
								for(int i = 0;i<mens.size();i++) {
									JSONObject mensa = (JSONObject) mens.get(i);
									String bodyMen = (String) mensa.get("body");
									JSONArray tempoMen = (JSONArray) mensa.get("temp");
									String addrOri = (String) mensa.get("origem");
								
									int[] tempo = new int[tempoMen.size()];
									for(int j = 0;j<tempo.length;i++) {
										int valor = Integer.parseInt((String)tempoMen.get(j));
										tempo[j] = valor;
									}
									
									Cliente c = novoGrupo.searchClient(addrOri);
									
									Mensagem men = new Mensagem(bodyMen,tempo,c);
									novoGrupo.receive(men);
									
								}
								
								
								
								ReadWriteLock  readWriteLock = new ReentrantReadWriteLock();
								Lock lock = readWriteLock.writeLock();
								try {
									lock.lock();
									Application.main.grupos.add(novoGrupo);
								}finally {
									lock.unlock();
								}
								break;
							case 2:
								JSONObject jsonBody2 = (JSONObject) json.get("body");
								String addr = (String) jsonBody2.get("addr");
								String nome = (String) jsonBody2.get("nome");
								String grupo = (String) jsonBody2.get("grupo");
								Cliente c = new Cliente(addr,nome);
								
								
								Iterator<Grupo> it = Application.main.grupos.iterator();
								
								while(it.hasNext()) {
									Grupo g = (Grupo) it.next();
									if(g.getNome().equals(grupo)) {
										g.addClient(c);
									}
								}
								
								
						}
						
					}
					
					if(tipo.equals("men")) {
						String mensagem = json.get("body").toString();
						String origem = json.get("origem").toString();
						String destino = json.get("grupo").toString();
						JSONArray tempoJson = (JSONArray) json.get("tempo");
						int[] tempo = new int[tempoJson.size()];
						for(int i = 0; i<tempoJson.size();i++) {
							int valor = Integer.parseInt( (String) tempoJson.get(i));
							tempo[i] = valor;
						}
						Grupo grupoDestino = grupoExiste(destino);
						Grupo viewGroup = null;
					
						if(grupoDestino!=null) {
							
							boolean participante = grupoDestino.isPart(origem);
							
							if(Application.main.grupoView>0&&participante) {
								viewGroup = Application.main.grupos.get(Application.main.grupoView-1);
								if(viewGroup.getNome().equals(grupoDestino.getNome())) {
							
									viewGroup.receive(new Mensagem(mensagem,tempo,viewGroup.searchClient(origem)));
									Iterator<Mensagem> i = viewGroup.getMensagens().iterator();
									
									for(int j = 0; j<50;j++) {
										System.out.println("");
									}
									
									 System.out.println("     "+viewGroup.getNome());
									 System.out.print(" seu relógio local: {");
							    	 for(int index = 0;index<viewGroup.getRelogio().length;index++) {
							    		 if(index==viewGroup.getRelogio().length-1) {
							    			 System.out.print(viewGroup.getRelogio()[index]);
							    		 }else {
							    			 System.out.print(viewGroup.getRelogio()[index]+",");
							    		 }
							    	 }
							    	 System.out.print("}\n");
									    while(i.hasNext()) {
									    	Mensagem m = i.next();
									    	
									    	if(m.getSource().getAddr().equals(Application.main.localhost)) {
									    		Mensagem out = (Mensagem) m;
									    		System.out.println(" \n                  você"+": \n                  		"+out.getBody()+"\n");
									    	}else {
									    		Mensagem IN = (Mensagem) m;
									    		System.out.println(" \n"+IN.getSource().getAddr()+": \n		"+IN.getBody()+" "+IN.getTime());
									    		 System.out.print(" relógio do remetente: {");
										    	 for(int index = 0;index<IN.getTime().length;index++) {
										    		 if(index==viewGroup.getRelogio().length-1) {
										    			 System.out.print(viewGroup.getRelogio()[index]);
										    		 }else {
										    			 System.out.print(viewGroup.getRelogio()[index]+",");
										    		 }
										    		 
										    	 }
										    	 System.out.print("}\n");
									    		
									    	}
									    	
									    	
									    }
									    
									    System.out.println("\ndigite uma mensagem para o grupo ou ENTER para sair:");
									
								}else {
									grupoDestino.receive(new Mensagem(mensagem,tempo,viewGroup.searchClient(origem)));
								}
							}else {
								if(participante) {
									
									grupoDestino.receive(new Mensagem(mensagem,tempo,grupoDestino.searchClient(origem)));
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
		Iterator<Grupo> it = Application.main.grupos.iterator();
		
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
