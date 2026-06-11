package staff;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilitaire.Utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class User {

    /**
     * rowid : int
     * entity : int (0 = superadmin) (1 = pompier)
     *
     * admin : int (bool) (1 = admin) (0 = non)
     * datec : date -> date d'arrivée ou de compte
     * login : string -> code d'utilisateur pour se connecter sur ce profil
     * address : string -> adresse
     * birth : date -> date de naissance
     * office_phone : int -> téléphone pro
     * email : string -> mail prenom.nom@email.com ?

     */

    private static  final String TABLENAME = "llx_user";
    private final static String PASSWORD = "password";

    private int rowid;
    private int role_code;
    private int bool_admin; // 0 or 1
    private Date date_crea_compte;
    private String login;
    private String password;

    private String lastname;
    private String firstname;

    private String adresse;
    private Date date_naissance;
    private String tel;
    private String email;


    public User(boolean admin){
        if (admin){
            this.bool_admin = 1;
        }
        else{
            this.bool_admin = 0;
        }

        this.rowid = -1;
        this.login = null;
        this.password = null;
        this.lastname = null;
        this.firstname = null;

    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public int getRowid() {
        return rowid;
    }

    public String getTel() {
        return tel;
    }

    @Override
    public String toString() {
        return "User{" +
                "rowid=" + rowid +
                "role_code=" + role_code +
                ", bool_admin=" + bool_admin +
                ", date_crea_compte=" + date_crea_compte +
                ", login='" + login + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", adresse='" + adresse + '\'' +
                ", date_naissance=" + date_naissance +
                ", tel=" + tel +
                ", email='" + email +
                '}';
    }

    /**
     * Génére tout les participants pour l'exercice
     * Compte admins utilisable dans Dolibarr
     * login : participantx
     * mdp : participantx
     *
     * (x étant un nombre de 1 à nbUser)
     *
     * @author Lison Boo
     * @param nbUser nombre de participants
     * @return Liste des participants
     */
    public static List<User> generateAllPartitipants( int nbUser) {

        List<User> users = new ArrayList<>();
        Random rand = new Random();

        for(int i =0; i<nbUser; i++){
            User user = new User(true);

            user.setLogin("participant"+(i+1));

            user.setPassword(Utils.chiffrementPassword("participant"+(i+1)));

            users.add(user);

        }


        return users;
    }

    /**
     * Créer tout les utilisateurs Dolibarr feminin via feuille Excel
     *
     * @author Lison Boo
     * @param nbUser nombre de user feminin à créer
     * @return liste de users feminin
     * @throws IOException Erreur si la feuille Excel n'existe pas
     */
    public static List<User> generateAllFemaleUser( int nbUser) throws IOException {

        List<User> users = new ArrayList<>();
        String cryptedPassword = Utils.chiffrementPassword();
        Random rand = new Random();

        FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheet("Personne");

        int nbOfLocationNames = sheet.getPhysicalNumberOfRows();//need for the 3 collumns to have the same nb of rows
        //System.out.println("Add Female, nb row = "+nbOfLocationNames);

        for(int i =0; i<nbUser; i++){
            User user = new User(false);

            int lineFirstName = rand.nextInt(1,nbOfLocationNames);
            //System.out.println("Add Female, nb row fn = "+lineFirstName);
            int lineLastName = rand.nextInt(1, nbOfLocationNames);
            //System.out.println("Add Female, nb row ln = "+lineLastName);

            user.setTel(User.randomTel());

            Iterator<Cell> cell_first_name_location = sheet.getRow(lineFirstName).cellIterator();
            while (cell_first_name_location.hasNext()) {
                Cell cell = cell_first_name_location.next();
                if(cell.getColumnIndex() == 0){ //women name
                    user.setFirstname(cell.getStringCellValue());
                }
            }

            Iterator<Cell> cell_last_name_location = sheet.getRow(lineLastName).cellIterator();
            while (cell_last_name_location.hasNext()) {
                Cell cell = cell_last_name_location.next();
                if(cell.getColumnIndex() == 2){ //last name
                    user.setLastname(cell.getStringCellValue());
                }
            }
            user.setLogin(user.lastname.toLowerCase()+"."+user.firstname.toLowerCase());
            user.setPassword(cryptedPassword);

            users.add(user);

        }


        return users;
    }

    /**
     * Créer tout les utilisateurs Dolibarr masculin via feuille Excel
     *
     * @author Lison Boo
     * @param nbUser nombre de user masculin à créer
     * @return liste de users masculin
     * @throws IOException Erreur si la feuille Excel n'existe pas
     */
    public static List<User> generateAllMaleUser( int nbUser) throws IOException {

        List<User> users = new ArrayList<>();
        String cryptedPassword = Utils.chiffrementPassword();
        Random rand = new Random();

        FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheet("Personne");

        int nbOfLocationNames = sheet.getPhysicalNumberOfRows();//need for the 3 collumns to have the same nb of rows
        //System.out.println("Add Female, nb row = "+nbOfLocationNames);

        for(int i =0; i<nbUser; i++){
            User user = new User(false);

            int lineFirstName = rand.nextInt(1,nbOfLocationNames);
            //System.out.println("Add Male, nb row fn = "+lineFirstName);
            int lineLastName = rand.nextInt(1, nbOfLocationNames);
            //System.out.println("Add Male, nb row ln = "+lineLastName);

            user.setTel(User.randomTel());

            Iterator<Cell> cell_first_name_location = sheet.getRow(lineFirstName).cellIterator();
            while (cell_first_name_location.hasNext()) {
                Cell cell = cell_first_name_location.next();
                if(cell.getColumnIndex() == 1){ //men name
                    user.setFirstname(cell.getStringCellValue());
                }
            }

            Iterator<Cell> cell_last_name_location = sheet.getRow(lineLastName).cellIterator();
            while (cell_last_name_location.hasNext()) {
                Cell cell = cell_last_name_location.next();
                if(cell.getColumnIndex() == 2){ //last name
                    user.setLastname(cell.getStringCellValue());
                }
            }
            user.setLogin(user.lastname.toLowerCase()+"."+user.firstname.toLowerCase());
            user.setPassword(cryptedPassword);

            users.add(user);

        }


        return users;
    }


    /**
     * Génére un numéro de téléphone commançant par 07
     *
     * @author Lison Boo
     * @return Un numéro de téléphone en format String
     */
    private static String randomTel(){
        String tel = "07";
        Random rand = new Random();

        for(int i =0; i<8;i++){
            tel = tel+rand.nextInt(10);
        }
        return  tel;
    }

    /**
     * Insert tout les user (participants, femme, homme) dans llx_user
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire l'insertion
     * @param password mot de passe du compte de la base de données qui va faire l'insertion
     * @throws IOException Voir generateAllFemaleUser & generateAllMaleUser
     * @throws SQLException Erreur si la connexion/insertion se déroule mal
     */
    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {
        List<User>users_temp = generateAllFemaleUser(30);
        users_temp.addAll(generateAllMaleUser(30));

        List<User> users = User.rendreUniques(users_temp);

        users.addAll(generateAllPartitipants(20));

        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(User u : users){
                String sql = "INSERT INTO " +
                        "`llx_user`(" +
                        "`admin`, " +
                        "`login`, " +
                        "`pass_crypted`, " +
                        "`lastname`, " +
                        "`firstname`, " +
                        "`office_phone`) " +
                        "VALUES (?,?,?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setInt(1, u.bool_admin);
                preparedStatement.setString(2, u.login);
                preparedStatement.setString(3, u.password);
                preparedStatement.setString(4, u.lastname);
                preparedStatement.setString(5, u.firstname);
                preparedStatement.setString(6, u.tel);

                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Change les logins de user masculin & feminin en cas de doublon)
     *
     * @author Lison Boo
     * @param users liste des user
     * @return Liste des users avec des logins uniques
     */
    private static List<User> rendreUniques(List<User> users) {

        Map<String, Integer> compteur = new HashMap<>();
        List<User> resultat = new ArrayList<>();

        for (User u : users) {

            int occurrence = compteur.getOrDefault(u.login, 0);

            if (occurrence == 0) {
                resultat.add(u);
            } else {
                u.setLogin(u.login+ "-" + occurrence);
                resultat.add(u);
            }

            compteur.put(u.login, occurrence + 1);
        }

        return resultat;
    }

    /**
     * Collecte tout les users
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @return Liste des User
     * @throws SQLException Erreur en cas de mauvaise connexion/lecture
     */
    public static List<User> collectSQL(String url,
                                       String user,
                                       String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<User> users = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_user";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                int id = resultSQL.getInt("rowid");
                //java.sql.Date date_aquis = resultSQL.getDate("date_creation");
                //String label = resultSQL.getString("label");

                User v = new User(false);
                v.setRowid(id);
                users.add(v);
            }

            return users;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Collecte les users ayant pour job "Médecin régulateur"
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @return Liste des Médecins régulateurs
     * @throws SQLException Erreur en cas de mauvaise connexion/lecture
     */
    public static List<User> collectSQL_Regulator(String url,
                                        String user,
                                        String password) throws SQLException {
        List<User> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)){

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * \n" +
                    "FROM llx_hrm_job\n" +
                    "JOIN llx_hrm_job_user ON llx_hrm_job_user.fk_job = llx_hrm_job.rowid\n" +
                    "JOIN llx_user ON llx_user.rowid = llx_hrm_job_user.fk_user\n" +
                    "WHERE llx_hrm_job.label = 'Médecin régulateur'";
            ResultSet resultSQL = stat.executeQuery(sql);

            //System.out.println("regulator : sql ok");

            while (resultSQL.next()) {

                //System.out.println("regulator : while");

                int id = resultSQL.getInt("llx_user.rowid");
                String tel = resultSQL.getString("llx_user.office_phone");

                User v = new User(false);
                v.setRowid(id);
                v.setTel(tel);
                users.add(v);
            }
            //System.out.println("regulator : end");
            return users;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Collecte les users ayant pour job "Opérateur"
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @return Liste des Opérateurs
     * @throws SQLException Erreur en cas de mauvaise connexion/lecture
     */
    public static List<User> collectSQL_Operator(String url,
                                                  String user,
                                                  String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<User> users = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * \n" +
                    "FROM llx_hrm_job\n" +
                    "JOIN llx_hrm_job_user ON llx_hrm_job_user.fk_job = llx_hrm_job.rowid\n" +
                    "JOIN llx_user ON llx_user.rowid = llx_hrm_job_user.fk_user\n" +
                    "WHERE llx_hrm_job.label = 'Opérateur'";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                int id = resultSQL.getInt("llx_user.rowid");
                String tel = resultSQL.getString("llx_user.office_phone");

                User v = new User(false);
                v.setRowid(id);
                v.setTel(tel);
                users.add(v);
            }

            return users;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Collecte les users ayant pour job "Infirmier"
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @return Liste des Infirmiers
     * @throws SQLException Erreur en cas de mauvaise connexion/lecture
     */
    public static List<User> collectSQL_Infirmier(String url,
                                                  String user,
                                                  String password) throws SQLException {
        List<User> users = new ArrayList<>();
        //System.out.println("regulator : enter");
        try (Connection conn = DriverManager.getConnection(url, user, password)){

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * \n" +
                    "FROM llx_hrm_job\n" +
                    "JOIN llx_hrm_job_user ON llx_hrm_job_user.fk_job = llx_hrm_job.rowid\n" +
                    "JOIN llx_user ON llx_user.rowid = llx_hrm_job_user.fk_user\n" +
                    "WHERE llx_hrm_job.label = 'Infirmier'";
            ResultSet resultSQL = stat.executeQuery(sql);

            //System.out.println("regulator : sql ok");

            while (resultSQL.next()) {

                //System.out.println("regulator : while");

                int id = resultSQL.getInt("llx_user.rowid");
                String tel = resultSQL.getString("llx_user.office_phone");

                User v = new User(false);
                v.setRowid(id);
                v.setTel(tel);
                users.add(v);
            }
            //System.out.println("regulator : end");
            return users;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Collecte les users ayant pour job "Ambulancier"
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @return Liste des Ambulanciers
     * @throws SQLException Erreur en cas de mauvaise connexion/lecture
     */
    public static List<User> collectSQL_Ambulancier(String url,
                                                  String user,
                                                  String password) throws SQLException {
        List<User> users = new ArrayList<>();
        //System.out.println("regulator : enter");
        try (Connection conn = DriverManager.getConnection(url, user, password)){

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * \n" +
                    "FROM llx_hrm_job\n" +
                    "JOIN llx_hrm_job_user ON llx_hrm_job_user.fk_job = llx_hrm_job.rowid\n" +
                    "JOIN llx_user ON llx_user.rowid = llx_hrm_job_user.fk_user\n" +
                    "WHERE llx_hrm_job.label = 'Ambulancier'";
            ResultSet resultSQL = stat.executeQuery(sql);

            //System.out.println("regulator : sql ok");

            while (resultSQL.next()) {

                //System.out.println("regulator : while");

                int id = resultSQL.getInt("llx_user.rowid");
                String tel = resultSQL.getString("llx_user.office_phone");

                User v = new User(false);
                v.setRowid(id);
                v.setTel(tel);
                users.add(v);
            }
            //System.out.println("regulator : end");
            return users;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Collecte les users ayant pour job "Médecin"
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @return Liste des Médecins
     * @throws SQLException Erreur en cas de mauvaise connexion/lecture
     */
    public static List<User> collectSQL_Med(String url,
                                                    String user,
                                                    String password) throws SQLException {
        List<User> users = new ArrayList<>();
        //System.out.println("regulator : enter");
        try (Connection conn = DriverManager.getConnection(url, user, password)){

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * \n" +
                    "FROM llx_hrm_job\n" +
                    "JOIN llx_hrm_job_user ON llx_hrm_job_user.fk_job = llx_hrm_job.rowid\n" +
                    "JOIN llx_user ON llx_user.rowid = llx_hrm_job_user.fk_user\n" +
                    "WHERE llx_hrm_job.label = 'Médecin'";
            ResultSet resultSQL = stat.executeQuery(sql);

            //System.out.println("regulator : sql ok");

            while (resultSQL.next()) {

                //System.out.println("regulator : while");

                int id = resultSQL.getInt("llx_user.rowid");
                String tel = resultSQL.getString("llx_user.office_phone");

                User v = new User(false);
                v.setRowid(id);
                v.setTel(tel);
                users.add(v);
            }
            //System.out.println("regulator : end");
            return users;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
