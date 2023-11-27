package application;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import dados.Cliente;
import dados.Delivery;
import dados.Grupo;
import dados.Mensagem;

public class Main extends Thread {
	
	public static LinkedList<Grupo> grupos;
	public static LinkedList<Mensagem> menssagens;
	public static int grupoView;
	public static Receiver receptor;
	public static String localhost;
	public static int clock;

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public Main(Runnable target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	public Main(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public Main(ThreadGroup group, Runnable target) {
		super(group, target);
		// TODO Auto-generated constructor stub
	}

	public Main(ThreadGroup group, String name) {
		super(group, name);
		// TODO Auto-generated constructor stub
	}

	public Main(Runnable target, String name) {
		super(target, name);
		// TODO Auto-generated constructor stub
	}

	public Main(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
		// TODO Auto-generated constructor stub
	}

	public Main(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
		// TODO Auto-generated constructor stub
	}

	public Main(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
		super(group, target, name, stackSize, inheritThreadLocals);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		grupos = new  LinkedList<Grupo>();
		clock = 0;
		grupoView = 0;
		
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		localhost = ip.getHostAddress();
		
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
				synchronized (Application.class) {
					System.out.println("\ndigite o nome do grupo: \n");
					String nome = sc.nextLine();
					grupos.add(new Grupo(nome,localhost));
				}
				
			}
			
			if(com.equals("3")) {
				synchronized (Application.class) {
					Iterator<Grupo> i = grupos.iterator();
					int index = 0;
					while(i.hasNext()) {
						Grupo g = i.next();
						index++;
						
						System.out.println("\n"+index+" - "+g.getNome());
					}
					
					System.out.println("\ndigite o numero do grupo: ");
					
					int grupoIndex = sc.nextInt();
					
					Scanner s = new Scanner(System.in);
					
					try {
						Grupo g = grupos.get(grupoIndex-1);
						System.out.println("\ndigite o ip do contato: ");
						String addr = s.nextLine();
						System.out.println("\ndigite o nome do contato: ");
						String nome = s.nextLine();
						Cliente c = new Cliente(addr,nome);
						
						g.addClient(c);
						
						Sync sync = new Sync(c.getAddr(),"add",g);
						sync.start();
											
					}catch(IndexOutOfBoundsException e) {
						System.out.println("\nO grupo selecionado não exsite\n");
					}
				}
				
			}
			
			if(com.equals("4")) {

				sc.close();
				System.exit(0);
			}
		}
	}
	
	public static void enviar(String m, int g) throws UnknownHostException {
		Grupo grupo = grupos.get(g-1);
		Iterator<Cliente> destinos = grupo.getClientes().iterator();
		Cliente c = new Cliente("você",localhost);
		Mensagem men = new Mensagem(m,clock,c);
		Delivery d = new Delivery(destinos,grupo,men);
		grupo.send(men);
		d.start();		
		
		
	}
	
	public static void exibir(){
		try {
			Grupo viewGroup = null;
			
			viewGroup = grupos.get(grupoView-1); 
			Iterator<Mensagem> men = viewGroup.getMensagens().iterator();
		    System.out.println("     "+viewGroup.getNome());
		    while(men.hasNext()) {
		    	Mensagem mens = men.next();
		    	
		    	if(mens.getSource().getAddr().equals(localhost)) {
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
			    	grupoView = 0;
			    	break;
			    }else {
			    	 enviar(mensagem, grupoView);
			    	 men = viewGroup.getMensagens().iterator();
			    	 for(int j = 0; j<50;j++) {
							System.out.println("");
					 }
			    	 System.out.println("     "+viewGroup.getNome());
					 while(men.hasNext()) {
						 Mensagem mens = men.next();
					    	
						 if(mens.getSource().getAddr().equals(localhost)) {
							 Mensagem out = (Mensagem) mens;
					    	System.out.println(" \n                  você"+": \n                  		"+out.getBody()+" "+out.getTime()+"\n");
						 }else {
							 Mensagem IN = (Mensagem) mens;
							 System.out.println(" \n"+IN.getSource().getAddr()+": \n		"+IN.getBody()+" "+IN.getTime()+"\n");
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
