package MyGCC;

import java.util.Stack;

public class StringManipulator{
    
		
    
    public static boolean isInteger(String input){
			try{
				Integer.parseInt(input);
				return true;
			}
			catch(Exception e){
				return false;
			}
		}
		
		public static int getPrecedence(String s){
			if(s.equals("imull") || s.equals("idivl"))
				return 2;
			else
				return 1;
		}
    
    
		/**
     * Calculates the value of a numeric expression, written in prefix notation
     **/
    public static Integer calculateNum(Expression e){
			String s = e.toNumeric();
			s = infixToPrefix(s);
			String tmp;
			Integer op1, op2;
			Integer result = 0;
			String[] sp = s.split(" ");
			Stack<Object> stack = new Stack();
			
			for(int i = sp.length-1; i >= 0; i--){
				tmp = sp[i];
				
				if(isInteger(tmp))
					stack.push(Integer.parseInt(tmp)); //push operand
				
				else{
					op1 = (Integer)(stack.pop());
					op2 = (Integer)(stack.pop());
					
					switch(OperationType.valueOf(tmp.toUpperCase())){
						case ADDL:
							result = op1 + op2;
							break;
							
						case SUBL:
							result = op1 - op2;
							break;
							
						case IMULL:
							result = op1 * op2;
							break;
							
						case IDIVL:
							result = op1 / op2;
							break;
							
						default:
							break;
					}
					stack.push(result);
				}
			}
			return (Integer)(stack.pop());
		}
		
		/**
		 * Returns a prefix version of the infix-notation input.
		 * Inspired from Shunting-yard algorithm.
		 **/
		public static String infixToPrefix(String s){
			StringBuffer sb = new StringBuffer();
			String[] sp = s.split(" ");
			String tmp;
			Stack<String> output = new Stack();
			Stack<String> opStack = new Stack();
			
			for(int i = sp.length-1; i >= 0; i--){
				tmp = sp[i];
				
				if(isInteger(tmp))
					output.push(tmp);
				
				else if(tmp.equals("(")){
					//TODO: push to opStack
				}
				
				else if(tmp.equals(")")){
					//TODO: until opStack.top is lparen, pop operators off the stack onto output.
					// pop lparen from stack
					// if stack runs out without finding lparen -> mismatched parentheses
				}
					
				else{ //operator
					if(!opStack.empty()){
						while(opStack.size() > 0 && getPrecedence(tmp) <= getPrecedence(opStack.peek())){
							//Empty opStack into output
							output.push(opStack.pop());
						}
					}
					opStack.push(tmp);
				}
			}
			
			while(!opStack.empty()){
				//TODO if top is parenthesis -> mismatched parentheses
				output.push(opStack.pop());
			}
			
			while(!output.empty())
				sb.append(output.pop() + " ");
				
			return sb.toString();
		}
		
    
    
    
		public static StringBuffer handleFunctionCall(StringBuffer sb, FunctionCall f, Context context) throws Exception{
      Variable tmp;
      String val;
      Register reg;
      int i = f.getArgs().size() - 1;
      for(Expression e : f.getArgs()){
        tmp = (Variable)e;
        val = String.valueOf(tmp.getValue());
        
        if(isInteger(val))
          sb.append("\tpushl\t $" + val + ", %" + Parser.regMan.getArgReg(val, i) + "\n");
        else{
          reg = Parser.regMan.addVariableToRegister(val, Register.RegisterType.CALLER_SAVED);
          sb.append("\tpushl\t %" + reg.toString() + ", %" + Parser.regMan.getArgReg(val, i) + "\n");
        }
        i--;
      }
        
      sb.append("\tcall\t " + f.getTag() + "\n");
      return sb;
    }
    
    
    public static StringBuffer handleVariable(StringBuffer sb, Variable a, Context context) throws Exception{
      if(a.getValue() != null){
        String val = String.valueOf(a.getValue());
        Register reg = Parser.regMan.addVariableToRegister(val, Register.RegisterType.CALLEE_SAVED);
        //FIXME operations should use eax registry.
          
        if(a.getValue() instanceof Integer)
          sb.append("\tmovl\t$" + a.getValue() + ", %" + reg.toString() + "\n");
          
        if(a.getValue() instanceof String) 
          sb.append("\tmovl\t" + context.getVariableLocation((String)a.getValue()) + ", %" + reg.toString() + "\n");
      }
			return sb;
		}
    
    public static StringBuffer handleOperation(StringBuffer sb, OperationType op, Expression l, Expression r, Context context) throws Exception{
      Register regL;
			Register regR;
      String lval = null;
      String rval = null;
      Variable ar1 = null;
      Variable ar2 = null;
    
      if(l.op == null && r.op == null){
        ar1 = (Variable)l;
        ar2 = (Variable)r;     
        lval = String.valueOf(ar1.getValue());
        rval = String.valueOf(ar2.getValue());
        regL = Parser.regMan.addVariableToRegister(lval, Register.RegisterType.CALLEE_SAVED);
        regR = Parser.regMan.addVariableToRegister(rval, Register.RegisterType.CALLEE_SAVED);
      
        sb.append("\t" + op.toString() + "\t %" + regR.toString() + ", %" + regL.toString() + "\n");
      }
      
      else if(l.op == null && r.op != null){
        ar1 = (Variable)l;  
        lval = String.valueOf(ar1.getValue());
        regL = Parser.regMan.addVariableToRegister(lval, Register.RegisterType.CALLEE_SAVED);
        
        sb.append("\t" + op.toString() + "\t %" + regL.toString() + ", " + sb.substring(sb.lastIndexOf(",") + 2).replace("\n","") + "\n");
      }
      
      else if(l.op != null && r.op == null){
        ar2 = (Variable)r;  
        rval = String.valueOf(ar2.getValue());
        regR = Parser.regMan.addVariableToRegister(rval, Register.RegisterType.CALLEE_SAVED);
        
        sb.append("\t" + op.toString() + "\t %" + regR.toString() + ", " + sb.substring(sb.lastIndexOf(",") + 2).replace("\n","") + "\n");
      }
      
      return sb;
    }
    
     
    
}
