package com.example.banking_transaction;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.function.UnaryOperator;


class GeneralNodes {
    /*
    * GeneralNodes contains nodes that's almost used on all scenes
    */

    // --------- Initialization of all layout panes to be used
    BorderPane root = new BorderPane();
    StackPane topPane = new StackPane();
    HBox topPaneNumber = new HBox(); // for displaying the censored acc.number after logged in on left side of topPane
    VBox centerPane = new VBox();
    VBox rightPane = new VBox();
    VBox leftPane = new VBox();
    // --------- End of all layout panes

    Rectangle blueTopBanner = new Rectangle();
    public Rectangle rectangleHeader80() {
        blueTopBanner.widthProperty().bind(scene.widthProperty());
        blueTopBanner.setHeight(80);
        blueTopBanner.setFill(Color.rgb(5,55,130));
        return blueTopBanner;
    }

    public Rectangle rectangleHeader100() {
        blueTopBanner.widthProperty().bind(scene.widthProperty());
        blueTopBanner.setHeight(100);
        blueTopBanner.setFill(Color.rgb(5,55,130));
        return blueTopBanner;
    }

    // BUTTONS ------------
    Button backBtn = new Button("←");
    Button logoutBtn = new Button("Log out");
    // end of BUTTONS -----


    Text errorText = new Text(" ");

    public Text setErrorTexts() {
        errorText.setFont(smallFont);
        errorText.setFill(Color.RED);
        errorText.setText("");
        return errorText;
    }

    // --------- FONTS
    Font titleFont = Font.font("Malgun Gothic", FontWeight.BOLD ,32);
    Font titleFont2 = Font.font("Malgun Gothic", 32);
    Font biggerBodyFont = Font.font("Malgun Gothic", 22);
    Font bodyFont = Font.font("Malgun Gothic", 16);
    Font smallFont = Font.font("Malgun Gothic", 10);

    Scene scene = new Scene(root, 820, 470, Color.WHITE);
}

class StylesAndProperties extends GeneralNodes {
    /*
     * StylesAndProperties will contain the properties set and its design on every node in GeneralNode
     * This separates the codes that handles the styling of each node to scenes
     */



    // BUTTON PROPERTIES ------------
        public Button setCommonButtonProperties(Button btn, boolean blueBtn) {
            btn.setFont(bodyFont);
            btn.setTextFill(Color.WHITE);
            btn.setPrefWidth(150);
            btn.setCursor(Cursor.HAND);
            if(blueBtn) {
                btn.setStyle("-fx-background-color:#0A2064;");
            }
            return btn;
        }
        public Button setBackBtnProperties() {
            backBtn.setFont(bodyFont);
            backBtn.setTextFill(Color.WHITE);
            backBtn.setPrefWidth(40);
            backBtn.setCursor(Cursor.HAND);
            backBtn.setStyle("-fx-background-color:#0A2064;");
            return backBtn;
        }
        public Button setLogOutBtn() {
            logoutBtn.setFont(bodyFont);
            logoutBtn.setTextFill(Color.WHITE);
            logoutBtn.setPrefWidth(100);
            logoutBtn.setCursor(Cursor.HAND);
            logoutBtn.setStyle("-fx-background-color:#0A2064;");
            return logoutBtn;
        }

    // end of BUTTON PROPERTIES -----


    // UnaryOperator will inspect if the input of the user has keypressed a digit or not
    //   this will be used for textfields to be only restricted on digits
    UnaryOperator<TextFormatter.Change> digitFilter = change -> {
        String userinput = change.getText();
        if (userinput.matches("[0-9]*")) {
            return change;
        }
        return null;
    };

    UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
        String userinput = change.getText();
        if (userinput.matches("\\d*\\.?\\d*")) {
            return change;
        }
        return null;
    };


    void designTopPane(String censoredAccountNumber) {
        topPane.getChildren().clear();
        topPaneNumber.getChildren().clear();


        // ------ Nodes for the Top Region of root
        //  Top Pane consists of the rectangle and the censored account number
        Text displayAccountNum = new Text(censoredAccountNumber);
        displayAccountNum.setFill(Color.WHITE);
        displayAccountNum.setFont(titleFont);

        topPaneNumber.getChildren().add(displayAccountNum);
        topPaneNumber.setPadding(new Insets(18, 0, 0, 100));

        topPane.getChildren().addAll(rectangleHeader80(), topPaneNumber);
    }

    // method censors the String accountnumber (the first 6 numbers only) which will be displayed on top left
    //   of the scene after user has logged in
    String censorDisplayNumber(String accountNumber) {
        char[] censorNumArray = accountNumber.toCharArray();

        for (int i = 0; i < 6; i++) {
            censorNumArray[i] = '*';
        }
        return String.valueOf(censorNumArray);
    }

    // Method sets the properties of 4 main buttons for mainPane
    void setMainButtonsProperties(Button button) {
        Font bodyFont = Font.font("Malgun Gothic", 16);
        button.setFont(bodyFont);
        button.setStyle("-fx-background-color:#12359E;");
        //button.setStyle("-fx-background-color:#5E5BFF;");
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(250);
        button.setPrefHeight(40);
        button.setCursor(Cursor.HAND);

    }


}

public class Scenes extends StylesAndProperties {

    /* This class will contain ALL the methods that will design ALL Panes
     *  All the Panes here are:
     *  1. LOGIN Pane /
     *  2. MAIN Pane /
     *  3. WITHDRAW Pane /
     *  4. DEPOSIT Pane /
     *  5.a. First CHECK BALANCE Pane /
     *          - consists of 2 buttons for user to choose on how to show account balance
     *  5.b. Second CHECK BALANCE Pane /
     *          - consists of displaying account balance on pane
     *  6. DISPLAY TEXT Pane /
     *      - displays single line of text at centerPane
     *  7. CONFIRM Pane /
     *  8. REGISTER Pane
     *
     *  Other Panes to be displayed soon
     *  8. TRANSACTION TABLE Pane
     *  9. ADMIN Pane (displays list of accounts)
     *
     *
     * This class also contains the functions of the buttons
     */


    static AccountDB mainDatabase = BankingTransaction.mainDatabase;


