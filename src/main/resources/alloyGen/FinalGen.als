abstract sig Type{
}

sig Object{
    type : Type,
    constructor : ConstructorCall
}

sig ConstructorCall{
    method: Method,
    params: seq Object
}

abstract sig Call{

}

one sig End extends Call{

}

abstract sig CallWithNext extends Call{
    nextMethod : Call
}

sig MethodCall extends CallWithNext{
    receiver : Object,
    method : Method,
    params : seq Object
}

one sig Begin extends CallWithNext{
}

abstract sig Method {
    paramTypes : seq Type,
    receiverType : Type
}



------ Method Constraints-------


-- Il doit y avoir un et un seul method.next= end
fact{
    one m : MethodCall | m.nextMethod in End
}

-- un method call ne peut pas se link lui même
fact{
    all mc : MethodCall | mc.nextMethod!=mc
}

-- le receveur est du bon type
fact{
    all mc : MethodCall |  mc.receiver.type=mc.method.receiverType
}

-- Un call ne peut pas etre fait deux fois
fact{
all mc: MethodCall | no mc2 : MethodCall |  (mc2 in mc.^nextMethod) && (mc2.nextMethod=mc)
}

-- Tous les methodCall ont été appelés par un Call
fact{
    all mc : MethodCall | one c : CallWithNext | c.nextMethod=mc
}

-- Un objet qui appelle une methode a le bon type
fact{
    all mc:MethodCall | mc.receiver.type=mc.method.receiverType
}


------- Param Constraints ------


-- verification des types (declaration methode/appel methode)
fact{
    all mc : MethodCall |validParam[mc.method,mc]
}

pred validParam[method : Method, call : MethodCall]{
	call.params.type=method.paramTypes
	#call.params=#method.paramTypes
    all  pt : method.paramTypes.elems | all p : call.params.elems | call.params.idxOf[p]=method.paramTypes.idxOf[pt] implies pt=p.type
}

-------- Primitive Types ------------

one sig Gen_Double extends Type{}
one sig Gen_Integer extends Type{}
one sig Gen_Float extends Type{}
one sig Gen_Boolean extends Type{}
one sig Gen_Byte extends Type{}
one sig Gen_Character extends Type{}
one sig Gen_Long extends Type{}
one sig Gen_Short extends Type{}
one sig ppc_Jeu_joueUnTour_52 extends Method{}
fact{
#ppc_Jeu_joueUnTour_52.paramTypes=2
ppc_Jeu_joueUnTour_52.paramTypes[0]=ppc_util_Joueur
ppc_Jeu_joueUnTour_52.paramTypes[1]=ppc_util_Joueur
ppc_Jeu_joueUnTour_52.receiverType=ppc_Jeu
}


one sig ppc_Jeu_readCoup_28 extends Method{}
fact{
#ppc_Jeu_readCoup_28.paramTypes=1
ppc_Jeu_readCoup_28.paramTypes[0]=java_lang_String
ppc_Jeu_readCoup_28.receiverType=ppc_Jeu
}


one sig ppc_Jeu_joueUnePartie_73 extends Method{}
fact{
#ppc_Jeu_joueUnePartie_73.paramTypes=2
ppc_Jeu_joueUnePartie_73.paramTypes[0]=ppc_util_Joueur
ppc_Jeu_joueUnePartie_73.paramTypes[1]=ppc_util_Joueur
ppc_Jeu_joueUnePartie_73.receiverType=ppc_Jeu
}


one sig ppc_Jeu_init_19 extends ConstructorCall{}
fact{
#ppc_Jeu_init_19.params=1
ppc_Jeu_init_19.params[0].type=Gen_Integer
}


one sig ppc_util_Input_readInt_31 extends Method{}
fact{
#ppc_util_Input_readInt_31.paramTypes=0
ppc_util_Input_readInt_31.receiverType=ppc_util_Input
}


one sig ppc_util_Input_readString_21 extends Method{}
fact{
#ppc_util_Input_readString_21.paramTypes=0
ppc_util_Input_readString_21.receiverType=ppc_util_Input
}


one sig ppc_util_Input_init_12 extends ConstructorCall{}
fact{
#ppc_util_Input_init_12.params=0
}


one sig ppc_util_Joueur_ajoutePoints_82 extends Method{}
fact{
#ppc_util_Joueur_ajoutePoints_82.paramTypes=1
ppc_util_Joueur_ajoutePoints_82.paramTypes[0]=Gen_Integer
ppc_util_Joueur_ajoutePoints_82.receiverType=ppc_util_Joueur
}


