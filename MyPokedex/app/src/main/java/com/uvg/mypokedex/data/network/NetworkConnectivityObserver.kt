package com.uvg.mypokedex.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Interface que define el contrato para observar la conectividad de red.
 */
interface ConnectivityObserver {
    fun observe(): Flow<Status>

    enum class Status {
        AVAILABLE,      // Hay conexión a internet
        UNAVAILABLE,    // No hay conexión
        LOSING,         // Perdiendo conexión
        LOST            // Conexión perdida
    }
}

/**
 * Implementación del observador de conectividad usando ConnectivityManager.
 * Utiliza NetworkCallback para detectar cambios en tiempo real.
 */
class NetworkConnectivityObserver(
    private val context: Context
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Retorna un Flow que emite el estado de conectividad actual.
     * El Flow se mantiene activo y emite cambios automáticamente.
     */
    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(ConnectivityObserver.Status.AVAILABLE)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                trySend(ConnectivityObserver.Status.LOSING)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(ConnectivityObserver.Status.LOST)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(ConnectivityObserver.Status.UNAVAILABLE)
            }
        }

        // Request para escuchar todos los tipos de red
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        // Registrar el callback
        connectivityManager.registerNetworkCallback(request, callback)

        // Enviar el estado inicial
        trySend(getCurrentConnectivityStatus())

        // Cleanup cuando el Flow se cancela
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged() // Solo emite si el estado cambió

    /**
     * Obtiene el estado actual de conectividad de forma síncrona.
     */
    private fun getCurrentConnectivityStatus(): ConnectivityObserver.Status {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return if (capabilities != null &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ) {
            ConnectivityObserver.Status.AVAILABLE
        } else {
            ConnectivityObserver.Status.UNAVAILABLE
        }
    }

    /**
     * Método auxiliar para verificar si hay conexión actualmente.
     */
    fun isCurrentlyConnected(): Boolean {
        return getCurrentConnectivityStatus() == ConnectivityObserver.Status.AVAILABLE
    }
}
