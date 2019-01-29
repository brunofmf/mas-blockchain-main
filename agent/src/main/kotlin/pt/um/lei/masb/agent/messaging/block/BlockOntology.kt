package pt.um.lei.masb.agent.messaging.block

import jade.content.onto.BasicOntology
import jade.content.onto.BeanOntology

object BlockOntology : BeanOntology("JBlock-Ontology", BasicOntology.getInstance()) {

    const val ONTOLOGY_NAME = "JBlock-Ontology"

    init {
        add("pt.um.lei.masb.agent.messaging.block")
        add("pt.um.lei.masb.agent.messaging.block.ontology.actions")
        add("pt.um.lei.masb.agent.messaging.block.ontology.concepts")
        add("pt.um.lei.masb.agent.messaging.block.ontology.predicates")
    }
}
