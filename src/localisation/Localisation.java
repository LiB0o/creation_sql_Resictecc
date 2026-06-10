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
        return num_location + " " + name_location + ", " + code_postal;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Génére une liste d'adresse pour être utilisé dans l'insertion dans les tables nécessitant une adresse.
     * Utilise un tableau Excel pour choisir aléatoirement un nom d'adresse (exemple: "Edward" dans "7 rue Edward")
     *
     * @author Lison Boo
     * @param nb_location Désigne le nombre d'adresse à générer
     * @return La liste des adresses
     * @throws IOException Si la feuille Excel utilisé pour la génération n'existe plus.
     */

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
                int numLocation = rand.nextInt(100)+1;
                String name_location = sheet.getRow(lineName).cellIterator().next().getStringCellValue();
                name_location = list_type_localisation.get(typeLocationPos).getName() + " "+name_location;
                int codePostal = rand.nextInt(86000,87000)+1;

                Localisation temp = new Localisation(numLocation,name_location,codePostal,typeLocationPos);
                list_localisation.add(temp);
            }

            // Closing the workbook to free resources
            wb.close();
            //System.out.println(typesLocation.toString());
            return list_localisation;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
