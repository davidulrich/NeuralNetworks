/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticalgorithm;


import java.awt.*;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;
import java.lang.*;

/**
 *
 * @author Faiding
 * 
 * Draws the map, plots locations and displays lines connecting them
 */
public class Map extends JPanel {

    public int count=0;
    protected GeneticTravelingSalesman owner;

    Map(final GeneticTravelingSalesman owner) {
        this.owner=owner;
    }


    @Override
    public void paint(final Graphics g) {
        update(g);

    }

    //update graphical display
    Image img = Toolkit.getDefaultToolkit().createImage("C:/map.gif");
    @Override
    public void update(final Graphics g) {
        final int width = getBounds().width;
        final int height = getBounds().height;


        g.drawImage(img, 0, 0, null);
        //g.setColor(Color.white);
        //g.fillRect(0,0,width,height);

        if (!this.owner.started) {
            return;
        }

        //plot the locations
        
        for (int i=0; i < GeneticTravelingSalesman.CITY_COUNT; i++) {
            final int xpos = this.owner.cities[i].getx();
            final int ypos = this.owner.cities[i].gety();
            g.setColor(Color.black);
            g.fillRect(xpos - 5, ypos - 5, 5, 5);
            g.setColor(Color.darkGray);
            g.drawString(i + ":(" + xpos + ")" + ",(" + ypos + ")", xpos - 5, ypos - 5);

        }

        //GENETIC ALGORITHM
        if (GeneticTravelingSalesman.IS_SA == 0) {  
            
            final TSPChromosome top = this.owner.getTopChromosome();
            
        //draw the lines
        g.setColor(Color.black);
        for (int i=0; i<GeneticTravelingSalesman.CITY_COUNT -1;i++){
            final int icity = top.getGene(i);
            final int icity2 = top.getGene(i+1);


            g.drawLine(this.owner.cities[icity].getx(),this.owner.cities[icity].gety(),this.owner.cities[icity2].getx(),this.owner.cities[icity2].gety());
            count++;
            //pass city coordinates back to GTS for review
            this.owner.hCities[icity] = new HoldCity(this.owner.cities[icity].getx(),this.owner.cities[icity].gety(), count,
                    this.owner.cities[icity2].getx(), this.owner.cities[icity2].gety());
            System.out.println(this.owner.cities[icity].getx() + " : " + this.owner.cities[icity2].getx() + " @ " + this.owner.getChromosome(i));
            
                        
        }
        }
        
        //SIMULATED ANNEALING
        if (GeneticTravelingSalesman.IS_SA == 1) { 
            
            final Integer path[] = this.owner.getAnneal().getArray(); 
            
        //draw the lines
        g.setColor(Color.black);        
        for(int i=0; i < path.length - 1; i++) {
            final int icity = path[i];
            final int icity2 = path[i+1];
            
            g.drawLine(this.owner.cities[icity].getx(), this.owner.cities[icity].gety(), this.owner.cities[icity2].getx(),
                    this.owner.cities[icity2].gety());
          //pass city coordinates back to GTS for review
            this.owner.hCities[icity] = new HoldCity(this.owner.cities[icity].getx(),this.owner.cities[icity].gety(), count,
               this.owner.cities[icity2].getx(), this.owner.cities[icity2].gety());
        }
            
        }



    }

}
