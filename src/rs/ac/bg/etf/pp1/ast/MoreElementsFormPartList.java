// generated with ast extension for cup
// version 0.8
// 27/5/2023 20:46:59


package rs.ac.bg.etf.pp1.ast;

public class MoreElementsFormPartList extends FormParList {

    private FormParList FormParList;
    private FormPar FormPar;

    public MoreElementsFormPartList (FormParList FormParList, FormPar FormPar) {
        this.FormParList=FormParList;
        if(FormParList!=null) FormParList.setParent(this);
        this.FormPar=FormPar;
        if(FormPar!=null) FormPar.setParent(this);
    }

    public FormParList getFormParList() {
        return FormParList;
    }

    public void setFormParList(FormParList FormParList) {
        this.FormParList=FormParList;
    }

    public FormPar getFormPar() {
        return FormPar;
    }

    public void setFormPar(FormPar FormPar) {
        this.FormPar=FormPar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParList!=null) FormParList.accept(visitor);
        if(FormPar!=null) FormPar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParList!=null) FormParList.traverseTopDown(visitor);
        if(FormPar!=null) FormPar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParList!=null) FormParList.traverseBottomUp(visitor);
        if(FormPar!=null) FormPar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MoreElementsFormPartList(\n");

        if(FormParList!=null)
            buffer.append(FormParList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormPar!=null)
            buffer.append(FormPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MoreElementsFormPartList]");
        return buffer.toString();
    }
}
