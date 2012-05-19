package MyGCC;

import java.util.ArrayList;
import java.util.HashMap;

public class StaticContext extends Context {
  
  private ArrayList<Integer> ids = new ArrayList<Integer>();
  private HashMap<String, Integer> pairing = new HashMap<String, Integer>();
  
	public StaticContext() {
    this.inheritedContext = null;
  }
  
  public String getVariableLocation(String name) throws Exception{
    // searching in Local Variables
    ContextEntry ce = localVariables.get(name);
    if (!(ce == null)) {
      
      if(!this.pairing.containsKey(name))
        this.pairing.put(name, generateId());
      int num = this.pairing.get(name);
      if(ce.arraySize == 0) {
        return name + "." + num + "(" + Register.RIP + ")";// TO PERFECT
      }
      return name + "." + num  + "(" + Register.RIP + ", " + Register.RAX + ", " + ce.type.size + ")";
    }
    throw new Exception("No parameter with the specified name : <" + name + "> found");
  }
  
  public String makeLabels() {
    StringBuffer sb = new StringBuffer();
    for(String c : localVariables.keySet()) {
      ContextEntry ce = localVariables.get(c);
      Integer id = this.pairing.get(c);
      int as = ce.arraySize;
      if(as == 0)
        as++;
      as *= ce.type.size;
      sb.append("\t.local " + c + "." + id +"\n");
      sb.append("\t.comm " + c + "." + id + "," + as +"," + as + "\n");
    }
    return sb.toString();
  }
  
  public int generateId() {
    int id;
    if(this.ids.isEmpty())
      id = 0;
    else
      id = this.ids.get(this.ids.size() - 1);
    id++;
    this.ids.add(new Integer(id));
    return id;
  }
}
