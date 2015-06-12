package com.example.pcuser.myweather.ss.pku.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.example.pcuser.myweather.ss.pku.util.Constants;
import com.example.pcuser.myweather.ss.pku.util.WeatherPic;

import java.lang.Integer;import java.lang.Override;
import java.util.ArrayList;
import java.util.List;

/**
 * ������������ͼ
 *
 *
 */
public class TrendView extends View {

	private Paint mPointPaint;
	private Paint mTextPaint;
	private Paint mLinePaint1;
	private Paint mLinePaint2;
	private Paint mbackLinePaint;

	private int x[] = new int[7];
	private float radius = 8;
	private int h;
	private List<Integer> topTem;
	private List<Integer> lowTem;
	private Bitmap[] topBmps;
	private Bitmap[] lowBmps;
	
	private Context c;

	public TrendView(Context context) {
		super(context);
		this.c = context;
		init();
	}
	public TrendView(Context context, AttributeSet attr) {
		super(context, attr);
		this.c = context;
		init();
	}
	private void init(){
		topBmps = new Bitmap[7];
		lowBmps = new Bitmap[7];
		
		topTem = new ArrayList<Integer>();
		lowTem = new ArrayList<Integer>();
		
		mbackLinePaint = new Paint();
		mbackLinePaint.setColor(Color.WHITE);

		mPointPaint = new Paint();
		mPointPaint.setAntiAlias(true);
		mPointPaint.setColor(Color.WHITE);

		mLinePaint1 = new Paint();
		mLinePaint1.setColor(Color.YELLOW);
		mLinePaint1.setAntiAlias(true);
		mLinePaint1.setStrokeWidth(7);
		mLinePaint1.setStyle(Style.FILL);
		
		mLinePaint2 = new Paint();
		mLinePaint2.setColor(Color.BLUE);
		mLinePaint2.setAntiAlias(true);
		mLinePaint2.setStrokeWidth(7);
		mLinePaint2.setStyle(Style.FILL);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextSize(25F);
		mTextPaint.setTextAlign(Align.CENTER);
	}
	public void setPosition(int a, int b, int c, int d,int e,int f,int g) {
		x[0] = a;
		x[1] = b;
		x[2] = c;
		x[3] = d;
        x[4] = e;
        x[5] = f;
        x[6] = g;
	}

	public void setWidthHeight(int w, int h){
		x[0] = w/8;
		x[1] = w*2/8;
		x[2] = w*3/8;
		x[3] = w*4/8;
        x[4] = w*5/8;
        x[5] = w*6/8;
        x[6] = w*7/8;

		this.h = h;
	}
	public void setTemperature(List<Integer> top, List<Integer> low){
		this.topTem = top;
		this.lowTem = low;
		
		postInvalidate();
	}
	public void setBitmap(List<String> topList, List<String> lowList){
		for (int i=0;i<7;++i){
            //动态获取drawable中的图片
            String imgName1="d"+topList.get(i);
            String imgName2="n"+topList.get(i);
            int imgId1 = getResources().getIdentifier(imgName1, "drawable", "com.example.pcuser.myweather.ss.pku.bean;");
            int imgId2 = getResources().getIdentifier(imgName2, "drawable", "com.example.pcuser.myweather.ss.pku.bean;");
            topBmps[i]= BitmapFactory.decodeResource(c.getResources(), imgId1);
            lowBmps[i]= BitmapFactory.decodeResource(c.getResources(), imgId2);
        }
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float space = 0f;
		float space1 = 0f;
		int temspace = 8;
		
		FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		// �������ָ߶�  
		float fontHeight = fontMetrics.bottom - fontMetrics.top;  
		
		int h = this.h/2;
		int h2 = (int) (h - fontHeight/2);
		int h3 = (int) (h - fontHeight - Constants.picSize);
		 
		int h4 = (int) (h + fontHeight);
		int h5 = (int) (h + fontHeight);
		
		for (int i = 0; i < topTem.size(); i++) {
			space = ( - topTem.get(i)) * temspace;
			if(topTem.get(i) != 100){
				if (i != topTem.size() - 1) {
					space1 = ( - topTem.get(i+1)) * temspace;
					canvas.drawLine(x[i], h + space, x[i+1], h + space1, mLinePaint1);
				}
				canvas.drawText(topTem.get(i) + "°C", x[i], h2 + space, mTextPaint);
				canvas.drawCircle(x[i], h + space, radius, mPointPaint);
				canvas.drawBitmap(topBmps[i], x[i]-topBmps[i].getWidth()/2, h3 + space, null);
			}
		}

		for (int i = 0; i < lowTem.size(); i++) {
			space = (-lowTem.get(i)) * temspace;
			if (i != lowTem.size() - 1) {
				space1 = ( - lowTem.get(i+1)) * temspace;
				canvas.drawLine(x[i], h + space, x[i+1], h + space1, mLinePaint2);
			} 
			canvas.drawText(lowTem.get(i) + "°C", x[i], h4 + space, mTextPaint);
			canvas.drawCircle(x[i], h + space, radius, mPointPaint);
			canvas.drawBitmap(lowBmps[i], x[i]-lowBmps[i].getWidth()/2, h5 + space, null);
		}
	}

}
