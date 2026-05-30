# Music Playlist Manager (MVC) - Gruppo 14

Applicazione desktop per la gestione di librerie musicali e la riproduzione simulata di playlist, sviluppata secondo la metodologia agile Scrum e fondata su principi rigorosi di ingegneria del software.

## Link Utili:
* **Bacheca Trello:** https://trello.com/b/eCa5wrH8/progettosadnostro
* **DoD Tracking:** https://docs.google.com/spreadsheets/d/1s4qMJCB3Hl-N1w0LTPqv4c8-tv-MiT8_BUWdmDgEYSY/edit?gid=2047320721#gid=2047320721
* **Product Backlog esteso:** https://docs.google.com/spreadsheets/d/1NBIS9MgPwVRjWRlEi3Rlg7k1KQAI5gZWGIWAH5KHkb8/edit?gid=0#gid=0
---

## 1. Visione Generale e Principi di Sviluppo

Il progetto è improntato alla massimizzazione dell'efficienza e alla riduzione della complessità non necessaria durante le fasi iniziali del ciclo di vita del software. Sono applicati i seguenti principi guida:

* **Principio YAGNI (You Aren't Gonna Need It):** Lo sviluppo è focalizzato sui requisiti strutturali e sulla simulazione logica della riproduzione. La gestione dei file audio fisici sul disco (MP3) e dei relativi percorsi assoluti è posticipata a iterazioni future, mantenendo il sistema scalabile.
* **Persistenza Leggera in JSON:** L'archiviazione dei metadati (brani, playlist, configurazioni) avviene tramite la serializzazione e deserializzazione di file in formato JSON utilizzando la libreria **Gson**. Ciò garantisce un salvataggio persistente e strutturato senza il sovraccarico architetturale di un database relazionale.

---

## 2. Architettura del Software (Pattern MVC)

L'applicazione adotta il pattern architetturale **Model-View-Controller (MVC)** per garantire la separazione degli interessi (*Separation of Concerns*), l'alta coesione dei componenti e il basso accoppiamento.

### 2.1 Scomposizione dei Livelli

* **MODEL (Dominio e Stato):** Incapsula i dati puri dell'applicazione e le regole di business.
  * **Entità di Dominio:** `Track` (metadati del brano), `Playlist` (aggregazione ordinata di tracce) e `Library` (registro centrale).
  * **PlayerSimulato:** Componente (`MediaPlayerContext`) che gestisce lo stato logico della riproduzione simulata (tempo corrente, traccia attiva), totalmente disaccoppiato dall'interfaccia.
  * **Persistenza JSON:** Modulo dedicato al caricamento e salvataggio locale dei dati strutturati.
* **VIEW (Interfaccia Utente):** Rappresenta la componente di presentazione passiva, sviluppata tramite la GUI **JavaFX**. Renderizza la tabella delle tracce, i tag visivi (es. *favourite*, *explicit*) e la barra di avanzamento sincronizzata del player.
* **CONTROLLER (Orchestratore):** Agisce da intermediario puro tra eventi visivi e logica di dominio. Comprende componenti specializzati (`PlaylistController`, `PlaybackController`, `UndoRedoCommandHandler`) che intercettano gli eventi della View e invocano le modifiche di stato sul Model.

---

## 3. Funzionalità Implementate (Product Backlog)

Il sistema supporta un set esteso di User Stories, prioritizzate secondo il Product Backlog di progetto:

* **Gestione Libreria:** Creazione, visualizzazione, modifica ed eliminazione delle tracce musicali. Assegnazione di tag visivi personalizzati.
* **Gestione Playlist:** Creazione, popolamento, rimozione selettiva ed eliminazione intera di playlist. Riarrangiamento dinamico delle tracce e generazione automatica basata su metadati (genere, anno, tag visivi).
* **Riproduzione Simulata:** Controlli core di Play, Pausa e Skip, con aggiornamento in tempo reale della UI e della barra di avanzamento.
* **Modalità Avanzate:** Supporto per logiche di coda sequenziali, casuali (Shuffle) e cicliche (Loop). Modifica strutturale della playlist durante la riproduzione in corso ("Live").
* **Storico Comandi:** Esecuzione di azioni di *Undo* e *Redo* per annullare o ripristinare variazioni applicate alle playlist.

---

## 4. Definition of Done (DoD)

Per garantire la stabilità del sistema, un task viene marcato come completato ("Done") esclusivamente se soddisfa tutti i seguenti criteri formali, validati tramite apposita matrice di tracciamento:

### I. Integrazione di Base
* **Compilazione:** Il codice sorgente compila correttamente, senza errori di sintassi o conflitti nell'ambiente di sviluppo.
* **Git Main:** Il codice è stato sottoposto a commit e caricato con successo sul repository remoto.

### II. Qualità della Codifica
* **Nomenclatura e Formattazione:** Applicazione rigorosa delle convenzioni Java (*camelCase* per variabili/metodi, *PascalCase* per classi) e delle norme di formattazione interna.
* **Assenza di Magic Numbers:** Qualsiasi costante o valore fisso è dichiarato tramite parametri espliciti (`public static final`).
* **Documentazione:** Presenza di commenti esplicativi e Javadoc per le interfacce e le API pubbliche.
* **Design Architetturale:** Rispetto sistematico del pattern MVC, senza violazioni dei confini logici (es. logica di business assente nella View).

### III. Testing Automatizzato
* **JUnit Auto:** Test unitari regolarmente implementati tramite il framework JUnit e resi eseguibili in maniera automatica.
* **Copertura:** I test validano le interfacce pubbliche e la logica core del Model.
* **Casi Limite:** Validazione approfondita su input anomali, valori nulli e casi limite dello spazio operativo.

### IV. Processo Agile (Scrum)
* **Code Review:** Revisione incrociata (Peer Review) condotta da almeno un secondo membro del team prima dell'integrazione.
* **Task Trello & Tempo Registrato:** Stato della bacheca aggiornato in tempo reale e ore effettive di effort debitamente consuntivate.

---

## 5. Strategia di Versionamento e Integrazione

Il repository adotta una strategia rigida per preservare l'integrità del software e favorire lo sviluppo parallelo del team:
* Il branch `main` rappresenta in modo esclusivo la versione stabile e rilasciabile del prodotto.
* Lo sviluppo avviene unicamente su branch di feature dedicati (es. `feature/US-1.1-creazione-traccia`).
* L'integrazione sul branch principale è subordinata all'apertura di una *Pull Request*, alla validazione tramite Code Review e all'esito positivo della suite di test automatizzati (JUnit).
