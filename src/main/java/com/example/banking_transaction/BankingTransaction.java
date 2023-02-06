package com.example.banking_transaction;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Random;

public class BankingTransaction extends Application {

    /* 3 Sample Accounts have been created as a test 'account' so the program can be
     *    fully used without the need to register or open account. All 3 accounts have the same PIN
     *      Account Number: 12100001
     *      PIN:            123456
     *
     *      Account Number: 12100002
     *      PIN:            123456
     *
     *      Account Number: 12100003
     *      PIN:            123456
     *
     */

    static AccountDB mainDatabase = new AccountDB();
    Scenes scenes = new Scenes();

    @Override
    public void start(Stage mainFrame) throws Exception {
        Image icon = new Image("SubwayBank logo.png");

        openSampleAccounts();
        mainFrame.getIcons().add(icon);
        mainFrame.setTitle("Subway Bank");
        mainFrame.setResizable(false);
        mainFrame.setScene(scenes.loginPane());
        mainFrame.show();

    }

    public static void receipt(String accountNumber, String transaction, String amount) {
        Image icon = new Image("SubwayBank logo.png");

        Stage receiptFrame = new Stage();

        BorderPane root = new BorderPane();

        StackPane topPane = new StackPane();
        StackPane centerPane = new StackPane();
        StackPane bottomPane = new StackPane();

        Font receiptFont = Font.font("Courier Prime", 10);

        // ------ Nodes for the Top Region of region root
            Text receiptTitle = new Text("SUBWAY BANK TRANSACTION RECORD");
                receiptTitle.setFont(receiptFont);

            topPane.getChildren().add(receiptTitle);
            topPane.setPadding(new Insets(35, 0, 35, 0));
        // ------ Nodes for the Center Region of receipt root
            GridPane centerGrid = new GridPane();

            Label dateLabel = new Label("DATE:");
            Label timeLabel = new Label("TIME:");
            Label locationLabel = new Label("LOCATION:");
            Label receiptNoLabel = new Label("RECEIPT NO.:");
            Label accountNoLabel = new Label("ACCOUNT NO.:");
            Label transactionLabel = new Label("TRANSACTION:");
            Label amountLabel = new Label("AMOUNT:");
            Label currentBalLabel = new Label("CURRENT BAL:");
            Label availBalLabel = new Label("AVAILABLE BAL:");

            Text dateInput = new Text(String.valueOf(java.time.LocalDate.now()));
            Text timeInput = new Text(String.valueOf(java.time.LocalTime.now()));
            Text locationInput = new Text("MEGAMALL BRANCH");

                Random randomNum = new Random();
                String receiptNumber = String.format("%08d", randomNum.nextInt(100000000));
            Text receiptNoInput = new Text(receiptNumber);
            Text accountNoInput = new Text(accountNumber);
            Text transactionInput = new Text(transaction);
            Text amountInput = new Text(amount);
            Text currentBalInput = new Text(mainDatabase.getBalance());
            Text availBalInput = new Text(mainDatabase.getBalance());

            centerGrid.add(dateLabel, 0, 0, 1, 1);
            centerGrid.add(timeLabel, 0, 1, 1, 1);
            centerGrid.add(locationLabel, 0, 2, 1, 1);
            centerGrid.add(receiptNoLabel, 0, 3, 1, 1);
            centerGrid.add(accountNoLabel, 0, 4, 1, 1);
            centerGrid.add(transactionLabel, 0, 5, 1, 1);

            centerGrid.add(dateInput, 1, 0, 1, 1);
            centerGrid.add(timeInput, 1, 1, 1, 1);
            centerGrid.add(locationInput, 1, 2, 1, 1);
            centerGrid.add(receiptNoInput, 1, 3, 1, 1);
            centerGrid.add(accountNoInput, 1, 4, 1, 1);
            centerGrid.add(transactionInput, 1, 5, 1, 1);

            // The Amount Row will not be displayed if user only checks the balance
            if (!transaction.equals("CHECK BALANCE")) {
                centerGrid.add(amountLabel, 0, 6, 1, 1);
                centerGrid.add(currentBalLabel, 0, 7, 1, 1);
                centerGrid.add(availBalLabel, 0, 8, 1, 1);
                centerGrid.add(amountInput, 1, 6, 1, 1);
                centerGrid.add(currentBalInput, 1, 7, 1, 1);
                centerGrid.add(availBalInput, 1, 8, 1, 1);
            } else {
                centerGrid.add(currentBalLabel, 0, 6, 1, 1);
                centerGrid.add(availBalLabel, 0, 7, 1, 1);
                centerGrid.add(currentBalInput, 1, 6, 1, 1);
                centerGrid.add(availBalInput, 1, 7, 1, 1);
            }

            centerGrid.setVgap(5);
            centerGrid.setHgap(40);
            centerGrid.setAlignment(Pos.TOP_CENTER);
            centerPane.getChildren().add(centerGrid);
        // ------ Nodes for the Bottom Region of receipt root

            VBox bottomVBox = new VBox();
            Text bottomText = new Text("THANK YOU FOR CHOOSING SUBWAY BANK.");
            Text bottomText2 = new Text("ENJOY THE CONVENIENCE OF 24/7 BANKING");
                bottomText.setFont(receiptFont);
                bottomText2.setFont(receiptFont);

                bottomVBox.setSpacing(5);
                bottomVBox.setAlignment(Pos.BOTTOM_CENTER);
                bottomVBox.getChildren().addAll(bottomText, bottomText2);
                bottomPane.setPadding(new Insets(35, 0, 35, 0));
                bottomPane.getChildren().add(bottomVBox);


        // ------ Set up 3 regions to be used in border pane
        root.setTop(topPane);
        root.setCenter(centerPane);
        root.setBottom(bottomPane);


        Scene receiptScene = new Scene(root, 350, 450, Color.WHITE);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - receiptScene.getWidth()) * 0.85;
        double y = bounds.getMinY() + (bounds.getHeight() - receiptScene.getHeight()) * 0.3;
        receiptFrame.setX(x);
        receiptFrame.setY(y);

        receiptFrame.getIcons().add(icon);
        receiptFrame.setResizable(false);
        receiptFrame.setScene(receiptScene);
        receiptFrame.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void openSampleAccounts() {
        HashMap<String, String> sampleName1 = new HashMap<>();
        sampleName1.put("firstname", "George");
        sampleName1.put("lastname", "Flint");
        HashMap<String, String> sampleName2 = new HashMap<>();
        sampleName2.put("firstname", "John");
        sampleName2.put("lastname", "Turner");
        HashMap<String, String> sampleName3 = new HashMap<>();
        sampleName3.put("firstname", "Chelsaey");
        sampleName3.put("lastname", "Guerrero");

        long pin = 123456;
        mainDatabase.openAccount(mainDatabase.generateAccountNumber(), "231289884457", sampleName1, 3000, pin);
        mainDatabase.openAccount(mainDatabase.generateAccountNumber(), "334519230078", sampleName2, 5000, pin);
        mainDatabase.openAccount(mainDatabase.generateAccountNumber(), "121135674909", sampleName3, 6500, pin);



    }
}