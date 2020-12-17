package superconn.pds.sw.superconn.coord;

public class coordMgrs {

    private final static double DEGREES_TO_RADIANS = Math.PI / 180d;
    //	private final static double RADIANS_TO_DEGREES = 180d / Math.PI;
    private static final int LETTERA = 0;   /* ARRAY INDEX FOR letter A               */
    //    private static final int letter_B = 1;   /* ARRAY INDEX FOR letter B               */
    private static final int LETTERC = 2;   /* ARRAY INDEX FOR letter C               */
    private static final int LETTERD = 3;   /* ARRAY INDEX FOR letter D               */
    private static final int LETTERE = 4;   /* ARRAY INDEX FOR letter E               */
    private static final int LETTERF = 5;   /* ARRAY INDEX FOR letter E               */
    private static final int LETTERG = 6;   /* ARRAY INDEX FOR letter H               */
    private static final int LETTERH = 7;   /* ARRAY INDEX FOR letter H               */
    //    private static final int letter_I = 8;   /* ARRAY INDEX FOR letter I               */
    private static final int LETTERJ = 9;   /* ARRAY INDEX FOR letter J               */
    private static final int LETTERK = 10;   /* ARRAY INDEX FOR letter J               */
    private static final int LETTERL = 11;   /* ARRAY INDEX FOR letter L               */
    private static final int LETTERM = 12;   /* ARRAY INDEX FOR letter M               */
    private static final int LETTERN = 13;   /* ARRAY INDEX FOR letter N               */
    //   private static final int letter_O = 14;   /* ARRAY INDEX FOR letter O               */
    private static final int LETTERP = 15;   /* ARRAY INDEX FOR letter P               */
    private static final int LETTERQ = 16;   /* ARRAY INDEX FOR letter Q               */
    private static final int LETTERR = 17;   /* ARRAY INDEX FOR letter R               */
    private static final int LETTERS = 18;   /* ARRAY INDEX FOR letter S               */
    private static final int LETTERT = 19;   /* ARRAY INDEX FOR letter S               */
    private static final int LETTERU = 20;   /* ARRAY INDEX FOR letter U               */
    private static final int LETTERV = 21;   /* ARRAY INDEX FOR letter V               */
    private static final int LETTERW = 22;   /* ARRAY INDEX FOR letter W               */
    private static final int LETTERX = 23;   /* ARRAY INDEX FOR letter X               */
//    private static final int letter_Y = 24;   /* ARRAY INDEX FOR letter Y               */
//    private static final int letter_Z = 25;   /* ARRAY INDEX FOR letter Z               */
//    private static final int MGRS_letterS = 3;  /* NUMBER OF letterS IN MGRS              */

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // Latitude Band Constants are in the following order:
    //        long letter;            /* letter representing latitude band  */
    //        double min_northing;    /* minimum northing for latitude band */
    //        double north;           /* upper latitude for latitude band   */
    //        double south;           /* lower latitude for latitude band   */
    private static final double[][] LATITUDEBANDCONSTANTS = {
            {LETTERC, 1100000.0, -72.0, -80.5, 0.0},
            {LETTERD, 2000000.0, -64.0, -72.0, 2000000.0},
            {LETTERE, 2800000.0, -56.0, -64.0, 2000000.0},
            {LETTERF, 3700000.0, -48.0, -56.0, 2000000.0},
            {LETTERG, 4600000.0, -40.0, -48.0, 4000000.0},
            {LETTERH, 5500000.0, -32.0, -40.0, 4000000.0},   //smithjl last column to table
            {LETTERJ, 6400000.0, -24.0, -32.0, 6000000.0},
            {LETTERK, 7300000.0, -16.0, -24.0, 6000000.0},
            {LETTERL, 8200000.0, -8.0, -16.0, 8000000.0},
            {LETTERM, 9100000.0, 0.0, -8.0, 8000000.0},
            {LETTERN, 0.0, 8.0, 0.0, 0.0},
            {LETTERP, 800000.0, 16.0, 8.0, 0.0},
            {LETTERQ, 1700000.0, 24.0, 16.0, 0.0},
            {LETTERR, 2600000.0, 32.0, 24.0, 2000000.0},
            {LETTERS, 3500000.0, 40.0, 32.0, 2000000.0},
            {LETTERT, 4400000.0, 48.0, 40.0, 4000000.0},
            {LETTERU, 5300000.0, 56.0, 48.0, 4000000.0},
            {LETTERV, 6200000.0, 64.0, 56.0, 6000000.0},
            {LETTERW, 7000000.0, 72.0, 64.0, 6000000.0},
            {LETTERX, 7900000.0, 84.5, 72.0, 6000000.0}};

