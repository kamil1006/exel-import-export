package kom.komowo.exelimportexport.excel;

import kom.komowo.exelimportexport.entity.MyCellMin;
import kom.komowo.exelimportexport.entity.MyRange;
import kom.komowo.exelimportexport.entity.MyWorkbook;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class ExcelTool {

    private MyWorkbook myWorkbook;
    private Workbook workbook;


    public ExcelTool() {
        myWorkbook = new MyWorkbook();
    }

    public void processExcelWorkbook( Workbook workbook) {
        this.workbook = workbook;
        myWorkbook=new MyWorkbook();

       // int numberOfSheets = workbook.getNumberOfSheets();
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            String sheetName = sheet.getSheetName();
            myWorkbook.addSheet(sheetName);


        }

    }


    public List<MyCellMin> getDataFromWorkbook(MyRange myRange) {
        List<MyCellMin> lista = new ArrayList<>();
        Sheet sheet = workbook.getSheet(myRange.getName());
        for( int i = myRange.getX1();i< myRange.getX2()+1; i++){
            for( int j = myRange.getY1();j< myRange.getY2()+1; j++) {

                CellReference cellReference = new CellReference(myRange.getName(),i-1,j-1,true,true);
                if(cellReference!=null) {
                    Row row = sheet.getRow(cellReference.getRow());
                    if (row != null) {
                        Cell cell = row.getCell(cellReference.getCol());
                        String cellFormula = "";
                        if (cell != null) {
                            CellType cellType = cell.getCellType();

                            switch (cellType) {
                                case BOOLEAN:
                                    cellFormula = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case NUMERIC:
                                    cellFormula = String.valueOf(cell.getNumericCellValue());
                                    break;
                                case STRING:
                                    cellFormula = String.valueOf(cell.getStringCellValue());
                                    break;
                                case BLANK:
                                    cellFormula = String.valueOf("");
                                    break;
                                case ERROR:
                                    cellFormula = String.valueOf(cell.getErrorCellValue());
                                    break;

                                // CELL_TYPE_FORMULA will never occur
                                case FORMULA:
                                    cellFormula = String.valueOf("");
                                    break;
                            }
                            MyCellMin myCellMin = new MyCellMin(i, j, cellFormula);
                            lista.add(myCellMin);
                        }
                    }
                }

            }
        }

    return lista;
    }
}
