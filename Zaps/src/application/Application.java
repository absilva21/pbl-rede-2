package application;
import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.*;
import dados.*;

//programa principal

public class Application {
	public static LinkedList<Mensagem> menssagens;
	
	public static void main(String[] args) {
		
	
		
		menssagens = new  LinkedList<Mensagem>();
		
		Receiver receptor = new Receiver();
		receptor.run();
		
		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		

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
