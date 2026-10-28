package hepl.faad.model.entity;

import java.io.Serializable;

public class Book implements Serializable {
    private Integer id;
    private int authorId;
    private int subjectId;
    private String title;
    private String isbn;
    private int pageCount;
    private int stockQuantity;
    private double price;
    private int publishYear;

    public Book(int authorId, int subjectId, String title, String isbn, int pageCount, int stockQuantity, double price, int publishYear)
    {
        this.authorId = authorId;
        this.subjectId = subjectId;
        this.title = title;
        this.isbn = isbn;
        this.pageCount = pageCount;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.publishYear = publishYear;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getIdAuthor()
    {
        return authorId;
    }

    public void setIdAuthor(int authorId)
    {
        this.authorId = authorId;
    }

    public int getIdSubject()
    {
        return subjectId;
    }

    public void setIdSubject(int subjectId)
    {
        this.subjectId = subjectId;
    }

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

    public int getStockQuantity()
    {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity)
    {
        this.stockQuantity = stockQuantity;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public int getPublishYear()
    {
        return publishYear;
    }

    public void setPublishYear(int publishYear)
    {
        this.publishYear=publishYear;
    }

    @Override
    public String toString(){
        return "Book{" +
                "id = " + id +
                ", authorId = " + authorId +
                ", subjectId = " + subjectId +
                ", title = " + title +
                ", isbn = " + isbn +
                ", pageCount = " + pageCount +
                ", stockQuantity = " + stockQuantity +
                ", price = " + price +
                ", publishYear = " + publishYear +
                '}';
    }
}
