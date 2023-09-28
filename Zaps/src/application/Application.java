package application;
import java.net.*;
import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.*;
import dados.*;

//programa principal

public class Application {
	public static LinkedList<Grupo> grupos;
	public static LinkedList<Mensagem> menssagens;
	
	public static void main(String[] args) throws UnknownHostException {
		
		grupos = new  LinkedList<Grupo>();
		
		
		menssagens = new  LinkedList<Mensagem>();
		
		InetAddress localhost = InetAddress.getLocalHost();
		String ip = new String(localhost.getAddress());
		
		Grupo n = new Grupo("uefs",ip);
		
		grupos.add(n);
		
		//Receiver receptor = new Receiver();
		//receptor.run();
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {	
			System.out.println("\n1 - ver grupos\n2-criar grupos\n3 - sair");
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
			}
		}
		
		
		

	}
	
	public static void novaMensagem(Mensagem m) {
		
		menssagens.add(m);
		
		for(int i = 0; i<50;i++) {
			System.out.println("");
		}
		
	    Iterator<Mensagem> men = menssagens.iterator();
	    
	    while(men.hasNext()) {
	    	Mensagem mensagem = men.next();
	    	
	    	if(mensagem instanceof MensagemIn) {
	    		MensagemIn IN = (MensagemIn) mensagem;
	    		System.out.println(" \n"+IN.getSource().getAddr()+": \n		"+IN.getBody()+" "+IN.getTime()+"\n");
	    	}
	    }
	}

}
