package pt.um.lei.masb.agent.messaging.data.ontology.concepts

import jade.content.Concept

data class JPhysicalData(
    var data: String,
    var instant: String,
    var lat: String,
    var lng: String
) : Concept
