package modbat.graph.trie;

import modbat.graph.Edge;
import modbat.graph.Node;
import modbat.graph.trie.trienode.TrieNode;
import modbat.graph.trie.trienode.RawTrieNode;
import modbat.graph.trie.trienode.TrieNodeDetails;

import java.util.*;
import java.util.stream.Collectors;

public class Trie<T> //todo need dotify to this too
{
    private TrieNode<T> root;
    private Map<TrieNode<T>, LinkedList<TrieNode<T>>> neighbours = new HashMap<>();
    private final boolean removeLoops;

    // the standard case will be to: visit the loop only once if it is a normal loop or self loop
    public Trie()
    {
        this(true);
    }

    public Trie(boolean removeLoops)
    {
        this.removeLoops = removeLoops;
    }

    public void addInitialEdge(T initDataWithoutHead)
    {
        root = createTrieNode(initDataWithoutHead, 0, null, 0);
    }


    public void add(TrieNode<T> newNode, TrieNode<T> parentNode)
    {
        LinkedList<TrieNode<T>> parentsAllChildren = neighbours.get(parentNode);

        if (parentsAllChildren == null) {
            System.out.println("That is wired");
            return;
        }

        if (!parentsAllChildren.contains(newNode))
            parentsAllChildren.add(newNode);

    }


    public TrieNode<T> createTrieNode(T newNode, int currentLevel, TrieNode<T> parentTrieNode, int id)
    {
        TrieNode<T> trieNode;
        if(removeLoops)
            trieNode = new RawTrieNode<T>(newNode, currentLevel, (RawTrieNode<T>) parentTrieNode, id);
        else
            trieNode = new TrieNodeDetails<T>(newNode, currentLevel, (TrieNodeDetails<T>) parentTrieNode, id);

        if (!neighbours.containsKey(trieNode))
            neighbours.put(trieNode, new LinkedList<TrieNode<T>>());
        return trieNode;
    }

    public TrieNode<T> getRoot() {
        return root;
    }



    //---------------------------------

    // methods for modbat:
    public LinkedList<TrieNode<T>> getChildren(TrieNode<T> parent)
    {
        return neighbours.get(parent);
    }


    public void markAsVisit(TrieNode<T> node2)
    {
        TrieNodeDetails<T> node = (TrieNodeDetails) node2; //todo

        if (isLeaf(node))
        {
            node.setVisited(true); // set the leaf

            // set the leaf's parent:
            TrieNode<T> parent = node.getParent();


        }
    }

    public boolean hasUnVisitedChildren(TrieNode<T> node)
    {
        // a more efficient way is store the number of unvisited children in the trieNode (-1 if unknown to be calculated later), 0 if no more unvisited children
        // and decrese the number each time we set a leaf. and so on until we reach the source
        return false;
    }

    public boolean isLeaf(TrieNode<T> node)
    {
        return  (neighbours.get(node) == null) || neighbours.get(node).isEmpty(); //todo not sure about the null thing
    }


    public TrieNode<T> findUnvisitedNode(TrieNode<T> parent)
    {
        LinkedList<TrieNode<T>> childrenList =  neighbours.get(parent);

        for (TrieNode<T> node : childrenList)
        {
            if (! node.isVisited())
                return node;
            //todo else remove from list ?? increase effeicincy

        }


        return null;
    }


    public void markVisitedUpToTheFirstParentWithUnvisitedNode(TrieNode<T> child)
    {
        child.setVisited(true);

        if (child == root)
        {
            root.setVisited(true);    //todo return false instead??
            return;
        }

        for (TrieNode<T> currentNode = child.getParent() ; findUnvisitedNode(currentNode) == null && currentNode.getParent() != null; currentNode = currentNode.getParent() )
        {
            currentNode.setVisited(true);
        }

        //int x = 5;
        //System.out.println(x);
    }


    public Set<TrieNode<T>> getAllNodes() {
        return neighbours.keySet();
    }

    public List<TrieNode<T>> getAllEdges() {
        return neighbours.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }










    @Override
    public String toString()
    {

        StringBuilder sb = new StringBuilder();
        Set<TrieNode<T>> keys = neighbours.keySet();

        LinkedList<TrieNode<T>> list;
        for (TrieNode<T> key : keys)
        {
            sb.append("children of '" + key + "' are : \n");
            list = neighbours.get(key);
            if (list == null || list.size() == 0) {
                sb.append("list is empty \n");
            }

            for (TrieNode<T> child : list)
            {
                sb.append(child.toString() + "\n");
            }

            sb.append("************\n");


        }

        return sb.toString();

    }
}

