package rs.ac.bg.etf.pp1;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import com.sun.scenario.effect.impl.prism.PrTexture;

import rs.ac.bg.etf.pp1.CounterVisitor.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.SemanticPass;

public class CodeGenerator extends VisitorAdaptor {
	
	CodeGenerator(){
			//chr
			Tab.chrObj.setAdr(Code.pc);
			Code.put(Code.enter);
			Code.put(1);
			Code.put(1);
			Code.put(Code.load_n);
			Code.put(Code.exit);
			Code.put(Code.return_);
		
			//ord
			Tab.ordObj.setAdr(Code.pc);
			Code.put(Code.enter);
			Code.put(1);
			Code.put(1);
			Code.put(Code.load_n);
			Code.put(Code.exit);
			Code.put(Code.return_);
		
			//len
			Tab.lenObj.setAdr(Code.pc);
			Code.put(Code.enter);
			Code.put(1);
			Code.put(1);
			Code.put(Code.load_n);
			Code.put(Code.arraylength);
			Code.put(Code.exit);
			Code.put(Code.return_);
		
	}
	
	private int mainPc;
	
	public int getMainPc(){
		return mainPc;
	}

	boolean newExprExprflag = false;
	
	int tempcnt =0;
	int matcnt =0;
	int patchMap;
	int patchNew;
	
	int oobTrap;
	int oobTrap1;
	int oobjmp;
	
	int oobTrapm;
	int oobTrap1m;
	int oobTrap2m;
	int oobTrap3m;
	int oobjmpm;
	
	
	
	public void visit(MethodName methodName){
		if("main".equalsIgnoreCase(methodName.getMethName())){
			mainPc = Code.pc;
		}
		((Meth)methodName.getParent()).obj.setAdr(Code.pc);
		
		// Collect arguments and local variables
		SyntaxNode methodNode = methodName.getParent();
	
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}
	
	public void visit(Meth methodDecl){
		//System.out.println(methodDecl.obj.getName()+" "+methodDecl.obj.getType().getKind()+" "+Struct.None);
		if(methodDecl.obj.getType().getKind() != Struct.None){
			Code.put(Code.trap);
			Code.loadConst(-1);
		}
		
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ReturnVoidStatement retvoidstmt){
		
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ReturnStatement retstmt){
		
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(Const con){
		if( !(con.getParent()instanceof OneElementConstList) && !(con.getParent()instanceof MoreElementsConstList) )
		Code.load(con.obj);
	}
	
	public void visit(ReadStatement rs){
		if(rs.getDesignator().obj.getType() == Tab.intType){
			
			Code.put(Code.read);
			Code.store(rs.getDesignator().obj);
			return;
		}
		if(rs.getDesignator().obj.getType() == Tab.charType){
			
			Code.put(Code.bread);
			Code.store(rs.getDesignator().obj);
			return;
		}
		
		if(rs.getDesignator().obj.getType() ==  new Struct(Struct.Bool)){
			
			Code.put(Code.read);
			Code.store(rs.getDesignator().obj);
			return;
		}
		
	}
	
	
	public void visit(PrintStatement ps){
		if(ps.getExpr().struct == Tab.intType){
			Code.loadConst(5);
			Code.put(Code.print);
			return;
		}
		if(ps.getExpr().struct == Tab.charType){
			Code.loadConst(1);
			Code.put(Code.bprint);
			return;
		}
		
		if(ps.getExpr().struct ==  new Struct(Struct.Bool)){
			Code.loadConst(1);
			Code.put(Code.print);
			return;
		}
		
	}
	
	public void visit(Print2paramsStatement p2s){
		Code.loadConst(p2s.getN2());
		if(p2s.getExpr().struct == Tab.intType){
			Code.put(Code.print);
		}
		else {
			Code.put(Code.bprint);
		}
	}
	
	public void visit(DesignatorStatementMap dsm){
		//System.out.println("map end");
	}
	
	public void visit (Mapend mapend){
		DesignatorStatementMap dsm =((DesignatorStatementMap)mapend.getParent());
		
		Code.put(Code.astore);
		
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup);
		Code.load(dsm.getDesignator1().obj);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.eq,patchMap);
		
