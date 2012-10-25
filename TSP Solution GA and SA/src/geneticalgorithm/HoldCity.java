/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticalgorithm;

/**
 *
 * @author Faiding
 * Holds the information for each city to be accessed universally
 */
public class HoldCity {
int xpos;
int ypos;
int xpos2;
int ypos2;
int count;

//Constructor
HoldCity(final int x, final int y, int c, final int x2, final int y2) {
    this.xpos=x;
    this.ypos=y;
    this.count=c;
    this.xpos2=x2;
    this.ypos2=y2;
}
//Return X position
int getx() {
    return this.xpos;
}
//Return Y position
int gety() {
    return this.ypos;
}
//Return second X position
int getxb() {
    return this.xpos2;
}
//Return second Y position
int getyb() {
    return this.ypos2;
}

//get count
int getCount() {
    return this.count;
}
}
