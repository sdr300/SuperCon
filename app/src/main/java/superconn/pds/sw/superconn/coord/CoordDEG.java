package superconn.pds.sw.superconn.coord;

public class CoordDEG {
    private double dLon;
    private double dLat;
    //public final CoordinateManager pManagerl;
    /**
     * Method Name : CoordDEG
     * Description :
     */
    public CoordDEG(){
        this.dLon = 0.0D;
        this.dLat = 0.0D;
        //  this.pManagerl = new CoordinateManager();
    }

    /**
     * Method Name : detLon
     * Description :
     *
     * @param dLon : 醫뚰몴蹂��닔
     * @return void
     */
    public void setLon(double dLon) { this.dLon = dLon; }

    /**
     * Method Name : getLon
     * Description :
     *
     * @return double �꽌鍮꾩뒪 泥섎━ 寃곌낵 媛믪쓣 諛섑솚
     */
    public double getLon() { return this.dLon; }

    /**
     * Method Name : setLat
     * Description :
     *
     * @param dLat : 醫뚰몴蹂��닔
     * @return void
     */
    public void setLat(double dLat) { this.dLat = dLat; }

    /**
     * Method Name : getLat
     * Description :
     *
     * @return double �꽌鍮꾩뒪 泥섎━ 寃곌낵 媛믪쓣 諛섑솚
     */
    public double getLat() { return this.dLat; }

