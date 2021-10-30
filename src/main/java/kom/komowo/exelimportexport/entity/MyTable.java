package kom.komowo.exelimportexport.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class MyTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int rowsCount;
    private  int columnsCount;
    private String name;

    @OneToMany(mappedBy= "myTable")
    private List<MyCellMin> myCellMin;

    public MyTable() {
    }

    public MyTable(int rowsCount, int columnsCount, String name) {
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
        this.name = name;
    }

    @Override
    public String toString() {
        return "MyTable{" +
                "id=" + id +
                ", rowsCount=" + rowsCount +
                ", columnsCount=" + columnsCount +
                ", name='" + name + '\'' +
                '}';
    }
}
