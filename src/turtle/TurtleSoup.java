/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.ArrayList;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
    	// avoid magic number, declare variabls as final since they are constant
    	final int squareSideNumber = 4;
    	final int squareAngle = 90;
    	
        for (int x = 0; x < squareSideNumber; x++) {
            turtle.forward(sideLength);
            turtle.turn(squareAngle);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
    	// since preconditions in specification indicate that sides must be > 2, we don't need to
    	// check this condition explicitly, or we can throw new unchecked exception and declare them
    	// inside specification as @throws ....
    	// you can refer to here for more information: 
    	// http://web.mit.edu/6.031/www/sp18/classes/06-specifications/#@unchecked_exceptions_signal
    	
    	final int regularPolygonAngleTotal = (sides - 2) * 180;
    	final double regularPolygonAngle = (double) regularPolygonAngleTotal / sides;
    	return regularPolygonAngle;
        //return (double) (sides - 2) * 180 / sides;
        /* why return (double) ((sides - 2) * 180 / sides); failed but upper worked? */
        // source: http://bmanolov.free.fr/javaoperators.php
        // reason: since casting operator takes in bigger priority than +,-,*,/, but () takes the
        // biggest priority, thus the failed version of code would first make results in integer format.
        // while the right version would do double casting to (sides - 2) first, then make whole result to
        // be double type.
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
    	// if checking is ommitted, and the reason is the same as the above one
        return (int) Math.round(360 / (180 - angle));
    }
    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
    	// if checking is ommitted, and the reason is the same as the above one
    	
        final double angle = 180 - calculateRegularPolygonAngle(sides);
        /*outside angle to draw right-hand turns*/
        for (int x = 0; x < sides; x++) {
            turtle.forward(sideLength);
            turtle.turn(angle);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {
        final int dX = targetX - currentX;
        final int dY = targetY - currentY;
        final double theta = 90 - Math.atan2(dY, dX) * 180 / Math.PI - currentHeading;
        
        if (theta < 0) {
            return theta + 360;
        } else{
            return theta;
        }
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> headingAdjustment = new ArrayList<>();
        int pointSeqSize = xCoords.size();
        double currentHeading;//Don't know why I need to double first,not in for loop
        
        for (int x = 1; x < pointSeqSize; x++) {
        	// is this if statement necessary?
            if (x == 1) {
                currentHeading = 0;//set initial heading angle as 0
            } else {
                currentHeading = calculateHeadingToPoint(0, xCoords.get(x-2), yCoords.get(x-2),
                        xCoords.get(x-1), yCoords.get(x-1));//calculate previous heading angle
            }
            
            headingAdjustment.add(calculateHeadingToPoint(currentHeading, xCoords.get(x-1), yCoords.get(x-1),
                        xCoords.get(x), yCoords.get(x)));//calculate adjusted heading angle
        }
        
        return headingAdjustment;
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     * @param sides number of sides, where sides must be > 2
     * @param sideLength length of each side, where each should be > 0
     */
    public static void drawPersonalArt(Turtle turtle,int sides,int sideLength) {        
    	// variables should be written in camel case
        final double polygonAngle = calculateRegularPolygonAngle(sides);
        final double turnAngle = 2 * (180 - polygonAngle);
        
        for (int x = 0; x < sides; x++) {
            turtle.forward(sideLength);
            turtle.turn(turnAngle);
        }
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        //drawSquare(turtle, 40);
        drawPersonalArt(turtle, 8, 40);

        // draw the window
        turtle.draw();
    }

}
