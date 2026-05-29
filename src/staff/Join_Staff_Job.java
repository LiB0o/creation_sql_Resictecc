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
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

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
        List<Join_Staff_Job> listJoin = Join_Staff_Job.generateAllJoin(url,user,password);
        try (Connection conn = DriverManager.getConnection(url, user, password)){
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

    public static List<Join_Staff_Job> collectSQL(String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<Join_Staff_Job> list = new ArrayList<>();

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT fk_user, fk_job, date_creation FROM llx_hrm_job_user");

            while (rs.next()) {
                Join_Staff_Job j = new Join_Staff_Job();
                j.setId_user(rs.getInt("fk_user"));
                j.setId_job(rs.getInt("fk_job"));
                j.setDate_creation(rs.getDate("date_creation"));
                list.add(j);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasSMURJob(String url, String user, String password, int id_user) {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            String sql = "SELECT j.label FROM llx_hrm_job j " +
                        "JOIN llx_hrm_job_user ju ON j.rowid = ju.fk_job " +
                        "WHERE ju.fk_user = ?";
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1,id_user);

            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("label");
                if(Objects.equals(name, "Infirmier") || Objects.equals(name, "Médecin") || Objects.equals(name, "Ambulancier") || Objects.equals(name, "RH SMUR")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isCESUStaff(String url, String user, String password, int id_user) {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            String sql = "SELECT j.label FROM llx_hrm_job j " +
                    "JOIN llx_hrm_job_user ju ON j.rowid = ju.fk_job " +
                    "WHERE ju.fk_user = ?";
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1,id_user);

            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("label");
                if(Objects.equals(name, "CESU staff")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
