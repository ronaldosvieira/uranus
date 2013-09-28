package simulador;

public class ULA {
	public Memoria m;
	public Constante c = new Constante();
	public ULA(Memoria m){
		this.m = m;
	}
	
	public void add(Registrador rs, Registrador rt, Registrador rd) throws Overflow{		
		rd.setValor(rs.getValor()+rt.getValor());
		if((rs.getValor()>=0)&&(rt.getValor()>=0)&&(rd.getValor()<0)) throw new Overflow();
		else if((rs.getValor()<0)&&(rt.getValor()<0)&&(rd.getValor()>=0)) throw new Overflow();
	}
	public void addu(Registrador rs, Registrador rt, Registrador rd){
		rd.setValor(rs.getValor()+rt.getValor());
	}
	public void sub(Registrador rs, Registrador rt, Registrador rd) throws Overflow{
		rd.setValor(rs.getValor()-rt.getValor());
		if((rs.getValor()>=0)&&(rt.getValor()<0)&&(rd.getValor()<0)) throw new Overflow();
		else if((rs.getValor()<0)&&(rt.getValor()>=0)&&(rd.getValor()>=0)) throw new Overflow();
	}
	public void subu(Registrador rs, Registrador rt, Registrador rd){
		rd.setValor(rs.getValor()-rt.getValor());
	}
	public void addi(Registrador rs, Registrador rt, int imediato) throws Overflow{
		rs.setValor(rt.getValor()+imediato);
		if((imediato>=0)&&(rt.getValor()>=0)&&(rs.getValor()<0)) throw new Overflow();
		else if((imediato<0)&&(rt.getValor()<0)&&(rs.getValor()>0)) throw new Overflow();
	}
	public void addiu(Registrador rs, Registrador rt, int imediato){
		rs.setValor(rt.getValor()+imediato);
	}
	public void or(Registrador rs, Registrador rt, Registrador rd){
		rd.setValor(rs.getValor()|rt.getValor());
	}
	public void and(Registrador rs, Registrador rt, Registrador rd){
		rd.setValor(rs.getValor()&rt.getValor());
	}
	public void nor(Registrador rs, Registrador rt, Registrador rd){
		rd.setValor(~(rs.getValor()|rt.getValor()));
	}
	public void ori(Registrador rs, Registrador rt, int imediato){
		rs.setValor(rt.getValor()|imediato);
	}
	public void andi(Registrador rs, Registrador rt, int imediato){
		rs.setValor(rt.getValor()&imediato);
	}
	public void mult(Registrador rs, Registrador rt, Registrador hi, Registrador lo){
		Long integ = (long) (rs.getValor()*rt.getValor());
		String i = c.toGreaterBinary(integ);
		hi.setValor(c.toInteger(i.substring(0,32)));
		lo.setValor(c.toInteger(i.substring(32)));
	}
	public void div(Registrador rs, Registrador rt, Registrador hi, Registrador lo){
		hi.setValor(rs.getValor()%rt.getValor());
		lo.setValor(rs.getValor()/rt.getValor());
	}
	public void mfhi(Registrador rd, Registrador hi){
		rd.setValor(c.toInteger(hi.valor));
	}
	public void mflo(Registrador rd, Registrador lo){
		rd.setValor(c.toInteger(lo.valor));
	}
	public void slt(Registrador rs, Registrador rt, Registrador rd){
		if(rs.getValor()<rt.getValor()) rd.setValor(1);
		else rd.setValor(0);
	}
	public void sltu(Registrador rs, Registrador rt, Registrador rd){
		if(rs.getUnsignedValue()<rt.getUnsignedValue()) rd.setValor(1);
		else rd.setValor(0);
	}
	public void slti(Registrador rs, Registrador rt, int valor){
		if(rt.getValor()<valor) rs.setValor(1);
		else rs.setValor(0);
	}
	public void sltiu(Registrador rs, Registrador rt, int valor){
		if(rt.getUnsignedValue()<c.toUnsignedInteger(Integer.toBinaryString(valor))) rs.setValor(1);
		else rs.setValor(0);
	}
	public void srl(Registrador rt, Registrador rd, int shamt){
		rd.setValor(rt.getValor()>>shamt);
	}
	public void sll(Registrador rt, Registrador rd, int shamt){
		rd.setValor(rt.getValor()<<shamt);
	}
	
	public void lb(Registrador rs, Registrador rt, int valor) throws ErroAcesso{
		if((valor%4!=0)||(rs.getValor()%4!=0)) throw new ErroAcesso();
		int pos = rs.getValor()+valor;
		rt.setValor(c.toInteger(m.getInstrucao(pos).substring(24)));
	}
	public void lbu(Registrador rs, Registrador rt, int valor) throws ErroAcesso{
		if((valor%4!=0)||(rs.getValor()%4!=0)) throw new ErroAcesso();
		int pos = rs.getValor()+valor;
		rt.setValor(Integer.parseInt(m.getInstrucao(pos).substring(24),2));
	}
	public void sb(Registrador rs, Registrador rt, int valor) throws ErroAcesso{
		if((valor%4!=0)||(rs.getValor()%4!=0)) throw new ErroAcesso();
		int pos = rs.getValor()+valor;
		int aux = Integer.parseInt(rt.valor.substring(24), 2);
		m.setValor(pos, aux);
	}	
	public void lh(Registrador rs, Registrador rt, int valor) throws ErroAcesso{
		if((valor%4!=0)||(rs.getValor()%4!=0)) throw new ErroAcesso();
		int pos = rs.getValor()+valor;
		rt.setValor(c.toInteger(m.getInstrucao(pos).substring(16)));
	}
	public void lhu(Registrador rs, Registrador rt, int valor) throws ErroAcesso{
		if((valor%4!=0)||(rs.getValor()%4!=0)) throw new ErroAcesso();
		int pos = rs.getValor()+valor;
		rt.setValor(Integer.parseInt(m.getInstrucao(pos).substring(16),2));
	}
	public void sh(Registrador rs, Registrador rt, int valor) throws ErroAcesso{
		if((valor%4!=0)||(rs.getValor()%4!=0)) throw new ErroAcesso();
		int pos = rs.getValor()+valor;
		int aux = Integer.parseInt(rt.valor.substring(16), 2);
		m.setValor(pos, aux);
	}
	public void lw(Registrador rs, Registrador rt, int valor) throws ErroAcesso{
		int pos;
		if((valor%4!=0)||(rs.getValor()%4!=0)) throw new ErroAcesso();
		pos = rs.getValor()+valor;
		rt.setValor(m.getValor(pos));
	}
	public void sw(Registrador rs, Registrador rt, int valor) throws ErroAcesso{
		if((valor%4!=0)||(rs.getValor()%4!=0)) throw new ErroAcesso();
		int pos = rs.getValor()+valor;
		m.setValor(pos, rt.getValor());
	}
	public void lui(Registrador rt, int imed){
		imed = imed<<16;
		rt.valor = c.toBinary(imed);
	}
	public boolean beq(Registrador rs, Registrador rt){
		if(rs.getValor()==rt.getValor()) return true;
		return false;
	}
	public boolean bne(Registrador rs, Registrador rt){
		if(rs.getValor()!=rt.getValor()) return true;
		return false;
	}
	public boolean blez(Registrador rs){
		if(rs.getValor()<=0) return true;
		return false;
	}
	public boolean bgtz(Registrador rs){
		if(rs.getValor()>0) return true;
		return false;
	}
}