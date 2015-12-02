abstract sig Type{}sig Object{    type : Type}abstract sig Call{}one sig End extends Call{}abstract sig CallWithNext extends Call{    nextMethod : Call}sig MethodCall extends CallWithNext{    receiver : Object,    method : Method,    params : seq Object}one sig Begin extends CallWithNext{}abstract sig Method {    paramTypes : seq Type,    receiverType : Type}------ Method Constraints--------- Il doit y avoir un et un seul method.next= endfact{    one m : MethodCall | m.nextMethod in End}-- un method call ne peut pas se link lui mêmefact{    all mc : MethodCall | mc.nextMethod!=mc}-- le receveur est du bon typefact{    all mc : MethodCall |  mc.receiver.type=mc.method.receiverType}-- Un call ne peut pas etre fait deux foisfact{all mc: MethodCall | no mc2 : MethodCall |  (mc2 in mc.^nextMethod) && (mc2.nextMethod=mc)}-- Tous les methodCall ont été appelés par un Callfact{    all mc : MethodCall | one c : CallWithNext | c.nextMethod=mc}-- Un objet qui appelle une methode a le bon typefact{    all mc:MethodCall | mc.receiver.type=mc.method.receiverType}------- Param Constraints -------- verification des types (declaration methode/appel methode)fact{    all mc : MethodCall |validParam[mc.method,mc]}pred validParam[method : Method, call : MethodCall]{	call.params.type=method.paramTypes	#call.params=#method.paramTypes     all  pt : method.paramTypes.elems | all p : call.params.elems | call.params.idxOf[p]=method.paramTypes.idxOf[pt] implies pt=p.type}--------Generic Types------------one sig Gen_String extends Type{}one sig Gen_Integer extends Type{}one sig Gen_Float extends Type{}one sig Gen_Boolean extends Type{}--------generated----------------one sig res_BarInt extends Type{}

one sig res_BarInt_getA_13 extends Method{}
fact{
#res_BarInt_getA_13.paramTypes=0
res_BarInt_getA_13.receiverType=res_BarInt
}

one sig res_BarInt_foo_6 extends Method{}
fact{
#res_BarInt_foo_6.paramTypes=1
res_BarInt_foo_6.paramTypes[0]=Gen_Integer
res_BarInt_foo_6.receiverType=res_BarInt
}

one sig res_FooInt extends Type{}

one sig res_FooInt_getA_16 extends Method{}
fact{
#res_FooInt_getA_16.paramTypes=0
res_FooInt_getA_16.receiverType=res_FooInt
}

one sig res_FooInt_bar_6 extends Method{}
fact{
#res_FooInt_bar_6.paramTypes=1
res_FooInt_bar_6.paramTypes[0]=Gen_Integer
res_FooInt_bar_6.receiverType=res_FooInt
}

one sig res_FooInt_bar_13 extends Method{}
fact{
#res_FooInt_bar_13.paramTypes=1
res_FooInt_bar_13.paramTypes[0]=Gen_String
res_FooInt_bar_13.receiverType=res_FooInt
}


run {} for 8
