package simulador;

import gui.InterfaceGrafica;

public class Uranus {
	public static InterfaceGrafica ig; // static = todos podem acessar
	
	public Uranus(Processador p, Memoria m, Assembler mont, UC uc) {
		Uranus.ig = new InterfaceGrafica(p, m, mont, uc);
	}
}
