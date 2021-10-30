package kom.komowo.exelimportexport.controller;

import kom.komowo.exelimportexport.entity.MyCellMin;
import kom.komowo.exelimportexport.entity.MyFile;
import kom.komowo.exelimportexport.entity.MyRange;
import kom.komowo.exelimportexport.entity.MyTable;
import kom.komowo.exelimportexport.excel.ExcelExportTool;
import kom.komowo.exelimportexport.excel.ExcelTool;
import kom.komowo.exelimportexport.repository.MyCellMinRepository;
import kom.komowo.exelimportexport.repository.MyTableRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class MainController {


    @Autowired
    MyTableRepository myTableRepository;

    @Autowired
    MyCellMinRepository myCellMinRepository;



    private String fileLocation;
    ExcelTool excelTool;
    List<MyCellMin> dataFromWorkbook;
    MyCellMin[][] tablica;
    List<List<MyCellMin>> listaList;

    MyRange myRangeGlobal;
    MyTable myTableGlobal;


    public MainController() {
        excelTool = new ExcelTool();
    }
    //----------------------------------------------------------------------------------------------------------------------
    @GetMapping(path = "")
    public String mainMenuGet(Model model){

        //String komunikat = "Wczytaj plik";
        //model.addAttribute("message", komunikat);


        List<MyTable> all = myTableRepository.findAll();
        model.addAttribute("tables", all);
        MyTable tablicaWybrana = new MyTable();
        model.addAttribute("tablica_wybrana", tablicaWybrana);

        return "index.html";
    }
    //----------------------------------------------------------------------------------------------------------------------
    @PostMapping(path = "/upload")
    public String mainMenuPost(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, Model model){

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        System.out.println(fileName);
        try{
            InputStream in = file.getInputStream();
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
            FileOutputStream f = new FileOutputStream(fileLocation);
            int ch = 0;
            while ((ch = in.read()) != -1) {
                f.write(ch);
            }
            f.flush();
            f.close();
            model.addAttribute("message", "File: " + file.getOriginalFilename() + " has been uploaded successfully!");
            model.addAttribute("sukces1","true");

        }catch (Exception e){

        }


        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");
        return "index.html";
    }
    //----------------------------------------------------------------------------------------------------------------------
    @RequestMapping(method = RequestMethod.GET, value = "/process")
    public String readPOI(Model model){

        MyRange zakres= new MyRange("",1,1,1,1);

        if (fileLocation != null) {
            if (fileLocation.endsWith(".xlsx") || fileLocation.endsWith(".xls")) {
                //Map<Integer, List<MyCell>> data = excelPOIHelper.readExcel(fileLocation);
              //  model.addAttribute("data", data);
                System.out.println("jest plik");

                FileInputStream file = null;
                try {
                    file = new FileInputStream(new File(fileLocation));
                    Workbook workbook = new XSSFWorkbook(file);
                    excelTool.processExcelWorkbook(workbook);
                    System.out.println(excelTool.getMyWorkbook().toString());;
                    model.addAttribute("sheets_count",excelTool.getMyWorkbook().getSheetsCount());
                    model.addAttribute("sheets_names",excelTool.getMyWorkbook().getSheets());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }




                model.addAttribute("sukces","true");
            } else {
                model.addAttribute("message", "Not a valid excel file!");
                System.out.println("not valid");
                model.addAttribute("sukces","false");


            }
        } else {
            model.addAttribute("message", "File missing! Please upload an excel file.");
            System.out.println("brak pliku");
            model.addAttribute("sukces","false");
        }
        model.addAttribute("zakres",zakres);
        return "excel_filter.html";
    }
    //----------------------------------------------------------------------------------------------------------------------
    @PostMapping(path = "/getvalues")
    public String testowa( @ModelAttribute MyRange myRange,Model model){


        myRangeGlobal = myRange;

        dataFromWorkbook = excelTool.getDataFromWorkbook(myRange);
        model.addAttribute("cells",dataFromWorkbook);

        int i = myRange.getX2() - myRange.getX1() + 1;
        int j = myRange.getY2() - myRange.getY1() + 1;

        LocalDateTime lt = LocalDateTime.now();
        myTableGlobal = new MyTable(i,j,myRange.getName()+"##"+lt);

       tablica = new MyCellMin[i+1][j+1];
        for(int x = 0; x< i; x++) {
            for (int y = 0; y < j; y++) {
                tablica[x][y] = new MyCellMin(x+1,y+1,"");
            }
        }

                for(MyCellMin m:dataFromWorkbook){
            tablica[m.getX() - myRange.getX1()][m.getY()-myRange.getY1()] = m;
        }
       listaList = new ArrayList<>();
            for(int x = 0; x< i; x++){
                List<MyCellMin> temp = new ArrayList<>();
                    for(int y =0; y<j; y++){
                        temp.add(tablica[x][y]);

                    }
                    listaList.add(temp);

            }

        model.addAttribute("rows", i);

        model.addAttribute("columns", j);

        model.addAttribute("zakres",myRange);
        model.addAttribute("cells2",tablica);
        model.addAttribute("cells3",listaList);

        model.addAttribute("sheets_count",excelTool.getMyWorkbook().getSheetsCount());
        model.addAttribute("sheets_names",excelTool.getMyWorkbook().getSheets());


        return "excel_filter.html";
    }
    //----------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value="/save_range", method= RequestMethod.POST)
    public String save(@ModelAttribute MyRange myRange,Model model) {

        System.out.println("######### save range ###################");

       myTableRepository.save(myTableGlobal);
       for (MyCellMin m : dataFromWorkbook ){
           m.setMyTable(myTableGlobal);
           myCellMinRepository.save(m);
       }

        List<MyTable> all2 = myTableRepository.findAll();
        model.addAttribute("tables", all2);
        MyTable tablicaWybrana = new MyTable();
        model.addAttribute("tablica_wybrana", tablicaWybrana);
        return "index.html";
    }

    //----------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value="/show_table", method= RequestMethod.POST)
    public String showTable( @ModelAttribute MyTable tablicaWybrana,Model model) {

        System.out.println("##########  show table  ########################");
        List<MyTable> all = myTableRepository.findAll();
        model.addAttribute("tables", all);

        String name = tablicaWybrana.getName();
        model.addAttribute("name_selected", name);

        MyTable byName = myTableRepository.findByName(name);
        model.addAttribute("tab_selected", byName);


        List<MyCellMin> allByMyTable = myCellMinRepository.findAllByMyTable(byName);
        model.addAttribute("range_founded", allByMyTable);

        tablica = new MyCellMin[byName.getRowsCount()+1][byName.getColumnsCount()+1];
        for(int x = 0; x< byName.getRowsCount(); x++){
            for(int y =0; y< byName.getColumnsCount(); y++){
                tablica[x][y] = new MyCellMin(x+1,y+1,"");
            }
        }

        int xStart =  allByMyTable.stream().min(Comparator.comparing(MyCellMin::getX)).orElseThrow(NoSuchElementException::new).getX();;
        int yStart =  allByMyTable.stream().min(Comparator.comparing(MyCellMin::getY)).orElseThrow(NoSuchElementException::new).getY();;



        for(MyCellMin m: allByMyTable){
            tablica[m.getX()- xStart][m.getY()- yStart] = m;
        }


        listaList = new ArrayList<>();
        for(int x = 0; x< byName.getRowsCount(); x++){
            List<MyCellMin> temp = new ArrayList<>();
            for(int y =0; y< byName.getColumnsCount(); y++){
                temp.add(tablica[x][y]);

            }
            listaList.add(temp);

        }
        model.addAttribute("cells3",listaList);

        List<MyTable> all2 = myTableRepository.findAll();
        model.addAttribute("tables", all2);
        tablicaWybrana = new MyTable();
        model.addAttribute("tablica_wybrana", tablicaWybrana);


        return "index.html";
    }

    //----------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value="/export_range", method= RequestMethod.POST)
    public ModelAndView export(@ModelAttribute MyTable tablicaWybrana,Model model) {
    //    public ModelAndView export(@ModelAttribute MyRange myRange,Model model) {
        System.out.println("######### export range ###################");

        MyTable byName = myTableRepository.findByName(tablicaWybrana.getName());
        List<MyCellMin> allByMyTable = myCellMinRepository.findAllByMyTable(byName);

        return new ModelAndView(new ExcelExportTool(), "lista_komorek", allByMyTable);
    }
    //----------------------------------------------------------------------------------------------------------------------

}