    //    private static final double PI = 3.14159265358979323;
//    private static final double PI_OVER_2 = (PI / 2.0e0);
//    private static final int MAX_precision = 5;
//    private static final double MIN_UTM_LAT = (-80 * PI) / 180.0;    // -80 degrees in radians
//    private static final double MAX_UTM_LAT = (84 * PI) / 180.0;     // 84 degrees in radians
    public static final double DEG_TO_RAD = 0.017453292519943295;   // PI/180
    private static final double RAD_TO_DEG = 57.29577951308232087;   // 180/PI

    //    private static final double MIN_EAST_NORTH = 0;
//    private static final double MAX_EAST_NORTH = 4000000;
    private static final double TWOMIL = 2000000;
    private static final double ONEHT = 100000;

    private static final String CLARKE_1866 = "CC";
    private static final String CLARKE_1880 = "CD";
    private static final String BESSEL_1841 = "BR";
    private static final String BESSEL_1841_NAMIBIA = "BN";

    // Ellipsoid parameters, default to WGS 84
//    private double MGRS_a = 6378137.0;          // Semi-major axis of ellipsoid in meters
//    private double MGRS_f = 1 / 298.257223563;  // Flattening of ellipsoid
    private static final String MGRS_ELLIPSOID_CODE = "WE";

    private String strMGRS;
    private long ltr2lowvalue;
    //    private long ltr2_high_value;       // this is only used for doing MGRS to xxx conversions.
    private double falsenorthing;
    private long lastletter;
//    private long last_error = 0;
//    private double north, south, min_northing, northing_offset;  //smithjl added north_offset
//    private double latitude;
//    private double longitude;
    /**
     * Method Name : CoordMGRS
     * Description : �깮�꽦�옄
     *
     * @return void
     */
/*
    CoordMGRS() {

    }
*/

    /**
     * Method Name : setMGRS
     * Description :
     *
     * @param mgrs :
     * @return void
     */
    public void setMGRS(String mgrs) { this.strMGRS = mgrs; }
    /**
     * Method Name : getMGRS
     * Description :
     *
     * @return String mgrs
     */
    public String getMGRS() { return this.strMGRS; }

    /**
     * Method Name : convertUTMToMGRS
     * Description :
     *
     * @param zone :
     * @param latitude :
     * @param easting :
     * @param northing :
     * @param precision :
     * @return  long
     */
    public boolean convertUTMToMGRS(long zone, double latitude, double easting, double northing, long precision)
    {
        double latrad = DEGREES_TO_RADIANS * latitude;
        double grideasting;        /* easting used to derive 2nd letter of MGRS   */
        double gridnorthing;       /* northing used to derive 3rd letter of MGRS  */
        long[] letters = new long[3];  /* Number location of 3 letters in alphabet    */
        double divisor;
        double eastingg;
        double northingg;
        boolean succes;

        /* Round easting and northing values */
        divisor = Math.pow(10.0, (5 - precision));
        eastingg = roundMGRS(easting / divisor) * divisor;
        northingg = roundMGRS(northing / divisor) * divisor;

        getGridValues(zone);

        succes = getLatitudeletter(latrad);
        letters[0] = lastletter;

        if (succes)
        {
            gridnorthing = northing;
            if (Double.compare(gridnorthing,1.e7) == 0) //�닔�떇泥섎━�뿉 �삁�빟�맂 �닽�옄濡� 吏��젙�씠�릺�뼱 �븘�닔�궗�빆
            { gridnorthing = gridnorthing - 1.0;}

            while (gridnorthing >= TWOMIL)
            {
                gridnorthing = gridnorthing - TWOMIL;
            }
            gridnorthing = gridnorthing + falsenorthing;   //smithjl

            if (gridnorthing >= TWOMIL)                     //smithjl
            { gridnorthing = gridnorthing - TWOMIL;}       //smithjl

            letters[2] = (long) (gridnorthing / ONEHT);
            if (letters[2] > LETTERH)
            { letters[2] = letters[2] + 1;}

            if (letters[2] > LETTERN)
            {letters[2] = letters[2] + 1;}

            grideasting = easting;
            if (((letters[0] == LETTERV) && (zone == 31)) && (Double.compare(grideasting, 500000.0) == 0))
            { grideasting = grideasting - 1.0;} /* SUBTRACT 1 METER */

            letters[1] = ltr2lowvalue + ((long) (grideasting / ONEHT) - 1);
            if ((ltr2lowvalue == LETTERJ) && (letters[1] > LETTERN))
            { letters[1] = letters[1] + 1;}

            makeMGRSString(zone, letters, eastingg, northingg, precision);
        }
        return succes;
    }


