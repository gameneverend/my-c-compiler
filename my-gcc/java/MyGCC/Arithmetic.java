package MyGCC;

public class Arithmetic<T> extends Expression{
  
	private T value;

  public Arithmetic(OperationType op, T value){
    super();
    this.op = op;
    this.value = value;
  }
    
  public Arithmetic(OperationType op, Expression l, Expression r){
    super();
    this.op = op;
    this.left = l;
    this.right = r;
  }
    
  public T getValue(){
    return value;
  }

}
