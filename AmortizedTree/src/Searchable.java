public interface Searchable {
    boolean add(String key);
    boolean find(String key);
    boolean remove(String key);
    int size();
    boolean rebalance();
    boolean rebalanceValue(String key);
}
