package staff;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vecteur_et_details.Vecteur;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.sql.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Job {
    private static final String TABLENAME = "llx_hrm_job";

    private int id;
    private String name;
    private Date dateCrea;

    public String getName() {
        return name;
    }
    public Date getDateCrea(){return dateCrea;}

    public Job(){
        this.id = -1;
        this.name = null;
        this.dateCrea = null;
    }

    public Job(String nom, Date date, int id){
        this.id = id;
        this.name = nom;
        this.dateCrea = date;
    }

    @Override
    public String toString() {
        return "Job{" +
                "name='" + name + '\'' +
                ", dateCrea=" + dateCrea +
                '}';
    }

    public int getId() {
        return id;
    }

    public static List<Job> generateAllJob() throws IOException {
        try{
            List<Job> typesStaff = new ArrayList<>();
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Staff_Type");

            // Iterating over rows using iterator
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    Job job= new Job();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                job.name = cell.getStringCellValue();
                                break;
                        }
                    }
                    LocalDate date = LocalDate.now();
                    job.dateCrea = java.sql.Date.valueOf(date);

                    typesStaff.add(job);
                }
            }
            // Closing the workbook to free resources
            wb.close();

            return typesStaff;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {
        List<Job> listJobs = generateAllJob();
        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(Job j : listJobs){
                String sql = "INSERT INTO " +
                        "`llx_hrm_job`(" +
                        "`label`, " +
                        "`date_creation`)" +
                        "VALUES (?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, j.getName());
                preparedStatement.setDate(2, j.getDateCrea());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Job> collectSQL(String url,
                                           String user,
                                           String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<Job> jobs = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_hrm_job";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {

                int id = resultSQL.getInt("rowid");
                Date date_aquis = resultSQL.getDate("date_creation");
                String label = resultSQL.getString("label");

                Job v = new Job(label,date_aquis,id);
                jobs.add(v);
            }

            return jobs;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
