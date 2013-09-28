package simulador;

import gui.SplashScreen;

public class Main {
	public static void main (String [] args) throws ErroAcesso, IntegerOutofRange, Overflow{ 
		
		SplashScreen splash = new SplashScreen(5000);
        splash.showSplash();
		
		Constante c = new Constante();
		Memoria m = new Memoria();
		Processador p = new Processador();
		ULA ula = new ULA(m);
		Assembler mont = new Assembler(m, p, c);
		UC uc = new UC(m, p, ula, mont);
		@SuppressWarnings("unused")
		Uranus ur = new Uranus(p, m, mont, uc);
	}
}