/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrauf.util;

import java.util.ArrayList;

/**
 *
 * @author Hammad
 */
public class XMLArrayList<E> extends ArrayList<E> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<XMLList>\n");
        for(int i=0; i<this.size(); i++) {
            sb.append(this.get(i).toString());
        }
        sb.append(" </XMLList>\n");
        return sb.toString();
    }
    
}
