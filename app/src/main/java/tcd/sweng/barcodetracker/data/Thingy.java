package tcd.sweng.barcodetracker.data;

import java.io.Serializable;

/**
 * Created by cal on 16/11/16.
 */

public class Thingy implements Serializable // Object was taken... ;)
{
    int id;
    String name;
    String barcode;

    Integer owner;
    Integer project;

    boolean damaged;
    Long damagedDate;
    long attachedDate;

    public Thingy(int id, String name, Integer owner, Integer project, String barcode, boolean damaged, Long damagedDate, long attachedDate)
    {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.project = project;
        this.barcode = barcode;
        this.damaged = damaged;
        this.damagedDate = damagedDate;
        this.attachedDate = attachedDate;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Integer getOwner()
    {
        return owner;
    }

    public Integer getProject()
    {
        return project;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public boolean isDamaged()
    {
        return damaged;
    }

    public Long getDamagedDate()
    {
        return damagedDate;
    }

    public long getAttachedDate()
    {
        return attachedDate;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
