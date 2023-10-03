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
	
	public static void main(String[] args) throws UnknownHostException {
		
		grupos = new  LinkedList<Grupo>();
		
		grupoView = 0;
		

		
		InetAddress localhost = InetAddress.getLocalHost();
		String ip = new String(localhost.getAddress());
		
		Grupo n = new Grupo("uefs",ip);
		
		
		grupos.add(n);
		
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
					Cliente c = new Cliente(addr,7010);
					
					g.addClient(c);
					
					
					
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
	
	public static void enviar(String m, int g) {
		Grupo grupo = grupos.get(g-1);
		Iterator<Cliente> destinos = grupo.getClientes().iterator();
		
		Delivery d = new Delivery(destinos,grupo,m);
		grupo.send(m);
		d.start();
		

		
	}
	
	public static void novaMensagem(String b, int t,String s) {
		
		
		
		try {
			Grupo viewGroup = null;
			viewGroup = grupos.get(grupoView-1);
			
			viewGroup.addMessage(new MensagemIn(b,t,new Cliente(s,7010)));
			
			
		}catch(IndexOutOfBoundsException e) {
			System.out.println("O grupo selecionado não exsite");
		}
		
		
		for(int i = 0; i<50;i++) {
			System.out.println("");
		}
		
		exibir();
	   
	}
	
	
	public static void exibir(){
		try {
			Grupo viewGroup = null;
			
			viewGroup = grupos.get(grupoView-1); 
			Iterator<Mensagem> men = viewGroup.getMensagens().iterator();
		    System.out.println("     "+viewGroup.getNome());
		    while(men.hasNext()) {
		    	Mensagem mensagem = men.next();
		    	
		    	if(mensagem instanceof MensagemIn) {
		    		MensagemIn IN = (MensagemIn) mensagem;
		    		System.out.println(" \n"+IN.getSource().getAddr()+": \n		"+IN.getBody()+" "+IN.getTime()+"\n");
		    	}
		    	
		    	if(mensagem instanceof MensagemOut) {
		    		MensagemOut out = (MensagemOut) mensagem;
		    		System.out.println(" \n                  você"+": \n                  		"+out.getBody()+" "+out.getTime()+"\n");
		    	}
		    }
		    
		    String mensagem = "";
		    Scanner leitor = new Scanner(System.in);
		    
			while(true) {
			    	
				System.out.println("\ndigite uma mensageem para o grupo ou ENTER para sair:");
			    mensagem = leitor.nextLine();
			    if(mensagem.equals("")) {
			    	break;
			    }else {
			    	enviar(mensagem, grupoView);
			    }
			}
		     
		}catch(IndexOutOfBoundsException e) {
			System.out.println("O grupo selecionado não exsite");
		}
	}

}
