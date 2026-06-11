package samu_intervention;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vecteur_et_details.Vecteur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TypeDAMU {

    private static  final String TABLENAME = "llx_resisteccsamusmur_type_bamu";

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public TypeDAMU(String name){
        this.name = name;
        this.id = -1;
    }

    public TypeDAMU(){
        this.name = null;
        this.id = -1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TypeDAMU{" +
                "name='" + name + '\'' +
                '}';
    }

    /**
     * Créer une liste de type de DAMU à insérer
     *
     * @author Lison Boo
     * @return La liste des types
     */
    public static List<TypeDAMU> generateAllTypeDAMU() {
        try{
            List<TypeDAMU> typesBAMU = new ArrayList<TypeDAMU>();
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("BAMU_Type");

            // Iterating over rows using iterator
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    TypeDAMU type= new TypeDAMU("");
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                type.name = cell.getStringCellValue();
                                break;
                        }
                    }
                    typesBAMU.add(type);
                }
            }
            // Closing the workbook to free resources
            wb.close();

            return typesBAMU;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insert type des BAMU dans la table llx_resisteccsamusmur_type_bamu
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire l'insertion
     * @param password mot de passe du compte de la base de données qui va faire l'insertion
     * @throws IOException voir generateAllTypeDAMU
     */
    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException {
        List<TypeDAMU> listTypeDamu = generateAllTypeDAMU();
        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(TypeDAMU t : listTypeDamu){
                String sql = "INSERT INTO `llx_resisteccsamusmur_type_bamu`(`nom`) " +
                        "VALUES (?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, t.getName());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Collecte tout les types DAMU de la table llx_resisteccsamusmur_type_bamu
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @return Liste des types de DAMU
     */
    public static List<TypeDAMU> collect(String url, String user, String password){
        List<TypeDAMU> list_DAMU = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(url, user, password)){
            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_type_bamu";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                int id_type_BAMU = resultSQL.getInt("id_type_BAMU");
                String nom = resultSQL.getString("nom");

                TypeDAMU t = new TypeDAMU(nom);
                t.setId(id_type_BAMU);
                list_DAMU.add(t);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list_DAMU;
    }

    /**
     * Collecte un type de BAMU selon l'id
     *
     * @author Lison Boo
     * @param url adresse url vers la base de données
     * @param user login du compte de la base de données qui va faire la lecture
     * @param password mot de passe du compte de la base de données qui va faire la lecture
     * @param id id du type de BAMU recherché
     * @return le type vide (si id non trouvé) ou type avec infos complétes
     */
    public static TypeDAMU collectOne(String url, String user, String password, int id){
        TypeDAMU t = new TypeDAMU();

        try(Connection conn = DriverManager.getConnection(url, user, password)){
            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_type_bamu WHERE id_type_BAMU ="+id;
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                int id_type_BAMU = resultSQL.getInt("id_type_BAMU");
                String nom = resultSQL.getString("nom");

                t.setName(nom);
                t.setId(id_type_BAMU);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return t;
    }

    private void setName(String nom) {
        this.name = nom;
    }


}
