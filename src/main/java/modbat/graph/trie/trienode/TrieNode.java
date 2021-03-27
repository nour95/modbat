package modbat.graph.trie.trienode;

public interface TrieNode<T>
{
    void setVisited(boolean visited);

    T getData();
    TrieNode<T> getParent();
    int getId();

    boolean isVisited();


    //todo have get parent too
}
