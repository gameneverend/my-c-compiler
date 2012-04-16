package MyGCC;

import java.util.Stack;

public abstract class Expression{
  
    public Expression left;
    public Expression right;
    public OperationType op;
    
    public Expression(){
      this.left = null;
      this.right = null;
      this.op = null;
    }
    
    /**
     * Checks if the expression is composed exclusively of numeric values
     **/
    public boolean isFullyNumeric(){
			boolean bl = true;
			boolean br = true;
			Arithmetic tmp = null;
			
			if(this.left != null){
				if(this.left instanceof Arithmetic)
					bl = this.left.isFullyNumeric();
				else
					return false;
			}
			
			if(this.right != null){
				if(this.right instanceof Arithmetic)
					br = this.right.isFullyNumeric();
				else
					return false;
			}
			
			if(this instanceof Arithmetic)
				tmp = (Arithmetic)this;
			else
				return false;
				
			if(tmp.getValue() instanceof String)
				return false;
				
			return bl && br;
		}
		
		/**
		 * Generates a String of the numeric expression.
		 **/
		public String toNumeric(){
			StringBuffer sb = new StringBuffer();
			Arithmetic tmp = (Arithmetic)this;
			if(this.op != null){
				Arithmetic lft = (Arithmetic)this.left;
				sb.append(lft.getValue().toString());
				
				while(tmp.op != null){
					if(tmp.right.left != null){
						sb.append(" " + tmp.op.toString() + " " +((Arithmetic)tmp.right.left).getValue());
						tmp = (Arithmetic)tmp.right;
					}
					else
						sb.append(" " + tmp.op.toString() + " " + ((Arithmetic)tmp.right).getValue());
						break;
				}
				return sb.toString();
			}
			return (tmp.getValue()).toString();
		}
    

    
    public StringBuffer handleExpression(Arithmetic a, Context context) throws Exception{
      StringBuffer sb = new StringBuffer();
				
      if(this.left != null)
        sb.append(this.left.handleExpression(a, context));
				
      if(this.right!= null)
        sb.append(this.right.handleExpression(a, context));
				
        
      if(this instanceof Arithmetic){
        System.out.println("Arithmetic caught");
        sb = StringManipulator.handleArithmetic(sb, (Arithmetic)this, context);
        sb.append("\tmovl " + sb.substring(sb.lastIndexOf("%")).replace("\n","") + ", %" + context.getVariableLocation(String.valueOf(a.getValue())) + "\n"); 
        //FIXME find a more suitable way to get a hand on the last register used
      }

      if(this instanceof FunctionCall){
        System.out.println("Function-call caught");
        sb = StringManipulator.handleFunctionCall(sb, (FunctionCall)this, context);
        sb.append("\tmovl %eax, %" + context.getVariableLocation(String.valueOf(a.getValue())) + "\n"); 
      }
        
      if(this.op != null){
        System.out.println("Operation handling");
        sb = StringManipulator.handleOperation(sb, this.op, this.left, this.right, context);
        sb.append("\tmovl " + sb.substring(sb.lastIndexOf("%")).replace("\n","") + ", %" + context.getVariableLocation(String.valueOf(a.getValue())) + "\n"); 
      }
      
      return sb;
    }
    
}
