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
package org.meteothink.data.meteodata;

import org.meteothink.ndarray.DimensionType;
import org.meteothink.ndarray.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.meteothink.global.util.DateUtil;
import org.meteothink.ndarray.DataType;

/**
 *
 * @author Yaqiang Wang
 */
public class Variable {
    // <editor-fold desc="Variables">
    /// <summary>
    /// Parameter number
    /// </summary>

    public int Number;
    private String shortName;
    private DataType dataType;
    protected int[] shape = new int[0];
    protected List<Dimension> dimensions = new ArrayList<>();
    protected List<Attribute> attributes = new ArrayList<>();
    private int _levelType;
    private List<Double> _levels;
    private String units;
    private String _description;
    //private List<Dimension> _dimensions = new ArrayList<>();
    private String _hdfPath;
    private boolean _isStation = false;
    private boolean _isSwath = false;
    //private NetCDF4.NcType _ncType;
    //private List<Attribute> _attributes = new ArrayList<>();
    //private int _attNumber;
    private int _varId;
    private boolean dimVar = false;
    private List<Integer> _levelIdxs = new ArrayList<>();
    private List<Integer> _varInLevelIdxs = new ArrayList<>();
    private double fill_value = -9999.0;
    private double scale_factor = 1;
    private double add_offset = 0;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public Variable() {
        this.shortName = "null";
        this.dataType = DataType.FLOAT;
        _levels = new ArrayList<>();
        units = "null";
        _description = "null";
    }

    /**
     * Constructor
     *
     * @param aNum Parameter number
     * @param aName The name
     * @param aDesc The description
     * @param aUnit The units
     */
    public Variable(int aNum, String aName, String aDesc, String aUnit) {
        Number = aNum;
        this.shortName = aName;
        this.units = aUnit;
        _description = aDesc;
        _levels = new ArrayList<>();
    }

    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    /**
     * Get name
     *
     * @return Name
     */
    public String getName() {
        return this.getShortName();
    }

    /**
     * Get short name
     *
     * @return Short name
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * Set short name
     *
     * @param value Short name
     */
    public void setShortName(String value) {
        this.shortName = value;
    }

    /**
     * Set name
     *
     * @param value Name
     */
    public void setName(String value) {
        this.setShortName(value);
    }

    /**
     * Get data type
     *
     * @return Data type
     */
    public DataType getDataType() {
        return this.dataType;
    }

    /**
     * Set data type
     *
     * @param value Data type
     */
    public void setDataType(DataType value) {
        this.dataType = value;
    }

    /**
     * Get dimensions
     *
     * @return Dimensions
     */
    public List<Dimension> getDimensions() {
        return this.dimensions;
    }

    /**
     * Get dimension
     *
     * @param index Dimension index
     * @return Dimension
     */
    public Dimension getDimension(int index) {
        return this.dimensions.get(index);
    }

    /**
     * Get attributes
     *
     * @return Attributes
     */
    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    /**
     * Get attribute
     *
     * @param index Attribute index
     * @return Attribute
     */
    public Attribute getAttribute(int index) {
        return this.attributes.get(index);
    }

    /**
     * Get level type
     *
     * @return Level type
     */
    public int getLevelType() {
        return _levelType;
    }

    /**
     * Set level type
     *
     * @param value Level type
     */
    public void setLevelType(int value) {
        _levelType = value;
    }

    /**
     * Get levels
     *
     * @return Levels
     */
    public List<Double> getLevels() {
        //return _levels;
        Dimension zDim = this.getZDimension();
        if (zDim == null) {
            return _levels;
        } else {
            return zDim.getDimValue();
        }
    }

    /**
     * Set levels
     *
     * @param value Levels
     */
    public void setLevels(List<Double> value) {
        _levels = value;
        this.updateZDimension();
    }

    /**
     * Set units
     *
     * @return Units
     */
    public String getUnits() {
        return units;
    }

    /**
     * Set units
     *
     * @param value Units
     */
    public void setUnits(String value) {
        units = value;
    }

    /**
     * Get description
     *
     * @return Description
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Set description
     *
     * @param value Description
     */
    public void setDescription(String value) {
        _description = value;
    }

    /**
     * Get dimension number
     *
     * @return Dimension number
     */
    public int getDimNumber() {
        return this.getDimensions().size();
    }

    /**
     * Get level number
     *
     * @return Level number
     */
    public int getLevelNum() {
        //return _levels.size();
        Dimension zDim = this.getZDimension();
        if (zDim == null) {
            return 0;
        } else {
            return zDim.getLength();
        }
    }

