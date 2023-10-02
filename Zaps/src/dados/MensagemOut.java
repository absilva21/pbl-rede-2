package dados;


//representa uma mensagem enviada
public class MensagemOut extends Mensagem {

	private Cliente destiny; 

	public MensagemOut(String b, int t, Cliente c) {
		super(b, t);
		this.destiny = c;
	}
	
	public Cliente getDestiny() {
		return destiny;
	}

	public void setDestiny(Cliente destiny) {
		this.destiny = destiny;
	}

}
