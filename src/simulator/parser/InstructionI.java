package simulator.parser;

import simulator.exceptions.InvalidInstructionException;

/**
 * Created by Ronaldo on 2015-01-14.
 */
public class InstructionI extends Instruction {
    private String instruction;
    // TODO: use Register instance instead
    private String rs, rt;
    private String imm;

    public InstructionI(String instruction, String rs, String rt, String imm) throws InvalidInstructionException {
        // TODO: check if instruction exists
        if (!(instruction.equals("addi") || instruction.equals("subi"))) throw new InvalidInstructionException(instruction);
        this.instruction = instruction;
        this.rs = rs;
        this.rt = rt;
        // TODO: check if it's bin, hex or dec
        this.imm = imm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstructionI)) return false;

        InstructionI that = (InstructionI) o;

        if (!imm.equals(that.imm)) return false;
        if (!instruction.equals(that.instruction)) return false;
        if (!rs.equals(that.rs)) return false;
        if (!rt.equals(that.rt)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = instruction.hashCode();
        result = 31 * result + rs.hashCode();
        result = 31 * result + rt.hashCode();
        result = 31 * result + imm.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return instruction + " " + rs + ", " + rt + ", " + imm;
    }
}
