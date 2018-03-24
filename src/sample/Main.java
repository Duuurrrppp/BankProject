package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.awt.*;

import static oracle.jrockit.jfr.events.Bits.intValue;

public class Main extends Application {
    Stage window;
    SaldoKaarten saldoKaarten = new SaldoKaarten();
    static SwitchingValue sceneSwitch = new SwitchingValue();
    static SwitchingValue menuSwitch = new SwitchingValue();
    static PinValueClass pinValue = new PinValueClass();
    static SaldoKaarten saldoSwitch = new SaldoKaarten();

    public static void main(String[] args) {
        try {
            SerialTest.main();
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        window = primaryStage;
        window.setTitle("EXAMPLE Terminal");

        //Menu Top Stackpane + children
        StackPane topMenu = new StackPane();
        Label bankNaam = new Label("EXAMPLE BANK");
        bankNaam.getStyleClass().add("label-bank");
        topMenu.getChildren().add(bankNaam);
        topMenu.setAlignment(Pos.CENTER);

        //Menu center StackPane + children
        StackPane middleMenu = new StackPane();
        Label pinText = new Label("_   _   _   _");
        pinText.getStyleClass().add("label-pinText");
        middleMenu.getChildren().addAll(pinText);

        //Menu bottom HBox + children
        HBox bottomMenu = new HBox(30);
        Label info1 = new Label("Property of EXAMPLE and partners");
        Label info2 = new Label("Machine Number 1337");
        info1.getStyleClass().addAll("label-infoText");
        info2.getStyleClass().addAll("label-infoText");
        bottomMenu.getChildren().addAll(info1, info2);

        //Start Top Stackpane + children
        StackPane topStart = new StackPane();
        Label bankNaamStart = new Label("EXAMPLE BANK");
        bankNaamStart.getStyleClass().add("label-bank");
        topStart.getChildren().add(bankNaamStart);
        topStart.setAlignment(Pos.CENTER);

        //Nesting in Left borderpane
        BorderPane startLinks = new BorderPane();
        StackPane startCenterLinks = new StackPane();
        Button saldo = new Button("4 - Saldo");
        saldo.getStyleClass().addAll("button-big-yellow");
        startCenterLinks.getChildren().addAll(saldo);

        StackPane startFillerLinksLinks = new StackPane();
        Label filler0 = new Label("      ");
        startFillerLinksLinks.getChildren().addAll(filler0);
        startLinks.setLeft(startFillerLinksLinks);
        startLinks.setCenter(startCenterLinks);


        //Start Right StackPane + children
        BorderPane startRechts = new BorderPane();
        StackPane startCenterRechts = new StackPane();
        Button opnemen = new Button("6 - Opnemen");
        opnemen.getStyleClass().addAll("button-big-yellow");
        startCenterRechts.getChildren().addAll(opnemen);

        StackPane startFillerRechtsRechts = new StackPane();
        Label filler1 = new Label("      ");
        startFillerRechtsRechts.getChildren().addAll(filler1);
        startRechts.setRight(startFillerRechtsRechts);
        startRechts.setCenter(startCenterRechts);


        //Bottom End Button
        VBox exitButtonPane = new VBox(10);
        Button exitButton = new Button("* - Exit");
        Label filler2 = new Label();
        exitButton.getStyleClass().addAll("button-big-yellow");
        exitButtonPane.getChildren().addAll(exitButton,filler2);
        exitButtonPane.setAlignment(Pos.CENTER);

        //Saldo Top Stackpane + children
        StackPane saldoTop = new StackPane();
        Label bankNaamSaldo = new Label("EXAMPLE BANK");
        bankNaamSaldo.getStyleClass().add("label-bank");
        saldoTop.getChildren().add(bankNaamSaldo);
        saldoTop.setAlignment(Pos.CENTER);

        //Saldo Center Vbox + Children
        VBox saldoCenter = new VBox(150);
        Label saldoLabelStandaard = new Label("Uw Huidig Saldo:");
        saldoLabelStandaard.getStyleClass().addAll("label-text");
        Label saldoLabelSaldo = new Label("1000");
        saldoLabelSaldo.getStyleClass().addAll("label-text");
        saldoCenter.getChildren().addAll(saldoLabelStandaard, saldoLabelSaldo);
        saldoCenter.setAlignment(Pos.CENTER);

        //Saldo Bottom Vbox + children
        VBox saldoBottom = new VBox(10);
        Button saldoExitButton = new Button("0 - Back");
        Label saldoFiller = new Label();
        saldoExitButton.getStyleClass().addAll("button-big-yellow");
        saldoBottom.getChildren().addAll(saldoExitButton, saldoFiller);
        saldoBottom.setAlignment(Pos.CENTER);

        //Opneem Top Stackpane + children
        StackPane opneemTop = new StackPane();
        Label bankNaamOpneem = new Label("EXAMPLE BANK");
        bankNaamOpneem.getStyleClass().add("label-bank");
        opneemTop.getChildren().add(bankNaamOpneem);
        opneemTop.setAlignment(Pos.CENTER);

        //Opneem Bottom Vbox + children
        VBox opneemBottom = new VBox(10);
        Button opneemExitButton = new Button("0 - Back");
        Label opneemFiller = new Label();
        opneemExitButton.getStyleClass().addAll("button-big-yellow");
        opneemBottom.getChildren().addAll(opneemExitButton, opneemFiller);
        opneemBottom.setAlignment(Pos.CENTER);

        //Opneem Center StackPane + Children
        StackPane opneemCenter = new StackPane();
        Label opneemLabelText1 = new Label("Selecteer het geldbedrag dat u wilt opnemen");
        opneemLabelText1.getStyleClass().addAll("label-text");
        opneemCenter.getChildren().addAll(opneemLabelText1);
        opneemCenter.setAlignment(Pos.CENTER);

        //Opneem Left Vbox + Children
        VBox opneemLinks = new VBox(100);
        Button twintig = new Button("1 - 20");
        twintig.getStyleClass().addAll("button-big-yellow");
        Button vijftig = new Button("2 - 50");
        vijftig.getStyleClass().addAll("button-big-yellow");
        Button seventig = new Button("3 - 70");
        seventig.getStyleClass().addAll("button-big-yellow");
        Button honderd = new Button("4 - 100");
        honderd.getStyleClass().addAll("button-big-yellow");
        opneemLinks.getChildren().addAll(twintig,vijftig,seventig,honderd);
        opneemLinks.setAlignment(Pos.CENTER);

        //Opneem Right Vbox + Children
        VBox opneemRechts = new VBox(100);
        Button honderdtwintig = new Button("5 - 120");
        honderdtwintig.getStyleClass().addAll("button-big-yellow");
        Button honderdvijftig = new Button(" 6 -150");
        honderdvijftig.getStyleClass().addAll("button-big-yellow");
        Button honderdseventig = new Button("7 - 170");
        honderdseventig.getStyleClass().addAll("button-big-yellow");
        Button tweehonderd = new Button("8 - 200");
        tweehonderd.getStyleClass().addAll("button-big-yellow");
        opneemRechts.getChildren().addAll(honderdtwintig, honderdvijftig, honderdseventig, tweehonderd);
        opneemRechts.setAlignment(Pos.CENTER);

        //Borderpane opneemScherm + layouts
        BorderPane opneemScherm = new BorderPane();
        opneemScherm.setTop(opneemTop);
        opneemScherm.setBottom(opneemBottom);
        opneemScherm.setCenter(opneemCenter);
        opneemScherm.setLeft(opneemLinks);
        opneemScherm.setRight(opneemRechts);

        //Borderpane saldoScherm + layouts
        BorderPane saldoScherm = new BorderPane();
        saldoScherm.setTop(saldoTop);
        saldoScherm.setCenter(saldoCenter);
        saldoScherm.setBottom(saldoBottom);

        //Borderpane inlogscherm + layouts
        BorderPane inlogScherm = new BorderPane();
        inlogScherm.setTop(topMenu);
        inlogScherm.setCenter(middleMenu);
        inlogScherm.setBottom(bottomMenu);

        //Borderpane Startscherm + layouts
        BorderPane startScherm = new BorderPane();
        startScherm.setTop(topStart);
        startScherm.setBottom(exitButtonPane);
        startScherm.setLeft(startLinks);
        startScherm.setRight(startRechts);

        //Scenes die de borderpanes bevatten
        Scene startScene = new Scene(startScherm, screenSize.getWidth(), screenSize.getHeight());
        Scene menuScene = new Scene(inlogScherm, screenSize.getWidth(), screenSize.getHeight());
        Scene saldoScene = new Scene(saldoScherm, screenSize.getWidth(), screenSize.getHeight());
        Scene opneemScene = new Scene(opneemScherm, screenSize.getWidth(), screenSize.getHeight());
        startScene.getStylesheets().add("sample/Viper.css");
        menuScene.getStylesheets().add("sample/Viper.css");
        saldoScene.getStylesheets().add("sample/Viper.css");
        opneemScene.getStylesheets().add("sample/Viper.css");

        //Laat het definitief zien aan de gebruiker
        window.setMaximized(true);
        window.setScene(menuScene);
        window.show();

        //Listeners
        sceneSwitch.switchValueProperty().addListener((v, oldValue, newValue) ->{
            int newNumber = intValue(newValue);

            if(newNumber == 0){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        window.setScene(menuScene);
                    }
                });
            }
            if(newNumber == 1){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        window.setScene(startScene);
                    }
                });

            }

        });

        pinValue.pinValueProperty().addListener((v, oldValue, newValue) ->{
            int newNumber = intValue(newValue);

            if(newNumber == 0){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        pinText.setText("_   _   _   _");
                    }
                });
            }
            if(newNumber == 1){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        pinText.setText("*   _   _   _");
                    }
                });
            }
            if(newNumber == 2){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        pinText.setText("*   *   _   _");
                    }
                });
            }
            if(newNumber == 3){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        pinText.setText("*   *   *   _");
                    }
                });
            }
            if(newNumber == 4){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        pinText.setText("*   *   *   *");
                    }
                });
            }
        });

        menuSwitch.menuSwitchProperty().addListener((v, oldValue, newValue) -> {
            int newNumber = intValue(newValue);

            if(newNumber == 0){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        window.setScene(startScene);
                    }
                });
            }
            if(newNumber == 1){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        window.setScene(saldoScene);
                    }
                });

            }
            if(newNumber == 2){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        window.setScene(opneemScene);
                    }
                });

            }
        });

        saldoSwitch.saldoProperty().addListener((v, oldValue, newValue) -> {
            saldoLabelSaldo.setText(Integer.toString(saldoKaarten.getDisplaySaldo()));
        });

    }
}
