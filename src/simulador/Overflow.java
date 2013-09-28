package simulador;

public class Overflow extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String toString(){
		Uranus.ig.writeError("Erro: Processo terminou com overflow.");
		return "Processo terminou com overflow";
	}
	
	public String getMessage(){
		return "Processo terminou com overflow.";
	}
}
