package kapistelykirjasto.dao;

import java.sql.*;
import java.util.ArrayList;

import kapistelykirjasto.domain.*;

public class SQLiteDao implements Dao {

    private Connection connection;

    /**
     * Creates a new connection to the SQLite database in fileName. Pass in
     * ":memory:" as the file name to create a in-memory database.
     *
     * @param fileName
     * @throws SQLException
     */
    public SQLiteDao(String fileName) {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);

            Statement statement = this.connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS book (id INTEGER PRIMARY KEY AUTOINCREMENT"
                            + ", title TEXT UNIQUE, comment TEXT, author TEXT, ISBN TEXT);");
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS video (id INTEGER PRIMARY KEY AUTOINCREMENT"
                            + ", title TEXT UNIQUE, comment TEXT, url TEXT, duration TEXT);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new connection to a SQLite database with the file name
     * "production.db"
     *
     * @throws SQLException
     */
    public SQLiteDao() throws SQLException {
        this("production.db");
    }

    private boolean existsEntry(Entry entry) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM entry WHERE"
                        + " title=?;"
        );
        statement.setString(1, entry.getTitle());
        ResultSet res = statement.executeQuery();
        boolean exists = res.next(); // If there is an element available in the result set, next() returns true.
        statement.close();
        return exists;
    }

    @Override
    public boolean createBook(Book book) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO book(title, comment, author, isbn) VALUES(?,?,?,?);");
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getComment());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getISBN());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.getErrorCode();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean createVideo(Video video) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO video(title, comment, url, duration) VALUES(?,?,?,?);");
            statement.setString(1, video.getTitle());
            statement.setString(2, video.getComment());
            statement.setString(3, video.getUrl());
            statement.setString(4, video.getDuration());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.getErrorCode();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<Book> getBooks() {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "SELECT * FROM book");
            ResultSet res = statement.executeQuery();
            ArrayList<Book> books = new ArrayList<>();
            while (res.next()) {
            	books.add(
            		new Book(
            			res.getString("title"), 
            			res.getString("comment"), 
            			res.getString("author"),
                        res.getString("ISBN")
            		)
            	);
            }
            statement.close();
            return books;

        } catch (SQLException e) {
            e.getErrorCode();
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Video> getVideos() {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM video");
            ResultSet res = statement.executeQuery();
            ArrayList<Video> videos = new ArrayList<>();
            while (res.next()) {
            	videos.add(
            		new Video(
            			res.getString("title"), 
            			res.getString("comment"), 
            			res.getString("url"),
                        res.getString("duration")
            		)
            	);
            }
            statement.close();
            return videos;

        } catch (SQLException e) {
            e.getErrorCode();
            e.printStackTrace();
        }
        return null;
    }

    private boolean existsBook(int id) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "SELECT * FROM book WHERE id=?;"
            );
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();
            boolean exists = res.next(); // If there is an element available in the result set, next() returns true.
            statement.close();
            return exists;
        } catch (SQLException e) {
            e.getErrorCode();
            e.printStackTrace();
            return false;
        }
    }

    private boolean existsVideo(int id) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "SELECT * FROM video WHERE id=?;"
            );
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();
            boolean exists = res.next(); // If there is an element available in the result set, next() returns true.
            statement.close();
            return exists;
        } catch (SQLException e) {
            e.getErrorCode();
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(int id) {
        try {
            if (!existsBook(id)) {
                return false;
            }
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM book WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.getErrorCode();
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public boolean deleteVideo(int id) {
        try {
            if (!existsVideo(id)) {
                return false;
            }
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM video WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.getErrorCode();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
