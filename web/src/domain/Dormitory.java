package domain;

import java.io.Serializable;

public class Dormitory implements
        Comparable<Dormitory>, Serializable {
    private Integer id;
    private Integer dno;
    private float remainEle;
    private float remain_water;

    public Dormitory(Integer id,float remain_water, Integer dno  ) {
        this.id = id;
        this.remain_water = remain_water;
        this.dno = dno;
    }

    public Dormitory(int id,int dno,float remainEle){
        this.id = id;
        this.dno = dno;
        this.remainEle = remainEle;
    }


    public Dormitory(int id, int dno, float remainEle, float remain_water) {
        this.id = id;
        this.dno = dno;
        this.remainEle = remainEle;
        this.remain_water = remain_water;
    }

    public int getId(){return id; }

    public void setId(int id) {
        this.id = id;
    }

    public int getDno() {
        return dno;
    }

    public void setDno(int dno) {
        this.dno = dno;
    }


    public float getRemainEle() {
        return remainEle;
    }

    public void setRemainEle(float remainEle) {
        this.remainEle = remainEle;
    }

    public float getRemain_water() {
        return remain_water;
    }

    public void setRemain_water(float remain_water) {
        this.remain_water = remain_water;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Dormitory other = (Dormitory) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!dno.equals(other.id))
            return false;
        return true;
    }


    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation
     * of this object.
     */
    public String toString()
    {
        final String TAB = "    ";

        String retValue = "";

        retValue = "Dormitory ( "
                + super.toString() + TAB
                + "id = " + this.id + TAB
                + "dno = " + this.dno + TAB
                + "remain_water = " + this.remain_water + TAB
                + "remain_ele = " + this.remainEle + TAB
                + " )";
        return retValue;
    }
    @Override
    public int compareTo(Dormitory o) {
        return this.id - o.getId();
    }
}
