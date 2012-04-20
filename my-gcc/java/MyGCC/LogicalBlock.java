package MyGCC;

import java.util.*;

public class LogicalBlock extends Block  {
  public Expression expr;
  public InstructionType type;
  
  public LogicalBlock() {
    super();
    this.expr = null;
    this.type = null;
  }
  
  public LogicalBlock(Expression e, InstructionType t) {
	super();
	this.expr = e;
	this.type = t;
  }
  
  public LogicalBlock(LinkedList<Instruction> ins, LinkedList<Block> bl, Expression e, InstructionType t) {
    super(ins, bl);
    this.expr = e;
    this.type = t;
  }
  
  public Expression getExpression() {
    return this.expr;
  }
  
  public InstructionType getInstructionType() {
    return this.type;
  }
  
}
