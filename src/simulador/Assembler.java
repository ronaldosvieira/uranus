package simulador;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

public class Assembler {
	public static Map<String,OperacaoMIPS> op;
	public static Map<String, Registrador> regs;
	public ArrayList<String> instrucoes = new ArrayList<String>();
	
	Memoria m;
	Constante c;
	Processador p;
	
	String opcode, rd, rs, rt, shamt, immed, funct, lab;
	String instrucaoBinario;
	
	String syscall = "syscall";
	String label = "\\s*\\D\\w+:\\s*";
	String r = "\\s*(addu|add|subu|sub|and|or|nor|sltu|slt)";
	String i = "\\s*(addiu|addi|andi|ori|sltiu|slti)";
	String j = "\\s*(j|jal)";
	String mult_div = "\\s*(mult|div)";
	String move_hi_lo = "\\s*(mfhi|mflo)";
	String bne_beq = "\\s*(bne|beq)";
	String blez_bgtz = "\\s*(blez|bgtz)";
	String bgt_blt = "\\s*(bgt|blt)";
	String load_store = "\\s*(lw|lb|lbu|sw|sb|lh|lhu|sh)";
	String lui = "\\s*(lui)";
	String li = "\\s*(li)";
	String move = "\\s*(move)";
	String shift = "\\s*(sll|srl)";
	String register = "(\\s+[$]((t\\d)|(s[0-7])|(a[0-3])|(v[0,1])|(at)|(sp)|(gp)|(fp)|(ra)))\\s*";
	String register2 = "(\\s+[$]((t\\d)|(s[0-7])|(a[0-3])|(v[0,1])||(at)|(sp)|(gp)|(fp)|(ra)|(zero)))\\s*";
	String register3 = "(\\s+\\d+[(][$]((t\\d)|(s[0-7])|(a[0-3])|(v[0,1])|(at)|(sp)|(gp)|(fp)|(ra))[)])\\s*";
	String imediato = "((\\s*\\d+\\s*)|(\\s*-\\d+\\s*))";
	String formatoR = r + register + "," + register2 + "," + register2;
	String formatoI = i + register + "," + register2 + "," + imediato;
	String formatoJ = j + "\\s+\\w+\\s*";
	String jump_register = "jr" + register;
	String formato_mult_div = mult_div + register2 + "," + register2;
	String formato_move_hi_lo = move_hi_lo + register;
	String formato_bne_beq = bne_beq + register + "," + register2 + "," + "\\s*\\D\\w+\\s*";
	String formato_blez_bgtz = blez_bgtz + register + "," + "\\s*\\D\\w+\\s*";
	String formato_bgt_blt = bgt_blt + register2 + "," + register2 + "," + "\\s*\\D\\w+\\s*";
	String formato_load_store = load_store + register2 + "," + register3;
	String formato_lui = lui + register + "," + imediato;
	String formato_shift = shift + register + "," + register2 + "," + imediato;
	String formato_li = li + register + "," + imediato;
	String formato_move = move + register + "," + register2;
	
	//Este regex é especial para identificar labels que ocorrem na mesma linha que uma instrução
	String label_com_instrucao = label + "\\s*\\D\\w+\\s*";	
	
	public Assembler(Memoria m, Processador p, Constante c){
		this.m = m;
		this.p = p;
		this.c = c;
	}
	
