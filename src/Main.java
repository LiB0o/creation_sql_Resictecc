import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import organisme_soin.TeleMedicalisation;
import samu_intervention.*;
import localisation.*;
import skill.Join_Skill_Job;
import skill.Join_Skill_User;
import skill.Skill;
import staff.Job;
import staff.SAMU;
import staff.SMUR;
import staff.User;
import staff.*;
import vecteur_et_details.*;
import victimes.Victime;

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
            //User.insertSQL(URL_OP,USER,PASSWORD);
            //Skill.insertSQL(URL_OP, USER, PASSWORD);

            //Join_Skill_Job.insertSQL(URL_OP,USER,PASSWORD);
            //Join_Staff_Job.insertSQL(URL_OP,USER,PASSWORD);
            //Join_Skill_User.insertSQL(URL_OP,USER,PASSWORD);
            Join_SMUR_User.insertSQL(URL_OP,USER,PASSWORD);

            //Victime.insertSQL(URL_OP,USER,PASSWORD);

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