package com.neverik.vman.model

import java.util.prefs.Preferences

abstract class ConfigModel: Model {
    private val prefs: Preferences = Preferences.userNodeForPackage(ConfigModel::class.java)

    override val state: Map<Long, VM> = (fun(): Map<Long, VM> {
        if(!prefs.nodeExists("vms")) return emptyMap()
        val map = mutableMapOf<Long, VM>()
        val node = prefs.node("vms")
        for(vmNodeName in node.childrenNames()) {
            val vmNode = node.node(vmNodeName)
            val vm = VM(
                    vmNode.get("name", "No name"),
                    vmNode.get("virtualDisk", ""),
                    vmNode.get("startupDisk", ""),
                    vmNode.getDouble("ramSize", 100.0),
                    vmNode.getLong("id", 0)
            )
            map[vmNodeName.toLong()] = vm
        }
        return map
    })()

    override fun commit(vms: Map<Long, VM>) {
        prefs.node("vms").removeNode()
        val node = prefs.node("vms")
        for(pair in vms) {
            val vmNode = node.node(pair.key.toString())
            vmNode.put("name", pair.value.name)
            vmNode.put("virtualDisk", pair.value.virtualDisk)
            vmNode.put("startupDisk", pair.value.startupDisk)
            vmNode.putDouble("ramSize", pair.value.ramSize)
            vmNode.putLong("id", pair.value.id)
        }
        prefs.flush()
    }
}

