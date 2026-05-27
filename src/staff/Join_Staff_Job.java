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
import java.util.Random;

public class Join_Staff_Job {

    private static final String TABLENAME = "llx_hrm_job_user";
    private int id_user;
    private int id_job;

    private Date date_creation;

    public Join_Staff_Job(){
        this.id_job = -1;
        this.date_creation = null;
        this.id_user = -1;
    }

    public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
    }

    public void setId_job(int id_job) {
        this.id_job = id_job;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public Date getDate_creation() {
        return date_creation;
    }

    public int getId_job() {
        return id_job;
    }

    public int getId_user() {
        return id_user;
    }

    @Override
    public String toString() {
        return "Join_Staff_Job{" +
                "id_user=" + id_user +
                ", id_job=" + id_job +
                ", date_creation=" + date_creation +
                '}';
    }

    public static List<Join_Staff_Job> generateAllJoin(String url,
                                                      String user,
                                                      String password) throws IOException {
        try{
            List<Join_Staff_Job> join_staff_jobs = new ArrayList<>();
            List<Job> jobs = Job.collectSQL(url,user,password);
            List<User> users = User.collectSQL(url,user,password);
            int nbJobs = jobs.size();
            Random rand = new Random();
            LocalDate localDate = LocalDate.now();

            for(User u : users){
                int id_job = jobs.get(rand.nextInt(nbJobs)).getId();
                Join_Staff_Job j = new Join_Staff_Job();

                j.setId_job(id_job);
                j.setId_user(u.getRowid());
                j.setDate_creation(java.sql.Date.valueOf(localDate));

                join_staff_jobs.add(j);
            }


            return join_staff_jobs;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException, SQLException {
        Connection conn = null;
        List<Join_Staff_Job> listJoin = Join_Staff_Job.generateAllJoin(url,user,password);
        try{
            conn = DriverManager.getConnection(url,user,password);
            System.out.println("Connected to the DB");

            for(Join_Staff_Job j : listJoin){
                String sql = "INSERT INTO " +
                        "`llx_hrm_job_user`(" +
                        "`date_creation`, " +
                        "`fk_user`, " +
                        "`fk_job`)"+
                        "VALUES (?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setDate(1, j.date_creation);
                preparedStatement.setInt(2, j.id_user);
                preparedStatement.setInt(3,j.id_job);
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
