package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PinValueClass {

    private static IntegerProperty pinValue = new SimpleIntegerProperty(new PinValueClass(), "pinValue", 0);

    public static IntegerProperty pinValueProperty() {
        return pinValue;
    }

    public static int getPinValue() {
        return pinValue.get();
    }

    public static void setPinValue(int pinValue) {
        PinValueClass.pinValue.set(pinValue);
    }
}
