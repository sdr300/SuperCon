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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import icops.framework.common.BasicReturn;
import icops.framework.common.GDI;
import icops.framework.common.Geometry;
import icops.warsym.common.CwsColorset;
import icops.warsym.common.CwsModifier;
import icops.warsym.common.WarSym;
import icops.warsym.inf.IWarSymFactory;
import icops.warsym.inf.IWarSymPrpTacGraphic;
import icops.warsym.inf.IWarSymPrpTacSymbol;
import icops.warsym.inf.IWarSymView;
import icops.warsym.inf.IWarSymbol;
import icops.warsym.test.SpeedTester;
import superconn.pds.sw.superconn.MapActivity;

/**
 * @author (주)씨엔지솔루션, 임창관
 * 
 */

@SuppressLint("DefaultLocale")
public class DemoMainView extends View
{
	private DemoSymbol.Array symbols = new DemoSymbol.Array();
	private GDI.Color mXorColor = new GDI.Color(128, 255, 0, 0);
	private GDI.Pen mAxisPen = new GDI.Pen(new GDI.Color(255, 0, 254,48 ), 2.5f);
	private GDI.Grpx mGrpx = new GDI.Grpx(null);
	private SpeedTester mSpeedTester = null;
	private boolean mShowSpeedTest = false;
	Paint paint = new Paint();
	int[] arr = new int[2];

	public MapActivity mMain = null;

	public DemoMainView(Context context) {
		super(context);
		this.getLocationOnScreen(arr);
	}

	public DemoMainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.getLocationOnScreen(arr);
	}

	public void testDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		android.graphics.Path dph = new android.graphics.Path();

		dph.moveTo(-0.5f, -5.0f);
		dph.lineTo(0.5f, -5.0f);
		dph.lineTo(0.5f, 5.0f);
		dph.lineTo(-0.5f, 5.0f);

		PathDashPathEffect pdpe = new PathDashPathEffect(dph, 5.0f, 0.0f, PathDashPathEffect.Style.MORPH);
		paint.setPathEffect(pdpe);
