package staff;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vecteur_et_details.TypeVecteur;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SMUR {

    private static  final String TABLENAME = "llx_resisteccsamusmur_smur";

    private int id;

    private String nom;

    private String id_SAMU;

    public SMUR(int id, String nom, String id_SAMU){
        this.nom = nom;
        this.id = id;
        this.id_SAMU = id_SAMU;
    }

    public SMUR(){
        this.id = -1;
        this.nom = null;
        this.id_SAMU = null;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_SAMU(String id_SAMU) {
        this.id_SAMU = id_SAMU;
    }

    public String getNom() {
        return nom;
    }

    public int getId() {
        return id;
    }

    public String getId_SAMU() {
        return id_SAMU;
    }

    @Override
    public String toString() {
        return "SMUR{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", id_SAMU='" + id_SAMU + '\'' +
                '}';
    }

    public static List<SMUR> generateAllSMUR() throws IOException {
        try{
            List<SMUR> smurs = new ArrayList<SMUR>();
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("SMUR");

            // Iterating over rows using iterator
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    SMUR s= new SMUR();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                s.nom = cell.getStringCellValue();
                                break;
                            case 1:
                                s.id_SAMU = cell.getStringCellValue();
                        }
                    }
                    smurs.add(s);
                }
            }
            // Closing the workbook to free resources
            wb.close();

            return smurs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SMUR> collectSQL(String url,
                                               String user,
                                               String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<SMUR> smurs = new ArrayList<>();

            Statement stat = null;

            System.out.println("Connected to the DB");

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_resisteccsamusmur_smur";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {
                int id = resultSQL.getInt("idSMUR");
                String nom = resultSQL.getString("nom_SMUR");
                String id_SAMU = resultSQL.getString("id_SAMU");

                SMUR v = new SMUR(id,nom,id_SAMU);
                smurs.add(v);
            }

            return smurs;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException {
        List<SMUR> listSMUR = generateAllSMUR();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            System.out.println("Connected to the DB");

            for(SMUR s : listSMUR){
                String sql = "INSERT INTO `llx_resisteccsamusmur_smur`(`nom_SMUR`,`id_SAMU`) " +
                        "VALUES (?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, s.getNom());
                preparedStatement.setString(2, s.getId_SAMU());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
