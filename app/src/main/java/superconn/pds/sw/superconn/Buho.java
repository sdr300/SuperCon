package superconn.pds.sw.superconn;

/**
 * created 2020-11-02
 */


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Buho {

    @PrimaryKey(autoGenerate = true)
    private int buhoID;
    @ColumnInfo(name="buhoDate")
    private String buhoDate;
    @ColumnInfo(name="buhoSender")
    private  String buhoSender;
    @ColumnInfo(name="buhoIcon")
    private String buhoIcon;
    @ColumnInfo(name="buhoLocation")
    private String buhoLocation;
    @ColumnInfo(name="buhoCompany")
    private String buhoCompany;

    public int getBuhoID() {
        return buhoID;
    }

    public void setBuhoID(int buhoID) {
        this.buhoID = buhoID;
    }

    public String getBuhoDate() {
        return buhoDate;
    }

    public void setBuhoDate(String buhoDate) {
        this.buhoDate = buhoDate;
    }

    public String getBuhoSender() {
        return buhoSender;
    }

    public void setBuhoSender(String buhoSender) {
        this.buhoSender = buhoSender;
    }

    public String getBuhoIcon() {
        return buhoIcon;
    }

    public void setBuhoIcon(String buhoIcon) {
        this.buhoIcon = buhoIcon;
    }

    public String getBuhoLocation() {
        return buhoLocation;
    }

    public void setBuhoLocation(String buhoLocation) {
        this.buhoLocation = buhoLocation;
    }

    public String getBuhoCompany() {
        return buhoCompany;
    }

    public void setBuhoCompany(String buhoCompany) {
        this.buhoCompany = buhoCompany;
    }

}
