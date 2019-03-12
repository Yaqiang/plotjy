/* Copyright 2012 Yaqiang Wang,
 * yaqiang.wang@gmail.com
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 */
package org.meteothink.geoprocess;

import org.meteothink.global.Extent;
import org.meteothink.global.MIMath;
import org.meteothink.global.PointD;
import org.meteothink.shape.Polygon;
import org.meteothink.shape.PolygonShape;
import org.meteothink.shape.PolylineShape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.meteothink.shape.CircleShape;

/**
 * GeoComputation class
 *
 * @author Yaqiang Wang
 */
public class GeoComputation {

    private static final double EARTH_RADIUS = 6378.137;

    // <editor-fold desc="General">        
    /**
     * Determine if a point array is clockwise
     *
     * @param pointList point list
     * @return boolean
     */
    public static boolean isClockwise(List<? extends PointD> pointList) {
        int i;
        PointD aPoint;
        double yMax = 0;
        int yMaxIdx = 0;
        for (i = 0; i < pointList.size() - 1; i++) {
            aPoint = pointList.get(i);
            if (i == 0) {
                yMax = aPoint.Y;
                yMaxIdx = 0;
            } else {
                if (yMax < aPoint.Y) {
                    yMax = aPoint.Y;
                    yMaxIdx = i;
                }
            }
        }
        PointD p1, p2, p3;
        int p1Idx, p2Idx, p3Idx;
        p1Idx = yMaxIdx - 1;
        p2Idx = yMaxIdx;
        p3Idx = yMaxIdx + 1;
        if (yMaxIdx == 0) {
            p1Idx = pointList.size() - 2;
        }

        p1 = pointList.get(p1Idx);
        p2 = pointList.get(p2Idx);
        p3 = pointList.get(p3Idx);
        return (p3.X - p1.X) * (p2.Y - p1.Y) - (p2.X - p1.X) * (p3.Y - p1.Y) > 0;

    }

    /**
     * Determine if a point array is clockwise
     *
     * @param points point array
     * @return boolean
     */
    public static boolean isClockwise(PointD[] points) {
        List<PointD> pointList = Arrays.asList(points);
        return isClockwise(pointList);
    }

    /**
     * Determine if a point is in a polygon
     *
     * @param poly Polygon border points
     * @param aPoint The point
     * @return If the point is in the polygon
     */
    public static boolean pointInPolygon(List<? extends PointD> poly, PointD aPoint) {
        double xNew, yNew, xOld, yOld;
        double x1, y1, x2, y2;
        int i;
        boolean inside = false;
        int nPoints = poly.size();

        if (nPoints < 3) {
            return false;
        }

        xOld = (poly.get(nPoints - 1)).X;
        yOld = (poly.get(nPoints - 1)).Y;
        for (i = 0; i < nPoints; i++) {
            xNew = (poly.get(i)).X;
            yNew = (poly.get(i)).Y;
            if (xNew > xOld) {
                x1 = xOld;
                x2 = xNew;
                y1 = yOld;
                y2 = yNew;
            } else {
                x1 = xNew;
                x2 = xOld;
                y1 = yNew;
                y2 = yOld;
            }

            //---- edge "open" at left end
            if ((xNew < aPoint.X) == (aPoint.X <= xOld)
                    && (aPoint.Y - y1) * (x2 - x1) < (y2 - y1) * (aPoint.X - x1)) {
                inside = !inside;
            }

            xOld = xNew;
            yOld = yNew;
        }

        return inside;
    }

    /**
     * Determine if a point is in a polygon
     *
     * @param aPolygon The polygon
     * @param aPoint The point
     * @return Boolean
     */
    public static boolean pointInPolygon(PolygonShape aPolygon, PointD aPoint) {
        if (!MIMath.pointInExtent(aPoint, aPolygon.getExtent())) {
            return false;
        }

        if (aPolygon instanceof CircleShape) {
            return ((CircleShape) aPolygon).contains(aPoint);
        }

        boolean isIn = false;
        for (int i = 0; i < aPolygon.getPolygons().size(); i++) {
            Polygon aPRing = aPolygon.getPolygons().get(i);
            isIn = pointInPolygon(aPRing.getOutLine(), aPoint);
            if (isIn) {
                if (aPRing.hasHole()) {
                    for (List<? extends PointD> aLine : aPRing.getHoleLines()) {
                        if (pointInPolygon(aLine, aPoint)) {
                            isIn = false;
                            break;
                        }
                    }
                }
            }

            if (isIn) {
                return isIn;
            }
        }

        return isIn;
    }

