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

import static application.Hookup.connStartmonth;

public class simulation_conn_info {

    HashMap<String, MyConn> conn_label;
    File file1;

    public simulation_conn_info(HashMap<String, MyConn> conn_label) {
        this.conn_label = conn_label;
        store_data();
    }

    void store_data() {
        try {
            //FileInputStream myxls = new FileInputStream("simulate_conn_info.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Information");

            int rowCount = 0;


            Map<MyConn, Integer> unsortedMap = new HashMap<MyConn, Integer>();
            for (int i = 1; i < conn_label.size(); i++) {
                unsortedMap.put(conn_label.get(i+""), connStartmonth.get(i));
            }

            //convert map to a List
            List<Map.Entry<MyConn, Integer>> list = new LinkedList<Map.Entry<MyConn, Integer>>(unsortedMap.entrySet());

            //sorting the list with a comparator
            Collections.sort(list, new Comparator<Map.Entry<MyConn, Integer>>() {
                @Override
                public int compare(Map.Entry<MyConn, Integer> o1, Map.Entry<MyConn, Integer> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            //convert sortedMap back to Map
            Map<MyConn, Integer> sortedMap = new LinkedHashMap<MyConn, Integer>();
            for (Map.Entry<MyConn, Integer> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<MyConn, Integer> entry : sortedMap.entrySet()) {

                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue((String) entry.getKey().Name);
                cell = row.createCell(columnCount++);
                cell.setCellValue((String) entry.getKey().Conn_Type);
                cell = row.createCell(columnCount++);
                cell.setCellValue(entry.getValue());
                cell = row.createCell(columnCount++);
                cell.setCellValue((String) entry.getKey().conn_between);
                cell = row.createCell(columnCount++);
                cell.setCellValue((Double) entry.getKey().startx);
                cell = row.createCell(columnCount++);
                cell.setCellValue((Double) entry.getKey().starty);
                cell = row.createCell(columnCount++);
                cell.setCellValue((Double) entry.getKey().endx);
                cell = row.createCell(columnCount++);
                cell.setCellValue((Double) entry.getKey().endy);
                cell = row.createCell(columnCount++);
                cell.setCellValue((Integer) entry.getKey().orientation);

            }
            FileOutputStream outFile = new FileOutputStream(new File(
                    "simulate_conn_info.xlsx"));
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
