package kapistelykirjasto.dao;

import java.io.*;
import java.sql.*;

import kapistelykirjasto.domain.Video;
import kapistelykirjasto.domain.Book;
import kapistelykirjasto.domain.Entry;

import org.junit.*;

import static org.junit.Assert.*;

public class SQLiteDaoTest {

    private SQLiteDao dao;
    private final File testDatabaseFile = new File("test_database.db");

    @Before
    public void setUp() throws SQLException, IOException {
        assertTrue(testDatabaseFile.createNewFile());
        this.dao = new SQLiteDao(testDatabaseFile.getAbsolutePath());
    }

    @After
    public void tearDown() {
        this.dao.close();
        assertTrue(testDatabaseFile.delete());
    }

    @Test
    public void constructorCreatesEntryTable() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + testDatabaseFile.getAbsolutePath());
        ResultSet tables = connection.getMetaData().getTables(null, null, null, null);

        boolean entryTableExists = false;
        while (tables.next()) {
            if (tables.getString("TABLE_NAME").equals("entry")) {
                entryTableExists = true;
            }
        }

        assertTrue(entryTableExists);
    }

    @Test
    public void createEntryCreatesRowInTableEntry() throws SQLException {
        this.dao.createEntry(new Entry("Test"));

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + testDatabaseFile.getAbsolutePath());
        Statement statement = connection.createStatement();
        ResultSet entries = statement.executeQuery("SELECT * FROM entry");

        assertTrue(entries.next());
        assertTrue(entries.getString("title").equals("Test"));

        entries.close();
        statement.close();
        connection.close();
    }

    @Test
    public void createBookCreatesRowInTableBook() throws SQLException {
        this.dao.createBook(new Book("Clean Code: A Handbook of Agile Software Craftsmanship",
                "comments here",
                "Robert Martin",
                "978-0132350884"));

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + testDatabaseFile.getAbsolutePath());
        Statement statement = connection.createStatement();
        ResultSet books = statement.executeQuery("SELECT * FROM book");

        assertTrue(books.next());
        assertTrue(books.getString("title").equals("Clean Code: A Handbook of Agile Software Craftsmanship"));

        books.close();
        statement.close();
        connection.close();
    }

    @Test
    public void createVideoCreatesRowInTableVideo() throws SQLException {
        this.dao.createVideo(new Video("Merge sort algorithm",
                "Vau! Miten visuaalista!",
                "https://www.youtube.com/watch?v=TzeBrDU-JaY",
                "7 min 34 sek"));

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + testDatabaseFile.getAbsolutePath());
        Statement statement = connection.createStatement();
        ResultSet videos = statement.executeQuery("SELECT * FROM video");

        assertTrue(videos.next());
        assertTrue(videos.getString("title").equals("Merge sort algorithm"));

        videos.close();
        statement.close();
        connection.close();
    }

    @Test
    public void createEntryReturnsFalseForDuplicateEntry() throws SQLException {
        this.dao.createEntry(new Entry("Test"));
        assertFalse(this.dao.createEntry(new Entry("Test")));
    }

    @Test
    public void getEntriesReturnsRightSizeList() throws SQLException {
        this.dao.createEntry(new Entry("Test1"));
        this.dao.createEntry(new Entry("Test2"));
        this.dao.createEntry(new Entry("Test3"));

        assertEquals(3, this.dao.getEntries().size());
    }

    @Test
    public void getBooksReturnsRightSizeList() throws SQLException {
        this.dao.createBook(new Book("otsikko", "kommentti", "tekija", "123"));
        this.dao.createBook(new Book("otsikko2", "kommentti", "tekija", "1234"));
        this.dao.createBook(new Book("otsikko3", "kommentti", "tekija", "1235"));

        assertEquals(3, this.dao.getBooks().size());
    }

    @Test
    public void getVideosReturnsRightSizeList() throws SQLException {
        this.dao.createVideo(new Video("otsikko", "kommentti", "tekija", "123"));
        this.dao.createVideo(new Video("otsikko2", "kommentti", "tekija", "1234"));
        this.dao.createVideo(new Video("otsikko3", "kommentti", "tekija", "1235"));

        assertEquals(3, this.dao.getVideos().size());
    }

    @Test
    public void getEntriesReturnsEmptyListWhenNoEntriesInDb() throws SQLException {
        assertEquals(0, this.dao.getEntries().size());
    }

    @Test
    public void getEntriesReturnsListContainingAllAddedEntries() {
        this.dao.createEntry(new Entry("Test1"));
        this.dao.createEntry(new Entry("Test2"));

        assertEquals("Test1", this.dao.getEntries().get(0).getTitle());
        assertEquals("Test2", this.dao.getEntries().get(1).getTitle());
    }

    @Test
    public void getBooksReturnsListContainingAllAddedBooks() {
        this.dao.createBook(new Book("title","comment", "author","ISBN123"));
        this.dao.createBook(new Book("title2","comment", "author","ISBN1234"));
        this.dao.createBook(new Book("title3","comment", "author","ISBN12345"));

        assertEquals("title", this.dao.getBooks().get(0).getTitle());
        assertEquals("title2", this.dao.getBooks().get(1).getTitle());
        assertEquals("title3", this.dao.getBooks().get(2).getTitle());
    }

    @Test
    public void getVideosReturnsListContainingAllAddedVideos() {
        this.dao.createVideo(new Video("title","comment", "author","1.23"));
        this.dao.createVideo(new Video("title2","comment", "author","1.59"));
        this.dao.createVideo(new Video("title3","comment", "author","2"));

        assertEquals("title", this.dao.getVideos().get(0).getTitle());
        assertEquals("title2", this.dao.getVideos().get(1).getTitle());
        assertEquals("title3", this.dao.getVideos().get(2).getTitle());
    }
    @Test
    public void getBooksReturnsEmptyListWhenNoBooksInDb() throws SQLException {
        assertEquals(0, this.dao.getBooks().size());
    }
    @Test
    public void getVideosReturnsEmptyListWhenNoVideosInDb() throws SQLException {
        assertEquals(0, this.dao.getBooks().size());
    }

    @Test
    public void createEntryReturnsFalseWhenDatabaseIsClosed() throws SQLException {
        this.dao.close();
        assertFalse(this.dao.createEntry(new Entry("")));
    }

    @Test
    public void createBookReturnsFalseWhenDatabaseIsClosed() throws SQLException {
        this.dao.close();
        assertFalse(this.dao.createBook(new Book("Clean Code: A Handbook of Agile Software Craftsmanship",
                "comments here",
                "Robert Martin",
                "978-0132350884")));
    }

    @Test
    public void deleteEntryBasedOnTitleReturnsFalseWhenGivenTitleIsNotInDb() {
        assertFalse(this.dao.deleteEntryBasedOnTitle("Not here"));
    }

    @Test
    public void deleteEntryBasedOnTitleReturnsTrueWhenGivenTitleIsInDb() {
        this.dao.createEntry(new Entry("Testbook"));
        assertTrue(this.dao.deleteEntryBasedOnTitle("Testbook"));
    }

    @Test
    public void deleteEntryBasedOnTitleReturnsFalseWhenDbClosed() {
        this.dao.close();
        assertFalse(this.dao.deleteEntryBasedOnTitle("TestTitle"));
    }

    @Test
    public void deleteEntryBasedOnTitleReturnsFalseIfNoTitleGiven() {
        assertFalse(this.dao.deleteEntryBasedOnTitle(""));
    }
}
