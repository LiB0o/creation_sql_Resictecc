import java.sql.Connection;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import java.sql.DriverManager;


import staff.*;
import samu_intervention.*;
import organisme_soin.*;
import skill.*;
import vecteur_et_details.*;
import victimes.*;
import utilitaire.*;


public class Main {

    /**
     * Efface le contenu des tables de la base de données
     * @author Eloise
     * @throws Exception Si la connection à la base de données ne marche pas
     */
    public static void resetDatabase() throws Exception {

        try (
                Connection conn = DriverManager.getConnection(URL_OP, USER, PASSWORD);
                Statement stmt = conn.createStatement()
        ) {

            // Suppression des utilisateurs générés
            stmt.executeUpdate("DELETE FROM llx_user WHERE rowid > 15000");
            stmt.executeUpdate("ALTER TABLE llx_user AUTO_INCREMENT = 15001");

            // Désactivation des contraintes FK
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            // Tables de liaison
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_concerner");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_envoyer_pds");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_envoyer_ambulance");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_envoyer_telemedicalisation");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_envoyer_sdis");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_employer_samu");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_planifier_lecons");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_enseigner");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_posseder");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_employer_smur");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_competence_necessaire");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_obtention_competence");

            // Tables métier
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_envoi_ressource");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_bamu");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_pds");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_ambulances");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_telemedicalisation");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_sdis");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_damu");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_materiels");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_vecteur");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_smur");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_type_materiels");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_samu");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_competences");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_staff");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_type_staff");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_victimes");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_type_vecteur");
            stmt.executeUpdate("DELETE FROM llx_resisteccsamusmur_type_bamu");

            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            // HRM
            stmt.executeUpdate("DELETE FROM llx_hrm_skillrank_extrafields");
            stmt.executeUpdate("DELETE FROM llx_hrm_skillrank");
            stmt.executeUpdate("DELETE FROM llx_hrm_skill");
            stmt.executeUpdate("DELETE FROM llx_hrm_job");

            // Reset auto increment
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_envoi_ressource AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_bamu AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_materiels AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_smur AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_competences AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_staff AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_type_staff AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_victimes AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_type_vecteur AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_type_bamu AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE llx_resisteccsamusmur_type_materiels AUTO_INCREMENT = 1");

            // Réactivation des contraintes FK


            System.out.println("Base réinitialisée avec succès.");
        }
    }

    private static String URL_OP = "jdbc:mysql://localhost:3306/dolibarr_op";
    private static String USER = "root";
    private static String PASSWORD = "";
    private static LocalDate DATE = LocalDate.now();


    public static void main(String[] args) {
        Connection conn = null;
        Statement stat = null;

        if(args.length < 3){
            System.out.println("Erreur, le nombre d'argument n'est pas suffisant. Attendu:" +
                    "[Url compléte de la base de donnée] [login administrator] [date de l'exercice] [mot de passe administrator si il n'est pas vide]");
        }
        else{

            URL_OP = "jdbc:mysql://"+args[0];
            DATE = LocalDate.parse(args[2]);
            USER = args[1];
            if(args.length >= 4){
                PASSWORD = args[3];
            }
            else{
                PASSWORD = "";
            }


            try{

                resetDatabase();


            TypeDAMU.insertSQL(URL_OP,USER,PASSWORD);
            TypeMateriel.insertSQL(URL_OP,USER,PASSWORD);
            TypeVecteur.insertSQL(URL_OP,USER,PASSWORD);
            SAMU.insertSQL(URL_OP,USER,PASSWORD);
            SMUR.insertSQL(URL_OP,USER,PASSWORD);
            Vecteur.insertSQL(URL_OP,USER,PASSWORD, DATE);
            Materiel.insertSQL(URL_OP,USER,PASSWORD, DATE);
            Job.insertSQL(URL_OP,USER,PASSWORD, DATE);
            User.insertSQL(URL_OP,USER,PASSWORD);
            Skill.insertSQL(URL_OP, USER, PASSWORD, DATE);

            Join_Skill_Job.insertSQL(URL_OP,USER,PASSWORD, DATE);
            Join_Staff_Job.insertSQL(URL_OP,USER,PASSWORD, DATE);
            Join_Skill_User.insertSQL(URL_OP,USER,PASSWORD, DATE);
            Join_SMUR_User.insertSQL(URL_OP,USER,PASSWORD, DATE);
            Join_SAMU_User.insertSQL(URL_OP,USER,PASSWORD, DATE);

            Victime.insertSQL(URL_OP,USER,PASSWORD);

            TeleMedicalisation.insertSQL(URL_OP,USER,PASSWORD);
            Ambulance.insertSQL(URL_OP,USER,PASSWORD);

            SDIS.insertSQL(URL_OP,USER,PASSWORD);

            PDS.insertSQL(URL_OP,USER,PASSWORD);


            DAMU.insertSQL(URL_OP,USER,PASSWORD, DATE);
            Join_Victime_DAMU.insertSQL(URL_OP,USER,PASSWORD);

            BAMU.insertSQL(URL_OP, USER, PASSWORD);

            List<BAMU> bamus = Join_BAMU_All.insertSQL(URL_OP, USER, PASSWORD);

            EnvoieSMUR.insertSQL(URL_OP, USER, PASSWORD, bamus);

            }catch(Exception e){
                e.printStackTrace();
            }
        }


    }
}
