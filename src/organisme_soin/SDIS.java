package organisme_soin;

import localisation.Localisation;
import utilitaire.Utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

            //victime.setTel(Ambulance.randomTel());
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
            System.out.println("Connected to the DB");

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

}
