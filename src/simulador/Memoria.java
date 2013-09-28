package simulador;

import java.util.Map;
import java.util.TreeMap;

public class Memoria {
	//public ArrayList<String> m = new ArrayList<String>();
	public Constante c = new Constante();
	public Map<Integer, String> m = new TreeMap<Integer, String>();
	
	public Memoria(){
		for(int i=0; i<4000; i=i+4) m.put(i, null);
	}
	
	public void setInstrucao(String f, int pos){
		m.remove(pos);
		m.put(pos, f);
	}
	
	public String getInstrucao(int index){
		return m.get(index);
	}
	
	public void setValor(int index, int valor){
		String s = Integer.toBinaryString(valor);
		if(s.length()<32){
			String aux = "0";
			int t = 32 - s.length();
			while(aux.length()!=t){
				aux += "0";
			}
			s = aux+s;
		}
		m.remove(index);
		m.put(index, s);
	}
	
	public int getValor(int index){
		//return Integer.parseInt(m.get(index),2);
		return c.toInteger(m.get(index));
	}
	
	public int size(){
		return m.size();
	}
	
	public void clear(){
		this.m.clear();
	}
}
