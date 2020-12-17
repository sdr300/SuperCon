package superconn.pds.sw.superconn.coord;

public class CoordinateManager {
    public static final double PI = 3.1415926536D;
    public static final double DEG_RAD = 0.01745329252D;

    /**
     * Method Name : utmToDeg
     * Description :
     *
     * @param dX :
     * @param dY :
     * @param nZone :
     * @return CoordDEG
     */
    public static CoordDEG utmToDeg(double dX, double dY, int nZone) {
        CoordDEG deg = new CoordDEG();

        deg.convertFromUtm(dX, dY, nZone);

        return deg;
    }

    /**
     * Method Name : mgrsToDeg
     * Description :
     *
     * @param strMGRS : 醫뚰몴蹂��닔 Zone
     * @return CoordDEG
     */
    public static CoordDEG mgrsToDeg(String strMGRS) {
        CoordDEG deg = new CoordDEG();
        CoordUTM utm = mgrsToUtm(strMGRS);
        if ( utm == null) return null;

        deg.convertFromMgrs(utm.getCoordX(), utm.getCoordY(), utm.getZone());

        return deg;
    }
    /**
     * Method Name : mgrsToDms
     * Description :  MGRS醫뚰몴瑜� DMS醫뚰몴濡� 蹂��솚�븳�떎
     *
     * @param strMGRS :
     * @return CoordDMS
     */
    public static CoordDMS mgrsToDms(String strMGRS){
        /*MGRS醫뚰몴 (�삁: S52123456789012)
         nLonDegree;		// 寃쎈룄 �룄
        nLonMin;	   	    // 寃쎈룄 遺�
        dLonSec;    // 寃쎈룄 珥�
        cLonLetter;	   // 寃쎈룄 諛⑹쐞臾몄옄(E,W)
        nLatDegree;	   // �쐞�룄 �룄
        nLatMin;          // �쐞�룄 遺�
        dLatSec;     // �쐞�룄 珥�
        cLatLetter;      // �쐞�룄 諛⑹쐞臾몄옄(N,S)
        �뿉 媛곴컖 媛믪��옣*/
        CoordDEG deg = mgrsToDeg(strMGRS);
        if (deg == null) return null;
        CoordDMS dms = new CoordDMS();

        dms.convertFromDeg(deg.getLon(),deg.getLat());

        return dms;
    }
    /**
     * Method Name : dmsToMgrs
     * Description : DMS
     *
     * @param dms :
     * @return CoordMGRS
     */
    public static coordMgrs dmsToMgrs(CoordDMS dms){
        /*
          (
             dms.nLatDegree=37;
            dms.nLatMin=34;
            dms.dLatSec=31;
            dms.cLatLetter='N';
            dms.nLonDegree=126;
            dms.nLonMin=58;
            dms.dLonSec=31;
            dms.cLonLetter='E';)
         */
        double lon = dms.getLonDecimalDegree();
        double lat = dms.getLatDecimalDegree();
        CoordUTM utm = new CoordUTM();

        utm.convertFromWgp(lon, lat, 2);

        coordMgrs mgrs = new coordMgrs();

        mgrs.convertUTMToMGRS(utm.getZone(), lat, utm.getCoordX(), utm.getCoordY(), 5);

        return mgrs;
    }

    /**
     * Method Name : mgrsToUtm
     * Description : MGRS
     *
     * @param strMGRS :
     * @return CoordUTM
     */
    public static CoordUTM mgrsToUtm(String strMGRS) {

        if ( strMGRS!=null)
        {
            return CoordUTM.convertFromMgrs(strMGRS);
        }
        return null;
    }

    /**
     * Method Name : dmsToDeg
     * Description :
     *
     * @param d :
     * @param m :
     * @param s :
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
    /**
     * Method Name : ins
     * Description :
     *
     */
    public String ins(String s){
        return s+ "";
    }

    /**
     * Method Name : jsMgrsToDms
     * Description :
     *
     */
    public static String jsMgrsToDms(String mgrs){
        /*MGRS
         nLonDegree;
        nLonMin;
        dLonSec;
        cLonLetter;
        nLatDegree;
        nLatMin;
        dLatSec;
        cLatLetter;
        */
        CoordDEG deg = mgrsToDeg(mgrs);
        if (deg == null) return null;
        CoordDMS dms = new CoordDMS();

        dms.convertFromDeg(deg.getLon(),deg.getLat());
        String LatDegree= String.valueOf(dms.getLatDegree());
        String LatMin = String.valueOf(dms.getLatMin());
        String LatSec= String.valueOf((int)dms.getLatSec());
        String LonDegree= String.valueOf(dms.getLonDegree());
        String LonMin= String.valueOf(dms.getLonMin());
        String LonSec= String.valueOf((int)dms.getLonSec());
        if (LatDegree.length()==1)
        {LatDegree = "0"+LatDegree;}
        if (LatMin.length()==1)
        {LatMin = "0"+LatMin;}
        if (LatSec.length()==1)
        {LatSec = "0"+LatSec;}
        if (dms.getLonDegree()<10)
        {LonDegree = "00"+LonDegree;}
        else if(dms.getLonDegree()>9&&dms.getLonDegree()<100)
        {LonDegree = "0"+LonDegree;}
        if (LonMin.length()==1)
        {LonMin = "0"+LonMin;}
        if (LonSec.length()==1)
        {LonSec = "0"+LonSec;}
        String dms2=LatDegree ;
        dms2 = dms2 +LatMin ;
        dms2 = dms2 + LatSec;
        dms2 = dms2 + String.valueOf(dms.getLatLetter());
        dms2 = dms2 + LonDegree ;
        dms2 = dms2 +LonMin ;
        dms2 = dms2 +LonSec ;
        dms2 = dms2 + String.valueOf(dms.getLonLetter());

        return dms2;

    }

    /**
     * Method Name : jsDmsToMgrs
     * Description :
     *
     * @param dms
     * @return String
     */
    public static String jsDmsToMgrs(String dms){
        /*
          (
             dms.nLatDegree=37;
            dms.nLatMin=34;
            dms.dLatSec=31;
            dms.cLatLetter='N';
            dms.nLonDegree=126;
            dms.nLonMin=58;
            dms.dLonSec=31;
            dms.cLonLetter='E';)
         */
        CoordDMS dms2 = new CoordDMS();
        String cvr;
        cvr=dms.substring(0,2);

        dms2.setLatDegree(Integer.parseInt(dms.substring(0,2)));
        dms2.setLatMin(Integer.parseInt(dms.substring(2,4)));
        dms2.setLatSec(Integer.parseInt(dms.substring(4,6)));
        dms2.setLatLetter(dms.substring(6,7).charAt(0));
        dms2.setLonDegree(Integer.parseInt(dms.substring(7,10)));
        dms2.setLonMin(Integer.parseInt(dms.substring(10,12)));;
        dms2.setLonSec(Integer.parseInt(dms.substring(12,14)));
        dms2.setLonLetter(dms.substring(14,15).charAt(0));

        double lon = dms2.getLonDecimalDegree();
        double lat = dms2.getLatDecimalDegree();
        CoordUTM utm = new CoordUTM();

        utm.convertFromWgp(lon, lat, 2);

        coordMgrs mgrs = new coordMgrs();

        mgrs.convertUTMToMGRS(utm.getZone(), lat, utm.getCoordX(), utm.getCoordY(), 5);

        String smgrs = mgrs.getMGRS();

        return smgrs;
    }
}
