package dados;
import java.net.*;

public class MensagemIn extends Mensagem {
	
	private DatagramPacket source;
	
	public MensagemIn(String b, int t,DatagramPacket p) {
		super(b,t);
		this.source = p;
	}

	public DatagramPacket getSource() {
		return source;
	}

	public void setSource(DatagramPacket source) {
		this.source = source;
	}

}
