package simulator;

import simulator.exceptions.AccessErrorException;
import simulator.exceptions.OverflowException;

public class ALU {
    public Memory mem;

    public ALU() {
        this.mem = Memory.getInstance();
    }

    public void add(Register rs, Register rt, Register rd) throws OverflowException {
        rd.setValue(rs.getValue() + rt.getValue());

        if ((rs.getValue() >= 0) && (rt.getValue() >= 0) && (rd.getValue() < 0))
            throw new OverflowException();
        else if ((rs.getValue() < 0) && (rt.getValue() < 0) && (rd.getValue() >= 0))
            throw new OverflowException();
    }

    public void addu(Register rs, Register rt, Register rd) {
        rd.setValue(rs.getValue() + rt.getValue());
    }

    public void sub(Register rs, Register rt, Register rd) throws OverflowException {
        rd.setValue(rs.getValue() - rt.getValue());

        if ((rs.getValue() >= 0) && (rt.getValue() < 0) && (rd.getValue() < 0))
            throw new OverflowException();
        else if ((rs.getValue() < 0) && (rt.getValue() >= 0) && (rd.getValue() >= 0))
            throw new OverflowException();
    }

    public void subu(Register rs, Register rt, Register rd) {
        rd.setValue(rs.getValue() - rt.getValue());
    }

    public void addi(Register rs, Register rt, int immediate) throws OverflowException {
        rs.setValue(rt.getValue() + immediate);

        if ((immediate >= 0) && (rt.getValue() >= 0) && (rs.getValue() < 0))
            throw new OverflowException();
        else if ((immediate < 0) && (rt.getValue() < 0) && (rs.getValue() > 0))
            throw new OverflowException();
    }

    public void addiu(Register rs, Register rt, int immediate) {
        rs.setValue(rt.getValue() + immediate);
    }

    public void or(Register rs, Register rt, Register rd) {
        rd.setValue(rs.getValue() | rt.getValue());
    }

    public void and(Register rs, Register rt, Register rd) {
        rd.setValue(rs.getValue() & rt.getValue());
    }

    public void nor(Register rs, Register rt, Register rd) {
        rd.setValue(~(rs.getValue() | rt.getValue()));
    }

    public void ori(Register rs, Register rt, int immediate) {
        rs.setValue(rt.getValue() | immediate);
    }

    public void andi(Register rs, Register rt, int immediate) {
        rs.setValue(rt.getValue() & immediate);
    }

    public void mult(Register rs, Register rt, Register hi, Register lo) {
        Long integ = (long) (rs.getValue() * rt.getValue());
        String i = Constant.toGreaterBinary(integ);
        hi.setValue(Constant.toInteger(i.substring(0, 32)));
        lo.setValue(Constant.toInteger(i.substring(32)));
    }

    public void div(Register rs, Register rt, Register hi, Register lo) {
        hi.setValue(rs.getValue() % rt.getValue());
        lo.setValue(rs.getValue() / rt.getValue());
    }

    public void mfhi(Register rd, Register hi) {
        rd.setValue(Constant.toInteger(hi.value));
    }

    public void mflo(Register rd, Register lo) {
        rd.setValue(Constant.toInteger(lo.value));
    }

    public void slt(Register rs, Register rt, Register rd) {
        if (rs.getValue() < rt.getValue()) rd.setValue(1);
        else rd.setValue(0);
    }

    public void sltu(Register rs, Register rt, Register rd) {
        if (rs.getUnsignedValue() < rt.getUnsignedValue()) rd.setValue(1);
        else rd.setValue(0);
    }

    public void slti(Register rs, Register rt, int valor) {
        if (rt.getValue() < valor) rs.setValue(1);
        else rs.setValue(0);
    }

    public void sltiu(Register rs, Register rt, int valor) {
        if (rt.getUnsignedValue() < Constant.toUnsignedInteger(Integer.toBinaryString(valor))) rs.setValue(1);
        else rs.setValue(0);
    }

    public void srl(Register rt, Register rd, int shamt) {
        rd.setValue(rt.getValue() >> shamt);
    }

    public void sll(Register rt, Register rd, int shamt) {
        rd.setValue(rt.getValue() << shamt);
    }

    public void lb(Register rs, Register rt, int valor) throws AccessErrorException {
        if ((valor % 4 != 0) || (rs.getValue() % 4 != 0)) throw new AccessErrorException();
        int pos = rs.getValue() + valor;
        rt.setValue(Constant.toInteger(mem.getWord(pos).substring(24)));
    }

    public void lbu(Register rs, Register rt, int valor) throws AccessErrorException {
        if ((valor % 4 != 0) || (rs.getValue() % 4 != 0)) throw new AccessErrorException();
        int pos = rs.getValue() + valor;
        rt.setValue(Integer.parseInt(mem.getWord(pos).substring(24), 2));
    }

    public void sb(Register rs, Register rt, int valor) throws AccessErrorException {
        if ((valor % 4 != 0) || (rs.getValue() % 4 != 0)) throw new AccessErrorException();
        int pos = rs.getValue() + valor;
        int aux = Integer.parseInt(rt.value.substring(24), 2);
        mem.setValue(pos, aux);
    }

    public void lh(Register rs, Register rt, int valor) throws AccessErrorException {
        if ((valor % 4 != 0) || (rs.getValue() % 4 != 0)) throw new AccessErrorException();
        int pos = rs.getValue() + valor;
        rt.setValue(Constant.toInteger(mem.getWord(pos).substring(16)));
    }

    public void lhu(Register rs, Register rt, int valor) throws AccessErrorException {
        if ((valor % 4 != 0) || (rs.getValue() % 4 != 0)) throw new AccessErrorException();
        int pos = rs.getValue() + valor;
        rt.setValue(Integer.parseInt(mem.getWord(pos).substring(16), 2));
    }

    public void sh(Register rs, Register rt, int valor) throws AccessErrorException {
        if ((valor % 4 != 0) || (rs.getValue() % 4 != 0)) throw new AccessErrorException();
        int pos = rs.getValue() + valor;
        int aux = Integer.parseInt(rt.value.substring(16), 2);
        mem.setValue(pos, aux);
    }

    public void lw(Register rs, Register rt, int valor) throws AccessErrorException {
        int pos;
        if ((valor % 4 != 0) || (rs.getValue() % 4 != 0)) throw new AccessErrorException();
        pos = rs.getValue() + valor;
        rt.setValue(mem.getValue(pos));
    }

    public void sw(Register rs, Register rt, int valor) throws AccessErrorException {
        if ((valor % 4 != 0) || (rs.getValue() % 4 != 0)) throw new AccessErrorException();
        int pos = rs.getValue() + valor;
        mem.setValue(pos, rt.getValue());
    }

    public void lui(Register rt, int immediate) {
        immediate = immediate << 16;
        rt.value = Constant.toBinary(immediate);
    }

    public boolean beq(Register rs, Register rt) {
        return rs.getValue() == rt.getValue();
    }

    public boolean bne(Register rs, Register rt) {
        return rs.getValue() != rt.getValue();
    }

    public boolean blez(Register rs) {
        return rs.getValue() <= 0;
    }

    public boolean bgtz(Register rs) {
        return rs.getValue() > 0;
    }
}