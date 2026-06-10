package staff;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Join_SMUR_User {
    private int id_smur;
    private int id_user;
    private Date date_emploi;

    public Join_SMUR_User() {
        this.id_smur = -1;
        this.id_user = -1;
        this.date_emploi = null;
    }

    /**
     * Créer la liste des lien SMUR_Staff à insérer
     * Besoin de lire les tables SMUR et Staff (user)
     *
     * @author Marine Virot
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @param date Date de l'exercice
     * @return Liste des jointure
     */
    public static List<Join_SMUR_User> generateAllJoin(String url, String user, String password, LocalDate date) {
        try {
            List<Join_SMUR_User> listJoin = new ArrayList<>();
            List<SMUR> listSmur = SMUR.collectSQL(url, user, password);
            List<User> listUser = User.collectSQL(url, user, password);
            LocalDate localDate = date;
            int nbSmur = listSmur.size();
            Random rand = new Random();

            for(User u : listUser) {
                int us = u.getRowid();
                int smur = listSmur.get(rand.nextInt(nbSmur)).getId();
                if(Join_Staff_Job.hasSMURJob(url, user, password, u.getRowid()) && !Join_Staff_Job.isCESUStaff(url, user, password, u.getRowid())) {
                    Join_SMUR_User join = new Join_SMUR_User();
                    join.id_user = us;
                    join.id_smur = smur;
                    join.date_emploi = Date.valueOf(localDate.minusDays(rand.nextInt(1825)));

                    listJoin.add(join);
                }
            }

            return listJoin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insert dans la table llx_resisteccsamusmur_employer_smur la liste des jointures
     *
     * @author Marine Virot
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire l'insertion
     * @param password  mot de passe du compte de la base de données qui va faire l'insertion
     * @param date Date de l'exercice
     */
    public static void insertSQL(String url, String user, String password, LocalDate date) {
        List<Join_SMUR_User> listJoin = Join_SMUR_User.generateAllJoin(url, user, password, date);

        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(Join_SMUR_User j : listJoin) {
                String sql = "INSERT INTO `llx_resisteccsamusmur_employer_smur`(`id_staff`,`idSMUR`,`date_emploi`)" +
                            "VALUES (?,?,?)";
                PreparedStatement p = conn.prepareStatement(sql);
                p.setInt(1,j.id_user);
                p.setInt(2,j.id_smur);
                p.setDate(3,j.date_emploi);
                p.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
