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

public class Ambulance {

    /**
     * CREATE TABLE llx_resisteccsamusmur_ambulances(
     *                                                  id_Ambulance VARCHAR(50) PRIMARY KEY,
     *                                                  nom_ambulance VARCHAR(50),
     *                                                  tel_ambulance VARCHAR(50),
     *                                                  ambulance_dispo INT DEFAULT 0,
     *                                                  adresse VARCHAR(50) NOT NULL
     */

    private String id;
    private String nom;
    private String tel;
    private int nbAmbulance;
    private String adresse;

    public Ambulance(){
        this.id = null;
        this.nom = null;
        this.tel = null;
        this.nbAmbulance = 0;
        this.adresse = null;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public void setNbAmbulance(int nbAmbulance) {
        this.nbAmbulance = nbAmbulance;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getTel() {
        return tel;
    }

    public int getNbAmbulance() {
        return nbAmbulance;
    }

    @Override
    public String toString() {
        return "Ambulance{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", tel='" + tel + '\'' +
                ", nbAmbulance=" + nbAmbulance +
                ", adresse='" + adresse + '\'' +
                '}';
    }

    private static String randomTel(){
        String tel = "08";
        Random rand = new Random();

        for(int i =0; i<8;i++){
            tel = tel+rand.nextInt(10);
        }
        return  tel;
    }

    public List<Ambulance> setIds(List<Ambulance> meds){
        int index = 1;
        for(Ambulance t : meds){
            t.setId("AMB"+index);
            index++;
        }
        return meds;
    }

    public static List<Ambulance> generateAmbulance(int nbAmbulance) throws IOException {
        List<Ambulance> ambulances = new ArrayList<>();

        Random rand = new Random();
        List<Localisation> localisations = Localisation.generateAllLocation(nbAmbulance);


        for(int i =0; i<nbAmbulance; i++){
            Ambulance victime = new Ambulance();

            victime.setTel(Ambulance.randomTel());
            victime.setAdresse(localisations.get(i).toString());
            victime.setId("AMB"+(i+1));
            victime.setNom("Ambulance "+i);
            victime.setNbAmbulance(rand.nextInt(6));

            ambulances.add(victime);

        }
        return ambulances;
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {
        Connection conn = null;
        Utils Utils = new Utils();

        List<Ambulance>ambulances = generateAmbulance(10);
        Random rand = new Random();

        try{
            conn = DriverManager.getConnection(url,user,password);
            System.out.println("Connected to the DB");

            for(Ambulance a : ambulances){
                String sql = "INSERT INTO " +
                        "`llx_resisteccsamusmur_ambulances`(" +
                        "`id_Ambulance`, " +
                        "`nom_ambulance`, " +
                        "`tel_ambulance`, " +
                        "`ambulance_dispo`, " +
                        "`adresse`) " +
                        "VALUES (?,?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, a.id);
                preparedStatement.setString(2, a.nom);
                preparedStatement.setString(3, a.tel);
                preparedStatement.setInt(4, a.nbAmbulance);
                preparedStatement.setString(5, a.adresse);

                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
