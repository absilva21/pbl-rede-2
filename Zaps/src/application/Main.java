package application;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.locks.*;
import java.util.Arrays;
import dados.Cliente;
import dados.Delivery;
import dados.Grupo;
import dados.Mensagem;

public class Main extends Thread {
	
	public  LinkedList<Grupo> grupos;
	public  LinkedList<Mensagem> menssagens;
	public  int grupoView;
	public  Receiver receptor;
	public  String localhost;
	public  Unpacker unpacker;

	
	
	

	public Main() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		grupos = new  LinkedList<Grupo>();
		grupoView = 0;
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		localhost = ip.getHostAddress();
		
		unpacker = new Unpacker();
		unpacker.start();
		
		receptor = new Receiver();
		receptor.start();
		
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {	
			System.out.println("\n1 - ver grupos\n2 - criar grupos\n3 - adicionar contato\n 4 - sair");
			System.out.println("\ndigite uma opção: \n");
			String com = sc.nextLine();
			
			if(com.equals("1")) {
				synchronized (Application.class) {
					Iterator<Grupo> i = grupos.iterator();
					int index = 0;
					while(i.hasNext()) {
						Grupo g = i.next();
						index++;
						
						System.out.println("\n"+index+" - "+g.getNome());
					}
					System.out.println("\nx - voltar\n");
					System.out.println("\ndigite uma opção: \n");
					String input = sc.nextLine();
					
					if(!input.equals("x")) {
						grupoView = Integer.parseInt(input); 
						exibir();
					}
				}
				
				    
			}
			
			if(com.equals("2")) {
				ReadWriteLock  readWriteLock = new ReentrantReadWriteLock();
				Lock lock = readWriteLock.writeLock();
				System.out.println("\ndigite o nome do grupo: \n");
				String nome = sc.nextLine();
				try {
					lock.lock();
					grupos.add(new Grupo(nome,localhost));
				}finally {
					lock.unlock();
				}
				
				
			}
			
			if(com.equals("3")) {
				
				ReadWriteLock  readWriteLock = new ReentrantReadWriteLock();
				Lock lock = readWriteLock.readLock();
					
				try {
					lock.lock();
					Iterator<Grupo> i = grupos.iterator();
					int index = 0;
					while(i.hasNext()) {
						Grupo g = i.next();
						index++;
						
						System.out.println("\n"+index+" - "+g.getNome());
					}
					
					
				}finally {
					lock.unlock();
				}
					
				System.out.println("\ndigite o numero do grupo: ");
				
				int grupoIndex = sc.nextInt();
				Cliente c = null;
				Grupo g = null;
				Scanner s = new Scanner(System.in);
				Lock lock2 = readWriteLock.writeLock();
				try {
					g = grupos.get(grupoIndex-1);
					System.out.println("\ndigite o ip do contato: ");
					String addr = s.nextLine();
					System.out.println("\ndigite o nome do contato: ");
					String nome = s.nextLine();
				    c = new Cliente(addr,nome);
					lock2.lock();
					g.addClient(c);
					
					
										
				}catch(IndexOutOfBoundsException e) {
					System.out.println("\nO grupo selecionado não exsite\n");
					lock2.unlock();
				}finally {
					lock2.unlock();
				}
				
				if(!c.equals(null)&&!g.equals(null)) {
					Sync sync = new Sync(c,1,g);
					sync.start();
				}
					
			}
			
			if(com.equals("4")) {

				sc.close();
				System.exit(0);
			}
		}
	}
	
	public  void enviar(String m, int g) throws UnknownHostException {
		Grupo grupo = grupos.get(g-1);
		Iterator<Cliente> destinos = grupo.getClientes().iterator();
		Cliente c = grupo.searchClient(this.localhost);
		Mensagem men = null;
		ReadWriteLock lock = new ReentrantReadWriteLock();
		Lock readLock = lock.readLock();
		try {
			readLock.lock();
			
			men =  new Mensagem(m,new int[1],c);
			grupo.send(men);
			Delivery d = new Delivery(destinos,grupo,men);
			d.start();		
		}finally {
			readLock.unlock();
		}
		 
		
		
		
	}
	
	public void exibir(){
		try {
			Grupo viewGroup = null;
			
			viewGroup = this.grupos.get(this.grupoView-1); 
			Iterator<Mensagem> men = viewGroup.getMensagens().iterator();
		    System.out.println("     "+viewGroup.getNome());
		    while(men.hasNext()) {
		    	Mensagem mens = men.next();
		    	
		    	if(mens.getSource().getAddr().equals(this.localhost)) {
		    		Mensagem out = (Mensagem) mens;
		    		System.out.println(" \n                  você"+": \n                  		"+out.getBody()+" "+out.getTime()+"\n");
		    	}else {
		    		Mensagem IN = (Mensagem) mens;
		    		System.out.println(" \n"+IN.getSource().getAddr()+": \n		"+IN.getBody()+" "+IN.getTime()+"\n");
		    	}
		    		
		    }
		    
		    String mensagem = "";
		    Scanner leitor = new Scanner(System.in);
		    
			while(true) {
			    	
				System.out.println("\ndigite uma mensagem para o grupo ou ENTER para sair:");
			    mensagem = leitor.nextLine();
			    if(mensagem.equals("")) {
			    	this.grupoView = 0;
			    	break;
			    }else {
			    	 enviar(mensagem, this.grupoView);
			    	 men = viewGroup.getMensagens().iterator();
			    	 for(int j = 0; j<50;j++) {
							System.out.println("");
					 }
			    	 System.out.print("     "+viewGroup.getNome());
			    	 System.out.print(" seu relógio local: {");
			    	 for(int index = 0;index<viewGroup.getRelogio().length;index++) {
			    		 if(index==viewGroup.getRelogio().length-1) {
			    			 System.out.print(viewGroup.getRelogio()[index]);
			    		 }else {
			    			 System.out.print(viewGroup.getRelogio()[index]+",");
			    		 }
			    		
			    	 }
			    	 System.out.print("}");
					 while(men.hasNext()) {
						 Mensagem mens = men.next();
					    	
						 if(mens.getSource().getAddr().equals(localhost)) {
							 Mensagem out = (Mensagem) mens;
					    	System.out.println(" \n                  você"+": \n                  		"+out.getBody()+"\n");
						 }else {
							 Mensagem IN = (Mensagem) mens;
							 System.out.println(" \n"+IN.getSource().getAddr()+": \n		"+IN.getBody()+"\n");
							 System.out.print(" relógio do remetente: {");
					    	 for(int index = 0;index<IN.getTime().length;index++) {
					    		 System.out.print(IN.getTime()[index]+",");
					    	 }
					    	 System.out.print("}\n");
					    }
					    		
					 }
			    }
			}
		     
		}catch(IndexOutOfBoundsException e) {
			System.out.println("O grupo selecionado não exsite");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
