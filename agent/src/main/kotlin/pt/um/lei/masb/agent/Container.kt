package pt.um.lei.masb.agent

import jade.core.Profile
import jade.core.ProfileImpl
import jade.core.Runtime
import jade.wrapper.ContainerController
import mu.KLogging
import pt.um.lei.masb.blockchain.BlockChain
import pt.um.lei.masb.blockchain.SideChain
import pt.um.lei.masb.blockchain.data.PollutionOWM
import pt.um.lei.masb.blockchain.data.TemperatureData
import pt.um.lei.masb.blockchain.emptyHash

class Container {
    private lateinit var rt: Runtime
    private lateinit var container: ContainerController

    fun initMainContainerInPlatform(
            host: String,
            port: String,
            containerName: String
    ) {
        // Get the JADE runtime interface (singleton)
        this.rt = Runtime.instance()

        // Create a Profile, where the launch arguments are stored
        val prof = ProfileImpl()
        prof.setParameter(Profile.CONTAINER_NAME, containerName)
        prof.setParameter(Profile.MAIN_HOST, host)
        prof.setParameter(Profile.MAIN_PORT, port)
        prof.setParameter(Profile.MAIN, "true")
        prof.setParameter(Profile.GUI, "true")

        // create a main agent container
        this.container = rt.createMainContainer(prof)
        rt.setCloseVM(true)
    }

    fun initContainerInPlatform(
        host: String,
        port: String,
        containerName: String
    ): ContainerController {
        // Get the JADE runtime interface (singleton)
        this.rt = Runtime.instance()

        // Create a Profile, where the launch arguments are stored
        val profile = ProfileImpl()
        profile.setParameter(Profile.CONTAINER_NAME, containerName)
        profile.setParameter(Profile.MAIN_HOST, host)
        profile.setParameter(Profile.MAIN_PORT, port)

        // create a non-main agent container
        return rt.createAgentContainer(profile)
    }

    fun startAEInPlatform(
        cc: ContainerController,
        name: String,
        classpath: String,
        sc: SideChain
    ) {
        try {
            val ac = cc.createNewAgent(name, classpath, arrayOf<Any>(sc))
            ac.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startSlaveAgentInPlatform(
        cc: ContainerController,
        name: String,
        classpath: String,
        arr: Array<String>
    ) {
        try {
            val ac = cc.createNewAgent(name, classpath, arr)
            ac.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object : KLogging() {
        @JvmStatic
        fun main(args: Array<String>) {

            val a = Container()
            val bc = BlockChain.getBlockChainByHash(hash = emptyHash()) ?: BlockChain(id = "smarthub")

            val sc = bc.registerSideChainOf(PollutionOWM::class.java).getSideChainOf(PollutionOWM::class.java)
                    ?: throw ClassNotFoundException("SideChain failed to be materialized")

            //Init Main Container
            a.initMainContainerInPlatform(
                "localhost",
                "9888",
                "TheMainContainer"
            )

            //Init Ledger Containers
            val ld1 = a.initContainerInPlatform(
                "localhost",
                "9889",
                "LedgerContainer_1"
            )
/*
            val ld2 = a.initContainerInPlatform(
                "localhost",
                "9890",
                "LedgerContainer_2"
            )
*/
            val ld3 = a.initContainerInPlatform(
                "localhost",
                "9891",
                "LedgerContainer_3"
            )
/*
            val ld4 = a.initContainerInPlatform(
                "localhost",
                "9892",
                "LedgerContainer_4"
            )

            val ld5 = a.initContainerInPlatform(
                "localhost",
                "9893",
                "LedgerContainer_5"
            )
*/
            //Init Ledger Agents
            a.startAEInPlatform(
                ld1,
                "Ld1",
                "pt.um.lei.masb.agent.SingleChainAgent",
                sc
            )
/*
            a.startAEInPlatform(
                ld2,
                "Ld2",
                "pt.um.lei.masb.agent.SingleChainAgent",
                sc
            )
*/
            a.startAEInPlatform(
                ld3,
                "Ld3",
                "pt.um.lei.masb.agent.SingleChainAgent",
                sc
            )
/*
            a.startAEInPlatform(
                ld4,
                "Ld4",
                "pt.um.lei.masb.agent.SingleChainAgent",
                sc
            )

            a.startAEInPlatform(
                ld5,
                "Ld5",
                "pt.um.lei.masb.agent.SingleChainAgent",
                sc
            )
*/
            //Init Slave Agents in Ledger Containers
            a.startSlaveAgentInPlatform(
                ld1,
                "Ld1_SlaveAgent_Uv_Braga",
                "pt.um.lei.masb.agent.SlaveAgent",
                arrayOf("10000", "UV", "41.54", "-8.43")
            )
/*
            a.startSlaveAgentInPlatform(
                ld2,
                "Ld2_SlaveAgent_Uv_Guimaraes",
                "pt.um.lei.masb.agent.SlaveAgent",
                arrayOf("10000", "UV", "41.44", "-8.29")
            )
*/
            a.startSlaveAgentInPlatform(
                ld3,
                "Ld3_SlaveAgent_Traffic_Braga",
                "pt.um.lei.masb.agent.SlaveAgent",
                arrayOf("10000", "TRAFFIC", "41.506531", "-8.451247", "41.574115", "-8.371253")
            )
/*
            a.startSlaveAgentInPlatform(
                ld4,
                "Ld4_SlaveAgent_Uv_Guimaraes",
                "pt.um.lei.masb.agent.SlaveAgent",
                arrayOf("10000", "UV", "41.44", "-8.29")
            )

            a.startSlaveAgentInPlatform(
                ld5,
                "Ld5_SlaveAgent_Uv_Braga",
                "pt.um.lei.masb.agent.SlaveAgent",
                arrayOf("10000", "UV", "41.54", "-8.43")
            )

            a.startSlaveAgentInPlatform(
                ld5,
                "Ld5_SlaveAgent_Traffic_Braga",
                "pt.um.lei.masb.agent.SlaveAgent",
                arrayOf("10000", "TRAFFIC", "41.506531", "-8.451247", "41.574115", "-8.371253")
            )
*/
        }
    }


}