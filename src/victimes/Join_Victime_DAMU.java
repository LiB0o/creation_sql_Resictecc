package victimes;

import samu_intervention.DAMU;

import java.sql.*;
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

    public String getId_damu() {
        return id_damu;
    }

    public int getId_victime() {
        return id_victime;
    }

    public int getId_type_damu() {
        return id_type_damu;
    }

    /**
     * Read the excel GL_SQL_datas.xlsx to generate a list of link between victim and DAMU
     * @param url : url toward the database
     * @param user : login to the database
     * @param password : password of the login
     * @return list of link between victim and DAMU ready to be inserted
     */
    public static List<Join_Victime_DAMU> generateAllJoin(String url, String user, String password) {
        try {
            List<Join_Victime_DAMU> listJoin = new ArrayList<>();
            List<DAMU> listDamu = DAMU.collectSQL(url, user, password);
            List<Victime> listVic = Victime.collectSQL(url, user, password);

            for (DAMU d : listDamu) {
                String id = d.getId();
                int type = d.getId_type_BAMU();

                Random rand = new Random();
                int nbVic = 1;

                ArrayList<Integer> ids_victimes = new ArrayList<>();

                for(int i = 0; i < nbVic; i++){
                    Join_Victime_DAMU join = new Join_Victime_DAMU();

                    int idVic = rand.nextInt(listVic.size());
                    idVic = uniqueId(ids_victimes,idVic, listVic.size());

                    join.id_damu = id;
                    join.id_type_damu = type;
                    join.id_victime = listVic.get(idVic).getId();

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
     * check id idVic is already used or not and if it is, we relaunch with another id
     * @param idsVictimes : list of ids already used
     * @param idVic : id of victim having to be verified
     * @param range : number of total victims
     * @return id of victim not used
     */
    private static int uniqueId(ArrayList<Integer> idsVictimes, int idVic, int range) {

        for(int ids : idsVictimes){
            if(ids == idVic){
                Random rand = new Random();
                idVic = uniqueId(idsVictimes,rand.nextInt(range) ,range);
            }
        }

        return idVic;
    }


    /**
     * insert into the table concerner the generated data
     * @param url : url toward the database
     * @param user : login to the database
     * @param password : password of the login
     * @throws SQLException
     */
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

    /**
     * collect from the database the list of every link between victim and DAMU stored
     * @param url : url toward the database
     * @param user : login to the database
     * @param password : password of the login
     * @return list of link collected from the database
     */
    public static List<Join_Victime_DAMU> collectSQL(String url, String user, String password){
        List<Join_Victime_DAMU> list_join = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_concerner";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()){
                int id_victime = resultSQL.getInt("id_victime");
                int id_type = resultSQL.getInt("id_type_BAMU");
                String damu = resultSQL.getString("id_demande");

                Join_Victime_DAMU join = new Join_Victime_DAMU();
                join.id_damu = damu;
                join.id_victime = id_victime;
                join.id_type_damu = id_type;

                list_join.add(join);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_join;
    }
}
