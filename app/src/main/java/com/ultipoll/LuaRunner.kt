package com.ultipoll

import android.util.Log
import org.luaj.vm2.*
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*

class LuaRunner {

    public fun runLua(source: String, map: Map<*, *>?, options: List<String>?): Int {
        val L = standardSandbox()          // new instance each time
        L.set("votes" , mapToLuaTable(map))
        L.set("options" , mapToLuaTable(options?.withIndex()?.associate { (i,v) -> i+1 to v }))
        val chunk = L.load(source)
        return chunk.call().toint()
    }
    private fun standardSandbox(): Globals = Globals().apply {
        load(BaseLib()); load(PackageLib()) ;load(MathLib()); load(TableLib())
        LoadState.install(this); LuaC.install(this)
    }


    public fun mapToLuaTable(map: Map<*, *>?): LuaTable?{
        val luaTable = LuaTable()
        if(map == null) return luaTable
        for(mapping in map){
            when(mapping.key){
                is String ->{
                    luaTable.set(mapping.key as String , mapping.value as Int)
                }
                is Int ->{
                    if(mapping.value is String){
                        luaTable.set(mapping.key as Int, LuaValue.valueOf(mapping.value.toString()) )
                        continue
                    }

                    if(mapping.value !is Map<*,*>){
                        Log.e("LuaRunner","Expected Int -> Map<*,*> mapping ")
                        return null
                    }
                    val innerMap = mapping.value as Map<*, *>
                    val innerLuaTable = LuaTable()
                    for(innerMapping in innerMap){
                        when(innerMapping.key){
                            is Int ->{
                                innerLuaTable.set(innerMapping.key as Int, LuaValue.valueOf(innerMapping.value as String))
                            }
                            is String->{
                                innerLuaTable.set(innerMapping.key as String, LuaValue.valueOf(innerMapping.value as Int))
                            }
                        }
                    }
                    luaTable.set(mapping.key as Int , innerLuaTable)
                }
                else ->{
                    Log.e("LuaRunner","Unknown keyType in mapToLuaTable")
                    return null
                }
            }

        }
        return  luaTable
    }
}