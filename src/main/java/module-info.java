module EmmePiTre {
    // Moduli base di JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires javafx.media;
    requires javafx.swing;

    // Librerie esterne rilevate dal tuo build (Jackson per i file JSON)
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    // Altre librerie nel tuo classpath (se le usi attivamente nel codice)
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires atlantafx.base;

    opens models to javafx.base, com.fasterxml.jackson.databind;
    // Esportazioni e Aperture per il Launcher
    exports emmepitre.com.emmepitre;
    opens emmepitre.com.emmepitre to javafx.graphics, javafx.fxml;

    // Apertura dei pacchetti per permettere a JavaFX di iniettare i nodi FXML
    opens controllers to javafx.fxml;
    // Se Jackson deve leggere/scrivere i tuoi modelli
}