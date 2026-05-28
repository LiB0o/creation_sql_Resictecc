package organisme_soin;

import localisation.Localisation;
import localisation.TypeLocalisation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilitaire.Utils;
import vecteur_et_details.TypeMateriel;
import victimes.Victime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static localisation.TypeLocalisation.generateAllTypeLocalisation;

public class TeleMedicalisation {

    /**
     * CREATE TABLE llx_resisteccsamusmur_telemedicalisation(
     *                                                          id_medecin VARCHAR(50) PRIMARY KEY,
     *                                                          prenom_medecin VARCHAR(50),
     *                                                          nom_medecin VARCHAR(50),
     *                                                          profession VARCHAR(50),
     *                                                          tel_telemed VARCHAR(50),
     *                                                          adresse VARCHAR(50) NOT NULL
     */

    private String id;
    private String prenom;
    private String nom;
    private String profession;
    private String tel;
    private String adresse;

    public TeleMedicalisation(){
        this.id = null;
        this.adresse = null;
        this.nom = null;
        this.prenom = null;
        this.tel = null;
        this.profession = null;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getId() {
        return id;
    }

    public String getTel() {
        return tel;
    }

    public String getNom() {
        return nom;
    }

    public String getProfession() {
        return profession;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "TeleMedicalisation{" +
                "id='" + id + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", profession='" + profession + '\'' +
                ", tel='" + tel + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }

    private static String randomTel(){
        String tel = "08";
        Random rand = new Random();

        for(int i =0; i<8;i++){
            tel = tel+rand.nextInt(10);
        }
        return  tel;
    }

    public static List<String> generateAllPosition() throws IOException {
        try{
            Random rand = new Random();

            List<String> list_position = new ArrayList<>();

            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Profession_TelMed");

            Iterator<Row> itr = sheet.iterator();

            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                list_position.add(cell.getStringCellValue());
                                break;
                        }
                    }
                }
            }
            // Closing the workbook to free resources
            wb.close();

            return list_position;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<TeleMedicalisation> generateMedMale(int nbUser) throws IOException {
        List<TeleMedicalisation> meds = new ArrayList<>();

        Random rand = new Random();
        List<Localisation> localisations = Localisation.generateAllLocation(nbUser);
        List<String> positions = TeleMedicalisation.generateAllPosition();
        int nbPostions = positions.size();

        FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheet("Personne");



        int nbOfLocationNames = sheet.getPhysicalNumberOfRows();//need for the 3 collumns to have the same nb of rows

        for(int i =0; i<nbUser; i++){
            TeleMedicalisation victime = new TeleMedicalisation();

            int lineFirstName = rand.nextInt(1,nbOfLocationNames);
            //System.out.println("Add Male, nb row fn = "+lineFirstName);
            int lineLastName = rand.nextInt(1, nbOfLocationNames);
            //System.out.println("Add Male, nb row ln = "+lineLastName);

            victime.setTel(TeleMedicalisation.randomTel());
            victime.setAdresse(localisations.get(i).toString());
            victime.setProfession(positions.get(rand.nextInt(nbPostions)));

            Iterator<Cell> cell_first_name_location = sheet.getRow(lineFirstName).cellIterator();
            while (cell_first_name_location.hasNext()) {
                Cell cell = cell_first_name_location.next();
                if(cell.getColumnIndex() == 1){ //men name
                    victime.setPrenom(cell.getStringCellValue());
                }
            }

            Iterator<Cell> cell_last_name_location = sheet.getRow(lineLastName).cellIterator();
            while (cell_last_name_location.hasNext()) {
                Cell cell = cell_last_name_location.next();
                if(cell.getColumnIndex() == 2){ //last name
                    victime.setNom(cell.getStringCellValue());
                }
            }

            meds.add(victime);

        }
        return meds;
    }

    public static List<TeleMedicalisation> generateMedFemale(int nbUser) throws IOException {
        List<TeleMedicalisation> meds = new ArrayList<>();

        Random rand = new Random();
        List<Localisation> localisations = Localisation.generateAllLocation(nbUser);
        List<String> positions = TeleMedicalisation.generateAllPosition();
        int nbPostions = positions.size();

        FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheet("Personne");



        int nbOfLocationNames = sheet.getPhysicalNumberOfRows();//need for the 3 collumns to have the same nb of rows
        //System.out.println("Add Female, nb row = "+nbOfLocationNames);

        for(int i =0; i<nbUser; i++){
            TeleMedicalisation victime = new TeleMedicalisation();

            int lineFirstName = rand.nextInt(1,nbOfLocationNames);
            //System.out.println("Add Male, nb row fn = "+lineFirstName);
            int lineLastName = rand.nextInt(1, nbOfLocationNames);
            //System.out.println("Add Male, nb row ln = "+lineLastName);

            victime.setTel(TeleMedicalisation.randomTel());
            victime.setAdresse(localisations.get(i).toString());
            victime.setProfession(positions.get(rand.nextInt(nbPostions)));

            Iterator<Cell> cell_first_name_location = sheet.getRow(lineFirstName).cellIterator();
            while (cell_first_name_location.hasNext()) {
                Cell cell = cell_first_name_location.next();
                if(cell.getColumnIndex() == 0){ //woman name
                    victime.setPrenom(cell.getStringCellValue());
                }
            }

            Iterator<Cell> cell_last_name_location = sheet.getRow(lineLastName).cellIterator();
            while (cell_last_name_location.hasNext()) {
                Cell cell = cell_last_name_location.next();
                if(cell.getColumnIndex() == 2){ //last name
                    victime.setNom(cell.getStringCellValue());
                }
            }

            meds.add(victime);

        }
        return meds;
    }


    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {
        Connection conn = null;
        Utils Utils = new Utils();

        List<TeleMedicalisation>users = generateMedFemale(30);
        users.addAll(generateMedMale(30));
        users = Utils.setIds(users);
        Random rand = new Random();

        try{
            conn = DriverManager.getConnection(url,user,password);
            System.out.println("Connected to the DB");

            for(TeleMedicalisation u : users){
                String sql = "INSERT INTO " +
                        "`llx_resisteccsamusmur_telemedicalisation`(" +
                        "`id_medecin`, " +
                        "`prenom_medecin`, " +
                        "`nom_medecin`, " +
                        "`profession`, " +
                        "`tel_telemed`, " +
                        "`adresse`) " +
                        "VALUES (?,?,?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, u.id);
                preparedStatement.setString(2, u.prenom);
                preparedStatement.setString(3, u.nom);
                preparedStatement.setString(4, u.profession);
                preparedStatement.setString(5, u.tel);
                preparedStatement.setString(6, u.adresse);

                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


}
