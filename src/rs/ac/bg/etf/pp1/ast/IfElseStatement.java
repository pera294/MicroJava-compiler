// generated with ast extension for cup
// version 0.8
// 27/5/2023 20:46:59


package rs.ac.bg.etf.pp1.ast;

public class IfElseStatement extends Statement {

    private ConditionStart ConditionStart;
    private Condition Condition;
    private ConditionEnd ConditionEnd;
    private Statement Statement;
    private Statement Statement1;

    public IfElseStatement (ConditionStart ConditionStart, Condition Condition, ConditionEnd ConditionEnd, Statement Statement, Statement Statement1) {
        this.ConditionStart=ConditionStart;
        if(ConditionStart!=null) ConditionStart.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.ConditionEnd=ConditionEnd;
        if(ConditionEnd!=null) ConditionEnd.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.Statement1=Statement1;
        if(Statement1!=null) Statement1.setParent(this);
    }

    public ConditionStart getConditionStart() {
        return ConditionStart;
    }

    public void setConditionStart(ConditionStart ConditionStart) {
        this.ConditionStart=ConditionStart;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public ConditionEnd getConditionEnd() {
        return ConditionEnd;
    }

    public void setConditionEnd(ConditionEnd ConditionEnd) {
        this.ConditionEnd=ConditionEnd;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public Statement getStatement1() {
        return Statement1;
    }

    public void setStatement1(Statement Statement1) {
        this.Statement1=Statement1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConditionStart!=null) ConditionStart.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(ConditionEnd!=null) ConditionEnd.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(Statement1!=null) Statement1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionStart!=null) ConditionStart.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(ConditionEnd!=null) ConditionEnd.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(Statement1!=null) Statement1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionStart!=null) ConditionStart.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(ConditionEnd!=null) ConditionEnd.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(Statement1!=null) Statement1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfElseStatement(\n");

        if(ConditionStart!=null)
            buffer.append(ConditionStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionEnd!=null)
            buffer.append(ConditionEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement1!=null)
            buffer.append(Statement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfElseStatement]");
        return buffer.toString();
    }
}
