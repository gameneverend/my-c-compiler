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
    
    
    public StringBuffer handleExpression(Context context) throws Exception{
			StringBuffer sb = new StringBuffer();
			
			if(this.isFullyNumeric()){
        System.out.println("Fully numeric found");
				//TODO get the corresponding register for this evaluation (always eax?)
        //TODO should add to register manager.
				return sb.append("\tmovl %" + StringManipulator.calculateNum(this) + ", %TMP\n");
			}
			
			else{
          sb = this.handleMe(context);
          return sb;
      }
		}

    
    public StringBuffer handleMe(Context context) throws Exception{
      StringBuffer sb = new StringBuffer();
      String lval = new String();
			String rval = new String();
      Expression lexp = this.left;
      Expression rexp = this.right;
      Arithmetic myLeft = null;
      Arithmetic myRight = null;
      FunctionCall f1 = null;
      FunctionCall f2 = null;
      Register regL = null;
      Register regR = null;
				

      if(lexp instanceof Arithmetic){
        myLeft = (Arithmetic)lexp;
        lval = String.valueOf(myLeft.getValue());
      }
      else
        f1 = (FunctionCall)lexp;
          
      if(rexp instanceof Arithmetic){
        myRight = (Arithmetic)rexp;
        rval = String.valueOf(myRight.getValue());
      }
      else
        f2 = (FunctionCall)rexp;
				
        
      if(this.left != null)
        sb.append(this.left.handleMe(context));
				
      if(this.right!= null)
        sb.append(this.right.handleMe(context));
				
        
      if(this instanceof Arithmetic){
        System.out.println("arithmetic caught");
        sb = StringManipulator.handleArithmetic(sb, (Arithmetic)this, context);
      }

      if(this instanceof FunctionCall){
        //TODO generate code for function calls
        System.out.println("FUNCTION CALL: code generation is not implemented yet.");
        sb = StringManipulator.handleFunctionCall(sb, f1, f2, context);
        return sb;
      }
        
      if(this.op != null){
        System.out.println("operation handling");
        sb = StringManipulator.handleOperation(sb, this.op, this.left, this.right, context);
      }
      
      return sb;
    }
    
}
