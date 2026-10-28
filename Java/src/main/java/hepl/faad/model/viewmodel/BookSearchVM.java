package hepl.faad.model.viewmodel;

import java.io.Serializable;

public class BookSearchVM implements Serializable {
    private Integer id;
    private String title;
    private String authorLastName;
    private String authorFirstName;
    private String subjectName;
    private Double maxPrice;

    public BookSearchVM()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthorLastName()
    {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName)
    {
        this.authorLastName = authorLastName;
    }

    public String getAuthorFirstName()
    {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName)
    {
        this.authorFirstName = authorFirstName;
    }

    public String getSubjectName()
    {
        return subjectName;
    }

    public void setSubjectName(String subjectName)
    {
        this.subjectName = subjectName;
    }

    public Double getMaxPrice()
    {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice)
    {
        this.maxPrice = maxPrice;
    }
}
