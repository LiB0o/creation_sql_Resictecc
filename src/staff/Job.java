package staff;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

    private String name;
    private Date dateCrea;

    public String getName() {
        return name;
    }
    public Date getDateCrea(){return dateCrea;}

    public Job(){
        this.name = null;
        this.dateCrea = null;
    }

    public Job(String nom, Date date){
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
        Connection conn = null;
        List<Job> listJobs = generateAllJob();
        try{
            conn = DriverManager.getConnection(url,user,password);
            System.out.println("Connected to the DB");

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


    /*public static void main(String[] args) throws IOException {
        List<Job> jobs = generateAllJob();

        for(Job j: jobs){
            System.out.println(j.toString());
        }
        //System.out.println("insertSQL done");
    }*/
}
