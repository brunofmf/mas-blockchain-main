package pt.um.lei.masb.agent

import jade.core.Agent
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.domain.FIPAException
import mu.KLogging
import pt.um.lei.masb.agent.behaviours.CaptureData
import pt.um.lei.masb.agent.behaviours.SlaveMessage

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

        //Add Messaging Behaviours
        this.addBehaviour(SlaveMessage())

        //Add Data Capture Behaviour
        this.addBehaviour(CaptureData(this, arguments[0].toString().toLong(), arguments as Array<String>))
    }

    companion object : KLogging()
}