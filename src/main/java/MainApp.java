import util.DBConnection;

public class MainApp {
    public static void main(String[] args) {
        try {
            DBConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}