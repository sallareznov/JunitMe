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
one sig naval_Bateau_toString_48 extends Method{}
fact{
#naval_Bateau_toString_48.paramTypes=0
naval_Bateau_toString_48.receiverType=naval_Bateau
}


one sig naval_Bateau_taille_17 extends Method{}
fact{
#naval_Bateau_taille_17.paramTypes=0
naval_Bateau_taille_17.receiverType=naval_Bateau
}


one sig naval_Bateau_estCoule_23 extends Method{}
fact{
#naval_Bateau_estCoule_23.paramTypes=0
naval_Bateau_estCoule_23.receiverType=naval_Bateau
}


one sig naval_Bateau_getVie_42 extends Method{}
fact{
#naval_Bateau_getVie_42.paramTypes=0
naval_Bateau_getVie_42.receiverType=naval_Bateau
}


one sig naval_Bateau_equals_56 extends Method{}
fact{
#naval_Bateau_equals_56.paramTypes=1
naval_Bateau_equals_56.paramTypes[0]=java_lang_Object
naval_Bateau_equals_56.receiverType=naval_Bateau
}


one sig naval_Bateau_touche_34 extends Method{}
fact{
#naval_Bateau_touche_34.paramTypes=0
naval_Bateau_touche_34.receiverType=naval_Bateau
}


one sig naval_Bateau_init_9 extends ConstructorCall{}
fact{
#naval_Bateau_init_9.params=1
naval_Bateau_init_9.params[0].type=Gen_Integer
}


one sig naval_Case_getBateau_50 extends Method{}
fact{
#naval_Case_getBateau_50.paramTypes=0
naval_Case_getBateau_50.receiverType=naval_Case
}


one sig naval_Case_getCaractere_83 extends Method{}
fact{
#naval_Case_getCaractere_83.paramTypes=1
naval_Case_getCaractere_83.paramTypes[0]=Gen_Boolean
naval_Case_getCaractere_83.receiverType=naval_Case
}


one sig naval_Case_aEteVisee_66 extends Method{}
fact{
#naval_Case_aEteVisee_66.paramTypes=0
naval_Case_aEteVisee_66.receiverType=naval_Case
}


one sig naval_Case_vise_73 extends Method{}
fact{
#naval_Case_vise_73.paramTypes=0
naval_Case_vise_73.receiverType=naval_Case
}


one sig naval_Case_setBateau_58 extends Method{}
fact{
#naval_Case_setBateau_58.paramTypes=1
naval_Case_setBateau_58.paramTypes[0]=naval_Bateau
naval_Case_setBateau_58.receiverType=naval_Case
}


one sig naval_Case_init_41 extends ConstructorCall{}
fact{
#naval_Case_init_41.params=0
}


one sig naval_DirectionEst_positionSuivante_12 extends Method{}
fact{
#naval_DirectionEst_positionSuivante_12.paramTypes=1
naval_DirectionEst_positionSuivante_12.paramTypes[0]=naval_Position
naval_DirectionEst_positionSuivante_12.receiverType=naval_DirectionEst
}


one sig naval_DirectionEst_init_6 extends ConstructorCall{}
fact{
#naval_DirectionEst_init_6.params=0
}


one sig naval_DirectionNord_positionSuivante_12 extends Method{}
fact{
#naval_DirectionNord_positionSuivante_12.paramTypes=1
naval_DirectionNord_positionSuivante_12.paramTypes[0]=naval_Position
naval_DirectionNord_positionSuivante_12.receiverType=naval_DirectionNord
}


one sig naval_DirectionNord_init_6 extends ConstructorCall{}
fact{
#naval_DirectionNord_init_6.params=0
}


one sig naval_DirectionOuest_positionSuivante_12 extends Method{}
fact{
#naval_DirectionOuest_positionSuivante_12.paramTypes=1
naval_DirectionOuest_positionSuivante_12.paramTypes[0]=naval_Position
naval_DirectionOuest_positionSuivante_12.receiverType=naval_DirectionOuest
}


one sig naval_DirectionOuest_init_6 extends ConstructorCall{}
fact{
#naval_DirectionOuest_init_6.params=0
}


one sig naval_DirectionSud_positionSuivante_12 extends Method{}
fact{
#naval_DirectionSud_positionSuivante_12.paramTypes=1
naval_DirectionSud_positionSuivante_12.paramTypes[0]=naval_Position
naval_DirectionSud_positionSuivante_12.receiverType=naval_DirectionSud
}


