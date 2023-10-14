package rs.ac.bg.etf.pp1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.sun.jmx.remote.util.OrderClassLoaders;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {
	int printCallCount = 0;

	boolean newExprflag = false;
	boolean newExprExprflag = false;
	boolean mapflag = false;
	boolean condflag = false;
	
	Designator firstReference = null;
	int refcnt=0;
	

	boolean actParsFlag = false;
	Stack<String> methodNameStack = new Stack<>();
	HashMap<String, Struct> actparsHashmap = new HashMap<String, Struct>();
	int actparscnt = 0;

	static int globalVarCount = 0;
	static int globalConstCount = 0;
	
	int loopLevel = 0;

	boolean returnFound = false;
	boolean errorDetected = false;

	Obj currentMethod = null;
	String currMethodName;

	Struct currType;
	Struct methodRetType;
	Struct checkValueType;

	int currValueInt;
	char currValueChar;
	boolean currValueBool;

	int nVars;

	Scope programScope = null;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public String Type_kind_toString(Struct type) {
		switch (type.getKind()) {
		case 0:
			return "void";
		case 1:
			return "int";
		case 2:
			return "char";
		case 5:
			return "bool";
		default:
			return "error";
		}

	}

	// ------------------Program-------------------------

	public void checkDoubleGlobalDeclarations(Program program) {
		int cnt = 0;
		Obj o = Tab.find(program.getProgramName().getProgName());
		for (Obj k : o.getLocalSymbols()) {
			if (k.getKind() == Obj.Con || k.getKind() == Obj.Var) {
				cnt++;
			}
		}

		if (globalVarCount + globalConstCount != cnt)
			report_error("Dupla deklaracija u globalnom domenu", null);

		// System.out.println("globalVarcount = " + globalVarCount);
		// System.out.println("globaConstcount = " +globalConstCount);
		// System.out.println("totalCount = " +cnt);
		// System.out.println(mapflag);

	}

	public void checkMainMethod(Program program) {
		Obj o = Tab.find("main");
		// System.out.println(o.getType());
		if (o == Tab.noObj) {
			report_error("Ne postoji main metoda ", null);
		} else {
			if (o.getLevel() != 0) {
				report_error("Main metoda mora biti bez argumenata ", null);
			}
		}

	}

	public void visit(ProgramName progName) {
		// pocni program
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
		programScope = Tab.currentScope;
	}

	public void visit(Program program) {
		// zavrsi program
		nVars = globalVarCount;
		Tab.chainLocalSymbols(program.getProgramName().obj);

		checkMainMethod(program);
		checkDoubleGlobalDeclarations(program);
		Tab.closeScope();
		
		//System.out.println("nvars = " + nVars);

	}

	// ------------------Tip------------------------------
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName()
					+ " u tabeli simbola! ", null);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
				currType = typeNode.getType();
			} else {
				report_error("Greska: Ime " + type.getTypeName()
						+ " ne predstavlja tip!", type);
				type.struct = Tab.noType;
			}
		}
	}

	// -----------------------Deklaracije--------------------------------------

	public void visit(NumValue numvalue) {
		currValueInt = numvalue.getNum();
		checkValueType = new Struct(Struct.Int);
	}

	public void visit(CharValue charvalue) {
		currValueChar = charvalue.getCh();
		checkValueType = new Struct(Struct.Char);
	}

	public void visit(BoolValue boolvalue) {
		currValueBool = boolvalue.getBl();
		checkValueType = new Struct(Struct.Bool);
	}

	public boolean DeclNameExists(String name) {
		Obj Node = Tab.find(name);
		if (Node == Tab.noObj) {
			return false;
		} else
			return true;

	}

	// konstante
	public void visit(Const con) {
		if (currType.getKind() == Struct.Int
				&& checkValueType.getKind() == Struct.Int) {
			Obj conObj = Tab.insert(Obj.Con, con.getConstName(), currType);
			globalConstCount++;
			conObj.setAdr(currValueInt);
			con.obj = conObj;
			//System.out.println("con value" +conObj.getLevel());
			/*
			 * report_info("insert " + con.getConstName() + ": const " +
			 * Type_kind_toString(currType) + " = " + currValueInt, null);
			 */
		} else if (currType.getKind() == Struct.Char
				&& checkValueType.getKind() == Struct.Char) {
			Obj conObj = Tab.insert(Obj.Con, con.getConstName(), currType);
			globalConstCount++;
			conObj.setAdr(currValueChar);
			con.obj = conObj;
			/*
			 * report_info("insert " + con.getConstName() + ": const " +
			 * Type_kind_toString(currType) + " = " + currValueChar, null);
			 */

		} else if (currType.getKind() == Struct.Bool
				&& checkValueType.getKind() == Struct.Bool) {
			Obj conObj = Tab.insert(Obj.Con, con.getConstName(), currType);
			globalConstCount++;
			if (currValueBool == true) {
				conObj.setAdr(1);
			} else
				conObj.setAdr(0);
			
			con.obj = conObj;

			/*
			 * report_info("insert " + con.getConstName() + ": const " +
			 * Type_kind_toString(currType) + " = " + currValueBool, null);
			 */

		} else {
			report_error("Pogresna dodela vrednosti: const "
					+ Type_kind_toString(currType) + " " + con.getConstName(),
					null);
		}
		
		

	}

	// Promenjive
	public void visit(VarIdent var) {
		var.obj = Tab.insert(Obj.Var, var.getVarType(), currType);
		//System.out.println("insertovao " + var.getVarType() );
		nVars++;
		if (Tab.currentScope == programScope)
			globalVarCount++;
		/*
		 * report_info("insert " + var.getVarType() + ": var of " +
		 * Type_kind_toString(currType), null);
		 */
	}

	public void visit(VarIdentBrackets var) {

		var.obj = Tab.insert(Obj.Var, var.getVarType(), new Struct(
				Struct.Array, currType));
		nVars++;
		if (Tab.currentScope == programScope)
			globalVarCount++;
		/*
		 * report_info("insert " + var.getVarType() + ": Array of " +
		 * Type_kind_toString(currType), null);
		 */
	}

	public void visit(VarIdentBracketsBrackets var) {
		var.obj = Tab.insert(Obj.Var, var.getVarType(), new Struct(
				Struct.Array, new Struct(Struct.Array, currType)));
		nVars++;
		if (Tab.currentScope == programScope)
			globalVarCount++;
		/*
		 * report_info("insert " + var.getVarType() + ": Matrix of " +
		 * Type_kind_toString(currType), null);
		 */

	}

	// ---------------Deklaracije Metoda-------------------

	public void checkSameLocalDeclarations() {

		if (nVars != currentMethod.getLocalSymbols().size()) {
			report_error(
					"Dupla deklaracija u metodi " + currentMethod.getName(),
					null);
		}
	}

	public void visit(RetValType rvt) {
		// postavi currType na tip povratne vrednosti funkcije (int,char,bool)
		methodRetType = rvt.getType().struct;
	}

	public void visit(RetValVoid rvv) {
		// postavi currType na tip povratne vrednosti funkcije (void)
		methodRetType = Tab.noType;
	}

	public void visit(MethodName methodName) {
		// pocni metodu
		currentMethod = Tab.insert(Obj.Meth, methodName.getMethName(), methodRetType);
		//System.out.println("method inserted " + currentMethod.getName());
		report_info("", null);
		report_info(
				Type_kind_toString(currType) + " " + currentMethod.getName()
						+ " started ", null);
		nVars = 0;
		Tab.openScope();
	}

	public void visit(Meth method) {
		// zavrsi metodu
		report_info(Type_kind_toString(currentMethod.getType()) + " "
				+ method.getMethodName().getMethName() + " finished", null);
		Tab.chainLocalSymbols(currentMethod);
		// provera istih deklaracija promenjiivih
		checkSameLocalDeclarations();
		Tab.closeScope();
		nVars = 0;
		
		if(methodRetType.getKind() != Struct.None && returnFound == false){
			report_error("Metoda nema return", method);
		}
		
		returnFound=false;
		method.obj=currentMethod;

		//System.out.println(" sempass" +method.obj.getName()+" "+method.obj.getType().getKind()+" "+Struct.None);
		//System.out.println(method.obj.getName());
		currentMethod = null;


	}

	public void visit(OneElementFormParList fpl) {
		// currentMethod.setLevel(0);
		fpl.getFormPar().getVar().obj.setFpPos(currentMethod.getLevel());
		currentMethod.setLevel(currentMethod.getLevel() + 1);

		//System.out.println("redni broj parametra"
				//+ fpl.getFormPar().getVar().obj.getName() + " je "
				//+ currentMethod.getLevel());
		// report_info("zavrsio parametre", null);
	}

	public void visit(MoreElementsFormPartList mefp) {
		mefp.getFormPar().getVar().obj.setFpPos(currentMethod.getLevel());
		currentMethod.setLevel(currentMethod.getLevel() + 1);
		//System.out.println("redni broj parametra"
				//+ mefp.getFormPar().getVar().obj.getName() + " je "
				//+ currentMethod.getLevel());

		// mefp.getFormPar().obj.setFpPos(currentMethod.getLevel());

	}

	// -----------------------Designator-----------------------------------------------------------

	public void visit(Designator designator) {
		Obj obj = Tab.find(designator.getName());
		if (obj == Tab.noObj) {
			report_error("Greska na liniji " + designator.getLine() + " : ime "
					+ designator.getName() + " nije deklarisano! ", null);
		}

		if( Tab.find(designator.getName()).getKind() == Obj.Meth){
			methodNameStack.push(designator.getName());
			//System.out.println("dodao metodu na stek "+ designator.getName());
		}
		// provera da li je index int
		if (designator.getDesignatorList() instanceof NotEmptyDesignatorList) {
			if (((NotEmptyDesignatorList) designator.getDesignatorList())
					.getExpr().struct.getKind() != Struct.Int) {
				report_error("index mora biti int", designator);
			}

			else if (((NotEmptyDesignatorList) designator.getDesignatorList())
					.getDesignatorList() instanceof NotEmptyDesignatorList) {

				if (((NotEmptyDesignatorList) ((NotEmptyDesignatorList) designator
						.getDesignatorList()).getDesignatorList()).getExpr().struct
						.getKind() != Struct.Int) {
					report_error("index mora biti int", designator);
				}
			}
		}
		
		

		// provera za map
		if (designator.getParent() instanceof DesignatorStatementMap) {
			mapflag = true;
			designator.obj = Tab.find(designator.getName());
			return;
		}
		
		
		// provera za factor = new type[expr]
		if (designator.getParent() instanceof DesignatorStatementAssign) {
			if (((DesignatorStatementAssign) designator.getParent()).getExpr() instanceof PositiveExpr) {
				if (((PositiveExpr) ((DesignatorStatementAssign) designator.getParent()).getExpr()).getTerm() instanceof TermFactor) {
					
					if (((TermFactor) ((PositiveExpr) ((DesignatorStatementAssign) designator.getParent()).getExpr()).getTerm()).getFactor() instanceof FactorNewExpr) {
						newExprflag = true;
						designator.obj = Tab.find(designator.getName());
						return;

					} else if (((TermFactor) ((PositiveExpr) ((DesignatorStatementAssign) designator
							.getParent()).getExpr()).getTerm()).getFactor() instanceof FactorNewExprExpr) {

						newExprExprflag = true;
						designator.obj = obj;
						return;

					}
				}

			}
			;
		}
		
		
		//prva referenca
		if( refcnt == 0 && designator.getParent() instanceof DesignatorStatementAssign &&
				designator.getDesignatorList() instanceof EmptyDesignatorList){
			refcnt=1;
			firstReference= designator;
			designator.obj = Tab.find(obj.getName());
			//System.out.println("prva referenca " + designator.obj.getName());
			return;
		}
		
		//druga referenca
		if(refcnt ==1 ){
			designator.obj = Tab.find(obj.getName());
			if(designator.getParent() instanceof FactorDesignator){
				if(  ((FactorDesignator)designator.getParent()).getParent() instanceof TermFactor  ){
					if(   ((TermFactor) ((FactorDesignator)designator.getParent()).getParent() ).getParent() instanceof PositiveExpr  ){
						if(  ((PositiveExpr)((TermFactor) ((FactorDesignator)designator.getParent()).getParent() ).getParent()).getAddopTermList() instanceof EmptyAddopTermList ){
							if(designator.getDesignatorList() instanceof EmptyDesignatorList){
								report_info("taj mi treba", designator);
								if(firstReference.obj.getType().getKind() == designator.obj.getType().getKind()){
				
									if(firstReference.obj.getType().getElemType().getKind() == designator.obj.getType().getElemType().getKind()){
										
										
										if((firstReference.obj.getType().getElemType().getElemType()== null && designator.obj.getType().getElemType().getElemType()== null)){
											//nizovi
											//System.out.println("okej");
											return;
											
											
										}
					
										
										if((firstReference.obj.getType().getElemType().getElemType() != null && designator.obj.getType().getElemType().getElemType() != null)){
											if(firstReference.obj.getType().getElemType().getElemType().getKind() == designator.obj.getType().getElemType().getElemType().getKind()){
												//matrice
												//System.out.println("okej");
												return;
											}
										}
										
									}
								}
							}
						}
					}
				}
			}
		}
		

		// provera za indexiranost
		DesignatorList designatorList = designator.getDesignatorList();
		if ( !condflag && !actParsFlag)
			if ((designatorList instanceof EmptyDesignatorList && obj.getType()
					.getKind() == Struct.Array)
					|| (designatorList instanceof NotEmptyDesignatorList
							&& obj.getType().getKind() == Struct.Array
							&& obj.getType().getElemType().getKind() == Struct.Array && ((NotEmptyDesignatorList) designatorList)
								.getDesignatorList() instanceof EmptyDesignatorList))
				report_error("matrice i nizovi moraju biti indeksirani",
						designator);

		designator.obj = Tab.find(obj.getName());
	}

	// -----------------------DesignatorStatement-----------------------------------------------------------

	public void visit(DesignatorStatementAssign designatorstmtAssign) {

		if (Tab.find(designatorstmtAssign.getDesignator().getName()).getKind() == Obj.Con) {
			report_error("Ne moze se dodeliti vrednost konstanti",
					designatorstmtAssign);
		}

		Struct left;
		Struct right;

		if (designatorstmtAssign.getDesignator().obj.getType().getKind() != Struct.Array) {
			left = designatorstmtAssign.getDesignator().obj.getType();
		} else if (designatorstmtAssign.getDesignator().obj.getType()
				.getElemType().getKind() != Struct.Array) {
			left = designatorstmtAssign.getDesignator().obj.getType()
					.getElemType();
		} else {
			left = designatorstmtAssign.getDesignator().obj.getType()
					.getElemType().getElemType();
		}

		if (designatorstmtAssign.getExpr().struct.getKind() != Struct.Array) {
			right = designatorstmtAssign.getExpr().struct;
		} else if (designatorstmtAssign.getExpr().struct.getElemType()
				.getKind() != Struct.Array) {
			right = designatorstmtAssign.getExpr().struct.getElemType();
		} else {
			right = designatorstmtAssign.getExpr().struct.getElemType()
					.getElemType();
		}

		if (newExprflag) {
			if (designatorstmtAssign.getDesignator().obj.getType().getKind() != Struct.Array) {
				report_error("Greska: mora se inicijalizovati niz ",
						designatorstmtAssign);
			}
		}

		if (newExprExprflag) {
			if (designatorstmtAssign.getDesignator().obj.getType().getKind() == Struct.Array) {
				if (designatorstmtAssign.getDesignator().obj.getType()
						.getElemType().getKind() != Struct.Array) {
					report_error("Greska: mora se inicijalizovati matrica ",
							designatorstmtAssign);
				}

			} else {
				report_error("Greska: mora se inicijalizovati matrica ",
						designatorstmtAssign);
			}
		}

		newExprflag = false;
		newExprExprflag = false;

		if (right.getKind() != left.getKind()) {
			report_error("Greska: razliciti tipovi " + left.getKind() + " "
					+ right.getKind(), designatorstmtAssign);
		}
		if(firstReference != null)
		//System.out.println("moja prva ref " + firstReference.obj.getName() );
		refcnt=0;
		firstReference = null;
		designatorstmtAssign.getDesignator().obj = Tab.find(designatorstmtAssign.getDesignator().getName());
	}

	public void visit(DesignatorStatementPlusplus designatorplusplus) {

		DesignatorList designatorList = designatorplusplus.getDesignator()
				.getDesignatorList();
		Designator designator = designatorplusplus.getDesignator();
		
		if(designator.obj.getKind() == Obj.Con){
			report_error("Ne mogu se inkrementirati konstante", designatorplusplus);
		}

		if (designatorList instanceof NotEmptyDesignatorList) {
			if (((NotEmptyDesignatorList) designatorList).getDesignatorList() instanceof NotEmptyDesignatorList) { // matrica

				if (designator.obj.getType().getElemType().getElemType()
						.getKind() != Struct.Int)
					report_error("Greska plusplus, int", designatorplusplus);

			} else { // niz

				if (designator.obj.getType().getElemType().getKind() != Struct.Int)
					report_error("Greska plusplus, int", designatorplusplus);
			}
		} else { // var
			if (designatorplusplus.getDesignator().obj.getType().getKind() == Struct.Int) {
				// report_info("Plusplus ok ", null);
			} else {

				report_error("Greska plusplus", null);
			}

		}
		designator.obj = Tab.find(designatorplusplus.getDesignator().getName());
	}

	public void visit(DesignatorStatementMinusminus designatorminusminus) {
		// System.out.println(designatorminusminus.getDesignator().obj.getType().getKind());

		DesignatorList designatorList = designatorminusminus.getDesignator()
				.getDesignatorList();
		Designator designator = designatorminusminus.getDesignator();
		
		if(designator.obj.getKind() == Obj.Con){
			report_error("Ne mogu se dekrementirati konstante", designatorminusminus);
		}

		if (designatorList instanceof NotEmptyDesignatorList) {
			if (((NotEmptyDesignatorList) designatorList).getDesignatorList() instanceof NotEmptyDesignatorList) { // matrica

				if (designator.obj.getType().getElemType().getElemType()
						.getKind() != Struct.Int)
					report_error("Greska minusminus, int", designatorminusminus);

			} else { // niz

				if (designator.obj.getType().getElemType().getKind() != Struct.Int)
					report_error("Greska minusminus, int", designatorminusminus);
			}
		} else { // var
			if (designatorminusminus.getDesignator().obj.getType().getKind() == Struct.Int) {
				// report_info("Minusminus ok ", null);
			} else {

				report_error("Greska minusminus", null);
			}

		}
		
		designator.obj = Tab.find(designatorminusminus.getDesignator().getName());

	}

	public boolean checkIfArray(Designator designator) {

		int BracketCnt = 0;

		if (designator.getDesignatorList() instanceof NotEmptyDesignatorList) {
			BracketCnt++;
			if (((NotEmptyDesignatorList) designator.getDesignatorList())
					.getDesignatorList() instanceof NotEmptyDesignatorList) {
				BracketCnt++;
			}
		}

		if (designator.obj.getType().getKind() != Struct.Array) {
			return false;
		} else if (designator.obj.getType().getElemType().getKind() == Struct.Array
				&& BracketCnt == 1) {
			return true;
		} else if (BracketCnt == 0) {
			return true;
		}

		return false;
	}

	public void visit(DesignatorStatementMap dsm) {

		if (!(checkIfArray(dsm.getDesignator()) && checkIfArray(dsm
				.getDesignator1()))) {
			report_error("operacija map zahteva 2 niza kao prva 2 operanda",
					dsm);
		}

		if (!DeclNameExists(dsm.getDesignator2().getName())) {
			report_error("operand nije deklarisan", dsm);
		}

		Struct type1 = findType(dsm.getDesignator().obj.getType());
		Struct type2 = findType(dsm.getDesignator1().obj.getType());
		Struct type3 = findType(dsm.getDesignator2().obj.getType());
		Struct type4 = findType(dsm.getExpr().struct);
		
		if(dsm.getDesignator2().obj.getType().getKind() == Struct.Array){
			report_error("Greska mora biti promenjiva ", dsm);
		}

		if (!(type1.getKind() == type2.getKind()
				&& type1.getKind() == type3.getKind() && type1.getKind() == type4
				.getKind())) {
			report_error("Svi operandi moraju biti istog tipa ", dsm);
		}

		
		
		mapflag = false;
	}

	// -----------------------Expr-Factor-Term--------------------------------------------------------

	public Struct findType(Struct s) {

		if (s == Tab.noType || s == null)
			return Tab.noType;
		// System.out.println("find type s = " + s.getKind());
		if (s.getKind() == Struct.Array) {
			if (s.getElemType().getKind() == Struct.Array) {
				// matrica
				return s.getElemType().getElemType();
			} else {
				// niz
				return s.getElemType();
			}
		}

		return s;

	}

	public void visit(OneElementMulopFactorList oemf) {
		oemf.struct = oemf.getFactor().struct;
	}

	public void visit(MoreElementsMulopFactorList moreElemMfl) {

		if (moreElemMfl.getMulopFactorList().struct != moreElemMfl.getFactor().struct) {
			report_error("Greska MoreElementsMulopFactorList", moreElemMfl);
		}
		moreElemMfl.struct = moreElemMfl.getFactor().struct;
	}

	public void visit(TermFactor tf) {
		tf.struct = tf.getFactor().struct;
	}

	public void visit(TermMulopFactor tmf) {
	
		Struct typeMfl = Tab.noType;
		Struct typeF = Tab.noType;

		if (tmf.struct != Tab.noType) {
			typeMfl = findType(tmf.getMulopFactorList().struct);

			typeF = findType(tmf.getFactor().struct);

			if (typeF != typeMfl) {
				report_error("Greska TermMulopFactor  " + typeF.getKind() + " "
						+ typeMfl.getKind(), tmf);
			}

		}
		tmf.struct = typeF;

	}

	public void visit(EmptyAddopTermList eatl) {
		eatl.struct = Tab.noType;
	}

	public void visit(NotEmptyAddopTermList neatl) {
		if (neatl.getAddopTermList().struct != Tab.noType)
			if (neatl.getTerm().struct != neatl.getAddopTermList().struct)
				report_error("Greska neatl", neatl);

		neatl.struct = neatl.getTerm().struct;
	}

	public void visit(PositiveExpr posexpr) {

		Struct typeAtl = Tab.noType;
		Struct typeT = Tab.noType;

		if (posexpr.struct != Tab.noType) {
			if (posexpr.getAddopTermList() != null)
				typeAtl = posexpr.getAddopTermList().struct;

			if (posexpr.getTerm() != null)
				typeT = posexpr.getTerm().struct;

			if (typeT != typeAtl && typeAtl != Tab.noType) {
				report_error("Greska posexpr  " + typeT.getKind() + " "
						+ typeAtl.getKind(), posexpr);
			}

		}

		// System.out.println("posexpr kraj atl struct = " +typeT.getKind());
		posexpr.struct = typeT;

	}

	public void visit(NegativeExpr negexpr) {
	

		Struct typeAtl = Tab.noType;
		Struct typeT = Tab.noType;

		if (negexpr.struct != Tab.noType) {
			typeAtl = findType(negexpr.getAddopTermList().struct);

			typeT = findType(negexpr.getTerm().struct);

			if (typeT != typeAtl && typeAtl != Tab.noType) {
				report_error("Greska posexpr  " + typeT.getKind() + " "
						+ typeAtl.getKind(), negexpr);
			}

		}

		// System.out.println("negexpr kraj atl struct = " +typeT.getKind());
		negexpr.struct = typeT;

	}

	// ---------------------Factor---------------------------------------------------
	public void visit(FactorDesignator factordesignator) {
		factordesignator.struct = factordesignator.getDesignator().obj
				.getType();
		if (factordesignator.getDesignator().getDesignatorList() instanceof NotEmptyDesignatorList) {
			if (((NotEmptyDesignatorList) factordesignator.getDesignator()
					.getDesignatorList()).getDesignatorList() instanceof NotEmptyDesignatorList) {
				factordesignator.struct = factordesignator.struct.getElemType()
						.getElemType();
			} else {
				factordesignator.struct = factordesignator.struct.getElemType();
			}
		}
	}

	public void visit(FactorNewExpr fne) {

		if (fne.getExpr().struct.getKind() != Struct.Int) {
			report_error("index mora biti int", fne);
		}

		fne.struct = fne.getType().struct;
		// System.out.println("FactorNewExpr");
	}

	public void visit(FactorNewExprExpr fnee) {
		if (fnee.getExpr().struct.getKind() != Struct.Int
				|| fnee.getExpr1().struct.getKind() != Struct.Int) {
			report_error("index mora biti int", fnee);
		}

		fnee.struct = fnee.getType().struct;
		// System.out.println("FactorNewExprExpr");
	}

	public void visit(FactorExpr factorexpr) {
		factorexpr.struct = factorexpr.getExpr().struct;
	}

	public void visit(FactorNum factorNum) {
		factorNum.struct = Tab.intType;
	}

	public void visit(FactorChar factorChar) {
		factorChar.struct = Tab.charType;
	}

	public void visit(FactorBool factorBool) {
		factorBool.struct = new Struct(Struct.Bool);
	}

	// ------------------Conditions--------------------------------------------

	public void visit(CondFactExpr cfe) {
		// System.out.println(findType(cfe.getExpr().struct).getKind());
		if (findType(cfe.getExpr().struct).getKind() != Struct.Bool) {
			report_error("Condition Expr nije bool", cfe);
		}

	}

	public void visit(CondFactExprExpr cfee) {

		Struct expr1 = cfee.getExpr().struct;
		Struct expr2 = cfee.getExpr1().struct;

		if (!expr1.assignableTo(expr2)) {
			report_error("ne mogu se porediti razliciti tipovi", cfee);
		}

		if (expr1.getKind() == Struct.Array && expr2.getKind() == Struct.Array) {
			if (!(cfee.getRelop() instanceof RelopEE)
					&& !(cfee.getRelop() instanceof RelopNE)) {
				report_error("reference je moguce porediti samo na jednakost",
						cfee);
			}
		}
	}

	public boolean passed() {
		return !errorDetected;
	}

	public void visit(ConditionStart cs) {
		condflag = true;
	}

	public void visit(ConditionEnd ce) {
		condflag = false;
	}

	
	
	//------------------------------------------Methods---------------------------------------------------------------------------
	public void visit(ActParsStart as) {
		//actparscnt = 0;
		//System.out.println("act pars start");
		actParsFlag = true;
	}

	public void visit(ActParsEnd ae) {
		//System.out.println("act pars end");
		// System.out.println("broj parametara je "+actparscnt);
		// actparscnt = 0;
		actParsFlag = false;

	}

	public void visit(ActParMoreExpr apme) {
		actparscnt++;
		String myMethodName = null;
		myMethodName = methodNameStack.peek();
		
		actparsHashmap.put(myMethodName + "" + actparscnt,apme.getExpr().struct); 
		
	}

	public void visit(ActParExpr ape) {
		actparscnt++;
		String myMethodName = null;
		myMethodName = methodNameStack.peek();
		
		actparsHashmap.put(myMethodName + "" + actparscnt,ape.getExpr().struct);
	}

	public void visit(FactorDesignatorActPars fdap) {
		//System.out.println("FactorDesignatoActPars");
		
		Obj node = Tab.find(fdap.getDesignator().getName());
		if (node == Tab.noObj) {
			report_error("Metoda nije deklarisana", fdap);
			return;
		}
		if (node.getKind() != Obj.Meth) {
			report_error("Ovo nije metoda", fdap);
			return;
		}
		if (node.getLevel() != actparscnt) {
			report_error("Pogresan broj parametara: potrebno je " + node.getLevel()+ " a ima " + actparscnt, fdap);
			return;
		}
		//System.out.println("okej broj argumenata " + node.getLevel());
		Collection<Obj> collectionArguments = Tab.find(fdap.getDesignator().getName()).getLocalSymbols();

		for (int i =  1; i <node.getLevel()+1; i++) {
			Struct s = actparsHashmap.get(fdap.getDesignator().getName() + ""+ i);
			for (Obj o : collectionArguments) {
				if (o.getFpPos() == i-1) {
					
					//provera za len
					if(fdap.getDesignator().obj.getName() == "len"){
						actparscnt = actparscnt- node.getLevel() ;
						if(s.getKind() != Struct.Array){
							report_error("parametar nije niz", fdap);
							
						}
						//System.out.println("skinuo metodu " +methodNameStack.pop() +" sa steka");
						fdap.struct = Tab.find(fdap.getDesignator().getName()).getType();
						return;
					}
					
					//provera za ord
					if(fdap.getDesignator().obj.getName() == "ord"){
						actparscnt = actparscnt- node.getLevel() ;
						if(s.getKind() != Struct.Char){
							report_error("parametar nije char", fdap);
							
						}
						//System.out.println("skinuo metodu " +methodNameStack.pop() +" sa steka");
						fdap.struct = Tab.find(fdap.getDesignator().getName()).getType();
						return;
					}
					
					//provera za chr
					if(fdap.getDesignator().obj.getName() == "ord"){
						actparscnt = actparscnt- node.getLevel() ;
						if(s.getKind() != Struct.Int){
							report_error("parametar nije int", fdap);
							
						}
						//System.out.println("skinuo metodu " +methodNameStack.pop() +" sa steka");
						fdap.struct = Tab.find(fdap.getDesignator().getName()).getType();
						return;
					}
					
					
					if (!(o.getType().assignableTo(s))) {
						report_error("Los tip parametra broj " + i, fdap);
						//System.out.println(" tip koji treba " + o.getType().getKind() + "tip je " + s.getKind());

					}else{
						//System.out.println("proveren i dobar argument broj " + i);
					}
					
				}
			}
		
			
		}
		
		actparscnt = actparscnt- node.getLevel() ;
		//System.out.println("skinuo metodu " +methodNameStack.pop() +" sa steka");
		fdap.struct = Tab.find(fdap.getDesignator().getName()).getType();

	}

	public void visit(FactorDesignatorNoActPars fdnap) {
		//System.out.println("FactorDesignatoNoActPars");
		Obj node = Tab.find(fdnap.getDesignator().getName());
		if (node == Tab.noObj) {
			report_error("Metoda nije deklarisana", fdnap);
		} else {
			if (node.getKind() != Obj.Meth) {
				report_error("Ovo nije metoda", fdnap);
			} else {
				if (node.getLevel() != 0) {
					report_error("Pogresan broj parametara: potrebno je "
							+ node.getLevel() + " a ima " + actparscnt, fdnap);
				} else {
					//System.out.println("okej broj argumenata");
				}
			}

		}
		
		//System.out.println("skinuo metodu " +methodNameStack.pop() +" sa steka");
		fdnap.struct = Tab.find(fdnap.getDesignator().getName()).getType();
	}

	public void visit(DesignatorStatementActPars dsap) {
		//System.out.println("DesignatorStatementActPars");
		Obj node = Tab.find(dsap.getDesignator().getName());
		if (node == Tab.noObj) {
			report_error("Metoda nije deklarisana", dsap);
			return;
		}
		if (node.getKind() != Obj.Meth) {
			report_error("Ovo nije metoda", dsap);
			return;
		}
		if (node.getLevel() != actparscnt) {
			report_error("Pogresan broj parametara: potrebno je " + node.getLevel()+ " a ima " + actparscnt, dsap);
			return;
		}
		//System.out.println("okej broj argumenata " + node.getLevel());
		Collection<Obj> collectionArguments = Tab.find(dsap.getDesignator().getName()).getLocalSymbols();

		for (int i =  1; i <node.getLevel()+1; i++) {
			Struct s = actparsHashmap.get(dsap.getDesignator().getName() + ""+ i);
			for (Obj o : collectionArguments) {
				if (o.getFpPos() == i-1) {
					if (!(o.getType().assignableTo(s))  ) {
						report_error("Los tip parametra broj " + i, dsap);
						//System.out.println(" tip koji treba " + o.getType().getKind() + "tip je " + s.getKind());

					}else{
						//System.out.println("proveren i dobar argument broj " + i);
					}
					
				}
			}
		
			
		}
		
		actparscnt = actparscnt- node.getLevel() ;
		//System.out.println("skinuo metodu " +methodNameStack.pop() +" sa steka");

	}

	public void visit(DesignatorStatementNoActPars dsnap) {
		//System.out.println("DesignatorStatementNoActPars");
		Obj node = Tab.find(dsnap.getDesignator().getName());
		if (node == Tab.noObj) {
			report_error("Metoda nije deklarisana", dsnap);
		} else {
			if (node.getKind() != Obj.Meth) {
				report_error("Ovo nije metoda", dsnap);
			} else {
				if (node.getLevel() != 0) {
					report_error("Pogresan broj parametara: potrebno je "
							+ node.getLevel() + " a ima " + actparscnt, dsnap);
				} else {
					//System.out.println("okej broj argumenata");
				}
			}

		}
		
		
		//System.out.println("skinuo metodu " +methodNameStack.pop() +" sa steka");

	}

	//---------------------------------------Statements--------------------------------------------------------
	
	
	public void visit(LoopStart loopstart){
		loopLevel ++;
	}
	
	public void visit(WhileStatement whilestmt){
		loopLevel --;
	}
	
	public void visit(BreakStatement breakstmt){
		if(loopLevel == 0){
			report_error("Break mora biti u petlji", breakstmt);
		}
		
	}
	
	public void visit(ContinueStatement continuestmt){
		if(loopLevel == 0){
			report_error("Continue mora biti u petlji", continuestmt);
		}
	}
	
	
	public void visit(ReturnStatement returnstmt){
		returnFound = true;
		if(methodRetType.getKind()!= returnstmt.getExpr().struct.getKind()){
			report_error("Pogresan tip povratne vrednosti", returnstmt);
		}
	}
	
	public void visit(ReturnVoidStatement returnstmt){
		returnFound = true;
		
		if(methodRetType.getKind() != Struct.None){
			report_error("Metoda mora vratiti povratnu vrednost", returnstmt);

		}
	}
	
	public void visit(Temp temp){
		//System.out.println("temp pass");
	}
	
	
	
	
}
