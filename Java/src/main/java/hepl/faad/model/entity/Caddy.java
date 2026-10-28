package hepl.faad.model.entity;

import java.time.LocalDate;

public class Caddy {
    private Integer id;
    private Integer clientId;
    private LocalDate date;
    private double amount;
    private boolean payed;

    public Caddy(Integer id, Integer clientId, LocalDate date, double amount, boolean payed)
    {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.amount = amount;
        this.payed = payed;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getClientId()
    {
        return clientId;
    }

    public void setClientId(Integer clientId)
    {
        this.clientId = clientId;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public boolean isPayed()
    {
        return payed;
    }

    public void setPayed(boolean payed)
    {
        this.payed = payed;
    }

    @Override
    public String toString(){
        return "Caddy{" +
                "id = " + id +
                ", clientId = " + clientId +
                ", date = " + date +
                ", amount = " + amount +
                ", payed = " + payed +
                '}';
    }
}
