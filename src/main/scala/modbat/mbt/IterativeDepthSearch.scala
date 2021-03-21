package modbat.mbt

import modbat.dsl.Transition
import modbat.dsl.State
import trie.Trie
import generator.Finder
import modbat.graphadaptor.{EdgeData, StateData}
import trie.node.TrieNode

import scala.collection.mutable.Set;

object IterativeDepthSearch
{

  var initialize = false;
  var trie : Trie[EdgeData] = null;


  var done = Set[Transition](); //todo make it mutable node1, node2, node3
  var currentPath = "1, 1, 2"  // because the working list will have multiple different paths so we need to store the path that I am working on right now
  var indexInCurrentPath = 0  // because we can only return one node at a time so we need to know wwhere we are in the working list path (if we are in node1 or 2, 3,...etc)

  var workList = Set[Transition]();  //make it a set of reference to transition instead names or ids
  var currentDepth = 0;

// EdgeData has the transition as a parameter (read the first line)

  // todo with George later find a way to have the graph,
  //  initial edge without head can be done easily here or in somewhere else,
  // todo maybe dummy state
//  def initializeTrie(trieType: Boolean ) =
//  {
//    if (initialize == false)
//    {
//      var finder: Finder[StateData, EdgeData]  = new Finder(trieType)
//      trie = finder.runFinder()
//
//      initialize = true;
//    }
    // else log ?? todo
//  }


  /*
  // solution to a data structure that can extend with time is :
    1- run exhuastive search mutliple time until the number of the nodes in the graph don't change?
    2- implement a super an un effeicient allgorithm that each time we dicovered some guys


  */

  def generatePathFromCurrentTransistion(transition: Transition) =
  {
    workList.add(transition);
    // find this transition children and then add them to the worklist. change wroklit to be list of pathes that have transitions
    //....

  }

//  def markPath() =
//  {
//    //todo what should I do with the rawTrieNode (have a parent inside without using it feels duplication? merge the equal method??)
//    var currentEdge : TrieNode[EdgeData] = trie.getRoot;
//
//    while(currentEdge.isVisited() != true )
//    {
//      if(trie.isLeaf(currentEdge))
//      {
//        // mark this and all the parent classes
//        trie.markVisitedUpToTheFirstParentWithUnvisitedNode(currentEdge); // todo this need to mark the transition int he graph too
//      }
//
//      else
//      {
//        currentEdge = trie.findUnvisitedNode(currentEdge);
//       if (currentEdge == null)
//         throw Exception("something wrong happened ");
//
//      }
//
//
//
//    }


//  }




}
