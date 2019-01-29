package pt.um.lei.masb.agent.behaviours

import controller.clients.OwmClient
import controller.clients.TomtomClient
import jade.content.lang.sl.SLCodec
import jade.core.AID
import jade.core.Agent
import jade.core.behaviours.TickerBehaviour
import jade.lang.acl.ACLMessage
import model.pollution.owm.PollutionOWM_SC
import mu.KLogging
import pt.um.lei.masb.agent.data.convertToJadePhysicalData
import pt.um.lei.masb.agent.messaging.data.DataOntology
import pt.um.lei.masb.agent.messaging.data.ontology.actions.DiffuseData
import pt.um.lei.masb.blockchain.data.PhysicalData
import pt.um.lei.masb.blockchain.data.PollutionOWM
import pt.um.lei.masb.blockchain.data.TrafficIncident
import java.math.BigDecimal
import java.time.Instant

class DataCaptureAndSharing(
        a: Agent,
        period: Long,
        private val arr: Array<String>
): TickerBehaviour(a, period) {

    private val slaveOwner = myAgent.localName.split("_")[0]

    override fun onTick() {
        if(arr[1].compareTo("UV")==0){
            val uv = OwmClient.getParallCurrentUltraviolet(arr[2], arr[3])
            if(uv != null){
                val owmUv = PollutionOWM(
                        uv.lat,
                        uv.lon,
                        uv.date,
                        uv.unit,
                        uv.parameter.name,
                        uv.value,
                        emptyList()
                )
                logger.info {
                    "$owmUv"
                }
                val pdUv = PhysicalData(
                        Instant.ofEpochMilli(owmUv.date),
                        BigDecimal(owmUv.lat),
                        BigDecimal(owmUv.lon),
                        owmUv
                )
                dataSharing(pdUv)
            }
        } else if(arr[1].compareTo("TRAFFIC")==0) {
            val trafficIncidentList = TomtomClient.getParallTrafficIncidents(arr[2], arr[3], arr[4], arr[5])
                if(trafficIncidentList != null && !trafficIncidentList.isEmpty()){
                    val iterate = trafficIncidentList.listIterator()
                    while (iterate.hasNext()) {
                        val aux = iterate.next()
                        val tomtomTrafficIncident = TrafficIncident(
                                aux.trafficLat,
                                aux.trafficLon,
                                aux.date,
                                aux.trafficModelId,
                                aux.id,
                                aux.iconLat,
                                aux.iconLon,
                                aux.incidentCategory,
                                aux.magnitudeOfDelay,
                                aux.clusterSize,
                                aux.description,
                                aux.causeOfAccident,
                                aux.from,
                                aux.to,
                                aux.length,
                                aux.delayInSeconds,
                                aux.affectedRoads
                        )
                        logger.info {
                            "$tomtomTrafficIncident"
                        }
                        val pdTomtomTrafficIncident = PhysicalData(
                                Instant.ofEpochMilli(tomtomTrafficIncident.date),
                                BigDecimal(tomtomTrafficIncident.trafficLat),
                                BigDecimal(tomtomTrafficIncident.trafficLon),
                                tomtomTrafficIncident
                        )
                        dataSharing(pdTomtomTrafficIncident)
                    }
            }
        }
    }

    fun dataSharing(pd: PhysicalData){
        //Get Slave Owner
        val receiver = AID()
        receiver.localName = slaveOwner
        //Codec definition
        val codec = SLCodec()
        //Message Construction
        val msg = ACLMessage(ACLMessage.INFORM)
        msg.language = codec.name
        msg.ontology = DataOntology.name
        myAgent.contentManager.fillContent(
                msg,
                DiffuseData(
                        convertToJadePhysicalData(
                            pd
                        )
                )
        )
        msg.addReceiver(receiver)
        //Send it
        myAgent.send(msg)
    }

    companion object : KLogging()

}