    /**
     * Method Name : getGridValues
     * Description :
     *
     * @param zone :
     * return void
     */
    private void getGridValues(long zone)
    {
        long setnumber;    /* Set number (1-6) based on UTM zone number */
        long aapattern;    /* Pattern based on ellipsoid code */

        setnumber = zone % 6;

        if (setnumber == 0)
        {  setnumber = 6;}

        if (MGRS_ELLIPSOID_CODE.compareTo(CLARKE_1866) == 0 || MGRS_ELLIPSOID_CODE.compareTo(CLARKE_1880) == 0 ||
                MGRS_ELLIPSOID_CODE.compareTo(BESSEL_1841) == 0 || MGRS_ELLIPSOID_CODE.compareTo(BESSEL_1841_NAMIBIA) == 0)
            aapattern = 0L;
        else
            aapattern = 1L;

        if ((setnumber == 1) || (setnumber == 4))
        {
            ltr2lowvalue = (long) LETTERA;
            //          ltr2_high_value = (long) LETTER_H;
        }
        else if ((setnumber == 2) || (setnumber == 5))
        {
            ltr2lowvalue = (long) LETTERJ;
            //           ltr2_high_value = (long) letter_R;
        }
        else if ((setnumber == 3) || (setnumber == 6))
        {
            ltr2lowvalue = (long) LETTERS;
            //          ltr2_high_value = (long) letter_Z;
        }

        /* False northing at A for second letter of grid square */
        if (aapattern == 1L)
        {
            if ((setnumber % 2) == 0)
                falsenorthing = 500000.0;             //smithjl was 1500000
            else
                falsenorthing = 0.0;
        }
        else
        {
            if ((setnumber % 2) == 0)
                falsenorthing = 1500000.0;            //smithjl was 500000
            else
                falsenorthing = 1000000.00;
        }
    }

    /**
     * Method Name : getLatitudeletter
     * Description :
     *
     * @param latitude :
     * @return long
     */
    private boolean getLatitudeletter(double latitude)
    {
        double temp;
        boolean succes;
        double latdeg = latitude * RAD_TO_DEG;

        if (latdeg >= 72 && latdeg < 84.5)
        {
            lastletter = (long) LETTERX;
            succes = true;
        }
        else if (latdeg > -80.5 && latdeg < 72)
        {
            temp = ((latitude + (80.0 * DEG_TO_RAD)) / (8.0 * DEG_TO_RAD)) + 1.0e-12;
            // lastletter = Latitude_Band_Table.get((int) temp).letter;
            lastletter = (long) LATITUDEBANDCONSTANTS[(int) temp][0];
            succes = true;
        }
        else
        {
            succes = false ;
        }

        return succes;
    }

    /**
     * Method Name : roundMGRS
     * Description :
     *
     * @param value :
     * @return double
     */
    private double roundMGRS(double value)
    {
        double ivalue = Math.floor(value);
        long ival;
        double fraction = value - ivalue;
        // double fraction = modf (value, &ivalue);

        ival = (long) (ivalue);
        if ((fraction > 0.5) || (Double.compare(fraction, 0.5) == 0  && (ival % 2 == 1)))  //�닔�떇泥섎━�뿉 �삁�빟�맂 �닽�옄濡� 吏��젙�씠�릺�뼱 �븘�닔�궗�빆
        {   ival++;}
        return (double) ival;
    }

    /**
     * Method Name : makeMGRSString
     * Description :
     *
     * @param zone :
     * @param letters :
     * @param easting :
     * @param northing :
     * @param precision :
     * @return long
     */
    private long makeMGRSString(long zone, long[] letters, double easting, double northing, long precision)
    {
        int j;
        double divisor;
        long east;
        long north;
        long errorcode = 0;
        double eastingg;
        double northingg;

        if (zone != 0)
            strMGRS = String.format("%02d", zone);
        else
            strMGRS = "  ";

        for (j = 0; j < 3; j++)
        {

            if (letters[j] < 0 || letters[j] > 26)
            {  return 0;}
            strMGRS = strMGRS + ALPHABET.charAt((int) letters[j]);
        }

        divisor = Math.pow(10.0, (5 - precision));
        eastingg = easting % 100000.0;
        if (eastingg >= 99999.5)
        {  eastingg = 99999.0;}
        east = (long) (eastingg / divisor);

        // Here we need to only use the number requesting in the precision
        Integer iEast = (int) east;
        String sEast = iEast.toString();
        if (sEast.length() > precision)
            sEast = sEast.substring(0, (int) precision - 1);
        else
        {
            int i;
            int length = sEast.length();
            for (i = 0; i < precision - length; i++)
            {
                sEast = "0" + sEast;
            }
        }
        strMGRS = strMGRS + sEast;

        northingg = northing % 100000.0;
        if (northingg >= 99999.5)
        {   northingg = 99999.0;}
        north = (long) (northingg / divisor);

        Integer iNorth = (int) north;
        String sNorth = iNorth.toString();
        if (sNorth.length() > precision)
            sNorth = sNorth.substring(0, (int) precision - 1);
        else
        {
            int i;
            int length = sNorth.length();
            for (i = 0; i < precision - length; i++)
            {
                sNorth = "0" + sNorth;
            }
        }
        strMGRS = strMGRS+ sNorth;

        return errorcode;
    }

}



