package superconn.pds.sw.superconn.DataBase;

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
    @ColumnInfo(name="buhoLatitude")
    private String buhoLatitude;
    @ColumnInfo(name="buhoLongitude")
    private String buhoLongitude;
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

    public String getBuhoLatitude() {
        return buhoLatitude;
    }

    public void setBuhoLatitude(String buhoLatitude) {
        this.buhoLatitude = buhoLatitude;
    }

    public String getBuhoLongitude() {
        return buhoLongitude;
    }

    public void setBuhoLongitude(String buhoLongitude) {
        this.buhoLongitude = buhoLongitude;
    }

    public String getBuhoCompany() {
        return buhoCompany;
    }

    public void setBuhoCompany(String buhoCompany) {
        this.buhoCompany = buhoCompany;
    }

}
