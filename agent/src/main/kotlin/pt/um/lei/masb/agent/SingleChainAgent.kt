package pt.um.lei.masb.agent

import jade.core.Agent
import jade.core.behaviours.ParallelBehaviour
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.domain.FIPAException
import mu.KLogging
import pt.um.lei.masb.agent.behaviours.GetMissingBlocks
import pt.um.lei.masb.agent.behaviours.Mining
import pt.um.lei.masb.agent.behaviours.SendMessages
import pt.um.lei.masb.agent.data.AgentPeers
import pt.um.lei.masb.blockchain.*
import pt.um.lei.masb.blockchain.data.PollutionOWM
import pt.um.lei.masb.blockchain.data.TemperatureData
import pt.um.lei.masb.blockchain.utils.RingBuffer
import java.util.*

@Suppress("UNCHECKED_CAST")
class SingleChainAgent : Agent() {

    //agent will try maximizing reward
    //sessionRewards could later be translated into user reward
    private var sessionRewards: Double = 0.toDouble()
    private var bl: Queue<Block> = ArrayDeque<Block>(6)
    private val toSend: RingBuffer<Transaction> = RingBuffer(3)

    override fun setup() {
        val sc = arguments[0] as SideChain
        val dfd = DFAgentDescription()
        dfd.name = aid
        try {
            DFService.register(this, dfd)
        } catch (fe: FIPAException) {
            logger.error(fe) {}
        }

        val agentPeers = AgentPeers(this)

        val i = Ident
        sessionRewards = 0.0


        /*val sc = bc.getSideChainOf(cl)
        val cl = arguments[1] as Class<out BlockChainData>
        val sc = bc.getSideChainOf(cl)
            ?: bc.registerSideChainOf(cl, arguments[2] as String)
                .getSideChainOf(cl)
            ?: throw ClassNotFoundException("SideChain failed to be materialized")
        */

        val b = object : ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL) {
            override fun onEnd(): Int {
                logger.info("Session Closed")
                return 0
            }
        }

        //b.addSubBehaviour(GetMissingBlocks(sc, agentPeers))
        //b.addSubBehaviour(ReceiveMessages(sc, agentPeers, TemperatureData::class.java))
        //b.addSubBehaviour(Mining(sc, bl))
        b.addSubBehaviour(SendMessages(sc, toSend, agentPeers, PollutionOWM::class.java))
        addBehaviour(b)
    }

    companion object : KLogging()

}
