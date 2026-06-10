package organisme_soin;

import localisation.Localisation;
import samu_intervention.BAMU;
import utilitaire.Utils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PDS {

    /**
     * CREATE TABLE llx_resisteccsamusmur_pds(
     *                                           id_PDS VARCHAR(50) PRIMARY KEY,
     *                                           prenom_PDS VARCHAR(50), -- à ignorer
     *                                           nom_PDS VARCHAR(50),
     *                                           tel_PDS VARCHAR(50),
     *                                           adresse VARCHAR(50) NOT NULL
     */

    private String id;
    private String nom;
    private String tel;
    private String adresse;

    public PDS(){
        this.id = null;
        this.nom = null;
        this.tel = null;
        this.adresse = null;
    }

    public String getId() {
        return id;
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

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "PDS{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
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
     * Génére une liste de permanances de soin pour être utilisé dans l'insertion dans la table assigné (llx_resisteccsamusmur_pds).
     *
     * @author Lison Boo
     * @param nbPDS Nombre de permanances de soin à générer
     * @return liste des permanance de soin
     * @throws IOException vient de la fonction Localisation.generateAllLocation pour la lecture dans le fichier Excel
     */
    public static List<PDS> generatePDS(int nbPDS) throws IOException {
        List<PDS> liste_pds = new ArrayList<>();

        Random rand = new Random();
        List<Localisation> localisations = Localisation.generateAllLocation(nbPDS);


        for(int i =0; i<nbPDS; i++){
            PDS pds = new PDS();

            pds.setTel(PDS.randomTel());
            pds.setAdresse(localisations.get(i).toString());
            pds.setId("PDS"+(i+1));
            pds.setNom("Permanence de soin n° "+(i+1));

            liste_pds.add(pds);

        }
        return liste_pds;
    }

    /**
     * Insert les permanences de soin dans la table de la base de données
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire l'insertion
     * @param password mot de passe du compte de la base de données qui va faire l'insertion
     * @throws IOException Voir la fonction generatePDS
     * @throws SQLException Si il y a eu un problème lors de la connection ou insertion
     */
    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {

        Utils Utils = new Utils();

        List<PDS>liste_pds = generatePDS(10);
        Random rand = new Random();

        try(Connection conn = DriverManager.getConnection(url, user, password)){

            for(PDS p : liste_pds){
                String sql = "INSERT INTO " +
                        "`llx_resisteccsamusmur_pds`(" +
                        "`id_PDS`, " +
                        "`nom_PDS`, " +
                        "`tel_PDS`, " +
                        "`adresse`) " +
                        "VALUES (?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, p.id);
                preparedStatement.setString(2, p.nom);
                preparedStatement.setString(3, p.tel);
                preparedStatement.setString(4, p.adresse);

                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Collecte toute les permanences de soin dans llx_resisteccsamusmur_pds
     *
     * @author Marine Virot
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @return Liste des permanances de soin
     * @throws SQLException Si il y a eu un problème lors de la connection ou insertion
     */
    public static List<PDS> collectSQL(String url,
                                        String user,
                                        String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<PDS> vecteurs = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_pds";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                String id = resultSQL.getString("id_PDS");

                PDS v = new PDS();
                v.id = id;

                vecteurs.add(v);
            }

            return vecteurs;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