    /**
     * Determine if a point is in a polygon
     *
     * @param aPolygon The polygon
     * @param x X
     * @param y Y
     * @return Boolean
     */
    public static boolean pointInPolygon(PolygonShape aPolygon, double x, double y) {
        return pointInPolygon(aPolygon, new PointD(x, y));
    }

    /**
     * Determine if a point is in a polygon
     *
     * @param aPolygon The polygon
     * @param aPoint The point
     * @return Boolean
     */
    public static boolean pointInPolygon(Polygon aPolygon, PointD aPoint) {
        if (!MIMath.pointInExtent(aPoint, aPolygon.getExtent())) {
            return false;
        }

        if (aPolygon.hasHole()) {
            boolean isIn = pointInPolygon(aPolygon.getOutLine(), aPoint);
            if (isIn) {
                for (List<? extends PointD> aLine : aPolygon.getHoleLines()) {
                    if (pointInPolygon(aLine, aPoint)) {
                        isIn = false;
                        break;
                    }
                }
            }

            return isIn;
        } else {
            return pointInPolygon(aPolygon.getOutLine(), aPoint);
        }
    }

    /**
     * Determine if a point located in polygons
     *
     * @param polygons The polygons
     * @param aPoint The point
     * @return Boolean
     */
    public static boolean pointInPolygons(List<PolygonShape> polygons, PointD aPoint) {
        boolean isIn = false;
        Extent ext = MIMath.getExtent(polygons);
        if (MIMath.pointInExtent(aPoint, ext)) {
            for (PolygonShape aPGS : polygons) {
                if (pointInPolygon(aPGS, aPoint)) {
                    isIn = true;
                    break;
                }
            }
        }

        return isIn;
    }

    /**
     * Calculate the distance between point and a line segment
     *
     * @param point The point
     * @param pt1 End point of the line segment
     * @param pt2 End point of the line segment
     * @return Distance
     */
    public static double dis_PointToLine(PointD point, PointD pt1, PointD pt2) {
        double dis;
        if (MIMath.doubleEquals(pt2.X, pt1.X)) {
            dis = Math.abs(point.X - pt1.X);
        } else if (MIMath.doubleEquals(pt2.Y, pt1.Y)) {
            dis = Math.abs(point.Y - pt1.Y);
        } else {
            double k = (pt2.Y - pt1.Y) / (pt2.X - pt1.X);
            double x = (k * k * pt1.X + k * (point.Y - pt1.Y) + point.X) / (k * k + 1);
            double y = k * (x - pt1.X) + pt1.Y;
            //double dis = Math.sqrt((point.Y - y) * (point.Y - y) + (point.X - x) * (point.X - x));
            dis = distance(point, new PointD(x, y));
        }
        return dis;
    }

    /**
     * Get distance between two points
     *
     * @param pt1 Point one
     * @param pt2 Point two
     * @return Distance
     */
    public static double distance(PointD pt1, PointD pt2) {
        return Math.sqrt((pt2.Y - pt1.Y) * (pt2.Y - pt1.Y) + (pt2.X - pt1.X) * (pt2.X - pt1.X));
    }

