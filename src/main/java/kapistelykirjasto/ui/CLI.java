package kapistelykirjasto.ui;

import kapistelykirjasto.domain.Application;
import kapistelykirjasto.domain.Entry;

import java.util.Scanner;

public class CLI implements UserInterface {

    private Application app;
    private IO io;

    public CLI(Application app, IO io) {
        this.app = app;
        this.io = io;
    }

    @Override
    public void run() {
        printWelcomeMessage();
        while (io.hasNextLine()) {
            String action = io.readLine("Anna toiminto: ");
            if (action.equals("X")) {
                io.print("suljetaan");
                break;
            } else if (action.equals("0")) {
                printActions();
            } else if (action.equals("1")) {
                addEntry();
            } else if (action.equals("2")) {
                getEntries();
            } else if (action.equals("3")) {
                deleteEntry();
            } else {
                io.print("epäkelpo toiminto");
            }
        }
    }

    private void printWelcomeMessage() {
        io.print("***********************************");
        io.print("* Tervetuloa käpistelykirjastoon! *");
        io.print("*                                 *");
        io.print("*  Toiminnot:                     *");
        io.print("*  - 0: tulosta valikko           *");
        io.print("*  - 1: lisää lukuvinkki          *");
        io.print("*  - 2: näytä lukuvinkit          *");
        io.print("*  - 3: poista lukuvinkki         *");
        io.print("*  - X: poistu sovelluksesta      *");
        io.print("***********************************");
        io.print("");
    }

    private void printActions() {
        io.print("- 0: tulosta valikko");
        io.print("- 1: lisää lukuvinkki");
        io.print("- 2: näytä lukuvinkit");
        io.print("- 3: poista lukuvinkki");
        io.print("- X: poistu sovelluksesta");
    }

    private void addEntry() {
        String typeOfEntryAdded = io.readLine("[1]: lisää kirja \n[2]: lisää video");
        if (typeOfEntryAdded.equals("1")) {
            addBook();
        } else if (typeOfEntryAdded.equals("2")) {
            addVideo();
        } else {
            io.print("epäkelpo toiminto");
        }
    }

    private void addBook() {
        String title = io.readLine("Syötä kirjan nimi:");
        String author = io.readLine("Syötä kirjan kirjoittaja:");
        String isbn = io.readLine("Syötä ISBN:");
        String comment = io.readLine("Syötä kommentti (vapaavalintainen):");

        if (app.createBook(title, comment, author, isbn)) {
            io.print("Lukuvinkki lisätty onnistuneesti");
        } else {
            io.print("Lukuvinkin lisääminen epäonnistui");
        }
    }

    private void addVideo() {
        String title = io.readLine("Syötä videon nimi:");
        String url = io.readLine("Syötä videon url-osoite:");
        String duration = io.readLine("Syötä videon kesto (vapaaehtoinen):");
        String comment = io.readLine("Syötä kommentti (vapaavalintainen):");

        if (app.createVideo(title, comment, url, duration)) {
            io.print("Lukuvinkki lisätty onnistuneesti");
        } else {
            io.print("Lukuvinkin lisääminen epäonnistui");
        }
    }

    private void getEntries() {

        if (app.getEntries().isEmpty()) {
            io.print("Ei lisättyjä lukuvinkkejä");
        } else {
            io.print("Lukuvinkit: ");
            String type = "";
            for (int i = 0; i < app.getEntries().size(); i++) {
                if (app.getEntries().get(i).getType() == Entry.Type.BOOK) {
                    type = "kirja";
                } else {
                    type = "video";
                }
                io.print(type + ": " + app.getEntries().get(i).getTitle());
            }
        }
    }

    private void deleteEntry() {

        String type = "";
        for (int i = 0; i < app.getEntries().size(); i++) {
            if (app.getEntries().get(i).getType() == Entry.Type.BOOK) {
                type = "kirja";
            } else {
                type = "video";
            }
            io.print(i + 1 + ". " + type + ": " + app.getEntries().get(i).getTitle());
        }
        String id = io.readLine("Syötä lukuvinkin numero, jonka haluat poistaa: ");
        int selectedNumber = Integer.valueOf(id);
    /*    int entryId = app.getEntries().get(selectedNumber-1).getId();

        if (this.app.deleteEntry(entryId) {
            io.print("Lukuvinkin poistaminen onnistui");
        } else {
            io.print("Lukuvinkin poistaminen ei onnistunut");
        }
    } */
    }

}

