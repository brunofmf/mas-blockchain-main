package pt.um.lei.masb.agent.messaging.data

import jade.content.onto.BasicOntology
import jade.content.onto.BeanOntology

object DataOntology : BeanOntology("JData-Ontology", BasicOntology.getInstance()) {

    const val ONTOLOGY_NAME = "JData-Ontology"

    init {
        add("pt.um.lei.masb.agent.messaging.data")
        add("pt.um.lei.masb.agent.messaging.data.ontology.actions")
        add("pt.um.lei.masb.agent.messaging.data.ontology.concepts")
    }
}