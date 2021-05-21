import java.util.ArrayList;

public class RamblersState  {
  

    SearchState state;
    private Coords point;
    private int localcost;
    private int globalCost;
    private RamblersState parent;

  

  /**
   * constructor
   * 
   * @param s  a SearchState
   * @param lc local cost of getting to this node from its predecessor
   */

  public RamblersState(Coords p, int lc) {
    
    localcost=lc;
    point =p;
}
  /**
   * accessor for point
   */

  public Coords get_Point() {
    return point;
  }
 /**
   * accessor for state
   */

  public SearchState get_State() {
    return state;
  }

  /**
   * accessor for parent
   */

  public RamblersState getParent() {
    return parent;
  }
  public void setParent(RamblersState pa) {
     parent=pa;
  }
  /**
   * mutator for localcost
   *
   */

  public void setLocalCost(int lc) {
    localcost = lc;
  }

  /**
   * acccessor for localcost
   *
   */

  public int getLocalCost() {
    return localcost;
  }

  /**
   * mutator for globalCost
   *
   */
  public void setGlobalCost(int lc) {
    globalCost = lc;
  }

  /**
   * acccessor for globalcost
   *
   */
  public int getGlobalCost() {
    return globalCost;
  }

  /**
   * mutator for point
   */
  public void setPoint(Coords p) {
    point = p;
  }

  
  public boolean goalPredicate(RamblersSearch searcher) {
 if(this.point.gety()==searcher.getgoal().gety()&&this.point.getx()==searcher.getgoal().getx())
 return true;
 else return false;
  }
  
  
  public ArrayList<RamblersState> getSuccessors(RamblersSearch searcher,Coords po) {
      int x=po.getx();
      int y=po.gety();
    
    ArrayList<Coords> slis  = new ArrayList<Coords>();
    if(y<searcher.get_terrainmap().getDepth()-1)
    slis.add(new Coords(y+1, x));
    if(x<searcher.get_terrainmap().getWidth()-1)
    slis.add(new Coords(y, x+1));
    if(y>0)
    slis.add(new Coords(y-1, x));
    if(x>0)
    slis.add(new Coords(y, x-1));
    
    ArrayList<RamblersState> nlis = new ArrayList<RamblersState>();

    for (Coords suc : slis) {
        int x1=suc.getx();
        int y1=suc.gety();
      if(searcher.get_terrainmap().getTmap()[y1][x1]<=searcher.get_terrainmap().getTmap()[y][x])
      {  nlis.add(new RamblersState(suc,1));}
      else{nlis.add(new RamblersState(suc,1+Math.abs(searcher.get_terrainmap().getTmap()[y1][x1]-searcher.get_terrainmap().getTmap()[y][x])));

      }
     
    }
    return nlis;
  }

  public boolean sameState(RamblersState n2) {
     
     if((this.get_Point().getx()==n2.get_Point().getx())&&(this.get_Point().gety()==n2.get_Point().gety()))
    return true;
   
   else return false;
  }
    
  public String toString() {
    String parent_state;
    if (parent == null)
     { parent_state = "null";
      return "node  ("+this.point.gety()+","  +this.point.getx()+ ") parent state ("+parent_state+") local cost (" 
       +localcost + ") global cost ("+globalCost + ")";
  }
    else

    return "node  ("+this.point.gety()+","  +this.point.getx()+ ") parent state ("+parent.point.gety()+","  +parent.point.getx()+ ") local cost (" 
       +localcost + ") global cost ("+globalCost + ")";
  }
}
