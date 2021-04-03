package modbat.mbt

import java.io.{File, FileOutputStream, IOException, PrintStream}

import scala.collection.JavaConverters.{asScalaBufferConverter, asScalaSetConverter}
import modbat.dsl.{State, Transition}
import modbat.graph.{Edge, Graph, Node, TrieBuilder}
import modbat.graph.trie.Trie
import modbat.graph.trie.trienode.TrieNode
import modbat.graphadaptor.{EdgeData, ExpectedExceptionTransition, NormalTransition, StateData, TransitionType}

import scala.collection.mutable
import scala.collection.mutable.Set;

class IterativeDepthSearch(graph: Graph[StateData, EdgeData], firstModelInstance: ModelInstance, config: Configuration) //todo object or class
{

  var trie : Trie[Edge[StateData, EdgeData]] = _;
  var currentTrieNode: TrieNode[Edge[StateData, EdgeData]] = _;
  var leafReached = false;

  var rootIsMarked = false;


  initializeExhaustiveTrie(graph, firstModelInstance, config)

  def initializeExhaustiveTrie(graph :  Graph[StateData, EdgeData], firstModelInstance: ModelInstance, config : Configuration): Unit =
  {
    val depth = config.depth;
    val removeLoops = config.removeLoops;


    val trieBuilder : TrieBuilder[StateData, EdgeData] = new TrieBuilder(removeLoops)
    val initEdge = new Edge[StateData, EdgeData](null, graph.getRoot) //todo this guy has null
    trieBuilder.runFinder(graph, initEdge, depth)
    trie = trieBuilder.getTrie;
    firstModelInstance.exhaustiveTrie = trie;

    currentTrieNode = trie.getRoot
    moveOnce() // get first real edge to return
  }



  /*
  4 cases to the return:

  1- if the path in the original graph continue and in the trie continue   (normal case, move once will find first unvisited trieNode)
  2- if the path in the original graph continue but in the trie this is the leaf

  3- if the path in the trie graph end and in the trie end
  4- if the path in the trie graph end and in the trie continue (should never happen)


  - and we have the cases where the root is marked as visited or unvisited

   */




  def getCurrentTransition(): Transition = //todo if this is a leaf and leaf already visitied return null.
  {
    if (trie.getRoot.isVisited == true) {
      rootIsMarked = true;
      return null
    };

    if (leafReached == true)
    {
      currentTrieNode =  trie.getRoot;
      moveOnce()
      leafReached = false;
      return null;  // todo make sure null here will not backtrack.
    }

    return currentTrieNode.getData.getData.transition
  }

  def restartFromRoot(): Unit =
  {
    leafReached = false
    trie.markVisitedUpToTheFirstParentWithUnvisitedNode(currentTrieNode);
    currentTrieNode =  trie.getRoot;
    moveOnce()
  }


  def moveOnce(): Unit =
  {
    val child = trie.findUnvisitedNode(currentTrieNode)

    if (child == null) {
      leafReached = true
      trie.markVisitedUpToTheFirstParentWithUnvisitedNode(currentTrieNode);
      // todo set the current to be the root again
    };
    else {

      currentTrieNode = child;
    }


  }


  /*
    1- we need to make null != backtrack.  How to distinguish the backtracking things?
    2- if the returned transistion from here didn't match any of the thranisistion in the makechoice transition list
          then we have maybe dicovered a choice transition => run random search ?? (and increase depth??)
      */


  /*
  val c1 : TrieNode[Edge[StateData, EdgeData]] = currentTrieNode;
    val c2 : Edge[StateData, EdgeData]           = currentTrieNode.getData
    val c3 : EdgeData                            = currentTrieNode.getData.getData
    val c4 : Transition                          = currentTrieNode.getData.getData.transition
   */


  /**
    * Print the graph represented by this GraphAdapter to a file of the given name.
    */
  def printTrieTo(fileName: String): Unit = {
    val out: PrintStream = getPrintStream(fileName)
    out.println("digraph model {")
    //    out.println("  orientation = landscape;")
    out.println("  graph [ rankdir = \"TB\", ranksep=\"0.4\", nodesep=\"0.2\" ];")
    out.println("  node [ fontname = \"Helvetica\", fontsize=\"12.0\"," +
      " margin=\"0.07\" ];")
    out.println("  edge [ fontname = \"Helvetica\", fontsize=\"12.0\"," +
      " margin=\"0.05\" ];")
    out.println("  {")
    //out.println("    node [ shape = \"box\"]")
    out.println("    node []")



    val allNodesInTrie: mutable.Set[TrieNode[Edge[StateData, EdgeData]]] = trie.getAllNodes.asScala

    for (parent <- allNodesInTrie)
    {
      //out.println("     \"" + parent.getId() + "\" [label= \"" + parent.toString.replace('\"', '\'') + "\"]")

      val buf = new StringBuffer(
        "     \"" + parent.getId() + "\" [label= \"" + parent.toString.replace('\"', '\'') + "\"")

      if(trie.isLeaf(parent)){
        buf.append(" color = \"red\"")
      }

      buf.append("]")

      out.println(buf.toString)

    }

    out.println("  }")
    out.println()



    for(parent <- allNodesInTrie)
    {

      val children: mutable.Buffer[TrieNode[Edge[StateData, EdgeData]]] = trie.getChildren(parent).asScala

      for(child <- children)
      {
        out.println("  \"" + parent.getId() + "\" -> " + "\"" + child.getId() + "\";")

      }
    }


    out.println("}")
    out.flush()
    out.close()
  }





  private val log = firstModelInstance.mbt.log //todo why not??

  private def getPrintStream(outputFileName: String): PrintStream = {
    val pathOfOutputFile: String = config.dotDir + File.separatorChar + outputFileName
    try {
      new PrintStream(new FileOutputStream(pathOfOutputFile), false, "UTF-8")
    } catch {
      case ioe: IOException =>
        log.error("Cannot open file " + pathOfOutputFile + ":")
        log.error(ioe.getMessage)
        throw ioe
    }
  }












  /*
  // solution to a data structure that can extend with time is :
    1- run exhuastive search mutliple time until the number of the nodes in the graph don't change?
    2- implement a super an un effeicient allgorithm that each time we dicovered some guys


  */



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
//    }
//
//  }


// todo if we reach the depth but not the leaf => continues with random search??




}
