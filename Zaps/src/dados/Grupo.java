package dados;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import application.Application;
import application.MensagemComparator;
import application.SyncM;
	
	public class Grupo {
	private String nome;
	private String adm;
	private int[] relogio;
	private int idIndex;
	private SyncM syncM;

	

	private LinkedList<Mensagem> mensagens;
	private LinkedList<Cliente> clientes;

	public String getAdm() {
		return adm;
	}

	public void setAdm(String adm) {
		this.adm = adm;
	}

	
	public String getNome() {
		return nome;
	}
	
	public int[] getRelogio() {
		return relogio;
	}

	public void setRelogio(int[] relogio) {
		this.relogio = relogio;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public LinkedList<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(LinkedList<Cliente> clientes) {
		this.clientes = clientes;
	}

	public LinkedList<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(LinkedList<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}
	
	public int getIdIndex() {
		return idIndex;
	}

	public void setIdIndex(int idIndex) {
		this.idIndex = idIndex;
	}
	
	public SyncM getSyncM() {
		return syncM;
	}

	public void setSyncM(SyncM syncM) {
		this.syncM = syncM;
	}
	
	@Deprecated
	public void addMessage(Mensagem m) {
		Cliente localHost = this.searchClient(Application.main.localhost);
		int index = localHost.getId();
		for(int i=0;i<this.relogio.length;i++) {
			if(i==index) {
				this.relogio[index]++;
			}else {
				this.relogio[i] = m.getTime()[i];
			}
		}
		
		m.setTime(this.relogio);
		this.mensagens.add(m);
	}

	public void removeClient(int index) throws UnknownHostException {
		InetAddress localhost = InetAddress.getLocalHost();
		String ip = new String(localhost.getAddress()); 
		if(ip.equals(adm)) {
			this.clientes.remove(index);
		}
		
	}
	
	public boolean addClient(Cliente c) {
		boolean result = false;
		
		Iterator<Cliente> i = this.clientes.iterator();
		
		while(i.hasNext()) {
			Cliente search = i.next();
			if(search.getAddr().equals(c.getAddr())) {
				break;
			}
			
			if((search.equals(this.clientes.getLast()))&&(!search.getAddr().equals(c.getAddr()))) {
				result = true;
				break;
			}
		}
		
		if(this.clientes.size()==0) {
			result = true;
		}
		
		if(result) {
			
			this.relogio = Arrays.copyOf(this.relogio, this.relogio.length+1);
			c.setId(this.relogio.length-1);
			this.clientes.add(c);
			

		}
		
		return result;
	}
	
	public LinkedList<Mensagem> getMensageSource(String addr){
		LinkedList<Mensagem> result = new LinkedList<Mensagem>();
		Iterator<Mensagem> i = this.mensagens.iterator();
		while(i.hasNext()) {
			Mensagem m = i.next();
			String source = m.getSource().getAddr();
			if(source.equals(addr)) {
				result.add(m);
			}
		}
		return result;
	}
	
	public LinkedList<Mensagem> getFouls(int[] f){
		LinkedList<Mensagem> result = new LinkedList<Mensagem>();
		for(int i = 0;i<f.length;i++) {
			Mensagem m = this.getMensage(f[i]);
			if(!m.equals(null)) {
				result.add(m);
			}
		}
		
		return result;
	}
	
	public Mensagem getMensage(int id) {
		Mensagem result = null;
		
		Iterator<Mensagem> i = this.mensagens.iterator();
		
		while(i.hasNext()) {
			Mensagem m = i.next();
			if(id==m.getIdLocal()) {
				result = m;
				break;
			}
		}
		
		
		return result;
	}
	
	public void receive(Mensagem m) {
		Cliente c = this.searchClient(Application.main.localhost);
		for(int i=0;i<this.relogio.length;i++) {
			
			this.relogio[i] = m.getTime()[i];
			
		}
		
		this.relogio[c.getId()]++;
		
		this.mensagens.add(m);
		
	}
	
	public void send(Mensagem m) {
		Cliente localHost = this.searchClient(Application.main.localhost);
		this.idIndex++;
		m.setIdLocal(this.idIndex);
		int index = localHost.getId();
		this.relogio[index]++;
		m.setTime(this.relogio);
		this.mensagens.add(m);
	}
	
	
	public Cliente searchClient(String ip) {
		Cliente result = null;
		
		
		Iterator<Cliente> i =this.clientes.iterator();
		
		while(i.hasNext()) {
			Cliente c = (Cliente) i.next();
			
			if(c.getAddr().equals(ip)) {
				result = c;
				break;
			}
		}
		
		return result;
	}
	
	public boolean isPart(String ip) {
		boolean result = false;
		
		Cliente c = this.searchClient(ip);
		
		if(c!=null) {
			result = true;
		}
		
		return result;
	}
	
	public void ordenarMensagens() {
		 Collections.sort(this.mensagens, new MensagemComparator());
	}
	
	public Grupo(String name, String adm) {
		// TODO Auto-generated constructor stub
		this.nome = name;
		this.adm = adm;
		this.clientes = new LinkedList<Cliente>();
		this.mensagens = new LinkedList<Mensagem>();
		this.relogio = new int[0];
		this.addClient(new Cliente(adm,"você"));
		this.idIndex = 0;
		//this.syncM = new SyncM(this);
		//this.syncM.start();
	}

}
