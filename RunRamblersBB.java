public class RunRamblersBB {
    
    public static void main(String[] arg) {

        TerrainMap tm = new TerrainMap("tmc.pgm");
        Coords s=new Coords(7,0);
        Coords g=new Coords(5,8);
        RamblersSearch ser=new RamblersSearch(tm, s, g);
        ser.runSearch();
    
      }
}
