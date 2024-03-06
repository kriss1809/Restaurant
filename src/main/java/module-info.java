module com.example.seminar1314finalizat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.seminar1314finalizat to javafx.fxml;
    exports com.example.seminar1314finalizat;

    exports com.example.seminar1314finalizat.controller;
    opens com.example.seminar1314finalizat.controller to javafx.fxml;
    opens com.example.seminar1314finalizat.domain to javafx.base;
}