package superconn.pds.sw.superconn.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * created 2020-11-17
 */
@Dao
public interface BuhoDao {

    @Insert
    public void addBuho(Buho buho);

    @Query("select * from Buho order by buhoID DESC")
    public List<Buho> getBuho();

    @Delete
    public void deleteBuho(Buho buho);

    @Update
    public void updateBuho(Buho buho);

    //Delete all query
    @Delete
    void resetBuho(List<Buho> buho);


    @Query("delete from Buho")
    void resetBuho2();
}
