import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import samu_intervention.*;
import localisation.*;
import staff.Job;
import staff.SAMU;
import staff.SMUR;
import staff.User;
import vecteur_et_details.*;

public class Main {

    private static final String URL_OP = "jdbc:mysql://localhost:3306/dolibarr_op";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    public static void main(String[] args) {
        Connection conn = null;
        Statement stat = null;

        try{
            //TypeDAMU.insertSQL(URL_OP,USER,PASSWORD);
            //TypeLocalisation.insertSQL(URL_OP,USER,PASSWORD);
            //TypeMateriel.insertSQL(URL_OP,USER,PASSWORD);
            //TypeVecteur.insertSQL(URL_OP,USER,PASSWORD);
            //SAMU.insertSQL(URL_OP,USER,PASSWORD);
            //SMUR.insertSQL(URL_OP,USER,PASSWORD);
            //Vecteur.insertSQL(URL_OP,USER,PASSWORD);
            //Materiel.insertSQL(URL_OP,USER,PASSWORD);
            //Job.insertSQL(URL_OP,USER,PASSWORD);
            User.insertSQL(URL_OP,USER,PASSWORD);

            /*List<User> users = User.generateAllMaleUser("password",10);
            for(User u : users){
                System.out.println(u.toString());
            }*/


            /*conn = DriverManager.getConnection(URL_OP,USER,PASSWORD);
            System.out.println("Connected to the DB");

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_user";
            ResultSet resultSQL = stat.executeQuery(sql);

            while(resultSQL.next()){
                int id = resultSQL.getInt("rowid");
                int entity = resultSQL.getInt("entity");
                System.out.println("ID de user:"+id+" - "+entity+"\n");
            }*/

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}