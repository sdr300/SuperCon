package superconn.pds.sw.superconn.coord;

public class CoordUTM {
    private int nZone;
    private double dCoordX;
    private double dCoordY;
    protected double dX;
    private double dY;
    private int nLetterLow;
    protected int nLetterHi;
    private double dLetterFy;
    private int nLetter1st;
    private int nLetter2nd;
    private int nLetter3rd;
    private double dSouth;
    protected double dNorth;
    protected double dEast;
    protected double dWest;
    protected double dLetterX;
    protected double dLetterY;

//	private CoordinateManager m_pManager;
    /**
     * Method Name : CoordUTM
     * Description :
     */
    public CoordUTM() {
        this.nZone = 0;
        this.dCoordX = 0.0D;
        this.dCoordY = 0.0D;
//		this.m_pManager = new CoordinateManager();
    }

    /**
     * Method Name : setZone
     * Description :
     *
     * @param nZone : 醫뚰몴蹂��닔 Zone
     * @return void
     */
    public void setZone(int nZone) {
        this.nZone = nZone;
    }

    /**
     * Method Name : getZone
     * Description :
     *
     * @return int 醫뚰몴蹂��닔 Zone get
     */
    public int getZone() {
        return this.nZone;
    }

    /**
     * Method Name : setCoordX
     * Description :
     *
     * @param dx : 醫뚰몴蹂��닔
     * @return void
     */
    public void setCoordX(double dx) {
        this.dCoordX = dx;
    }

    /**
     * Method Name : getCoordX
     * Description :
     *
     * @return double 醫뚰몴蹂��닔 get
     */
    public double getCoordX() {
        return this.dX+this.dCoordX;
    }

    /**
     * Method Name : setCoordY
     * Description :
     *
     * @param dy : 醫뚰몴蹂��닔
     * @return void
     */
    public void setCoordY(double dy) {
        this.dCoordY = dy;
    }

    /**
     * Method Name : getCoordY
     * Description :
     *
     * @return double 醫뚰몴蹂��닔 Y
     */
    public double getCoordY() {
        return this.dY+this.dCoordY;
    }

    /**
     * Method Name : convert2MGRS
     * Description :
     *
     * @param strMgrs : 醫뚰몴蹂��닔
     * @return void
     */
    public static CoordUTM convertFromMgrs(String strMgrs) {
        if (strMgrs == null) {return null;}
        if (strMgrs.length()<15) 	{return null;}

        CoordUTM rtn =new CoordUTM();
        try {
            //int isph = 21;
            double oneht = 100000.0D;
            double twomil = oneht * 20.0D;
            double zero = 0.0D;

            //int sign = 1;
            rtn.nLetter1st =rtn. alphabet2Int(strMgrs.substring(2, 3));
            rtn.nLetter2nd = rtn.alphabet2Int(strMgrs.substring(3, 4));
            rtn.nLetter3rd =rtn. alphabet2Int(strMgrs.substring(4, 5));

            if (rtn.nLetter1st == 0 || rtn.nLetter2nd == 0 ||  rtn.nLetter3rd == 0 )
            { return null;}
			/*if (rtn.nLetter1st < 14) {
		//	sign = -1;
		}*/

            rtn.nZone = Integer.parseInt(strMgrs.substring(0, 2));// null return

            if ((rtn.nZone == 32) || (rtn.nZone == 34)
                    || (rtn.nZone == 36) || (rtn.nLetter1st == 24)) {
                rtn.dX = 0.0D;
                rtn.dY = 0.0D;
            }

            rtn.setUTM();

            rtn.limitUTM();

            double slcm = (rtn.nZone * 6 - 183) * 0.01745329252D;

            rtn.convertFromWgp(slcm, rtn.dSouth, 1);

            double ylow = (int) ((int) (rtn.dY / oneht) * oneht + 0.5D * rtn.booleanEx(rtn.dY));

            double yslow = ylow;
            while (yslow >= twomil) {
                yslow -= twomil;
            }

            yslow = (int) (yslow + 0.5D * rtn.booleanEx(yslow));

            rtn.dY = ((rtn.nLetter3rd - 1) * oneht + rtn.dLetterFy);
            rtn.dX = ((rtn.nLetter2nd - rtn.nLetterLow + 1) * oneht);

            if ((rtn.nLetterLow == 10) && (rtn.nLetter2nd > 15)) {
                rtn.dX -= oneht;
            }

            if (rtn.nLetter3rd > 15) {
                rtn.dY -= oneht;
            }
            if (rtn.nLetter3rd > 9) {
                rtn.dY -= oneht;
            }

            if ((int) (rtn.dY + 0.5D * rtn.booleanEx(rtn.dY)) >= (int) (twomil + 0.1D)) {
                rtn.dY -= twomil;
            }

            rtn.dY = ((int) (rtn.dY + 0.5D * rtn.booleanEx(rtn.dY)));

            rtn.dY -= yslow;

            if (rtn.dY < zero) {
                rtn.dY += twomil;
            }

            rtn.dY = (ylow + rtn.dY + 0.5D * rtn.booleanEx(ylow + rtn.dY));

            rtn.dCoordX = Integer.parseInt(strMgrs.substring(5, 10));// null check
            rtn.dCoordY = Integer.parseInt(strMgrs.substring(10, 15));//null check
        }
        catch (NumberFormatException e){
            return null;
        }
        return rtn;
    }

