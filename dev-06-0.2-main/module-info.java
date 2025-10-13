module cs151.spartantrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires com.mysql.cj.jdbc;

    opens cs151.spartantrack to javafx.fxml;
    opens cs151.spartantrack.controller to javafx.fxml;

    exports cs151.spartantrack;
    exports cs151.spartantrack.controller;
}