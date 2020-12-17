package superconn.pds.sw.superconn.coord;

public class CoordDMS {
    private int nLonDegree;
    private int nLonMin;
    private double dLonSec;
    private char cLonLetter;
    private int nLatDegree;
    private int nLatMin;
    private  double dLatSec;
    private char cLatLetter;

    /**
     * Method Name : getnLonDegree
     * Description :
     *
     * @return int
     */
    public int getLonDegree() {
        return nLonDegree;
    }
    /**
     * Method Name : setnLonDegree
     * Description : .
     *
     * @param nLonDegree :
     * @return void
     */
    public void setLonDegree(int nLonDegree) {
        this.nLonDegree = nLonDegree;
    }
    /**
     * Method Name : getnLonMin
     * Description :
     *
     * @return int
     */
    public int getLonMin() {
        return nLonMin;
    }
    /**
     * Method Name : setnLonMin
     * Description :
     *
     * @param nLonMin :
     * @return void
     */
    public void setLonMin(int nLonMin) {
        this.nLonMin = nLonMin;
    }
    /**
     * Method Name : getdLonSec
     * Description :
     *
     * @return double
     */
    public double getLonSec() {
        return dLonSec;
    }
    /**
     * Method Name : setdLonSec
     * Description :
     *
     * @param dLonSec :
     * @return void
     */
    public void setLonSec(double dLonSec) {
        this.dLonSec = dLonSec;
    }
    /**
     * Method Name : getcLonLetter
     * Description :
     *
     * @return char
     */
    public char getLonLetter() {
        return cLonLetter;
    }
    /**
     * Method Name : setcLonLetter
     * Description :
     *
     * @param cLonLetter :
     * @return void
     */
    public void setLonLetter(char cLonLetter) {
        this.cLonLetter = cLonLetter;
    }
    /**
     * Method Name : getnLatDegree
     * Description :
     *
     * @return int
     */
    public int getLatDegree() {
        return nLatDegree;
    }
    /**
     * Method Name : setnLatDegree
     * Description :
     *
     * @param nLatDegree :
     * @return void
     */
    public void setLatDegree(int nLatDegree) {
        this.nLatDegree = nLatDegree;
    }
    /**
     * Method Name : getnLatMin
     * Description :
     *
     * @return int
     */
    public int getLatMin() {
        return nLatMin;
    }
    /**
     * Method Name : setnLatMin
     * Description :
     *
     * @param nLatMin :
     * @return void
     */
    public void setLatMin(int nLatMin) {
        this.nLatMin = nLatMin;
    }
    /**
     * Method Name : getdLatSec
     * Description :
     *
     * @return double
     */
    public double getLatSec() {
        return dLatSec;
    }
    /**
     * Method Name : setdLatSec
     * Description :
     *
     * @param dLatSec :
     * @return void
     */
    public void setLatSec(double dLatSec) {
        this.dLatSec = dLatSec;
    }
    /**
     * Method Name : getcLatLetter
     * Description :
     *
     * @return char
     */
    public char getLatLetter() {
        return cLatLetter;
    }
    /**
     * Method Name : setcLatLetter
     * Description :
     *
     * @param cLatLetter :
     * @return void
     */
    public void setLatLetter(char cLatLetter) {
        this.cLatLetter = cLatLetter;
    }
    /**
     * Method Name : convertFromDeg
     * Description :
     *
     * @param dLon :
     * @param dLat :
     * @return void
     */
    public void convertFromDeg(double dLon, double dLat)
    {
        double absLon = Math.abs(dLon);
        double absLat = Math.abs(dLat);
        this.cLonLetter = dLon < 0 ? 'W' : 'E' ;
        this.nLonDegree=(int)absLon;
        this.nLonMin=(int) ((absLon-this.nLonDegree)*60);
        this.dLonSec= ((absLon-this.nLonDegree)*60-this.nLonMin)*60;
        this.cLatLetter = dLat < 0 ? 'S' : 'N' ;
        this.nLatDegree=(int)absLat;
        this.nLatMin=(int) ((absLat-this.nLatDegree)*60);
        this.dLatSec= ((absLat-this.nLatDegree)*60-this.nLatMin)*60;


    }
    /**
     * Method Name : getLatDecimalDegree
     * Description :
     *
     * @return double
     */
    public  double getLatDecimalDegree( )
    {
        return 	 dmsToDeg((cLatLetter == 'S' ? nLatDegree*-1 : nLatDegree) ,nLatMin, dLatSec );


    }
    /**
     * Method Name : getLonDecimalDegree
     *
     * @return double
     */
    public  double getLonDecimalDegree( )
    {
        return dmsToDeg(( cLonLetter == 'S' ? nLonDegree*-1 : nLonDegree), nLonMin, dLonSec );
    }
    /**
     * Method Name : dmsToDeg
     * Description : DMS醫뚰몴瑜� DEG濡� 蹂��솚
     *
     * @return DMS2DEG
     */
    public static double dmsToDeg(int d, int m, double s) {
        int iSign;

        if (d > 0) {
            iSign = 1;
        } else {
            if (d < 0){
                iSign = -1;
            } else {
                iSign = 0;
            }
        }

        double deg = Math.abs(d) + m / 60.0D + s / 3600.0D;
        deg = iSign * deg;

        return deg;
    }
}
