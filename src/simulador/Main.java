package simulador;

import gui.SplashScreen;

public class Main {
	public static void main (String [] args) throws ErroAcesso, IntegerOutofRangeException, OverflowException {
		
		SplashScreen splash = new SplashScreen(5000);
        splash.showSplash();

		Memory m = new Memory();
		Processador p = new Processador();
		ULA ula = new ULA(m);
		Assembler mont = new Assembler(m, p);
		UC uc = new UC(m, p, ula, mont);
		@SuppressWarnings("unused")
		Uranus ur = new Uranus(p, m, mont, uc);
	}
}