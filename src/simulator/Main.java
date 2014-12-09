package simulator;

import gui.SplashScreen;
import simulator.exceptions.AccessErrorException;
import simulator.exceptions.IntegerOutofRangeException;
import simulator.exceptions.OverflowException;

public class Main {
	public static void main (String [] args) throws AccessErrorException, IntegerOutofRangeException, OverflowException {
		
		SplashScreen splash = new SplashScreen(5000);
        splash.showSplash();

		Processor p = new Processor();
		ALU ALU = new ALU();
		Parser mont = new Parser(p);
		CU CU = new CU(p, ALU, mont);
		@SuppressWarnings("unused")
		Uranus ur = new Uranus(p, mont, CU);
	}
}