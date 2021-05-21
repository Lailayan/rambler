
    /*
*	Search.java - abstract class specialising to MapSearch etc
* Phil Green 2013 version
* Heidi Christensen (heidi.christensen@sheffield.ac.uk) 2021 version
*/

import java.util.*;

import java.util.Iterator;
public class RamblersSearch {
 TerrainMap terrainmap;
 private Coords start;
 private Coords goal;
public RamblersSearch(TerrainMap tm,Coords s,Coords g) {
    terrainmap=tm;
    start=s;
    goal=g;
    }
 public void setstart(Coords s)
    {
    start=s;
    }
 public void set_terrainmap(TerrainMap tm)
    {
       terrainmap=tm;
    }
 public void setgoal(Coords g)
    {
    goal=g;
    }
 public Coords getstart()
    {
 return start;
    }

 public TerrainMap get_terrainmap()
    {
 return terrainmap;
    }
 public Coords getgoal()
    {
     return goal;
    }
 //............................................................................

 protected RamblersState initNode; // initial node
  protected RamblersState currentNode; // current node
  protected RamblersState old_node; // node found on open with same state as new one
  protected ArrayList<RamblersState> open; // open - list of SearchNodes
  protected ArrayList<RamblersState> closed; // closed
  protected ArrayList<RamblersState> successorNodes; // used in expand & vetSuccessors

  // run a search
  public String runSearch() {

    initNode = new RamblersState(start, this.get_terrainmap().getTmap()[start.gety()][start.getx()]); // create initial node
    initNode.setGlobalCost(0); // change from search2

   

    open = new ArrayList<RamblersState>(); // initial open, closed
    open.add(initNode);
    closed = new ArrayList<RamblersState>();

    int numIteration = 1;

    while (!open.isEmpty()) {

      // print contents of open
      System.out.println("-------------------------");
      System.out.println("iteration no " + numIteration);
      System.out.println("open is");
      for (RamblersState nn : open) {
        String nodestr = nn.toString();
        System.out.println(nodestr);
      }

      selectNode(); // change from search1 -selectNode selects next node given strategy,
      // makes it currentNode & removes it from open
      System.out.println("Current node: " + currentNode.toString());

      if (currentNode.goalPredicate(this))
        return reportSuccess(); // success
      // change from search1 - call reportSuccess

      expand(); // go again
      closed.add(currentNode); // put current node on closed
      numIteration = numIteration + 1;
    }
   
    return "Search Fails"; // out of the while loop - failure


  }

  // expand current node
  private void expand() {

    // get all successor nodes
    successorNodes = currentNode.getSuccessors(this,currentNode.get_Point()); // pass search instance

    // change from search2
    // set global costs and parents for successors
    
    for (RamblersState snode : successorNodes) {
      snode.setGlobalCost(currentNode.getGlobalCost() + snode.getLocalCost());
   
      snode.setParent(currentNode);
    }

    vetSuccessors(); // filter out unwanted - DP check

    // add surviving nodes to open
    for (RamblersState snode : successorNodes)
      open.add(snode);
  }

  // vet the successors - reject any whose states are on open or closed
  // change from search2 to do DP check

  private void vetSuccessors() {
    ArrayList<RamblersState> vslis = new ArrayList<RamblersState>();

    for (RamblersState snode : successorNodes) {
      if (!(onClosed(snode))) { // if on closed, ignore
        if (!(onOpen(snode))) {
          vslis.add(snode); // if not on open, add it
        } else { // DPcheck - node found on open is in old_node
          if (snode.getGlobalCost() <= old_node.getGlobalCost()) { // compare global costs
            old_node.setParent(snode.getParent()); // better route, modify node
            old_node.setGlobalCost(snode.getGlobalCost());
            old_node.setLocalCost(snode.getLocalCost());
          }
        }
      }
    }

    successorNodes = vslis;
  }

  // onClosed - is the state for a node the same as one on closed?
  private boolean onClosed(RamblersState newNode) {
    boolean ans = false;
    for (RamblersState closedNode : closed) {
      if (newNode.sameState(closedNode))
        ans = true;
    }
    return ans;
  }

