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
package org.meteothink.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.meteothink.geoprocess.analysis.InterpolationSetting;
import org.meteothink.common.Extent;
import org.meteothink.global.MIMath;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.meteothink.geoprocess.GeoComputation;
import org.meteothink.common.PointD;
import org.meteothink.layer.VectorLayer;
import org.meteothink.math.ArrayMath;
import org.meteothink.projection.info.ProjectionInfo;
import org.meteothink.projection.Reproject;
import org.meteothink.shape.PolygonShape;
import org.meteothink.shape.ShapeTypes;
import org.meteothink.ndarray.Array;
import org.meteothink.ndarray.DataType;
import org.meteothink.ndarray.DimArray;

/**
 * Template
 *
 * @author Yaqiang Wang
 */
public class StationData {
    // <editor-fold desc="Variables">
    /// <summary>
    /// station data: longitude, latitude, value
    /// </summary>
    private Array data;
    private Array xArray;
    private Array yArray;
    private Array stations;
    /// <summary>
    /// Data extent
    /// </summary>
    public Extent dataExtent;
    /// <summary>
    /// Undef data
    /// </summary>
    public double missingValue;
    /**
     * Projection information
     */
    public ProjectionInfo projInfo = null;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public StationData() {
        dataExtent = new Extent();
        missingValue = -9999;
    }
    
    /**
     * Constructor
     * @param n Station number
     */
    public StationData(int n) {
        this();
        int[] shape = new int[]{n};
        this.data = Array.factory(DataType.DOUBLE, shape);
        this.xArray = Array.factory(DataType.DOUBLE, shape);
        this.yArray = Array.factory(DataType.DOUBLE, shape);
        this.stations = Array.factory(DataType.STRING, shape);
    }

    /**
     * Constructor
     *
     * @param a Array data
     * @param x Array x
     * @param y Array y
     * @param missingv Missing value
     */
    public StationData(Array a, Array x, Array y, Number missingv) {
        this.data = a;
        this.xArray = x;
        this.yArray = y;
        int n = (int) a.getSize();
        this.missingValue = missingv.doubleValue();
        stations = Array.factory(DataType.STRING, a.getShape());
        dataExtent = new Extent();
        for (int i = 0; i < n; i++) {
            stations.setString(i, "s_" + String.valueOf(i + 1));
        }
        this.updateExtent();
    }
    
    /**
     * Constructor
     * @param a DimArray
     */
    public StationData(DimArray a) {
        this();
        this.data = a.getArray();
        this.xArray = a.getDimension(1).getDimArray();
        this.yArray = a.getDimension(0).getDimArray();
        this.stations = Array.factory(DataType.STRING, a.getArray().getShape());
        this.updateExtent();
    }

