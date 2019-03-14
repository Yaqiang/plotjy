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
package org.meteothink.projection.info;

import java.util.ArrayList;
import java.util.List;
import org.meteothink.math.ArrayUtil;
import org.meteothink.global.PointD;
import org.meteothink.projection.ProjectionNames;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.datum.Datum;
import org.locationtech.proj4j.datum.Ellipsoid;
import org.locationtech.proj4j.proj.Projection;
import org.meteothink.shape.PolygonShape;
import org.meteothink.ndarray.Array;

/**
 *
 * @author Yaqiang Wang
 */
public abstract class ProjectionInfo {
    // <editor-fold desc="Variables">

    protected CoordinateReferenceSystem crs;
    protected PolygonShape boundary;
    protected float cutoff = Float.NaN;    //Latitude cutoff - valiad for some speciafic projections
    // </editor-fold>
    // <editor-fold desc="Constructor">
    /**
     * Create new ProjectionInfo with crs
     * @param crs Coordinate reference system
     * @return Projection info
     */
    public static ProjectionInfo factory(CoordinateReferenceSystem crs) {
        ProjectionInfo projInfo;
        Projection proj = crs.getProjection();
        switch (proj.toString()) {
            case "LongLat":
                projInfo = new LongLat(crs);
                break;
            case "Albers Equal Area":
                projInfo = new Albers(crs);
                break;
            case "Lambert Conformal Conic":
                projInfo = new LambertConformalConic(crs);
                break;
            case "Lambert Equal Area Conic":
                projInfo = new LambertEqualAreaConic(crs);
                break;
            case "Lambert Azimuthal Equal Area":
                projInfo = new LambertAzimuthalEqualArea(crs);
                break;
            case "Stereographic Azimuthal":
                projInfo = new StereographicAzimuthal(crs);
                break;
            case "Mercator":
                projInfo = new Mercator(crs);
                break;
            case "Robinson":
                projInfo = new Robinson(crs);
                break;
            case "Molleweide":
                projInfo = new Molleweide(crs);
                break;
            case "Geostationary Satellite":
                projInfo = new GeostationarySatellite(crs);
                break;
            case "Sinusoidal":
                projInfo = new Sinusoidal(crs);
                break;
            case "Orthographic Azimuthal":
                projInfo = new OrthographicAzimuthal(crs);
                break;
            case "Hammer Eckert":
                projInfo = new Hammer(crs);
                break;
            case "Universal Tranverse Mercator":
            case "Transverse Mercator":
                projInfo = new TransverseMercator(crs);
                break;
            case "Wagner III":
                projInfo = new Wagner3(crs);
                break;
            default:
                projInfo = new Common(crs);
                break;
        }  
        
        projInfo.updateBoundary();
        
        return projInfo;
    }
    
    /**
     * Create new ProjectionInfo with crs
     * @param proj4Str proj4 string
     * @return Projection info
     */
    public static ProjectionInfo factory(String proj4Str) {
        CRSFactory crsFactory = new CRSFactory();
        proj4Str = proj4Str.replace("+", " +");
        proj4Str = proj4Str.trim();
        return factory(crsFactory.createFromParameters("custom", proj4Str));
    }
    
    /**
     * Create new ProjectionInfo with crs
     * @param name ProjectionName
     * @return Projection info
     */
    public static ProjectionInfo factory(ProjectionNames name) {
        CRSFactory crsFactory = new CRSFactory();
        String proj4Str = "+proj=" + name.getProj4Name();        
        return factory(crsFactory.createFromParameters("custom", proj4Str));
    }
    
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get CoordinateReferenceSystem
     *
     * @return CoordinateReferenceSystem
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * Get projection name
     *
     * @return Projection name
     */
    public abstract ProjectionNames getProjectionName();
    
    /**
     * Get if is Lon/Lat projection
     * 
     * @return Boolean
     */
    public boolean isLonLat(){
        return this.getProjectionName() == ProjectionNames.LongLat;
    }
    
    /**
     * Get center longitude
     * @return Center longitude
     */
    public double getCenterLon(){
        return this.crs.getProjection().getProjectionLongitudeDegrees();
    }
    
    /**
     * Get center latitude
     * @return Center latitude
     */
    public double getCenterLat() {
        return this.crs.getProjection().getProjectionLatitudeDegrees();
    }
    
    /**
     * Get map boundary
     * @return Map boundary
     */
    public PolygonShape getBoundary(){
        return this.boundary;
    }
    
