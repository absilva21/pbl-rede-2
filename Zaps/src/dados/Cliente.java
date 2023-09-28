package dados;

public class Cliente {
	
	private String addr;
	private int port;

	public Cliente(String addr, int p) {
		// TODO Auto-generated constructor stub
		this.addr = addr;
		this.port = p;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
