/*
 본 프로그램 소스에 대한 모든 권리는 (주)씨엔지솔루션에 있습니다.
 Copyright 2017 by COP and GeoINT Solution Inc. All rights reserved.

 이 파일은 ICOPS 패키지의 일부이며, 'ICOPS-COPYRIGHT.TXT' 파일에 기술된 아이캅스 사용권
 조건에서만 사용, 변경, 배포를 할 수 있습니다. 이 파일을 사용, 변경, 배포하는 것은 당신이 라이선스를
 충분이 이해하고 받아들인다는 것을 의미합니다.

 This file is part of ICOPS package, and may only be used, modified,
 and distributed under the terms of the ICOPS license described in the
 file 'ICOPS-COPYRIGHT.TXT'. By continuing to use, modify, or distribute
 this file you indicate that you have read the license and understand and
 accept it fully.
 */
package superconn.pds.sw.superconn.ICOPS;

import java.util.ArrayList;

import icops.framework.common.GDI;
import icops.framework.common.Geometry;
import icops.warsym.inf.IWarSymbol;

public class DemoSymbol {
	public static boolean mDrawSimple = false;
	public IWarSymbol mSymbol = null;
	public Geometry.Point.Array mPoints = new Geometry.Point.Array();
	public boolean hudSym = false;
	public String distance = "";

	public void draw(GDI.Grpx pGrpx, double mpw, GDI.Color xorColor) {
		if (mSymbol==null | mPoints.size()<=0) {
			return;
		}

		Geometry.Point[] pBuf = mPoints.getBuffer();
		mSymbol.draw(pGrpx,  pBuf,  mpw);
		if (mDrawSimple) {
			mSymbol.simpleDraw(pGrpx, pBuf, mpw, xorColor);
		}
	}

	public static class Array extends ArrayList<DemoSymbol> {
		private static final long serialVersionUID = -5687559083543987796L;
	}
}
