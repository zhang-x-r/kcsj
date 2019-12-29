package domain;

public class BuyEle {
    private int id;
    private float money;
    private String cno;
    private String password;

    public BuyEle(int id, float money, String cno, String password) {
        this.id = id;
        this.money = money;
        this.cno = cno;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
