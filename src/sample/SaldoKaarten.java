package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SaldoKaarten {

    static IntegerProperty saldoSwitch = new SimpleIntegerProperty(new SwitchingValue(), "saldo", 0);
    static private int saldoEE7528CF = 1000;
    static private int saldo3D39DE2B = 1000;
    static private int displaySaldo;

    public static IntegerProperty saldoProperty() {
        return saldoSwitch;
    }
    public static int getSaldo() {
        return saldoSwitch.get();
    }
    public static void setSaldo(int saldo) {
        SaldoKaarten.saldoSwitch.set(saldo);
    }

    private int getSaldo3D39DE2B() {
        return saldo3D39DE2B;
    }

    private int getSaldoEE7528CF() {
        return saldoEE7528CF;
    }

    public int getHuidigSaldo(String uid){
        if(uid.equals("EE7528CF")){
            return getSaldoEE7528CF();
        }
        if(uid.equals("3D39DE2B")){
            return getSaldo3D39DE2B();
        }
        return 0;
    }

    public void setNewSaldo(int saldo, String uid, String pin){
        if (uid.equals("EE7528CF")){
            if(pin.equals("1234")){
                setSaldoEE7528CF(saldo);
            }
        } else if (uid.equals("3D39DE2B")){
            if(pin.equals("1234")){
                setSaldo3D39DE2B(saldo);
            }
        }
    }

    private void setSaldoEE7528CF(int saldoEE7528CF) {
        this.saldoEE7528CF = saldoEE7528CF;
        this.displaySaldo = saldoEE7528CF;
    }

    private void setSaldo3D39DE2B(int saldo3D39DE2B) {
        this.saldo3D39DE2B = saldo3D39DE2B;
        this.displaySaldo = saldo3D39DE2B;
    }

    public int getDisplaySaldo(){
        return displaySaldo;
    }
}
