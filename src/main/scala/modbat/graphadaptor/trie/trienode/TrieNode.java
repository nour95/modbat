package modbat.graphadaptor.trie.trienode;

public interface TrieNode<T>
{
    void setVisited(boolean visited);

    T getData();
    TrieNode<T> getParent();
    int getId();

    boolean isVisited();
    boolean isSynthetic();

}
