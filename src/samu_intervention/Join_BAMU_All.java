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

                if(total < nbBAMU/5) {

                    for(int i = 0; i < nb; i++) {
                        Join_BAMU_All join = new Join_BAMU_All();

                        join.id_bamu = id;
                        join.id_ambulance = listAmbulance.get(rand.nextInt(listAmbulance.size())).getId();

                        listJoin.add(join);
                    }
                } else if (total < (nbBAMU/5)*2) {

                    for(int i = 0; i < nb; i++) {
                        Join_BAMU_All join = new Join_BAMU_All();

                        join.id_bamu = id;
                        join.id_pds = listPDS.get(rand.nextInt(listPDS.size())).getId();

                        listJoin.add(join);
                    }
                } else if (total < (nbBAMU/5)*3) {
                    for(int i = 0; i < nb; i++) {
                        Join_BAMU_All join = new Join_BAMU_All();

                        join.id_bamu = id;
                        join.id_sdis = listSDIS.get(rand.nextInt(listSDIS.size())).getId();

                        listJoin.add(join);
                    }
                } else if (total < nbBAMU - (nbBAMU/5)) {
                    for(int i = 0; i < nb; i++) {
                        Join_BAMU_All join = new Join_BAMU_All();

                        join.id_bamu = id;
                        join.id_telmed = listTelMed.get(rand.nextInt(listTelMed.size())).getId();

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

    public static void insertSQL(String url, String user, String password) throws SQLException {
        List<Join_BAMU_All> listJoin = Join_BAMU_All.generateAllJoin(url, user, password);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            for(Join_BAMU_All join : listJoin) {

                String type = join.id_ambulance != null ? "ambulance"
                        : join.id_pds       != null ? "pds"
                        : join.id_sdis      != null ? "sdis"
                        : join.id_telmed    != null ? "telmed"
                        : null;

                if (type == null) continue;

                String sql;
                String id;

                switch (type) {
                    case "ambulance" -> {
                        sql = "INSERT INTO llx_resisteccsamusmur_envoyer_ambulance(id_intervention, id_Ambulance) VALUES (?, ?)";
                        id  = join.id_ambulance;
                    }
                    case "pds" -> {
                        sql = "INSERT INTO llx_resisteccsamusmur_envoyer_pds(id_intervention, id_PDS) VALUES (?, ?)";
                        id  = join.id_pds;
                    }
                    case "sdis" -> {
                        sql = "INSERT INTO llx_resisteccsamusmur_envoyer_sdis(id_intervention, id_SDIS) VALUES (?, ?)";
                        id  = join.id_sdis;
                    }
                    case "telmed" -> {
                        sql = "INSERT INTO llx_resisteccsamusmur_envoyer_telemedicalisation(id_intervention, id_medecin) VALUES (?, ?)";
                        id  = join.id_telmed;
                    }
                    default -> { continue; }
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
    }


    public static List<BAMU> collectBAMULeft(String url, String user, String password) {
        List<Join_BAMU_All> listJoin = Join_BAMU_All.generateAllJoin(url, user, password);
        List<BAMU> listBAMU = BAMU.collectSQL(url,user,password);
        List<BAMU> listBAMULeft = new ArrayList<>();

        for (Join_BAMU_All join : listJoin) {
            boolean isLeft = join.id_ambulance == null
                    && join.id_pds       == null
                    && join.id_sdis      == null
                    && join.id_telmed    == null;

            if (isLeft) {
                listBAMULeft.add(listBAMU.get(join.id_bamu));
            }
        }

        return listBAMULeft;
    }
}
