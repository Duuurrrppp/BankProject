package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;


public class SerialTest implements SerialPortEventListener {

    static SwitchingValue sceneSwitch = new SwitchingValue();
    static SwitchingValue menuSwitch = new SwitchingValue();
    static PinValueClass pinValue = new PinValueClass();
    static SaldoKaarten saldoSwitch = new SaldoKaarten();

    private String uid;
    private String pincode = "";
    private boolean loggedIn = false;

    //regelt de scenes met het keypad
    private boolean inStartScene = true;
    private boolean inSaldoScene = false;
    private boolean inOpneemScene = false;

    //database stud
    private boolean blocked3D39DE2B = false;
    private int counter3D39DE2B = 0;
    private boolean blockedEE7528CF = false;
    private int counterEE7528CF = 0;
    SaldoKaarten saldoKaarten = new SaldoKaarten();

    SerialPort serialPort;
    private static final String PORT_NAMES[] = {
            "COM3", // Windows
    };
    private BufferedReader input;
    private OutputStream output;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;

    public void initialize() {
        // the next line is for Raspberry Pi and
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    public static void main() throws Exception {
        SerialTest main = new SerialTest();
        main.initialize();
        Thread t=new Thread() {
            public void run() {
                //the following line will keep this app alive for 1000 seconds,
                //waiting for events to occur and responding to them (printing incoming messages to console).
                try {Thread.sleep(100000);} catch (InterruptedException ie) {}
            }
        };
        t.start();
        //System.out.println("Started");
    }

    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine=input.readLine();
                if (!loggedIn){
                    loginMain(inputLine);
                } else {
                    keypadTracker(inputLine);
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    private void loginMain(String input){
        if(input.length() > 2){
            uid = input;
        } else if (!input.equals("*")){
            if(input.equals("#")){
                pincode = pincode.substring(0, pincode.length() - 1);
            } else {
                pincode += input;
            }
        } else if (input.equals("*")){
            pinValue.setPinValue(0);
            if (checklogin(uid, pincode)){
                loggedIn = true;
            } else {
                pincode="";
            }
        }
        //System.out.println(pincode);
        updateDisplay(pincode);
    }

    private boolean checklogin(String ID, String Code){
        if (ID.equals("EE7528CF")){
            if(!blockedEE7528CF){
                if(Code.equals("1234")){
                    sceneSwitch.setSwitchValue(1);
                    counterEE7528CF = 0;
                    return true;
                } else {
                    counterEE7528CF++;
                    if (counterEE7528CF == 3){
                        blockedEE7528CF = true;
                    }
                    return false;
                }
            }
        }    else if (ID.equals("3D39DE2B")){
            if(!blocked3D39DE2B){
                if(Code.equals("1234")){
                    sceneSwitch.setSwitchValue(1);
                    counter3D39DE2B = 0;
                    return true;
                } else {
                    counter3D39DE2B++;
                    if (counter3D39DE2B == 3){
                        blocked3D39DE2B = true;
                    }
                    return false;
                }
            }
        }
        //database stud
//        System.out.println("counter EE7528CF: " + counterEE7528CF);
//        System.out.println("blocked EE7528CF: " + blockedEE7528CF);
//
//        System.out.println("counter 3D39DE2B: " + counter3D39DE2B);
//        System.out.println("blocked 3D39DE2B: " + blocked3D39DE2B);
        return false;
    }

    private void updateDisplay(String code){
        if (code.length()==1){
            pinValue.setPinValue(1);
        } else if (code.length()==2){
            pinValue.setPinValue(2);
        } else if (code.length()==3){
            pinValue.setPinValue(3);
        } else if (code.length()==4){
            pinValue.setPinValue(4);
        } else if (code.length()==0){
            pinValue.setPinValue(0);
        }
    }

    private void keypadTracker(String input){
        saldoKaarten.setNewSaldo(saldoKaarten.getHuidigSaldo(uid),uid,pincode);
        saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
        if (input.equals("*")){
            System.out.println("* pressed");
            inOpneemScene = false;
            inSaldoScene = false;
            inStartScene = true;
            loggedIn = false;
            menuSwitch.setMenuSwitch(0);
            sceneSwitch.setSwitchValue(0);
            pincode="";
            updateDisplay(pincode);
        } else if (inStartScene){
            if(input.equals("4")){
                menuSwitch.setMenuSwitch(1);
                inStartScene = false;
                inSaldoScene = true;
            }
            if(input.equals("6")){
                menuSwitch.setMenuSwitch(2);
                inStartScene = false;
                inOpneemScene = true;
            }
        } else if (inSaldoScene){
            if(input.equals("0")){
                menuSwitch.setMenuSwitch(0);
                saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
                inSaldoScene = false;
                inStartScene = true;
            }
        } else if (inOpneemScene){
            int huidigSaldo = saldoKaarten.getHuidigSaldo(uid);
            switch (input){
                case "0":
                    menuSwitch.setMenuSwitch(0);
                    inOpneemScene = false;
                    inStartScene = true;
                    break;
                case "1": //20
                    if (huidigSaldo - 20 >= 0){
                        saldoKaarten.setNewSaldo(huidigSaldo - 20,uid,pincode);
                    }
                    saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
                    break;
                case "2": //50
                    if (huidigSaldo - 50 >= 0){
                        saldoKaarten.setNewSaldo(huidigSaldo - 50,uid,pincode);
                    }
                    saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
                    break;
                case "3": //70
                    if (huidigSaldo - 70 >= 0){
                        saldoKaarten.setNewSaldo(huidigSaldo - 70,uid,pincode);
                    }
                    saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
                    break;
                case "4": //100
                    if (huidigSaldo - 100 >= 0){
                        saldoKaarten.setNewSaldo(huidigSaldo - 100,uid,pincode);
                    }
                    saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
                    break;
                case "5": //120
                    if (huidigSaldo - 120 >= 0){
                        saldoKaarten.setNewSaldo(huidigSaldo - 120,uid,pincode);
                    }
                    saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
                    break;
                case "6": //150
                    if (huidigSaldo - 150 >= 0){
                        saldoKaarten.setNewSaldo(huidigSaldo - 150,uid,pincode);
                    }
                    saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
                    break;
                case "7": //170
                    if (huidigSaldo - 170 >= 0){
                        saldoKaarten.setNewSaldo(huidigSaldo - 170,uid,pincode);
                    }
                    saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
                    break;
                case "8": //200
                    if (huidigSaldo - 200 >= 0){
                        saldoKaarten.setNewSaldo(huidigSaldo - 200,uid,pincode);
                    }
                    saldoSwitch.setSaldo(saldoSwitch.getSaldo() + 1);
                    break;
            }

        }
//        System.out.println("inOpneemScene "+inOpneemScene);
//        System.out.println("inSaldoScene " + inSaldoScene);
//        System.out.println("inStartScene: " + inStartScene);
//        System.out.println(input);
//        System.out.println("uid & saldo: " + uid + saldoKaarten.getHuidigSaldo(uid));

    }
}