    /**
     * Method Name : convertFromUtm
     * Description : UTM醫뚰몴濡� 蹂��솚 硫붿냼�뱶
     *
     * @param dX : 醫뚰몴蹂��닔 X
     * @param dYY : 醫뚰몴蹂��닔 Y
     * @param nZone : 醫뚰몴蹂��닔 zone
     * @return void
     */
    public void convertFromUtm(double dX, double dYY, int nZone){
        double a = 6378137.0D;
        double f = 0.0033528106647474805D;
        double fe = 500000.0D;
        double ok = 0.9996D;
        double degrad = Math.atan(1.0D) / 45.0D;

        double recf = 1.0D / f;
        double b = a * (recf - 1.0D) / recf;
        double es = (Math.pow(a, 2.0D) - Math.pow(b, 2.0D)) / Math.pow(a, 2.0D);
        double ebs = (Math.pow(a, 2.0D) - Math.pow(b, 2.0D)) / Math.pow(b, 2.0D);
        double tn = (a - b) / (a + b);
        double ap = a * (1.0D - tn + 5.0D * (Math.pow(tn, 2.0D) - Math.pow(tn, 3.0D)) / 4.0D + 81.0D * (Math.pow(tn, 4.0D) - Math.pow(tn, 5.0D)) / 64.0D);
        double bp = 3.0D * a * (tn - Math.pow(tn, 2.0D) + 7.0D * (Math.pow(tn, 3.0D) - Math.pow(tn, 4.0D)) / 8.0D + 55.0D * Math.pow(tn, 5.0D) / 64.0D) / 2.0D;

        double cp = 15.0D * a * (Math.pow(tn, 2.0D) - Math.pow(tn, 3.0D) + 3.0D * (Math.pow(tn, 4.0D) - Math.pow(tn, 5.0D)) / 4.0D) / 16.0D;
        double dp = 35.0D * a * (Math.pow(tn, 3.0D) - Math.pow(tn, 4.0D) + 11.0D * Math.pow(tn, 5.0D) / 16.0D) / 48.0D;
        double ep = 315.0D * a * (Math.pow(tn, 4.0D) - Math.pow(tn, 5.0D)) / 512.0D;

        double nfn = 0.0D;
        if (dYY < 0.0D)
        {
            nfn = 1.0E7D;
        }

        double dY = Math.abs(dYY);

        double tmd = (dY - nfn) / ok;

        double sphi = 0.0D;
        double sr = a * (1.0D - es) / Math.pow(Math.sqrt(1.0D - es * Math.pow(Math.sin(sphi), 2.0D)), 3.0D);

        double ftphi = tmd / sr;

        for (int i = 0; i < 5; i++)
        {
            double t10 = ap * ftphi - bp * Math.sin(2.0D * ftphi) + cp * Math.sin(4.0D * ftphi) - dp * Math.sin(6.0D * ftphi) + ep * Math.sin(8.0D * ftphi);
            sr = a * (1.0D - es) / Math.pow(Math.sqrt(1.0D - es * Math.pow(Math.sin(ftphi), 2.0D)), 3.0D);
            ftphi += (tmd - t10) / sr;
        }

        sr = a * (1.0D - es) / Math.pow(Math.sqrt(1.0D - es * Math.pow(Math.sin(ftphi), 2.0D)), 3.0D);

        double sn = a / Math.sqrt(1.0D - es * Math.sin(ftphi) * Math.sin(ftphi));

        double s = Math.sin(ftphi);
        double c = Math.cos(ftphi);
        double t = s / c;
        double eta = ebs * c * c;

        double de = dX - fe;

        double t10 = t / (2.0D * sr * sn * ok * ok);

        double t11 = t * (5.0D + 3.0D * Math.pow(t, 2.0D) + eta - 4.0D * Math.pow(eta, 2.0D) - 9.0D * Math.pow(t, 2.0D) * eta) / (24.0D * sr * Math.pow(sn, 3.0D) * Math.pow(ok, 4.0D));

        double t12 = t * (61.0D + 90.0D * Math.pow(t, 2.0D) + 46.0D * eta + 45.0D * Math.pow(t, 4.0D) - 252.0D * Math.pow(t, 2.0D) * eta - 3.0D * Math.pow(eta, 2.0D) + 100.0D * Math.pow(eta, 3.0D) - 66.0D * Math.pow(t, 2.0D) * Math.pow(eta, 2.0D) - 90.0D * Math.pow(t, 4.0D) * eta + 88.0D * Math.pow(eta, 4.0D) + 225.0D * Math.pow(t, 4.0D) * Math.pow(eta, 2.0D) + 84.0D * Math.pow(t, 2.0D) * Math.pow(eta, 3.0D) - 192.0D * Math.pow(t, 2.0D) * Math.pow(eta, 4.0D)) / (720.0D * sr * Math.pow(sn, 5.0D) * Math.pow(ok, 6.0D));

        double t13 = t * (1385.0D + 3633.0D * Math.pow(t, 2.0D) + 4095.0D * Math.pow(t, 4.0D) + 1575.0D * Math.pow(t, 6.0D)) / (40320.0D * sr * Math.pow(sn, 7.0D) * Math.pow(ok, 8.0D));

        sphi = ftphi - Math.pow(de, 2.0D) * t10 + Math.pow(de, 4.0D) * t11 - Math.pow(de, 6.0D) * t12 + Math.pow(de, 8.0D) * t13;

        double t14 = 1.0D / (sn * c * ok);

        double t15 = (1.0D + 2.0D * Math.pow(t, 2.0D) + eta) / (6.0D * Math.pow(sn, 3.0D) * c * Math.pow(ok, 3.0D));

        double t16 = (5.0D + 6.0D * eta + 28.0D * Math.pow(t, 2.0D) - 3.0D * Math.pow(eta, 2.0D) + 8.0D * Math.pow(t, 2.0D) * eta + 24.0D * Math.pow(t, 4.0D) - 4.0D * Math.pow(eta, 3.0D) + 4.0D * Math.pow(t, 2.0D) * Math.pow(eta, 2.0D) + 24.0D * Math.pow(t, 2.0D) * Math.pow(eta, 3.0D)) / (120.0D * Math.pow(sn, 5.0D) * c * Math.pow(ok, 5.0D));

        double t17 = (61.0D + 662.0D * Math.pow(t, 2.0D) + 1320.0D * Math.pow(t, 4.0D) + 720.0D * Math.pow(t, 6.0D)) / (5040.0D * Math.pow(sn, 7.0D) * c * Math.pow(ok, 7.0D));

        double dlam = de * t14 - Math.pow(de, 3.0D) * t15 + Math.pow(de, 5.0D) * t16 - Math.pow(de, 7.0D) * t17;
        double olam = (nZone * 6 - 183) * degrad;
        double slam = olam + dlam;

        sphi = sphi * 180.0D / 3.1415926536D;
        slam = slam * 180.0D / 3.1415926536D;
        this.dLon = slam;
        this.dLat = sphi;
    }

