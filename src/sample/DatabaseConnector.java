package sample;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class DatabaseConnector {
    private static HttpURLConnection con;
    //URL aanpassen aan welke API gebruikt wordt
    private String url = "https://bytegroep2.scriptonic.nl/BankApi.php";
    //Test Parameters om inloggen te testen
    private String urlParameters = "username=71090885&password=1234&terminalId=2&action=fetch";
    //Roep deze aan om de input van de API te bekijken
    private String stringOutput;
    //Variabelen om aan te roepen in je bank
    private double bankRekeningSaldo;
    private double spaarRekeningSaldo;
    private int bankRekeningNummer;
    private int spaarRekeningNummer;
    private String achternaam;
    private boolean blocked;
    private int attempts;
    private boolean succesfulLogin = false;
    private boolean nietGenoegSaldo = false;

    public void StuurData() throws IOException {


        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            //Zet de Output in een String
            stringOutput = content.toString();
            getData();

        } finally {

            con.disconnect();
        }
    }

    //Stuurt de verzamelde data door naar de Server
    public void getVerification(String PasID, int IngevoerdePincode, int TerminalID) throws IOException {
        urlParameters = "username=" + PasID + "&password=" + IngevoerdePincode + "&terminalId=" + TerminalID +"&action=fetch";
        StuurData();
        if (blocked) {
            System.out.println("Deze Kaart is Geblokkeerd");
            //Doe hier zelf iets mee in je eigen systeem
        }

    }

    //Bekijkt alle Data en splitst het per variabele
    public void getData() {
        String[] parts = stringOutput.split(":");

        int temp = parseInt(parts[1]);
        if (temp >= 1) succesfulLogin = true;
        else succesfulLogin = false;
        if(succesfulLogin) {
            bankRekeningSaldo = parseDouble(parts[3]);
            spaarRekeningSaldo = parseDouble(parts[5]);
            bankRekeningNummer = parseInt(parts[7]);
            spaarRekeningNummer = parseInt(parts[9]);
            achternaam = parts[11];
            temp = parseInt(parts[13]);
            if (temp >= 1) blocked = true;
            else blocked = false;
            attempts = parseInt(parts[15]);
        }else{
            temp = parseInt(parts[5]);
            if (temp >= 1) blocked = true;
            else blocked = false;
            attempts = parseInt(parts[3]);
        }

    }

    public void veranderSaldo(String PasID, int IngevoerdePincode, int TerminalID, int Amount) throws IOException {
        if (bankRekeningSaldo + Amount >= 0) {
            nietGenoegSaldo = false;
            //Stuur Request naar Server
            urlParameters = "username=" + PasID + "&password=" + IngevoerdePincode + "&terminalId=" + TerminalID +"&amount="+Amount+"&action=update";
            //Herlaad Data
            StuurData();
        }else {
            System.out.println("Niet genoeg saldo");
            nietGenoegSaldo = true;
        }
    }

    public String getStringOutput() {
        return stringOutput;
    }

    public double getBankRekeningSaldo() {
        return bankRekeningSaldo;
    }

    public double getSpaarRekeningSaldo() {
        return spaarRekeningSaldo;
    }

    public int getBankRekeningNummer() {
        return bankRekeningNummer;
    }

    public int getSpaarRekeningNummer() {
        return spaarRekeningNummer;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean isSuccesfulLogin() {
        return succesfulLogin;
    }

    public boolean isNietGenoegSaldo() {
        return nietGenoegSaldo;
    }
}