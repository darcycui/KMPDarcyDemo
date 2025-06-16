package com.darcy.kmpdemo.network.ssl

import java.io.ByteArrayInputStream
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLException
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SslSettings {
    // resources中内置证书路径
    const val KEYSTORE_PATH_SERVER = "files/ssl/test2ServerSelf.p12"
    const val KEYSTORE_PATH_IP = "files/ssl/test2IPSelf241.p12"

    /**
     * 初始化证书 certBytes
     */
    var certBytesList: List<ByteArray>? = null

    fun initCertBytes(bytesList: List<ByteArray>) {
        certBytesList = bytesList
    }

    /**
     * 获取 KeyStore
     */
    fun getKeyStore(): KeyStore {
        val certList = certBytesList ?: throw IllegalStateException("Certificates not initialized")
        val keyStorePassword = "1234".toCharArray()
        val keyStore = KeyStore.getInstance("PKCS12").apply {
            // 初始化空KeyStore
            load(null, null)
        }

        certList.forEachIndexed { index, bytes ->
            ByteArrayInputStream(bytes).use { certStream ->
                // 创建临时KeyStore加载单个证书
                val tempKeyStore = KeyStore.getInstance("PKCS12").apply {
                    load(certStream, keyStorePassword)
                }

                // 将证书复制到主KeyStore
                tempKeyStore.aliases().toList().forEach { alias ->
                    when {
                        tempKeyStore.isCertificateEntry(alias) -> {
                            // 处理纯证书条目
                            val cert = tempKeyStore.getCertificate(alias)
                            keyStore.setCertificateEntry("cert_$index", cert)
                        }
                        tempKeyStore.isKeyEntry(alias) -> {
                            // 处理私钥条目（包含证书链）
                            val chain = tempKeyStore.getCertificateChain(alias)
                            chain?.forEachIndexed { chainIndex, cert ->
                                keyStore.setCertificateEntry("cert_${index}_$chainIndex", cert)
                            }
                        }
                    }
                }
            }
        }
        return keyStore
    }

    /**
     * 获取 TrustManagerFactory
     */
    fun getTrustManagerFactory(): TrustManagerFactory? {
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(getKeyStore())
        return trustManagerFactory
    }

    /**
     * 获取 SSLContext
     */
    fun getSslContext(): SSLContext? {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, getTrustManagerFactory()?.trustManagers, null)
        return sslContext
    }

    /**
     * 获取 Cert TrustManager
     */
    fun getTrustManager(): X509TrustManager? {
        // 1. 获取系统默认TrustManager
        val systemTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        systemTrustManagerFactory.init(null as KeyStore?) // 加载系统默认CA
        val systemTrustManager = systemTrustManagerFactory.trustManagers.first { it is X509TrustManager } as X509TrustManager

        // 2. 获取自定义证书TrustManager
        val customTrustManager =
            getTrustManagerFactory()?.trustManagers?.first { it is X509TrustManager } as? X509TrustManager

        // 验证证书链非空
        if (customTrustManager ==null || customTrustManager.acceptedIssuers?.isEmpty() == true) {
            throw SSLException("No trusted certificates found in keystore")
        }

        // 3. 合并两者
        return CompositeX509TrustManager(systemTrustManager, customTrustManager)
    }
}