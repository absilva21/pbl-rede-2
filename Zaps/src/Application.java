import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.*;



public class Application {
	public static LinkedList<String> menssagens;
	
	public static void main(String[] args) {
		
	
		
		menssagens = new  LinkedList<String>();
		
		Receiver receptor = new Receiver();
		receptor.run();
	

	}
	
	public static void novaMensagem() {

	    Iterator<String> men = menssagens.iterator();
	    
	    while(men.hasNext()) {
	    	String mensagem = men.next();
	    	System.out.println("\nfulano disse: \n "+mensagem);
	    }
	}

}