one sig ppc_util_Joueur_toString_55 extends Method{}
fact{
#ppc_util_Joueur_toString_55.paramTypes=0
ppc_util_Joueur_toString_55.receiverType=ppc_util_Joueur
}


one sig ppc_util_Joueur_joueUnCoup_90 extends Method{}
fact{
#ppc_util_Joueur_joueUnCoup_90.paramTypes=0
ppc_util_Joueur_joueUnCoup_90.receiverType=ppc_util_Joueur
}


one sig ppc_util_Joueur_getScore_47 extends Method{}
fact{
#ppc_util_Joueur_getScore_47.paramTypes=0
ppc_util_Joueur_getScore_47.receiverType=ppc_util_Joueur
}


one sig ppc_util_Joueur_compareScores_64 extends Method{}
fact{
#ppc_util_Joueur_compareScores_64.paramTypes=1
ppc_util_Joueur_compareScores_64.paramTypes[0]=ppc_util_Joueur
ppc_util_Joueur_compareScores_64.receiverType=ppc_util_Joueur
}


one sig ppc_util_Joueur_init_27 extends ConstructorCall{}
fact{
#ppc_util_Joueur_init_27.params=1
ppc_util_Joueur_init_27.params[0].type=java_lang_String
}


one sig ppc_util_Joueur_init_37 extends ConstructorCall{}
fact{
#ppc_util_Joueur_init_37.params=2
ppc_util_Joueur_init_37.params[0].type=java_lang_String
ppc_util_Joueur_init_37.params[1].type=ppc_util_Strategie
}


one sig ppc_util_StrategieAleatoire_coupAJouer_14 extends Method{}
fact{
#ppc_util_StrategieAleatoire_coupAJouer_14.paramTypes=0
ppc_util_StrategieAleatoire_coupAJouer_14.receiverType=ppc_util_StrategieAleatoire
}


one sig ppc_util_StrategieAleatoire_init_8 extends ConstructorCall{}
fact{
#ppc_util_StrategieAleatoire_init_8.params=0
}


one sig ppc_util_StrategieHumaine_litCoup_13 extends Method{}
fact{
#ppc_util_StrategieHumaine_litCoup_13.paramTypes=1
ppc_util_StrategieHumaine_litCoup_13.paramTypes[0]=java_lang_String
ppc_util_StrategieHumaine_litCoup_13.receiverType=ppc_util_StrategieHumaine
}


one sig ppc_util_StrategieHumaine_coupAJouer_36 extends Method{}
fact{
#ppc_util_StrategieHumaine_coupAJouer_36.paramTypes=0
ppc_util_StrategieHumaine_coupAJouer_36.receiverType=ppc_util_StrategieHumaine
}


one sig ppc_util_StrategieHumaine_init_6 extends ConstructorCall{}
fact{
#ppc_util_StrategieHumaine_init_6.params=0
}


one sig ppc_util_StrategiePapier_coupAJouer_12 extends Method{}
fact{
#ppc_util_StrategiePapier_coupAJouer_12.paramTypes=0
ppc_util_StrategiePapier_coupAJouer_12.receiverType=ppc_util_StrategiePapier
}


one sig ppc_util_StrategiePapier_init_6 extends ConstructorCall{}
fact{
#ppc_util_StrategiePapier_init_6.params=0
}


one sig ppc_util_StrategiePierre_coupAJouer_12 extends Method{}
fact{
#ppc_util_StrategiePierre_coupAJouer_12.paramTypes=0
ppc_util_StrategiePierre_coupAJouer_12.receiverType=ppc_util_StrategiePierre
}


one sig ppc_util_StrategiePierre_init_6 extends ConstructorCall{}
fact{
#ppc_util_StrategiePierre_init_6.params=0
}


one sig ppc_util_Coup_compareCoups_17 extends Method{}
fact{
#ppc_util_Coup_compareCoups_17.paramTypes=1
ppc_util_Coup_compareCoups_17.paramTypes[0]=ppc_util_Coup
ppc_util_Coup_compareCoups_17.receiverType=ppc_util_Coup
}


one sig ppc_util_Coup_init_6 extends ConstructorCall{}
fact{
#ppc_util_Coup_init_6.params=0
}


one sig ppc_util_Strategie extends Type{}
one sig java_lang_String extends Type{}
one sig ppc_Jeu extends Type{}
one sig ppc_util_Joueur extends Type{}
one sig ppc_util_StrategieAleatoire extends Type{}
one sig ppc_util_StrategieHumaine extends Type{}
one sig ppc_util_StrategiePierre extends Type{}
one sig ppc_util_Coup extends Type{}
one sig ppc_util_Input extends Type{}
one sig ppc_util_StrategiePapier extends Type{}

run {} for 10
