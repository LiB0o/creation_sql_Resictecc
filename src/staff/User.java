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

    public void setBool_admin(int bool_admin) {
        this.bool_admin = bool_admin;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRole_code(int role_code) {
        this.role_code = role_code;
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

    public void setDate_naissance(Date date_naissance) {
        this.date_naissance = date_naissance;
    }

    public void setDate_crea_compte(Date date_crea_compte) {
        this.date_crea_compte = date_crea_compte;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public int getRowid() {
        return rowid;
    }

    @Override
    public String toString() {
        return "User{" +
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

    public static List<User> generateAllFemaleUser( int nbUser) throws SQLException, IOException {

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

    public static List<User> generateAllMaleUser( int nbUser) throws SQLException, IOException {

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


    private static String randomTel(){
        String tel = "07";
        Random rand = new Random();

        for(int i =0; i<8;i++){
            tel = tel+rand.nextInt(10);
        }
        return  tel;
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {
        List<User>users_temp = generateAllFemaleUser(30);
        users_temp.addAll(generateAllMaleUser(30));

        List<User> users = User.rendreUniques(users_temp);

        try (Connection conn = DriverManager.getConnection(url, user, password)){
            System.out.println("Connected to the DB");

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

    public static List<User> collectSQL(String url,
                                       String user,
                                       String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<User> users = new ArrayList<>();

            Statement stat = null;

            System.out.println("Connected to the DB");

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

    public static List<User> collectSQL_Regulator(String url,
                                        String user,
                                        String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<User> users = new ArrayList<>();

            Statement stat = null;

            System.out.println("Connected to the DB");

            stat = conn.createStatement();
            String sql = "SELECT * \n" +
                    "FROM llx_hrm_job\n" +
                    "JOIN llx_hrm_job_user ON llx_hrm_job_user.fk_job = llx_hrm_job.rowid\n" +
                    "JOIN llx_user ON llx_user.rowid = llx_hrm_job_user.fk_user\n" +
                    "WHERE llx_hrm_job.label = 'Médecin régulateur'";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                int id = resultSQL.getInt("rowid");
                String tel = resultSQL.getString("office_phone");

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

    public static List<User> collectSQL_Operator(String url,
                                                  String user,
                                                  String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<User> users = new ArrayList<>();

            Statement stat = null;

            System.out.println("Connected to the DB");

            stat = conn.createStatement();
            String sql = "SELECT * \n" +
                    "FROM llx_hrm_job\n" +
                    "JOIN llx_hrm_job_user ON llx_hrm_job_user.fk_job = llx_hrm_job.rowid\n" +
                    "JOIN llx_user ON llx_user.rowid = llx_hrm_job_user.fk_user\n" +
                    "WHERE llx_hrm_job.label = 'Opérateur'";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                int id = resultSQL.getInt("rowid");
                String tel = resultSQL.getString("office_phone");

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

}
