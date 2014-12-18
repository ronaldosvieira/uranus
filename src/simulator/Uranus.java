package simulator;

import gui.UI;

public class Uranus {
	public static UI ui;
	
	public Uranus(Processor p, Parser mont, ControlUnit ControlUnit) {
		Uranus.ui = new UI(p, Memory.getInstance(), mont, ControlUnit);
	}
}