    public Scene loginPane() {

        /* -----------------------------------------------
         * The first scene displayed when the program runs
         * -----------------------------------------------
         * Login Pane consists of
         * - topPane with rectangle100 (100px height) and name of the bank
         * - centerPane (or named loginForm)
         *      for two text fields, one small clickable text for registration of account and one login button
         */

        VBox loginForm = new VBox();
        VBox loginForm2 = new VBox();

        // ------ Nodes for the Top Region of root
        Text title = new Text("SubwayBank");

        // StackPane automatically centers the objects
        title.setFill(Color.WHITE);
        title.setFont(titleFont);

        topPane.getChildren().clear();
        topPane.getChildren().addAll(rectangleHeader100(), title);

        // ------ Nodes for the Center Region of root
        Label accountNumLabel = new Label("Account Number: ");
        Label pinLabel = new Label("PIN: ");
        TextField accountNumText = new TextField();
        PasswordField pinText = new PasswordField();
        Text registerNow = new Text("Create an Account? Register now.");
        Button loginBtn = new Button("Log in");

        Insets padding = new Insets(20, 250, 30, 250);

        // For both Labels and TextFields
        accountNumLabel.setLabelFor(accountNumText);
        pinLabel.setLabelFor(pinText);

        accountNumLabel.setFont(bodyFont);
        pinLabel.setFont(bodyFont);


        accountNumText.setFont(bodyFont);
        pinText.setFont(bodyFont);

        accountNumText.setTextFormatter(new TextFormatter<String>(digitFilter));
        pinText.setTextFormatter(new TextFormatter<String>(digitFilter));

        accountNumText.setPrefHeight(40);
        pinText.setPrefHeight(40);


        // For log in button
        loginBtn.setFont(bodyFont);
        loginBtn.setStyle("-fx-background-color:#0A2064;");
        loginBtn.setTextFill(Color.WHITE);
        loginBtn.setPrefWidth(100);

        // Set ActionEvent on the LogIn Button and registernow text
        loginBtn.setOnAction(event ->
                loginBtnEvent(accountNumText.getText(), pinText.getText()));
        registerNow.setOnMouseClicked(event -> registerPane());

        // For register now text
        registerNow.setFont(smallFont);
        registerNow.setFill(Color.rgb(5,55,130));

        // For hovering the cursor on the nodes
        accountNumText.setCursor(Cursor.TEXT);
        pinText.setCursor(Cursor.TEXT);
        loginBtn.setCursor(Cursor.HAND);
        registerNow.setCursor(Cursor.HAND);

        // For the overall Log In Form (including the small text and the Log in button in loginForm2 layout pane)
        loginForm.setSpacing(10.0);
        loginForm.setPadding(padding);
        loginForm.setStyle("-fx-margin-left: 20px;");

        loginForm2.setSpacing(10.0);
        loginForm2.setAlignment(Pos.BOTTOM_CENTER);
        loginForm2.getChildren().addAll(registerNow, loginBtn);


        loginForm.getChildren().addAll(setErrorTexts(), accountNumLabel, accountNumText, pinLabel, pinText, loginForm2);

        // ------ Set up the 4 regions of border pane
        root.setTop(topPane);
        root.setCenter(loginForm);
        root.setLeft(null);
        root.setRight(null);

        return scene;
    }

    public Scene mainPane(String accountNumber) {
        /* -----------------------------------------------
         * Scene displays after the user has logged in
         *  giving user choice on what to do next
         * -----------------------------------------------
         * Main Pane consists of
         * - topPane with rectangle8 (80px height) and censored account number
         * - centerPane for 3 main buttons (deposit, withdraw, check balance)
         * - rightPane for Logout button (displayed on bottom right)
         */

        String censoredAccountNumber = censorDisplayNumber(accountNumber);

        // Nodes for Top Region of root
                designTopPane(censoredAccountNumber);

        // Nodes for Center Region of root

            // centerPane nodes and additional properties
        Button toDepositBtn = new Button("Deposit");
        Button toWithdrawBtn = new Button("Withdraw");
        Button toCheckBalanceBtn = new Button("Check Balance");
                setMainButtonsProperties(toDepositBtn);
                setMainButtonsProperties(toWithdrawBtn);
                setMainButtonsProperties(toCheckBalanceBtn);

            // ------ Button Action Events
            toDepositBtn.setOnAction(event -> depositPane());
            toWithdrawBtn.setOnAction(event -> withdrawPane());
            toCheckBalanceBtn.setOnAction(event -> checkBalancePane());

            // centerPane properties
            Insets padding = new Insets(50, 110, 30, 290);
            centerPane.setSpacing(20.0);
            centerPane.setPadding(padding);

                // Add Nodes to centerPane ----------------------------------------------------------
                centerPane.getChildren().clear(); // clear first before adding designated node
                centerPane.getChildren().addAll(toDepositBtn, toWithdrawBtn, toCheckBalanceBtn);
                // end of adding nodes to centerPane ------------------------------------------------

        // Nodes of Right Region of root

            // ------ Logout Button Action Event
            logoutBtn.setOnAction(event -> logOutBtnEvent());

            // rightPane properties
            rightPane.setPadding(new Insets(320, 40, 0, 40));

                // Add Nodes to rightPane -----------------------------------------------------------
                rightPane.getChildren().clear(); // clear first before adding designated node
                rightPane.getChildren().add(setLogOutBtn());
                // end of adding nodes to rightPane -------------------------------------------------

        // ------ Set up the 4 regions of border pane
        root.setTop(topPane);
        root.setCenter(centerPane);
        root.setRight(rightPane);
        root.setLeft(null);

        return scene;
    }

    public Scene mainPane() {
        /* -----------------------------------------------
         * mainPane w/o param is used by switching from other panes (besides loginPane) to mainPane
         * as TopPane has already been set after logging in
         * -----------------------------------------------
         */

        // Nodes for Center Region of root

            // centerPane nodes and additional properties
        Button toDepositBtn = new Button("Deposit");
        Button toWithdrawBtn = new Button("Withdraw");
        Button toCheckBalanceBtn = new Button("Check Balance");
                setMainButtonsProperties(toDepositBtn);
                setMainButtonsProperties(toWithdrawBtn);
                setMainButtonsProperties(toCheckBalanceBtn);

            // ------ Button Action Events
            toDepositBtn.setOnAction(event -> depositPane());
            toWithdrawBtn.setOnAction(event -> withdrawPane());
            toCheckBalanceBtn.setOnAction(event -> checkBalancePane());

            // centerPane properties
            Insets padding = new Insets(50, 110, 30, 290);
            centerPane.setSpacing(20.0);
            centerPane.setPadding(padding);

                // Add Nodes to centerPane ----------------------------------------------------------
                centerPane.getChildren().clear(); // clear first before adding designated node
                centerPane.getChildren().addAll(toDepositBtn, toWithdrawBtn, toCheckBalanceBtn);
                // end of adding nodes to centerPane ------------------------------------------------


        // Nodes of Right Region of root

            // ------ Logout Button Action Event
            logoutBtn.setOnAction(event -> logOutBtnEvent());

            // rightPane properties
            rightPane.setPadding(new Insets(320, 40, 0, 40));

                // Add Nodes to rightPane -----------------------------------------------------------
                rightPane.getChildren().clear(); // clear first before adding designated node
                rightPane.getChildren().add(setLogOutBtn());
                // end of adding nodes to rightPane -------------------------------------------------

        // ------ Set up the 4 regions of border pane
        // Top Pane has already been set in mainPane w/ param
        root.setCenter(centerPane);
        root.setRight(rightPane);
        root.setLeft(null);

        return scene;
    }