    /**
     * Select polyline shape by a point
     *
     * @param sp The point
     * @param aPLS The polyline shape
     * @param buffer Buffer
     * @return Is the polyline shape selected
     */
    public static Object selectPolylineShape(PointD sp, PolylineShape aPLS, double buffer) {
        Extent aExtent = new Extent();
        aExtent.minX = sp.X - buffer;
        aExtent.maxX = sp.X + buffer;
        aExtent.minY = sp.Y - buffer;
        aExtent.maxY = sp.Y + buffer;
        double dis;
        if (MIMath.isExtentCross(aExtent, aPLS.getExtent())) {
            for (int j = 0; j < aPLS.getPointNum(); j++) {
                PointD aPoint = aPLS.getPoints().get(j);
                if (MIMath.pointInExtent(aPoint, aExtent)) {
                    return GeoComputation.distance(sp, aPoint);
                }
                if (j < aPLS.getPointNum() - 1) {
                    PointD bPoint = aPLS.getPoints().get(j + 1);
                    if (Math.abs(sp.Y - aPoint.Y) <= Math.abs(bPoint.Y - aPoint.Y)
                            || Math.abs(sp.X - aPoint.X) <= Math.abs(bPoint.X - aPoint.X)) {
                        dis = GeoComputation.dis_PointToLine(sp, aPoint, bPoint);
                        if (dis < aExtent.getWidth()) {
                            return dis;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Select polyline shape by a point
     *
     * @param sp The point
     * @param points The point list
     * @param buffer Buffer
     * @return Is the polyline shape selected
     */
    public static Object selectPolyline(PointD sp, List<PointD> points, double buffer) {
        Extent aExtent = new Extent();
        aExtent.minX = sp.X - buffer;
        aExtent.maxX = sp.X + buffer;
        aExtent.minY = sp.Y - buffer;
        aExtent.maxY = sp.Y + buffer;
        Extent bExtent = MIMath.getPointsExtent(points);
        double dis;
        if (MIMath.isExtentCross(aExtent, bExtent)) {
            for (int j = 0; j < points.size(); j++) {
                PointD aPoint = points.get(j);
                if (MIMath.pointInExtent(aPoint, aExtent)) {
                    return GeoComputation.distance(sp, aPoint);
                }
                if (j < points.size() - 1) {
                    PointD bPoint = points.get(j + 1);
                    if (Math.abs(sp.Y - aPoint.Y) <= Math.abs(bPoint.Y - aPoint.Y)
                            || Math.abs(sp.X - aPoint.X) <= Math.abs(bPoint.X - aPoint.X)) {
                        dis = GeoComputation.dis_PointToLine(sp, aPoint, bPoint);
                        if (dis < aExtent.getWidth()) {
                            return new Object[]{j + 1, dis};
                        }
                    }
                }
            }
        }

        return null;
    }

    // </editor-fold>
    // <editor-fold desc="Earth">
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * Get polygon area
     *
     * @param x X coordinates
     * @param y Y coordinates
     * @param isLonLat If is on earth surface (lon/lat)
     * @return Area
     */
    public static double getArea(List<Number> x, List<Number> y, boolean isLonLat) {
        List<PointD> points = new ArrayList<>();
        for (int i = 0; i < x.size(); i++) {
            points.add(new PointD(x.get(i).doubleValue(), y.get(i).doubleValue()));
        }

        return getArea(points, isLonLat);
    }

    /**
     * Get polygon area on earth surface
     *
     * @param points point list
     * @param isLonLat if is lon/lat
     * @return area
     */
    public static double getArea(List<? extends PointD> points, boolean isLonLat) {

        int Count = points.size();
        if (Count > 2) {
            double mtotalArea = 0;

            if (isLonLat) {
                return sphericalPolygonArea(points);
            } else {
                int i, j;
                double p1x, p1y;
                double p2x, p2y;
                for (i = Count - 1, j = 0; j < Count; i = j, j++) {

                    p1x = points.get(i).X;
                    p1y = points.get(i).Y;

                    p2x = points.get(j).X;
                    p2y = points.get(j).Y;

                    mtotalArea += p1x * p2y - p2x * p1y;
                }
                mtotalArea /= 2.0;

                if (mtotalArea < 0) {
                    mtotalArea = -mtotalArea;
                }

                return mtotalArea;
            }
        }
        return 0;
    }

    /**
     * Get polygon area on earth surface
     *
     * @param points point list
     * @return area
     */
    public static double getArea(List<? extends PointD> points) {
        return getArea(points, false);
    }

    /**
     * Get polygon area on earth surface
     *
     * @param points point list
     * @return area
     */
    public static double calArea(List<PointD> points) {
        if (points.size() < 3) {
            return 0.0;
        }

        double sum = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            double bx = points.get(i).X;
            double by = points.get(i).Y;
            double cx = points.get(i + 1).X;
            double cy = points.get(i + 1).Y;
            sum += (bx + cx) * (cy - by);
        }
        return -sum / 2.0;
    }

    /**
     * Compute the Area of a Spherical Polygon
     *
     * @param points lon/lat point list
     * @return area
     */
    public static double sphericalPolygonArea(List<? extends PointD> points) {
        return sphericalPolygonArea(points, EARTH_RADIUS * 1000);
    }

    /**
     * Compute the Area of a Spherical Polygon
     *
     * @param points lon/lat point list
     * @param r spherical radius
     * @return area
     */
    public static double sphericalPolygonArea(List<? extends PointD> points, double r) {
        double[] lat = new double[points.size()];
        double[] lon = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            lon[i] = rad(points.get(i).X);
            lat[i] = rad(points.get(i).Y);
        }

        return sphericalPolygonArea(lat, lon, r);
    }

    /**
     * Haversine function : hav(x) = (1-cos(x))/2
     *
     * @param x
     * @return Returns the value of Haversine function
     */
    public static double haversine(double x) {
        return (1.0 - Math.cos(x)) / 2.0;
    }

    /**
     * Compute the Area of a Spherical Polygon
     *
     * @param lat the latitudes of all vertices(in radian)
     * @param lon the longitudes of all vertices(in radian)
     * @param r spherical radius
     * @return Returns the area of a spherical polygon
     */
    public static double sphericalPolygonArea(double[] lat, double[] lon, double r) {
        double lam1, lam2 = 0, beta1, beta2 = 0, cosB1, cosB2 = 0;
        double hav;
        double sum = 0;

        for (int j = 0; j < lat.length; j++) {
            //int k = j + 1;
            if (j == 0) {
                lam1 = lon[j];
                beta1 = lat[j];
                lam2 = lon[j + 1];
                beta2 = lat[j + 1];
                cosB1 = Math.cos(beta1);
                cosB2 = Math.cos(beta2);
            } else {
                int k = (j + 1) % lat.length;
                lam1 = lam2;
                beta1 = beta2;
                lam2 = lon[k];
                beta2 = lat[k];
                cosB1 = cosB2;
                cosB2 = Math.cos(beta2);
            }
            if (lam1 != lam2) {
                hav = haversine(beta2 - beta1)
                        + cosB1 * cosB2 * haversine(lam2 - lam1);
                double a = 2 * Math.asin(Math.sqrt(hav));
                double b = Math.PI / 2 - beta2;
                double c = Math.PI / 2 - beta1;
                double s = 0.5 * (a + b + c);
                double t = Math.tan(s / 2) * Math.tan((s - a) / 2)
                        * Math.tan((s - b) / 2) * Math.tan((s - c) / 2);

                double excess = Math.abs(4 * Math.atan(Math.sqrt(
                        Math.abs(t))));

                if (lam2 < lam1) {
                    excess = -excess;
                }

                sum += excess;
            }
        }
        return Math.abs(sum) * r * r;
    }

    /**
     * Get distance
     *
     * @param points Point list
     * @param isLonLat If is lon/lat
     * @return Distance
     */
    public static double getDistance(List<? extends PointD> points, boolean isLonLat) {
        double tdis = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            double ax = points.get(i).X;
            double ay = points.get(i).Y;
            double bx = points.get(i + 1).X;
            double by = points.get(i + 1).Y;
            double dx = Math.abs(bx - ax);
            double dy = Math.abs(by - ay);
            double dist;
            if (isLonLat) {
                double y = (by + ay) / 2;
                double factor = Math.cos(y * Math.PI / 180);
                dx *= factor;
                dist = Math.sqrt(dx * dx + dy * dy);
                dist = dist * 111319.5;
            } else {
                dist = Math.sqrt(dx * dx + dy * dy);
            }

            tdis += dist;
        }

        return tdis;
    }

    /**
     * Get distance
     *
     * @param xx X coordinates
     * @param yy Y coordinates
     * @param isLonLat If is lon/lat
     * @return Distance
     */
    public static double getDistance(List<Number> xx, List<Number> yy, boolean isLonLat) {
        double tdis = 0.0;
        for (int i = 0; i < xx.size() - 1; i++) {
            double ax = xx.get(i).doubleValue();
            double ay = yy.get(i).doubleValue();
            double bx = xx.get(i + 1).doubleValue();
            double by = yy.get(i + 1).doubleValue();
            double dx = Math.abs(bx - ax);
            double dy = Math.abs(by - ay);
            double dist;
            if (isLonLat) {
                double y = (by + ay) / 2;
                double factor = Math.cos(y * Math.PI / 180);
                dx *= factor;
                dist = Math.sqrt(dx * dx + dy * dy);
                dist = dist * 111319.5;
            } else {
                dist = Math.sqrt(dx * dx + dy * dy);
            }

            tdis += dist;
        }

        return tdis;
    }
    // </editor-fold>

}