    /**
     * Set map boundary
     * @param value Map boundary
     */
    public void setBoundary(PolygonShape value) {
        this.boundary = value;
    }
    
    /**
     * Get latitude cutoff
     * @return Latitude cutoff
     */
    public float getCutoff(){
        return this.cutoff;
    }
    
    /**
     * Set latitude cutoff
     * @param value Latitude cutoff
     */
    public void setCutoff(float value) { }
    
    /**
     * Set latitude cutoff
     * @param value Latitude cutoff
     */
    public void setCutoff_bak(float value) {
        this.cutoff = value;
        this.updateBoundary();
    }

    // </editor-fold>
    // <editor-fold desc="Methods">
    /**
     * Get valid parameters
     * @return Valid parameters
     */
    public List<String> getValidParas() {
        return new ArrayList<>();
    }
    
    void updateBoundary() {}
    
    /**
     * Define a projection boundary using an ellipse.This type of boundary is used by several projections.
     * @param semimajor
     * @param semiminor
     * @param easting
     * @param northing
     * @param n 
     * @return  Ellipse boundary
     */
    protected List<PointD> ellipse_boundary(double semimajor, double semiminor, double easting, double northing, int n) {
        Array t = ArrayUtil.lineSpace(0, -2 * Math.PI, n, true);
        List<PointD> r = new ArrayList<>();
        double x, y;
        for (int i = 0; i < t.getSize(); i++) {
            x = semimajor * Math.cos(t.getDouble(i)) + easting;
            y = semiminor * Math.sin(t.getDouble(i)) + northing;
            r.add(new PointD(x, y));
        }
        
        return r;
    }
    
    /**
     * Get reference cut longitude for projection operation
     * @return Refrence cut longitude
     */
    public double getRefCutLon() {
        double refLon = this.getCoordinateReferenceSystem().getProjection().getProjectionLongitudeDegrees();
        refLon += 180;
        if (refLon > 180) {
            refLon = refLon - 360;
        } else if (refLon < -180) {
            refLon = refLon + 360;
        }
        return refLon;
    }

    /**
     * Get proj4 string
     *
     * @return Proj4 string
     */
    public String toProj4String() {
        return crs.getParameterString();
    }
    
    /**
     * Calculates the inverse of the flattening factor, commonly saved to ESRI
     * projections, or else provided as the "rf" parameter for Proj4 strings.This is simply calculated 
     * as a / (a - b) where a is the semi-major axis and b is the semi-minor axis.
     *
     * @param ellipsoid
     * @return Inverse flatting
     */
    public double getInverseFlattening(Ellipsoid ellipsoid) {
        if (ellipsoid.poleRadius == ellipsoid.equatorRadius) {
            return 0; // prevent divide by zero for spheres.
        }
        return (ellipsoid.equatorRadius) / (ellipsoid.equatorRadius - ellipsoid.poleRadius);
    }
    
    public String toEsriString(Ellipsoid ellipsoid) {
        return "SPHEROID[\"" + ellipsoid.getName() + "\"," + ellipsoid.getEquatorRadius() + "," + getInverseFlattening(ellipsoid) + "]";
    }
    
    public String toEsriString(Datum datum) {
        return "DATUM[\"" + datum.getName() + "\"," + toEsriString(datum.getEllipsoid()) + "]";
    }
    
    /**
     * Get Esri projection string
     * @return Esri projection string
     */
    public String toEsriString(){       
        String result = "";
        String geoName = "GCS_WGS_1984";
        Projection proj = this.crs.getProjection();
        if (proj.getName().equals("longlat")) {
            result = "GEOGCS[\"" + geoName + "\"," + toEsriString(this.crs.getDatum()) + "," + "PRIMEM[\"Greenwich\",0.0]"
                    + "," + "UNIT[\"Degree\",0.0174532925199433]" + "]";
            return result;
        } else {
            String name = "Custom";
            result = "PROJCS[\"" + name + "\"," + "GEOGCS[\"" + geoName + "\"," + toEsriString(this.crs.getDatum()) + ","
                    + "PRIMEM[\"Greenwich\",0.0]" + "," + "UNIT[\"Degree\",0.0174532925199433]" + "]" + ", ";
        }

        result += "PROJECTION[\"" + proj.getName() + "\"],";        
        result += "PARAMETER[\"False_Easting\"," + String.valueOf(proj.getFalseEasting()) + "],";
        result += "PARAMETER[\"False_Northing\"," + String.valueOf(proj.getFalseNorthing()) + "],";
        result += "PARAMETER[\"Central_Meridian\"," + String.valueOf(proj.getProjectionLongitudeDegrees()) + "],";
        result += "PARAMETER[\"Standard_Parallel_1\"," + String.valueOf(proj.getProjectionLatitude1Degrees()) + "],";
        result += "PARAMETER[\"Standard_Parallel_2\"," + String.valueOf(proj.getProjectionLatitude2Degrees()) + "],";
        result += "PARAMETER[\"Scale_Factor\"," + String.valueOf(proj.getScaleFactor()) + "],";
        result += "PARAMETER[\"Latitude_Of_Origin\"," + String.valueOf(proj.getProjectionLatitudeDegrees()) + "],";
        result += "UNIT[\"Meter\",1.0]]";
        return result;
    
    }
    
