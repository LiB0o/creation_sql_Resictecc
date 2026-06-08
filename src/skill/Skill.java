package skill;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import staff.Job;
import staff.SMUR;
import staff.User;
import utilitaire.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class Skill {

    private static  final String TABLENAME = "llx_hrm_skill";
    private String label;
    private String description;
    private Date date_creation;
    private int date_validite;
    private int id;

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate_creation() {
        return date_creation;
    }

    public int getDate_validite() {
        return date_validite;
    }

    public int getId() {
        return id;
    }

    public Skill() {
        this.label = null;
        this.description = null;
        this.date_creation = null;
        this.date_validite = -1;
        this.id = -1;
    }

    public Skill(int id, String label, String desc, Date dc, int dv) {
        this.label = label;
        this.description = desc;
        this.date_validite = dv;
        this.date_creation = dc;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Skill{" + "label='" + label + "\'" +
                ", description='" + description + "\'" +
                ", rowid=" + id +
                ", date_creation='" + date_creation + "\'" +
                ", date_validite='" + date_validite + "\'}";
    }

    public static List<Skill> generateAllSkill(LocalDate date) throws IOException {
        try {
            List<Skill> skills = new ArrayList<>();
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Compétence");

            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Random rand = new Random();
                Row row = itr.next();
                if(row.getRowNum()>0){
                    // Iterating over each column in a row
                    Skill skill= new Skill();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                skill.label = cell.getStringCellValue();
                                break;
                            case 1:
                                skill.description = cell.getStringCellValue();
                        }
                    }
                    LocalDate localDate = date;
                    skill.date_creation = java.sql.Date.valueOf(localDate);

                    skill.date_validite = rand.nextInt(365);

                    skills.add(skill);
                }
            }
            // Closing the workbook to free resources
            wb.close();
            return skills;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Skill> collectSQL(String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            List<Skill> skills = new ArrayList<>();

            Statement stat = null;

            stat = conn.createStatement();
            String sql = "SELECT * FROM llx_hrm_skill";
            ResultSet resultSQL = stat.executeQuery(sql);

            while (resultSQL.next()) {
                int id = resultSQL.getInt("rowid");
                String nom = resultSQL.getString("label");
                String desc = resultSQL.getString("description");
                Date dc = resultSQL.getDate("date_creation");
                int dv = resultSQL.getInt("date_validite");

                Skill v = new Skill(id,nom,desc,dc,dv);
                skills.add(v);
            }

            return skills;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password, LocalDate date) throws IOException, SQLException {
        List<Skill> listSkill = generateAllSkill(date);
        List<User> listUser = User.collectSQL(url, user, password);
        try (Connection conn = DriverManager.getConnection(url, user, password)){

            for(Skill s : listSkill){
                String sql =
                        "INSERT INTO `llx_hrm_skill` (`label`, `description`, `skill_type`, `date_creation`, `fk_user_creat`, `required_level`, `date_validite`, `temps_theorique`)" +
                        "VALUES (?, ?, 1, ?, ?, 0, ?, 0)";

                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, s.getLabel());
                preparedStatement.setString(2, s.getDescription());
                preparedStatement.setDate(3, s.getDate_creation());
                preparedStatement.setInt(4, listUser.getFirst().getRowid());
                preparedStatement.setInt(5, s.getDate_validite());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
