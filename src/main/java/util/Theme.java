package util;

import javafx.scene.Scene;

/**
 * Gestione centralizzata del tema chiaro/scuro dell'applicazione.
 *
 * Il tema e' realizzato con un unico foglio di stile (app.css) in cui i colori
 * sono "looked-up": ridefiniti sotto la classe "dark" sul nodo root. Attivare il
 * tema scuro significa quindi solo aggiungere la classe "dark" al root della scena.
 *
 * Lo stato (chiaro/scuro) e' condiviso da tutta l'app, cosi' anche le finestre
 * modali e il player aperti dopo lo switch rispettano il tema corrente.
 */
public final class Theme {

    private static final String CSS = "/css/app.css";
    private static final String DARK_CLASS = "dark";

    private static boolean dark = false;

    private Theme() {
    }

    public static boolean isDark() {
        return dark;
    }

    /** Applica a una scena il foglio di stile e la classe del tema corrente. */
    public static void apply(Scene scene) {
        if (scene == null) {
            return;
        }
        String css = Theme.class.getResource(CSS).toExternalForm();
        if (!scene.getStylesheets().contains(css)) {
            scene.getStylesheets().add(css);
        }
        applyClass(scene);
    }

    /** Inverte il tema su una scena e restituisce true se ora e' scuro. */
    public static boolean toggle(Scene scene) {
        dark = !dark;
        applyClass(scene);
        return dark;
    }

    private static void applyClass(Scene scene) {
        if (scene == null || scene.getRoot() == null) {
            return;
        }
        scene.getRoot().getStyleClass().remove(DARK_CLASS);
        if (dark) {
            scene.getRoot().getStyleClass().add(DARK_CLASS);
        }
    }
}
