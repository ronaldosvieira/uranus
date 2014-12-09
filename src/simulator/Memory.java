package simulator;

import java.util.Map;
import java.util.TreeMap;

public class Memory {
    private Map<Integer, String> m;

    private static Memory instance = null;
    private static int SIZE_MEM_BYTES = 4000;

    private Memory() {
        m = new TreeMap<>();

        for (int i = 0; i < SIZE_MEM_BYTES; i = i + 4) m.put(i, null);
    }

    public static Memory getInstance() {
        if (instance == null) instance = new Memory();

        return instance;
    }

    public void setWord(String f, int index) {
        m.put(index, f);
    }

    public String getWord(int index) {
        return m.get(index);
    }

    public void setValue(int index, int valor) {
        String s = Integer.toBinaryString(valor);

        if (s.length() < 32) {
            String aux = "0";
            int t = 32 - s.length();

            while (aux.length() != t) aux += "0";

            s = aux + s;
        }

        m.put(index, s);
    }

    public int getValue(int index) {
        //return Integer.parseInt(mem.get(index),2);
        return Constant.toInteger(m.get(index));
    }

    public int size() {
        return m.size();
    }

    public void clear() {
        this.m.clear();
    }
}
