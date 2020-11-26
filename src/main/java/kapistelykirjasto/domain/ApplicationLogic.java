package kapistelykirjasto.domain;

import kapistelykirjasto.dao.Dao;

import java.util.ArrayList;

public class ApplicationLogic implements Application {
    
    private Dao dao;
    
    public ApplicationLogic(Dao dao) {
        this.dao = dao;
    }
    
    @Override
    public boolean createEntry(String name) {
        Entry entry = new Entry(name);
        if (name.length() == 0 || !this.dao.createEntry(entry)) {
        	return false;
        }
        return true;
    }

    @Override
    public boolean createBook(String title, String comment, String author, String ISBN) {
        System.out.println("adding book!");
        return true;
    }

    @Override
    public ArrayList<Entry> getEntries() {
        ArrayList<Entry>entries = new ArrayList<>();
        entries = this.dao.getEntries();

        return entries;
    }

    @Override
    public boolean deleteEntryBasedOnTitle(String title) {
        if (!this.dao.deleteEntryBasedOnTitle(title)) {
            return false;
        }
        return true;
    }
}