package kapistelykirjasto.domain;


import kapistelykirjasto.dao.StubDao;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationTest {

    private ApplicationLogic logic;
    //   private SQLiteDao dao;
    private StubDao dao;

    @Before
    public void setUp() {
        //  this.dao = new SQLiteDao(":memory:");
        this.dao = new StubDao();
        this.logic = new ApplicationLogic(dao, dao, dao);
    }

    @Test
    public void createBookCreatesNewBookWithDao() {
        this.logic.createBook("testi", "testi", "testi", "testi");
        assertEquals(this.dao.getBooks().size(), 1);
        assertEquals(this.dao.getBooks().get(0).getTitle(), "testi");
    }

    @Test
    public void addBookReturnsFalseIfRequiredAttributeMissing() {
        assertFalse(this.logic.createBook("testiTitle", "comment", "", "asdc123"));
    }

    @Test
    public void addBookReturnsTrueIfAllRequiredParamsGiven() {
        assertTrue(this.logic.createBook("testiTitle", "", "testiAuthor", "asdc123"));
    }

    @Test
    public void addVideoReturnsFalseIfRequiredAttributeMissing() {
        assertFalse(this.logic.createVideo("testiTitle", "comment", "", "asdc123"));
    }

    @Test
    public void addVideoReturnsTrueIfAllParamsGiven() {
        assertTrue(this.logic.createVideo("testiTitle", "comment", "url", "asdc123"));
    }

    @Test
    public void addVideoReturnsTrueIfRequiredParamsGiven() {
        assertTrue(this.logic.createVideo("testiTitle", "", "url", ""));
    }

    @Test
    public void getBooksReturnsRightList() {
        this.logic.createBook("title", "comment", "author", "ISBN1");
        this.logic.createBook("title1", "comment", "author", "ISBN2");
        this.logic.createBook("title2", "comment", "author", "ISBN3");

        assertEquals(3, this.logic.getBooks().size());
        assertEquals(this.dao.getBooks().get(0).getTitle(), "title");
        assertEquals(this.dao.getBooks().get(2).getTitle(), "title2");
    }

    @Test
    public void getVideosReturnsRightList() {
        this.logic.createVideo("title", "comment", "author", "123");
        this.logic.createVideo("title1", "comment", "author", "1234");
        this.logic.createVideo("title2", "comment", "author", "12345");

        assertEquals(3, this.logic.getVideos().size());
        assertEquals(this.dao.getVideos().get(0).getTitle(), "title");
        assertEquals(this.dao.getVideos().get(2).getTitle(), "title2");
    }

    @Test
    public void deleteBookDeletesBook() {
        this.logic.createBook("title", "comment", "author", "ISBN");
        this.logic.deleteBook(this.logic.getBooks().get(0).getId());
        assertEquals(0, this.logic.getBooks().size());
    }

    @Test
    public void deleteVideoDeletesVideo() {
        this.logic.createVideo("title", "comment", "url", "duration");
        this.logic.deleteVideo(this.logic.getVideos().get(0).getId());
        assertEquals(0, this.logic.getVideos().size());
    }

    @Test
    public void deleteEntryDeletesBook() {
        this.logic.createBook("title", "comment", "author", "ISBN");
        this.logic.deleteEntry(this.logic.getBooks().get(0));
        assertEquals(0, this.logic.getBooks().size());
    }

    @Test
    public void deleteEntryDeletesVideo() {
        this.logic.createVideo("title", "comment", "url", "duration");
        this.logic.deleteEntry(this.logic.getVideos().get(0));
        assertEquals(0, this.logic.getVideos().size());
    }
    @Test
    public void editBookEditsBook() {
        this.logic.createBook("title", "comment", "author", "ISBN");
        int bookId = this.logic.getBooks().get(0).getId();

        assertTrue(this.logic.editBook(bookId,"title2", "comment2", "author2", "ISBN2"));
        Book book = this.logic.getBooks().get(0);

        assertEquals("title2", book.getTitle());
        assertEquals("author2", book.getAuthor());
        assertEquals("ISBN2", book.getISBN());
        assertEquals("comment2", book.getComment());
    }
    @Test
    public void editVideoEditsVideo() {
        this.logic.createVideo("title", "comment", "url", "duration");
        int videoId = this.logic.getVideos().get(0).getId();

        assertTrue(this.logic.editVideo(videoId,"title2", "comment2", "url2", "duration2"));
        Video video = this.logic.getVideos().get(0);

        assertEquals("title2", video.getTitle());
        assertEquals("comment2", video.getComment());
        assertEquals("url2", video.getUrl());
        assertEquals("duration2", video.getDuration());
    }

    @Test
    public void deleteBookReturnsFalseOnInvalidId() {
        assertFalse(this.logic.deleteBook(0));
    }

    @Test
    public void deleteVideoReturnsFalseOnInvalidId() {
        assertFalse(this.logic.deleteVideo(0));
    }

    @Test
    public void createCourseReturnsTrueIfValidParamsGiven() {
        assertTrue(this.logic.createCourse("TKT20005", "Laskennan mallit"));
    }

    @Test
    public void createCourseReturnsFalseIfInvalidParamsGiven() {
        assertFalse(this.logic.createCourse("", ""));
    }

    @Test
    public void createCourseReturnsFalseIfCourseCodeMissing() {
        assertFalse(this.logic.createCourse("", "Todennäköisyyslaskenta"));
    }

    @Test
    public void createCourseReturnsFalseIfNameMissing() {
        assertFalse(this.logic.createCourse("TKT20011", ""));
    }
}
