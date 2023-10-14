// generated with ast extension for cup
// version 0.8
// 27/5/2023 20:46:59


package rs.ac.bg.etf.pp1.ast;

public class EmptyMethList extends MethList {

    public EmptyMethList () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EmptyMethList(\n");

        buffer.append(tab);
        buffer.append(") [EmptyMethList]");
        return buffer.toString();
    }
}