//		paint.setStrokeWidth(20.0f);
		float cx = 0.5f * getWidth();
		float cy = 0.5f * getHeight();
		float radius = 0.8f * Math.min(cx, cy);
		canvas.drawCircle(cx, cy, radius, paint);
		canvas.drawLine(5.0f, 5.0f, cx, cy, paint);

		android.graphics.Path ph = new android.graphics.Path();
		ph.moveTo(0.0f, 40.0f);
		ph.lineTo(100.0f, 40.0f);
		ph.lineTo(90.0f, 100.0f);
		ph.lineTo(200.0f, 150.0f);
		canvas.drawPath(ph, paint);
		
	}

	@Override
	public void layout(int l, int t, int r, int b) {
		this.getLocationOnScreen(arr);
		super.layout(l, t, r+arr[0], b+arr[1]);
	}

	public void onDraw(Canvas canvas) {
//		testDraw(canvas);
		paint.setARGB(255,255,255,255);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setTextSize(14);

		// 성능시험 도시
		if (mSpeedTester!=null) {
			if (mShowSpeedTest) {
				mGrpx.setTarget(canvas);
				String strElapsed = mSpeedTester.draw(mGrpx);
				String strMsg = String.format("부호개수: %d\n소요시간(초): %s", mSpeedTester.mSymbols.length, strElapsed);
			}
			return;
		}

		mGrpx.setTarget(canvas);

		int w = getWidth();
		int h = getHeight();
		double mpw = 1.0;		// 임시
		Log.d("아이캅스 뷰 크기 ","width: "+ w+" height : "+h);

		DemoSymbol sym;
		int count = symbols.size();
		for (int inx = 0; inx < count; inx++) {
			sym = symbols.get(inx);
			if (sym.mPoints.size()<=0) {
				// 미리보기 참조점 얻기
				sym.mPoints.set(sym.mSymbol.getPreviewPoints(w/2, h/2));
			}

			sym.draw(mGrpx, mpw, mXorColor);
			if(sym.hudSym){
				if ( Double.parseDouble(sym.distance) >= 1000 ) {
					canvas.drawText(String.format("%.2f", Double.parseDouble(sym.distance)/1000)


							+"km",sym.mPoints.get(0).x-20,sym.mPoints.get(0).y-25,paint);
				}else{
					canvas.drawText(sym.distance+"m",sym.mPoints.get(0).x-20,sym.mPoints.get(0).y-25,paint);
				}

			}
		}

		/*// 중심축 도시
		mAxisPen.apply(mGrpx);
		mGrpx.drawLine(0.0f+arr[0], (0.5f *( h - arr[1]) )+ arr[1], w+arr[0], (0.5f * (h - arr[1])) + arr[1]);
		mGrpx.drawLine(0.5f * w+arr[0], 0.0f+arr[1], 0.5f * w+arr[0], h+arr[1]);*/
	}


	public void drawMulti(Geometry.Point.Array mPoints, String code) {
		//symbols.clear();

		// 군대부호 객체 생성
		DemoSymbol demoSym = onShowTacGraphic(code);

		if (demoSym==null) {
			return;
		}
		demoSym.mPoints.copy(mPoints);
		IWarSymbol symbol = demoSym.mSymbol;
		// 수식정보 변경
		IWarSymView symView = symbol.getSymbolView();

		// 도시 속성 설정 인터페이스 얻기
		BasicReturn.TypeObject retObj = new BasicReturn.TypeObject();
		boolean res = symbol.getPropInterface("icops.warsym.inf.IWarSymPrpTacSymbol",  retObj);
		if (res) {
			try {
				IWarSymPrpTacSymbol symPrp = (IWarSymPrpTacSymbol) retObj.value;
				// 부호크기
				symPrp.setSymbolSize(2.0f);

			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		// 피아구분 및 수식정보 변경 내용 반영
		symView.linkFrameSymbol();
		symView.linkModifierInfo();

		invalidate();
	}

	public void drawSingle(String code, int cnt, Geometry.Point.Array gPoints) {
		//symbols.clear();

		// 군대부호 객체 생성
//		DemoSymbol demoSym = createSymbol("SFGPEV------***");
		DemoSymbol demoSym = createSymbol(code);
		if (demoSym==null) {
			return;
		}
		demoSym.mPoints.copy(gPoints);
		IWarSymbol symbol = demoSym.mSymbol;
		// 수식정보 변경
		IWarSymView symView = symbol.getSymbolView();
		CwsModifier symMod = symView.getModifier();
		if (!code.substring(code.length()-1).equals("*")){
			symMod.setModifierValue("B",code.substring(code.length()-1));
		}
		if(cnt>0){
			symMod.setModifierValue("C", String.valueOf(cnt));
		}
		//symMod.setModifierValue("T", "215");
		//symMod.setModifierValue("M", "33");
		//symMod.Set("S", "1");	// 지휘소 표시

		// 도시 속성 설정 인터페이스 얻기
		BasicReturn.TypeObject retObj = new BasicReturn.TypeObject();
		boolean res = symbol.getPropInterface("icops.warsym.inf.IWarSymPrpTacSymbol",  retObj);
		if (res) {
			try {
				IWarSymPrpTacSymbol symPrp = (IWarSymPrpTacSymbol) retObj.value;

				// 피아구분
				symPrp.setAffiliation(code.charAt(1));	// 피아구분 코드 문자 사용

				// 외형 색상
				CwsColorset.Return retColorset = new CwsColorset.Return();
				boolean useCustomColor = false;
				res = symPrp.getColorset(retColorset);
				if (res) {
					CwsColorset colorset = retColorset.value;
					symPrp.setIsCustomFrameColor(useCustomColor);
					GDI.Color frmColor;
					if (useCustomColor) {
						// 사용자지정 색상 사용
						frmColor = new GDI.Color(128, 0, 0, 255);	// 임의의 색상 지정
						colorset.setAffiliationColor(WarSym.ColorInx.Custom, frmColor);
					} else {
						// 표준색상 사용
						int frmColorInx = CwsColorset.getStdAffiliationColorIndex(symbol.getAffiliation());
						frmColor = CwsColorset.getStdFrameColor(frmColorInx);
						colorset.setAffiliationColor(frmColorInx, frmColor);
					}
				}

				// 상태구분 및 운용조건 표시 위치 설정
				//symPrp.setStatus(WarSym.StateCode.Destroyed);
				//symPrp.setOpCondDisplayMode(WarSym.OpCondDispMode.Bottom);
				// 부호크기
				symPrp.setSymbolSize(3.0f);

				// 기능부호 표시 여부
				symPrp.setShowIcon(true);

				// 외형부호 표시 여부
				symPrp.setShowFrame(true);

				// 외형 채움 여부
				symPrp.setFillFrame(true);

				// 외형 물채움 (외형 채움 설정 무시됨)
				//symPrp.SetFillRate(0.5f); // 0.0 ~ 1.0 : 해당 비율로 물채움, 음수 : 물채움 하지 않음
				//symPrp.SetFillRateColor(new GDI.Color(128, 0, 0, 255)); // 반투명 파랑색으로 물채움

				// 민간부호 구분 - 표준색상을 사용해야 적용됨
				/*char aff = symbol.getAffiliation();
				if (aff=='H' || aff=='S' || aff=='J' || aff=='K') {
					// 적군이면 민간구분 표시하지 않음
					symPrp.setClassifyCIV(false);
				} else {
					symPrp.setClassifyCIV(true);
				}*/
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		// 피아구분 및 수식정보 변경 내용 반영
		symView.linkFrameSymbol();
		symView.linkModifierInfo();

		invalidate();
	}

	protected DemoSymbol createSymbol(String symCode) {
		if (mMain==null) {
			return null;
		}
		if (mMain.mAppCxt==null) {
			return null;
		}

		BasicReturn.TypeObject retObj = new BasicReturn.TypeObject();
		boolean res;

		// 군대부호 관리 객체 얻기
		res = mMain.mAppCxt.getStockObject("icops.warsym.inf.IWarSymFactory", retObj);
		if (!res) {
			// 군대부호 관리 객체 얻기 실패
			return null;
		}

		IWarSymFactory symFac;
		try {
			symFac = (IWarSymFactory) retObj.value;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}

		// 군대부호 객체 생성
		IWarSymbol.Return retSym = new IWarSymbol.Return();
		res = symFac.createSymbol(symCode, retSym, true, true);
		if (!res) {
			// 군대부호 객체 생성 실패
			return null;
		}
		
		// 부호 목록에 추가
		DemoSymbol demoSym = new DemoSymbol();
		demoSym.mSymbol = retSym.value;

		symbols.add(demoSym);

		return demoSym;
	}

	protected DemoSymbol onShowTacGraphic(String symCode) {
		// 군대부호 객체 생성
		DemoSymbol demoSym = createSymbol(symCode);
		if (demoSym==null) {
			return null;
		}
		IWarSymbol symbol = demoSym.mSymbol;

		// 도시 속성 설정 인터페이스 얻기
		BasicReturn.TypeObject retObj = new BasicReturn.TypeObject();
		boolean res = symbol.getPropInterface("icops.warsym.inf.IWarSymPrpTacGraphic",  retObj);
		if (res) {
			try {
				IWarSymPrpTacGraphic symPrp = (IWarSymPrpTacGraphic) retObj.value;

				// 피아구분 설정
				// symPrp.SetAffiliation('F');	// 피아구분 코드 문자 사용

				// 부호 색상
				CwsColorset.Return retColorset = new CwsColorset.Return();
				boolean useCustomColor = false;
				GDI.Color frmColor = null;
				res = symPrp.getColorset(retColorset);
				if (res) {
					CwsColorset colorset = retColorset.value;
					symPrp.setIsCustomColor(useCustomColor);
					if (useCustomColor) {
						// 사용자지정 색성 사용
						frmColor = new GDI.Color(128, 0, 255, 0);	// 임의의 색상 지정
						colorset.setAffiliationColor(WarSym.ColorInx.Custom, frmColor);
					}
				}

				// 부호크기
				// symPrp.SetSymbolSize(5.0f);
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
		return demoSym;
	}

	public void clearSymbol(){
		symbols.clear();
	}

	public void drawSingle(String code, int cnt, Geometry.Point.Array gPoints, double distance) {
//symbols.clear();

		// 군대부호 객체 생성
//		DemoSymbol demoSym = createSymbol("SFGPEV------***");
		DemoSymbol demoSym = createSymbol(code,distance);

		if (demoSym==null) {
			return;
		}

		demoSym.mPoints.copy(gPoints);
		IWarSymbol symbol = demoSym.mSymbol;
		// 수식정보 변경
		IWarSymView symView = symbol.getSymbolView();
		CwsModifier symMod = symView.getModifier();
		if (!code.substring(code.length()-1).equals("*")){
			symMod.setModifierValue("B",code.substring(code.length()-1));
		}
		if(cnt>0){
			symMod.setModifierValue("C", String.valueOf(cnt));
		}


		//symMod.setModifierValue("M", "33");
		//symMod.Set("S", "1");	// 지휘소 표시

		// 도시 속성 설정 인터페이스 얻기
		BasicReturn.TypeObject retObj = new BasicReturn.TypeObject();
		boolean res = symbol.getPropInterface("icops.warsym.inf.IWarSymPrpTacSymbol",  retObj);
		if (res) {
			try {
				IWarSymPrpTacSymbol symPrp = (IWarSymPrpTacSymbol) retObj.value;

				// 피아구분
				symPrp.setAffiliation(code.charAt(1));	// 피아구분 코드 문자 사용

				// 외형 색상
				CwsColorset.Return retColorset = new CwsColorset.Return();
				boolean useCustomColor = false;
				res = symPrp.getColorset(retColorset);
				if (res) {
					CwsColorset colorset = retColorset.value;
					symPrp.setIsCustomFrameColor(useCustomColor);
					GDI.Color frmColor;
					if (useCustomColor) {
						// 사용자지정 색상 사용
						frmColor = new GDI.Color(128, 0, 0, 255);	// 임의의 색상 지정
						colorset.setAffiliationColor(WarSym.ColorInx.Custom, frmColor);
					} else {
						// 표준색상 사용

						int frmColorInx = CwsColorset.getStdAffiliationColorIndex(symbol.getAffiliation());
						frmColor = CwsColorset.getStdFrameColor(frmColorInx);
						colorset.setAffiliationColor(frmColorInx, frmColor);
					}
				}

				// 상태구분 및 운용조건 표시 위치 설정
				//symPrp.setStatus(WarSym.StateCode.Destroyed);
				//symPrp.setOpCondDisplayMode(WarSym.OpCondDispMode.Bottom);
				// 부호크기
				symPrp.setSymbolSize(3.0f);

				// 기능부호 표시 여부
				symPrp.setShowIcon(true);

				// 외형부호 표시 여부
				symPrp.setShowFrame(true);

				// 외형 채움 여부
				symPrp.setFillFrame(true);

				// 외형 물채움 (외형 채움 설정 무시됨)
				//symPrp.SetFillRate(0.5f); // 0.0 ~ 1.0 : 해당 비율로 물채움, 음수 : 물채움 하지 않음
				//symPrp.SetFillRateColor(new GDI.Color(128, 0, 0, 255)); // 반투명 파랑색으로 물채움

				// 민간부호 구분 - 표준색상을 사용해야 적용됨
				/*char aff = symbol.getAffiliation();
				if (aff=='H' || aff=='S' || aff=='J' || aff=='K') {
					// 적군이면 민간구분 표시하지 않음
					symPrp.setClassifyCIV(false);
				} else {
					symPrp.setClassifyCIV(true);
				}*/
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		// 피아구분 및 수식정보 변경 내용 반영
		symView.linkFrameSymbol();
		symView.linkModifierInfo();

		invalidate();
	}

	private DemoSymbol createSymbol(String symCode, double distance) {
		if (mMain==null) {
			return null;
		}
		if (mMain.mAppCxt==null) {
			return null;
		}

		BasicReturn.TypeObject retObj = new BasicReturn.TypeObject();
		boolean res;

		// 군대부호 관리 객체 얻기
		res = mMain.mAppCxt.getStockObject("icops.warsym.inf.IWarSymFactory", retObj);
		if (!res) {
			// 군대부호 관리 객체 얻기 실패
			return null;
		}

		IWarSymFactory symFac;
		try {
			symFac = (IWarSymFactory) retObj.value;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}

		// 군대부호 객체 생성
		IWarSymbol.Return retSym = new IWarSymbol.Return();
		res = symFac.createSymbol(symCode, retSym, true, true);
		if (!res) {
			// 군대부호 객체 생성 실패
			return null;
		}

		// 부호 목록에 추가
		DemoSymbol demoSym = new DemoSymbol();
		demoSym.mSymbol = retSym.value;
		demoSym.hudSym=true;
		demoSym.distance= String.format("%.0f",distance);
		symbols.add(demoSym);

		return demoSym;
	}
}
