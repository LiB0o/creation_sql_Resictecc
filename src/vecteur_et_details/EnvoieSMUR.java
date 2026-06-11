package vecteur_et_details;

import samu_intervention.BAMU;
import staff.User;
import utilitaire.Utils;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnvoieSMUR {

    /**
     * CREATE TABLE llx_resisteccsamusmur_envoi_ressource(
     *                                                       id_envoi INT AUTO_INCREMENT PRIMARY KEY,
     *                                                       date_envoi DATE,
     *                                                       heure_envoi TIME,
     *                                                       heure_retour TIME,
     *                                                       date_retour DATE,
     *                                                       id_staff INT NOT NULL,
     *                                                       id_staff_1 INT NOT NULL,
     *                                                       id_staff_2 INT,
     *                                                       immatriculation VARCHAR(50),
     *                                                       id_intervention INT
     */

    private int id_envoi;
    private Date date_envoi;
    private Time heure_envoie;
    private Time heure_retour;
    private Date date_retour;

    private int id_infirmier;
    private int id_ambulancier;
    private int id_med;

    private String immatriculation;
    private int id_intervention;

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public void setDate_retour(Date date_retour) {
        this.date_retour = date_retour;
    }

    public void setDate_envoi(Date date_envoi) {
        this.date_envoi = date_envoi;
    }

    public void setHeure_envoie(Time heure_envoie) {
        this.heure_envoie = heure_envoie;
    }

    public void setHeure_retour(Time heure_retour) {
        this.heure_retour = heure_retour;
    }

    public void setId_ambulancier(int id_ambulancier) {
        this.id_ambulancier = id_ambulancier;
    }

    public void setId_envoi(int id_envoi) {
        this.id_envoi = id_envoi;
    }

    public void setId_infirmier(int id_infirmier) {
        this.id_infirmier = id_infirmier;
    }

    public void setId_intervention(int id_intervention) {
        this.id_intervention = id_intervention;
    }

    public void setId_med(int id_med) {
        this.id_med = id_med;
    }

    @Override
    public String toString() {
        return "EnvoieSMUR{" +
                "id_envoi=" + id_envoi +
                ", date_envoi=" + date_envoi +
                ", heure_envoie=" + heure_envoie +
                ", heure_retour=" + heure_retour +
                ", date_retour=" + date_retour +
                ", id_infirmier=" + id_infirmier +
                ", id_ambulancier=" + id_ambulancier +
                ", id_med=" + id_med +
                ", immatriculation='" + immatriculation + '\'' +
                ", id_intervention=" + id_intervention +
                '}';
    }

    /**
     * Read the excel GL_SQL_datas.xlsx to generate a list of envoi_ressource
     * @param url : url toward the database
     * @param user : login to the database
     * @param password : password of the login
     * @param bamus : list of every bamus not used
     * @return list of envoi_ressource ready to be inserted
     * @throws SQLException
     */
    public static List<EnvoieSMUR> generateAllEnvoie(String url, String user, String password, List<BAMU> bamus) throws SQLException {
        List<EnvoieSMUR> envoies = new ArrayList<>();
        LocalTime time = LocalTime.now();
        Random rand = new Random();

        List<User> meds = User.collectSQL_Med(url, user, password);
        List<User> ambulanciers = User.collectSQL_Ambulancier(url, user, password);
        List<User> infirmiers = User.collectSQL_Infirmier(url, user, password);

        List<Vecteur> vecteurs = Vecteur.collectSQL(url, user, password);

        for(BAMU b : bamus){
            EnvoieSMUR e = new EnvoieSMUR();
            e.setId_intervention(b.getId_inter());

            e.setDate_envoi(b.getDate());
            e.setDate_retour(b.getDate());
            e.setHeure_envoie(java.sql.Time.valueOf(Utils.addTime(time, -rand.nextInt(180))));
            e.setHeure_retour(java.sql.Time.valueOf(Utils.addTime(time, rand.nextInt(180))));

            e.setImmatriculation(vecteurs.get(rand.nextInt(vecteurs.size())).getImmatriculation());
            e.setId_ambulancier(ambulanciers.get(rand.nextInt(ambulanciers.size())).getRowid());
            e.setId_infirmier(infirmiers.get(rand.nextInt(infirmiers.size())).getRowid());
            e.setId_med(meds.get(rand.nextInt(meds.size())).getRowid());

            envoies.add(e);
        }
        return envoies;
    }

    /**
     * insert into the table envoi_ressource the generated data
     * @param url : url toward the database
     * @param user : login to the database
     * @param password : password of the login
     * @param bamus : list of every bamus not used
     * @throws IOException
     * @throws SQLException
     */
    public static void insertSQL(String url,
                          String user,
                          String password, List<BAMU> bamus) throws IOException, SQLException {

        List<EnvoieSMUR>envoies = generateAllEnvoie(url, user, password, bamus);

        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(EnvoieSMUR e : envoies){
                String sql = "INSERT INTO " +
                        "`llx_resisteccsamusmur_envoi_ressource`(" +
                        "`date_envoi`, " +
                        "`heure_envoi`, " +
                        "`heure_retour`, " +
                        "`date_retour`, " +
                        "`id_staff`, " +
                        "`id_staff_1`, " +
                        "`id_staff_2`, " +
                        "`immatriculation`, " +
                        "`id_intervention`) " +
                        "VALUES (?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setDate(1, e.date_envoi);
                preparedStatement.setTime(2, e.heure_envoie);
                preparedStatement.setTime(3, e.heure_retour);
                preparedStatement.setDate(4, e.date_retour);
                preparedStatement.setInt(5, e.id_infirmier);
                preparedStatement.setInt(6, e.id_ambulancier);
                preparedStatement.setInt(7,e.id_med);
                preparedStatement.setString(8, e.immatriculation);
                preparedStatement.setInt(9, e.id_intervention);

                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
