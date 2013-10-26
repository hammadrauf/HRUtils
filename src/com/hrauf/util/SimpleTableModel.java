/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hrauf.util;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hammad Rauf
 */
public class SimpleTableModel extends DefaultTableModel {
    	private Object columnNames[]; 
    	private Object[][] data;

    public SimpleTableModel() {
        super();
    }    
        
    public SimpleTableModel(Vector tableValues, Vector colnames) {
                super(tableValues.size(), colnames.size());
    		columnNames= new Object[colnames.size()];
    		int numbRows=tableValues.size()/colnames.size();
    		data=new Object[numbRows][colnames.size()];
    		//
    		columnNames= colnames.toArray();
    		//
    		for(int i=0, k=0;i<numbRows;i++){
    			for(int j=0;j<colnames.size();j++){
    				data[i][j]=tableValues.get(k);
    				k++;
    			}
    		}
    		
    	}

        public SimpleTableModel(Collection tableValues, Collection colnames) {
                super(tableValues.size(), colnames.size());
                Object[] tableValuesArray = tableValues.toArray();
    		columnNames= new Object[colnames.size()];
    		int numbRows=tableValues.size()/colnames.size();
    		data=new Object[numbRows][colnames.size()];
    		columnNames= colnames.toArray();
    		for(int i=0, k=0;i<numbRows;i++){
    			for(int j=0;j<colnames.size();j++){
    				data[i][j]=tableValuesArray[k];
    				k++;
    			}
    		}
    	}  

    @Override
          public int getColumnCount() {
            if(columnNames==null)
                return 0;
            else
                return columnNames.length;
        }

    @Override
       public int getRowCount() {
        if(data==null)
            return 0;
        else
            return data.length;
        }
 
    @Override
        public String getColumnName(int col) {
            return String.valueOf(columnNames[col]);
        }
        
    @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
 
        
    @Override
        public boolean isCellEditable(int row, int col) {
                return false;
        }

    
}
