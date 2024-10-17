package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;
    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // FIXME
        this.paint = paint; // FIXME
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        paint.setColor(c.getColor());
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        paint.setStyle(Style.FILL);


        f.getShape().accept(this);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for(Shape shape : g.getShapes()) {
            //group all shapes
            shape.accept(this);
        }

        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        //canvas.save();
        //canvas.translate(l.getX(), l.getY());
        //l.getShape().accept(this);
        //canvas.restore();
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        int numPoints = s.getPoints().size();
        if (numPoints > 1) {
            final float[] pts = new float[(numPoints - 1) * 4];
            int i = 0;
            Point prevPoint = s.getPoints().get(0);
            for (int j = 1; j < numPoints; j++) {
                Point currentPoint = s.getPoints().get(j);
                pts[i++] = prevPoint.getX();
                pts[i++] = prevPoint.getY();
                pts[i++] = currentPoint.getX();
                pts[i++] = currentPoint.getY();
                prevPoint = currentPoint;
            }
            canvas.drawLines(pts, paint);
            // Draw the closing line segment to complete the polygon
            Point firstPoint = s.getPoints().get(0);
            canvas.drawLine(prevPoint.getX(), prevPoint.getY(), firstPoint.getX(), firstPoint.getY(), paint);
        }
        return null;

    }
}
