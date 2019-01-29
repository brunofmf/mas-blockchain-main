package pt.um.lei.masb.agent.behaviours

import jade.core.AID
import jade.core.behaviours.SimpleBehaviour
import jade.lang.acl.ACLMessage
import mu.KLogging

class SlaveMessage : SimpleBehaviour() {

    override fun action() {
        var receiver = AID()
        receiver.localName = myAgent.localName.split("_")[0]

        var msg = ACLMessage(ACLMessage.INFORM)
        msg.conversationId = ""+System.currentTimeMillis()
        msg.addReceiver(receiver)

        msg.content = "Teste msg"

        myAgent.send(msg)
    }

    override fun done(): Boolean {
        return false
    }

    companion object : KLogging()
}