    /**
     * Method Name : convertWGP
     * Description :
     *
     * @param dLon :
     * @param dLat :
     * @param nMode :
     * @return void
     */
    public void convertFromWgp(double dLon, double dLat, int nMode) {
        double lat = 0.0D;
        double lon = 0.0D;
        double londeg = 0.0D;

        if (nMode == 1) {
            lat = dLat;
            lon = dLon;
            londeg = dLon / 3.1415926536D * 180.0D;
        } else if (nMode == 2) {
            lat = dLat / 180.0D * 3.1415926536D;
            lon = dLon / 180.0D * 3.1415926536D;
            londeg = dLon;
        } else if (nMode == 3) {

            int d = (int) dLat;
            int m = (int) ((dLat - d) * 100.0D);
            float s = (float) (dLat - d - m / 100) * 10000.0F;
            lat = CoordinateManager.dmsToDeg(d, m, s) / 180.0D * 3.1415926536D;

            d = (int) dLon;
            m = (int) ((dLon - d) * 100.0D);
            s = (float) (dLon - d - m / 100) * 10000.0F;
            lon = CoordinateManager.dmsToDeg(d, m, s) / 180.0D * 3.1415926536D;
            londeg = CoordinateManager.dmsToDeg(d, m, s);
        }

        double a = 6378137.0D;
        double f = 0.0033528106647474805D;
        double fe = 500000.0D;
        double ok = 0.9996D;
        double recf = 1.0D / f;
        double b = a * (recf - 1.0D) / recf;
        double es = (a * a - b * b) / (a * a);
        double ebs = (a * a - b * b) / (b * b);
        double tn = (a - b) / (a + b);
        double ap = a
                * (1.0D - tn + 5.0D * (Math.pow(tn, 2.0D) - Math.pow(tn, 3.0D))
                / 4.0D + 81.0D * (Math.pow(tn, 4.0D) - Math.pow(tn,
                5.0D)) / 64.0D);

        double bp = 3.0D
                * a
                * (tn - Math.pow(tn, 2.0D) + 7.0D
                * (Math.pow(tn, 3.0D) - Math.pow(tn, 4.0D)) / 8.0D + 55.0D * Math
                .pow(tn, 5.0D) / 64.0D) / 2.0D;

        double cp = 15.0D
                * a
                * (Math.pow(tn, 2.0D) - Math.pow(tn, 3.0D) + 3.0D * (Math.pow(
                tn, 4.0D) - Math.pow(tn, 5.0D)) / 4.0D) / 16.0D;

        double dp = 35.0D
                * a
                * (Math.pow(tn, 3.0D) - Math.pow(tn, 4.0D) + 11.0D * Math.pow(
                tn, 5.0D) / 16.0D) / 48.0D;

        double ep = 315.0D * a * (Math.pow(tn, 4.0D) - Math.pow(tn, 5.0D))
                / 512.0D;

        this.nZone = (31 + (int) (londeg / 6.0D));
        if (this.nZone > 60){
            this.nZone = 60;
        }
        if (this.nZone < 1) {
            this.nZone = 1;
        }
        double olam = (this.nZone * 6.0D - 183.0D) * 3.1415926536D / 180.0D;
        double dlam = lon - olam;

        double s = Math.sin(lat);
        double c = Math.cos(lat);
        double t = s / c;
        double eta = ebs * c * c;

        double sn = a / Math.sqrt(1.0D - es * Math.pow(Math.sin(lat), 2.0D));
        double tmd = ap * lat - bp * Math.sin(2.0D * lat) + cp
                * Math.sin(4.0D * lat) - dp * Math.sin(6.0D * lat) + ep
                * Math.sin(8.0D * lat);

        double t1 = tmd * ok;
        double t2 = sn * s * c * ok / 2.0D;
        double t3 = sn
                * s
                * Math.pow(c, 3.0D)
                * ok
                * (5.0D - Math.pow(t, 2.0D) + 9.0D * eta + 4.0D * Math.pow(eta,
                2.0D)) / 24.0D;

        double t4 = sn
                * s
                * Math.pow(c, 5.0D)
                * ok
                * (61.0D - 58.0D * Math.pow(t, 2.0D) + Math.pow(t, 4.0D)
                + 270.0D * eta - 330.0D * Math.pow(t, 2.0D) * eta
                + 445.0D * Math.pow(eta, 2.0D) + 324.0D
                * Math.pow(eta, 3.0D) - 680.0D * Math.pow(eta, 2.0D)
                * Math.pow(t, 2.0D) + 88.0D * Math.pow(eta, 4.0D)
                - 600.0D * Math.pow(t, 2.0D) * Math.pow(eta, 3.0D) - 192.0D
                * Math.pow(t, 2.0D) * Math.pow(eta, 4.0D)) / 720.0D;

        double t5 = sn
                * s
                * Math.pow(c, 7.0D)
                * ok
                * (1385.0D - 3111.0D * Math.pow(t, 2.0D) + 543.0D
                * Math.pow(t, 4.0D) - Math.pow(t, 6.0D)) / 40320.0D;

        double nfn = 0.0D;
        if (lat < 0.0D) {
            nfn = 1.0E7D;
        }

        this.dY = (nfn + t1 + Math.pow(dlam, 2.0D) * t2
                + Math.pow(dlam, 4.0D) * t3 + Math.pow(dlam, 6.0D) * t4 + Math
                .pow(dlam, 8.0D) * t5);

        double t6 = sn * c * ok;
        double t7 = sn * Math.pow(c, 3.0D) * ok
                * (1.0D - Math.pow(t, 2.0D) + eta) / 6.0D;

        double t8 = sn
                * Math.pow(c, 5.0D)
                * ok
                * (5.0D - 18.0D * Math.pow(t, 2.0D) + Math.pow(t, 4.0D) + 14.0D
                * eta - 58.0D * Math.pow(t, 2.0D) * eta + 13.0D
                * Math.pow(eta, 2.0D) + 4.0D * Math.pow(eta, 3.0D)
                - 64.0D * Math.pow(eta, 2.0D) * Math.pow(t, 2.0D) - 24.0D * Math
                .pow(eta, 4.0D)) / 120.0D;

        double t9 = sn
                * Math.pow(c, 7.0D)
                * ok
                * (61.0D - 479.0D * Math.pow(t, 2.0D) + 179.0D
                * Math.pow(t, 4.0D) - Math.pow(t, 6.0D)) / 5040.0D;

        this.dX = (fe + dlam * t6 + Math.pow(dlam, 3.0D) * t7
                + Math.pow(dlam, 5.0D) * t8 + Math.pow(dlam, 7.0D) * t9);
    }