		Code.put(Code.pop);
		
	}
	
	public void visit (Mapmid mapmid){
		DesignatorStatementMap dsm =((DesignatorStatementMap)mapmid.getParent());
		
		Code.loadConst(0); //cnt = 0
		
		Code.load(dsm.getDesignator1().obj);
		Code.put(Code.arraylength);
		Code.put(Code.newarray);
		Code.put(1);
		Code.store(dsm.getDesignator().obj);
		
		//ovo se ponavlja
		patchMap= Code.pc;
		//System.out.println(patchMap);
		Code.load(dsm.getDesignator().obj);
		
		Code.put(Code.dup2);
		Code.put(Code.pop);
		
		Code.load(dsm.getDesignator1().obj);
		
		Code.put(Code.dup2);
		Code.put(Code.pop);
		
		Code.put(Code.aload);
		Code.store(dsm.getDesignator2().obj);

	}
	
	
	public void visit(DesignatorStatementPlusplus dpp){
		Code.load(dpp.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(dpp.getDesignator().obj);
		
	}
	
	public void visit(DesignatorStatementMinusminus dmm){
		Code.load(dmm.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(dmm.getDesignator().obj);
		
	}
	
	public void visit (DesignatorStatementAssign desStmtAssign){
		if(newExprExprflag){
		
			Code.store(desStmtAssign.getDesignator().obj);
			Code.loadConst(0); // cnt = 0
			
			//ovo se ponavlja		
			patchNew = Code.pc;
			Code.put(Code.dup2);
			Code.put(Code.pop);
			
			Code.put(Code.dup2);
			Code.load(desStmtAssign.getDesignator().obj); // mat
			
			Code.put(Code.dup_x2);
			Code.put(Code.pop);
			
			Code.put(Code.newarray);
			Code.put(1);
			
			Code.put(Code.astore);
			Code.put(Code.pop);
			
			Code.loadConst(1);
			Code.put(Code.add);
			Code.put(Code.dup);
			
			Code.load(desStmtAssign.getDesignator().obj);
			Code.put(Code.arraylength);
			Code.putFalseJump(Code.eq, patchNew);
			
			Code.put(Code.pop);
			Code.put(Code.pop);
			
			newExprExprflag= false;
			return;
		}
		
		if(desStmtAssign.getDesignator().obj.getType().getElemType() != null){
			if(desStmtAssign.getDesignator().obj.getType().getElemType().getElemType() != null){
				//System.out.println("matrica assign");
				if(matcnt>0){//matrice
					Code.put(Code.dup_x2);
					Code.put(Code.pop);
					Code.put(Code.dup_x1);
					Code.put(Code.pop);
					
					Code.load(desStmtAssign.getDesignator().obj);
					Code.put(Code.dup_x1);
					Code.put(Code.pop);
					Code.put(Code.aload);
					
					Code.put(Code.dup_x1);
					Code.put(Code.pop);
					
					Code.put(Code.dup_x2);
					Code.put(Code.pop);
					Code.put(Code.dup_x2);
					Code.put(Code.pop);
					
					Code.put(Code.astore);
					
					Code.put(Code.pop);
					
					matcnt--;
					return;
					
				}else{
					Code.store(desStmtAssign.getDesignator().obj);
					return;
				}
				
			}
		}
		
		
		if(tempcnt>0){ //nizovi
			Code.put(Code.astore);
			tempcnt--;
			return;
		}
		else{ //promenjive
			Code.store(desStmtAssign.getDesignator().obj);
		}
		
		
		
		
	}
	
	
	
	 
	public void visit(Temp temp){ // za nizove i matrice
		
		if( ((Designator)temp.getParent()).getDesignatorList() instanceof NotEmptyDesignatorList){
			if(((NotEmptyDesignatorList)((Designator)temp.getParent()).getDesignatorList()  ).getDesignatorList() instanceof EmptyDesignatorList){
				Code.load(  ((Designator)temp.getParent()).obj );
				//System.out.println("povecao tempcnt");
				tempcnt ++;
			}
			
		}
		
		
		if( ((Designator)temp.getParent()).getDesignatorList() instanceof NotEmptyDesignatorList){
			if(((NotEmptyDesignatorList)((Designator)temp.getParent()).getDesignatorList()  ).getDesignatorList() instanceof NotEmptyDesignatorList){
				Code.load(  ((Designator)temp.getParent()).obj );
				//System.out.println("povecao matcnt");
				matcnt ++;
			}
			
		}
		
		
	}
	
	
	public void visit(NotEmptyAddopTermList neadtl){
		if(neadtl.getAddop() instanceof AddopPlus){
			Code.put(Code.add);
			return;
		}
		
		if(neadtl.getAddop() instanceof AddopMinus){
			Code.put(Code.sub);
			return;
		}
	}
	
	public void visit(OneElementMulopFactorList oemfl){
		if(oemfl.getMulop() instanceof MulopMul){
			Code.put(Code.mul);
			return;
		}
		if(oemfl.getMulop() instanceof MulopDiv){
			Code.put(Code.div);
			return;
		}
		if(oemfl.getMulop() instanceof MulopMod){
			Code.put(Code.rem);
			return;
		}
	}
	
	public void visit(MoreElementsMulopFactorList memfl){
		if(memfl.getMulop() instanceof MulopMul){
			Code.put(Code.mul);
			return;
		}
		if(memfl.getMulop() instanceof MulopDiv){
			Code.put(Code.div);
			return;
		}
		if(memfl.getMulop() instanceof MulopMod){
			Code.put(Code.rem);
			return;
		}
	}
	
	public void visit(TermFactor tf){
		if(tf.getParent() instanceof NegativeExpr){
			Code.put(Code.neg);
		}
	}
	
	public void visit(TermMulopFactor tmf){
		if(tmf.getParent() instanceof NegativeExpr){
			Code.put(Code.neg);
		}
	}
	
	
	public void visit(FactorDesignator factorDesignator){
		
		if(factorDesignator.getDesignator().obj.getKind() == Obj.Con){
			Code.load(factorDesignator.getDesignator().obj);
			return;
		}
		
		if(factorDesignator.getDesignator().obj.getType().getElemType()!=null){
			if(factorDesignator.getDesignator().obj.getType().getElemType().getElemType() !=null){
				
				//provera za assign matrica
				if(factorDesignator.getParent() instanceof TermFactor){
					if(  ((TermFactor)factorDesignator.getParent() ).getParent() instanceof PositiveExpr ){
						if(  ((PositiveExpr) ((TermFactor)factorDesignator.getParent() ).getParent()).getParent() instanceof DesignatorStatementAssign ){
							if(  ((PositiveExpr) ((TermFactor)factorDesignator.getParent() ).getParent()).getAddopTermList() instanceof EmptyAddopTermList  ){
							//System.out.println("nemoj da assign matricu");
							Code.load(factorDesignator.getDesignator().obj);
							//System.out.println("nemo da assign matricu matcnt = " + matcnt);
							matcnt--;
							return;
							}
						}
					}
				}
				
				//System.out.println("fd matrica");
				Code.put(Code.dup_x2);
				Code.put(Code.pop);
				Code.put(Code.aload);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
				Code.put(Code.aload);
				return;
			}
			
		}
		
		if(tempcnt==0){
			Code.load(factorDesignator.getDesignator().obj);
		}
		else{
			Code.put(Code.aload);
			tempcnt--;
		}
		
	}
	
	public void visit(DesignatorStatementNoActPars dsnap){
		Obj functionObj = dsnap.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if(dsnap.getDesignator().obj.getType() != Tab.noType){
			Code.put(Code.pop);
		}
	
	}
	
	public void visit(DesignatorStatementActPars dsap){
		Obj functionObj = dsap.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc ;
		Code.put(Code.call);
		
		Code.put2(offset);
		
	}
	
	public void visit(FactorDesignatorNoActPars fdnap){
		Obj functionObj = fdnap.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		
	}
	
	public void visit(FactorDesignatorActPars fdap){
		Obj functionObj = fdap.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	public void visit(FactorNewExpr fne){
		Code.put(Code.newarray);
		Code.put(1);
	}
	
	public void visit(FactorNewExprExpr fnee){
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.newarray);
		Code.put(1);
		newExprExprflag=true;
		
	}
	
	public void visit(FactorNum fn){
		Code.loadConst(fn.getN1());
	}
	
	public void visit(FactorChar fc){
		Code.loadConst(fc.getC1());
	}
	
	public void visit(FactorBool fb){
		if(fb.getB1() == true){
			Code.loadConst(1);
		}
		else 
			Code.loadConst(0);
			
	}
	
	public void visit(EmptyDesignatorList edl){ // provera opsega
		if(edl.getParent() instanceof Designator) return;
		
		if (edl.getParent() instanceof  NotEmptyDesignatorList){
			if( ((NotEmptyDesignatorList)edl.getParent()).getParent() instanceof NotEmptyDesignatorList ){
				//matrica
				Designator des = null;
				if(   ((NotEmptyDesignatorList)((NotEmptyDesignatorList)edl.getParent()).getParent()).getParent()  instanceof Designator){
					des = ((Designator)((NotEmptyDesignatorList)((NotEmptyDesignatorList)edl.getParent()).getParent()).getParent() );
				}
				
				//System.out.println("dmatrica");
				//System.out.println(des.obj.getName());
				
				Code.put(Code.dup2);
				Code.load(des.obj);
				Code.loadConst(0);
				Code.put(Code.aload);
				Code.put(Code.arraylength);
				
				Code.putFalseJump(Code.lt, 0);
				oobTrapm = Code.pc-2;
				
				Code.put(Code.dup);
				Code.loadConst(-1);
				Code.putFalseJump(Code.gt, oobTrap1);
				oobTrap1m = Code.pc-2;
				
				//sad prvi index
				
				Code.load(des.obj);
				Code.put(Code.arraylength);
				
				Code.putFalseJump(Code.lt, 0);
				oobTrap2m = Code.pc-2;
				
				Code.put(Code.dup);
				Code.loadConst(-1);
				Code.putFalseJump(Code.gt, oobTrap1);
				oobTrap3m = Code.pc-2;
				
				Code.putJump(0);
				oobjmpm = Code.pc-2;
				
				Code.fixup(oobTrapm);
				Code.fixup(oobTrap1m);
				Code.fixup(oobTrap2m);
				Code.fixup(oobTrap3m);
				Code.put(Code.trap);
				Code.loadConst(1);
				
				Code.fixup(oobjmpm);
				
				
				return;
			}
			//niz
			//proveri da li je index out of bounds 
			//poslednja vrednost na steku je index
			
			Code.put(Code.dup2);
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			Code.put(Code.arraylength);
			
			Code.putFalseJump(Code.lt, 0);
			oobTrap = Code.pc-2;
			
			Code.put(Code.dup);
			Code.loadConst(-1);
			Code.putFalseJump(Code.gt, oobTrap1);
			oobTrap1 = Code.pc-2;
			
			Code.putJump(0);
			oobjmp = Code.pc-2;
			
			Code.fixup(oobTrap);
			Code.fixup(oobTrap1);
			Code.put(Code.trap);
			Code.loadConst(1);
			
			Code.fixup(oobjmp);
			return;
			
		}
	}
	
}
