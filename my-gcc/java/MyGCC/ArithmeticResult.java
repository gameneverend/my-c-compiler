package MyGCC;

public class ArithmeticResult<T> extends Expression{
  
	private T value;
  public int priority;

  public ArithmeticResult(OperationType op, T value){
    super();
    this.op = op;
    this.value = value;
    this.priority = 1;
  }
    
  public ArithmeticResult(OperationType op, Expression l, Expression r, int priority){
    super();
    this.op = op;
    this.left = l;
    this.right = r;
    this.priority = priority;
  }
    
  public T getValue(){
    return value;
  }

}