    /**
     * Get HDF path
     *
     * @return HDF path
     */
    public String getHDFPath() {
        return _hdfPath;
    }

    /**
     * Set HDF path
     *
     * @param value HDF path
     */
    public void setHDFPath(String value) {
        _hdfPath = value;
    }

    /**
     * Get X dimension
     *
     * @return X dimension
     */
    public Dimension getXDimension() {
        return getDimension(DimensionType.X);
    }

    /**
     * Set X dimension
     *
     * @param value X dimension
     */
    public void setXDimension(Dimension value) {
        setDimension(value, DimensionType.X);
    }

    /**
     * Get Y dimension
     *
     * @return Y dimension
     */
    public Dimension getYDimension() {
        return getDimension(DimensionType.Y);
    }

    /**
     * Set Y dimension
     *
     * @param value Y dimension
     */
    public void setYDimension(Dimension value) {
        setDimension(value, DimensionType.Y);
    }

    /**
     * Get Z dimension
     *
     * @return Z dimension
     */
    public Dimension getZDimension() {
        return getDimension(DimensionType.Z);
    }

    /**
     * Set Z dimension
     *
     * @param value Z dimension
     */
    public void setZDimension(Dimension value) {
        setDimension(value, DimensionType.Z);
    }

    /**
     * Get T dimension
     *
     * @return T dimension
     */
    public Dimension getTDimension() {
        return getDimension(DimensionType.T);
    }

    /**
     * Set T dimension
     *
     * @param value T dimension
     */
    public void setTDimension(Dimension value) {
        setDimension(value, DimensionType.T);
    }

    /**
     * Get dimension identifers
     *
     * @return Dimension identifers
     */
    public int[] getDimIds() {
        int[] dimids = new int[this.getDimensions().size()];
        for (int i = 0; i < this.getDimensions().size(); i++) {
            dimids[i] = ((Dimension) this.getDimension(i)).getDimId();
        }

        return dimids;
    }

    /**
     * Get if the variable is station data set
     *
     * @return Boolean
     */
    public boolean isStation() {
        return _isStation;
    }

    /**
     * Set if the variable is station data set
     *
     * @param value Boolean
     */
    public void setStation(boolean value) {
        _isStation = value;
    }

    /**
     * Get if the variable is swath data set
     *
     * @return Boolean
     */
    public boolean isSwath() {
        return _isSwath;
    }

    /**
     * Set if the variable is swath data set
     *
     * @param value Boolean
     */
    public void setSwath(boolean value) {
        _isSwath = value;
    }

    /**
     * Get if the variable is plottable (has both X and Y dimension)
     *
     * @return Boolean
     */
    public boolean isPlottable() {
        if (_isStation) {
            return true;
        }
        if (this.getXDimension() == null) {
            return false;
        }
        if (this.getYDimension() == null) {
            return false;
        }

        return true;
    }

    /**
     * Get attribute number
     *
     * @return Attribute number
     */
    public int getAttNumber() {
        return this.getAttributes().size();
    }

    /**
     * Get variable identifer
     *
     * @return Variable identifer
     */
    public int getVarId() {
        return _varId;
    }

    /**
     * Set variable identifer
     *
     * @param value Variable identifer
     */
    public void setVarId(int value) {
        _varId = value;
    }

    /**
     * Get if the variable is dimension variable
     *
     * @return Boolean
     */
    public boolean isDimVar() {
        return dimVar;
    }

    /**
     * Set if the variable is dimension variable
     *
     * @param value Boolean
     */
    public void setDimVar(boolean value) {
        dimVar = value;
    }

    /**
     * Get level index list - for ARL data
     *
     * @return Level index list
     */
    public List<Integer> getLevelIdxs() {
        return _levelIdxs;
    }

    /**
     * Set level index list
     *
     * @param value Level index list
     */
    public void setLevelIdxs(List<Integer> value) {
        _levelIdxs = value;
    }

    /**
     * Get variable index in level index list - for ARL data
     *
     * @return Variable index
     */
    public List<Integer> getVarInLevelIdxs() {
        return _varInLevelIdxs;
    }

    /**
     * Set variable index in level index list - for ARL data
     *
     * @param value Variable index
     */
    public void setVarInLevelIdxs(List<Integer> value) {
        _varInLevelIdxs = value;
    }

    /**
     * Get fill value
     *
     * @return Fill value
     */
    public double getFillValue() {
        return this.fill_value;
    }

    /**
     * Set fill value
     *
     * @param value Fill value
     */
    public void setFillValue(double value) {
        this.fill_value = value;
    }

    /**
     * Get scale factor
     *
     * @return Scale factor
     */
    public double getScaleFactor() {
        return this.scale_factor;
    }

