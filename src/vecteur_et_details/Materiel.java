package vecteur_et_details;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import staff.SMUR;
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

public class Materiel {

    private int id_materiel;
    private String nom;
    private Date date_peremption;
    private int id_type_materiel;
    private String immatriculation_vehicule_associe;

     public Materiel(){
         id_materiel = -1;
         nom = null;
         date_peremption = null;
         id_materiel = -1;
         immatriculation_vehicule_associe = null;
     }

     public Materiel(int id_materiel, String nom, Date date_peremption, int id_type_materiel, String immatriculation_vehicule_associe){
         this.id_materiel = id_materiel;
         this.nom = nom;
         this.date_peremption = date_peremption;
         this.id_type_materiel = id_type_materiel;
         this.immatriculation_vehicule_associe = immatriculation_vehicule_associe;
     }

    public String getNom() {
        return nom;
    }

    public Date getDate_peremption() {
        return date_peremption;
    }

    public int getId_materiel() {
        return id_materiel;
    }

    public int getId_type_materiel() {
        return id_type_materiel;
    }

    public String getImmatriculation_vehicule_associe() {
        return immatriculation_vehicule_associe;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate_peremption(Date date_peremption) {
        this.date_peremption = date_peremption;
    }

    public void setId_materiel(int id_materiel) {
        this.id_materiel = id_materiel;
    }

    public void setId_type_materiel(int id_type_materiel) {
        this.id_type_materiel = id_type_materiel;
    }

    public void setImmatriculation_vehicule_associe(String immatriculation_vehicule_associe) {
        this.immatriculation_vehicule_associe = immatriculation_vehicule_associe;
    }

    @Override
    public String toString() {
        return "Materiel{" +
                "id_materiel=" + id_materiel +
                ", nom='" + nom + '\'' +
                ", date_peremption=" + date_peremption +
                ", id_type_materiel=" + id_type_materiel +
                ", immatriculation_vehicule_associe='" + immatriculation_vehicule_associe + '\'' +
                '}';
    }

    public static List<Materiel> generateAllMateriel(String url, String user, String password) throws IOException {
        try{
            List<Materiel> materiels = new ArrayList<>();
            List<TypeMateriel> typeMateriels = TypeMateriel.collectSQL(url,user,password);
            List<Vecteur> vecteurs = Vecteur.collectSQL(url,user,password);
            
            FileInputStream file = new FileInputStream(new File("assets/GL_SQL_datas.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Material");

            // Iterating over rows using iterator
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                for(Vecteur v : vecteurs){

                    Random rand = new Random();
                    int nb_material = rand.nextInt(6);

                    for(int i = 0; i<nb_material; i++){

                        if(row.getRowNum()>0){
                            // Iterating over each column in a row
                            Materiel mat= new Materiel();
                            Iterator<Cell> cellIterator = row.cellIterator();

                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                switch (cell.getColumnIndex()) {
                                    case 0:
                                        mat.nom = cell.getStringCellValue();
                                        break;
                                    case 1:
                                        mat.id_type_materiel = Materiel.getIdTypeMaterial(typeMateriels,cell.getStringCellValue());
                                        break;
                                }
                            }
                            mat.immatriculation_vehicule_associe = v.getImmatriculation();
                            LocalDate localDate = LocalDate.now();
                            mat.date_peremption = java.sql.Date.valueOf(Utils.addDays(localDate, rand.nextInt(120)));


                            materiels.add(mat);
                        }
                    }
                }
            }
            // Closing the workbook to free resources
            wb.close();

            return materiels;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int getIdTypeMaterial(List<TypeMateriel> typeMateriels, String nameType){
         for(TypeMateriel t : typeMateriels){
             if(t.getNom().equals(nameType)){
                 return t.getId();
             }
         }
         return -1;
    }

    public static void insertSQL(String url,
                                 String user,
                                 String password) throws IOException {
        Connection conn = null;
        List<Materiel> listMateriel = generateAllMateriel(url,user,password);
        try{
            conn = DriverManager.getConnection(url,user,password);
            System.out.println("Connected to the DB");

            for(Materiel m : listMateriel){
                String sql = "INSERT INTO `llx_resisteccsamusmur_materiels`(`nom`, `date_peremption`, `id_type_materiel`, `immatriculation`) " +
                        "VALUES (?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, m.getNom());
                preparedStatement.setDate(2,m.getDate_peremption());
                preparedStatement.setInt(3,m.getId_type_materiel());
                preparedStatement.setString(4,m.getImmatriculation_vehicule_associe());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
