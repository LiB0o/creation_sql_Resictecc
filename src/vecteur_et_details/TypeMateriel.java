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

public class TypeMateriel {

    private static  final String TABLENAME = "llx_resisteccsamusmur_type_materiels";

    private int id;

    private String nom;

    public TypeMateriel(int id, String nom){
        this.id = id;
        this.nom = nom;
    }

    public TypeMateriel(){
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
        return "Type_Materiel{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }


    public static List<TypeMateriel> generateAllTypeMateriel() throws IOException {
        try{
            List<TypeMateriel> typesMateriel = new ArrayList<TypeMateriel>();
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Material_Type");

            // Iterating over rows using iterator
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    TypeMateriel type= new TypeMateriel();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                type.nom = cell.getStringCellValue();
                                break;
                        }
                    }
                    typesMateriel.add(type);
                }
            }
            // Closing the workbook to free resources
            wb.close();

            return typesMateriel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException {
        List<TypeMateriel> listTypeMateriel = generateAllTypeMateriel();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            System.out.println("Connected to the DB");

            for(TypeMateriel t : listTypeMateriel){
                String sql = "INSERT INTO `llx_resisteccsamusmur_type_materiels`(`nom_type_materiel`) " +
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

    public static List<TypeMateriel> collectSQL(String url,
                                               String user,
                                               String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<TypeMateriel> typeMateriels = new ArrayList<>();

            Statement stat = null;

            System.out.println("Connected to the DB");

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_type_materiels";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                int id = resultSQL.getInt("id_type_materiel");
                String nom = resultSQL.getString("nom_type_materiel");
                TypeMateriel t = new TypeMateriel(id,nom);
                typeMateriels.add(t);
            }

            return typeMateriels;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
