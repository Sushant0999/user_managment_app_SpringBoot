package com.netsmartz.springsecurity.model;

import org.springframework.stereotype.Service;

@Service
public class Quote {

	@Override
	public String toString() {
		return "Quote [quote=" + quote + ", author=" + author + ", category=" + category + "]";
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private String quote;
	private String author;
	private String category;
}
