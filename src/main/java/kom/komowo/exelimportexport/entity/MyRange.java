package kom.komowo.exelimportexport.entity;

import lombok.Data;

@Data
public class MyRange {

    String name;
    int x1;
    int x2;
    int y1;
    int y2;

    public MyRange(String name, int x1, int x2, int y1, int y2) {
        this.name = name;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
}
