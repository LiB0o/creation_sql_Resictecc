package vecteur_et_details;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TypeVecteur {

    private static  final String TABLENAME = "llx_resisteccsamusmur_type_vecteur";

    private int id;

    private String nom;

    public TypeVecteur(int id, String name){
        this.id = id;
        this.nom = name;
    }

    public TypeVecteur(){
        this.id = -1;
        this.nom = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "TypeVecteur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }


    public static List<TypeVecteur> generateAllTypeVecteur() throws IOException {
        try{
            List<TypeVecteur> typesVecteur = new ArrayList<TypeVecteur>();
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Vecteur_Type");

            // Iterating over rows using iterator
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    TypeVecteur type= new TypeVecteur();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                type.nom = cell.getStringCellValue();
                                break;
                        }
                    }
                    typesVecteur.add(type);
                }
            }
            // Closing the workbook to free resources
            wb.close();

            return typesVecteur;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<TypeVecteur> collectSQL(String url,
                                               String user,
                                               String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<TypeVecteur> typeVecteurs = new ArrayList<>();

            Statement stat = null;

            System.out.println("Connected to the DB");

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_type_vecteur";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {
                //System.out.println("name from SQL "+resultSQL.getString("name"));

                int id = resultSQL.getInt("id_type_vecteur");
                String nom = resultSQL.getString("name");
                TypeVecteur v = new TypeVecteur(id,nom);
                typeVecteurs.add(v);
            }

            return typeVecteurs;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException {
        List<TypeVecteur> listTypeVecteur = generateAllTypeVecteur();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            System.out.println("Connected to the DB");

            for(TypeVecteur t : listTypeVecteur){
                String sql = "INSERT INTO `llx_resisteccsamusmur_type_vecteur`(`name`) " +
                        "VALUES (?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, t.getNom());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
