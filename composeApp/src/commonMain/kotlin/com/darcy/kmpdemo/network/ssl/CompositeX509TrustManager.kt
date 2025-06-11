package com.darcy.kmpdemo.network.ssl

import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

// 合并系统默认CA和自定义证书的TrustManager
class CompositeX509TrustManager(
    private val systemTrustManager: X509TrustManager,
    private val customTrustManager: X509TrustManager
) : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) {
        try {
            customTrustManager.checkClientTrusted(chain, authType)
        } catch (e: CertificateException) {
            systemTrustManager.checkClientTrusted(chain, authType)
        }
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {
        try {
            customTrustManager.checkServerTrusted(chain, authType)
        } catch (e: CertificateException) {
            systemTrustManager.checkServerTrusted(chain, authType)
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return systemTrustManager.acceptedIssuers + customTrustManager.acceptedIssuers
    }
}