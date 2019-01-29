package pt.um.lei.masb.agent.behaviours

import controller.clients.OwmClient
import controller.clients.TomtomClient
import jade.core.Agent
import jade.core.behaviours.TickerBehaviour

class CaptureData(
        a: Agent,
        period: Long,
        private val arr: Array<String>
): TickerBehaviour(a, period) {

    override fun onTick() {

        if(arr[1].compareTo("UV")==0){
            var uv = OwmClient.getParallCurrentUltraviolet(arr[2], arr[3])
            println(uv.toString())
        } else if(arr[1].compareTo("TRAFFIC")==0) {
            var traffic = TomtomClient.getParallTrafficIncidents(arr[2], arr[3], arr[4], arr[5])
            println(traffic.toString())
        }

    }

}