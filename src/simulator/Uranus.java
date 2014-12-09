package simulator;

import gui.UI;

public class Uranus {
	public static UI ui;
	
	public Uranus(Processor p, Parser mont, CU CU) {
		Uranus.ui = new UI(p, Memory.getInstance(), mont, CU);
	}
}
