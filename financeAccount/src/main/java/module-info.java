module den_n.financeaccount {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires jakarta.persistence;
    requires java.prefs;

    opens den_n.financeaccount.pages.connecrionSettings;
    opens den_n.financeaccount.module;
    opens den_n.financeaccount.pages.main;
    opens den_n.financeaccount.pages.accountInfo;
    opens den_n.financeaccount.pages.AddAccount;
    opens den_n.financeaccount.pages.newTransaction;
    opens den_n.financeaccount.pages.AddCategory;
    opens den_n.financeaccount.pages.renameAccountDialog;

    exports den_n.financeaccount;
    exports den_n.financeaccount.module;
    exports den_n.financeaccount.pages.main;
    exports den_n.financeaccount.pages.AddAccount;
    exports den_n.financeaccount.pages.AddCategory;
    exports den_n.financeaccount.pages.accountInfo;
    exports den_n.financeaccount.pages.connecrionSettings;
    exports den_n.financeaccount.pages.newTransaction;
    exports den_n.financeaccount.pages.renameAccountDialog;
    exports den_n.financeaccount.util;
}