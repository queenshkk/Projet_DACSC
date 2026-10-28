package hepl.faad.server.reponses;

public class ReponseCLIENT implements Reponse {
    private Integer idClient;

    public ReponseCLIENT(Integer idClient)
    {
        this.idClient = idClient;
    }

    public Integer getIdClient()
    {
        return idClient;
    }
}
