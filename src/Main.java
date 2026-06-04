import java.sql.Connection;

import java.sql.Statement;
import java.util.List;


import staff.*;
import samu_intervention.*;
import organisme_soin.*;
import skill.*;
import vecteur_et_details.*;
import victimes.*;
import utilitaire.*;


public class Main {

    private static final String URL_OP = "jdbc:mysql://localhost:3306/dolibarr_op";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    public static void main(String[] args) {
        Connection conn = null;
        Statement stat = null;

        try{
            

            /*TypeDAMU.insertSQL(URL_OP,USER,PASSWORD);
            TypeMateriel.insertSQL(URL_OP,USER,PASSWORD);
            TypeVecteur.insertSQL(URL_OP,USER,PASSWORD);
            SAMU.insertSQL(URL_OP,USER,PASSWORD);
            SMUR.insertSQL(URL_OP,USER,PASSWORD);
            Vecteur.insertSQL(URL_OP,USER,PASSWORD);
            Materiel.insertSQL(URL_OP,USER,PASSWORD);
            Job.insertSQL(URL_OP,USER,PASSWORD);
            User.insertSQL(URL_OP,USER,PASSWORD);
            Skill.insertSQL(URL_OP, USER, PASSWORD);

            Join_Skill_Job.insertSQL(URL_OP,USER,PASSWORD);
            Join_Staff_Job.insertSQL(URL_OP,USER,PASSWORD);
            Join_Skill_User.insertSQL(URL_OP,USER,PASSWORD);
            Join_SMUR_User.insertSQL(URL_OP,USER,PASSWORD);
            Join_SAMU_User.insertSQL(URL_OP,USER,PASSWORD);
          
            Victime.insertSQL(URL_OP,USER,PASSWORD);

            TeleMedicalisation.insertSQL(URL_OP,USER,PASSWORD);
            Ambulance.insertSQL(URL_OP,USER,PASSWORD);

            SDIS.insertSQL(URL_OP,USER,PASSWORD);

            PDS.insertSQL(URL_OP,USER,PASSWORD);


            DAMU.insertSQL(URL_OP,USER,PASSWORD);
            Join_Victime_DAMU.insertSQL(URL_OP,USER,PASSWORD);

            BAMU.insertSQL(URL_OP, USER, PASSWORD);*/

            List<BAMU> bamus = Join_BAMU_All.insertSQL(URL_OP, USER, PASSWORD);

            EnvoieSMUR.insertSQL(URL_OP, USER, PASSWORD, bamus);

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