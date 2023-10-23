package application;
import java.net.*;
import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedList;
import dados.*;

//programa principal

public class Application {
	
	public static LinkedList<Grupo> grupos;
	public static LinkedList<Mensagem> menssagens;
	public static int grupoView;
	public static Receiver receptor;
	public static String localhost;
		
	public static void main(String[] args) throws UnknownHostException {
		
		grupos = new  LinkedList<Grupo>();
		
		grupoView = 0;
		
		InetAddress ip = InetAddress.getLocalHost();
		localhost = ip.getHostAddress();
		
		receptor = new Receiver();
		receptor.start();
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {	
			System.out.println("\n1 - ver grupos\n2 - criar grupos\n3 - adicionar contato\n 4 - sair");
			System.out.println("\ndigite uma opção: \n");
			String com = sc.nextLine();
			
			if(com.equals("1")) {
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
			
			if(com.equals("2")) {
				System.out.println("\ndigite o nome do grupo: \n");
				String nome = sc.nextLine();
				grupos.add(new Grupo(nome,localhost));
			}
			
			if(com.equals("3")) {
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
			
			if(com.equals("4")) {

				sc.close();
				System.exit(0);
			}
		}
		
	}
	
	public static void enviar(String m, int g) throws UnknownHostException {
		Grupo grupo = grupos.get(g-1);
		Iterator<Cliente> destinos = grupo.getClientes().iterator();
		
		Delivery d = new Delivery(destinos,grupo,m);
		grupo.send(m,localhost);
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
