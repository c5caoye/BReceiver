package miaoyipu.broadcastreceiverpractice.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class FilterView extends View {
    private Paint myPaint;
    private int trans = 5;

    public FilterView(Context c) {
        super(c);
        myPaint = new Paint();
        myPaint.setTextSize(100);
        myPaint.setARGB(trans, 10, 10, 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(trans, 51, 204, 51);
        canvas.drawText("Testing", 500, 1000, myPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
    }

    public void update() {
        if (trans < 255) {
            trans += 4;
        }
    }
}