    public Scene depositPane() {
        /* -----------------------------------------------
         * Scene displays after user has clicked deposit button
         * -----------------------------------------------
         * Deposit Pane consists of
         *  - topPane with rectangle80 (80px height) and censored account number
         *  - centerPane for displaying:
         *      - Label "Amount"
         *      - Text field which accepts amount
         *      - Deposit Button
         *  - leftPane with left arrow button
         */

        // Nodes for Center Region of root

        // This VBox pane will align button placement on bottom center of CenterPane
        VBox depositBtnPlacement = new VBox();

            // centerPane nodes and additional properties
        Label depositLabel = new Label("Amount:");
        TextField depositText = new TextField();
        Button depositBtn = new Button("Deposit");
            depositLabel.setFont(bodyFont);
            depositText.setFont(bodyFont);
            depositBtn.setFont(bodyFont);

            depositText.setPrefHeight(40);
            depositText.setTextFormatter(new TextFormatter<String>(doubleFilter));

                setCommonButtonProperties(depositBtn, true);
            depositBtn.disableProperty().bind(Bindings.isEmpty(depositText.textProperty()));

            // ------ Button Action Events
            depositBtn.setOnAction(event -> depositBtnEvent(depositText.getText()));

                // Add Node to VBox
                depositBtnPlacement.setAlignment(Pos.BOTTOM_CENTER);
                depositBtnPlacement.getChildren().add(depositBtn);
                // end of adding node to VBox

            // centerPane properties
            Insets padding = new Insets(25, 280, 0, 150);
            centerPane.setSpacing(15.0);
            centerPane.setPadding(padding);
            centerPane.setPrefWidth(scene.getWidth());
            centerPane.setAlignment(Pos.TOP_LEFT);

                // Add Nodes to centerPane ----------------------------------------------------------
                centerPane.getChildren().clear(); // clear first before adding designated node
                centerPane.getChildren().addAll(setErrorTexts(), depositLabel, depositText, depositBtnPlacement);
                // end of adding nodes to centerPane ------------------------------------------------

        // Nodes of Left Region of root

            // ------ Back Button Action Events
            backBtn.setOnAction(event -> backBtnEvent());

            // leftPane properties
            leftPane.setPadding(new Insets(20, 40, 0, 40));

                // Add Nodes to leftPane ------------------------------------------------------------
                leftPane.getChildren().clear(); // clear first before add the designated node
                leftPane.getChildren().add(setBackBtnProperties());
                // end of adding nodes to leftPane --------------------------------------------------

        // ------ Set up the 4 regions of border pane
        root.setCenter(centerPane);
        root.setLeft(leftPane);
        root.setRight(null);

        return scene;
    }

