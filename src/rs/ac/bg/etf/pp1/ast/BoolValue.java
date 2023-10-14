// generated with ast extension for cup
// version 0.8
// 27/5/2023 20:46:59


package rs.ac.bg.etf.pp1.ast;

public class BoolValue extends Value {

    private Boolean bl;

    public BoolValue (Boolean bl) {
        this.bl=bl;
    }

    public Boolean getBl() {
        return bl;
    }

    public void setBl(Boolean bl) {
        this.bl=bl;
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
        buffer.append("BoolValue(\n");

        buffer.append(" "+tab+bl);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BoolValue]");
        return buffer.toString();
    }
}
