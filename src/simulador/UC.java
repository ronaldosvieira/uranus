package simulador;

import java.util.ArrayList;

public class UC {	
	
	public ULA ula;
	public Memoria m;
	public Processador p;
	public Constante c = new Constante();
	public ArrayList<String> list = new ArrayList<String>();
	public int pc;
	public String linhaAtual;
	
	public UC(Memoria m, Processador p, ULA u, Assembler a){
		this.m = m;
		this.p = p;
		this.ula = u;
		this.list = a.instrucoes;
		this.pc = 0;
	}
	
	public boolean getInstruction() throws ErroAcesso, IntegerOutofRange, Overflow{
		for(int i=0; i<list.size(); i++){
			if(list.get(i).matches("\\s*\\D+:\\s*")) list.remove(i);
		}
		
		String function;
		Registrador rs;
		Registrador rt;
		Registrador rd;
		int shamt;
		int imediato;
		String instrucao;
		if(m.getInstrucao(pc)!=null){
			linhaAtual = list.get(pc/4);
			instrucao = m.getInstrucao(pc);
			if(instrucao.startsWith("000000")){
				function = instrucao.substring(26,32);
				if(function.equals("100000")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));					
					ula.add(rs, rt, rd);
				}
				else if(function.equals("100001")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));					
					ula.addu(rs, rt, rd);
				}
				else if(function.equals("100010")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));
					ula.sub(rs, rt, rd);
				}
				else if(function.equals("100011")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));
					ula.subu(rs, rt, rd);
				}
				else if(function.equals("100100")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));
					ula.and(rs, rt, rd);
				}
				else if(function.equals("100101")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));
					ula.or(rs, rt, rd);
				}
				else if(function.equals("100111")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));
					ula.nor(rs, rt, rd);
				}
				else if(function.equals("101010")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));
					ula.slt(rs, rt, rd);
				}
				else if(function.equals("101011")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));
					ula.sltu(rs, rt, rd);
				}
				else if(function.equals("011000")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					Registrador hi = p.getRegister("hi");
					Registrador lo = p.getRegister("lo");
					ula.mult(rs, rt, hi, lo);					
				}
				else if(function.equals("011010")){
					rs = p.getRegister(instrucao.substring(6, 11));
					rt = p.getRegister(instrucao.substring(11, 16));
					Registrador hi = p.getRegister("hi");
					Registrador lo = p.getRegister("lo");
					ula.div(rs, rt, hi, lo);		
				}
				else if(function.equals("010000")){
					rd = p.getRegister(instrucao.substring(16,21));
					Registrador hi = p.getRegister("hi");
					ula.mfhi(rd, hi);
				}
				else if(function.equals("010010")){
					rd = p.getRegister(instrucao.substring(16,21));
					Registrador lo = p.getRegister("lo");
					ula.mflo(rd, lo);
				}
				else if(function.equals("000010")){
					rt = p.getRegister(instrucao.substring(11, 16));
					rd = p.getRegister(instrucao.substring(16, 21));
					shamt = Integer.parseInt(instrucao.substring(21, 26),2);
					ula.srl(rt, rd, shamt);
				}
				else if(function.equals("000000")){
					rt = p.getRegister(instrucao.substring(11,16));
					rd = p.getRegister(instrucao.substring(16,21));
					shamt = Integer.parseInt(instrucao.substring(21,26),2);
					ula.sll(rt, rd, shamt);
				}
				else if(function.equals("001000")){
					pc = p.getRegister(instrucao.substring(6,11)).getValor();
				}
				else if(function.equals("001100")){
					rt = p.getRegister(instrucao.substring(11,16));
					rd = p.getRegister(instrucao.substring(16,21));					
					if(rt.getValor()==1) Uranus.ig.writeln(Integer.toString(rd.getValor()));//System.out.println(rd.getValor());
					else if(rt.getValor()==10) return false;
				}
			}
			else if(instrucao.startsWith("001000")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = c.toInteger(instrucao.substring(16));
				ula.addi(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001001")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = c.toInteger(instrucao.substring(16));
				ula.addiu(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001100")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = (int) c.toUnsignedInteger(instrucao.substring(16));
				ula.andi(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001101")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = (int) c.toUnsignedInteger(instrucao.substring(16));
				ula.ori(rs, rt, imediato);
			}
			else if(instrucao.startsWith("000100")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				if(ula.beq(rs, rt)) pc = imediato-4;
			}
			else if(instrucao.startsWith("000101")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				if(ula.bne(rs, rt)) pc = imediato-4;
			}
			else if(instrucao.startsWith("000110")){
				rs = p.getRegister(instrucao.substring(6, 11));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				if(ula.blez(rs)) pc = imediato-4;
			}
			else if(instrucao.startsWith("000111")){
				rs = p.getRegister(instrucao.substring(6, 11));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				if(ula.bgtz(rs)) pc = imediato-4;
			}
			else if(instrucao.startsWith("100011")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.lw(rs, rt, imediato);
			}
			else if(instrucao.startsWith("101011")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.sw(rs, rt, imediato);
			}
			else if(instrucao.startsWith("100001")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.lh(rs, rt, imediato);
			}
			else if(instrucao.startsWith("100101")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.lhu(rs, rt, imediato);
			}
			else if(instrucao.startsWith("101001")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.sh(rs, rt, imediato);
			}
			else if(instrucao.startsWith("100000")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.lb(rs, rt, imediato);
			}
			else if(instrucao.startsWith("100100")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.lbu(rs, rt, imediato);
			}
			else if(instrucao.startsWith("101000")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.sb(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001111")){
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.lui(rt, imediato);
			}
			else if(instrucao.startsWith("001010")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.slti(rs, rt, imediato);
			}
			else if(instrucao.startsWith("001011")){
				rs = p.getRegister(instrucao.substring(6, 11));
				rt = p.getRegister(instrucao.substring(11, 16));
				imediato = Integer.parseInt(instrucao.substring(16), 2);
				ula.sltiu(rs, rt, imediato);
			}
			else if(instrucao.startsWith("000010")){ //jump unconditional
				imediato = Integer.parseInt(instrucao.substring(6), 2);
				pc = imediato-4;
			}
			else if(instrucao.startsWith("000011")){ //jump and link
				imediato = Integer.parseInt(instrucao.substring(6), 2);
				p.getRegister("11111").setValor(pc);
				pc = imediato-4;
			}
			pc +=4;
		}
		return true;
	}
}