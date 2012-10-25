/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticalgorithm;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.NumberFormat;
import javax.swing.*;
import java.awt.event.*;


import javax.swing.JFrame;
//import javax.swing.JLabel;

import java.io.File;
import java.util.Date;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;


import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.math.BigInteger;
/**
 *
 * @author Faiding
 * --The main operator--
 *
 */
public class GeneticTravelingSalesman extends JFrame implements Runnable, ComponentListener{

    //Is random?
    public static int IS_RANDOM;
    //SA or GA?
    public static int IS_SA;
    
    // ---GENETIC ALGORITHM VARIABLES---
    //Number of locations
    public static int CITY_COUNT;
    //Number of chromosomes
    public static int POPULATION_SIZE;
    //Percent of newborns to mutate
    public static double MUTATION_PERCENT;
    
    // ---SIMULATED ANNEALING VARIABLES---
    //assignables
    public static double START_TEMPERATURE;
    public static double STOP_TEMPERATURE;
    public static int CYCLES;
    
    //epoch number
    public int epoc;
    protected TSPSimulatedAnnealingID anneal;
    
    //run options
    public static int TEST_NUMBER;
    public static String FILE_NAME;

    //Part eligible for mating
    protected int matingPopulationSize = POPULATION_SIZE / 2;
    //Part favorable for mating
    protected int favoredPopulationSize = this.matingPopulationSize / 2;
    //Object to display the map
    protected Map map = null;
    //status
    protected JLabel status;
    //background map
    protected JLabel USMap;
    //how much genetic material to take during mating
    protected int cutLength = CITY_COUNT / 5;
    //current generation (epoch)
    protected int generation;
    //background worker thread
    protected Thread worker = null;
    //thread started?
    protected boolean started = false;
    //finished?
    protected boolean finished = false;
    //track cost
    protected int trackCost;
    //list of locations
    protected City[] cities;
    //city locations for GA
    protected HoldCity[] hCities;
    public long startTimeMS;
    
    
    //get time in nanoseconds
    public double NS1, NS2;
    //get finish time
    public static long finishTimeMS;
    //time display variables
    String timeMS;
    double pTime, pTimeS;

    protected MainConsole mc;

    //the genetic algorithm
    protected TSPGeneticAlgorithm genetic;

    //constructor
    public GeneticTravelingSalesman() {
        addComponentListener(this);
        setSize(550,400);
        setResizable(false);
        if (IS_SA ==0) { setTitle("Mapping (Genetic Algorithm)"); } else { setTitle("Mapping (Simulated Annealing)"); }
        setLocation(100,200);
    }

    //not used
    public void componentHidden(final ComponentEvent e) {    }
    public void componentMoved(final ComponentEvent e) {    }
    public void componentResized(final ComponentEvent e) {    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    }
/**
 * Used for reporting to Excel file
*/
    public class HoldResults {
        private int generation;
        private double cost;
        private int position;
        private double time;
        public HoldResults(int g, double c, int s, double t){
            this.generation=g;
            this.cost=c;
            this.position=s;
            this.time = t;
    }
    }
    public static HoldResults[] hr = new HoldResults[10000];
    public static HoldResults[] hrA = new HoldResults[10000];

    //workbook parameters
    WritableWorkbook workbook;
    WritableSheet sheet;

    public int holdLoop=0;
    
    //assignables for workbook
    String hcXa, hcYa;
    String hcXb, hcYb;
    String xyDist;