    /**
     * Constructor
     *
     * @param aStData Station data
     */
    public StationData(StationData aStData) {
        projInfo = aStData.projInfo;
        stations = aStData.stations.copy();
        dataExtent = (Extent)aStData.dataExtent.clone();
        missingValue = aStData.missingValue;
        this.data = aStData.data.copy();
        this.xArray = aStData.xArray.copy();
        this.yArray = aStData.yArray.copy();
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get station number
     *
     * @return Station number
     */
    public int getStNum() {
        if (this.data == null)
            return 0;
        else
            return (int)data.getSize();
    }

    /**
     * Get X coordinates array
     *
     * @return X array
     */
    public Array getX() {
        return this.xArray;
    }
    
    /**
     * Set X coordinates array
     * @param value X array
     */
    public void setX(Array value) {
        this.xArray = value;
    }
    
    /**
     * Get Y coordinates array
     *
     * @return Y array
     */
    public Array getY() {
        return this.yArray;
    }
    
    /**
     * Set Y coordinate array
     * @param value Y array
     */
    public void setY(Array value) {
        this.yArray = value;
    }
    
    /**
     * Get stations array
     * @return Stations array
     */
    public Array getStations() {
        return this.stations;
    }
    
    /**
     * Set stations array
     * @param value Stations array
     */
    public void setStations(Array value) {
        this.stations = value;
    }
    
    /**
     * Get data array
     * @return data array
     */
    public Array getData() {
        return this.data;
    }
    
    /**
     * Get data with x, y and value
     * @return All data
     */
    public double[][] getAllData() {
        int n = this.getStNum();
        double[][] r = new double[n][3];
        for (int i = 0; i < n; i++) {
            r[i][0] = this.xArray.getDouble(i);
            r[i][1] = this.yArray.getDouble(i);
            r[i][2] = this.data.getDouble(i);
        }
        
        return r;
    }
    
    /**
     * Set data array
     * @param value data array
     */
    public void setData(Array value) {
        this.data = value;
    }
        
    /**
     * Set data array
     *
     * @param data Data array
     * @param x X array
     * @param y Y array
     */
    public void setData(Array data, Array x, Array y) {
        this.data = data;
        this.xArray = x;
        this.yArray = y;
        this.updateExtent();
    }
    // </editor-fold>
    // <editor-fold desc="Methods">

    // <editor-fold desc="Operator">
    /**
     * Add operator with another station data
     *
     * @param bStData Station data
     * @return Result station data
     */
    public StationData add(StationData bStData) {
        if (!MIMath.isExtentCross(this.dataExtent, bStData.dataExtent)) {
            return null;
        }

        StationData cStData = new StationData();
        cStData.projInfo = bStData.projInfo;
        String aStid;
        int stIdx;
        double x, y;
        List bStations = java.util.Arrays.asList(bStData.stations.getStorage());
        for (int i = 0; i < stations.getSize(); i++) {
            aStid = stations.getString(i);
            if (aStid.equals("99999")) {
                continue;
            }

            double aValue = this.getData(i);
            if (aValue == missingValue) {
                continue;
            }

            stIdx = bStations.indexOf(aStid);
            if (stIdx >= 0) {
                double bValue = bStData.getData(stIdx);
                if (bValue == bStData.missingValue) {
                    continue;
                }

                x = this.getX(i);
                y = this.getY(i);
                cStData.addData(aStid, x, y, aValue + bValue);
            }
        }

        return cStData;
    }

    /**
     * Add operator with a double value
     *
     * @param value The value
     * @return Result station data
     */
    public StationData add(double value) {
        StationData cStData = new StationData();
        cStData.projInfo = this.projInfo;
        cStData.stations = this.stations.copy();
        cStData.xArray = this.xArray.copy();
        cStData.yArray = this.yArray.copy();
        cStData.dataExtent = (Extent)this.dataExtent.clone();
        cStData.data = ArrayMath.add(this.data, value);        

        return cStData;
    }

    /**
     * Subtract operator with another station data
     *
     * @param bStData Station data
     * @return Result station data
     */
    public StationData sub(StationData bStData) {
        if (!MIMath.isExtentCross(this.dataExtent, bStData.dataExtent)) {
            return null;
        }

        StationData cStData = new StationData();
        String aStid;
        int stIdx;
        double x, y;
        List bStations = java.util.Arrays.asList(bStData.stations.getStorage());
        for (int i = 0; i < stations.getSize(); i++) {
            aStid = stations.getString(i);
            if (aStid.equals("99999")) {
                continue;
            }

            double aValue = this.getData(i);
            if (aValue == missingValue) {
                continue;
            }

            stIdx = bStations.indexOf(aStid);
            if (stIdx >= 0) {
                double bValue = bStData.getData(stIdx);
                if (bValue == bStData.missingValue) {
                    continue;
                }

                x = this.getX(i);
                y = this.getY(i);
                cStData.addData(aStid, x, y, aValue - bValue);
            }
        }

        return cStData;
    }

    /**
     * Subtract operator with a double value
     *
     * @param value The value
     * @return Result station data
     */
    public StationData sub(double value) {
        StationData cStData = new StationData();
        cStData.projInfo = this.projInfo;
        cStData.stations = this.stations.copy();
        cStData.xArray = this.xArray.copy();
        cStData.yArray = this.yArray.copy();
        cStData.dataExtent = (Extent)this.dataExtent.clone();
        cStData.data = ArrayMath.sub(this.data, value);  

        return cStData;
    }

    /**
     * multiply operator with another station data
     *
     * @param bStData Station data
     * @return Result station data
     */
    public StationData mul(StationData bStData) {
        if (!MIMath.isExtentCross(this.dataExtent, bStData.dataExtent)) {
            return null;
        }

        StationData cStData = new StationData();
        String aStid;
        int stIdx;
        double x, y;
        List bStations = java.util.Arrays.asList(bStData.stations.getStorage());
        for (int i = 0; i < stations.getSize(); i++) {
            aStid = stations.getString(i);
            if (aStid.equals("99999")) {
                continue;
            }

            double aValue = this.getData(i);
            if (aValue == missingValue) {
                continue;
            }

            stIdx = bStations.indexOf(aStid);
            if (stIdx >= 0) {
                double bValue = bStData.getData(stIdx);
                if (bValue == bStData.missingValue) {
                    continue;
                }

                x = this.getX(i);
                y = this.getY(i);
                cStData.addData(aStid, x, y, aValue * bValue);
            }
        }

        return cStData;
    }

    /**
     * Multiply operator with a double value
     *
     * @param value The value
     * @return Result station data
     */
    public StationData mul(double value) {
        StationData cStData = new StationData();
        cStData.projInfo = this.projInfo;
        cStData.stations = this.stations.copy();
        cStData.xArray = this.xArray.copy();
        cStData.yArray = this.yArray.copy();
        cStData.dataExtent = (Extent)this.dataExtent.clone();
        cStData.data = ArrayMath.mul(this.data, value);  

        return cStData;
    }

    /**
     * Divide operator with another station data
     *
     * @param bStData Station data
     * @return Result station data
     */
    public StationData div(StationData bStData) {
        if (!MIMath.isExtentCross(this.dataExtent, bStData.dataExtent)) {
            return null;
        }

        StationData cStData = new StationData();
        String aStid;
        int stIdx;
        double x, y;
        List bStations = java.util.Arrays.asList(bStData.stations.getStorage());
        for (int i = 0; i < stations.getSize(); i++) {
            aStid = stations.getString(i);
            if (aStid.equals("99999")) {
                continue;
            }

            double aValue = this.getData(i);
            if (aValue == missingValue) {
                continue;
            }

            stIdx = bStations.indexOf(aStid);
            if (stIdx >= 0) {
                double bValue = bStData.getData(stIdx);
                if (bValue == bStData.missingValue) {
                    continue;
                }

                x = this.getX(i);
                y = this.getY(i);
                cStData.addData(aStid, x, y, aValue / bValue);
            }
        }

        return cStData;
    }

    /**
     * Divide operator with a double value
     *
     * @param value The value
     * @return Result station data
     */
    public StationData div(double value) {
        StationData cStData = new StationData();
        cStData.projInfo = this.projInfo;
        cStData.stations = this.stations.copy();
        cStData.xArray = this.xArray.copy();
        cStData.yArray = this.yArray.copy();
        cStData.dataExtent = (Extent)this.dataExtent.clone();
        cStData.data = ArrayMath.div(this.data, value);  

        return cStData;
    }

    // </editor-fold>
    // <editor-fold desc="Functions">

    /**
     * Calculate abstract station data
     *
     * @return Result station data
     */
    public StationData abs() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.abs(this.data);  

        return stationData;
    }

