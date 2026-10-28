package hepl.faad.model.entity;

import java.time.LocalDate;

public class Author {
    private Integer id;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;

    public Author(Integer id, String lastName, String firstName)
    {
        this.id = id;
        this.lastName=lastName;
        this.firstName=firstName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName=lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName=firstName;
    }

    public LocalDate getBirthDate(){
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate){
        this.birthDate=birthDate;
    }

    @Override
    public String toString(){
        return "Author{" +
                "id = " + id +
                ", lastName = " + lastName +
                ", firstName = " + firstName +
                ", birthDate = " + birthDate +
                '}';
    }

}
