package dados;

public class Cliente {
	
	private String addr;
	private String nome;
	
	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Cliente(String addr, String n) {
		// TODO Auto-generated constructor stub
		this.addr = addr;
		this.nome = n;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}
