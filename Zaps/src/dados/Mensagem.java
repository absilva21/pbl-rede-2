package dados;

//representa uma mensagem
public class Mensagem {
	private String body;
	private int time;
	private Cliente source;
	 
	
	
	
	public Cliente getSource() {
		return source;
	}

	public void setSource(Cliente source) {
		this.source = source;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Mensagem(String b, int t, Cliente c) {
		this.body = b;
		this.time = t;
		this.source = c;
		
	}

}
