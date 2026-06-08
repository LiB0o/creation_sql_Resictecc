package vecteur_et_details;

import utilitaire.Utils;
import staff.SMUR;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Vecteur {

    /**
     * Hélicoptère : DH - [3 lettres entre A et Z]
     * VHL : [2 lettres entre A et Z] - [3 nombres entre 0 et 9] - [2 lettres entre A et Z]
     * UMH : UMH - [3 nombres entre 0 et 9]
     */

    private static  final String TABLENAME = "llx_resisteccsamusmur_vecteur";
    private static final List<String> status_liste = Arrays.asList("Disponible", "En maintenance", "Envoyé");
    private static final List<Character> char_liste = Arrays.asList(
            'A', 'B', 'C',
            'D','E','F',
            'G', 'H','I',
            'J', 'K','L',
            'M','N','O',
            'P','Q','R',
            'S','T','U',
            'V','W','X',
            'Y','Z');

    private String immatriculation;
    private int nbKM; //sert aussi pour les hélicos
    private Date date_aquis;
    private Date date_last_checkup;
    private String status;
    private int id_type_vecteur;
    private int id_SMUR;

    public Vecteur(){
        this.immatriculation = null;
        this.nbKM = -1;
        this.id_type_vecteur = -1;
        this.date_aquis = null;
        this.date_last_checkup = null;
        this.status = null;
    }

    public Vecteur(String immatriculation,
                     int nbKM,
                     Date date_aquis,
                     Date date_last_checkup,
                     String status,
                     int id_type_vecteur,
                     int id_SMUR){
        this.immatriculation = immatriculation;
        this.nbKM = nbKM;
        this.date_aquis = date_aquis;
        this.date_last_checkup = date_last_checkup;
        this.status = status;
        this.id_type_vecteur = id_type_vecteur;
        this.id_SMUR = id_SMUR;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate_aquis(Date date_aquis) {
        this.date_aquis = date_aquis;
    }

    public void setDate_last_checkup(Date date_last_checkup) {
        this.date_last_checkup = date_last_checkup;
    }

    public void setId_type_vecteur(int id_type_vecteur) {
        this.id_type_vecteur = id_type_vecteur;
    }

    public void setNbKM(int nbKM) {
        this.nbKM = nbKM;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public void setId_SMUR(int id_SMUR) {
        this.id_SMUR = id_SMUR;
    }

    public int getId_SMUR() {
        return id_SMUR;
    }

    public Date getDate_aquis() {
        return date_aquis;
    }

    public Date getDate_last_checkup() {
        return date_last_checkup;
    }

    public int getNbKM() {
        return nbKM;
    }

    public int getId_type_vecteur() {
        return id_type_vecteur;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Vecteur{" +
                "immatriculation='" + immatriculation + '\'' +
                ", nbKM=" + nbKM +
                ", date_aquis=" + date_aquis +
                ", date_last_checkup=" + date_last_checkup +
                ", status='" + status + '\'' +
                ", id_type_vecteur=" + id_type_vecteur +
                ", id_SMUR=" + id_SMUR +
                '}';
    }

    public static List<Vecteur> generateAllVecteur(String url,
                                                   String user,
                                                   String password, LocalDate date) throws SQLException {

            Random rand = new Random();
            List<Vecteur> vecteurs = new ArrayList<Vecteur>();

            List<TypeVecteur> typesVecteur= TypeVecteur.collectSQL(url,user,password);
            int nbType = typesVecteur.size();

            List<SMUR> smurs = SMUR.collectSQL(url,user,password);
            int nbSMUR = smurs.size();

            int valueMaintenance;

            for(int i=0; i<nbType; i++){
                for(int j = 0; j<10;j++){
                    Vecteur v = new Vecteur();

                    //statut_vecteur
                    valueMaintenance = rand.nextInt(100)+1;
                    if(valueMaintenance == 1){
                        v.setStatus(status_liste.get(1)); //En Maintenance
                    }
                    else{
                        v.setStatus(status_liste.get(0));
                    }

                    String value = typesVecteur.get(i).getNom();

                    //immatriculation et id_type_vecteur
                    switch(value){
                        case "VHL": v.setImmatriculation(v.generateImmatriculationVHL(vecteurs));
                                    v.setId_type_vecteur(typesVecteur.get(i).getId());
                                    break;
                        case "UMH": v.setImmatriculation(v.generateImmatriculationUMH(vecteurs));
                                    v.setId_type_vecteur(typesVecteur.get(i).getId());
                                    break;
                        case "Hélicoptère":
                                    v.setImmatriculation(v.generateImmatriculationHelico(vecteurs));
                                    v.setId_type_vecteur(typesVecteur.get(i).getId());
                                    break;
                        default : Exception e;
                    }

                    v.setNbKM(rand.nextInt(100000)); //nbmax

                    LocalDate localDate = date;
                    v.setDate_aquis(java.sql.Date.valueOf(Utils.addDays(localDate,-rand.nextInt(1095))));
                    v.setDate_last_checkup(java.sql.Date.valueOf(Utils.addDays(localDate,-rand.nextInt(1095))));

                    v.setId_SMUR(smurs.get(rand.nextInt(nbSMUR)).getId());

                    vecteurs.add(v);
                }
            }
            return vecteurs;
    }



    private String generateImmatriculationHelico(List<Vecteur> vecteurs){
        Random rand = new Random();

        String immatriculation = "DH-"+char_liste.get(rand.nextInt(26))
                                    +char_liste.get(rand.nextInt(26))
                                    +char_liste.get(rand.nextInt(26));

        for(Vecteur v: vecteurs){
            if(immatriculation.equals(v.immatriculation)){
                immatriculation = generateImmatriculationHelico(vecteurs); //oui c'est récursif
            }
        }

        return immatriculation;
    }

    private String generateImmatriculationVHL(List<Vecteur> vecteurs){
        Random rand = new Random();

        String immatriculation = ""+char_liste.get(rand.nextInt(26))
                +char_liste.get(rand.nextInt(26))+"-"
                + rand.nextInt(10)+rand.nextInt(10)+rand.nextInt(10)
                + "-" +char_liste.get(rand.nextInt(26)) +char_liste.get(rand.nextInt(26))
                ;

        for(Vecteur v: vecteurs){
            if(immatriculation.equals(v.immatriculation)){
                immatriculation = generateImmatriculationVHL(vecteurs); //oui c'est récursif
            }
        }

        return immatriculation;
    }

    private String generateImmatriculationUMH(List<Vecteur> vecteurs){
        Random rand = new Random();

        String immatriculation = "UMH-"+ rand.nextInt(10)
                +rand.nextInt(10)
                +rand.nextInt(10)
                ;

        for(Vecteur v: vecteurs){
            if(immatriculation.equals(v.immatriculation)){
                immatriculation = generateImmatriculationUMH(vecteurs); //oui c'est récursif
            }
        }

        return immatriculation;
    }


    public static void insertSQL(String url,
                                 String user,
                                 String password, LocalDate date) throws IOException, SQLException {
        List<Vecteur> listVecteur = generateAllVecteur(url,user,password, date);
        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(Vecteur v : listVecteur){
                String sql = "INSERT INTO " +
                        "`llx_resisteccsamusmur_vecteur`(" +
                        "`immatriculation`, " +
                        "`nb_km`, " +
                        "`date_aquis`, " +
                        "`date_controle`, " +
                        "`statut_vecteur`, " +
                        "`id_type_vecteur`, " +
                        "`id_smur`)"+
                        "VALUES (?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, v.getImmatriculation());
                preparedStatement.setInt(2,v.getNbKM());
                preparedStatement.setDate(3,v.getDate_aquis());
                preparedStatement.setDate(4,v.getDate_last_checkup());
                preparedStatement.setString(5,v.getStatus());
                preparedStatement.setInt(6,v.getId_type_vecteur());
                preparedStatement.setInt(7,v.getId_SMUR());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Vecteur> collectSQL(String url,
                                               String user,
                                               String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<Vecteur> vecteurs = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_vecteur";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                String immatriculation = resultSQL.getString("immatriculation");
                int nbKM = resultSQL.getInt("nb_km");
                Date date_aquis = resultSQL.getDate("date_aquis");
                Date date_last_checkup = resultSQL.getDate("date_controle");
                String status = resultSQL.getString("statut_vecteur");
                int id_type_vecteur = resultSQL.getInt( "id_type_vecteur");
                int id_SMUR= resultSQL.getInt( "id_smur");

                Vecteur v = new Vecteur(immatriculation,nbKM,date_aquis,date_last_checkup,status,id_type_vecteur,id_SMUR);
                vecteurs.add(v);
            }

            return vecteurs;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
