package modbat.graphadaptor

import graph.Graph
import graph.Node
import modbat.dsl.Transition
import modbat.mbt.{Configuration, ModelInstance}

class GraphAdaptor(val config: Configuration, val model: ModelInstance) {
  // log that could be used for debugging purposes
  // private val log = model.mbt.log

  /*
     "graph" is a public field representing the model instance.
     - "graph" has root "model.initialState".
     - "graph" has an initial number of nodes of "model.states.size" which is allowed
       to grow by adding more states and edges.
   */
  val graph: Graph[StateData, EdgeData] = new Graph(new Node(new StateData(model.initialState)), model.states.size)

  // call createGraph (which is the starting point for creating a graph representation of the model)
  createGraph()

  /**
    * Get transition label of "transition" given the configuration and
    * the model instance of this graph adaptor.
    */
  private def getTransitionLabel(transition: Transition): String = {
    if (!config.autoLabels && transition.action.label.isEmpty) {
      ""
    } else if (transition.action.transfunc != null) {
      transition.ppTrans(config.autoLabels) // String
    } else {
      ""
    }
  }

  /**
    * Add parentheses around label "(label)".
    */
  private def addParens(label: String): String = {
    if (label.equals("")) {
      ""
    } else {
      "(" + label + ")"
    }
  }

  /**
    * Create (and return) edge data for "transition" whether it is a normal transition
    * or an expected exception transition.
    */
  private def createEdgeData(transition: Transition): EdgeData = {
    val transitionLabel: String = getTransitionLabel(transition)
    if (transition.expectedExceptions.isEmpty) {
      new EdgeData(
        transitionLabel = transitionLabel,
        transitionType = NormalTransition(isDeterministic = true),
        transition = transition
      )
    } else {
      val transitionLabel = transition.expectedExceptions.mkString(", ")
      new EdgeData(
        transitionLabel = transitionLabel,
        transitionType = ExpectedExceptionTransition(isDeterministic = true),
        transition = transition
      )
    }
  }

  /**
    * This method is the entry point for this graph adapter to create
    * a graph given a model instance and a configuration.
    */
  private def createGraph(): Unit = {
    for (transition <- model.transitions) {
      if (!transition.isSynthetic) {
        // create edges data for deterministic "transition" whether it is a normal transition
        // or an expected exception transition
        val transitionEdgeData = createEdgeData(transition)
        // add edge data to graph (that goes from origin state to destination state)
        graph.addEdge(transitionEdgeData.originState, transitionEdgeData.destinationState, transitionEdgeData)

        // create edge data for all nextIf (deterministic) transitions and maybeNextif (non-deterministic) transitions
        for (nextStatePredicate <- transition.nextStatePredicates) {
          // ask Cyrille about label to override: ( "original transition label" )
          val overridingTransitionEdgeData = new EdgeData(
            transitionLabel = addParens(transitionEdgeData.transitionLabel),
            transitionType = NormalTransition(isDeterministic = !nextStatePredicate.nonDet),
            transition = nextStatePredicate.target // transition
          )

          // add edge data to graph (that goes from origin state to destination state)
          graph.addEdge(
            overridingTransitionEdgeData.originState,
            overridingTransitionEdgeData.destinationState,
            overridingTransitionEdgeData
          )
        }

        // create edge data for all non-deterministic exception transitions
        for (nonDeterministicException <- transition.nonDetExceptions) {
          val nonDeterministicExceptionTransition_EdgeData = new EdgeData(
            transitionLabel = nonDeterministicException.exception.toString(),
            transitionType = ExpectedExceptionTransition(isDeterministic = false),
            transition = nonDeterministicException.target // transition
          )

          // add edge data to graph (that goes from origin state to destination state)
          graph.addEdge(
            nonDeterministicExceptionTransition_EdgeData.originState,
            nonDeterministicExceptionTransition_EdgeData.destinationState,
            nonDeterministicExceptionTransition_EdgeData
          )
        }
      }
    }
  }
}
