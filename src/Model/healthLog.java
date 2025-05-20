package Model;
import java.util.Date;

public abstract class healthLog {
    private Date date;

    public abstract void inputLog();
    public abstract void displayLog();
    public abstract String getType(); // Add this method for type checking
}
