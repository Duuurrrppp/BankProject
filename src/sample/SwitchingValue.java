package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SwitchingValue {

    //Login -> Menu switch (switchValue)
    private static IntegerProperty switchValue = new SimpleIntegerProperty(new SwitchingValue(), "switchValue", 0);
    public IntegerProperty switchValueProperty() {
        return switchValue;
    }
    public int getSwitchValue() {
        return switchValue.get();
    }
    public void setSwitchValue(int switchValue) {
        this.switchValue.set(switchValue);
    }

    //menuSwitch
    public static IntegerProperty menuSwitch = new SimpleIntegerProperty(new SwitchingValue(), "menuSwitch", 0);
    public IntegerProperty menuSwitchProperty() {
        return menuSwitch;
    }
    public int getMenuSwitch() {
        return menuSwitch.get();
    }
    public void setMenuSwitch(int menuSwitch) {
        SwitchingValue.menuSwitch.set(menuSwitch);
    }
}
