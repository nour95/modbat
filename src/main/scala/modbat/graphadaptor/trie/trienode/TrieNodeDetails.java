package modbat.graphadaptor.trie.trienode;

import modbat.graph.Edge;

import java.util.Objects;

public class TrieNodeDetails<T extends Edge> implements TrieNode<T>
{
    private T data;
    private int level; // level
    private TrieNodeDetails<T> parent;
    private boolean visited;
    private int id;

    public TrieNodeDetails(T data, int level, TrieNodeDetails<T> parent, int id)
    {
        this.data = data;
        this.level = level;
        this.parent = parent;
        this.visited = false;
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TrieNode<T> getParent() {
        return parent;
    }

    public void setParent(TrieNodeDetails<T> parent) {
        this.parent = parent;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrieNodeDetails<?> trieNode = (TrieNodeDetails<?>) o;
        return Objects.equals(data, trieNode.data) && Objects.equals(level, trieNode.level) &&
                Objects.equals(parent, (trieNode.parent));
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, level, parent);
    }

    @Override
    public String toString()
    {
        String s = data.toString() + " at level " + level + " with parent: ";
        if (parent != null)
            s += parent.getData();
        else
            s += "null";

        return s;
    }
}
