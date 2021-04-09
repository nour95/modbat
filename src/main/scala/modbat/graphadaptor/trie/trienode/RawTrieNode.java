package modbat.graphadaptor.trie.trienode;

import java.util.Objects;

public class RawTrieNode<T> implements TrieNode<T>
{

    private T data;
    private int level; // level
    private RawTrieNode<T> parent;
    private boolean visited;
    private int id;
    private boolean isSynthetic;

    public RawTrieNode(T data, int level, RawTrieNode<T> parent, int id, boolean isSynthetic)
    {
        this.data = data;
        this.level = level;
        this.parent = parent;
        this.visited = false;
        this.id = id;
        this.isSynthetic = isSynthetic;

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public TrieNode<T> getParent() {
        return parent;
    }

    public int getId() { return id; }

    public boolean isSynthetic() { return isSynthetic; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawTrieNode<?> trieNode = (RawTrieNode<?>) o;
        return Objects.equals(data, trieNode.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString()
    {
        String synthetic = isSynthetic ? " (Synthetic)" : "";
        String s = data.toString() + synthetic + " at level " + level + " with parent: ";
        if (parent != null)
            s += parent.getData();
        else
            s += "null";

        return s;
    }
}
