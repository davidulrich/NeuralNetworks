/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticalgorithm;

/**
 *
 * @author Faiding
 *
 * Holds the location of the cities
 */
public class City {
int xpos;
int ypos;

//Constructor
City(final int x, final int y) {
    this.xpos=x;
    this.ypos=y;
}
//Return X position
int getx() {
    return this.xpos;
}
//Return Y position
int gety() {
    return this.ypos;
}
//How close one city is to the other
// cother=other city
// returns the distance
int proximity(final City cother){
    return proximity(cother.getx(), cother.gety());
}

//Applies pythagorean theorem to calculate the distance between two cities
//the distance is then multiplied by the cost
int proximity(final int x, final int y) {
    final int xdiff = this.xpos - x;
    final int ydiff = this.ypos - y;
    return (int) Math.sqrt((xdiff * xdiff) + (ydiff * ydiff));
}
}
