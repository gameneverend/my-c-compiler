package MyGCC;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalContext extends Context{
  
  public GlobalContext() {
    this.inheritedContext = null;
  }
  
  public String getVariableLocation(String name) throws Exception{
    // searching in Local Variables
    ContextEntry ce = localVariables.get(name);
    if (!(ce == null)) {
      if(ce.arraySize == 0) {
        return name + "(" + Register.RIP + ")";// TO PERFECT
      }
      return name + "(" + Register.RIP + ", " + Register.RAX + ", " + ce.type.size + ")";
    }
    throw new Exception("No parameter with the specified name : <" + name + "> found");
  }
  
   public String makeLabels() {
    StringBuffer sb = new StringBuffer();
    for(String c : localVariables.keySet()) {
      ContextEntry ce = localVariables.get(c);
      int as = ce.arraySize;
      if(as == 0)
        as++;
      as *= ce.type.size;
      sb.append("\t.comm " + c + "," + as +"," + as + "\n");
    }
    return sb.toString();
  }
}
