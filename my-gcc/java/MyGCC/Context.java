package MyGCC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class Context{

  protected Context inheritedContext;
  public ArrayList<Parameter> parameters = new ArrayList<Parameter>();

  /** Contain all the variables local to the context with their type **/
  protected HashMap<String,ContextEntry> localVariables = new HashMap<String,ContextEntry>();
  
  /** Contain the "virtual" location of the variables,
   *  for example: if <i>a</i> is in <i>-4(%rbp)</i>, the tuple will be <i>(a,-4)</i> **/
  protected HashMap<String,Integer> variablesLocations = new HashMap<String, Integer>();
  
  /** This is the place taken by all the local variables in bytes,
   *  Like with gcc, it is always a multiple of 16.
   **/
  protected int variablesTotalSize;
  
  /** useful for a lazy-mechanism **/
  protected boolean localVariablesLocated = false;
  
  protected int stackPosition = 0;

  public Context(){
  }

  public Context(Context inheritedContext){
    this.inheritedContext = inheritedContext;
  }

  public String getVariableLocation(String name) throws Exception{
		ContextEntry ce = localVariables.get(name);
    Integer result = variablesLocations.get(name);
		
    // searching in Local Variables
    if (result != null) {
      if(ce.arraySize == 0) {
        if(Parser.DEBUG) System.out.println("Found variable : " + name);
        return result.intValue() + "(" + Register.RBP + ")";// TO PERFECT
      }
      return result.intValue() + "(" + Register.RBP + ", " + Register.RAX + ", " + ce.type.size + ")";
    }
    if (inheritedContext != null) {
      return inheritedContext.getVariableLocation(name);
     }
    throw new Exception("No parameter with the specified name : <" + name + "> found");
  }
  
  
  public String getArrayLocation(String name) throws Exception{
		ContextEntry ce = localVariables.get(name);
    
		for(Parameter p : parameters){
			if(p.name.equals(name) && p.arraySize != 0){
				return "(" + Register.RCX + ", " + Register.RDX + ", " + ce.type.size + ")";
			}
		}
    throw new Exception("No parameter with the specified name : <" + name + "> found");
  }
  
  public boolean isArrayArgument(String name) throws Exception{
		for(Parameter p : parameters){
			if(p.name.equals(name))
				return !(p.arraySize == 0);
		}
		return false;
	}

  public boolean isArrayType(String name) throws Exception{
		for(Parameter p : parameters){
			if(p.name.equals(name))
				return !(p.arraySize == 0);
		}
		
		ContextEntry ce = localVariables.get(name);
		Integer result = variablesLocations.get(name);
		if(result != null)
			return !(ce.arraySize == 0);
				
		if(inheritedContext != null)
			return inheritedContext.isArrayType(name);
		throw new Exception("No parameter with the specified name: <" + name + "> found");
	}
  /**
   * When a function is called,
   * local variables will always be used like <i>-...(%rbp)</i>
   * We use a mapping in order to get the track of all this location
   */
  public void prepareLocalVariablesLocation(){
    if (localVariablesLocated) return;  
    variablesTotalSize = 0;
    for (Entry<String, ContextEntry> e : localVariables.entrySet()){
      ContextEntry ce = e.getValue();
      int i = ce.arraySize;
      if(i == 0)
        i++;
      variablesTotalSize += i * ce.type.size;
      variablesLocations.put(e.getKey(), -variablesTotalSize);
    }
    variablesTotalSize = variablesTotalSize + (16 -variablesTotalSize % 16);
    stackPosition = -variablesTotalSize;
    localVariablesLocated = true;
  }

  public void addVariable(Type type, String identifier, int arraySize) {
    localVariables.put(identifier, new ContextEntry(type, arraySize));
    localVariablesLocated = false;
  }
  
  public int getVariableLocalSize(){
    return variablesTotalSize;
  }
  

  public String virtualPush(String s){
    return "\t" + Assembly.MOV + "\t" + s +", " + (stackPosition -= 8) + "(" + Register.RBP + ")\n";
  }
  
  public String virtualPop(String s){
    int tmp = stackPosition;
    stackPosition += 8;
    return "\t" + Assembly.MOV + "\t" + tmp + "(" + Register.RBP + "), " + s + "\n";
  }

}
