package utilitaire;

import java.time.LocalDate;
import org.mindrot.jbcrypt.BCrypt;

public class Utils {

    public static String chiffrementPassword(String password) {

        //String password = "password";

        // coût 10 = équivalent PHP par défaut
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
        hash = hash.replaceFirst("^\\$2a\\$", "\\$2y\\$");

        //System.out.println(hash);
        return hash;
    }

    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    public static void main(String[] args) {
        Utils.chiffrementPassword("password");
    }
}
