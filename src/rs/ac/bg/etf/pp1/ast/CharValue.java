// generated with ast extension for cup
// version 0.8
// 27/5/2023 20:46:59


package rs.ac.bg.etf.pp1.ast;

public class CharValue extends Value {

    private Character ch;

    public CharValue (Character ch) {
        this.ch=ch;
    }

    public Character getCh() {
        return ch;
    }

    public void setCh(Character ch) {
        this.ch=ch;
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
        buffer.append("CharValue(\n");

        buffer.append(" "+tab+ch);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CharValue]");
        return buffer.toString();
    }
}
