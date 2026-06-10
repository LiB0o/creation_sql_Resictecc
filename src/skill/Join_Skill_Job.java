package skill;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import staff.Job;
import staff.Join_Staff_Job;
import staff.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;

public class Join_Skill_Job {
    private static final String TABLENAME = "llx_hrm_skillrank";

    private Date date_creation;
    private int id_skill;
    private int id_job;

    public Join_Skill_Job() {
        this.id_skill = -1;
        this.id_job = -1;
        this.date_creation = null;
    }


    @Override
    public String toString() {
        return "Join_Staff_Job{" +
                "id_skill=" + id_skill +
                ", id_job=" + id_job +
                ", date_creation=" + date_creation +
                '}';
    }

    /**
     * Read the excel GL_SQL_datas.xlsx to generate a list of link between jobs and skill
     * @param url : url toward the database
     * @param user : login to the database
     * @param password : password of the login
     * @param date : the date used for generating data
     * @return list of link between skill and job ready to be inserted
     * @throws IOException
     */
    public static List<Join_Skill_Job> generateAllJoin(String url, String user, String password, LocalDate date) throws IOException {
        try {
            List<Join_Skill_Job> listJoin = new ArrayList<>();

            List<Skill> listSkill = Skill.collectSQL(url, user, password);
            List<Job> listJob = Job.collectSQL(url, user, password);

            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Skill_Job");

            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum()>0){
                    Join_Skill_Job join = new Join_Skill_Job();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                String skill = cell.getStringCellValue();
                                listSkill.stream()
                                        .filter(s -> s.getLabel().equalsIgnoreCase(skill))
                                        .findFirst()
                                        .ifPresent(s -> join.id_skill = s.getId());
                                break;
                            case 1:
                                String job = cell.getStringCellValue();
                                listJob.stream()
                                        .filter(s -> s.getName().equalsIgnoreCase(job))
                                        .findFirst()
                                        .ifPresent(s -> join.id_job = s.getId());
                        }
                    }
                    LocalDate localDate = date;
                    join.date_creation = java.sql.Date.valueOf(localDate);

                    listJoin.add(join);
                }
            }
            return listJoin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * insert into the table skillrank of Dolibarr the generated data
     * @param url : url toward the database
     * @param user : login to the database
     * @param password : password of the login
     * @param date : the date used for generating data
     * @throws IOException
     * @throws SQLException
     */
    public static void insertSQL(String url,
                                 String user,
                                 String password, LocalDate date) throws IOException, SQLException {
        List<Join_Skill_Job> listJoin = Join_Skill_Job.generateAllJoin(url,user,password, date);
        List<User> listUser = User.collectSQL(url, user, password);
        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(Join_Skill_Job j : listJoin){
                String sql = "INSERT INTO " +
                        "`llx_hrm_skillrank`(" +
                        "`fk_skill`, " +
                        "`rankorder`,"+
                        "`fk_object`,"+
                        "`date_creation`,"+
                        "`fk_user_creat`,"+
                        "`objecttype`)"+
                        "VALUES (?,0,?,?,?,'job')";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, j.id_skill);
                preparedStatement.setInt(2,j.id_job);
                preparedStatement.setDate(3, j.date_creation);
                preparedStatement.setInt(4, listUser.getFirst().getRowid());
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * collect every skill linked to a specific job
     * @param conn : connexion to the database
     * @param jobId : id of the job
     * @return list of skill linked to jobId
     */
    public static List<Skill> collectSkillsByJob(Connection conn, int jobId) {
        try {
            List<Skill> skills = new ArrayList<>();

            String sql = "SELECT s.rowid, s.label, s.description, s.date_creation, s.date_validite " +
                    "FROM llx_hrm_skill s " +
                    "JOIN llx_hrm_skillrank sr ON sr.fk_skill = s.rowid " +
                    "WHERE sr.fk_object = ? AND sr.objecttype = 'job'";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id        = rs.getInt("rowid");
                String label  = rs.getString("label");
                String desc   = rs.getString("description");
                Date dc       = rs.getDate("date_creation");
                int dv        = rs.getInt("date_validite");
                skills.add(new Skill(id, label, desc, dc, dv));
            }

            return skills;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
