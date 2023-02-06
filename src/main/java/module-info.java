module com.example.banking_transaction {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    opens com.example.banking_transaction to javafx.fxml;
    exports com.example.banking_transaction;
}