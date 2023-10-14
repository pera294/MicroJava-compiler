// generated with ast extension for cup
// version 0.8
// 27/5/2023 20:46:59


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementMap extends DesignatorStatement {

    private Designator Designator;
    private Assignop Assignop;
    private Designator Designator1;
    private Designator Designator2;
    private Mapmid Mapmid;
    private Expr Expr;
    private Mapend Mapend;

    public DesignatorStatementMap (Designator Designator, Assignop Assignop, Designator Designator1, Designator Designator2, Mapmid Mapmid, Expr Expr, Mapend Mapend) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.Assignop=Assignop;
        if(Assignop!=null) Assignop.setParent(this);
        this.Designator1=Designator1;
        if(Designator1!=null) Designator1.setParent(this);
        this.Designator2=Designator2;
        if(Designator2!=null) Designator2.setParent(this);
        this.Mapmid=Mapmid;
        if(Mapmid!=null) Mapmid.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.Mapend=Mapend;
        if(Mapend!=null) Mapend.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public Assignop getAssignop() {
        return Assignop;
    }

    public void setAssignop(Assignop Assignop) {
        this.Assignop=Assignop;
    }

    public Designator getDesignator1() {
        return Designator1;
    }

    public void setDesignator1(Designator Designator1) {
        this.Designator1=Designator1;
    }

    public Designator getDesignator2() {
        return Designator2;
    }

    public void setDesignator2(Designator Designator2) {
        this.Designator2=Designator2;
    }

    public Mapmid getMapmid() {
        return Mapmid;
    }

    public void setMapmid(Mapmid Mapmid) {
        this.Mapmid=Mapmid;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public Mapend getMapend() {
        return Mapend;
    }

    public void setMapend(Mapend Mapend) {
        this.Mapend=Mapend;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(Assignop!=null) Assignop.accept(visitor);
        if(Designator1!=null) Designator1.accept(visitor);
        if(Designator2!=null) Designator2.accept(visitor);
        if(Mapmid!=null) Mapmid.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(Mapend!=null) Mapend.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(Assignop!=null) Assignop.traverseTopDown(visitor);
        if(Designator1!=null) Designator1.traverseTopDown(visitor);
        if(Designator2!=null) Designator2.traverseTopDown(visitor);
        if(Mapmid!=null) Mapmid.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(Mapend!=null) Mapend.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(Assignop!=null) Assignop.traverseBottomUp(visitor);
        if(Designator1!=null) Designator1.traverseBottomUp(visitor);
        if(Designator2!=null) Designator2.traverseBottomUp(visitor);
        if(Mapmid!=null) Mapmid.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(Mapend!=null) Mapend.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementMap(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Assignop!=null)
            buffer.append(Assignop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Designator1!=null)
            buffer.append(Designator1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Designator2!=null)
            buffer.append(Designator2.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mapmid!=null)
            buffer.append(Mapmid.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mapend!=null)
            buffer.append(Mapend.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementMap]");
        return buffer.toString();
    }
}