    BigInteger hFactor;
    NumberFormat formatter = new DecimalFormat("#0.00");
    WritableFont boldFont = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);
    

    /**
     * Display worksheets for Genetic Algorithm and Simulated Annealing results
     * @param filename
     * @param testType 
     */
    public void BuildSheet(String filename, int testType) {
                if(testType == 0) {        
        try {
            workbook = Workbook.createWorkbook(new File(filename)); 

                //GENETIC ALGORITHM SPECIFICATIONS
            
            //HEADERS + + +

            
                sheet = workbook.createSheet("Genetic Algorithms", 0);
                    sheet.addCell(new Label(1,0,"Generation", new WritableCellFormat(boldFont)));
                    sheet.addCell(new Label(2,0,"Solution Fitness", new WritableCellFormat(boldFont)));
                    sheet.addCell(new Label(3,0,"Processed At (MS)", new WritableCellFormat(boldFont)));
                    //time processed
                    sheet.addCell(new Label(0,0,"Total Time", new WritableCellFormat(boldFont)));
                        timeMS = Float.toString(finishTimeMS);
                    sheet.addCell(new Label(0,1,timeMS + "ms"));
                    //number of locations
                    sheet.addCell(new Label(0,2,"Locations", new WritableCellFormat(boldFont)));
                    sheet.addCell(new Number(0,3,this.CITY_COUNT));
                    //number of chromosomes
                    sheet.addCell(new Label(0,4,"Chromosomes", new WritableCellFormat(boldFont)));
                    sheet.addCell(new Number(0,5,this.POPULATION_SIZE));
                    //mutation %
                    sheet.addCell(new Label(0,6,"Mutation %", new WritableCellFormat(boldFont)));
                    sheet.addCell(new Label(0,7,Double.toString(this.MUTATION_PERCENT * 100) + "%"));
                    //coordinates
                    sheet.addCell(new Label(4,0,"Loc A (X,Y)", new WritableCellFormat(boldFont)));
                    sheet.addCell(new Label(5,0,"Loc B (X,Y)", new WritableCellFormat(boldFont)));
                    sheet.addCell(new Label(6,0,"Distance", new WritableCellFormat(boldFont)));
           //VALUES + + +
                for(int i=0;i<this.trackCost;i++) {
                    pTime = Double.parseDouble(formatter.format(hr[i].time));
                    //time in seconds
                    pTimeS = pTime / 1000;
                    sheet.addCell(new Number(1,i+1,hr[i].generation));
                    sheet.addCell(new Number(2,i+1,hr[i].cost));
                    sheet.addCell(new Number(3,i+1,pTime));

    holdLoop++;
            }

                
                for(int k=0;k<holdLoop;){
                   for(int j=0;j<this.hCities.length;j++){
                    k++;
                    
                    hcXa = Integer.toString(hCities[j].getx());
                    hcYa = Integer.toString(hCities[j].gety());
                    hcXb = Integer.toString(hCities[j].getxb());
                    hcYb = Integer.toString(hCities[j].getyb());
                    xyDist = Integer.toString(getDist(hCities[j].getxb(),hCities[j].getx(),hCities[j].getyb(),hCities[j].gety()));
                        sheet.addCell(new Label(4,(k),"(" + hcXa + "," + hcYa + ")"));
                        sheet.addCell(new Label(5,(k),"(" + hcXb + "," + hcYb + ")"));
                        sheet.addCell(new Label(6,(k),xyDist));
                    }

 
            }
                //clear extra cells from pattern
                    for(int i=0;i<this.hCities.length;i++){
                        sheet.addCell(new Label(4,(this.trackCost + 1) + i,""));
                        sheet.addCell(new Label(5,(this.trackCost + 1) + i,""));
                        sheet.addCell(new Label(6,(this.trackCost + 1) + i,""));
                    }
                workbook.write();
                workbook.close();

        }
        catch (Exception ex){
        ex.printStackTrace();
    }
        //reset loop
    //holdLoop = 0;
    }
                
       //SIMULATED ANNEALING SPECIFICATIONS
                else if(testType == 1) {
           try {
        workbook = Workbook.createWorkbook(new File(filename)); 

                //Build worksheet according to Genetic Algorithm specifications

        //HEADERS + + +
                sheet = workbook.createSheet("Simulated Annealing", 1);
                sheet.addCell(new Label(1,0,"Epoch", new WritableCellFormat(boldFont)));
                sheet.addCell(new Label(2,0,"Solution Fitness", new WritableCellFormat(boldFont)));
                sheet.addCell(new Label(3,0,"Processed At (MS)", new WritableCellFormat(boldFont)));
                //time processed
                sheet.addCell(new Label(0,0,"Total Time", new WritableCellFormat(boldFont)));
                 timeMS = Float.toString(finishTimeMS);
                sheet.addCell(new Label(0,1,timeMS + "ms"));
                //number of locations
                sheet.addCell(new Label(0,2,"Locations", new WritableCellFormat(boldFont)));
                sheet.addCell(new Number(0,3,this.CITY_COUNT));
                //start temperature
                sheet.addCell(new Label(0,4,"Start Temperature", new WritableCellFormat(boldFont)));
                sheet.addCell(new Number(0,5,this.START_TEMPERATURE));                
                //finish temperature
                sheet.addCell(new Label(0,6,"Stop Temperature", new WritableCellFormat(boldFont)));
                sheet.addCell(new Number(0,7,this.STOP_TEMPERATURE));                   
                //cycles
                sheet.addCell(new Label(0,8,"Cycles", new WritableCellFormat(boldFont)));
                sheet.addCell(new Number(0,9,this.CYCLES));                 
                //coordinates
                sheet.addCell(new Label(4,0,"Loc A (X,Y)", new WritableCellFormat(boldFont)));
                sheet.addCell(new Label(5,0,"Loc B (X,Y)", new WritableCellFormat(boldFont)));
                
        //VALUES + + +
           for(int i=0;i<this.trackCost;i++) {
                    pTime = Double.parseDouble(formatter.format(hrA[i].time));
                    //time in seconds
                    pTimeS = pTime / 1000;
                    sheet.addCell(new Number(1,i+1,hrA[i].generation));
                    sheet.addCell(new Number(2,i+1,hrA[i].cost));
                    sheet.addCell(new Number(3,i+1,pTime));

    holdLoop++;
            }

                for(int k=0;k<holdLoop;){
                   for(int j=0;j<this.hCities.length;j++){
                    k++;

                    hcXa = Integer.toString(hCities[j].getx());
                    hcYa = Integer.toString(hCities[j].gety());
                    hcXb = Integer.toString(hCities[j].getxb());
                    hcYb = Integer.toString(hCities[j].getyb());
                    xyDist = Integer.toString(getDist(hCities[j].getxb(),hCities[j].getx(),hCities[j].getyb(),hCities[j].gety()));
                        sheet.addCell(new Label(4,(k),"(" + hcXa + "," + hcYa + ")"));
                        sheet.addCell(new Label(5,(k),"(" + hcXb + "," + hcYb + ")"));
                        sheet.addCell(new Label(6,(k),xyDist));
                    }

 
            }
                    //clear extra cells from pattern
                    for(int i=0;i<this.hCities.length;i++){
                        sheet.addCell(new Label(4,(this.trackCost + 1) + i,""));
                        sheet.addCell(new Label(5,(this.trackCost + 1) + i,""));
                        sheet.addCell(new Label(6,(this.trackCost + 1) + i,""));
                    }
                workbook.write();
                workbook.close();
             }
                   
        catch (Exception ex){
        ex.printStackTrace();
    }
       }
             
    }
    private int getDist(int x1, int y1, int x2, int y2) {
        int xdiff = x2 - x1;
        int ydiff = y2 - y1;
        return (int) Math.sqrt((xdiff * xdiff) + (ydiff * ydiff));
    }
    

    /**
     * Map constructor
     * @param e 
     */
    public void componentShown(final ComponentEvent e) {
        getContentPane().setLayout(new BorderLayout());

        //assign the canvas for display
        if(this.map == null) {
            this.map = new Map(this);
            getContentPane().add(this.map, "Center");
            this.status = new JLabel("Starting");
            getContentPane().add(this.status,"South");

        }


        //Build city arrays
        final int height = getBounds().height;
        final int width = getBounds().width;
        
        this.cities = new City[GeneticTravelingSalesman.CITY_COUNT];
        this.hCities = new HoldCity[GeneticTravelingSalesman.CITY_COUNT];
        
        
/**
 * Assign X,Y coordinates to cities array
*/
        

        if(IS_RANDOM==0){
            for (int i=0; i < GeneticTravelingSalesman.CITY_COUNT; i++) {
                this.cities[i] = new City(mc.hl[i].getX(),mc.hl[i].getY());
            }
       }
       
        
        
/**
 * Construct Genetic Algorithm Test
*/
        if( IS_SA == 0) {
            //Pass variables to constructor class
        this.genetic = new TSPGeneticAlgorithm(this.cities, GeneticTravelingSalesman.POPULATION_SIZE, GeneticTravelingSalesman.MUTATION_PERCENT, 0.25, 0.5,
                GeneticTravelingSalesman.CITY_COUNT / 5);
            //call the background thread
        start();
        }
        
/**
 * Construct Simulated Annealing Test
 * Uniform function
*/
        
       else if( IS_SA == 1) {
//Simulated Annealing Array constructor.  This process is synonymous to that used in Chromosome.java for GA
            //Sends the array of cities, start temperature, stop temperature and cycles to the function
            
        this.anneal = new TSPSimulatedAnnealingID(this.cities, START_TEMPERATURE, STOP_TEMPERATURE, CYCLES);
        
        final boolean taken[] = new boolean [this.cities.length];
        final Integer path[] = new Integer[this.cities.length];
        
        for(int i=0; i < path.length; i++) { taken[i]=false; }
        
        for(int i=0; i < path.length - 1; i++) {
            int icandidate;
            do {
                icandidate = (int) (Math.random() * path.length);
                
            } while (taken[icandidate]);
            path[i] = icandidate;
            taken[icandidate] = true;
            
            if( i==path.length - 2) {
                icandidate = 0;
                while (taken[icandidate]) {
                    icandidate++;
                }
                path[i+1] = icandidate;
            }
        }
        
        this.anneal.putArray(path);
        
        start();
        }
    }
    
