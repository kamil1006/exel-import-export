package kom.komowo.exelimportexport.excel;

import kom.komowo.exelimportexport.entity.MyCellMin;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelExportTool extends AbstractXlsView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {

    List<MyCellMin> cellList = (List<MyCellMin>) model.get("lista_komorek");

    String fileName = "file.xls";
    String sheetName = "ArkuszOne";
    response.setHeader("Content-Disposition", "attachment;filename=\""+fileName+"\"");
    Sheet sheet = workbook.createSheet(sheetName);
        //****************************************
        //****************************************
    int xStart =  cellList.stream().min(Comparator.comparing(MyCellMin::getX)).orElseThrow(NoSuchElementException::new).getX();;
    int yStart =  cellList.stream().min(Comparator.comparing(MyCellMin::getY)).orElseThrow(NoSuchElementException::new).getY();;

    int xEnd =  cellList.stream().max(Comparator.comparing(MyCellMin::getX)).orElseThrow(NoSuchElementException::new).getX();;
    int yEnd =  cellList.stream().max(Comparator.comparing(MyCellMin::getY)).orElseThrow(NoSuchElementException::new).getY();;

    int rows = xEnd-xStart+1;
    int cols = yEnd -yStart+1;
    MyCellMin[][] tablica = new MyCellMin[rows][cols];
        for(int x = 0; x< rows; x++){
            for(int y =0; y<cols; y++){
                tablica[x][y] = new MyCellMin(x+1,y+1,"");
            }
        }

        for(MyCellMin m: cellList){
            tablica[m.getX()- xStart][m.getY()- yStart] = m;
        }


        List<List<MyCellMin>> listaList = new ArrayList<>();
        for(int x = 0; x< rows; x++){
            List<MyCellMin> temp = new ArrayList<>();
            for(int y =0; y< cols; y++){
                temp.add(tablica[x][y]);
            }
            listaList.add(temp);

        }
        //****************************************
        //****************************************
        for(int x = 0; x< rows; x++){
            Row row = sheet.createRow(x);
            for(int y =0; y< cols; y++){
                row.createCell(y).setCellValue( tablica[x][y].getValue() );
            }


        }



    }

    public void saveToXlsx(){

        String fileName = "file.xlsx";
        try{
            File excel = new File(fileName);
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();





        }catch (Exception e){

        }


    }


}
