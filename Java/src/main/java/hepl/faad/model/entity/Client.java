package hepl.faad.model.entity;

public class Client {
    private Integer id;
    private String lastName;
    private String firstName;
    private String address;

    public Client(Integer id, String lastName, String firstName, String address)
    {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }


    @Override
    public String toString(){
        return "Client{" +
                "id = " + id +
                ", lastName = " + lastName +
                ", firstName = " + firstName +
                ", address = " + address +
                '}';
    }
}
