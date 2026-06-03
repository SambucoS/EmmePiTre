module emmepitre.com.emmepitre {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires atlantafx.base;
    opens controllers to javafx.fxml;
    opens models to com.fasterxml.jackson.databind;
    opens emmepitre.com.emmepitre to javafx.fxml;
    exports emmepitre.com.emmepitre;
}