    /**
     * Method Name : SetUTM
     * Description :
     *
     * @return void
     */
    private void setUTM() {
//		int isph = 21;

        int i = 1;

        while ((i < 7) && ((this.nZone - i) / 6 * 6 + i != this.nZone)) {

            i++;
        }

        switch (i % 3) {
            case 0:
                this.nLetterLow = 19;
                this.nLetterHi = 26;
                break;
            case 1:
                this.nLetterLow = 1;
                this.nLetterHi = 8;
                break;
            case 2:
                this.nLetterLow = 10;
                this.nLetterHi = 18;
                break;
            default:
                break;
        }

        double dTemp = 0.0D;

        if (i % 2 == 0) {
            dTemp = 1500000.0D;
        }
        this.dLetterFy = dTemp;
    }

    /**
     * Method Name : LimitUTM
     * Description :
     *
     * @return void
     */
    private void limitUTM() {
        if (this.nLetter1st == 0) {

            this.nLetter1st = 13;

            if (this.nLetter1st > 8){
                this.nLetter1st += 1;
            }
            if (this.nLetter1st > 14) {
                this.nLetter1st += 1;
            }
            if (this.nLetter1st >= 25) {
                this.nLetter1st = 24;
            }
            if (this.nLetter1st == 12) {
                this.nLetter1st = 14;
            }
        }
        int isphi = (this.nLetter1st - 3) * 8 - 80;

        if (this.nLetter1st > 8){
            isphi -= 8;
        }
        if (this.nLetter1st > 14){
            isphi -= 8;
        }
        this.dSouth = (isphi * 0.01745329252D);

        this.dNorth = (this.dSouth + 0.13962634016D);

        if (this.nLetter1st == 24) {
            this.dNorth = (this.dSouth + 0.20943951024000002D);
        }

        int icm = this.nZone * 6 - 183;
        double slcm = icm * 0.01745329252D;

        this.dEast = (slcm + 0.052359877560000004D);
        this.dWest = (slcm - 0.052359877560000004D);

        if ((this.nZone < 31) || (this.nZone > 37)) {
            return;
        }

        if (this.nLetter1st < 22) {
            return;
        }

        if ((this.nLetter1st == 22) && (this.nZone == 31)) {
            this.dEast = 0.052359877560000004D;
        }
        if ((this.nLetter1st == 22) && (this.nZone == 32)) {
            this.dWest = 0.052359877560000004D;
        }

        if (this.nLetter1st < 24) {
            return;
        }

        if (this.nZone == 31) {
            this.dEast = 0.15707963268D;
        }

        if (this.nZone == 33) {
            this.dWest = 0.15707963268D;
        }

        if (this.nZone == 33) {
            this.dEast = 0.36651914292D;
        }

        if (this.nZone == 35) {
            this.dWest = 0.36651914292D;
        }

        if (this.nZone == 35) {
            this.dEast = 0.5759586531600001D;
        }

        if (this.nZone == 37) {
            this.dWest = 0.5759586531600001D;
        }
    }

