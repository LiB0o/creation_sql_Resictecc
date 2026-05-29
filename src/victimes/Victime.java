package victimes;

import localisation.Localisation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import staff.User;
import utilitaire.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Victime {

    private String prenom;
    private String nom;
    private String sexe;
    private String tel;
    private String adresse;

    public Victime(){
        this.prenom = null;
        this.nom = null;
        this.sexe = null;
        this.adresse = null;
        this.tel = null;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTel() {
        return tel;
    }

    public String getSexe() {
        return sexe;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    @Override
    public String toString() {
        return "Victime{" +
                "prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", sexe='" + sexe + '\'' +
                ", tel='" + tel + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }

    private static String randomTel(){
        String tel = "06";
        Random rand = new Random();

        for(int i =0; i<8;i++){
            tel = tel+rand.nextInt(10);
        }
        return  tel;
    }

    public static List<Victime> generateVictimesMale(int nbUser) throws IOException {
        List<Victime> victimes = new ArrayList<>();
        //String cryptedPassword = Utils.chiffrementPassword(passwordForAll);
        Random rand = new Random();

        FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheet("Personne");

        List<Localisation> localisations = Localisation.generateAllLocation(nbUser);
        int nbOfLocationNames = sheet.getPhysicalNumberOfRows();//need for the 3 collumns to have the same nb of rows
        //System.out.println("Add Female, nb row = "+nbOfLocationNames);

        for(int i =0; i<nbUser; i++){
            Victime victime = new Victime();
            victime.setSexe("M");

            int lineFirstName = rand.nextInt(1,nbOfLocationNames);
            //System.out.println("Add Male, nb row fn = "+lineFirstName);
            int lineLastName = rand.nextInt(1, nbOfLocationNames);
            //System.out.println("Add Male, nb row ln = "+lineLastName);

            victime.setTel(Victime.randomTel());
            victime.setAdresse(localisations.get(i).toString());

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

            victimes.add(victime);

        }
        return victimes;
    }

    public static List<Victime> generateVictimesFemale(int nbUser) throws IOException {
        List<Victime> victimes = new ArrayList<>();
        //String cryptedPassword = Utils.chiffrementPassword(passwordForAll);
        Random rand = new Random();

        FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheet("Personne");

        List<Localisation> localisations = Localisation.generateAllLocation(nbUser);
        int nbOfLocationNames = sheet.getPhysicalNumberOfRows();//need for the 3 collumns to have the same nb of rows
        //System.out.println("Add Female, nb row = "+nbOfLocationNames);

        for(int i =0; i<nbUser; i++){
            Victime victime = new Victime();
            victime.setSexe("F");

            int lineFirstName = rand.nextInt(1,nbOfLocationNames);
            //System.out.println("Add Male, nb row fn = "+lineFirstName);
            int lineLastName = rand.nextInt(1, nbOfLocationNames);
            //System.out.println("Add Male, nb row ln = "+lineLastName);

            victime.setTel(Victime.randomTel());
            victime.setAdresse(localisations.get(i).toString());

            Iterator<Cell> cell_first_name_location = sheet.getRow(lineFirstName).cellIterator();
            while (cell_first_name_location.hasNext()) {
                Cell cell = cell_first_name_location.next();
                if(cell.getColumnIndex() == 0){ //men name
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

            victimes.add(victime);

        }
        return victimes;
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {
        List<Victime>users = generateVictimesFemale(30);
        users.addAll(generateVictimesMale(30));
        Random rand = new Random();

        try (Connection conn = DriverManager.getConnection(url, user, password)){
            System.out.println("Connected to the DB");

            for(Victime u : users){
                String sql = "INSERT INTO " +
                        "`llx_resisteccsamusmur_victimes`(" +
                        "`prenom`, " +
                        "`nom`, " +
                        "`age`, " +
                        "`sexe`, " +
                        "`tel_victime`, " +
                        "`adresse`) " +
                        "VALUES (?,?,?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, u.prenom);
                preparedStatement.setString(2, u.nom);
                preparedStatement.setInt(3, rand.nextInt(90)+1);
                preparedStatement.setString(4, u.sexe);
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
