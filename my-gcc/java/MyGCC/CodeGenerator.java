package MyGCC;

import java.util.Collections;
import java.util.Stack;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.io.*;

public class CodeGenerator{

  private LinkedList<Stack<Object>> myStack;
  private LinkedList<Prototype> globalPrototypes = new LinkedList<Prototype>();
  private LinkedList<Function> globalFunctions = new LinkedList<Function>();

  private Context globalContext;
  private Context actualContext;
  private Function currentFunction;
  private Block currentBlock = null;
  private Stack<Expression> args;

  public CodeGenerator(){
    myStack = new LinkedList<Stack<Object>>();
    args = new Stack();
  }
  
  public void pushArgument(Expression e){
		args.push(e);
	}
	
	public ArrayList<Expression> getArguments(){
		ArrayList<Expression> lst = new ArrayList<Expression>();
		while(!args.empty())
			lst.add(args.pop());
		return lst;
	}
  
  public void pushInstruction(Instruction i){
    System.out.println("Pushing instruction to function: " + currentFunction.name);
    if(currentFunction == null)
      System.err.println("ERROR: currentFunction is null");
      
    this.currentBlock.pushInstruction(i);
  }

  public void pushInformation(Object o){
    if(o == null)
      System.err.println("Pushing NULL object");
      
    if (myStack.size() == 0)
      openNewContext();
    myStack.getLast().push(o);
  }

  public void openNewContext(){
    myStack.add(new Stack<Object>());
  }

  @SuppressWarnings("unchecked")
	public void declarePrototype(){
    Type returnType = null;
    String name = null;
    ArrayList<Type> parameters = new ArrayList<Type>();

    Stack<Object> tmpStack = myStack.getLast();
    while (!tmpStack.isEmpty()){
      ParsingResult r = (ParsingResult) tmpStack.pop();
      switch (r.type){
        case TYPE :        returnType = ((ParsingResult<Type>)r).getValue();          break;
        case ID:           name       = ((ParsingResult<String>)r).getValue();        break;
        case PARAMETER :   parameters.add(((ParsingResult<Type>)r).getValue());       break;
      }
    }
    Collections.reverse(parameters);
    System.out.println("Declaring a prototype with :"
                       + "\n\tReturnType : " + returnType
                       + "\n\tname : " + name);
    //globalPrototypes.add(new Prototype(
  }

  @SuppressWarnings("unchecked")
	public void startFunctionDefinition(){
    System.out.println("Starting function definition");
    Type returnType = null;
    String name = null;
    ArrayList<Parameter> parameters = new ArrayList<Parameter>();
    Stack<Object> tmpStack = myStack.getLast();
    while (!tmpStack.isEmpty()){
      ParsingResult r = (ParsingResult) tmpStack.pop();
        
      switch (r.type){
        case TYPE :        returnType = ((ParsingResult<Type>)r).getValue();          break;
        case ID:           name       = ((ParsingResult<String>)r).getValue();        break;
        case PARAMETER :   parameters.add(((ParsingResult<Parameter>)r).getValue());  break;
      }
    }
    Collections.reverse(parameters);
    Function f = new Function(name, returnType, parameters);
    globalFunctions.add(f);
    this.currentFunction = f;
    this.currentBlock = f.body.mainBlock;
  }
  
  public void startBlockDefinition() {
    Block b = new Block();
    b.parent = this.currentBlock;
    this.currentBlock = b;
  }
  
  public void closeBlockDefinition() {
    this.currentBlock = this.currentBlock.parent;
  }


  @SuppressWarnings("unchecked")
	public void declareVariable(){
    Type type = null;
    String identifier = null; 
    int arraySize = 0;
    Stack<Object> tmpStack = myStack.getLast();
    while(!tmpStack.isEmpty()) {
      ParsingResult r = (ParsingResult) tmpStack.pop();
      switch(r.type) {
        case TYPE : 
          type = ((ParsingResult<Type>)r).getValue();
          break;
        case ID : 
          identifier = ((ParsingResult<String>)r).getValue();
          break;
        case VARIABLE :
          arraySize = ((ParsingResult<Integer>)r).getValue();
          break;
        default:
          System.err.println("Unexpected Type :" + r.type);
      }
    }
    if(currentFunction != null) {
    	currentFunction.addDeclaration(type,identifier, arraySize);
      //this.currentFunction.body.declarations.add(new Declaration(type, identifier, arraySize));
    }
  }

  public String generateCode(){
    StringBuffer sb = new StringBuffer();
    for (Function f : globalFunctions){
      sb.append("\n" + f.toString() + "\n");
    }
    return sb.toString();
  }
  
  public void closeFunction(){
    //TODO
  }

}
