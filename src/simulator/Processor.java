package simulator;

import java.io.*;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Processor {
    private static Processor instance;

    private Map<String, Register> regs;
    private Map<String, Operation> ops;

    private Processor() {
        regs = new TreeMap<>();
        ops = new TreeMap<>();

        // TODO: make it read all the registers and operations from a file
        /*
        FileReader fr = null;
        try {
            fr = new FileReader(new File("simulator/data.txt"));
            BufferedReader br = new BufferedReader(fr);

            while (br.ready()) {
                StringTokenizer st = new StringTokenizer(br.readLine(), " ");

                while (st.hasMoreElements()) {
                    String registerName = st.nextToken();
                    String registerAddress = st.nextToken();

                    regs.put(registerName, new Register(registerAddress));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        regs.put("$zero", new Register("00000"));
        regs.put("$at", new Register("00001"));
        regs.put("$v0", new Register("00010"));
        regs.put("$v1", new Register("00011"));
        regs.put("$a0", new Register("00100"));
        regs.put("$a1", new Register("00101"));
        regs.put("$a2", new Register("00110"));
        regs.put("$a3", new Register("00111"));
        regs.put("$t0", new Register("01000"));
        regs.put("$t1", new Register("01001"));
        regs.put("$t2", new Register("01010"));
        regs.put("$t3", new Register("01011"));
        regs.put("$t4", new Register("01100"));
        regs.put("$t5", new Register("01101"));
        regs.put("$t6", new Register("01110"));
        regs.put("$t7", new Register("01111"));
        regs.put("$s0", new Register("10000"));
        regs.put("$s1", new Register("10001"));
        regs.put("$s2", new Register("10010"));
        regs.put("$s3", new Register("10011"));
        regs.put("$s4", new Register("10100"));
        regs.put("$s5", new Register("10101"));
        regs.put("$s6", new Register("10110"));
        regs.put("$s7", new Register("10111"));
        regs.put("$t8", new Register("11000"));
        regs.put("$t9", new Register("11001"));
        regs.put("$k0", new Register("11010"));
        regs.put("$k1", new Register("11011"));
        regs.put("$gp", new Register("11100"));
        regs.put("$sp", new Register("11101"));
        regs.put("$fp", new Register("11110"));
        regs.put("$ra", new Register("11111"));
        regs.put("hi", new Register(null));
        regs.put("lo", new Register(null));

        ops.put("add", new Operation("000000", "100000"));
        ops.put("addu", new Operation("000000", "100001"));
        ops.put("sub", new Operation("000000", "100010"));
        ops.put("subu", new Operation("000000", "100011"));
        ops.put("and", new Operation("000000", "100100"));
        ops.put("or", new Operation("000000", "100101"));
        ops.put("nor", new Operation("000000", "100111"));
        ops.put("slt", new Operation("000000", "101010"));
        ops.put("sltu", new Operation("000000", "101011"));
        ops.put("mult", new Operation("000000", "011000"));
        ops.put("div", new Operation("000000", "011010"));
        ops.put("srl", new Operation("000000", "000010"));
        ops.put("sll", new Operation("000000", "000000"));
        ops.put("jr", new Operation("000000", "001000"));
        ops.put("syscall", new Operation("000000", "001100"));
        ops.put("mfhi", new Operation("000000", "010000"));
        ops.put("mflo", new Operation("000000", "010010"));
        ops.put("lw", new Operation("100011", null));
        ops.put("sw", new Operation("101011", null));
        ops.put("lb", new Operation("100000", null));
        ops.put("lbu", new Operation("100100", null));
        ops.put("sb", new Operation("101000", null));
        ops.put("lh", new Operation("100001", null));
        ops.put("lhu", new Operation("100101", null));
        ops.put("sh", new Operation("101001", null));
        ops.put("lui", new Operation("001111", null));
        ops.put("beq", new Operation("000100", null));
        ops.put("bne", new Operation("000101", null));
        ops.put("blez", new Operation("000110", null));
        ops.put("bgtz", new Operation("000111", null));
        ops.put("addi", new Operation("001000", null));
        ops.put("addiu", new Operation("001001", null));
        ops.put("slti", new Operation("001010", null));
        ops.put("sltiu", new Operation("001011", null));
        ops.put("andi", new Operation("001100", null));
        ops.put("ori", new Operation("001101", null));
        ops.put("j", new Operation("000010", null));
        ops.put("jal", new Operation("000011", null));

        regs.get("$zero").setValue(0);
        regs.get("$sp").setValue(4000);
    }

    public static Processor getInstance() {
        if (instance == null) instance = new Processor();

        return instance;
    }

    public String getRegisterAdress(String str) {
        return regs.get(str).getAddress();
    }

    public String getOpcode(String str) {
        return ops.get(str).opcode;
    }

    public String getFunction(String str) {
        return ops.get(str).function;
    }

    public Map<String, Register> getRegister() {
        return regs;
    }

    public Register getRegisterByAddress(String str) {
        switch (str) {
            case "00000":
                return regs.get("$zero");
            case "00001":
                return regs.get("$at");
            case "00010":
                return regs.get("$v0");
            case "00011":
                return regs.get("$v1");
            case "00100":
                return regs.get("$a0");
            case "00101":
                return regs.get("$a1");
            case "00110":
                return regs.get("$a2");
            case "00111":
                return regs.get("$a3");
            case "01000":
                return regs.get("$t0");
            case "01001":
                return regs.get("$t1");
            case "01010":
                return regs.get("$t2");
            case "01011":
                return regs.get("$t3");
            case "01100":
                return regs.get("$t4");
            case "01101":
                return regs.get("$t5");
            case "01110":
                return regs.get("$t6");
            case "01111":
                return regs.get("$t7");
            case "10000":
                return regs.get("$s0");
            case "10001":
                return regs.get("$s1");
            case "10010":
                return regs.get("$s2");
            case "10011":
                return regs.get("$s3");
            case "10100":
                return regs.get("$s4");
            case "10101":
                return regs.get("$s5");
            case "10110":
                return regs.get("$s6");
            case "10111":
                return regs.get("$s7");
            case "11000":
                return regs.get("$t8");
            case "11001":
                return regs.get("$t9");
            case "11100":
                return regs.get("$gp");
            case "11101":
                return regs.get("$sp");
            case "11110":
                return regs.get("$fp");
            case "11111":
                return regs.get("$ra");
            case "hi":
                return regs.get("hi");
            case "lo":
                return regs.get("lo");
            default:
                return null;
        }
    }

    public Register getRegisterByName(String name) {
        return regs.get(name);
    }

    public void resetRegisters() {
        for (String reg : regs.keySet()) {
            regs.get(reg).setValue(0);
        }

        regs.get("$sp").setValue(4000);
    }
}