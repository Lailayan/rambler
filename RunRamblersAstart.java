public class RunRamblersAstart {
    
    public static void main(String[] arg) {

        TerrainMap tm = new TerrainMap("tmc.pgm");
        Coords s=new Coords(2,7);
        Coords g=new Coords(7,2);
        RamblersSearch ser=new RamblersSearch(tm, s, g);
        ser.runSearch("Astarheight");
    
      }
}
