package victimes;

import samu_intervention.DAMU;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Join_Victime_DAMU {
    private int id_victime;
    private String id_damu;
    private int id_type_damu;

    public Join_Victime_DAMU() {
        this.id_victime = -1;
        this.id_type_damu = -1;
        this.id_damu = null;
    }

    public static List<Join_Victime_DAMU> generateAllJoin(String url, String user, String password) {
        try {
            List<Join_Victime_DAMU> listJoin = new ArrayList<>();
            List<DAMU> listDamu = DAMU.collectSQL(url, user, password);
            List<Victime> listVic = Victime.collectSQL(url, user, password);

            for (DAMU d : listDamu) {
                String id = d.getId();
                int type = d.getId_type_BAMU();

                Random rand = new Random();
                int nbVic = rand.nextInt(4);

                for(int i = 0; i < nbVic; i++){
                    Join_Victime_DAMU join = new Join_Victime_DAMU();

                    join.id_damu = id;
                    join.id_type_damu = type;
                    join.id_victime = listVic.get(rand.nextInt(listVic.size())).getId();

                    listJoin.add(join);
                }
            }

            return listJoin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void insertSQL(String url, String user, String password) throws SQLException {
        List<Join_Victime_DAMU> listJoin = Join_Victime_DAMU.generateAllJoin(url, user, password);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            for(Join_Victime_DAMU join : listJoin) {
                String sql = "INSERT INTO `llx_resisteccsamusmur_concerner`(`id_victime`,`id_type_BAMU`,`id_demande`)" +
                            "VALUES (?,?,?)";
                PreparedStatement p = conn.prepareStatement(sql);
                p.setInt(1, join.id_victime);
                p.setInt(2, join.id_type_damu);
                p.setString(3, join.id_damu);
                p.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
