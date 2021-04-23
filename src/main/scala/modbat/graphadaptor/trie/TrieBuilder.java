package modbat.graphadaptor.trie;

import modbat.dsl.Transition;
import modbat.graph.Edge;
import modbat.graph.Graph;
import modbat.graph.Node;
import modbat.graphadaptor.EdgeData;
import modbat.graphadaptor.trie.Trie;
import modbat.graphadaptor.trie.trienode.TrieNode;

public class TrieBuilder<NT, D extends EdgeData>
{
    private Trie<Edge<NT, D>> trie;
    private int counter = 1;

    public TrieBuilder(boolean removeLoops)
    {
        this.trie = new Trie<>(removeLoops);
    }


    //todo have this as atatic method that take the type
    public void runBuilder(Graph<NT, D> graph, Edge<NT, D> initialEdgeWithoutHead, int depth)
    {
        // set a destination to this edge ..
        Node<NT> root = graph.getRoot();

        trie.addInitialEdge(initialEdgeWithoutHead);
        TrieNode<Edge<NT, D>> source = trie.getRoot();

        findAllPaths(graph, depth, 1, initialEdgeWithoutHead, source);
    }


    private void findAllPaths(Graph<NT, D> graph, int depth, int currentLevel, Edge<NT, D> parentEdge, TrieNode<Edge<NT, D>> parentTrieNode)
    {
        if(depth == 0)
            return;
        if(graph.isLeaf(parentEdge.getDestination())) //todo // and maybe move it to the edge class parentEdge.getDestination().isLeaf
            return;

        //LinkedList<Edge<T>> x = graph.edgesComingOutOfNode(parentEdge.getDestination());
        for (Edge<NT, D> outGoingEdge : graph.outgoingEdges(parentEdge.getDestination())) // todo and think where to put it
        {

            //Node<Edge<T>> childNode = new Node<Edge<T>>(outGoingEdge);
            if (outGoingEdge.getData().transition().isSynthetic())
                continue;

            TrieNode<Edge<NT, D>> childTrieNode = trie.createTrieNode(outGoingEdge, currentLevel, parentTrieNode, counter);
            if (childTrieNode == null)
                continue;

            counter++;

            trie.add(childTrieNode, parentTrieNode);
            findAllPaths(graph, depth - 1, currentLevel + 1, outGoingEdge, childTrieNode);

        }

    }



    public Trie<Edge<NT, D>> getTrie()
    {
        return trie;
    }


}
