package pt.um.lei.masb.agent.messaging.transaction

import jade.content.onto.BeanOntology
import pt.um.lei.masb.agent.messaging.block.BlockOntology
import pt.um.lei.masb.agent.messaging.data.DataOntology

object TransactionOntology : BeanOntology(
    "JTransaction-Ontology",
    arrayOf(DataOntology, BlockOntology)
) {

    const val ONTOLOGY_NAME = "JTransaction-Ontology"

    init {
        //Ontology made up of all the transaction classes.
        add("pt.um.lei.masb.agent.messaging.transaction")
        add("pt.um.lei.masb.agent.messaging.transaction.ontology.actions")
        add("pt.um.lei.masb.agent.messaging.transaction.ontology.concepts")
    }
}
