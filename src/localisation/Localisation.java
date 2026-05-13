package localisation;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static localisation.TypeLocalisation.generateAllTypeLocalisation;

public class Localisation {

    private static final int CODE_POSTAL_MIN = 86000;
    private static final int CODE_POSTAL_MAX = 87000;
    private static final String TABLENAME = "llx_resisteccsamusmur_localisation";

    private String id;
    private int num_location;
    private String name_location;
    private int code_postal;
    private int id_type_location;

    public Localisation(/*String id,*/
                        int num_location,
                        String name_location,
                        int code_postal,
                        int id_type_location){
        //this.id = id;
        this.num_location = num_location;
        this.name_location = name_location;
        this.code_postal = code_postal;
        this.id_type_location = id_type_location;
    }

    @Override
    public String toString() {
        return "Localisation{" +
                "id='" + id + '\'' +
                ", num_location=" + num_location +
                ", name_location='" + name_location + '\'' +
                ", code_postal=" + code_postal +
                ", id_type_location=" + id_type_location +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public static List<Localisation> generateAllLocation(int nb_location) throws IOException{
        try{
            Random rand = new Random();

            List<TypeLocalisation> list_type_localisation = generateAllTypeLocalisation();
            int list_type_location_size = list_type_localisation.size();
            List<Localisation> list_localisation = new ArrayList<>();

            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Location_Nom");

            int nbOfLocationNames = sheet.getPhysicalNumberOfRows();


            for(int i = 0; i<nb_location; i++){
                int lineName = rand.nextInt(1,nbOfLocationNames);
                int typeLocationPos = rand.nextInt(0,list_type_location_size);

                String name_location = sheet.getRow(lineName).cellIterator().next().getStringCellValue();

                
            }

            // Closing the workbook to free resources
            wb.close();
            //System.out.println(typesLocation.toString());
            return list_localisation;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException{
        generateAllLocation(3);
    }
}
