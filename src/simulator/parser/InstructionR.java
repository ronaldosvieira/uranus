package simulator.parser;

import simulator.Register;
import simulator.exceptions.InvalidInstructionException;

/**

 * Created by Ronaldo on 2015-01-14.
 */
public class InstructionR extends Instruction {
    private String instruction;
    // TODO: use Register instance instead
    private String rd, rs, rt;

    public InstructionR(String instruction, String rd, String rs, String rt) throws InvalidInstructionException {
        // TODO: check if instruction exists
        if (!(instruction.equals("add") || instruction.equals("sub"))) throw new InvalidInstructionException(instruction);
        this.instruction = instruction;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstructionR)) return false;

        InstructionR that = (InstructionR) o;

        if (!instruction.equals(that.instruction)) return false;
        if (!rd.equals(that.rd)) return false;
        if (!rs.equals(that.rs)) return false;
        if (!rt.equals(that.rt)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = instruction.hashCode();
        result = 31 * result + rd.hashCode();
        result = 31 * result + rs.hashCode();
        result = 31 * result + rt.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return instruction + " " + rd + ", " + rs + ", " + rt;
    }
}
