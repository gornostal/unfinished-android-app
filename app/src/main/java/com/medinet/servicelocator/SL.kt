package com.medinet.servicelocator

import android.annotation.SuppressLint
import android.content.Context
import java.util.*

@SuppressLint("StaticFieldLeak")
object SL {

    private val sServicesInstances = HashMap<String, Any>()
    private val sServicesImplementationsMapping = HashMap<String, Class<*>>()

    private var mContext: Context? = null

    private val sServicesInstancesLock = Any()

    /**
     * All Services provided by the Service Locator have to implement this interface.
     * This is just a "marker interface pattern", it can be replaced by an annotation later.
     */
    interface IService

    fun init(context: Context) {
        mContext = context.applicationContext
    }

    /**
     * Return instance of desired class or object that implement desired interface.
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(clazz: Class<T>): T {
        return getService(clazz.name, mContext) as T
    }

    /**
     * This method allows to bind a custom service implementation to an interface.
     *
     * @param interfaceClass      interface
     * @param implementationClass class which implement interface specified in first param
     */
    fun bindCustomServiceImplementation(interfaceClass: Class<*>, implementationClass: Class<*>) {
        synchronized(sServicesInstancesLock) {
            sServicesImplementationsMapping.put(interfaceClass.name, implementationClass)
        }
    }

    private fun getService(name: String, applicationContext: Context?): Any {
        synchronized(sServicesInstancesLock) {
            val o = sServicesInstances[name]
            return o ?: try {
                val clazz: Class<*> = if (sServicesImplementationsMapping.containsKey(name)) {
                    sServicesImplementationsMapping[name]!!
                } else {
                    Class.forName(name)
                }

                val serviceInstance: Any = try {
                    val e1 = clazz.getConstructor(Context::class.java)
                    e1.newInstance(applicationContext)
                } catch (var6: NoSuchMethodException) {
                    val constructor = clazz.getConstructor()
                    constructor.newInstance()
                } as? SL.IService
                    ?: throw IllegalArgumentException("Requested service must implement IService interface")

                sServicesInstances[name] = serviceInstance
                serviceInstance
            } catch (e: ClassNotFoundException) {
                throw IllegalArgumentException("Requested service class was not found: $name", e)
            } catch (e: Exception) {
                throw IllegalArgumentException("Cannot initialize requested service: $name", e)
            }
        }
    }

    interface Creator<T> {
        fun newInstance(context: Context): T
    }
}