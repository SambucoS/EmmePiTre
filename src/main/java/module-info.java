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
    opens emmepitre.com.emmepitre to javafx.fxml;

    // LA RIGA CORRETTA: Apre il pacchetto models sia a Jackson che a JavaFX in un colpo solo
    opens models to com.fasterxml.jackson.databind, javafx.base;

    exports emmepitre.com.emmepitre;
}