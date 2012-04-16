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
      Arithmetic tmp;
      for(Expression e : f.getArgs()){
        tmp = (Arithmetic)e;
        sb.append("\tpushl %TMP\n");
      }
        
      sb.append("\tcall " + f.getTag() + "\n");
      return sb;
    }
    
    
    public static StringBuffer handleArithmetic(StringBuffer sb, Arithmetic a, Context context) throws Exception{
      if(a.getValue() != null){
        String val = String.valueOf(a.getValue());
        Register reg = Parser.regMan.addVariableToRegister(val, Register.RegisterType.CALLEE_SAVED);
        //FIXME operations should use eax registry.
          
        if(a.getValue() instanceof Integer)
          sb.append("\tmovl %" + a.getValue() + ", %" + reg.toString() + "\n");
          
        if(a.getValue() instanceof String) 
          sb.append("\tmovl %" + context.getVariableLocation((String)a.getValue()) + ", %" + reg.toString() + "\n");
      }
			return sb;
		}
    
    public static StringBuffer handleOperation(StringBuffer sb, OperationType op, Expression l, Expression r, Context context) throws Exception{
      Register regL;
			Register regR;
      String lval = null;
      String rval = null;
      Arithmetic ar1 = null;
      Arithmetic ar2 = null;
    
      if(l instanceof Arithmetic){
        ar1 = (Arithmetic)l;
        lval = String.valueOf(ar1.getValue());
      }
      if(r instanceof Arithmetic){
        ar2 = (Arithmetic)r;
        rval = String.valueOf(ar2.getValue());
      }
      
      regL = Parser.regMan.addVariableToRegister(lval, Register.RegisterType.CALLEE_SAVED);
      regR = Parser.regMan.addVariableToRegister(rval, Register.RegisterType.CALLEE_SAVED);
      
      sb.append("\t" + op.toString() + " %" + regR.toString() + ", %" + regL.toString() + "\n");
      return sb;
    }
    
     
    
}
