package MyGCC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class Context{

	protected Context inheritedContext;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();

	/** Contain all the variables local to the context with their type **/
	protected HashMap<String,Type> localVariables = new HashMap<String,Type>();
  
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
		// searching in Local Variables
		Integer result = variablesLocations.get(name);
		if (result != null) return result.intValue() + "(" + Register.RBP + ")";// TO PERFECT
		if (inheritedContext != null) return inheritedContext.getVariableLocation(name);
		throw new Exception("No parameter with the specified name : <" + name + "> found");
	}

	/**
	 * When a function is called,
	 * local variables will always be used like <i>-...(%rbp)</i>
	 * We use a mapping in order to get the track of all this location
	 */
	public void prepareLocalVariablesLocation(){
		if (localVariablesLocated) return;	
		variablesTotalSize = 0;
		for (Entry<String, Type> e : localVariables.entrySet()){
			variablesTotalSize += e.getValue().size;
			variablesLocations.put(e.getKey(), -variablesTotalSize);
		}
		variablesTotalSize = variablesTotalSize + (16 -variablesTotalSize % 16);
		stackPosition = -variablesTotalSize;
		localVariablesLocated = true;
	}

	public void addVariable(Type type, String identifier, int arraySize) {
    if(arraySize == 0)
      localVariables.put(identifier, type);
    else
      for(int i = 0; i < arraySize; i++) // This should work, but I am in no way happy with it.
        localVariables.put(new String(identifier + '[' + i + ']'), type);
		localVariablesLocated = false;
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
