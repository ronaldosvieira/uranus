package simulador;

import gui.UI;

public class Uranus {
	public static UI ui;
	
	public Uranus(Processador p, Memory m, Assembler mont, UC uc) {
		Uranus.ui = new UI(p, m, mont, uc);
	}
}
