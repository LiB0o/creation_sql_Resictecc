package samu_intervention;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TypeDAMU {

    private static  final String TABLENAME = "llx_resisteccsamusmur_type_bamu";

    private String name;

    public String getName() {
        return name;
    }

    public TypeDAMU(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "TypeLocalisation{" +
                "name='" + name + '\'' +
                '}';
    }

    public static List<TypeDAMU> generateAllTypeDAMU() throws IOException {
        try{
            List<TypeDAMU> typesBAMU = new ArrayList<TypeDAMU>();
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("BAMU_Type");

            // Iterating over rows using iterator
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    TypeDAMU type= new TypeDAMU("");
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                type.name = cell.getStringCellValue();
                                break;
                        }
                    }
                    typesBAMU.add(type);
                }
            }
            // Closing the workbook to free resources
            wb.close();

            return typesBAMU;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertSQL() throws IOException {
        try{
            List<TypeDAMU> list = generateAllTypeDAMU();
            FileWriter writer = new FileWriter("assets/sql_inserts.txt",true);

            for(TypeDAMU t : list){
                writer.write("INSERT INTO "+TABLENAME+" VALUES('"+t.getName()+"');\n");
            }
            writer.close();
        }
        catch(IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) throws IOException {
        insertSQL();
        System.out.println("insertSQL done");
    }*/
}