    /**
     * Method Name : BooleanEx
     * Description :
     *
     */
    private int booleanEx(double x) {
        if (x > 0.0D){
            return 1;
        }
        if (x < 0.0D) {
            return -1;
        }
        return 0;
    }

    /**
     * Method Name : Alphabet2Int
     * Description :
     *
     * @param str : 醫뚰몴蹂��닔
     * @return int 醫뚰몴媛�
     */

    private int alphabet2Int(String str) {
        int n = 0;

        if ("A".equals(str)) {				n = 1;
        } else if ("B".equals(str)) {		n = 2;
        } else if ("C".equals(str)) {		n = 3;
        } else if ("D".equals(str)) {		n = 4;
        } else if ("E".equals(str)) {		n = 5;
        } else if ("F".equals(str)) {		n = 6;
        } else if ("G".equals(str)) {		n = 7;
        } else if ("H".equals(str)) {		n = 8;
        } else if ("I".equals(str)) {		n = 9;
        } else if ("J".equals(str)) {		n = 10;
        } else if ("K".equals(str)) {		n = 11;
        } else if ("L".equals(str)) {		n = 12;
        } else if ("M".equals(str)) {		n = 13;
        } else if ("N".equals(str)) {		n = 14;
        } else if ("O".equals(str)) {		n = 15;
        } else if ("P".equals(str)) {		n = 16;
        } else if ("Q".equals(str)) {		n = 17;
        } else if ("R".equals(str)) {		n = 18;
        } else if ("S".equals(str)) {		n = 19;
        } else if ("T".equals(str)) {		n = 20;
        } else if ("U".equals(str)) {		n = 21;
        } else if ("V".equals(str)) {		n = 22;
        } else if ("W".equals(str)) {		n = 23;
        } else if ("X".equals(str)) {		n = 24;
        } else if ("Y".equals(str)) {		n = 25;
        } else if ("Z".equals(str)) {		n = 26;
        }
        return n;
    }
    public String degToUtm(double Lat, double Lon){
        String utm="";
        String Letter="";
        double Zone= Math.floor(Lon/6+31);
        if (Lat<-72)
            Letter="C";
        else if (Lat<-64)
            Letter="D";
        else if (Lat<-56)
            Letter="E";
        else if (Lat<-48)
            Letter="F";
        else if (Lat<-40)
            Letter="G";
        else if (Lat<-32)
            Letter="H";
        else if (Lat<-24)
            Letter="J";
        else if (Lat<-16)
            Letter="K";
        else if (Lat<-8)
            Letter="L";
        else if (Lat<0)
            Letter="M";
        else if (Lat<8)
            Letter="N";
        else if (Lat<16)
            Letter="P";
        else if (Lat<24)
            Letter="Q";
        else if (Lat<32)
            Letter="F";
        else if (Lat<40)
            Letter="S";
        else if (Lat<48)
            Letter="T";
        else if (Lat<56)
            Letter="U";
        else if (Lat<64)
            Letter="Y";
        else if (Lat<72)
            Letter="W";
        else
            Letter="X";
        double Easting=0.5* Math.log((1+ Math.cos(Lat* Math.PI/180)* Math.sin(Lon* Math.PI/180-(6*Zone-183)* Math.PI/180))/(1- Math.cos(Lat* Math.PI/180)* Math.sin(Lon* Math.PI/180-(6*Zone-183)* Math.PI/180)))*0.9996*6399593.62/ Math.pow((1+ Math.pow(0.0820944379, 2)* Math.pow(Math.cos(Lat* Math.PI/180), 2)), 0.5)*(1+ Math.pow(0.0820944379,2)/2* Math.pow((0.5* Math.log((1+ Math.cos(Lat* Math.PI/180)* Math.sin(Lon* Math.PI/180-(6*Zone-183)* Math.PI/180))/(1- Math.cos(Lat* Math.PI/180)* Math.sin(Lon* Math.PI/180-(6*Zone-183)* Math.PI/180)))),2)* Math.pow(Math.cos(Lat* Math.PI/180),2)/3)+500000;
        Easting= Math.round(Easting*100)*0.01;
        double Northing = (Math.atan(Math.tan(Lat* Math.PI/180)/ Math.cos((Lon* Math.PI/180-(6*Zone -183)* Math.PI/180)))-Lat* Math.PI/180)*0.9996*6399593.625/ Math.sqrt(1+0.006739496742* Math.pow(Math.cos(Lat* Math.PI/180),2))*(1+0.006739496742/2* Math.pow(0.5* Math.log((1+ Math.cos(Lat* Math.PI/180)* Math.sin((Lon* Math.PI/180-(6*Zone -183)* Math.PI/180)))/(1- Math.cos(Lat* Math.PI/180)* Math.sin((Lon* Math.PI/180-(6*Zone -183)* Math.PI/180)))),2)* Math.pow(Math.cos(Lat* Math.PI/180),2))+0.9996*6399593.625*(Lat* Math.PI/180-0.005054622556*(Lat* Math.PI/180+ Math.sin(2*Lat* Math.PI/180)/2)+4.258201531e-05*(3*(Lat* Math.PI/180+ Math.sin(2*Lat* Math.PI/180)/2)+ Math.sin(2*Lat* Math.PI/180)* Math.pow(Math.cos(Lat* Math.PI/180),2))/4-1.674057895e-07*(5*(3*(Lat* Math.PI/180+ Math.sin(2*Lat* Math.PI/180)/2)+ Math.sin(2*Lat* Math.PI/180)* Math.pow(Math.cos(Lat* Math.PI/180),2))/4+ Math.sin(2*Lat* Math.PI/180)* Math.pow(Math.cos(Lat* Math.PI/180),2)* Math.pow(Math.cos(Lat* Math.PI/180),2))/3);
        if ((Letter.charAt(0)<"M".charAt(0)))
            Northing = Northing + 10000000;

        Northing= Math.round(Northing*100)*0.01;
        utm = String.format("%.0f%s 동거 %.0f 북거 %.0f",Zone,Letter,Easting,Northing);
        return utm;
    }
}
