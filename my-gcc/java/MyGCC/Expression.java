package MyGCC;

import java.util.Stack;

public abstract class Expression{
  
    public Expression left;
    public Expression right;
    public OperationType op;
    public boolean priority;
    
    public Expression(){
      this.left = null;
      this.right = null;
      this.op = null;
      this.priority = false;
    }
    
    /**
     * This functions return the highest number of parameters contained in a
     * function call of the expression.
     * The value returned is <b>-1</b> if there's no call to functions in the expression
     */
    public int maxParameters(){
      int n = -1;
      if (left != null) n = Math.max(n, left.maxParameters());
      if (right != null) n = Math.max(n, right.maxParameters());
      return n;
    }
    
    /**
     * Checks if the expression is composed exclusively of numeric values
     **/
    public boolean isFullyNumeric(){
			boolean bl = true;
			boolean br = true;
			Variable tmp = null;
			
			if(this.left != null){
				if(this.left instanceof Variable)
					bl = this.left.isFullyNumeric();
				else
					return false;
			}
			
			if(this.right != null){
				if(this.right instanceof Variable)
					br = this.right.isFullyNumeric();
				else
					return false;
			}
			
			if(this instanceof Variable)
				tmp = (Variable)this;
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
			Variable tmp = (Variable)this;
      
			if(this.op != null){
				Variable lft = (Variable)this.left;
				sb.append(lft.getValue().toString());
				
				while(tmp.op != null){
					if(tmp.right.left != null){
						sb.append(" " + tmp.op.toString() + " " +((Variable)tmp.right.left).getValue());
						tmp = (Variable)tmp.right;
					}
					else{
						sb.append(" " + tmp.op.toString() + " " + ((Variable)tmp.right).getValue());
						break;
          }
				}
				return sb.toString();
			}
			return (tmp.getValue()).toString();
		}
    
    
    
    public StringBuffer handleExpression(Expression e, Context context) throws Exception{
			StringBuffer sb = new StringBuffer();
			String lastReg;
			System.out.println("Handling expression");
			
			
			if(this.right == null){
				
				if(this instanceof Variable){
					System.out.println("Variable caught: " + ((Variable)this).getValue());
					sb = StringManipulator.handleVariable(sb, (Variable)this, "%rax", context);
					
				}

				else if(this instanceof FunctionCall){
					System.out.println("Function-call caught: " + ((FunctionCall)this).getTag());
					sb = StringManipulator.handleFunctionCall(sb, (FunctionCall)this, context);
				}
				return sb;
			}
			
			
			sb.append(this.left.handleExpression(e, context));
			if(this.right.op != null){
				
				if(this.right.priority){
					this.right.priority = false;
					
          sb.append(context.virtualPush("%rax"));
          sb.append(this.right.handleExpression(e, context));
          sb.append(context.virtualPop("%rdx"));
          sb = StringManipulator.handleOperation(sb, this.op, "%rax", "%rdx");
          sb.append("\tmovq\t%rdx, %rax\n");	
				}
				
				
				else if(StringManipulator.getPriority(this.op.toString()) > StringManipulator.getPriority(this.right.op.toString())){
					
					if(this.right.left instanceof Variable)
						sb = StringManipulator.handleVariable(sb, (Variable)this.right.left, "%rdx", context);
					else{
						sb.append("\tmovq\t%rax, %rdx\n");
						sb = StringManipulator.handleFunctionCall(sb, (FunctionCall)this.right.left, context);
					}
					
					sb = StringManipulator.handleOperation(sb, this.op, "%rdx", "%rax");
					sb.append(context.virtualPush("%rax"));
					sb.append(this.right.right.handleExpression(e, context));
					sb.append(context.virtualPop("%rdx"));
					sb = StringManipulator.handleOperation(sb, this.right.op, "%rax", "%rdx");
					sb.append("\tmovq\t%rdx, %rax\n");
				}		
				
				else{
					if(this.left instanceof FunctionCall)
						lastReg = "%rax";
					else
						lastReg = sb.substring(sb.lastIndexOf(",") + 2).replace("\n","");
					
					sb.append(context.virtualPush(lastReg));
					sb.append(this.right.handleExpression(e, context));
					sb.append("\tmovq\t%rax, %rdx\n");
					sb.append(context.virtualPop("%rax"));
					sb = StringManipulator.handleOperation(sb, this.op, "%rax", "%rdx");
					sb.append("\tmovq\t%rdx, %rax\n");
				}
			}
			
			else{
				
				if(this.right instanceof Variable){
					
					if(((Variable)this.right).getValue() instanceof Integer)
						sb = StringManipulator.handleOperation(sb, this.op, "$"+((Variable)this.right).getValue(), "%rax");
						
					else{
						sb = StringManipulator.handleVariable(sb, (Variable)this.right, "%rdx", context);
						sb = StringManipulator.handleOperation(sb, this.op, "%rdx", "%rax");
					}
				}
					
				else{
					sb.append("\tmovq\t%rax, %rdx\n");
					sb = StringManipulator.handleFunctionCall(sb, (FunctionCall)this.right, context);
					sb = StringManipulator.handleOperation(sb, this.op, "%rdx", "%rax");
				}
			}
			
			return sb;
		}
		
    
    
    public int length(){
      int i = 0;
      
      if(this.left != null)
        i += this.left.length();
      if(this.right != null)
        i += this.right.length();
      
      if(this != null && this.op == null)
        i++;
      return i;
    }
    
}
