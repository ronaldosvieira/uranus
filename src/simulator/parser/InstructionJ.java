package simulator.parser;

import simulator.exceptions.InvalidInstructionException;

/**
 * Created by Ronaldo on 2015-01-14.
 */
public class InstructionJ extends Instruction {
    private String instruction;
    // TODO: use Label instance instead
    private String label;

    public InstructionJ(String instruction, String label) throws InvalidInstructionException {
        // TODO: check if instruction exists
        if (!(instruction.equals("j") || instruction.equals("jal"))) throw new InvalidInstructionException(instruction);
        this.instruction = instruction;
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstructionJ)) return false;

        InstructionJ that = (InstructionJ) o;

        if (!instruction.equals(that.instruction)) return false;
        if (!label.equals(that.label)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = instruction.hashCode();
        result = 31 * result + label.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return instruction + " " + label;
    }
}