one sig naval_DirectionSud_init_6 extends ConstructorCall{}
fact{
#naval_DirectionSud_init_6.params=0
}


one sig naval_Jeu_init_18 extends Method{}
fact{
#naval_Jeu_init_18.paramTypes=0
naval_Jeu_init_18.receiverType=naval_Jeu
}


one sig naval_Jeu_joue_24 extends Method{}
fact{
#naval_Jeu_joue_24.paramTypes=0
naval_Jeu_joue_24.receiverType=naval_Jeu
}


one sig naval_Jeu_init_14 extends ConstructorCall{}
fact{
#naval_Jeu_init_14.params=2
naval_Jeu_init_14.params[0].type=Gen_Integer
naval_Jeu_init_14.params[1].type=Gen_Integer
}


one sig naval_Mer_poseBateau_87 extends Method{}
fact{
#naval_Mer_poseBateau_87.paramTypes=3
naval_Mer_poseBateau_87.paramTypes[0]=naval_Bateau
naval_Mer_poseBateau_87.paramTypes[1]=naval_Position
naval_Mer_poseBateau_87.paramTypes[2]=naval_Direction
naval_Mer_poseBateau_87.receiverType=naval_Mer
}


one sig naval_Mer_vise_26 extends Method{}
fact{
#naval_Mer_vise_26.paramTypes=1
naval_Mer_vise_26.paramTypes[0]=naval_Position
naval_Mer_vise_26.receiverType=naval_Mer
}


one sig naval_Mer_renvoieCase_52 extends Method{}
fact{
#naval_Mer_renvoieCase_52.paramTypes=1
naval_Mer_renvoieCase_52.paramTypes[0]=naval_Position
naval_Mer_renvoieCase_52.receiverType=naval_Mer
}


one sig naval_Mer_getPlateau_19 extends Method{}
fact{
#naval_Mer_getPlateau_19.paramTypes=0
naval_Mer_getPlateau_19.receiverType=naval_Mer
}


one sig naval_Mer_affichage_64 extends Method{}
fact{
#naval_Mer_affichage_64.paramTypes=1
naval_Mer_affichage_64.paramTypes[0]=Gen_Boolean
naval_Mer_affichage_64.receiverType=naval_Mer
}


one sig naval_Mer_init_8 extends ConstructorCall{}
fact{
#naval_Mer_init_8.params=2
naval_Mer_init_8.params[0].type=Gen_Integer
naval_Mer_init_8.params[1].type=Gen_Integer
}


one sig naval_Position_toString_46 extends Method{}
fact{
#naval_Position_toString_46.paramTypes=0
naval_Position_toString_46.receiverType=naval_Position
}


one sig naval_Position_getY_38 extends Method{}
fact{
#naval_Position_getY_38.paramTypes=0
naval_Position_getY_38.receiverType=naval_Position
}


one sig naval_Position_equals_54 extends Method{}
fact{
#naval_Position_equals_54.paramTypes=1
naval_Position_equals_54.paramTypes[0]=naval_Position
naval_Position_equals_54.receiverType=naval_Position
}


one sig naval_Position_getX_30 extends Method{}
fact{
#naval_Position_getX_30.paramTypes=0
naval_Position_getX_30.receiverType=naval_Position
}


one sig naval_Position_init_21 extends ConstructorCall{}
fact{
#naval_Position_init_21.params=2
naval_Position_init_21.params[0].type=Gen_Integer
naval_Position_init_21.params[1].type=Gen_Integer
}


one sig naval_Reponse_init_6 extends ConstructorCall{}
fact{
#naval_Reponse_init_6.params=0
}


one sig naval_DirectionNord extends Type{}
one sig naval_Position extends Type{}
one sig naval_Jeu extends Type{}
one sig naval_Mer extends Type{}
one sig naval_Bateau extends Type{}
one sig naval_DirectionSud extends Type{}
one sig naval_DirectionOuest extends Type{}
one sig java_lang_Object extends Type{}
one sig naval_Case extends Type{}
one sig naval_DirectionEst extends Type{}
one sig naval_Direction extends Type{}
one sig naval_Reponse extends Type{}

run {} for 12
