package application;
import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.*;

//programa principal

public class Application {
	public static LinkedList<String> menssagens;
	
	public static void main(String[] args) {
		
	
		
		menssagens = new  LinkedList<String>();
		
		Receiver receptor = new Receiver();
		receptor.run();
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		

	}
	
	public static void novaMensagem() {
		
		for(int i = 0; i<50;i++) {
			System.out.println("");
		}
		
	    Iterator<String> men = menssagens.iterator();
	    
	    while(men.hasNext()) {
	    	String mensagem = men.next();
	    	System.out.println("\nfulano disse: \n "+mensagem);
	    }
	}

}