	//Identificação das pseudoinstruções e cria uma nova arraylist com as instruções correspondentes
	public void setNewList(ArrayList<String> list) throws IntegerOutofRange{
		int i  =0;
		while(i<list.size()){
			if(list.get(i).matches(formato_move)){
				StringTokenizer st = new StringTokenizer(list.get(i), " |,|(|)|:");
				ArrayList<String> array = new ArrayList<String>();
				while(st.hasMoreTokens()){
					array.add(st.nextToken());
				}
				String str = "addu " + array.get(1) + ", $zero, " + array.get(2);
				instrucoes.add(str);
			}
			else if(list.get(i).matches(formato_li)){
				StringTokenizer st = new StringTokenizer(list.get(i), " |,|(|)|:");
				ArrayList<String> array = new ArrayList<String>();
				while(st.hasMoreTokens()){
					array.add(st.nextToken());
				}
				String str = "addiu " + array.get(1) + ", $zero, " + array.get(2);
				instrucoes.add(str);
			}
			
			else if(list.get(i).matches(formatoI)){
				StringTokenizer st = new StringTokenizer(list.get(i), " |,|(|)|:");
				ArrayList<String> array = new ArrayList<String>();
				while(st.hasMoreTokens()){
					array.add(st.nextToken());
				}
				int imediato = Integer.parseInt(array.get(3));
				
				//Quando o valor é maior do que 2e16-1 ou menor do que -2e16, é necessário executar
				//as instruções lui com os 16 bits mais significativos do inteiro, seguida de ori,
				//com os 16 mais a direita e por fim, a instrução add
				if(array.get(0).equals("addi")){
					if(imediato<-32768||imediato>32767){
						String aux = c.toBinary(imediato);
						String str01 = "lui $at, " + c.toInteger(aux.substring(0,16));
						String str02 = "ori $at, $at, " + c.toUnsignedInteger(aux.substring(16,32));
						String str03 = "add " + array.get(1) + ", " + array.get(2) + ", $at";
						instrucoes.add(str01);
						instrucoes.add(str02);
						instrucoes.add(str03);
					}
					else instrucoes.add(list.get(i));
				}
				else if(array.get(0).equals("addiu")){
					if(imediato<-32768||imediato>32767){
						String aux = c.toBinary(imediato);
						String str01 = "lui $at, " + c.toInteger(aux.substring(0,16));
						String str02 = "ori $at, $at, " + c.toUnsignedInteger(aux.substring(16,32));
						String str03 = "addu " + array.get(1) + ", " + array.get(2) + ", $at";
						instrucoes.add(str01);
						instrucoes.add(str02);
						instrucoes.add(str03);
					}
					else instrucoes.add(list.get(i));
				}
				//As instruções andi e ori também necessitam de um tratamento especial
				//no caso de o imediato ser negativo ou maior do que 2e16-1
				else if(array.get(0).equals("andi")){
					if(imediato<0||imediato>65535){
						String aux = c.toBinary(imediato);
						String str01 = "lui $at, " + c.toInteger(aux.substring(0,16));
						String str02 = "ori $at, $at, " + c.toUnsignedInteger(aux.substring(16,32));
						String str03 = "and " + array.get(1) + ", " + array.get(2) + ", $at";
						instrucoes.add(str01);
						instrucoes.add(str02);
						instrucoes.add(str03);
					}
					else instrucoes.add(list.get(i));
				}
				else if(array.get(0).equals("ori")){
					if(imediato<0||imediato>65535){
						String aux = c.toBinary(imediato);
						String str01 = "lui $at, " + c.toInteger(aux.substring(0,16));
						String str02 = "ori $at, $at, " + c.toUnsignedInteger(aux.substring(16,32));
						String str03 = "or " + array.get(1) + ", " + array.get(2) + ", $at";
						instrucoes.add(str01);
						instrucoes.add(str02);
						instrucoes.add(str03);
					}
					else instrucoes.add(list.get(i));
				}
				else instrucoes.add(list.get(i));
			}
			else if(list.get(i).matches(formato_bgt_blt)){
				StringTokenizer st = new StringTokenizer(list.get(i), " |,|(|)|:");
				ArrayList<String> array = new ArrayList<String>();
				while(st.hasMoreTokens()){
					array.add(st.nextToken());
				}
				
				if(array.get(0).equals("bgt")){
					String str01 = "slt $at, " + array.get(2) + ", " + array.get(1);
					String str02 = "bne $at, $zero, " + array.get(3);
					instrucoes.add(str01);
					instrucoes.add(str02);
				}
				
				else{
					String str01 = "slt $at, " + array.get(1) + ", " + array.get(2);
					String str02 = "bne $at, $zero, " + array.get(3);
					instrucoes.add(str01);
					instrucoes.add(str02);
				}
			}
			
			else if(list.get(i).startsWith("#")==false){
				instrucoes.add(list.get(i));
			}
			i++;
		}
	}
	
	public void retirarLabels(){
		for(int i=0; i<instrucoes.size(); i++){
			if(instrucoes.get(i).matches("\\s*\\D+:\\s*")) instrucoes.remove(i);
		}
	}
	
