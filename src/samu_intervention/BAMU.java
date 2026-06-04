package samu_intervention;

import localisation.Localisation;
import staff.User;
import utilitaire.Utils;
import victimes.Join_Victime_DAMU;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BAMU {

    /**
     * CREATE TABLE llx_resisteccsamusmur_bamu(
     *                                            id_intervention INT AUTO_INCREMENT PRIMARY KEY,
     *                                            raison_intervention VARCHAR(255),
     *                                            statut_bamu VARCHAR(50),
     *                                            date_ DATE,
     *                                            id_type_BAMU INT NOT NULL,
     *                                            id_demande VARCHAR(50) NOT NULL,
     *                                            id_victime INT NOT NULL,
     *                                            CHECK (statut_bamu IN ('En cours de validation', 'Validé'))
     */
    private int id_inter;
    private String rais_interv;
    private String status_bamu;
    private Date date;
    private int id_type;
    private String id_demande;
    private int id_victime;



    public int getId_inter() {
        return id_inter;
    }

    public Date getDate() {
        return date;
    }

    public int getId_type() {
        return id_type;
    }

    public int getId_victime() {
        return id_victime;
    }

    public String getId_demande() {
        return id_demande;
    }

    public String getRais_interv() {
        return rais_interv;
    }

    public String getStatus_bamu() {
        return status_bamu;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId_demande(String id_demande) {
        this.id_demande = id_demande;
    }

    public void setId_inter(int id_inter) {
        this.id_inter = id_inter;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public void setId_victime(int id_victime) {
        this.id_victime = id_victime;
    }

    public void setRais_interv(String rais_interv) {
        this.rais_interv = rais_interv;
    }

    public void setStatus_bamu(String status_bamu) {
        this.status_bamu = status_bamu;
    }

    @Override
    public String toString() {
        return "BAMU{" +
                "id_inter=" + id_inter +
                ", rais_interv='" + rais_interv + '\'' +
                ", status_bamu='" + status_bamu + '\'' +
                ", date=" + date +
                ", id_type=" + id_type +
                ", id_demande='" + id_demande + '\'' +
                ", id_victime=" + id_victime +
                '}';
    }

    public static List<BAMU> generateAllBAMU(String url,
                                             String user,
                                             String password) throws SQLException, IOException {


        List<BAMU> bamus = new ArrayList<>();
        Random rand = new Random();
        List<Join_Victime_DAMU> list_urgence_victime = Join_Victime_DAMU.collectSQL(url, user, password);

        for(Join_Victime_DAMU j : list_urgence_victime){
            BAMU bamu = new BAMU();

            DAMU damu = DAMU.collectSQL_One_demand(url, user, password, j.getId_damu());
            TypeDAMU type = TypeDAMU.collectOne(url, user, password, damu.getId_type_BAMU());

            if(!Objects.equals(damu.getStatus(), "Refusé")){
                bamu.setRais_interv(type.getName());
                bamu.setStatus_bamu("Validé");

                bamu.setDate(damu.getDate());

                bamu.setId_type(j.getId_type_damu());
                bamu.setId_demande(j.getId_damu());
                bamu.setId_victime(j.getId_victime());

                bamus.add(bamu);
            }
        }

        return bamus;
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {

        List<BAMU> listBAMUs = generateAllBAMU(url, user, password);
        //System.out.println("insert DAMU : damus ok");

        try (Connection conn = DriverManager.getConnection(url, user, password)){
            System.out.println("Connected to the DB");

            for(BAMU b : listBAMUs){
                String sql = "INSERT INTO " +
                        "`llx_resisteccsamusmur_bamu`(" +
                        "`raison_intervention`, " +
                        "`statut_bamu`, " +
                        "`date_`, " +
                        "`id_type_BAMU`, " +
                        "`id_demande`, " +
                        "`id_victime`) " +
                        "VALUES (?,?,?,?,?,?)";

                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, b.rais_interv);
                preparedStatement.setString(2,b.status_bamu);
                preparedStatement.setDate(3,b.date);
                preparedStatement.setInt(4,b.id_type);
                preparedStatement.setString(5,b.id_demande);
                preparedStatement.setInt(6,b.id_victime);

                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