    /**
     * Set scale factor
     *
     * @param value Scale factor
     */
    public void setScaleFactor(double value) {
        this.scale_factor = value;
    }

    /**
     * Get add offset
     *
     * @return Add offset
     */
    public double getAddOffset() {
        return this.add_offset;
    }

    /**
     * Set add offset
     *
     * @param value Add offset
     */
    public void setAddOffset(double value) {
        this.add_offset = value;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">

    /**
     * Clone
     *
     * @return Parameter object
     */
    @Override
    public Object clone() {
        Variable aPar = new Variable();
        aPar.Number = Number;
        aPar.setShortName(this.getShortName());
        aPar.setUnits(units);
        aPar.setDescription(_description);
        aPar.setLevelType(_levelType);

        //aPar.getAttributes().addAll(_attributes);
        aPar.getDimensions().addAll(this.getDimensions());
        aPar.setDimVar(dimVar);
        aPar.getLevels().addAll(_levels);
        //aPar.NCType = _ncType;
        aPar.setVarId(_varId);

        return aPar;
    }

    /**
     * Determine if two parameter are equal
     *
     * @param aVar The variable
     * @return If equal
     */
    public boolean equals(Variable aVar) {
        if (!this.getShortName().equals(aVar.getShortName())) {
            return false;
        }
        if (Number != aVar.Number) {
            return false;
        }
        if (!_description.equals(aVar.getDescription())) {
            return false;
        }
        if (!units.equals(aVar.getUnits())) {
            return false;
        }

        return true;
    }

    /**
     * Determine if two parameter are totally equal
     *
     * @param aVar The variable
     * @return If equal
     */
    public boolean tEquals(Variable aVar) {
        if (!this.getShortName().equals(aVar.getShortName())) {
            return false;
        }
        if (Number != aVar.Number) {
            return false;
        }
        if (!_description.equals(aVar.getDescription())) {
            return false;
        }
        if (!units.equals(aVar.getUnits())) {
            return false;
        }
        if (_levelType != aVar.getLevelType()) {
            return false;
        }

        return true;
    }

    /**
     * Add a level
     *
     * @param levelValue Level value
     */
    public void addLevel(double levelValue) {
        if (!_levels.contains(levelValue)) {
            _levels.add(levelValue);
        }
    }

    /**
     * Get true level number
     *
     * @return True level number
     */
    public int getTrueLevelNumber() {
        if (getLevelNum() == 0) {
            return 1;
        } else {
            return getLevelNum();
        }
    }

    /**
     * Get dimension by type
     *
     * @param dimType Dimension type
     * @return Dimension
     */
    public Dimension getDimension(DimensionType dimType) {
        for (int i = 0; i < getDimNumber(); i++) {
            Dimension aDim = ((Dimension) this.getDimension(i));
            if (aDim.getDimType() == dimType) {
                return aDim;
            }
        }

        return null;
    }

    /**
     * Use when dimensions have changed, to recalculate the shape.
     */
    public void resetShape() {
        // if (immutable) throw new IllegalStateException("Cant modify");  LOOK allow this for unlimited dimension updating
        this.shape = new int[dimensions.size()];
        for (int i = 0; i < dimensions.size(); i++) {
            Dimension dim = dimensions.get(i);
            shape[i] = dim.getLength();
        }
    }

    /**
     * Set a dimension
     *
     * @param tstr Dimension type string
     * @param values Dimension values
     * @param reverse If is reverse
     */
    public void setDimension(String tstr, List<Number> values, boolean reverse) {
        DimensionType dType = DimensionType.Other;
        switch (tstr) {
            case "X":
                dType = DimensionType.X;
                break;
            case "Y":
                dType = DimensionType.Y;
                break;
            case "Z":
                dType = DimensionType.Z;
                break;
            case "T":
                dType = DimensionType.T;
                break;
        }
        Dimension dim = new Dimension("null", values.size(), dType);
        dim.setDimValues(values);
        dim.setReverse(reverse);
        this.setDimension(dim);
    }

    /**
     * Set a dimension
     *
     * @param tstr Dimension type string
     * @param values Dimension values
     * @param index Index
     * @param reverse If is reverse
     */
    public void setDimension(String tstr, List<Number> values, boolean reverse, int index) {
        DimensionType dType = DimensionType.Other;
        switch (tstr) {
            case "X":
                dType = DimensionType.X;
                break;
            case "Y":
                dType = DimensionType.Y;
                break;
            case "Z":
                dType = DimensionType.Z;
                break;
            case "T":
                dType = DimensionType.T;
                break;
        }
        Dimension dim = new Dimension("null", values.size(), dType);
        dim.setDimValues(values);
        dim.setReverse(reverse);
        this.setDimension(index, dim);
    }

    /**
     * Set dimension
     *
     * @param aDim The dimension
     */
    public void setDimension(Dimension aDim) {
        if (aDim == null) {
            return;
        }

        if (aDim.getDimType() == DimensionType.Other) {
            this.addDimension(aDim);
        } else {
            boolean hasDim = false;
            for (int i = 0; i < getDimNumber(); i++) {
                Dimension bDim = (Dimension) this.getDimension(i);
                if (bDim.getDimType() == aDim.getDimType()) {
                    this.setDimension(i, aDim);
                    hasDim = true;
                    break;
                }
            }

            if (!hasDim) {
                this.addDimension(aDim);
                this.resetShape();
            }
        }
    }

    /**
     * Set dimension
     *
     * @param aDim The dimension
     * @param idx Index
     */
    public void setDimension(int idx, Dimension aDim) {
        if (aDim == null) {
            return;
        }

        if (this.getDimNumber() > idx) {
            this.setDimension(idx, aDim);
        } else {
            this.setDimension(aDim);
        }
        this.resetShape();
    }

    /**
     * Set dimension by dimension type
     *
     * @param aDim The dimension
     * @param dimType Dimension type
     */
    public void setDimension(Dimension aDim, DimensionType dimType) {
        if (aDim.getDimType() == dimType) {
            setDimension(aDim);
        }
    }

    /**
     * Get index of a dimension
     *
     * @param aDim The dimension
     * @return Index
     */
    public int getDimIndex(Dimension aDim) {
        int idx = -1;
        for (int i = 0; i < getDimNumber(); i++) {
            if (aDim.equals(this.getDimension(i))) {
                idx = i;
                break;
            }
        }

        return idx;
    }

    /**
     * Get dimension length
     *
     * @param idx Dimension index
     * @return Dimension length
     */
    public int getDimLength(int idx) {
        return this.getDimension(idx).getLength();
    }

    /**
     * Determine if has Xtrack dimension
     *
     * @return Boolean
     */
    public boolean hasXtrackDimension() {
        boolean has = false;
        for (int i = 0; i < getDimNumber(); i++) {
            if (((Dimension) this.getDimension(i)).getDimType() == DimensionType.Xtrack) {
                has = true;
                break;
            }
        }

        return has;
    }

    /**
     * Determine if the variable has a dimension
     *
     * @param dimId Dimension identifer
     * @return Boolean
     */
    public boolean hasDimension(int dimId) {
        for (int i = 0; i < this.getDimNumber(); i++) {
            Dimension aDim = (Dimension) this.getDimension(i);
            if (aDim.getDimId() == dimId) {
                return true;
            }
        }

        return false;
    }

    /**
     * If the variable has a null dimension
     *
     * @return Boolean
     */
    public boolean hasNullDimension() {
        for (int i = 0; i < this.getDimNumber(); i++) {
            Dimension aDim = (Dimension) this.getDimension(i);
            if (aDim == null) {
                return true;
            }
            if (aDim.getShortName() == null) {
                return true;
            }
            if (aDim.getShortName().equals("null")) {
                return true;
            }
        }
        return false;
    }

    /**
     * If the dimensions equales with another variable
     *
     * @param var Another variable
     * @return Boolean
     */
    public boolean dimensionEquales(Variable var) {
        if (this.getDimNumber() != var.getDimNumber()) {
            return false;
        }
        for (int i = 0; i < this.getDimNumber(); i++) {
            Dimension adim = (Dimension) this.getDimension(i);
            Dimension bdim = (Dimension) var.getDimension(i);
            if (!adim.getShortName().equals(bdim.getShortName())) {
                return false;
            }
        }

        return true;
    }

    /**
     * If the dimensions size equales with another variable
     *
     * @param var Another variable
     * @return Boolean
     */
    public boolean dimensionSizeEquals(Variable var) {
        if (this.getDimNumber() != var.getDimNumber()) {
            return false;
        }

        for (int i = 0; i < this.getDimNumber(); i++) {
            Dimension adim = (Dimension) this.getDimension(i);
            Dimension bdim = (Dimension) var.getDimension(i);
            if (adim.getLength() != bdim.getLength()) {
                return false;
            }
        }

        return true;
    }

    /**
     * If the dimensions contains the diemsions of another variable
     *
     * @param var Another variable
     * @return Boolean
     */
    public boolean dimensionContains(Variable var) {
        if (this.getDimNumber() < var.getDimNumber()) {
            return false;
        }

        int sidx = 0;
        if (this.getDimNumber() > var.getDimNumber()) {
            sidx = this.getDimNumber() - var.getDimNumber();
        }
        for (int i = sidx; i < var.getDimNumber(); i++) {
            Dimension adim = (Dimension) this.getDimension(i);
            Dimension bdim = (Dimension) var.getDimension(i - sidx);
            if (adim.getLength() != bdim.getLength()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get level dimension for SWATH data variable
     *
     * @param var Variable
     * @return Dimension
     */
    public Dimension getLevelDimension(Variable var) {
        if (this.getDimNumber() > var.getDimNumber()) {
            for (int i = var.getDimNumber(); i < this.getDimNumber(); i++) {
                Dimension dim = (Dimension) this.getDimension(i);
                if (dim.getDimType() == DimensionType.Other) {
                    return dim;
                }
            }
        }

        return null;
    }

    /**
     * Get times
     *
     * @return Times
     */
    public List<Date> getTimes() {
        Dimension tDim = this.getTDimension();
        if (tDim == null) {
            return null;
        }

        List<Double> values = tDim.getDimValue();
        List<Date> times = new ArrayList<>();
        for (Double v : values) {
            times.add(DateUtil.fromOADate(v));
        }

        return times;
    }

    /**
     * Get attribute index by name, return -1 if the name not exist.
     *
     * @param attName Attribute name
     * @return Attribute index
     */
    public int getAttributeIndex(String attName) {
        int idx = -1;
        for (int i = 0; i < this.getAttributes().size(); i++) {
            if (this.getAttributes().get(i).getShortName().equalsIgnoreCase(attName)) {
                idx = i;
                break;
            }
        }

        return idx;
    }

    /**
     * Get attribute value string by name
     *
     * @param attName Attribute name
     * @return Attribute value string
     */
    public String getAttributeString(String attName) {
        String attStr = "";
        for (Attribute aAtt : this.getAttributes()) {
            if (aAtt.getShortName().equalsIgnoreCase(attName)) {
                attStr = aAtt.toString();
            }
        }

        return attStr;
    }

    /**
     * Add a dimension
     *
     * @param dim Dimension
     */
    public void addDimension(Dimension dim) {
        this.getDimensions().add(dim);
        this.resetShape();
    }

    /**
     * Add a dimension
     *
     * @param idx Index
     * @param dim Dimension
     */
    public void addDimension(int idx, Dimension dim) {
        this.getDimensions().add(idx, dim);
        this.resetShape();
    }

    /**
     * Add a dimension
     *
     * @param dType Dimension type
     * @param values Dimension values
     */
    public void addDimension(DimensionType dType, List<Number> values) {
        Dimension dim = new Dimension("null", values.size(), dType);
        dim.setDimValues(values);
        this.addDimension(dim);
    }

    /**
     * Add a dimension
     *
     * @param tstr Dimension type string
     * @param values Dimension values
     */
    public void addDimension(String tstr, List<Number> values) {
        DimensionType dType = DimensionType.Other;
        switch (tstr) {
            case "X":
                dType = DimensionType.X;
                break;
            case "Y":
                dType = DimensionType.Y;
                break;
            case "Z":
                dType = DimensionType.Z;
                break;
            case "T":
                dType = DimensionType.T;
                break;
        }
        Dimension dim = new Dimension("null", values.size(), dType);
        dim.setDimValues(values);
        this.addDimension(dim);
    }
    
    /**
     * Add an attribute
     * @param attr Attribute
     */
    public void addAttribute(Attribute attr) {
        this.attributes.add(attr);
    }

    /**
     * Add attribute
     *
     * @param attName Attribute name
     * @param attValue Attribute value
     */
    public void addAttribute(String attName, List attValue) {
        Attribute aAtt = new Attribute(attName, attValue);;

        this.addAttribute(aAtt);
    }

    /**
     * Add attribute
     *
     * @param attName Attribute name
     * @param attValue Attribute value
     */
    public void addAttribute(String attName, String attValue) {
        Attribute aAtt = new Attribute(attName, attValue);

        this.addAttribute(aAtt);
    }

    /**
     * Add attribute
     *
     * @param attName Attribute name
     * @param attValue Attribute name
     */
    public void addAttribute(String attName, double attValue) {
        Attribute aAtt = new Attribute(attName, attValue);

        this.addAttribute(aAtt);
    }

    /**
     * Update z dimension from levels
     */
    public void updateZDimension() {
        if (_levels.size() > 0) {
            Dimension zdim = new Dimension("null", 0, DimensionType.Z);
            zdim.setValues(_levels);
            this.setZDimension(zdim);
        }
    }
    // </editor-fold>
}
