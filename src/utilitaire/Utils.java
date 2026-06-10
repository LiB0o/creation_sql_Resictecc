package utilitaire;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;


public class Utils {

    /**
     * crypt a generated password
     * @return crypted password
     */
    public static String chiffrementPassword() {

        String password = generate_password();

        // coût 10 = équivalent PHP par défaut
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
        hash = hash.replaceFirst("^\\$2a\\$", "\\$2y\\$");

        //System.out.println(hash);
        return hash;
    }

    /**
     * crypt a given password
     * @param password : password needing to be crypted
     * @return crypted password
     */
    public static String chiffrementPassword(String password) {

        //String password = generate_password();

        // coût 10 = équivalent PHP par défaut
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
        hash = hash.replaceFirst("^\\$2a\\$", "\\$2y\\$");

        //System.out.println(hash);
        return hash;
    }

    /**
     * add number of days to a date
     * @param date : date needing to be modified
     * @param days : days needed to be added
     * @return new date with days added
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    /**
     * add a quantity of time to a date
     * @param time : time needing to be modified
     * @param minutes : minutes needed to be added
     * @return new date with minutes added
     */
    public static LocalTime addTime(LocalTime time, int minutes) {
        return time.plusMinutes(minutes);
    }

    /**
     * generate an aleatory password
     * @return password generated
     */
    private static String generate_password() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

}
