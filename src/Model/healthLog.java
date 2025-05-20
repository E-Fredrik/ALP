package Model;
import java.util.Date;
import java.text.SimpleDateFormat;

public abstract class healthLog {
    private Date date;
    
    
    public healthLog() {
        this.date = new Date(); // Sets to current date and time
    }
    

    public healthLog(Date date) {
        this.date = date;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }
    
    public abstract void inputLog();
    public abstract void displayLog();
    public abstract String getType();
}