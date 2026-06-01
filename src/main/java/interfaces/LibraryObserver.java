package interfaces;

public interface LibraryObserver {
    // Questo metodo viene notificato a tutte le viste registrate quando i dati cambiano
    void onLibraryChanged();
}