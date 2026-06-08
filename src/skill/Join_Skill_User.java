package skill;

import staff.Join_Staff_Job;
import staff.User;
import utilitaire.Utils;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Join_Skill_User {
    private Date date_obtention;
    private Date date_creation;
    private int id_skill;
    private int id_user;
    private int rowid;

    public Join_Skill_User() {
        this.id_skill = -1;
        this.id_user = -1;
        this.date_creation = null;
        this.date_obtention = null;
        this.rowid = -1;
    }

    public Join_Skill_User(int rowid, int id_skill, int id_user, Date date_creation, Date date_obtention) {
        this.rowid = rowid;
        this.id_skill = id_skill;
        this.id_user = id_user;
        this.date_creation = date_creation;
        this.date_obtention = date_obtention;
    }

    @Override
    public String toString() {
        return "Join_Staff_Job{" +
                "id_skill=" + id_skill +
                ", id_user=" + id_user +
                ", date_obtention=" + date_obtention +
                ", date_creation=" + date_creation +
                '}';
    }

    public static List<Join_Skill_User> generateAllJoin(String url, String user, String password, LocalDate date) throws IOException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            List<Join_Skill_User> listSkUs = new ArrayList<>();
            List<Skill> allSkills = Skill.collectSQL(url, user, password);
            List<Join_Staff_Job> listUsJob = Join_Staff_Job.collectSQL(url, user, password);
            LocalDate localDate = date;

            for(Join_Staff_Job uj : listUsJob) {
                Random rand = new Random();
                int usid = uj.getId_user();
                int jobid = uj.getId_job();

                List<Skill> listSkill = Join_Skill_Job.collectSkillsByJob(conn, jobid);

                for(Skill s : listSkill) {
                    Join_Skill_User join = new Join_Skill_User();

                    join.id_user = usid;
                    join.id_skill = s.getId();
                    join.date_creation = Date.valueOf(localDate);
                    join.date_obtention = Date.valueOf(Utils.addDays(localDate, -rand.nextInt(1825)));

                    listSkUs.add(join);
                }

                if(rand.nextBoolean()){
                    List<Integer> jobSkillIds = listSkill.stream()
                            .map(Skill::getId)
                            .toList();

                    List<Skill> availableSkills = allSkills.stream()
                            .filter(s -> !jobSkillIds.contains(s.getId()))
                            .toList();

                    if(!availableSkills.isEmpty()){
                        int extraCount = rand.nextInt(Math.min(3, availableSkills.size())) + 1;
                        List<Skill> shuffled = new ArrayList<>(availableSkills);
                        java.util.Collections.shuffle(shuffled);

                        for (int i = 0; i < extraCount; i++) {
                            Join_Skill_User join = new Join_Skill_User();
                            join.id_user        = usid;
                            join.id_skill       = shuffled.get(i).getId();
                            join.date_creation  = Date.valueOf(localDate);
                            join.date_obtention = Date.valueOf(localDate.minusDays(rand.nextInt(1825)));
                            listSkUs.add(join);
                        }
                    }
                }
            }
            return listSkUs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertSQL(String url, String user, String password, LocalDate date) throws IOException, SQLException {
        List<Join_Skill_User> listJoin = Join_Skill_User.generateAllJoin(url,user,password, date);
        List<User> listUser = User.collectSQL(url, user, password);

        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(Join_Skill_User skillUser : listJoin) {
                String sql1 = "INSERT INTO `llx_hrm_skillrank`(`fk_skill`,`rankorder`,`fk_object`,`date_creation`,`fk_user_creat`,`objecttype`)" +
                            "VALUES (?, 0, ?, ?, ?, 'user')";
                PreparedStatement ps1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
                ps1.setInt(1, skillUser.id_skill);
                ps1.setInt(2, skillUser.id_user);
                ps1.setDate(3, skillUser.date_creation);
                ps1.setInt(4, listUser.getFirst().getRowid());
                ps1.executeUpdate();

                ResultSet generatedKeys = ps1.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedRowid = generatedKeys.getInt(1);

                    String sql2 = "INSERT INTO `llx_hrm_skillrank_extrafields`(`fk_object`,`date_obtention`) " +
                            "VALUES (?,?)";
                    PreparedStatement ps2 = conn.prepareStatement(sql2);
                    ps2.setInt(1, generatedRowid);
                    ps2.setDate(2, skillUser.date_obtention);
                    ps2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Join_Skill_User> collectSQL(String url, String user, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<Join_Skill_User> listJoin = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT sr.rowid, sr.fk_skill, sr.fk_object, sr.date_creation, ex.date_obtention " +
                    "FROM llx_hrm_skillrank sr " +
                    "JOIN llx_hrm_skillrank_extrafields ex ON ex.fk_object = sr.rowid " +
                    "WHERE sr.objecttype = 'user'";

            ResultSet res1 = stat.executeQuery(sql);

            while(res1.next()) {
                Date dateObtention = res1.getDate("date_obtention");
                int id = res1.getInt("rowid");
                int id_skill = res1.getInt("fk_skill");
                int id_user = res1.getInt("fk_object");
                Date dateCreation = res1.getDate("date_creation");

                Join_Skill_User join = new Join_Skill_User(id,id_skill,id_user,dateCreation,dateObtention);

                listJoin.add(join);
            }

            return listJoin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
