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
					else{
						sb.append(" " + tmp.op.toString() + " " + ((Arithmetic)tmp.right).getValue());
						break;
					}
				}
				return sb.toString();
			}
			return (tmp.getValue()).toString();
		}

    
    
    public StringBuffer handleExpression(Context context) throws Exception{
			StringBuffer sb = new StringBuffer();
			String lval = new String();
			String rval = new String();
			
			if(this == null){
				//sb.append("\tleave\n\tret\n"); Already handled in epilogue()
				System.err.println("Expression is null\n");
			}
			
			if(this.isFullyNumeric()){
				//TODO get the corresponding register for this evaluation (always eax?)
				return sb.append("\tmovl %" + StringManipulator.calculateNum(this) + ", %TMP\n");
			}
			
			else{
				
				Expression lexp = this.left;
				Expression rexp = this.right;
				Arithmetic myLeft = null;
				Arithmetic myRight = null;
				Register regL = null;
				Register regR = null;
				

				if(lexp instanceof Arithmetic){
					myLeft = (Arithmetic)lexp;
					lval = String.valueOf(myLeft.getValue());
				}

				if(rexp instanceof Arithmetic){
					myRight = (Arithmetic)rexp;
					rval = String.valueOf(myRight.getValue());
				}
				
				if(this.left != null){
					if(this.left.op != null){
						sb.append(this.left.handleExpression(context));
					}
				}
				
				if(this.right!= null){
					if(this.right.op != null)
						sb.append(this.right.handleExpression(context));
				}
				
					
				if(lexp instanceof Arithmetic && rexp instanceof Arithmetic)
					sb = StringManipulator.handleArithmetics(sb, myLeft, myRight, this.op, context);

				else{
					//TODO generate code for function calls
					System.out.println("NON-ARITHMETIC");
					return sb;
				}
			}
      
      return sb;
		}
		
    
    
}
