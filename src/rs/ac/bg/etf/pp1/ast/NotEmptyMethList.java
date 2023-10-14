// generated with ast extension for cup
// version 0.8
// 27/5/2023 20:46:59


package rs.ac.bg.etf.pp1.ast;

public class NotEmptyMethList extends MethList {

    private Meth Meth;
    private MethList MethList;

    public NotEmptyMethList (Meth Meth, MethList MethList) {
        this.Meth=Meth;
        if(Meth!=null) Meth.setParent(this);
        this.MethList=MethList;
        if(MethList!=null) MethList.setParent(this);
    }

    public Meth getMeth() {
        return Meth;
    }

    public void setMeth(Meth Meth) {
        this.Meth=Meth;
    }

    public MethList getMethList() {
        return MethList;
    }

    public void setMethList(MethList MethList) {
        this.MethList=MethList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Meth!=null) Meth.accept(visitor);
        if(MethList!=null) MethList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Meth!=null) Meth.traverseTopDown(visitor);
        if(MethList!=null) MethList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Meth!=null) Meth.traverseBottomUp(visitor);
        if(MethList!=null) MethList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NotEmptyMethList(\n");

        if(Meth!=null)
            buffer.append(Meth.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethList!=null)
            buffer.append(MethList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NotEmptyMethList]");
        return buffer.toString();
    }
}
