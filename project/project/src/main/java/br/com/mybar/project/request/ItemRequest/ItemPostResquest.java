package br.com.mybar.project.request.ItemRequest;

public class ItemPostResquest {
    private int number_item;
    private int type;
    private double value;
    private String name;
    private boolean available;

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public int getNumber_item() {
        return number_item;
    }

    public int getType() {
        return type;
    }

    public boolean isAvailable()
    {
        return available;
    }
}
