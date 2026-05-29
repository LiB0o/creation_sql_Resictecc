package staff;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SAMU {

    private static  final String TABLENAME = "llx_resisteccsamusmur_samu";

    private String id;

    private String nom;

    public SAMU(String id, String nom){
        this.nom = nom;
        this.id = id;
    }

    public  SAMU(){
        this.id = null;
        this.nom = null;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SAMU{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }

    public static List<SAMU> generateAllSAMU() throws IOException {
        try{
            List<SAMU> samus = new ArrayList<SAMU>();
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("SAMU");

            // Iterating over rows using iterator
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    SAMU s= new SAMU();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                s.id = cell.getStringCellValue();
                                break;
                            case 1:
                                s.nom = cell.getStringCellValue();
                        }
                    }
                    samus.add(s);
                }
            }
            // Closing the workbook to free resources
            wb.close();

            return samus;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException {
        List<SAMU> listSAMU = generateAllSAMU();
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            System.out.println("Connected to the DB");

            for(SAMU s : listSAMU){
                String sql = "INSERT INTO `llx_resisteccsamusmur_samu`(`id_SAMU`,`nom`) " +
                        "VALUES (?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, s.getId());
                preparedStatement.setString(2, s.getNom());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
