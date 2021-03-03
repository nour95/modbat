package modbat.mbt

import scala.collection.mutable.Set;

object IterativeDepthSearch
{
  var initialize = false;
  var done = Set[String](); //todo make it mutable node1, node2, node3
  var currentPath = "1, 1, 2"  // because the working list will have multiple different paths so we need to store the path that I am working on right now
  var indexInCurrentPath = 0  // because we can only return one node at a time so we need to know wwhere we are in the working list path (if we are in node1 or 2, 3,...etc)

  var workList = Set[String]();  //make it a set of reference to transition instead names or ids
  var currentDepth = 0;




}
