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
		
		public static int getPriority(String s){
			if(s.equals(OperationType.IMULQ.toString()) || s.equals(OperationType.IDIVQ.toString()))
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
						case ADDQ:
							result = op1 + op2;
							break;
							
						case SUBQ:
							result = op1 - op2;
							break;
							
						case IMULQ:
							result = op1 * op2;
							break;
							
						case IDIVQ:
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
        System.out.println(tmp);
				
				if(isInteger(tmp))
					output.push(tmp);
				
				else if(tmp.equals("(")){
          System.out.println("111");
					//TODO Elyas: push to opStack
				}
				
				else if(tmp.equals(")")){
					//TODO Elyas: until opStack.top is lparen, pop operators off the stack onto output.
					// pop lparen from stack
					// if stack runs out without finding lparen -> mismatched parentheses
				}
					
				else{ //operator
					if(!opStack.empty()){
						while(opStack.size() > 0 && getPriority(tmp) <= getPriority(opStack.peek())){
							//Empty opStack into output
							output.push(opStack.pop());
						}
					}
					opStack.push(tmp);
				}
			}
			
			while(!opStack.empty()){
				//TODO Elyas: if top is parenthesis -> mismatched parentheses
				output.push(opStack.pop());
			}
			
			while(!output.empty())
				sb.append(output.pop() + " ");
				
			return sb.toString();
		}
		
    
    
    
		public static StringBuffer handleFunctionCall(StringBuffer sb, FunctionCall f, Context context) throws Exception{
      Variable tmp;
      String val;
      Integer num;
      Register reg;
      System.out.println("\tTag: " + f.getTag());
      int i = f.getArgs().size() - 1;
      for(Expression e : f.getArgs()){
				System.out.println("\tHandling next arg");
				
				if(e.isFullyNumeric()){
					num = calculateNum(e);
					sb.append("\tmovq\t$" + num + ", %" + Parser.regMan.getArgReg(String.valueOf(num), i) + "\n"); 
				}
        
        else if(e.op == null){
          tmp = (Variable)e;
          val = String.valueOf(tmp.getValue());
          
					if(Parser.regMan.isListedVariable(val)){
						reg = Parser.regMan.addVariableToRegister(val, Register.RegisterType.CALLER_SAVED);
            sb.append("\tmovq\t%" + reg.toString() + ", %" + Parser.regMan.getArgReg(val, i) + "\n");
          }
          else
            sb.append("\tmovq\t" + context.getVariableLocation(val) + ", %" + Parser.regMan.getArgReg(val, i) + "\n");
        }
        
        else{
					sb.append(e.handleExpression(null, context).toString());
					sb.append("\tmovq\t" + sb.substring(sb.lastIndexOf(",") + 2).replace("\n","") + ", %" + Parser.regMan.getArgReg(null, i) + "\n");  //getArgReg param1: ?
        }
          
        i--;
      }
        
      sb.append("\tcall\t" + f.getTag() + "\n");
      return sb;
    }
    
    
    public static StringBuffer handleVariable(StringBuffer sb, Variable a, String dst, Context context) throws Exception{
      if(a.getValue() != null){
        String val = String.valueOf(a.getValue());
          
        if(a.getValue() instanceof Integer)
          sb.append("\tmovq\t$" + a.getValue() + ", " + dst + "\n");
          
        if(a.getValue() instanceof String) 
					if( !(a.getValue().equals("(")) && !(a.getValue().equals(")")))
						sb.append("\tmovq\t" + context.getVariableLocation((String)a.getValue()) + ", " + dst + "\n");
      }
			return sb;
		}
    
    public static StringBuffer handleOperation(StringBuffer sb, OperationType op, String src, String dst) throws Exception{
			if(op.equals(OperationType.IDIVQ)){
				//TODO Elyas: consider situation when src = %rax
				sb.append("\tmovq\t" + src + ", %rbx\n");
				sb.append("\txorl\t" + src + ", " + src + "\n");
				sb.append("\t" + op.toString() + "\t%rbx\n");
			}
			
			else
				sb.append("\t" + op.toString() + "\t" + src + ", " + dst + "\n");
			return sb;
    }
    
     
    
}