    public Scene withdrawPane() {
        /* -----------------------------------------------
         * Scene displays after user has clicked withdraw button
         * -----------------------------------------------
         * Deposit Pane consists of
         *  - topPane with rectangle80 (80px height) and censored account number
         *  - centerPane for displaying GridPane with checkboxes labeled from P1,000 to P10,000 and other amount
         *  - leftPane with left arrow button
         */


        // Nodes for Center Region of root

        // GridPane is used for the layout of radio buttons
        GridPane centerGridPane = new GridPane();
        // This VBox pane will align button placement on bottom center of CenterPane
        VBox withdrawBtnPlacement = new VBox();
        final ToggleGroup btnGroup = new ToggleGroup();

            // centerPane nodes and additional properties
                // nodes for centerGridPane
                RadioButton oneThousand = new RadioButton("₱1,000");
                RadioButton twoThousand = new RadioButton("₱2,000");
                RadioButton threeThousand = new RadioButton("₱3,000");
                RadioButton fourThousand = new RadioButton("₱4,000");
                RadioButton fiveThousand = new RadioButton("₱5,000");
                RadioButton sixThousand = new RadioButton("₱6,000");
                RadioButton sevenThousand = new RadioButton("₱7,000");
                RadioButton eightThousand = new RadioButton("₱8,000");
                RadioButton nineThousand = new RadioButton("₱9,000");
                RadioButton tenThousand = new RadioButton("₱10,000");
                RadioButton otherAmount = new RadioButton("Other Amount");
                TextField otherAmountTxtField = new TextField();

                oneThousand.setFont(biggerBodyFont);
                    oneThousand.setToggleGroup(btnGroup);
                        oneThousand.setCursor(Cursor.HAND);
                twoThousand.setFont(biggerBodyFont);
                    twoThousand.setToggleGroup(btnGroup);
                        twoThousand.setCursor(Cursor.HAND);
                threeThousand.setFont(biggerBodyFont);
                    threeThousand.setToggleGroup(btnGroup);
                        threeThousand.setCursor(Cursor.HAND);
                fourThousand.setFont(biggerBodyFont);
                    fourThousand.setToggleGroup(btnGroup);
                        fourThousand.setCursor(Cursor.HAND);
                fiveThousand.setFont(biggerBodyFont);
                    fiveThousand.setToggleGroup(btnGroup);
                        fiveThousand.setCursor(Cursor.HAND);
                sixThousand.setFont(biggerBodyFont);
                    sixThousand.setToggleGroup(btnGroup);
                        sixThousand.setCursor(Cursor.HAND);
                sevenThousand.setFont(biggerBodyFont);
                    sevenThousand.setToggleGroup(btnGroup);
                        sevenThousand.setCursor(Cursor.HAND);
                eightThousand.setFont(biggerBodyFont);
                    eightThousand.setToggleGroup(btnGroup);
                        eightThousand.setCursor(Cursor.HAND);
                nineThousand.setFont(biggerBodyFont);
                    nineThousand.setToggleGroup(btnGroup);
                        nineThousand.setCursor(Cursor.HAND);
                tenThousand.setFont(biggerBodyFont);
                    tenThousand.setToggleGroup(btnGroup);
                        tenThousand.setCursor(Cursor.HAND);
                otherAmount.setFont(biggerBodyFont);
                    otherAmount.setToggleGroup(btnGroup);
                        otherAmount.setCursor(Cursor.HAND);

                otherAmountTxtField.visibleProperty().bind(otherAmount.selectedProperty());
                otherAmountTxtField.setTextFormatter(new TextFormatter<String>(doubleFilter));


                centerGridPane.setHgap(50);
                centerGridPane.setVgap(20);
                centerGridPane.setAlignment(Pos.CENTER);
                    centerGridPane.add(oneThousand, 0, 0, 1, 1);
                    centerGridPane.add(twoThousand, 0, 1, 1, 1);
                    centerGridPane.add(threeThousand, 0, 2, 1, 1);
                    centerGridPane.add(fourThousand, 0, 3, 1, 1);
                    centerGridPane.add(fiveThousand, 1, 0, 1, 1);
                    centerGridPane.add(sixThousand, 1, 1, 1, 1);
                    centerGridPane.add(sevenThousand, 1, 2, 1, 1);
                    centerGridPane.add(eightThousand, 1, 3, 1, 1);
                    centerGridPane.add(nineThousand, 2, 0, 1, 1);
                    centerGridPane.add(tenThousand, 2, 1, 1, 1);
                    centerGridPane.add(otherAmount, 2, 2, 1, 1);
                    centerGridPane.add(otherAmountTxtField, 2, 3, 1, 1);

            Button withdrawBtn = new Button("Withdraw");
            setCommonButtonProperties(withdrawBtn, true);
            withdrawBtn.setDisable(true);

            // listens if radio buttons are selected or not to whether disable withdrawBtn or not
            // also includes the "Other Amount" choice to keep the button disabled until user typed amount in textfield
            btnGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                    if(otherAmount.isSelected()) {
                        withdrawBtn.disableProperty().bind(
                                Bindings.isEmpty(otherAmountTxtField.textProperty()));
                    } else {
                        // makes sure that btn unbinds to the textfield if other choice is selected
                        withdrawBtn.disableProperty().unbind();
                        if (withdrawBtn.isDisabled()) {
                            withdrawBtn.setDisable(false);
                        }
                    }
                }
            });

            // ------ Button Action Events
            withdrawBtn.setOnAction(event -> {
                // basically gets the text of the choice, if it's "other amount", gets the input from textfield
                RadioButton selectedRadioButton  = (RadioButton) btnGroup.getSelectedToggle();
                String toggledButtonAmount = selectedRadioButton.getText();
                if (toggledButtonAmount.equals(otherAmount.getText())) {
                    withdrawBtnEvent(otherAmountTxtField.getText());
                } else {
                    withdrawBtnEvent(toggledButtonAmount);
                }
            });


                // Add Node to VBox
                withdrawBtnPlacement.setAlignment(Pos.BOTTOM_CENTER);
                withdrawBtnPlacement.getChildren().add(withdrawBtn);
                // end of adding node to VBox

            // centerPane properties
            Insets padding = new Insets(25, 160, 0, 30);
            centerPane.setSpacing(20.0);
            centerPane.setPadding(padding);
            centerPane.setPrefWidth(scene.getWidth());

                // Add Nodes to centerPane ----------------------------------------------------------
                centerPane.getChildren().clear(); // clear first before adding designated node
                centerPane.getChildren().addAll(setErrorTexts(), centerGridPane, withdrawBtnPlacement);
                // end of adding nodes to centerPane ------------------------------------------------


        // Nodes of Left Region of root

            // ------ Back Button Action Events
            backBtn.setOnAction(event -> backBtnEvent());

            // leftPane properties
            leftPane.setPadding(new Insets(20, 40, 0, 40));

                // Add Nodes to leftPane ------------------------------------------------------------
                leftPane.getChildren().clear(); // clear first before add the designated node
                leftPane.getChildren().add(setBackBtnProperties());
                // end of adding nodes to leftPane --------------------------------------------------

        // ------ Set up the 4 regions of border pane
        root.setCenter(centerPane);
        root.setLeft(leftPane);
        root.setRight(null);

        return scene;
    }

    public Scene checkBalancePane() {

        /* -----------------------------------------------
         * Scene displays after user clicked Check Balance Button
         * -----------------------------------------------
         * Check Balance Pane consists of
         *  - topPane with rectangle80 (80px height) and censored account number
         *  - centerPane for two button display
         *      1. Show Balance on Screen Button
         *      2. Print Balance Receipt Button
         *  - leftPane with left Arrow Button
         */

        // Nodes of Center Region of root

        Button balanceScreenBtn = new Button("Show Balance on Screen");
        Button balanceReceiptBtn = new Button("Prince Balance Receipt");
                setMainButtonsProperties(balanceScreenBtn);
                setMainButtonsProperties(balanceReceiptBtn);

            // ------ set Button Action Events
            balanceScreenBtn.setOnAction(event -> balanceScreenBtnEvent());
            balanceReceiptBtn.setOnAction(event -> balanceReceiptBtnEvent());


            // centerPane properties
            Insets padding = new Insets(50, 290, 30, 170);
            centerPane.setSpacing(20.0);
            centerPane.setPadding(padding);

            // Add Nodes to centerPane ----------------------------------------------------------
            centerPane.getChildren().clear(); // clear first before adding designated node
            centerPane.getChildren().addAll(balanceScreenBtn, balanceReceiptBtn);
            // end of adding nodes to centerPane ------------------------------------------------

        // Nodes of Left Region of root

            // ------ Back Button Action Events
            backBtn.setOnAction(event -> backBtnEvent());

            // leftPane properties
            leftPane.setPadding(new Insets(20, 40, 0, 40));

                // Add Nodes to leftPane ------------------------------------------------------------
                leftPane.getChildren().clear(); // clear first before add the designated node
                leftPane.getChildren().add(setBackBtnProperties());
                // end of adding nodes to leftPane --------------------------------------------------

        // ------ Set up the 4 regions of border pane
        root.setCenter(centerPane);
        root.setLeft(leftPane);
        root.setRight(null);

        return scene;
    }

    public Scene screenBalancePane(String displayBalance) {
        /* -----------------------------------------------
         * Scene displays after user clicked "Show Balance on Screen" Button on Check Balance Pane
         * -----------------------------------------------
         * Screen Balance Pane consists of
         *  - topPane with rectangle80 (80px height) and censored account number
         *  - centerPane text display of amount balance
         *      1. Show Balance on Screen Button
         *      2. Print Balance Receipt Button
         *  - leftPane with left Arrow Button
         */


        // Nodes of Center Region of root

        Text currentBalanceText = new Text("Current Balance");
        Text accountBalance = new Text("₱" + displayBalance);
            currentBalanceText.setFont(titleFont);
            accountBalance.setFont(titleFont2);

            // centerPane properties
            Insets padding = new Insets(50, 280, 0, 150);
            centerPane.setSpacing(20.0);
            centerPane.setPadding(padding);
            centerPane.setPrefWidth(scene.getWidth());
            centerPane.setAlignment(Pos.TOP_CENTER);


                // Add Nodes to centerPane ----------------------------------------------------------
                centerPane.getChildren().clear(); // clear first before adding designated node
                centerPane.getChildren().addAll(currentBalanceText, accountBalance);
                // end of adding nodes to centerPane ------------------------------------------------

        // Nodes of Left Region of root

            // ------ Back Button Action Events
            backBtn.setOnAction(event -> backBtnEvent());

            // leftPane properties
            leftPane.setPadding(new Insets(20, 40, 0, 40));

                // Add Nodes to leftPane ------------------------------------------------------------
                leftPane.getChildren().clear(); // clear first before add the designated node
                leftPane.getChildren().add(setBackBtnProperties());
                // end of adding nodes to leftPane --------------------------------------------------

        // ------ Set up the 4 regions of border pane
        root.setCenter(centerPane);
        root.setLeft(leftPane);
        root.setRight(null);

        return scene;
    }

    public Scene messagePane(String type) {
        /* -----------------------------------------------
         * Scene displays after user has confirmed transaction or printed account balance
         * -----------------------------------------------
         * Message Pane consists of
         *  - topPane with rectangle80 (80px height) and censored account number
         *  - centerPane for text display
         *      - Text Display would either be:
         *          1. "Processing..." title only or
         *          2. "Transaction Sucessful." or "Printing Successful." with a small text below them
         */

        // Nodes of Center Region of root

        Text messageDisplay = new Text();
        Text messageLogOffAutomatically = new Text("will be logged out automatically...");
        Text pleaseRememberMessage = new Text("Please remember your account Number"); // this text is for only on registration part
        Text displayAccountNum = new Text(); // this text is for only on registration part
        Button loginFormBtn = new Button("Back to Login Form");
            messageDisplay.setFont(titleFont);
            messageLogOffAutomatically.setFont(smallFont);
                setCommonButtonProperties(loginFormBtn, true);
                loginFormBtn.setPrefWidth(180);

                // ------ Button Action Events
                loginFormBtn.setOnAction(actionEvent -> logOutBtnEvent());

            // centerPane properties
            Insets padding = new Insets(50, 0, 0, 0);
            centerPane.setSpacing(20.0);
            centerPane.setPadding(padding);
            centerPane.setPrefWidth(scene.getWidth());
            centerPane.setAlignment(Pos.TOP_CENTER);

            // The nodes to be added in centerPane will be determined based on what type of
            // message to be displayed
            switch (type) {
                case "process":
                    messageDisplay.setText("Processing...");

                    // Add Nodes to centerPane ----------------------------------------------------------
                    centerPane.getChildren().clear(); // clear first before adding designated node
                    centerPane.getChildren().addAll(messageDisplay);
                    // end of adding nodes to centerPane ------------------------------------------------
                    break;
                case "transactionSuccess":
                    messageDisplay.setText("Transaction Successful.");

                    // Add Nodes to centerPane ----------------------------------------------------------
                    centerPane.getChildren().clear(); // clear first before adding designated node
                    centerPane.getChildren().addAll(messageDisplay, messageLogOffAutomatically);
                    // end of adding nodes to centerPane ------------------------------------------------
                    break;
                case "printSuccess":
                    messageDisplay.setText("Printing Successful.");

                    // Add Nodes to centerPane ----------------------------------------------------------
                    centerPane.getChildren().clear(); // clear first before adding designated node
                    centerPane.getChildren().addAll(messageDisplay, messageLogOffAutomatically);
                    // end of adding nodes to centerPane ------------------------------------------------
                    break;
                case "registerSuccess":
                    pleaseRememberMessage.setFont(biggerBodyFont);
                    displayAccountNum.setFont(biggerBodyFont);

                    messageDisplay.setText("Thank you for Registering to SubwayBank.");
                    messageLogOffAutomatically.setText(" ");
                    displayAccountNum.setText(mainDatabase.loggedAccount);

                    // Add Nodes to centerPane ----------------------------------------------------------
                    centerPane.getChildren().clear(); // clear first before adding designated node
                    centerPane.getChildren().addAll(messageDisplay, pleaseRememberMessage, displayAccountNum, loginFormBtn);
                    // end of adding nodes to centerPane ------------------------------------------------
                    break;
            }

        // ------ Set up the 4 regions of border pane
        root.setCenter(centerPane);
        root.setRight(null);
        root.setLeft(null);

        return scene;
    }

    public Scene confirmPane(char type, double amount) {
        /* -----------------------------------------------
         * Scene displays after user has clicked deposit or withdraw button to confirm the transaction
         * -----------------------------------------------
         * Confirm Pane consists of
         *  - topPane with rectangle80 (80px height) and censored account number
         *  - centerPane for displaying the amount of confirmation and the two buttons to confirm or cancel
         */

        // Nodes of Center Region of root
        Text confirmTransaction = new Text();
            if (type == 'd') // DEPOSIT TYPE
                confirmTransaction.setText("Confirm Deposit Transaction?");
            else if (type == 'w') // WITHDRAW TYPE
                confirmTransaction.setText("Confirm Withdraw Transaction?");
        Text amountTransaction = new Text("₱"+String.valueOf(amount));
            confirmTransaction.setFont(titleFont);
            amountTransaction.setFont(titleFont2);

        // used to place confirm and cancel buttons side by side
        HBox confirmButtons = new HBox();

        Button confirmBtn = new Button("Confirm");
            confirmBtn.setDefaultButton(true);
            confirmBtn.setFont(bodyFont);
                setCommonButtonProperties(confirmBtn, true);

        Button cancelBtn = new Button("Cancel");
            cancelBtn.setCancelButton(true);
            cancelBtn.setFont(bodyFont);
                setCommonButtonProperties(cancelBtn, false);

            // --------- Button Action Events
            confirmBtn.setOnAction(event -> confirmBtnEvent(type, amount));
            cancelBtn.setOnAction(event -> cancelBtnEvent());

                // Add nodes to HBox
                confirmButtons.getChildren().addAll(confirmBtn, cancelBtn);
                confirmButtons.setSpacing(10);
                confirmButtons.setAlignment(Pos.BOTTOM_CENTER);
                // end of add nodes to HBox

            // centerPane properties
            Insets padding = new Insets(50, 0, 0, 0);
            centerPane.setSpacing(20.0);
            centerPane.setPadding(padding);
            centerPane.setPrefWidth(scene.getWidth());
            centerPane.setAlignment(Pos.TOP_CENTER);

                // Add Nodes to centerPane ----------------------------------------------------------
                centerPane.getChildren().clear(); // clear first before adding designated node
                centerPane.getChildren().addAll(confirmTransaction, amountTransaction, confirmButtons);
                // end of adding nodes to centerPane ------------------------------------------------

        // ------ Set up the 4 regions of border pane
        root.setCenter(centerPane);
        root.setRight(null);
        root.setLeft(null);

        return scene;
    }

    public Scene registerPane() {
        /* -----------------------------------------------
         * Scene displays when user clicks on small text "Create Account" in LoginPane
         * Purpose of this scene is for user to create a new account
         * -----------------------------------------------
         * Register Pane consists of
         * - topPane with rectangle100 (100px height) and name of the bank
         * - centerPane
         *      text fields needed on having new account
         */

        // Nodes for Center Region of root
        // GridPane used to layout textfields at center of window
        GridPane centerGridPane = new GridPane();

            // nodes for GridPane
            Label govId = new Label("Government ID:* ");
            Label firstName = new Label("First Name:* ");
            Label lastName = new Label("Last Name:* ");
            Label pin = new Label("PIN (6 Digits): ");
            Label confirmPin = new Label("Confirm PIN: ");
            TextField govIdTxtField = new TextField("");
            TextField firstNameTxtField = new TextField("");
            TextField lastNameTxtField = new TextField("");
            PasswordField pinTxtField = new PasswordField();
            PasswordField confirmPinTxtField = new PasswordField();
                //Label address = new Label("Address:* ");
                //TextField addressTxtField = new TextField("Register not finished yet");

            govId.setFont(bodyFont);
                govIdTxtField.setFont(bodyFont);
                govIdTxtField.setTextFormatter(new TextFormatter<Object>(digitFilter));
            firstName.setFont(bodyFont);
                firstNameTxtField.setFont(bodyFont);
            lastName.setFont(bodyFont);
                lastNameTxtField.setFont(bodyFont);
            pin.setFont(bodyFont);
                pinTxtField.setFont(bodyFont);
                pinTxtField.setTextFormatter(new TextFormatter<Object>(digitFilter));
            confirmPin.setFont(bodyFont);
                confirmPinTxtField.setFont(bodyFont);
                confirmPinTxtField.setTextFormatter(new TextFormatter<Object>(digitFilter));
                /*address.setFont(bodyFont);
                    addressTxtField.setFont(bodyFont);*/

            centerGridPane.setHgap(40);
            centerGridPane.setVgap(10);
            centerGridPane.setAlignment(Pos.CENTER);
                // Add the nodes to the GridPane (All labels are on 1st col, all textfields on 2nd col)
                centerGridPane.add(govId, 0, 0, 1, 1);
                centerGridPane.add(firstName, 0, 1, 1, 1);
                centerGridPane.add(lastName, 0, 2, 1, 1);
                centerGridPane.add(pin, 0, 3, 1, 1);
                centerGridPane.add(confirmPin, 0, 4, 1, 1);
                centerGridPane.add(govIdTxtField, 1, 0, 1, 1);
                centerGridPane.add(firstNameTxtField, 1, 1, 1, 1);
                centerGridPane.add(lastNameTxtField, 1, 2, 1, 1);
                centerGridPane.add(pinTxtField, 1, 3, 1, 1);
                centerGridPane.add(confirmPinTxtField, 1, 4, 1, 1);
                    //centerGridPane.add(address, 0, 3, 1, 1);
                    //centerGridPane.add(addressTxtField, 1, 3, 1, 1);

            Text loginNow = new Text("Already have an account? Log in.");
            Button registerBtn = new Button("Open Account");
                    loginNow.setFont(smallFont);
                    loginNow.setCursor(Cursor.HAND);
                    loginNow.setFill(Color.rgb(5,55,130));

                    setCommonButtonProperties(registerBtn, true);
                    // properties for disabling button unless all textfields are filled
                    registerBtn.disableProperty().bind(Bindings.isEmpty(govIdTxtField.textProperty()));
                    registerBtn.disableProperty().bind(Bindings.isEmpty(firstNameTxtField.textProperty()));
                    registerBtn.disableProperty().bind(Bindings.isEmpty(lastNameTxtField.textProperty()));
                    registerBtn.disableProperty().bind(Bindings.isEmpty(pinTxtField.textProperty()));
                    registerBtn.disableProperty().bind(Bindings.isEmpty(confirmPinTxtField.textProperty()));

                    // --------- Button Action Events (including small font)
                        // Event brings the user to another pane
                        // that displays a textfield which asks user to input initial deposit
                    registerBtn.setOnAction(event ->
                            registerBtnEvent(
                                    govIdTxtField.getText(),
                                    firstNameTxtField.getText(),
                                    lastNameTxtField.getText(),
                                    pinTxtField.getText(),
                                    confirmPinTxtField.getText()));
                    loginNow.setOnMouseClicked(event -> loginPane());

            // centerPane properties
            Insets padding = new Insets(30, 0, 0, 0);

            centerPane.setSpacing(10.0);
            centerPane.setPadding(padding);
            centerPane.setPrefWidth(scene.getWidth());
            centerPane.setAlignment(Pos.TOP_CENTER);

                // Add Nodes to centerPane ----------------------------------------------------------
                centerPane.getChildren().clear(); // clear first before adding designated node
                centerPane.getChildren().addAll(setErrorTexts(), centerGridPane, registerBtn, loginNow);
                // end of adding nodes to centerPane ------------------------------------------------

        // ------ Set up the 4 regions of border pane
        root.setCenter(centerPane);
        root.setRight(null);
        root.setLeft(null);

        return scene;
    }

    public Scene initialDepositRegisterPane(String inputgovID, String inputfirstname, String inputlastname, String inputpin) {
        /* -----------------------------------------------
         * Scene displays to ask user initial deposit
         * -----------------------------------------------
         * initial deposit Register Pane consists of
         *  - topPane with rectangle80 (80px height) and censored account number
         *  - centerPane with textfield for user to enter initial deposit amount
         */

        // Nodes for Center Region of root

        // This VBox pane will align button placement on bottom center of CenterPane
        VBox depositBtnPlacement = new VBox();

        // centerPane nodes and additional properties
        Label depositLabel = new Label("Initial Deposit: (min. of ₱3,000.00)");
        TextField depositText = new TextField();
        Text loginNow = new Text("Already have an account? Log in.");
        Button initialDepositBtn = new Button("Register");
            depositLabel.setFont(bodyFont);
            depositText.setFont(bodyFont);
            loginNow.setFont(smallFont);
            loginNow.setCursor(Cursor.HAND);
            loginNow.setFill(Color.rgb(5,55,130));
                setCommonButtonProperties(initialDepositBtn, true);

            depositText.setPrefHeight(40);
            depositText.setTextFormatter(new TextFormatter<String>(doubleFilter));

            initialDepositBtn.disableProperty().bind(Bindings.isEmpty(depositText.textProperty()));

                // ------ Button Action Events
                initialDepositBtn.setOnAction(event ->
                        initialDepositBtnEvent(depositText.getText(),
                                inputgovID, inputfirstname, inputlastname, inputpin));
                loginNow.setOnMouseClicked(event -> loginPane());

                // Add Node to VBox
                depositBtnPlacement.setAlignment(Pos.BOTTOM_CENTER);
                depositBtnPlacement.setSpacing(15);
                depositBtnPlacement.getChildren().addAll(initialDepositBtn, loginNow);
                // end of adding node to VBox

            // centerPane properties
            Insets padding = new Insets(25, 260, 0, 280);
            centerPane.setSpacing(15.0);
            centerPane.setPadding(padding);
            centerPane.setPrefWidth(scene.getWidth());
            centerPane.setAlignment(Pos.TOP_LEFT);

                // Add Nodes to centerPane ----------------------------------------------------------
                centerPane.getChildren().clear(); // clear first before adding designated node
                centerPane.getChildren().addAll(setErrorTexts(), depositLabel, depositText, depositBtnPlacement);
                // end of adding nodes to centerPane ------------------------------------------------

        // ------ Set up the 4 regions of border pane
        root.setCenter(centerPane);
        root.setRight(null);
        root.setLeft(null);


        return scene;
    }

    public Scene registerConfirmPane(double inputinitialDeposit, String inputgovID, String inputfirstname, String inputlastname, String inputpin) {
        /* -----------------------------------------------
         * Scene displays to ask user initial deposit
         * -----------------------------------------------
         * initial deposit Register Pane consists of
         *  - topPane with rectangle80 (80px height) and censored account number
         *  - centerPane displays all user input for confirmation
         */

        // Nodes for Center Region of root
        // GridPane used to layout textfields at center of window
        GridPane centerGridPane = new GridPane();

        Text pleaseConfirmTxt = new Text("Please Confirm your Details to Open Account.");
            pleaseConfirmTxt.setFont(biggerBodyFont);

            // nodes for GridPane
            Label govIDLabel = new Label("Government ID: ");
            Label firstNameLabel = new Label("First Name: ");
            Label lastNameLabel = new Label("Last Name: ");
            Label initialDepositLabel = new Label("Initial Deposit: ");
            TextField govID = new TextField(inputgovID);
            TextField firstName = new TextField(inputfirstname);
            TextField lastName = new TextField(inputlastname);
            TextField initialDeposit = new TextField("₱" + String.valueOf(inputinitialDeposit));
                govIDLabel.setFont(bodyFont);
                firstNameLabel.setFont(bodyFont);
                lastNameLabel.setFont(bodyFont);
                initialDepositLabel.setFont(bodyFont);
                govID.setFont(bodyFont);
                firstName.setFont(bodyFont);
                lastName.setFont(bodyFont);
                initialDeposit.setFont(bodyFont);
                govID.setEditable(false);
                firstName.setEditable(false);
                lastName.setEditable(false);
                initialDeposit.setEditable(false);

                centerGridPane.setHgap(10);
                centerGridPane.setVgap(10);
                centerGridPane.setAlignment(Pos.CENTER);
                    // Add the nodes to the GridPane (All labels are on 1st col, all user input display on 2nd col)
                    centerGridPane.add(govIDLabel, 0, 0, 1, 1);
                    centerGridPane.add(firstNameLabel, 0, 1, 1, 1);
                    centerGridPane.add(lastNameLabel, 0, 2, 1, 1);
                    centerGridPane.add(initialDepositLabel, 0, 3, 1, 1);
                    centerGridPane.add(govID, 1, 0, 1, 1);
                    centerGridPane.add(firstName, 1, 1, 1, 1);
                    centerGridPane.add(lastName, 1, 2, 1, 1);
                    centerGridPane.add(initialDeposit, 1, 3, 1, 1);
        // used to place confirm and cancel buttons side by side
        HBox confirmButtons = new HBox();

        Button confirmBtn = new Button("Confirm");
            confirmBtn.setDefaultButton(true);
            confirmBtn.setFont(bodyFont);
            setCommonButtonProperties(confirmBtn, true);

        Button cancelBtn = new Button("Cancel");
            cancelBtn.setCancelButton(true);
            cancelBtn.setFont(bodyFont);
            setCommonButtonProperties(cancelBtn, false);

            // --------- Button Action Events
            confirmBtn.setOnAction(event -> confirmRegisterBtnEvent(inputinitialDeposit,
                    inputgovID,
                    inputfirstname,
                    inputlastname,
                    inputpin
            ));
            cancelBtn.setOnAction(event -> logOutBtnEvent());

                // Add nodes to HBox
                confirmButtons.getChildren().addAll(confirmBtn, cancelBtn);
                confirmButtons.setSpacing(10);
                confirmButtons.setAlignment(Pos.BOTTOM_CENTER);
                // end of add nodes to HBox

            // centerPane properties
            Insets padding = new Insets(25, 0, 0, 0);
            centerPane.setSpacing(20.0);
            centerPane.setPadding(padding);
            centerPane.setPrefWidth(scene.getWidth());
            centerPane.setAlignment(Pos.TOP_CENTER);

                // Add Nodes to centerPane ----------------------------------------------------------
                centerPane.getChildren().clear(); // clear first before adding designated node
                centerPane.getChildren().addAll(pleaseConfirmTxt, centerGridPane, confirmButtons);
                // end of adding nodes to centerPane ------------------------------------------------

        // ------ Set up the 4 regions of border pane
        root.setCenter(centerPane);
        root.setRight(null);
        root.setLeft(null);

        return scene;
    }


    // -------------------------------------------------------------------------------------------
    // BUTTON EVENT METHODS BELOW

    private void loginBtnEvent(String accountNumber, String PIN) {
        if (mainDatabase.checkAccount(accountNumber, PIN)) {
            mainPane(accountNumber);
            mainDatabase.loggedAccount = accountNumber;
            errorText.setText(" ");
        } else {
            errorText.setText("Wrong Account Number or PIN");
        }
    }

    private void logOutBtnEvent() {
        loginPane();
        mainDatabase.loggedAccount = "00000000";
    }

    private void initialDepositBtnEvent(String userInitialDeposit, String usergovID, String userfirstname, String userlastname, String userpin) {
        try {
            if (Double.parseDouble(userInitialDeposit) < 3000) {
                errorText.setText("Initial Deposit's minimum is ₱3,000.00");
            } else {
                double initialDepositAmount = Double.parseDouble(userInitialDeposit);
                registerConfirmPane(initialDepositAmount, usergovID, userfirstname, userlastname, userpin);
                errorText.setText(" ");
            }
        } catch(Exception e) {
            errorText.setText("Invalid input");
        }
    }

    private void depositBtnEvent(String userInputAmount) {
        try {
            if (Double.parseDouble(userInputAmount) < 1) {
                errorText.setText("Initial Deposit minimum is ₱1.00");
            } else {
                double depositAmount = Double.parseDouble(userInputAmount);
                confirmPane('d', depositAmount);
                errorText.setText(" ");
            }
        } catch(Exception e) {
            errorText.setText("Invalid input");
        }
    }

    private void withdrawBtnEvent(String userSelectedAmount) {
        try {
            double withdrawAmount = 0;
            //  Since the method of withdrawing is choosing a radio button, switch case will
            //    be used to determine what radio button user has chosen based on its first three
            //    character in a string
            if (userSelectedAmount.charAt(0) == '₱') {
                switch (userSelectedAmount.substring(0,3)) {
                    case "₱1," -> withdrawAmount = 1000;
                    case "₱2," -> withdrawAmount = 2000;
                    case "₱3," -> withdrawAmount = 3000;
                    case "₱4," -> withdrawAmount = 4000;
                    case "₱5," -> withdrawAmount = 5000;
                    case "₱6," -> withdrawAmount = 6000;
                    case "₱7," -> withdrawAmount = 7000;
                    case "₱8," -> withdrawAmount = 8000;
                    case "₱9," -> withdrawAmount = 9000;
                    case "₱10" -> withdrawAmount = 10000;
                }
                // Check if the amount is greater than the account balance, which will provide an error message if so
                if (Double.parseDouble(mainDatabase.getBalance()) < withdrawAmount) {
                    errorText.setText("Amount is greater than your account balance");
                } else {
                    confirmPane('w', withdrawAmount);
                    errorText.setText(" ");
                }
            } else {
                // This will run if user has chosen the Other Amount radio button
                withdrawAmount = Double.parseDouble(userSelectedAmount);
                if (Double.parseDouble(userSelectedAmount) < 1) {
                    errorText.setText("Amount should be more than ₱1.00");
                } else if(Double.parseDouble(mainDatabase.getBalance()) < withdrawAmount) {
                    errorText.setText("Amount is greater than your account balance");
                } else {
                    confirmPane('w', withdrawAmount);
                    errorText.setText(" ");
                }

            }

        } catch(Exception e) {
            errorText.setText("Invalid input");
        }
    }

    private void balanceScreenBtnEvent() {
        screenBalancePane(mainDatabase.getBalance());
    }

    private void balanceReceiptBtnEvent() {
        confirmBtnEvent();
    }

    private void backBtnEvent() {
        mainPane();
        errorText.setText(" ");
    }

    // This provides a few second delay before going back to loginPane (after transaction)
    Timeline logoutDelay = new Timeline(
        new KeyFrame(Duration.seconds(6),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        logOutBtnEvent();
                    }
                }
        ));
    private void confirmBtnEvent() {
        // event for only printing the balance through receipt

        messagePane("process"); // only displays the "Processing..." text

        // delays the display of "______ Successful" text after displaying processing... text
        Timeline processDelay = new Timeline( new KeyFrame(Duration.seconds(3),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                            messagePane("printSuccess");
                            BankingTransaction.receipt(mainDatabase.loggedAccount, "CHECK BALANCE", mainDatabase.getBalance());

                    }
                }
            ));
        processDelay.setCycleCount(1);
        processDelay.play();

        logoutDelay.setCycleCount(1);
        logoutDelay.play();
    }

    private void confirmBtnEvent(char type, double amount) {
        // event for deposit or withdraw transaction

        messagePane("process"); // only displays the "Processing..." text

        if (type == 'd')
            mainDatabase.accountDeposit(amount);
        else if (type == 'w')
            mainDatabase.accountWithdraw(amount);

        // delays the display of "______ Successful" text after displaying processing... text
        Timeline processDelay = new Timeline(new KeyFrame(Duration.seconds(3),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        messagePane("transactionSuccess");
                        if (type == 'd') {
                            BankingTransaction.receipt(mainDatabase.loggedAccount, "DEPOSIT", String.valueOf(amount));
                        } else {
                            BankingTransaction.receipt(mainDatabase.loggedAccount, "WITHDRAW", String.valueOf(amount));
                        }
                    }
                }
            ));
        processDelay.setCycleCount(1);
        processDelay.play();

        logoutDelay.setCycleCount(1);
        logoutDelay.play();
    }

    private void confirmRegisterBtnEvent(double initialdeposit, String govID, String firstname, String lastname, String stringPin) {
        // event for registration

        messagePane("process");
        HashMap<String, String> accountName = new HashMap<>();
        accountName.put("firstname", firstname);
        accountName.put("lastname", lastname);
        long pin = Long.parseLong(stringPin);
        String newAccountNumber = mainDatabase.generateAccountNumber();
        mainDatabase.openAccount(newAccountNumber, govID, accountName, initialdeposit, pin);

        Timeline processDelay = new Timeline(
                new KeyFrame(Duration.seconds(3),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                mainDatabase.loggedAccount = newAccountNumber;
                                messagePane("registerSuccess");
                            }
                        }
                ));
        processDelay.setCycleCount(1);
        processDelay.play();

    }


    private void registerBtnEvent(String govID, String firstname, String lastname, String pin, String confirmPIN) {
        // Conditions before registration
        // 1. enter pin twice
        // 2. pin must be 6 digits only
        // 2. government id must be unique

        if (!mainDatabase.checkGovIdAlreadyExisting(govID)) {
            if (pin.length() == 6 && confirmPIN.length() == 6) {
                if (pin.equals(confirmPIN)) {
                    initialDepositRegisterPane(govID,
                            firstname,
                            lastname,
                            pin);
                } else {
                    errorText.setText("Please confirm your PIN correctly.");
                }
            } else {
                errorText.setText("PIN must be 6 digits only.");
            }
        } else {
            errorText.setText("Government ID already registered.");
        }
        //confirmRegisterPane(govID, firstname, lastname);
    }

    private void cancelBtnEvent() {
        mainPane();
    }


}