    /**
     * To string
     * @return String - Proj4 string
     */
    @Override
    public String toString(){
        return crs.getParameterString();
    }
    
    /**
     * Check if two projections are equals
     * @param projA Projection A.
     * @param projB Projection B.
     * @return Boolean
     */
    public boolean equals(Projection projA, Projection projB){
        if (!projA.getName().equals(projB.getName()))
            return false;
        if (projA.getEquatorRadius() != projB.getEquatorRadius())
            return false;        
        if (projA.getEllipsoid().eccentricity != projB.getEllipsoid().eccentricity)
            return false;
        if (!projA.getEllipsoid().isEqual(projA.getEllipsoid(), 0.0000001))
            return false;
        if (projA.getEllipsoid().eccentricity2 != projB.getEllipsoid().eccentricity2)
            return false;
        if (projA.getFalseEasting() != projB.getFalseEasting())
            return false;
        if (projA.getFalseNorthing() != projB.getFalseNorthing())
            return false;
        if (projA.getFromMetres() != projB.getFromMetres())
            return false;
//        if (projA.getHeightOfOrbit() != projB.getHeightOfOrbit())
//            return false;
        if (projA.getProjectionLatitudeDegrees() != projB.getProjectionLatitudeDegrees())
            return false;
        if (projA.getProjectionLatitude1Degrees() != projB.getProjectionLatitude1Degrees())
            return false;
//        if (this.getProjectionLatitude2Degrees() != projB.getProjectionLatitude2Degrees())
//            return false;
        if (projA.getProjectionLongitudeDegrees() != projB.getProjectionLongitudeDegrees())
            return false;
        if (projA.getScaleFactor() != projB.getScaleFactor())
            return false;
        if (projA.getTrueScaleLatitudeDegrees() != projB.getTrueScaleLatitudeDegrees())
            return false;
        
        return true;        
    }
    
    /**
     * Determine if the projection is same with another projection
     * 
     * @param projInfo Projection info
     * @return Boolean
     */
    public boolean equals(ProjectionInfo projInfo){
        if (this.getProjectionName() == ProjectionNames.LongLat && projInfo.getProjectionName() == ProjectionNames.LongLat)
            return true;
        else {
            String proj4Str1 = this.toProj4String();
            String proj4Str2 = projInfo.toProj4String();
            if (proj4Str1.equals(proj4Str2))
                return true;
            else {
                if (!this.crs.getDatum().isEqual(projInfo.crs.getDatum()))
                    return false;
                return equals(this.crs.getProjection(), projInfo.crs.getProjection());
            }
        }            
    }

    /**
     * Calculate scale factor from standard parallel
     *
     * @param stP Standard parallel
     * @return Scale factor
     */
    public static double calScaleFactorFromStandardParallel(double stP) {
        double e = 0.081819191;
        stP = Math.PI * stP / 180;
        double tF;
        if (stP > 0) {
            tF = Math.tan(Math.PI / 4.0 - stP / 2.0) * (Math.pow((1.0 + e * Math.sin(stP)) / (1.0 - e * Math.sin(stP)), e / 2.0));
        } else {
            tF = Math.tan(Math.PI / 4.0 + stP / 2.0) / (Math.pow((1.0 + e * Math.sin(stP)) / (1.0 - e * Math.sin(stP)), e / 2.0));
        }

        double mF = Math.cos(stP) / Math.pow(1.0 - e * e * Math.pow(Math.sin(stP), 2.0), 0.5);
        double k0 = mF * (Math.pow(Math.pow(1.0 + e, 1.0 + e) * Math.pow(1.0 - e, 1.0 - e), 0.5)) / (2.0 * tF);

        return k0;
    }
    // </editor-fold>
}
