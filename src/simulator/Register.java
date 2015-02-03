package simulator;

public class Register {
    public String address;
    public String value;

    public Register(String address) {
        this.address = address;
        this.value = "00000000000000000000000000000000";
    }

    public int getValue() {
        return Constant.toInteger(value);
    }

    public void setValue(int value) {
        this.value = Constant.toBinary(value);
    }

    public String getAddress() {
        return address;
    }

    public long getUnsignedValue() {
        return Constant.toUnsignedInteger(value);
    }

    public String toString() {
        return address;
    }
}