  // onOpen - is the state for a node the same as one on open?
  // if node found, remember it in old_node
  private boolean onOpen(RamblersState newNode) {
    boolean ans = false;
    Iterator ic = open.iterator();
    while ((ic.hasNext()) && !ans) { // there can only be one node on open with same state
        RamblersState openNode = (RamblersState) ic.next();
      if (newNode.sameState(openNode)) {
        ans = true;
        old_node = openNode;
      }
    }
    return ans;
  }

  // select the next node
  private void selectNode() {
    
    Astar_height();
  }

  



  // change from search2
  private void branchAndBound() {
    Iterator i = open.iterator();
    RamblersState minCostNode = (RamblersState) i.next();
    while ( i.hasNext()) {
        RamblersState n = (RamblersState) i.next();
      if (n.getGlobalCost() < minCostNode.getGlobalCost()) {
        minCostNode = n;
        
      }
    }

    currentNode = minCostNode;
    open.remove(minCostNode);
  }

  // change from search1
  // report success - reconstruct path, convert to string & return
  private String reportSuccess() {

    RamblersState n = currentNode;
    StringBuffer buf = new StringBuffer(n.toString());
    int plen = 1;

    while (n.getParent() != null) {
      buf.insert(0, "\n");
      n = n.getParent();
      buf.insert(0, n.toString());
      plen = plen + 1;
    }

    System.out.println("=========================== \n");
    System.out.println("Search Succeeds");

    System.out.println("Efficiency " + ((float) plen / (closed.size() + 1)));
    System.out.println("Solution Path\n");
     
    return buf.toString();
  }
  //............Manhattan Distance..............................

public int Manhattan(Coords s,Coords g)
{
    int dx,dy,result;
dx=Math.abs(s.getx()-g.getx());
dy=Math.abs(s.gety()-g.gety());
result=dx+dy;
return result;
}
//...............................................................
//........Euclidean distance..........
public int Euclidean(Coords s,Coords g )
{
int dx,dy,result;
dx=Math.abs(s.getx()-g.getx());
dy=Math.abs(s.gety()-g.gety());
result=(int) Math.sqrt((double)(dx * dx + dy * dy));
return result;
}
//.....................................
//..............height difference.....
public int height(int s,int g )
{
return s-g;
}
//....................................
private void Astar_Manhattan() {
  Iterator i = open.iterator();
  RamblersState minCostNode = (RamblersState) i.next();
  while ( i.hasNext()) {
      RamblersState n = (RamblersState) i.next();
    if (n.getGlobalCost() +Manhattan(n.get_Point(), goal)< minCostNode.getGlobalCost()+Manhattan(minCostNode.get_Point(), goal)) {
      minCostNode = n;
      
    }
  }

  currentNode = minCostNode;
  open.remove(minCostNode);
}
//.......................
private void Astar_Euclidean() {
  Iterator i = open.iterator();
  RamblersState minCostNode = (RamblersState) i.next();
  while ( i.hasNext()) {
      RamblersState n = (RamblersState) i.next();
    if (n.getGlobalCost() +Euclidean(n.get_Point(), goal)< minCostNode.getGlobalCost()+Euclidean(minCostNode.get_Point(), goal)) {
      minCostNode = n;
      
    }
  }

  currentNode = minCostNode;
  open.remove(minCostNode);
}
//...............................
private void Astar_height() {
  Iterator i = open.iterator();
  RamblersState minCostNode = (RamblersState) i.next();
  while ( i.hasNext()) {
      RamblersState n = (RamblersState) i.next();
    if (n.getGlobalCost()+height(this.get_terrainmap().getTmap()[n.get_Point().gety()][n.get_Point().getx()], this.get_terrainmap().getTmap()[goal.gety()][goal.getx()]) < minCostNode.getGlobalCost()+height(this.get_terrainmap().getTmap()[minCostNode.get_Point().gety()][minCostNode.get_Point().getx()], this.get_terrainmap().getTmap()[goal.gety()][goal.getx()])) {
      minCostNode = n;
      
    }
  }

  currentNode = minCostNode;
  open.remove(minCostNode);
}

}

    

