package simulador;

import java.util.Map;
import java.util.TreeMap;

public class Processador {
	public Map<String, Registrador> regs = new TreeMap<String, Registrador>();
	public Map<String, OperacaoMIPS> ops = new TreeMap<String, OperacaoMIPS>();	
	
	public Processador(){
		regs.put("$zero", new Registrador("00000"));
		regs.put("$at", new Registrador("00001"));
		regs.put("$v0", new Registrador("00010"));
		regs.put("$v1", new Registrador("00011"));
		regs.put("$a0", new Registrador("00100"));
		regs.put("$a1", new Registrador("00101"));
		regs.put("$a2", new Registrador("00110"));
		regs.put("$a3", new Registrador("00111"));
		regs.put("$t0", new Registrador("01000"));
		regs.put("$t1", new Registrador("01001"));
		regs.put("$t2", new Registrador("01010"));
		regs.put("$t3", new Registrador("01011"));
		regs.put("$t4", new Registrador("01100"));
		regs.put("$t5", new Registrador("01101"));
		regs.put("$t6", new Registrador("01110"));
		regs.put("$t7", new Registrador("01111"));
		regs.put("$s0", new Registrador("10000"));
		regs.put("$s1", new Registrador("10001"));
		regs.put("$s2", new Registrador("10010"));
		regs.put("$s3", new Registrador("10011"));
		regs.put("$s4", new Registrador("10100"));
		regs.put("$s5", new Registrador("10101"));
		regs.put("$s6", new Registrador("10110"));
		regs.put("$s7", new Registrador("10111"));
		regs.put("$t8", new Registrador("11000"));
		regs.put("$t9", new Registrador("11001"));
		regs.put("$k0", new Registrador("11010"));
		regs.put("$k1", new Registrador("11011"));
		regs.put("$gp", new Registrador("11100"));
		regs.put("$sp", new Registrador("11101"));
		regs.put("$fp", new Registrador("11110"));
		regs.put("$ra", new Registrador("11111"));
		regs.put("hi", new Registrador(null));
		regs.put("lo", new Registrador(null));
		
		ops.put("add", new OperacaoMIPS("000000","100000"));
		ops.put("addu", new OperacaoMIPS("000000","100001"));
		ops.put("sub", new OperacaoMIPS("000000","100010"));
		ops.put("subu", new OperacaoMIPS("000000","100011"));
		ops.put("and", new OperacaoMIPS("000000","100100"));
		ops.put("or", new OperacaoMIPS("000000","100101"));
		ops.put("nor", new OperacaoMIPS("000000","100111"));
		ops.put("slt", new OperacaoMIPS("000000","101010"));
		ops.put("sltu", new OperacaoMIPS("000000","101011"));
		ops.put("mult", new OperacaoMIPS("000000","011000"));
		ops.put("div", new OperacaoMIPS("000000","011010"));
		ops.put("srl", new OperacaoMIPS("000000","000010"));
		ops.put("sll", new OperacaoMIPS("000000","000000"));
		ops.put("jr", new OperacaoMIPS("000000","001000"));
		ops.put("syscall", new OperacaoMIPS("000000","001100"));
		ops.put("mfhi", new OperacaoMIPS("000000","010000"));
		ops.put("mflo", new OperacaoMIPS("000000","010010"));
		ops.put("lw", new OperacaoMIPS("100011",null));
		ops.put("sw", new OperacaoMIPS("101011",null));
		ops.put("lb", new OperacaoMIPS("100000",null));
		ops.put("lbu", new OperacaoMIPS("100100",null));
		ops.put("sb", new OperacaoMIPS("101000",null));
		ops.put("lh", new OperacaoMIPS("100001",null));
		ops.put("lhu", new OperacaoMIPS("100101", null));
		ops.put("sh", new OperacaoMIPS("101001",null));
		ops.put("lui", new OperacaoMIPS("001111",null));
		ops.put("beq", new OperacaoMIPS("000100",null));
		ops.put("bne", new OperacaoMIPS("000101",null));
		ops.put("blez", new OperacaoMIPS("000110", null));
		ops.put("bgtz", new OperacaoMIPS("000111", null));
		ops.put("addi", new OperacaoMIPS("001000",null));
		ops.put("addiu", new OperacaoMIPS("001001",null));
		ops.put("slti", new OperacaoMIPS("001010",null));
		ops.put("sltiu", new OperacaoMIPS("001011",null));
		ops.put("andi", new OperacaoMIPS("001100", null));
		ops.put("ori", new OperacaoMIPS("001101", null));
		ops.put("j", new OperacaoMIPS("000010",null));
		ops.put("jal", new OperacaoMIPS("000011",null));
		
		regs.get("$zero").setValor(0);
		regs.get("$sp").setValor(4000);
	}
	
	public String getRegisterAdress(String str){
		return regs.get(str).endereco;
	}
	
	public String getOpcode(String str){
		return ops.get(str).opcode;
	}
	
	public String getFunction(String str){
		return ops.get(str).function;
	}
	
	public Map<String,Registrador> getRegister(){
		return regs;
	}
	
	public Registrador getRegister(String str){
		if(str.equals("00000")) return regs.get("$zero");
		else if(str.equals("00001")) return regs.get("$at");
		else if(str.equals("00010")) return regs.get("$v0");
		else if(str.equals("00011")) return regs.get("$v1");
		else if(str.equals("00100")) return regs.get("$a0");
		else if(str.equals("00101")) return regs.get("$a1");
		else if(str.equals("00110")) return regs.get("$a2");
		else if(str.equals("00111")) return regs.get("$a3");
		else if(str.equals("01000")) return regs.get("$t0");
		else if(str.equals("01001")) return regs.get("$t1");
		else if(str.equals("01010")) return regs.get("$t2");
		else if(str.equals("01011")) return regs.get("$t3");
		else if(str.equals("01100")) return regs.get("$t4");
		else if(str.equals("01101")) return regs.get("$t5");
		else if(str.equals("01110")) return regs.get("$t6");
		else if(str.equals("01111")) return regs.get("$t7");
		else if(str.equals("10000")) return regs.get("$s0");
		else if(str.equals("10001")) return regs.get("$s1");
		else if(str.equals("10010")) return regs.get("$s2");
		else if(str.equals("10011")) return regs.get("$s3");
		else if(str.equals("10100")) return regs.get("$s4");
		else if(str.equals("10101")) return regs.get("$s5");
		else if(str.equals("10110")) return regs.get("$s6");
		else if(str.equals("10111")) return regs.get("$s7");
		else if(str.equals("11000")) return regs.get("$t8");
		else if(str.equals("11001")) return regs.get("$t9");
		else if(str.equals("11100")) return regs.get("$gp");
		else if(str.equals("11101")) return regs.get("$sp");
		else if(str.equals("11110")) return regs.get("$fp");
		else if(str.equals("11111")) return regs.get("$ra");
		else if(str.equals("hi")) return regs.get("hi");
		else if(str.equals("lo")) return regs.get("lo");
		else return null;
	}
	
	public void resetRegisters(){
		for (String reg : regs.keySet()) {
			regs.get(reg).setValor(0);
		}
		regs.get("$sp").setValor(4000);
	}
}