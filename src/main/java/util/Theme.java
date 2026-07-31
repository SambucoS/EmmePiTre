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
 *
 * @version 1.0
 */
public final class Theme {

    private static final String CSS = "/css/app.css";
    private static final String DARK_CLASS = "dark";

    private static boolean dark = false;

    private Theme() {
    }

    /**
     * Indica il tema attualmente attivo per l'intera applicazione.
     *
     * @return {@code true} se il tema scuro e' attivo, {@code false} se e' attivo quello chiaro
     */
    public static boolean isDark() {
        return dark;
    }

    /**
     * Applica a una scena il foglio di stile globale e la classe del tema
     * corrente. Va invocato su ogni nuova scena (finestra principale, modali,
     * player) cosi' che segua sempre il tema attivo.
     *
     * @param scene la {@link Scene} a cui applicare lo stylesheet e il tema
     */
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

    /**
     * Inverte il tema corrente (chiaro/scuro) e lo applica alla scena indicata.
     *
     * @param scene la {@link Scene} su cui aggiornare la classe del tema
     * @return {@code true} se il tema risultante e' quello scuro
     */
    public static boolean toggle(Scene scene) {
        dark = !dark;
        applyClass(scene);
        return dark;
    }

    /**
     * Aggiunge o rimuove la classe CSS del tema scuro sul nodo root della
     * scena, in base allo stato corrente di {@link #dark}.
     *
     * @param scene la {@link Scene} il cui root va aggiornato
     */
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
