// generated with ast extension for cup
// version 0.8
// 27/5/2023 20:46:59


package rs.ac.bg.etf.pp1.ast;

public class Designator implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String name;
    private Temp Temp;
    private DesignatorList DesignatorList;

    public Designator (String name, Temp Temp, DesignatorList DesignatorList) {
        this.name=name;
        this.Temp=Temp;
        if(Temp!=null) Temp.setParent(this);
        this.DesignatorList=DesignatorList;
        if(DesignatorList!=null) DesignatorList.setParent(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public Temp getTemp() {
        return Temp;
    }

    public void setTemp(Temp Temp) {
        this.Temp=Temp;
    }

    public DesignatorList getDesignatorList() {
        return DesignatorList;
    }

    public void setDesignatorList(DesignatorList DesignatorList) {
        this.DesignatorList=DesignatorList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Temp!=null) Temp.accept(visitor);
        if(DesignatorList!=null) DesignatorList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Temp!=null) Temp.traverseTopDown(visitor);
        if(DesignatorList!=null) DesignatorList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Temp!=null) Temp.traverseBottomUp(visitor);
        if(DesignatorList!=null) DesignatorList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator(\n");

        buffer.append(" "+tab+name);
        buffer.append("\n");

        if(Temp!=null)
            buffer.append(Temp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorList!=null)
            buffer.append(DesignatorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator]");
        return buffer.toString();
    }
}
