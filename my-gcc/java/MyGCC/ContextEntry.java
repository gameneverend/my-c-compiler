package MyGCC;

public class ContextEntry {
  public Type type;
  public int arraySize;
  
  public ContextEntry(Type t, int as) {
    this.type = t;
    this.arraySize  = as;
  }
}