//Misc return functions --------------------------------------------------
    public void init() {   (new GeneticTravelingSalesman()).setVisible(true);    }

    //get first chromosome in list
    public TSPChromosome getTopChromosome() {  return this.genetic.getChromosome(0); }

    public TSPChromosome getChromosome(int i) { return this.genetic.getChromosome(i); }

    //ANNEALING
    public TSPSimulatedAnnealingID getAnneal() { return this.anneal; }
//Misc return functions --------------------------------------------------
    
    
    /**
     * Main background loop
     * Uniform function
     */
    public void run() {
    //get start time
    startTimeMS = System.currentTimeMillis();
    NS1 = System.nanoTime();
        //define cost
        double thisCost = 500.0;
        double oldCost = 0.0;
        int countSame = 0;

        this.map.update(this.map.getGraphics());

        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        
if (IS_SA==0) {
/**
 * Genetic Algorithm iterations 
 *  -Runs iterations
     * -Counts same for error
     * -Assigns cost variables from respective cost functions
     * -Updates map graphics
     * -Reports test parameters before/after completion
 */
    
        //if the cost stays the same for 100 generations, then we assume completion
        while (countSame < 100) {
            this.generation++;
            //convert nanoseconds to milliseconds
            NS2 = (System.nanoTime()-NS1) / 1000000;
            this.status.setText("Generation: " + this.generation + " Cost: " + (int) thisCost + " Mutated: " + nf.format(this.genetic.getMutationPercent()) + "% " +  "@ " + NS2 + " ms.");
            this.genetic.iteration();
            thisCost = this.getTopChromosome().getCost();
            this.trackCost++;

            hr[this.trackCost-1] = new HoldResults(this.generation, thisCost ,this.trackCost, NS2);
            if((int) thisCost == (int) oldCost) {
                //if iteration results are equal, count++
                countSame++;
            } else {
                countSame=0;
                oldCost = thisCost;
            }
            this.map.update(this.map.getGraphics());
        }
        
        //report test
        finishTimeMS = (System.currentTimeMillis() - startTimeMS);
        this.status.setText("Solution found after " + this.generation + " generations in " + finishTimeMS + "ms.");
        

        BuildSheet(FILE_NAME,0);
        System.out.println("BREAK:" + hr[10].cost);
}else{
    
    /**
     * Simulated Annealing Iterations
     * -Assigns error to cost
     * -Performs an iteration
     */
    
    //using 10 for error count
    while (countSame < 10) {
        this.epoc ++;
        thisCost = this.anneal.getError();
        
        NS2 = (System.nanoTime()-NS1) / 1000000;
        this.status.setText("Epoch " + this.epoc + ". Cost: " + thisCost);
        
        //perform an iteration
        this.anneal.iteration();
        
        //spreadsheet
        this.trackCost++;
        hrA[this.trackCost-1] = new HoldResults(this.epoc, thisCost ,this.trackCost, NS2);

        
        if((int) thisCost == (int) oldCost) {
            countSame ++;
        }else{
            countSame=0;
            oldCost = thisCost;
        }
        this.map.update(this.map.getGraphics());
    }
    
    //report test
    finishTimeMS = (System.currentTimeMillis() - startTimeMS);
    this.status.setText("Solution found after " + this.epoc + " epochs in " + finishTimeMS + "ms.");
    BuildSheet(FILE_NAME,1);

}

    }

    //get time in nanoseconds
    public float getNS(){  return System.nanoTime();  }

/**
 * Start the thread
*/
    public void start() {
        //create initial chromosomes and start background thread

        this.started=true;
        //update graphics
        this.map.update(this.map.getGraphics());

        //generation for GA, epoc for SA
        this.generation = 0;
        this.epoc = 0;

        if(this.worker != null) {
            this.worker=null;
        }
        this.worker = new Thread(this);
        this.worker.start();
    }

}
