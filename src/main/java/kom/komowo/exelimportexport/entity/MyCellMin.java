package kom.komowo.exelimportexport.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class MyCellMin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private int x;
    private int y;
    private String value;


    @ManyToOne
    @JoinColumn(name="my_table_id", nullable=false)
    private MyTable myTable;



    public MyCellMin() {

    }

    public MyCellMin(int x, int y, String value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public MyCellMin(int x, int y, String value, MyTable myTable) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.myTable = myTable;
    }

    public MyCellMin(MyCellMin myCellMin, MyTable myTable) {
        this.x = myCellMin.getX();
        this.y = myCellMin.getY();
        this.value = myCellMin.getValue();
        this.myTable = myTable;
    }


    @Override
    public String toString() {
        return "MyCellMin{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", value='" + value + '\'' +
                ", myTable=" + myTable +
                '}';
    }
}
