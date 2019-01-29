package pt.um.lei.masb.agent

import jade.content.lang.sl.SLCodec
import jade.core.Agent
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.domain.FIPAAgentManagement.FIPAManagementOntology
import jade.domain.FIPAException
import jade.domain.JADEAgentManagement.JADEManagementOntology
import jade.domain.introspection.IntrospectionOntology
import jade.domain.mobility.MobilityOntology
import mu.KLogging
import pt.um.lei.masb.agent.behaviours.DataCaptureAndSharing
import pt.um.lei.masb.agent.messaging.data.DataOntology
import pt.um.lei.masb.agent.messaging.transaction.TransactionOntology

class SlaveAgent : Agent() {

    override fun setup() {
        //Agent register
        val dfd = DFAgentDescription()
        dfd.name = aid
        try {
            DFService.register(this, dfd)
        } catch (fe: FIPAException) {
            logger.error("", fe)
        }
        //Register message and ontologies
        val codec = SLCodec()
        this.contentManager.registerLanguage(codec)
        this.contentManager.registerOntology(DataOntology)
        //Add Data Capture Behaviour
        this.addBehaviour(DataCaptureAndSharing(this, arguments[0].toString().toLong(), arguments as Array<String>))
    }

    companion object : KLogging()
}