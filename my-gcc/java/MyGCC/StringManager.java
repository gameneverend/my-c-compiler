package MyGCC;


public class StringManager {
  private HashMap<String, String> contents;
  
  public StringManager() {
    this.contents = new HashMap<String, String>();
  }

  public String addString(String string) {
    if(string != null)
      if(this.contents.containsKey(string))
        return this.contents.get(string);
      else {
        String label = LabelManager.getStringLabel();
        this.contents.put(string, label);
        return label;
      }
  }
  
  public HashMap<String, String> getContents() {
    return this.contents.clone();
  }
}
