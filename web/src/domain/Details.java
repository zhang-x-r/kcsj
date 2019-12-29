package domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Details implements Comparable<Details>, Serializable {
    private int id;
    private String detail;
    private String time;
    private Card card;
    private float money;

    public Details(int id, String detail, Card card, float money, String time) {
        this.id = id;
        this.detail = detail;
        this.card = card;
        this.money = money;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public float getMoney() {
        return money;
    }
    public void setMoney(float money) {
        this.money = money;
    }

    @Override
    public int compareTo(Details details) {
        return this.id - details.id ;
    }
}