    /**
     * Calculate anti-cosine station data
     *
     * @return Result station data
     */
    public StationData acos() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.acos(this.data);

        return stationData;
    }

    /**
     * Calculate anti-sine station data
     *
     * @return Result station data
     */
    public StationData asin() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.asin(this.data);

        return stationData;
    }

    /**
     * Calculate anti-tangent station data
     *
     * @return Result station data
     */
    public StationData atan() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.atan(this.data);

        return stationData;
    }

    /**
     * Calculate cosine station data
     *
     * @return Result station data
     */
    public StationData cos() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.cos(this.data);

        return stationData;
    }

    /**
     * Calculate sine station data
     *
     * @return Result station data
     */
    public StationData sin() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.sin(this.data);

        return stationData;
    }

    /**
     * Calculate tangent station data
     *
     * @return Result station data
     */
    public StationData tan() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.tan(this.data);

        return stationData;
    }

    /**
     * Calculate e raised specific power value of station data
     *
     * @return Result station data
     */
    public StationData exp() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.exp(this.data);

        return stationData;
    }

    /**
     * Calculate power station data
     *
     * @param p Power value
     * @return Result station data
     */
    public StationData pow(double p) {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.pow(this.data, p);

        return stationData;
    }

    /**
     * Calculate square root station data
     *
     * @return Result station data
     */
    public StationData sqrt() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.sqrt(this.data);

        return stationData;
    }

    /**
     * Calculate logrithm station data
     *
     * @return Result station data
     */
    public StationData log() {
        StationData stationData = new StationData(this);
        stationData.data = ArrayMath.log(this.data);

        return stationData;
    }

    /**
     * Calculate base 10 logrithm station data
     *
     * @return Result station data
     */
    public StationData log10() {
        StationData stationData = new StationData(this);
       stationData.data = ArrayMath.log10(this.data);

        return stationData;
    }
    // </editor-fold>
    // <editor-fold desc="Data">

    /**
     * Add a data
     *
     * @param id Data identifer
     * @param x X coordinate
     * @param y Y coordinate
     * @param value Value
     */
    public void addData(String id, double x, double y, double value) {
        int n = this.getStNum();
        this.stations = this.stations.reshapeVLen(new int[]{n + 1});
        this.xArray = this.xArray.reshapeVLen(new int[]{n + 1});
        this.yArray = this.yArray.reshapeVLen(new int[]{n + 1});
        this.data = this.data.reshapeVLen(new int[]{n + 1});
        stations.setString(n, id);
        this.xArray.setDouble(n, x);
        this.yArray.setDouble(n, y);
        this.data.setDouble(n, value);

        if (n + 1 == 1) {
            dataExtent.minX = x;
            dataExtent.maxX = x;
            dataExtent.minY = y;
            dataExtent.maxY = y;
        } else {
            if (x < dataExtent.minX) {
                dataExtent.minX = x;
            }
            if (x > dataExtent.maxX) {
                dataExtent.maxX = x;
            }
            if (y < dataExtent.minY) {
                dataExtent.minY = y;
            }
            if (y > dataExtent.maxY) {
                dataExtent.maxY = y;
            }
        }
    }

    /**
     * Get station identifer by index
     *
     * @param idx Index
     * @return Station identifer
     */
    public String getStid(int idx) {
        return stations.getString(idx);
    }

    /**
     * Set station identifer by index
     *
     * @param idx Index
     * @param value Station identifer
     */
    public void setStid(int idx, String value) {
        stations.setString(idx, value);
    }

    /**
     * Get x coordinate by index
     *
     * @param idx Index
     * @return X coordinate
     */
    public double getX(int idx) {
        return this.xArray.getDouble(idx);
    }
    
    /**
     * Set x value by index
     * @param idx index
     * @param value x value
     */
    public void setX(int idx, double value) {
        this.xArray.setDouble(idx, value);
    }

    /**
     * Get y coordinate by index
     *
     * @param idx Index
     * @return Y coordinate
     */
    public double getY(int idx) {
        return this.yArray.getDouble(idx);
    }
    
    /**
     * Set y value by index
     * @param idx index
     * @param value y value
     */
    public void setY(int idx, double value) {
        this.yArray.setDouble(idx, value);
    }
    
    /**
     * Get data value by index
     * @param idx index
     * @return Data value
     */
    public double getData(int idx) {
        return this.data.getDouble(idx);
    }

    /**
     * Set data value by index
     *
     * @param idx Index
     * @param value Data value
     */
    public void setData(int idx, double value) {
        this.data.setDouble(idx, value);
    }
    
    /**
     * Get values
     * @return Values
     */
    public List<Double> getDatas(){
        List<Double> values = new ArrayList<>();
        double v;
        for (int i = 0; i <this.getStNum(); i++){
            v = this.getData(i);
            if (MIMath.doubleEquals(v, this.missingValue)){
                values.add(Double.NaN);
            } else {
                values.add(v);
            }           
        }
        
        return values;
    }
    
    /**
     * Get valid values
     * @return Values
     */
    public List<Double> getValidValues(){
        List<Double> values = new ArrayList<>();
        double v;
        for (int i = 0; i <this.getStNum(); i++){
            v = this.getData(i);
            if (!MIMath.doubleEquals(v, this.missingValue)){                
                values.add(v);
            }           
        }
        
        return values;
    }
    
    /**
     * Index of - by station identifer
     * @param stid Station identifer
     * @return Data index
     */
    public int indexOf(String stid){
        return java.util.Arrays.asList(this.stations.getStorage()).indexOf(stid);
    }

    /**
     * Save station data to a CVS file
     *
     * @param fileName File name
     * @param fieldName Field name
     */
    public void saveAsCSVFile(String fileName, String fieldName) {
        this.saveAsCSVFile(fileName, fieldName, false);
    }

    /**
     * Save station data to a CVS file
     *
     * @param fileName File name
     * @param fieldName Field name
     * @param saveMissingData If save missing data
     */
    public void saveAsCSVFile(String fileName, String fieldName, boolean saveMissingData) {
        BufferedWriter sw = null;
        try {
            sw = new BufferedWriter(new FileWriter(new File(fileName)));
            String aStr = "Stid,Longitude,Latitude," + fieldName;
            sw.write(aStr);
            for (int i = 0; i < this.getStNum(); i++) {
                if (!saveMissingData) {
                    if (MIMath.doubleEquals(this.getData(i), missingValue)) {
                        continue;
                    }
                }

                aStr = stations.getString(i) + "," + String.valueOf(this.getX(i)) + "," + String.valueOf(this.getY(i))
                        + "," + String.valueOf(this.getData(i));
                sw.newLine();
                sw.write(aStr);
            }
            sw.flush();
            sw.close();
        } catch (IOException ex) {
            Logger.getLogger(StationData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Maskout station data
     *
     * @param polygonShape Mask polygon shape
     * @return Result station data
     */
    public StationData maskout(PolygonShape polygonShape) {
        StationData stData = new StationData();
        stData.projInfo = this.projInfo;
        stData.missingValue = this.missingValue;
        for (int i = 0; i < this.getStNum(); i++) {
            if (GeoComputation.pointInPolygon(polygonShape, new PointD(this.getX(i), this.getY(i)))) {
                stData.addData(this.getStid(i), this.getX(i), this.getY(i), this.getData(i));
            }
        }

        return stData;
    }

    /**
     * Maskout station data
     *
     * @param polygonShapes Mask polygon shapes
     * @return Result station data
     */
    public StationData maskout(List<PolygonShape> polygonShapes) {
        StationData stData = new StationData();
        stData.projInfo = this.projInfo;
        stData.missingValue = this.missingValue;
        for (int i = 0; i < this.getStNum(); i++) {
            if (GeoComputation.pointInPolygons(polygonShapes, new PointD(this.getX(i), this.getY(i)))) {
                stData.addData(this.getStid(i), this.getX(i), this.getY(i), this.getData(i));
            }
        }

        return stData;
    }

    /**
     * Maskout station data
     *
     * @param maskLayer Mask layer
     * @return Result station data
     */
    public StationData maskout(VectorLayer maskLayer) {
        if (maskLayer.getShapeType() != ShapeTypes.Polygon) {
            return this;
        }

        List<PolygonShape> polygons = (List<PolygonShape>) maskLayer.getShapes();
        return this.maskout(polygons);
    }

    /**
     * Maskin station data
     *
     * @param polygonShape Mask polygon shape
     * @return Result station data
     */
    public StationData maskin(PolygonShape polygonShape) {
        StationData stData = new StationData();
        stData.projInfo = this.projInfo;
        stData.missingValue = this.missingValue;
        for (int i = 0; i < this.getStNum(); i++) {
            if (!GeoComputation.pointInPolygon(polygonShape, new PointD(this.getX(i), this.getY(i)))) {
                stData.addData(this.getStid(i), this.getX(i), this.getY(i), this.getData(i));
            }
        }

        return stData;
    }

    /**
     * Maskin station data
     *
     * @param polygonShapes Mask polygon shapes
     * @return Result station data
     */
    public StationData maskin(List<PolygonShape> polygonShapes) {
        StationData stData = new StationData();
        stData.projInfo = this.projInfo;
        stData.missingValue = this.missingValue;
        for (int i = 0; i < this.getStNum(); i++) {
            if (!GeoComputation.pointInPolygons(polygonShapes, new PointD(this.getX(i), this.getY(i)))) {
                stData.addData(this.getStid(i), this.getX(i), this.getY(i), this.getData(i));
            }
        }

        return stData;
    }

    /**
     * Maskin station data
     *
     * @param maskLayer Mask layer
     * @return Result station data
     */
    public StationData maskin(VectorLayer maskLayer) {
        if (maskLayer.getShapeType() != ShapeTypes.Polygon) {
            return this;
        }

        List<PolygonShape> polygons = (List<PolygonShape>) maskLayer.getShapes();
        return this.maskin(polygons);
    }

    /**
     * Filter station data
     *
     * @param stations Station identifer list
     * @return Result station data
     */
    public StationData filter(List<String> stations) {
        StationData stData = new StationData();
        stData.projInfo = this.projInfo;
        stData.missingValue = this.missingValue;
        for (int i = 0; i < this.getStNum(); i++) {
            if (stations.contains(this.getStid(i))) {
                stData.addData(this.getStid(i), this.getX(i), this.getY(i), this.getData(i));
            }
        }

        return stData;
    }

    /**
     * Join an other station data
     *
     * @param indata Other station data
     * @return Joined station data
     */
    public StationData join(StationData indata) {
        StationData stData = new StationData(this);
        List bStations = java.util.Arrays.asList(stData.stations.getStorage());
        for (int i = 0; i < indata.getStNum(); i++) {
            if (!bStations.contains(indata.getStid(i))) {
                stData.addData(indata.getStid(i), indata.getX(i), indata.getY(i), indata.getData(i));
            }
        }

        return stData;
    }

    /**
     * Project station data
     *
     * @param fromProj From projection info
     * @param toProj To projection info
     * @return Projected station data
     */
    public StationData project(ProjectionInfo fromProj, ProjectionInfo toProj) {
        int i;
        double x, y;
        StationData nsData = new StationData();
        nsData.missingValue = missingValue;

        double[][] points = new double[1][];
        for (i = 0; i < this.getStNum(); i++) {
            points[0] = new double[]{this.getX(i), this.getY(i)};
            try {
                Reproject.reprojectPoints(points, fromProj, toProj, 0, 1);
                x = points[0][0];
                y = points[0][1];

                nsData.addData(this.getStid(i), x, y, this.getData(i));
            } catch (Exception e) {
                i++;
            }
        }

        nsData.projInfo = toProj;
        return nsData;
    }
    // </editor-fold>    
    // <editor-fold desc="Update">

    /**
     * Get station identifer index
     *
     * @param stid Station identifer
     * @return Index
     */
    public int getStidIndex(String stid) {
        int idx = -1;
        for (int i = 0; i < this.getStNum(); i++) {
            if (this.getStid(i).trim().equals(stid.trim())) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    /**
     * Update data extent
     */
    public void updateExtent() {
        int stNum = this.getStNum();
        double minX, maxX, minY, maxY;
        minX = 0;
        maxX = 0;
        minY = 0;
        maxY = 0;
        double lon, lat;
        for (int i = 0; i < stNum; i++) {
            lon = this.xArray.getDouble(i);
            lat = this.yArray.getDouble(i);
            if (i == 0) {
                minX = lon;
                maxX = minX;
                minY = lat;
                maxY = minY;
            } else {
                if (minX > lon) {
                    minX = lon;
                } else if (maxX < lon) {
                    maxX = lon;
                }
                if (minY > lat) {
                    minY = lat;
                } else if (maxY < lat) {
                    maxY = lat;
                }
            }
        }
        dataExtent.minX = minX;
        dataExtent.maxX = maxX;
        dataExtent.minY = minY;
        dataExtent.maxY = maxY;
    }

    // </editor-fold>
    // <editor-fold desc="Interpolation">
    /**
     * Interpolate to grid data
     *
     * @param interSet Interpolation setting
     * @return Grid data
     */
    public GridData interpolateData(InterpolationSetting interSet) {
        GridData aGridData = null;
        double[] X;
        double[] Y;
        List<double[]> values = createGridXY(interSet.getGridDataSetting());
        X = values.get(0);
        Y = values.get(1);
        double[][] aData = this.getAllData();
        switch (interSet.getInterpolationMethod()) {
            case IDW_Radius:
                this.filterData_Radius(interSet.getRadius(), interSet.getGridDataSetting().dataExtent);
                aGridData = interpolate_Radius(aData,
                        X, Y, interSet.getMinPointNum(), interSet.getRadius(), missingValue);
                break;
            case IDW_Neighbors:
                this.filterData_Radius(interSet.getRadius(), interSet.getGridDataSetting().dataExtent);
                aGridData = interpolate_Neighbor(aData, X, Y,
                        interSet.getMinPointNum(), missingValue);
                break;
            case Cressman:
                this.filterData_Radius(0, interSet.getGridDataSetting().dataExtent);
                aGridData = interpolate_Cressman(aData, X, Y, interSet.getRadiusList(), missingValue);
                break;
            case AssignPointToGrid:
                this.filterData_Radius(0, interSet.getGridDataSetting().dataExtent);
                aGridData = interpolate_Assign(aData, X, Y, missingValue);
                break;
        }

        return aGridData;
    }

    /**
     * Interpolate by IDW radius method
     *
     * @param S Station data array
     * @param X X coordinate array
     * @param Y Y coordinate array
     * @param minPNum Minimum point number
     * @param radius Radius
     * @param missingValue Missing value
     * @return Grid data
     */
    public GridData interpolate_Radius(double[][] S, double[] X, double[] Y,
            int minPNum, double radius, double missingValue) {
        double[][] dataArray;
        dataArray = wcontour.Interpolate.interpolation_IDW_Radius(S, X, Y, minPNum, radius, missingValue);

        GridData gridData = new GridData();
        gridData.data = dataArray;
        gridData.missingValue = missingValue;
        gridData.xArray = X;
        gridData.yArray = Y;

        return gridData;
    }
    
    /**
     * Interpolate by IDW radius method
     *
     * @param X X coordinate array
     * @param Y Y coordinate array
     * @param minPNum Minimum point number
     * @param radius Radius
     * @param missingValue Missing value
     * @return Grid data
     */
    public GridData interpolate_Radius(List<Number> X, List<Number> Y,
            int minPNum, double radius, double missingValue) {
        double[] nX = new double[X.size()];
        double[] nY = new double[Y.size()];
        for (int i = 0; i < X.size(); i++){
            nX[i] = X.get(i).doubleValue();
        }
        for (int i = 0; i < Y.size(); i++){
            nY[i] = Y.get(i).doubleValue();
        }
        
        return this.interpolate_Radius(this.getAllData(), nX, nY, minPNum, radius, missingValue);
    }

    /**
     * Interpolate by IDW_Neighbor method
     *
     * @param S Station data array
     * @param X X coordinate array
     * @param Y Y coordinate array
     * @param pNum Point number
     * @param missingValue Missing value
     * @return Grid data
     */
    public GridData interpolate_Neighbor(double[][] S, double[] X, double[] Y, int pNum, double missingValue) {
        double[][] dataArray = wcontour.Interpolate.interpolation_IDW_Neighbor(S, X, Y, pNum, missingValue);

        GridData gridData = new GridData();
        gridData.data = dataArray;
        gridData.missingValue = missingValue;
        gridData.xArray = X;
        gridData.yArray = Y;

        return gridData;
    }
    
    /**
     * Interpolate by IDW_Neighbor method
     *
     * @param X X coordinate array
     * @param Y Y coordinate array
     * @param pNum Point number
     * @param missingValue Missing value
     * @return Grid data
     */
    public GridData interpolate_Neighbor(List<Number> X, List<Number> Y, int pNum, double missingValue) {
        double[] nX = new double[X.size()];
        double[] nY = new double[Y.size()];
        for (int i = 0; i < X.size(); i++){
            nX[i] = X.get(i).doubleValue();
        }
        for (int i = 0; i < Y.size(); i++){
            nY[i] = Y.get(i).doubleValue();
        }
        
        return this.interpolate_Neighbor(this.getAllData(), nX, nY, pNum, missingValue);
    }

    /**
     * Interpolation by Cressman method
     *
     * @param S Station data array
     * @param X X coordinate array
     * @param Y Y coordinate array
     * @param radList Radius list
     * @param missingValue Missing value
     * @return Grid data
     */
    public GridData interpolate_Cressman(double[][] S, double[] X, double[] Y,
            List<Double> radList, double missingValue) {
        double[][] dataArray = wcontour.Interpolate.cressman(S, X, Y, missingValue, radList);

        GridData gridData = new GridData();
        gridData.data = dataArray;
        gridData.missingValue = missingValue;
        gridData.xArray = X;
        gridData.yArray = Y;

        return gridData;
    }
    
    /**
     * Interpolation by Cressman method
     *
     * @param X X coordinate array
     * @param Y Y coordinate array
     * @param radList Radius list
     * @param missingValue Missing value
     * @return Grid data
     */
    public GridData interpolate_Cressman(List<Number> X, List<Number> Y,
            List<Number> radList, double missingValue) {
        double[] nX = new double[X.size()];
        double[] nY = new double[Y.size()];
        for (int i = 0; i < X.size(); i++){
            nX[i] = X.get(i).doubleValue();
        }
        for (int i = 0; i < Y.size(); i++){
            nY[i] = Y.get(i).doubleValue();
        }

        List<Double> rlist = new ArrayList<>();
        for (Number r : radList){
            rlist.add(r.doubleValue());
        }
        return this.interpolate_Cressman(this.getAllData(), nX, nY, rlist, missingValue);
    }

    /**
     * Interpolation by assign method
     *
     * @param S Station data array
     * @param X X coordinate array
     * @param Y Y coordinate array
     * @param missingValue Missing value
     * @return Grid data
     */
    public GridData interpolate_Assign(double[][] S, double[] X, double[] Y, double missingValue) {
        double[][] dataArray = wcontour.Interpolate.assignPointToGrid(S, X, Y, missingValue);

        GridData gridData = new GridData();
        gridData.data = dataArray;
        gridData.missingValue = missingValue;
        gridData.xArray = X;
        gridData.yArray = Y;

        return gridData;
    }
    
    /**
     * Interpolation by assign method
     *
     * @param X X coordinate array
     * @param Y Y coordinate array
     * @param missingValue Missing value
     * @return Grid data
     */
    public GridData interpolate_Assign(List<Number> X, List<Number> Y, double missingValue) {
        double[] nX = new double[X.size()];
        double[] nY = new double[Y.size()];
        for (int i = 0; i < X.size(); i++){
            nX[i] = X.get(i).doubleValue();
        }
        for (int i = 0; i < Y.size(); i++){
            nY[i] = Y.get(i).doubleValue();
        }

        return this.interpolate_Assign(this.getAllData(), nX, nY, missingValue);
    }

    /**
     * Create grid X/Y coordinate
     *
     * @param gSet
     * @return X/Y coordinate array list
     */
    public List<double[]> createGridXY(GridDataSetting gSet) {
        double xDelt = (gSet.dataExtent.maxX - gSet.dataExtent.minX) / (double) (gSet.xNum - 1);
        double yDelt = (gSet.dataExtent.maxY - gSet.dataExtent.minY) / (double) (gSet.yNum - 1);

        return wcontour.Interpolate.createGridXY_Delt(gSet.dataExtent.minX, gSet.dataExtent.minY,
                gSet.dataExtent.maxX, gSet.dataExtent.maxY, xDelt, yDelt);
    }

    /**
     * Filte station data by radius and extent
     *
     * @param radius Radius
     * @param aExtent Data extent
     */
    public void filterData_Radius(double radius, Extent aExtent) {
        List<double[]> disDataList = new ArrayList<>();
        List<String> nstations = new ArrayList<>();
        int i;
        double x, y, v;
        for (i = 0; i < this.getStNum(); i++) {
            v = this.data.getDouble(i);
            x = this.xArray.getDouble(i);
            y = this.yArray.getDouble(i);
            if (MIMath.doubleEquals(v, missingValue)) {
                continue;
            }
            if (x + radius < aExtent.minX || x - radius > aExtent.maxX
                    || y + radius < aExtent.minY || y - radius > aExtent.maxY) {
                continue;
            } else {
                disDataList.add(new double[]{x, y, v});
                nstations.add(this.stations.getString(i));
            }
        }

        int n = nstations.size();
        int[] shape = new int[]{n};
        Array x1 = Array.factory(this.xArray.getDataType(), shape);
        Array y1 = Array.factory(this.yArray.getDataType(), shape);
        Array data1 = Array.factory(this.data.getDataType(), shape);
        Array station1 = Array.factory(this.stations.getDataType(), shape);
        for (i = 0; i < n; i++) {
            x1.setDouble(i, disDataList.get(i)[0]);
            y1.setDouble(i, disDataList.get(i)[1]);
            data1.setDouble(i, disDataList.get(i)[2]);
            station1.setString(i, nstations.get(i));
        }

        this.setData(data1, x1, y1);
        stations = station1;
    }
    // </editor-fold>
    // <editor-fold desc="Statictics">

    /**
     * Get minimum value
     *
     * @return Minimum value
     */
    public double getMinValue() {
        return (double)this.getMinValueIndex()[0];
    }

    /**
     * Get maximum value
     *
     * @return Maximum value
     */
    public double getMaxValue() {
        return (double)this.getMaxValueIndex()[0];
    }
    
    /**
     * Get minimum value and index
     *
     * @return Minimum value and index
     */
    public Object[] getMinValueIndex() {
        double min = 0;
        int vdNum = 0;
        int idx = 0;
        double v;
        for (int i = 0; i < this.getStNum(); i++) {
            v = this.getData(i);
            if (MIMath.doubleEquals(v, missingValue)) {
                continue;
            }

            if (vdNum == 0) {
                min = v;
                idx = i;
            } else {
                if (min > v) {
                    min = v;
                    idx = i;
                }
            }
            vdNum += 1;
        }

        return new Object[]{min, idx};
    }

    /**
     * Get maximum value and index
     *
     * @return Maximum value and index
     */
    public Object[] getMaxValueIndex() {
        double max = 0;
        int vdNum = 0;
        int idx = 0;
        double v;
        for (int i = 0; i < this.getStNum(); i++) {
            v = this.getData(i);
            if (MIMath.doubleEquals(v, missingValue)) {
                continue;
            }

            if (vdNum == 0) {
                max = v;
                idx = i;
            } else {
                if (max < v) {
                    max = v;
                    idx = i;
                }
            }
            vdNum += 1;
        }

        return new Object[]{max, idx};
    }

    /**
     * Get maximum and minimum values
     *
     * @param maxmin Maximum and minimum value array
     * @return Has missing value or not
     */
    public boolean getMaxMinValue(double[] maxmin) {
        double max = 0;
        double min = 0;
        int vdNum = 0;
        double v;
        boolean hasMissingValue = false;
        for (int i = 0; i < this.getStNum(); i++) {
            v = this.getData(i);
            if (MIMath.doubleEquals(v, missingValue)) {
                hasMissingValue = true;
                continue;
            }

            if (vdNum == 0) {
                max = v;
                min = max;
            } else {
                if (max < v) {
                    max = v;
                }
                if (min > v) {
                    min = v;
                }
            }
            vdNum += 1;
        }

        maxmin[0] = max;
        maxmin[1] = min;
        return hasMissingValue;
    }

    /**
     * Calculate average value
     *
     * @return Average value
     */
    public double average() {
        double ave = 0;
        int vdNum = 0;
        double v;
        for (int i = 0; i < this.getStNum(); i++) {
            v = this.getData(i);
            if (MIMath.doubleEquals(v, missingValue)) {
                continue;
            }

            ave += v;
            vdNum += 1;
        }

        ave = ave / vdNum;

        return ave;
    }
    
    /**
     * Calculate summary value
     *
     * @return Summary value
     */
    public double sum() {
        double sum = 0;
        double v;
        for (int i = 0; i < this.getStNum(); i++) {
            v = this.getData(i);
            if (MIMath.doubleEquals(v, missingValue)) {
                continue;
            }

            sum += v;
        }

        return sum;
    }
    // </editor-fold>
    // </editor-fold>
}
