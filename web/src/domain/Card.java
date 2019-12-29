package domain;



import java.io.Serializable;

public final class Card implements Comparable<Card>, Serializable {
    @Override
    public int compareTo(Card card) {
        // TODO Auto-generated method stub
        return this.id - card.id;
    }
    private int id;
    private String cno;
    private String status;
    private float money;
    private String password;
    private Student student;
    public Card(int id,String cno,String status,Float money,String password,Student student){
        this.id=id;
        this.cno=cno;
        this.status=status;
        this.money=money;
        this.password=password;
        this.student=student;

    }
    public Card(String cno,String status,float money,String password,Student student){
        this.cno=cno;
        this.status=status;
        this.money=money;
        this.password=password;
        this.student=student;
    }

    public Student getStudent() {
        return student;
    }

    public int getId() {
        return id;
    }

    public String getCno() {
        return cno;
    }

    public float getMoney() {
        return money;
    }

    public String getPassword() {
        return password;
    }

    public void setStatus(String status){
        this.status=status;
    }
    public String getStatus(){
        return this.status;
    }
    public String toString()
    {
        final String TAB = "    ";

        String retValue = "";

        retValue = "Card ( "
                + super.toString() + TAB
                + "cno = " + this.cno + TAB
                + "money = " + this.money + TAB
                + "password = " + this.password + TAB
                + "student = " + this.student + TAB
                + " )";

        return retValue;
    }
}
