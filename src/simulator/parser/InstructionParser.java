package simulator.parser;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import simulator.exceptions.InvalidInstructionException;
import simulator.exceptions.UnableToParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ronaldo on 2015-01-14.
 */
public class InstructionParser {

    private static final String R_TYPE = "([a-z]+)\\s+(\\$?[a-z]+[0-9]?)\\s*,\\s*(\\$?[a-z]+[0-9]?)\\s*,\\s*(\\$?[a-z]+[0-9]?)";
    private static final String I_TYPE = "([a-z]+)\\s+(\\$?[a-z]+[0-9]?)\\s*,\\s*(\\$?[a-z]+[0-9]?)\\s*,\\s*(-?[0-9]+)";
    private static final String J_TYPE = "([a-z]+)\\s+([a-z0-9]+)";
    private static final String LABEL = "(\\D\\w+):";
    private static final String COMMENT = "//(.*)";

    public static List<Instruction> parseInstruction(String line) throws UnableToParseException, InvalidInstructionException {

        ArrayList<Instruction> instructions = new ArrayList<>();

        Pattern patternLabel = Pattern.compile(LABEL);
        Pattern patternComment = Pattern.compile(COMMENT);
        Pattern patternR = Pattern.compile(R_TYPE);
        Pattern patternI = Pattern.compile(I_TYPE);
        Pattern patternJ = Pattern.compile(J_TYPE);

        line = line.trim().toLowerCase();

        Matcher matcherR = patternR.matcher(line);
        Matcher matcherI = patternI.matcher(line);
        Matcher matcherJ = patternJ.matcher(line);
        Matcher matcherLabel = patternLabel.matcher(line);
        Matcher matcherComment = patternComment.matcher(line);

        if (matcherR.matches()) {
            String instruction = matcherR.group(1);
            String rd = matcherR.group(2);
            String rs = matcherR.group(3);
            String rt = matcherR.group(4);
            instructions.add(new InstructionR(instruction, rd, rs, rt));
        } else if (matcherI.matches()) {
            String instruction = matcherI.group(1);
            String rs = matcherI.group(2);
            String rt = matcherI.group(3);
            String imm = matcherI.group(4);
            instructions.add(new InstructionI(instruction, rs, rt, imm));
        } else if (matcherJ.matches()) {
            String instruction = matcherJ.group(1);
            String label = matcherJ.group(2);
            instructions.add(new InstructionJ(instruction, label));
        } else if (matcherLabel.matches()) {
            // TODO: decide how to handle labels
            instructions.add(new Label());
        } else if (!matcherComment.matches()) {
            throw new UnableToParseException(line);
        }

        return instructions;
    }

    public static List<Instruction> parseAllInstructions(List<String> lines) {
        return null;
    }

    public static List<Instruction> parseRInstruction(String line) {
        return null;
    }
}
