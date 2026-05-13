package localisation;

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



public class TypeLocalisation {

    private static  final String TABLENAME = "llx_resisteccsamusmur_type_localisation";


    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TypeLocalisation(String id,String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "TypeLocalisation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static List<TypeLocalisation> generateAllTypeLocalisation() throws IOException {
        try{
            List<TypeLocalisation> typesLocation = new ArrayList<TypeLocalisation>();
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Localisation_Type");

            // Iterating over rows using iterator
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    TypeLocalisation type= new TypeLocalisation("","");
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                type.id = cell.getStringCellValue();
                                break;
                            case 1:
                                type.name = cell.getStringCellValue();
                                break;
                        }
                    }
                    //System.out.println(type.toString());
                    typesLocation.add(type);
                }

                //System.out.println("");
            }
            // Closing the workbook to free resources
            wb.close();
            //System.out.println(typesLocation.toString());
            return typesLocation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertSQL() throws IOException {
        try{
            List<TypeLocalisation> list = generateAllTypeLocalisation();
            FileWriter writer = new FileWriter("assets/sql_inserts.txt",true);

            for(TypeLocalisation t : list){
                writer.write("INSERT INTO "+TABLENAME+" VALUES('"+t.getId()+"','"+t.getName()+"');\n");
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
