package hepl.faad.server.requetes;

public class RequeteCLIENT implements Requete {
    private String lastName;
    private String firstName;
    private boolean nouveau;

    public RequeteCLIENT(String lastName, String firstName, boolean nouveau)
    {
        this.lastName = lastName;
        this.firstName = firstName;
        this.nouveau = nouveau;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public boolean isNouveau()
    {
        return nouveau;
    }
}
