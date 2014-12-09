package simulador;

public class Constant {
    public static final String[] syntax = {"add", "addi", "addiu", "addu", "and", "andi", "bgt", "blt", "bge", "ble", "bgtu", "bgtz", "beq", "bne", "div", "divu", "j", "jal", "jr", "la", "lb", "lbu", "lh", "lhu", "li", "lui", "lw", "lwcZ", "mfhi", "mflo", "mthi", "mtlo", "mfc0", "mfcZ", "mtcZ", "mult", "multu", "nor", "xor", "or", "ori", "sb", "sh", "slt", "slti", "sltiu", "sltu", "sll", "srl", "sra", "sub", "subu", "sb", "sw", "swcZ", "sh", "syscall"};

    private Constant() {}

    public static String toBinary(int num) {
        String aux = "0";
        String con = Integer.toBinaryString(num);

        if (num >= 0) {
            if (con.length() < 32) {
                int d = 32 - con.length();
                while (aux.length() != d) {
                    aux += "0";
                }
            }

            con = aux + con;
        }

        return con;
    }

    public static String toGreaterBinary(long num) {
        String aux = "0";
        String con = Long.toBinaryString(num);

        if (num >= 0) {
            if (con.length() < 64) {
                int d = 64 - con.length();
                while (aux.length() != d) {
                    aux += "0";
                }
            }

            con = aux + con;
        }

        return con;
    }

    public static String toHexadecimal(int num) {
        String aux = "0";
        String con = Integer.toHexString(num);

        if (num >= 0) {
            if (con.length() < 8) {
                int d = 8 - con.length();
                while (aux.length() != d) aux += "0";
            }

            con = aux + con;
        }

        return con;
    }

    public static int toInteger(String str) {
        int i = 0;
        if (str.charAt(0) == '1') {
            String aux = "0";
            while (i < str.length()) {
                if (str.charAt(i) == '1') aux += "0";
                else aux += "1";
                i++;
            }
            return -1 * (Integer.parseInt(aux, 2) + 1);
        } else {
            return Integer.parseInt(str, 2);
        }
    }

    public static long toUnsignedInteger(String str) {
        return Long.parseLong(str, 2);
    }
}