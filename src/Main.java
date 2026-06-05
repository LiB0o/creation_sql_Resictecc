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

    private static String URL_OP = "jdbc:mysql://localhost:3306/dolibarr_op";
    private static String USER = "root";
    private static String PASSWORD = "";


    public static void main(String[] args) {
        Connection conn = null;
        Statement stat = null;

        if(args.length < 2){
            System.out.println("Erreur, le nombre d'argument n'est pas suffisant. Attendu:" +
                    "[Url compléte de la base de donnée] [login administrator] [mot de passe administrator si il n'est pas vide]");
        }
        else{

            URL_OP = "jdbc:mysql://"+args[0];
            USER = args[1];
            if(args.length >= 3){
                PASSWORD = args[2];
            }
            else{
                PASSWORD = "";
            }


            try{


            TypeDAMU.insertSQL(URL_OP,USER,PASSWORD);
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

            BAMU.insertSQL(URL_OP, USER, PASSWORD);

            List<BAMU> bamus = Join_BAMU_All.insertSQL(URL_OP, USER, PASSWORD);

            for(BAMU b : bamus){
                System.out.println(b.toString());
            }

            EnvoieSMUR.insertSQL(URL_OP, USER, PASSWORD, bamus);

            }catch(Exception e){
                e.printStackTrace();
            }
        }


    }
}