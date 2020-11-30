package kapistelykirjasto.domain;

import kapistelykirjasto.dao.models.BookModel;

public class Book extends Entry {
	
	private BookModel model;
	
	public Book(BookModel model) {
		this.model = model;
	}
	
	public int getId() {
		return model.getId();
	}

	public String getTitle() {
		return model.getTitle();
	}

	public String getAuthor() {
		return model.getAuthor();
	}

	public String getComment() {
		return model.getComment();
	}

	public String getISBN() {
		return model.getISBN();
	}

	@Override
	public Type getType() {
		return Type.BOOK;
	}

	@Override
	public String toString() {
		String s = "";
		String author = getAuthor();
		if (!author.equals("")) {
			s += author + ": ";
		}
		s += getTitle();
		String isbn = getISBN();
		if (!isbn.equals("")) {
			s += "\n\t" + isbn;
		}
		String comment = getComment();
		if (!comment.equals("")) {
			s += "\n\t" + comment;
		}
		return s;
	}
}
