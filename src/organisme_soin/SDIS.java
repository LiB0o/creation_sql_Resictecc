package organisme_soin;

import localisation.Localisation;
import utilitaire.Utils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SDIS {

    /**
     * CREATE TABLE llx_resisteccsamusmur_sdis(
     *                                            id_SDIS VARCHAR(50) PRIMARY KEY,
     *                                            nom_SDIS VARCHAR(50),
     *                                            adresse VARCHAR(50) NOT NULL
     */

    private String id;
    private String nom;
    private String adresse;

    public SDIS(){
        this.id = null;
        this.nom = null;
        this.adresse = null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getId() {
        return id;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return "SDIS{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }

    public static List<SDIS> generateSDIS(int nbSDIS) throws IOException {
        List<SDIS> liste_sdis = new ArrayList<>();

        Random rand = new Random();
        List<Localisation> localisations = Localisation.generateAllLocation(nbSDIS);


        for(int i =0; i<nbSDIS; i++){
            SDIS sdis = new SDIS();

            sdis.setAdresse(localisations.get(i).toString());
            sdis.setId("SDIS"+(i+1));
            sdis.setNom("SDIS n° "+i);

            liste_sdis.add(sdis);

        }
        return liste_sdis;
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {
        Connection conn = null;
        Utils Utils = new Utils();

        List<SDIS>liste_sdis = generateSDIS(10);
        Random rand = new Random();

        try{
            conn = DriverManager.getConnection(url,user,password);

            for(SDIS s : liste_sdis){
                String sql = "INSERT INTO " +
                        "`llx_resisteccsamusmur_sdis`(" +
                        "`id_SDIS`, " +
                        "`nom_SDIS`, " +
                        "`adresse`) " +
                        "VALUES (?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, s.id);
                preparedStatement.setString(2, s.nom);
                preparedStatement.setString(3, s.adresse);

                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<SDIS> collectSQL(String url,
                                       String user,
                                       String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<SDIS> vecteurs = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_sdis";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                String id = resultSQL.getString("id_SDIS");

                SDIS v = new SDIS();
                v.id = id;

                vecteurs.add(v);
            }

            return vecteurs;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
