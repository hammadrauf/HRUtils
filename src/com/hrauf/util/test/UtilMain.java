/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrauf.util.test;

/**
 *
 * @author Hammad
 */
public class UtilMain {
   public static void main(String[] args) {
     ScribbleFrame myFrame = new ScribbleFrame();
      myFrame.setVisible(true);
      TestScribble myFrame2 = new TestScribble(myFrame, false);
      myFrame2.setVisible(true);
   }
}
