package domain;

public class BuyWater {
    private Integer dno;
    private String cno;
    private String password;
    private float money;

    public BuyWater(Integer dno, String cno, String password, float money) {
        this.dno = dno;
        this.cno = cno;
        this.password = password;
        this.money = money;
    }

    public void setDno(Integer dno) {
        this.dno = dno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Integer getDno() {
        return dno;
    }

    public String getCno() {
        return cno;
    }

    public String getPassword() {
        return password;
    }

    public float getMoney() {
        return money;
    }
}
