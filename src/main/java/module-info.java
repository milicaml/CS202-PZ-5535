module com.pz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;


    opens com.pz to javafx.fxml;
    exports com.pz;
    exports com.pz.scenes;
    exports com.pz.game;
    exports com.pz.database;
    exports com.pz.networking;
    opens com.pz.scenes to javafx.fxml;
}