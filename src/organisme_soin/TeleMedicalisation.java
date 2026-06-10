package organisme_soin;

import localisation.Localisation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilitaire.Utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

    /**
     * Génére un numéro de téléphone qui commance par "08"
     *
     * @author Lison Boo
     * @return une numéro de téléphone en String
     */
    private static String randomTel(){
        String tel = "08";
        Random rand = new Random();

        for(int i =0; i<8;i++){
            tel = tel+rand.nextInt(10);
        }
        return  tel;
    }

    /**
     * Génére une liste de spécialisation médical (Ophtalmologue, Dentiste,...)
     *
     * @author Lison Boo
     * @return La liste des spécialisations en format String
     * @throws IOException Si la feuille Excel utilisé pour la génération n'existe plus.
     */
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

    /**
     * Génére une liste de médecins femme pour être utilisé dans l'insertion dans la table assigné (llx_resisteccsamusmur_telemedicalisation).
     *
     * @author Lison Boo
     * @param nbUser Nombre de médecins femme à générer
     * @return La liste de médecins femme à insérer
     * @throws IOException Voir la fonction generateAllPosition OU Si la feuille Excel utilisé pour la génération n'existe plus (Personne).
     */
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

    /**
     * Génére une liste de médecins homme pour être utilisé dans l'insertion dans la table assigné (llx_resisteccsamusmur_telemedicalisation).
     *
     * @author Lison Boo
     * @param nbUser Nombre de médecins homme à générer
     * @return La liste de médecins homme à insérer
     * @throws IOException Voir la fonction generateAllPosition OU Si la feuille Excel utilisé pour la génération n'existe plus (Personne).
     */
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

    /**
     * Assigne à chaque médecin un id
     *
     * @author Lison Boo
     * @param meds Liste des médecins à qui mettre une id
     * @return Liste des médecins avec un id par médecin
     */
    public static List<TeleMedicalisation> setIds(List<TeleMedicalisation> meds){
        int index = 1;
        for(TeleMedicalisation t : meds){
            t.setId("MED"+index);
            index++;
        }
        return meds;
    }

    /**
     * Insert les médecins dans la table de la base de données
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire l'insertion
     * @param password mot de passe du compte de la base de données qui va faire l'insertion
     * @throws IOException voir generateMedFemale & generateMedMale
     * @throws SQLException Si il y a eu un problème lors de la connection ou insertion
     */
    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {
        Utils Utils = new Utils();

        List<TeleMedicalisation>users = generateMedFemale(30);
        users.addAll(generateMedMale(30));
        users = TeleMedicalisation.setIds(users);
        Random rand = new Random();

        try (Connection conn = DriverManager.getConnection(url, user, password)){

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


    /**
     * Collecte tout les médecins dans llx_resisteccsamusmur_telemedicalisation.
     *
     * @author Marine Virot
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @return Liste des médecins
     * @throws SQLException Si il y a eu un problème lors de la connection ou lecture
     */
    public static List<TeleMedicalisation> collectSQL(String url,
                                       String user,
                                       String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<TeleMedicalisation> vecteurs = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_telemedicalisation";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                String id = resultSQL.getString("id_medecin");

                TeleMedicalisation v = new TeleMedicalisation();
                v.id = id;

                vecteurs.add(v);
            }

            return vecteurs;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
