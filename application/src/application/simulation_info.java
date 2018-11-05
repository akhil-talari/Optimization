package application;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static application.GA.mapmodnums;
import static application.Install.modinstallmonth;
import static application.Main.module;

public class simulation_info {

    HashMap<Integer, MyObject> module_label;
    File file1;

    public simulation_info(HashMap<Integer, MyObject> module_label) {
        this.module_label = module_label;
        store_data();
    }

    void store_data() {
        try {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Information");

            HashMap<String, Double> mapHeight = new HashMap<>();
            for(int i = 0; i < module_label.size(); i++) {
                mapHeight.put(module_label.get(i).name, module.get(i).getHeight());
            }
            HashMap<String, Double> mapWidth = new HashMap<>();
            for(int i = 0; i < module_label.size(); i++) {
                mapWidth.put(module_label.get(i).name, module.get(i).getWidth());
            }

            int rowCount = 0;

            Map<MyObject, Integer> unsortedMap = new HashMap<MyObject, Integer>();
            for (int i = 0; i < module_label.size(); i++) {
                unsortedMap.put(module_label.get(i), modinstallmonth.get(mapmodnums.get((int) Double.parseDouble(module_label.get(i).name))));
            }

            //convert map to a List
            List<Map.Entry<MyObject, Integer>> list = new LinkedList<Map.Entry<MyObject, Integer>>(unsortedMap.entrySet());

            //sorting the list with a comparator
            Collections.sort(list, new Comparator<Map.Entry<MyObject, Integer>>() {
                @Override
                public int compare(Map.Entry<MyObject, Integer> o1, Map.Entry<MyObject, Integer> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            //convert sortedMap back to Map
            Map<MyObject, Integer> sortedMap = new LinkedHashMap<MyObject, Integer>();
            for (Map.Entry<MyObject, Integer> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }


            for (Map.Entry<MyObject, Integer> entry : sortedMap.entrySet()) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(entry.getKey().name);
                cell = row.createCell(columnCount++);
                cell.setCellValue(entry.getKey().zone);
                double d = Double.parseDouble(entry.getKey().name);
                cell = row.createCell(columnCount++);
                cell.setCellValue(modinstallmonth.get(mapmodnums.get((int) d)));
                cell = row.createCell(columnCount++);
                cell.setCellValue(entry.getKey().x);
                cell = row.createCell(columnCount++);
                cell.setCellValue(entry.getKey().y);
                cell = row.createCell(columnCount++);

                cell.setCellValue(mapHeight.get(entry.getKey().name));
                cell = row.createCell(columnCount++);
                cell.setCellValue(mapWidth.get(entry.getKey().name));
            }

            FileOutputStream outFile = new FileOutputStream(new File("simulate_info.xlsx"));
            workbook.write(outFile);
            outFile.close();
            workbook.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
