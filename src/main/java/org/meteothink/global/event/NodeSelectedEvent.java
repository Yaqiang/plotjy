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
package org.meteothink.global.event;

import java.util.EventObject;

/**
 * Item node of the LayersLegend selected event
 * 
 * @author Yaqiang Wang
 */
public class NodeSelectedEvent extends EventObject {
    /**
     * Constructor
     * @param source Source object
     */
    public NodeSelectedEvent(Object source){
        super(source);
    }
}