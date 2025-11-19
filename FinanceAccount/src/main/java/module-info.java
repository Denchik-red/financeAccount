module den_n.financeaccount {
    requires javafx.controls;
    requires javafx.fxml;


    opens den_n.financeaccount to javafx.fxml;
    exports den_n.financeaccount;
}