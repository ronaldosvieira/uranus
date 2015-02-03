package test;

import org.junit.Before;
import org.junit.Test;
import simulator.exceptions.InvalidInstructionException;
import simulator.exceptions.UnableToParseException;
import simulator.parser.InstructionI;
import simulator.parser.InstructionJ;
import simulator.parser.InstructionR;
import simulator.parser.InstructionParser;

import static org.junit.Assert.*;

public class InstructionParserTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testParseAllInstructions() throws Exception {

    }

    @Test
    public void testParseInstruction() throws UnableToParseException, InvalidInstructionException {
        String inst;

        // Type R cases
        inst = "add $t1, $t2, $s0";
        assertEquals(new InstructionR("add", "$t1", "$t2", "$s0"), InstructionParser.parseInstruction(inst).get(0));

        inst = "    sub $t1,    $t2,     $s0    ";
        assertEquals(new InstructionR("sub", "$t1", "$t2", "$s0"), InstructionParser.parseInstruction(inst).get(0));

        inst = "add $t1, $t2, hi";
        assertEquals(new InstructionR("add", "$t1", "$t2", "hi"), InstructionParser.parseInstruction(inst).get(0));

        inst = "add$t1, $t2, $s0";
        try {
            InstructionParser.parseInstruction(inst);
            fail(inst + " not supposed to be successful parsed");
        } catch (UnableToParseException e) {}

        inst = "nonExistentInstruction $t1, $t2, $s0";
        try {
            InstructionParser.parseInstruction(inst);
            fail(inst + " not supposed to be successful parsed");
        } catch (InvalidInstructionException e) {}
        //

        // Type I cases
        inst = "addi $t1, $t2, 9";
        assertEquals(new InstructionI("addi", "$t1", "$t2", "9"), InstructionParser.parseInstruction(inst).get(0));

        inst = "    subi $t1,    $t2,     -9    ";
        assertEquals(new InstructionI("subi", "$t1", "$t2", "-9"), InstructionParser.parseInstruction(inst).get(0));

        inst = "addi$t1, $t2, 1";
        try {
            InstructionParser.parseInstruction(inst);
            fail(inst + " not supposed to be successful parsed");
        } catch (UnableToParseException e) {}

        inst = "nonExistentInstruction $t1, $t2, 1";
        try {
            InstructionParser.parseInstruction(inst);
            fail(inst + " not supposed to be successful parsed");
        } catch (InvalidInstructionException e) {}
        //

        // Type J cases
        inst = "jal label";
        assertEquals(new InstructionJ("jal", "label"), InstructionParser.parseInstruction(inst).get(0));

        inst = "    j   5    ";
        assertEquals(new InstructionJ("j", "5"), InstructionParser.parseInstruction(inst).get(0));

        inst = "nonExistentInstruction label";
        try {
            InstructionParser.parseInstruction(inst);
            fail(inst + " not supposed to be successful parsed");
        } catch (InvalidInstructionException e) {}
        //
    }
}