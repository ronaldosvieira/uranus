package simulador;

public class Registrador {
	public String endereco;
	public String valor = "00000000000000000000000000000000";
	public Constante c = new Constante();
	public Registrador(String endereco){
		this.endereco = endereco;
	}
	
	public int getValor(){
		return c.toInteger(valor);
	}
	
	public long getUnsignedValue(){
		return c.toUnsignedInteger(valor);		
	}
	
	public void setValor(int valor){
		this.valor = c.toBinary(valor);
	}
	
	public String toString(){
		return endereco;
	}
}