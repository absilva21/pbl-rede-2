package dados;


public class MensagemIn extends Mensagem {
	
	private Cliente source;
	
	public MensagemIn(String b, int t, Cliente c) {
		super(b,t);
		this.source = c;
	}

	public Cliente getSource() {
		return source;
	}

	public void setSource(Cliente source) {
		this.source = source;
	}

}
