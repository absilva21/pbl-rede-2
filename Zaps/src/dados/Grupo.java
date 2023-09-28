package dados;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
	
	public class Grupo {
	private String nome;
	private String adm;
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
	
	public void addMessage(Mensagem m) {
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
				this.clientes.add(c);
				break;
			}
		}
				
		return result;
	}
	
	public Grupo(String name, String adm) {
		// TODO Auto-generated constructor stub
		this.nome = name;
		this.adm = adm;
		this.clientes = new LinkedList<Cliente>();
		this.mensagens = new LinkedList<Mensagem>();
	}

}
