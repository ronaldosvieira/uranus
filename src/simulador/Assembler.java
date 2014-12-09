package simulador;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Assembler {
    //public static Map<String, OperacaoMIPS> op;
    //public static Map<String, Register> regs;
    public ArrayList<String> instrucoes = new ArrayList<>();

    private Memory m;
    private Processador p;

    private String label = "\\s*\\D\\w+:\\s*";
    private String r = "\\s*(addu|add|subu|sub|and|or|nor|sltu|slt)";
    private String i = "\\s*(addiu|addi|andi|ori|sltiu|slti)";
    private String j = "\\s*(j|jal)";
    private String mult_div = "\\s*(mult|div)";
    private String move_hi_lo = "\\s*(mfhi|mflo)";
    private String bne_beq = "\\s*(bne|beq)";
    private String blez_bgtz = "\\s*(blez|bgtz)";
    private String bgt_blt = "\\s*(bgt|blt)";
    private String load_store = "\\s*(lw|lb|lbu|sw|sb|lh|lhu|sh)";
    private String lui = "\\s*(lui)";
    private String li = "\\s*(li)";
    private String move = "\\s*(move)";
    private String shift = "\\s*(sll|srl)";
    private String register = "(\\s+[$]((t\\d)|(s[0-7])|(a[0-3])|(v[0,1])|(at)|(sp)|(gp)|(fp)|(ra)))\\s*";
    private String register2 = "(\\s+[$]((t\\d)|(s[0-7])|(a[0-3])|(v[0,1])||(at)|(sp)|(gp)|(fp)|(ra)|(zero)))\\s*";
    private String register3 = "(\\s+\\d+[(][$]((t\\d)|(s[0-7])|(a[0-3])|(v[0,1])|(at)|(sp)|(gp)|(fp)|(ra))[)])\\s*";
    private String imediato = "((\\s*\\d+\\s*)|(\\s*-\\d+\\s*))";
    private String formatoR = r + register + "," + register2 + "," + register2;
    private String formatoI = i + register + "," + register2 + "," + imediato;
    private String formatoJ = j + "\\s+\\w+\\s*";
    private String jump_register = "jr" + register;
    private String formato_mult_div = mult_div + register2 + "," + register2;
    private String formato_move_hi_lo = move_hi_lo + register;
    private String formato_bne_beq = bne_beq + register + "," + register2 + "," + "\\s*\\D\\w+\\s*";
    private String formato_blez_bgtz = blez_bgtz + register + "," + "\\s*\\D\\w+\\s*";
    private String formato_bgt_blt = bgt_blt + register2 + "," + register2 + "," + "\\s*\\D\\w+\\s*";
    private String formato_load_store = load_store + register2 + "," + register3;
    private String formato_lui = lui + register + "," + imediato;
    private String formato_shift = shift + register + "," + register2 + "," + imediato;
    private String formato_li = li + register + "," + imediato;
    private String formato_move = move + register + "," + register2;

    //Este regex � especial para identificar labels que ocorrem na mesma linha que uma instru��o
    String label_com_instrucao = label + "\\s*\\D\\w+\\s*";

    public Assembler(Memory m, Processador p) {
        this.m = m;
        this.p = p;
    }

    //Identifica��o das pseudoinstru��es e cria uma nova arraylist com as instru��es correspondentes
    public void setNewList(ArrayList<String> list) {
        int i = 0;
        while (i < list.size()) {
            if (list.get(i).matches(formato_move)) {
                StringTokenizer st = new StringTokenizer(list.get(i), " |,|(|)|:");
                ArrayList<String> array = new ArrayList<>();
                while (st.hasMoreTokens()) {
                    array.add(st.nextToken());
                }
                String str = "addu " + array.get(1) + ", $zero, " + array.get(2);
                instrucoes.add(str);
            } else if (list.get(i).matches(formato_li)) {
                StringTokenizer st = new StringTokenizer(list.get(i), " |,|(|)|:");
                ArrayList<String> array = new ArrayList<>();
                while (st.hasMoreTokens()) {
                    array.add(st.nextToken());
                }
                String str = "addiu " + array.get(1) + ", $zero, " + array.get(2);
                instrucoes.add(str);
            } else if (list.get(i).matches(formatoI)) {
                StringTokenizer st = new StringTokenizer(list.get(i), " |,|(|)|:");
                ArrayList<String> array = new ArrayList<>();
                while (st.hasMoreTokens()) {
                    array.add(st.nextToken());
                }
                int imediato = Integer.parseInt(array.get(3));

                //Quando o value � maior do que 2e16-1 ou menor do que -2e16, � necess�rio executar
                //as instru��es lui com os 16 bits mais significativos do inteiro, seguida de ori,
                //com os 16 mais a direita e por fim, a instru��o add
                if (array.get(0).equals("addi")) {
                    if (imediato < -32768 || imediato > 32767) {
                        String aux = Constant.toBinary(imediato);
                        String str01 = "lui $at, " + Constant.toInteger(aux.substring(0, 16));
                        String str02 = "ori $at, $at, " + Constant.toUnsignedInteger(aux.substring(16, 32));
                        String str03 = "add " + array.get(1) + ", " + array.get(2) + ", $at";
                        instrucoes.add(str01);
                        instrucoes.add(str02);
                        instrucoes.add(str03);
                    } else instrucoes.add(list.get(i));
                } else if (array.get(0).equals("addiu")) {
                    if (imediato < -32768 || imediato > 32767) {
                        String aux = Constant.toBinary(imediato);
                        String str01 = "lui $at, " + Constant.toInteger(aux.substring(0, 16));
                        String str02 = "ori $at, $at, " + Constant.toUnsignedInteger(aux.substring(16, 32));
                        String str03 = "addu " + array.get(1) + ", " + array.get(2) + ", $at";
                        instrucoes.add(str01);
                        instrucoes.add(str02);
                        instrucoes.add(str03);
                    } else instrucoes.add(list.get(i));
                }
                //As instru��es andi e ori tamb�m necessitam de um tratamento especial
                //no caso de o imediato ser negativo ou maior do que 2e16-1
                else if (array.get(0).equals("andi")) {
                    if (imediato < 0 || imediato > 65535) {
                        String aux = Constant.toBinary(imediato);
                        String str01 = "lui $at, " + Constant.toInteger(aux.substring(0, 16));
                        String str02 = "ori $at, $at, " + Constant.toUnsignedInteger(aux.substring(16, 32));
                        String str03 = "and " + array.get(1) + ", " + array.get(2) + ", $at";
                        instrucoes.add(str01);
                        instrucoes.add(str02);
                        instrucoes.add(str03);
                    } else instrucoes.add(list.get(i));
                } else if (array.get(0).equals("ori")) {
                    if (imediato < 0 || imediato > 65535) {
                        String aux = Constant.toBinary(imediato);
                        String str01 = "lui $at, " + Constant.toInteger(aux.substring(0, 16));
                        String str02 = "ori $at, $at, " + Constant.toUnsignedInteger(aux.substring(16, 32));
                        String str03 = "or " + array.get(1) + ", " + array.get(2) + ", $at";
                        instrucoes.add(str01);
                        instrucoes.add(str02);
                        instrucoes.add(str03);
                    } else instrucoes.add(list.get(i));
                } else instrucoes.add(list.get(i));
            } else if (list.get(i).matches(formato_bgt_blt)) {
                StringTokenizer st = new StringTokenizer(list.get(i), " |,|(|)|:");
                ArrayList<String> array = new ArrayList<>();
                while (st.hasMoreTokens()) {
                    array.add(st.nextToken());
                }

                if (array.get(0).equals("bgt")) {
                    String str01 = "slt $at, " + array.get(2) + ", " + array.get(1);
                    String str02 = "bne $at, $zero, " + array.get(3);
                    instrucoes.add(str01);
                    instrucoes.add(str02);
                } else {
                    String str01 = "slt $at, " + array.get(1) + ", " + array.get(2);
                    String str02 = "bne $at, $zero, " + array.get(3);
                    instrucoes.add(str01);
                    instrucoes.add(str02);
                }
            } else if (!list.get(i).startsWith("#")) {
                instrucoes.add(list.get(i));
            }
            i++;
        }
    }

    public void retirarLabels() {
        for (int i = 0; i < instrucoes.size(); i++) {
            if (instrucoes.get(i).matches("\\s*\\D+:\\s*")) instrucoes.remove(i);
        }
    }

    public boolean setInstrucao() throws IntegerOutofRangeException {
        ArrayList<String> tokens = new ArrayList<>();
        int cont = 0;
        int i = 0;
        while (i < instrucoes.size()) {
            String str = instrucoes.get(i);
            StringTokenizer st = new StringTokenizer(str, " |,|(|)|:");

            while (st.hasMoreTokens()) tokens.add(st.nextToken());

            String opcode;
            String rd;
            String rs;
            String rt;
            String shamt;
            String immed;
            String funct;
            String lab;
            String instrucaoBinario;
            String syscall = "syscall";

            if (str.matches(formatoR)) {
                shamt = "00000";
                opcode = p.getOpcode(tokens.get(0));
                rd = p.getRegisterAdress(tokens.get(1));
                rt = p.getRegisterAdress(tokens.get(3));
                rs = p.getRegisterAdress(tokens.get(2));
                funct = p.getFunction(tokens.get(0));
                instrucaoBinario = opcode + rs + rt + rd + shamt + funct;
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(formatoI)) {
                immed = tokens.get(3);
                opcode = p.getOpcode(tokens.get(0));
                rs = p.getRegisterAdress(tokens.get(1));
                rt = p.getRegisterAdress(tokens.get(2));
                int imediato = Integer.parseInt(immed);
                if (tokens.get(0).equals("slti") || tokens.get(0).equals("sltiu")) {
                    if (imediato < -32768 || imediato > 32767) {
                        throw new IntegerOutofRangeException();
                    }
                }
                instrucaoBinario = opcode + rs + rt + Constant.toBinary(imediato).substring(16);
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(formatoJ)) {
                opcode = tokens.get(0);
                lab = tokens.get(1);
                int endereco = (instrucoes.indexOf(lab + ":") * 4);
                immed = Constant.toBinary(endereco).substring(6);

                opcode = p.getOpcode(opcode);
                instrucaoBinario = opcode + immed;
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(formato_mult_div)) {
                rd = "00000";
                shamt = "00000";
                opcode = p.getOpcode(tokens.get(0));
                rs = p.getRegisterAdress(tokens.get(1));
                rt = p.getRegisterAdress(tokens.get(2));
                funct = p.getFunction(tokens.get(0));

                instrucaoBinario = opcode + rs + rt + rd + shamt + funct;
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(formato_move_hi_lo)) {
                opcode = p.getOpcode(tokens.get(0));
                rd = p.getRegisterAdress(tokens.get(1));
                funct = p.getFunction(tokens.get(0));
                rs = "00000";
                rt = "00000";
                shamt = "00000";
                instrucaoBinario = opcode + rs + rt + rd + shamt + funct;
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(jump_register)) {
                rt = "00000";
                rd = "00000";
                shamt = "00000";
                opcode = p.getOpcode(tokens.get(0));
                funct = p.getFunction(tokens.get(0));
                rs = p.getRegisterAdress(tokens.get(1));
                instrucaoBinario = opcode + rs + rt + rd + shamt + funct;
                m.setWord(instrucaoBinario, cont);
                p.getRegister(rs).setValue(cont);
            } else if (str.matches(formato_bne_beq)) {
                lab = tokens.get(3);
                int endereco = (instrucoes.indexOf(lab + ":") * 4) - 4;
                immed = Constant.toBinary(endereco).substring(16);

                opcode = p.getOpcode(tokens.get(0));
                rs = p.getRegisterAdress(tokens.get(1));
                rt = p.getRegisterAdress(tokens.get(2));
                instrucaoBinario = opcode + rs + rt + immed;
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(formato_blez_bgtz)) {
                lab = tokens.get(2);
                int endereco = (instrucoes.indexOf(lab + ":") * 4) - 4;
                immed = Constant.toBinary(endereco).substring(16);
                opcode = p.getOpcode(tokens.get(0));
                rs = p.getRegisterAdress(tokens.get(1));
                instrucaoBinario = opcode + rs + "00000" + immed;
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(formato_load_store)) {
                immed = tokens.get(2);
                int endereco = Integer.parseInt(immed);
                immed = Constant.toBinary(endereco).substring(16);

                opcode = p.getOpcode(tokens.get(0));
                rs = p.getRegisterAdress(tokens.get(3));
                rt = p.getRegisterAdress(tokens.get(1));
                instrucaoBinario = opcode + rs + rt + immed;
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(formato_lui)) {
                rs = "00000";
                immed = tokens.get(2);
                int inteiro = Integer.parseInt(immed);
                immed = Constant.toBinary(inteiro).substring(16);
                opcode = p.getOpcode(tokens.get(0));
                rt = p.getRegisterAdress(tokens.get(1));
                instrucaoBinario = opcode + rs + rt + immed;
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(formato_shift)) {
                rs = "00000";
                shamt = tokens.get(3);
                int shift = Integer.parseInt(shamt);
                if (shift < 0 || shift > 31) {
                    throw new IntegerOutofRangeException();
                }
                shamt = Constant.toBinary(shift).substring(26);
                opcode = p.getOpcode(tokens.get(0));
                rd = p.getRegisterAdress(tokens.get(1));
                rt = p.getRegisterAdress(tokens.get(2));
                funct = p.getFunction(tokens.get(0));
                instrucaoBinario = opcode + rs + rt + rd + shamt + funct;
                m.setWord(instrucaoBinario, cont);
            } else if (str.matches(label)) {
                cont -= 4;
            } else if (str.matches(syscall)) {
                instrucaoBinario = "00000000000000100010000000001100";
                m.setWord(instrucaoBinario, cont);
            } else {
                Uranus.ui.writeError("Erro na linha " + (i + 1) + ": " + str);
                return false;
            }
            tokens.clear();
            i++;
            cont += 4;
        }
        return true;
    }
}