	public boolean setInstrucao() throws IntegerOutofRange{
		ArrayList<String> tokens = new ArrayList<String>();
		int cont = 0;
		int i = 0;
		while(i<instrucoes.size()){
			String str = instrucoes.get(i);
			StringTokenizer st = new StringTokenizer(str, " |,|(|)|:");
			while(st.hasMoreTokens()){
				tokens.add(st.nextToken());
			}
			if(str.matches(formatoR)==true) {
				shamt = "00000";
				opcode = p.getOpcode(tokens.get(0));
				rd = p.getRegisterAdress(tokens.get(1));
				rt = p.getRegisterAdress(tokens.get(3));
				rs = p.getRegisterAdress(tokens.get(2));
				funct = p.getFunction(tokens.get(0));
				instrucaoBinario = opcode+rs+rt+rd+shamt+funct;
				m.setInstrucao(instrucaoBinario, cont);
			}
			else if(str.matches(formatoI)==true) {
				immed = tokens.get(3);
				opcode = p.getOpcode(tokens.get(0));
				rs = p.getRegisterAdress(tokens.get(1));
				rt = p.getRegisterAdress(tokens.get(2));
				int imediato = Integer.parseInt(immed);
				if(tokens.get(0).equals("slti")||tokens.get(0).equals("sltiu")){
					if(imediato<-32768||imediato>32767) {
						throw new IntegerOutofRange();
					}
				}
				instrucaoBinario = opcode+rs+rt+c.toBinary(imediato).substring(16);
				m.setInstrucao(instrucaoBinario,cont);
			}
			else if(str.matches(formatoJ)==true){
				opcode = tokens.get(0);
				lab = tokens.get(1);
				int endereco = (instrucoes.indexOf(lab+":")*4);
				immed = c.toBinary(endereco).substring(6);
				
				opcode = p.getOpcode(opcode);
				instrucaoBinario = opcode+immed;
				m.setInstrucao(instrucaoBinario, cont);
			}
			else if(str.matches(formato_mult_div)){
				rd = "00000";
				shamt = "00000";
				opcode = p.getOpcode(tokens.get(0));
				rs = p.getRegisterAdress(tokens.get(1));
				rt = p.getRegisterAdress(tokens.get(2));
				funct = p.getFunction(tokens.get(0));
				
				instrucaoBinario = opcode+rs+rt+rd+shamt+funct;
				m.setInstrucao(instrucaoBinario, cont);
			}
			else if(str.matches(formato_move_hi_lo)){
				opcode = p.getOpcode(tokens.get(0));
				rd = p.getRegisterAdress(tokens.get(1));
				funct = p.getFunction(tokens.get(0));
				rs = "00000";
				rt = "00000";
				shamt = "00000";
				instrucaoBinario = opcode+rs+rt+rd+shamt+funct;
				m.setInstrucao(instrucaoBinario, cont);
			}
			else if(str.matches(jump_register)==true){
				rt = "00000";
				rd = "00000";
				shamt = "00000";
				opcode = p.getOpcode(tokens.get(0));
				funct = p.getFunction(tokens.get(0));
				rs = p.getRegisterAdress(tokens.get(1));
				instrucaoBinario = opcode+rs+rt+rd+shamt+funct;
				m.setInstrucao(instrucaoBinario,cont);
				p.getRegister(rs).setValor(cont);
			}
			else if(str.matches(formato_bne_beq)==true) {
				lab = tokens.get(3);
				int endereco = (instrucoes.indexOf(lab+":")*4)-4;
				immed = c.toBinary(endereco).substring(16);
				
				opcode = p.getOpcode(tokens.get(0));
				rs = p.getRegisterAdress(tokens.get(1));
				rt = p.getRegisterAdress(tokens.get(2));
				instrucaoBinario = opcode+rs+rt+immed;
				m.setInstrucao(instrucaoBinario, cont);
			}
			else if(str.matches(formato_blez_bgtz)==true){
				lab = tokens.get(2);
				int endereco = (instrucoes.indexOf(lab+":")*4)-4;
				immed = c.toBinary(endereco).substring(16);
				opcode = p.getOpcode(tokens.get(0));
				rs = p.getRegisterAdress(tokens.get(1));
				instrucaoBinario = opcode+rs+"00000"+immed;
				m.setInstrucao(instrucaoBinario, cont);
			}			
			
			else if(str.matches(formato_load_store)==true) {
				immed = tokens.get(2);
				int endereco = Integer.parseInt(immed);
				immed = c.toBinary(endereco).substring(16);
				
				opcode = p.getOpcode(tokens.get(0));
				rs = p.getRegisterAdress(tokens.get(3));
				rt = p.getRegisterAdress(tokens.get(1));
				instrucaoBinario = opcode+rs+rt+immed;
				m.setInstrucao(instrucaoBinario, cont);
			}
			else if(str.matches(formato_lui)==true){
				rs = "00000";
				immed = tokens.get(2);
				int inteiro = Integer.parseInt(immed);
				immed = c.toBinary(inteiro).substring(16);
				opcode = p.getOpcode(tokens.get(0));
				rt = p.getRegisterAdress(tokens.get(1));
				instrucaoBinario = opcode+rs+rt+immed;
				m.setInstrucao(instrucaoBinario, cont);
			}
			else if(str.matches(formato_shift)==true) {
				rs = "00000";
				shamt = tokens.get(3);
				int shift = Integer.parseInt(shamt);
				if(shift<0||shift>31){
					throw new IntegerOutofRange();
				}
				shamt = c.toBinary(shift).substring(26);
				opcode = p.getOpcode(tokens.get(0));
				rd = p.getRegisterAdress(tokens.get(1));
				rt = p.getRegisterAdress(tokens.get(2));				
				funct = p.getFunction(tokens.get(0));
				instrucaoBinario = opcode+rs+rt+rd+shamt+funct;
				m.setInstrucao(instrucaoBinario,cont);
			}
			
			else if(str.matches(label)==true){
				cont -=4;
			}
			
			else if(str.matches(syscall)==true){
				instrucaoBinario = "00000000000000100010000000001100";
				m.setInstrucao(instrucaoBinario, cont);
			}
			
			else {
				Uranus.ig.writeError("Erro na linha "+(i+1)+": "+str);
				return false;
			}
			tokens.clear();
			i++;
			cont +=4;
		}
		return true;
	}
}