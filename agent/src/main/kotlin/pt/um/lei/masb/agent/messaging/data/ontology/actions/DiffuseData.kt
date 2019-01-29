package pt.um.lei.masb.agent.messaging.data.ontology.actions

import jade.content.AgentAction
import pt.um.lei.masb.agent.messaging.data.ontology.concepts.JPhysicalData

data class DiffuseData (
        var data : JPhysicalData
) : AgentAction