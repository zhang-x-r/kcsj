package domain;

import java.sql.Timestamp;

public class Record implements  Comparable<Record>{
    private Integer id;
    private String time;
    private String description;
    private Card card;

    public Record(Integer id, String time, String description, Card card) {
        this.id = id;
        this.time = time;
        this.description = description;
        this.card = card;
    }

    public Record(String time, String description, Card card) {
        this.time = time;
        this.description = description;
        this.card = card;
    }

    public Integer getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public Card getCard() {
        return card;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public int compareTo(Record o) {
        return this.id-o.id;
    }
}
