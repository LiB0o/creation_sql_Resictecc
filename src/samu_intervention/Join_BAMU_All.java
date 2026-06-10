package samu_intervention;

import organisme_soin.Ambulance;
import organisme_soin.PDS;
import organisme_soin.SDIS;
import organisme_soin.TeleMedicalisation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Join_BAMU_All {
    private int id_bamu;
    private String id_ambulance;
    private String id_sdis;
    private String id_telmed;
    private String id_pds;

    public Join_BAMU_All() {
        this.id_ambulance = null;
        this.id_bamu = -1;
        this.id_sdis = null;
        this.id_telmed = null;
        this.id_pds = null;
    }

    /**
     * Change l'ambulance si elle est déjà présente dans la liste
     *
     * @author Marine Virot
     * @param idsVictimes Liste des ambulances déjà présentes
     * @param list Liste de toute les ambulances
     * @param idVic id de l'ambulance actuel
     * @param range Taille de list
     * @return id de l'ambulance choisie
     */
    private static String uniqueIdAmbulance(List<String> idsVictimes, List<Ambulance> list, String idVic, int range) {

        for(String ids : idsVictimes){
            if(Objects.equals(ids, idVic)){
                Random rand = new Random();
                idVic = uniqueIdAmbulance(idsVictimes,list,list.get(rand.nextInt(range)).getId() ,range);
            }
        }

        return idVic;
    }

    /**
     * Change la permanance de soin si elle est déjà présente dans la liste
     *
     * @author Marine Virot
     * @param idsVictimes Liste des permanances déjà présentes
     * @param list Liste de toutes les permanances
     * @param idVic id de la permanance actuel
     * @param range Taille de list
     * @return id de la permanance choisie
     */
    private static String uniqueIdPDS(List<String> idsVictimes, List<PDS> list, String idVic, int range) {

        for(String ids : idsVictimes){
            if(Objects.equals(ids, idVic)){
                Random rand = new Random();
                idVic = uniqueIdPDS(idsVictimes,list,list.get(rand.nextInt(range)).getId() ,range);
            }
        }

        return idVic;
    }

    /**
     * Change le SDIS si il est déjà présent dans la liste
     *
     * @author Marine Virot
     * @param idsVictimes Liste des SDIS déjà présents
     * @param list Liste de tout les SDIS
     * @param idVic id du SDIS actuel
     * @param range Taille de list
     * @return id du SDIS choisi
     */
    private static String uniqueIdSDIS(List<String> idsVictimes, List<SDIS> list, String idVic, int range) {

        for(String ids : idsVictimes){
            if(Objects.equals(ids, idVic)){
                Random rand = new Random();
                idVic = uniqueIdSDIS(idsVictimes,list,list.get(rand.nextInt(range)).getId() ,range);
            }
        }

        return idVic;
    }

    /**
     * Change l'id du médecin en télémédicalisation si il est déjà dans la liste
     *
     * @author Marine Virot
     * @param idsVictimes Liste des médecins déjà présents
     * @param list Liste de tout les Médecins
     * @param idVic id du Médecin actuel
     * @param range Taille de list
     * @return id du Médecin choisi
     */
    private static String uniqueIdTEL(List<String> idsVictimes, List<TeleMedicalisation> list, String idVic, int range) {

        for(String ids : idsVictimes){
            if(Objects.equals(ids, idVic)){
                Random rand = new Random();
                idVic = uniqueIdTEL(idsVictimes,list,list.get(rand.nextInt(range)).getId() ,range);
            }
        }

        return idVic;
    }


    /**
     * Génére toute les futures envoie des différents organismes (SDIS, PDS, Ambulances privées, Médecins Télémédicalisation)
     * A besoin de lire les tables de chaque organisme
     *
     * @author Marine Virot
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire les lectures
     * @param password mot de passe du compte de la base de données qui va faire les lectures
     * @return Liste des envoies
     */
    public static List<Join_BAMU_All> generateAllJoin(String url, String user, String password) {
        try {
            List<Join_BAMU_All> listJoin = new ArrayList<>();

            List<BAMU> listBAMU = BAMU.collectSQL(url,user,password);
            List<Ambulance> listAmbulance = Ambulance.collectSQL(url,user,password);
            List<PDS> listPDS = PDS.collectSQL(url,user,password);
            List<SDIS> listSDIS = SDIS.collectSQL(url,user,password);
            List<TeleMedicalisation> listTelMed = TeleMedicalisation.collectSQL(url,user,password);

            int nbBAMU = listBAMU.size();
            int total = 0;

            Random rand = new Random();

            while(total < nbBAMU) {
                int id = listBAMU.get(total).getId_inter();
                int nb = rand.nextInt(4);

                if(total %5 == 0) {
                    List<String> ids = new ArrayList<String>();

                    for(int i = 0; i < nb; i++) {
                        Join_BAMU_All join = new Join_BAMU_All();

                        join.id_bamu = id;
                        String idAmbu = listAmbulance.get(rand.nextInt(listAmbulance.size())).getId();
                        idAmbu = uniqueIdAmbulance(ids, listAmbulance, idAmbu, listAmbulance.size());
                        ids.add(idAmbu);
                        join.id_ambulance = idAmbu;

                        listJoin.add(join);
                    }
                }
                else if (total %5 == 1) {
                    List<String> ids = new ArrayList<String>();
                    for(int i = 0; i < nb; i++) {
                        Join_BAMU_All join = new Join_BAMU_All();

                        join.id_bamu = id;
                        String idPDS = listPDS.get(rand.nextInt(listPDS.size())).getId();
                        idPDS = uniqueIdPDS(ids, listPDS, idPDS, listPDS.size());
                        ids.add(idPDS);
                        join.id_pds = idPDS;

                        listJoin.add(join);
                    }
                }
                else if (total %5 == 2) {
                    List<String> ids = new ArrayList<String>();
                    for(int i = 0; i < nb; i++) {
                        Join_BAMU_All join = new Join_BAMU_All();

                        join.id_bamu = id;
                        String idSDIS = listSDIS.get(rand.nextInt(listSDIS.size())).getId();
                        idSDIS = uniqueIdSDIS(ids, listSDIS, idSDIS, listSDIS.size());
                        ids.add(idSDIS);
                        join.id_sdis = idSDIS;

                        listJoin.add(join);
                    }
                }
                else if (total %5 == 3) {
                    List<String> ids = new ArrayList<String>();
                    for(int i = 0; i < nb; i++) {
                        Join_BAMU_All join = new Join_BAMU_All();

                        join.id_bamu = id;
                        String idTEL = listTelMed.get(rand.nextInt(listTelMed.size())).getId();
                        idTEL = uniqueIdTEL(ids, listTelMed, idTEL, listTelMed.size());
                        ids.add(idTEL);
                        join.id_telmed = idTEL;

                        listJoin.add(join);
                    }
                } else {
                    Join_BAMU_All join = new Join_BAMU_All();

                    join.id_bamu = id;
                    listJoin.add(join);
                }

                total++;
            }


            return listJoin;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insert tout les envoies créer par generateAllJoin, renvoie une partie des BAMU qui n'a pas inserré pour les lier au SMUR
     *
     * @author Marine Virot
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire l'insertion
     * @param password mot de passe du compte de la base de données qui va faire l'insertion
     * @return Liste de BAMU
     * @throws SQLException Erreur si la connection/insertion c'est mal dérroulé
     */
    public static List<BAMU> insertSQL(String url, String user, String password) throws SQLException {
        List<Join_BAMU_All> listJoin = Join_BAMU_All.generateAllJoin(url, user, password);
        List<BAMU> listBamuLeft = new ArrayList<>();
        List<BAMU> listBAMU = BAMU.collectSQL(url, user, password);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            for (Join_BAMU_All join : listJoin) {

                String type = join.id_ambulance != null ? "ambulance"
                        : join.id_pds != null ? "pds"
                          : join.id_sdis != null ? "sdis"
                            : join.id_telmed != null ? "telmed"
                              : null;


                String sql;
                String id;

                switch (type) {
                    case "ambulance" -> {
                        sql = "INSERT INTO llx_resisteccsamusmur_envoyer_ambulance(id_intervention, id_Ambulance) VALUES (?, ?)";
                        id = join.id_ambulance;
                    }
                    case "pds" -> {
                        sql = "INSERT INTO llx_resisteccsamusmur_envoyer_pds(id_intervention, id_PDS) VALUES (?, ?)";
                        id = join.id_pds;
                    }
                    case "sdis" -> {
                        sql = "INSERT INTO llx_resisteccsamusmur_envoyer_sdis(id_intervention, id_SDIS) VALUES (?, ?)";
                        id = join.id_sdis;
                    }
                    case "telmed" -> {
                        sql = "INSERT INTO llx_resisteccsamusmur_envoyer_telemedicalisation(id_intervention, id_medecin) VALUES (?, ?)";
                        id = join.id_telmed;
                    }
                    case null -> {
                        listBamuLeft.add(BAMU.collectSQLOne(url, user, password, join.id_bamu));
                        continue;
                    }
                    default -> {
                        continue;
                    }
                }

                try (PreparedStatement p = conn.prepareStatement(sql)) {
                    p.setInt(1, join.id_bamu);
                    p.setString(2, id);
                    p.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listBamuLeft;
    }

}
