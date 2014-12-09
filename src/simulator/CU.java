package simulator;

import simulator.exceptions.AccessErrorException;
import simulator.exceptions.IntegerOutofRangeException;
import simulator.exceptions.OverflowException;

import java.util.ArrayList;

public class CU {
	
	public ALU ALU;
	public Memory m;
	public Processor p;
	public ArrayList<String> list;
	public int pc;
	public String currentLine;
	
	public CU(Processor p, ALU u, Parser a){
		this.m = Memory.getInstance();
		this.p = p;
		this.ALU = u;
		this.list = a.instrucoes;
		this.pc = 0;

		this.list = new ArrayList<>();
	}
	
	public boolean getInstruction() throws AccessErrorException, IntegerOutofRangeException, OverflowException {
		for(int i=0; i<list.size(); i++){
			if(list.get(i).matches("\\s*\\D+:\\s*")) list.remove(i);
		}
		
		String function;
		Register rs;
		Register rt;
		Register rd;
		int shamt;
		int imediato;
		String instrucao;
		if(m.getWord(pc)!=null){
			currentLine = list.get(pc/4);
			instrucao = m.getWord(pc);
			if(instrucao.startsWith("000000")){
				function = instrucao.substring(26,32);
				switch (function) {
					case "100000":
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						ALU.add(rs, rt, rd);
						break;
					case "100001":
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						ALU.addu(rs, rt, rd);
						break;
					case "100010":
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						ALU.sub(rs, rt, rd);
						break;
					case "100011":
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						ALU.subu(rs, rt, rd);
						break;
					case "100100":
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						ALU.and(rs, rt, rd);
						break;
					case "100101":
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						ALU.or(rs, rt, rd);
						break;
					case "100111":
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						ALU.nor(rs, rt, rd);
						break;
					case "101010":
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						ALU.slt(rs, rt, rd);
						break;
					case "101011":
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						ALU.sltu(rs, rt, rd);
						break;
					case "011000": {
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						Register hi = p.getRegister("hi");
						Register lo = p.getRegister("lo");
						ALU.mult(rs, rt, hi, lo);
						break;
					}
					case "011010": {
						rs = p.getRegister(instrucao.substring(6, 11));
						rt = p.getRegister(instrucao.substring(11, 16));
						Register hi = p.getRegister("hi");
						Register lo = p.getRegister("lo");
						ALU.div(rs, rt, hi, lo);
						break;
					}
					case "010000": {
						rd = p.getRegister(instrucao.substring(16, 21));
						Register hi = p.getRegister("hi");
						ALU.mfhi(rd, hi);
						break;
					}
					case "010010": {
						rd = p.getRegister(instrucao.substring(16, 21));
						Register lo = p.getRegister("lo");
						ALU.mflo(rd, lo);
						break;
					}
					case "000010":
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						shamt = Integer.parseInt(instrucao.substring(21, 26), 2);
						ALU.srl(rt, rd, shamt);
						break;
					case "000000":
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						shamt = Integer.parseInt(instrucao.substring(21, 26), 2);
						ALU.sll(rt, rd, shamt);
						break;
					case "001000":
						pc = p.getRegister(instrucao.substring(6, 11)).getValue();
						break;
					case "001100":
						rt = p.getRegister(instrucao.substring(11, 16));
						rd = p.getRegister(instrucao.substring(16, 21));
						if (rt.getValue() == 1)
							Uranus.ui.writeln(Integer.toString(rd.getValue()));//System.out.println(rd.getValue());
						else if (rt.getValue() == 10) return false;
						break;
				}
			}
			else if(instrucao.startsWith("001000")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Constant.toInteger(instrucao.substring(16));
				ALU.addi(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001001")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Constant.toInteger(instrucao.substring(16));
				ALU.addiu(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001100")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = (int) Constant.toUnsignedInteger(instrucao.substring(16));
				ALU.andi(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001101")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = (int) Constant.toUnsignedInteger(instrucao.substring(16));
				ALU.ori(rs, rt, imediato);
			}
			else if(instrucao.startsWith("000100")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				if(ALU.beq(rs, rt)) pc = imediato-4;
			}
			else if(instrucao.startsWith("000101")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				if(ALU.bne(rs, rt)) pc = imediato-4;
			}
			else if(instrucao.startsWith("000110")){
				rs = p.getRegister(instrucao.substring(6, 11));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				if(ALU.blez(rs)) pc = imediato-4;
			}
			else if(instrucao.startsWith("000111")){
				rs = p.getRegister(instrucao.substring(6, 11));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				if(ALU.bgtz(rs)) pc = imediato-4;
			}
			else if(instrucao.startsWith("100011")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.lw(rs, rt, imediato);
			}
			else if(instrucao.startsWith("101011")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.sw(rs, rt, imediato);
			}
			else if(instrucao.startsWith("100001")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.lh(rs, rt, imediato);
			}
			else if(instrucao.startsWith("100101")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.lhu(rs, rt, imediato);
			}
			else if(instrucao.startsWith("101001")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.sh(rs, rt, imediato);
			}
			else if(instrucao.startsWith("100000")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.lb(rs, rt, imediato);
			}
			else if(instrucao.startsWith("100100")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.lbu(rs, rt, imediato);
			}
			else if(instrucao.startsWith("101000")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.sb(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001111")){
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.lui(rt, imediato);
			}
			else if(instrucao.startsWith("001010")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.slti(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001011")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ALU.sltiu(rs, rt, imediato);
			}
			else if(instrucao.startsWith("000010")){ //jump unconditional
				imediato = Integer.parseInt(instrucao.substring(6), 2);
				pc = imediato-4;
			}
			else if(instrucao.startsWith("000011")){ //jump and link
				imediato = Integer.parseInt(instrucao.substring(6), 2);
				p.getRegister("11111").setValue(pc);
				pc = imediato-4;
			}
			pc +=4;
		}
		return true;
	}
}