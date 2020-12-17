package superconn.pds.sw.superconn;

/**
 * created 2020-11-02
 */


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Person {

    @PrimaryKey(autoGenerate = true)
    private int piaID;
    @ColumnInfo(name="piaDate")
    private String piaDate;
    @ColumnInfo(name="piaQna")
    private String piaQna;
    @ColumnInfo(name="piaResult")
    private String piaResult;
    @ColumnInfo(name="piaCompany")
    private String piaCompany;

    public int getPiaID() {
        return piaID;
    }

    public void setPiaID(int piaID) {
        this.piaID = piaID;
    }

    public String getPiaDate() {
        return piaDate;
    }

    public void setPiaDate(String piaDate) {
        this.piaDate = piaDate;
    }

    public String getPiaQna() {
        return piaQna;
    }

    public void setPiaQna(String piaQna) {
        this.piaQna = piaQna;
    }

    public String getPiaResult() {
        return piaResult;
    }

    public void setPiaResult(String piaResult) {
        this.piaResult = piaResult;
    }

    public String getPiaCompany() {
        return piaCompany;
    }

    public void setPiaCompany(String piaCompany) {
        this.piaCompany = piaCompany;
    }




}
