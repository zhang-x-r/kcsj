package domain;

import java.sql.Timestamp;

public class Payment {
    private int id;
    private String description;
    private float money;
    private String time;
    private Dormitory dormitory;

    public Payment(int id, String description, float money, String time, Dormitory dormitory) {
        this.id = id;
        this.description = description;
        this.money = money;
        this.time = time;
        this.dormitory = dormitory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Dormitory getDormitory() {
        return dormitory;
    }

    public void setDormitory(Dormitory dormitory) {
        this.dormitory = dormitory;
    }
}
