package dados;
import java.net.*;

//representa uma mensagem enviada
public class MensagemOut extends Mensagem {

	private DatagramPacket destiny; 

	public MensagemOut(String b, int t, DatagramPacket d) {
		super(b, t);
		this.destiny = d;
	}

	public DatagramPacket getDestiny() {
		return destiny;
	}

	public void setDestiny(DatagramPacket destiny) {
		this.destiny = destiny;
	}

}