/* NOTES:
   Additional Codes for Checking Transactions (will be used soon)

// NODES AND PROPERTIES TO BE USED IN MAINPANE()
   Button toCheckTransactionBtn = new Button("Check Transaction");
           setMainButtonsProperties(toCheckTransactionBtn);
   toCheckTransactionBtn.setOnAction(event -> checkTransactionPane());


// CHECK TRANSACTION SCENE
    public Scene checkTransactionPane() {
    TableView<BankAccount> transactionTable = new TableView();
    ObservableList<BankAccount> listOfTransactions = (ObservableList<BankAccount>) mainDatabase.getArrayListTransaction();
        transactionTable.setItems(listOfTransactions);

                TableColumn<BankAccount, String> typeCol = new TableColumn<BankAccount, String>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        TableColumn<BankAccount, String> amountCol = new TableColumn<BankAccount, String>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory("amount"));

        transactionTable.getColumns().setAll(typeCol, amountCol);
        centerPane.getChildren().add(transactionTable);

        // ------ Node for the Left Region of root
        backBtn.setOnAction(event -> backBtnEvent());
        // padding left right 40 40
        leftPane.setPadding(new Insets(20, 40, 0, 40));
        leftPane.getChildren().clear(); // clear first before add the designated node
        leftPane.getChildren().add(setBackBtnProperties());

        // ------ Set up the 4 regions of border pane
        // Top Pane has already been set in mainPane w/ param
        root.setCenter(centerPane);
        root.setRight(null);
        root.setLeft(leftPane);

        return scene;
        }



 */

