package samu_intervention;

import localisation.Localisation;
import staff.User;
import utilitaire.Utils;
import vecteur_et_details.Vecteur;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DAMU {
    /**
     * CREATE TABLE llx_resisteccsamusmur_damu(
     *                                            id_type_BAMU INT,
     *                                            id_demande VARCHAR(50),
     *                                            description VARCHAR(500) NOT NULL,
     *                                            date_ DATE NOT NULL,
     *                                            tel_damu VARCHAR(50) NOT NULL,
     *                                            statut_damu VARCHAR(50),
     *                                            adresse VARCHAR(50) NOT NULL,
     *                                            id_staff INT,
     *                                            id_staff_1 INT,
     *                                            PRIMARY KEY(id_type_BAMU, id_demande),
     *                                            CHECK (statut_damu IN ('Validé', 'Refusé'))
     */

    private int id_type_BAMU;
    private String id;
    private String description;
    private Date date;

    private String tel; //tel med régulateur si pas de med régulateur

    private String status; //'Validé' or 'Refusé' nothing else

    private String adresse;

    private int id_staff_demandeur;
    private int id_staff_regulateur; //ne prendre que les médecins régulateurs

    public DAMU(){
        this.adresse = null;
        this.tel = null;

        this.id = null;
        this.status = null;
        this.description = null;
        this.date = null;

        this.id_type_BAMU = -1;
        this.id_staff_regulateur = -1;
        this.id_staff_demandeur = -1;


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

    public String getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }

    public int getId_staff_demandeur() {
        return id_staff_demandeur;
    }

    public int getId_staff_regulateur() {
        return id_staff_regulateur;
    }

    public int getId_type_BAMU() {
        return id_type_BAMU;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId_staff_demandeur(int id_staff_demandeur) {
        this.id_staff_demandeur = id_staff_demandeur;
    }

    public void setId_staff_regulateur(int id_staff_regulateur) {
        this.id_staff_regulateur = id_staff_regulateur;
    }

    public void setId_type_BAMU(int id_type_BAMU) {
        this.id_type_BAMU = id_type_BAMU;
    }

    @Override
    public String toString() {
        return "DAMU{" +
                "id_type_BAMU=" + id_type_BAMU +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", tel='" + tel + '\'' +
                ", status='" + status + '\'' +
                ", adresse='" + adresse + '\'' +
                ", id_staff_demandeur=" + id_staff_demandeur +
                ", id_staff_regulateur=" + id_staff_regulateur +
                '}';
    }


    public static List<DAMU> generateAllDAMU(String url,
                                             String user,
                                             String password,
                                             int nbDAMUs, LocalDate date) throws SQLException, IOException {

        //System.out.println("generateAllDAMU : enter");

        List<DAMU> damus = new ArrayList<>();
        //System.out.println("generateAllDAMU : damus empty ok");
        List<User> users_regulators = User.collectSQL_Regulator(url, user, password);
        //System.out.println("generateAllDAMU : regulator ok");
        List<User> users_premier_contact = User.collectSQL_Operator(url, user, password);
        //System.out.println("generateAllDAMU : contact ok");
        List<TypeDAMU> typeDAMUS = TypeDAMU.collect(url, user, password);
        //System.out.println("generateAllDAMU : type damu");
        List<Localisation> localisations = Localisation.generateAllLocation(nbDAMUs);

        Random rand = new Random();


        for(int i =0; i<nbDAMUs; i++){
            DAMU damu = new DAMU();

            damu.setId("D"+(i+1));
            damu.setAdresse(localisations.get(i).toString());

            int pos_type = rand.nextInt(typeDAMUS.size());
            damu.setDescription(typeDAMUS.get(pos_type).getName());
            damu.setId_type_BAMU(typeDAMUS.get(pos_type).getId());

            int pos_regulator = rand.nextInt(users_regulators.size());
            damu.setId_staff_regulateur(users_regulators.get(pos_regulator).getRowid());

            String tel = users_regulators.get(pos_regulator).getTel();
            if(tel == null){
                damu.setTel("00");
            }
            else{
                damu.setTel(tel);
            }

            int pos_operator = rand.nextInt(users_premier_contact.size());
            damu.setId_staff_demandeur(users_premier_contact.get(pos_operator).getRowid());

            LocalDate localDate = date;
            damu.setDate(java.sql.Date.valueOf(Utils.addDays(localDate,-rand.nextInt(1095))));

            int valide = rand.nextInt(100);
            if(valide ==0){
                damu.setStatus("Refusé");
            }
            else{
                damu.setStatus("Validé");
            }

            damus.add(damu);

        }
        //System.out.println("generateAllDAMU : end");

        return damus;
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password, LocalDate date) throws IOException, SQLException {

        List<DAMU> listDAMUs = generateAllDAMU(url, user, password, 100, date);
        //System.out.println("insert DAMU : damus ok");

        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(DAMU d : listDAMUs){
                String sql = "INSERT INTO " +
                        "`llx_resisteccsamusmur_damu`(" +
                        "`id_type_BAMU`, " +
                        "`id_demande`, " +
                        "`description`, " +
                        "`date_`, " +
                        "`tel_damu`, " +
                        "`statut_damu`, " +
                        "`adresse`, " +
                        "`id_staff`, " +
                        "`id_staff_1`)"+
                        "VALUES (?,?,?,?,?,?,?,?,?)";


                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, d.id_type_BAMU);
                preparedStatement.setString(2,d.id);
                preparedStatement.setString(3,d.description);
                preparedStatement.setDate(4,d.date);
                preparedStatement.setString(5,d.tel);
                preparedStatement.setString(6,d.status);
                preparedStatement.setString(7,d.adresse);
                preparedStatement.setInt(8,d.id_staff_demandeur);
                preparedStatement.setInt(9,d.id_staff_regulateur);

                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<DAMU> collectSQL(String url,
                                           String user,
                                           String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<DAMU> vecteurs = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_damu";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                String id = resultSQL.getString("id_demande");
                int typeBAMU = resultSQL.getInt("id_type_BAMU");
                String status = resultSQL.getString("statut_damu");
                String description = resultSQL.getString("description");
                Date date = resultSQL.getDate("date_");

                DAMU v = new DAMU();
                v.setId_type_BAMU(typeBAMU);
                v.setId(id);
                v.setStatus(status);
                v.setDescription(description);
                v.setDate(date);

                vecteurs.add(v);
            }

            return vecteurs;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DAMU collectSQL_One_demand(String url,
                                        String user,
                                        String password, String idDAMU) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            DAMU v = new DAMU();

            Statement stat = null;

            System.out.println("Connected to the DB");

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_damu WHERE id_demande = '"+idDAMU+"'";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                String id = resultSQL.getString("id_demande");
                int typeBAMU = resultSQL.getInt("id_type_BAMU");
                String status = resultSQL.getString("statut_damu");
                String description = resultSQL.getString("description");
                Date date = resultSQL.getDate("date_");


                v.setId_type_BAMU(typeBAMU);
                v.setId(id);
                v.setStatus(status);
                v.setDescription(description);
                v.setDate(date);
            }

            return v;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
