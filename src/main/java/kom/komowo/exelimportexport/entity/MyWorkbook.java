package kom.komowo.exelimportexport.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyWorkbook {
    private int sheetsCount;
    private List<String> sheets;
    private List<MyCellMin> cells;
    private int rowStart;
    private int columnStart;
    private int rowEnd;
    private int columnEnd;


    public MyWorkbook() {
        sheetsCount = 0;
        sheets = new ArrayList<>();
        cells = new ArrayList<>();


    }
    public void addSheet(String s){
        sheets.add(s);
        sheetsCount++;

    }

    public void addCell(MyCellMin cell){
        cells.add(cell);

    }

    public int getSheetsCount() {
        return sheetsCount;
    }

    public void setSheetsCount(int sheetsCount) {
        this.sheetsCount = sheetsCount;
    }

    public List<String> getSheets() {
        return sheets;
    }

    public void setSheets(List<String> sheets) {
        this.sheets = sheets;
    }



}