    /**
     * Method Name : convertFromMgrs
     * Description : MGRS醫뚰몴濡� 蹂��솚�븯�뒗 硫붿냼�뱶
     *
     * @param dX : 醫뚰몴蹂��닔 X
     * @param dYY : 醫뚰몴蹂��닔 Y
     * @param nZone : 醫뚰몴蹂��닔 zone
     * @return void
     */
    public void convertFromMgrs(double dX, double dYY, int nZone){
        double a = 6378137.0D;
        double f = 0.0033528106647474805D;
        double fe = 500000.0D;
        double ok = 0.9996D;
        double degrad = Math.atan(1.0D) / 45.0D;

        double recf = 1.0D / f;
        double b = a * (recf - 1.0D) / recf;
        double es = (Math.pow(a, 2.0D) - Math.pow(b, 2.0D)) / Math.pow(a, 2.0D);
        double ebs = (Math.pow(a, 2.0D) - Math.pow(b, 2.0D)) / Math.pow(b, 2.0D);
        double tn = (a - b) / (a + b);
        double ap = a * (1.0D - tn + 5.0D * (Math.pow(tn, 2.0D) - Math.pow(tn, 3.0D)) / 4.0D + 81.0D * (Math.pow(tn, 4.0D) - Math.pow(tn, 5.0D)) / 64.0D);
        double bp = 3.0D * a * (tn - Math.pow(tn, 2.0D) + 7.0D * (Math.pow(tn, 3.0D) - Math.pow(tn, 4.0D)) / 8.0D + 55.0D * Math.pow(tn, 5.0D) / 64.0D) / 2.0D;

        double cp = 15.0D * a * (Math.pow(tn, 2.0D) - Math.pow(tn, 3.0D) + 3.0D * (Math.pow(tn, 4.0D) - Math.pow(tn, 5.0D)) / 4.0D) / 16.0D;
        double dp = 35.0D * a * (Math.pow(tn, 3.0D) - Math.pow(tn, 4.0D) + 11.0D * Math.pow(tn, 5.0D) / 16.0D) / 48.0D;
        double ep = 315.0D * a * (Math.pow(tn, 4.0D) - Math.pow(tn, 5.0D)) / 512.0D;

        double nfn = 0.0D;
        if (dYY < 0.0D)
        {
            nfn = 1.0E7D;
        }
        double dY = Math.abs(dYY);

        double tmd = (dY - nfn) / ok;

        double sphi = 0.0D;
        double sr = a * (1.0D - es) / Math.pow(Math.sqrt(1.0D - es * Math.pow(Math.sin(sphi), 2.0D)), 3.0D);

        double ftphi = tmd / sr;
        for (int i = 0; i < 5; i++)
        {
            double t10 = ap * ftphi - bp * Math.sin(2.0D * ftphi) + cp * Math.sin(4.0D * ftphi) - dp * Math.sin(6.0D * ftphi) + ep * Math.sin(8.0D * ftphi);
            sr = a * (1.0D - es) / Math.pow(Math.sqrt(1.0D - es * Math.pow(Math.sin(ftphi), 2.0D)), 3.0D);
            ftphi += (tmd - t10) / sr;
        }

        sr = a * (1.0D - es) / Math.pow(Math.sqrt(1.0D - es * Math.pow(Math.sin(ftphi), 2.0D)), 3.0D);

        double sn = a / Math.sqrt(1.0D - es * Math.sin(ftphi) * Math.sin(ftphi));

        double s = Math.sin(ftphi);
        double c = Math.cos(ftphi);
        double t = s / c;
        double eta = ebs * c * c;

        double de = dX - fe;

        double t10 = t / (2.0D * sr * sn * ok * ok);

        double t11 = t * (5.0D + 3.0D * Math.pow(t, 2.0D) + eta - 4.0D * Math.pow(eta, 2.0D) - 9.0D * Math.pow(t, 2.0D) * eta) / (24.0D * sr * Math.pow(sn, 3.0D) * Math.pow(ok, 4.0D));

        double t12 = t * (61.0D + 90.0D * Math.pow(t, 2.0D) + 46.0D * eta + 45.0D * Math.pow(t, 4.0D) - 252.0D * Math.pow(t, 2.0D) * eta - 3.0D * Math.pow(eta, 2.0D) + 100.0D * Math.pow(eta, 3.0D) - 66.0D * Math.pow(t, 2.0D) * Math.pow(eta, 2.0D) - 90.0D * Math.pow(t, 4.0D) * eta + 88.0D * Math.pow(eta, 4.0D) + 225.0D * Math.pow(t, 4.0D) * Math.pow(eta, 2.0D) + 84.0D * Math.pow(t, 2.0D) * Math.pow(eta, 3.0D) - 192.0D * Math.pow(t, 2.0D) * Math.pow(eta, 4.0D)) / (720.0D * sr * Math.pow(sn, 5.0D) * Math.pow(ok, 6.0D));
        double t13 = t * (1385.0D + 3633.0D * Math.pow(t, 2.0D) + 4095.0D * Math.pow(t, 4.0D) + 1575.0D * Math.pow(t, 6.0D)) / (40320.0D * sr * Math.pow(sn, 7.0D) * Math.pow(ok, 8.0D));

        sphi = ftphi - Math.pow(de, 2.0D) * t10 + Math.pow(de, 4.0D) * t11 - Math.pow(de, 6.0D) * t12 + Math.pow(de, 8.0D) * t13;

        double t14 = 1.0D / (sn * c * ok);

        double t15 = (1.0D + 2.0D * Math.pow(t, 2.0D) + eta) / (6.0D * Math.pow(sn, 3.0D) * c * Math.pow(ok, 3.0D));

        double t16 = (5.0D + 6.0D * eta + 28.0D * Math.pow(t, 2.0D) - 3.0D * Math.pow(eta, 2.0D) + 8.0D * Math.pow(t, 2.0D) * eta + 24.0D * Math.pow(t, 4.0D) - 4.0D * Math.pow(eta, 3.0D) + 4.0D * Math.pow(t, 2.0D) * Math.pow(eta, 2.0D) + 24.0D * Math.pow(t, 2.0D) * Math.pow(eta, 3.0D)) / (120.0D * Math.pow(sn, 5.0D) * c * Math.pow(ok, 5.0D));


        double t17 = (61.0D + 662.0D * Math.pow(t, 2.0D) + 1320.0D * Math.pow(t, 4.0D) + 720.0D * Math.pow(t, 6.0D)) / (5040.0D * Math.pow(sn, 7.0D) * c * Math.pow(ok, 7.0D));

        double dlam = de * t14 - Math.pow(de, 3.0D) * t15 + Math.pow(de, 5.0D) * t16 - Math.pow(de, 7.0D) * t17;
        double olam = (nZone * 6 - 183) * degrad;
        double slam = olam + dlam;

        sphi = sphi * 180.0D / 3.1415926536D;
        slam = slam * 180.0D / 3.1415926536D;
        this.dLon = slam;
        this.dLat = sphi;
    }
}
