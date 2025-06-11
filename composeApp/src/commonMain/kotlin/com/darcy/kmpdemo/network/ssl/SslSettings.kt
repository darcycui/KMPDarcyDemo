package com.darcy.kmpdemo.network.ssl

import java.io.ByteArrayInputStream
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLException
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SslSettings {
    // resources中内置证书路径
    const val KEYSTORE_PATH = "files/ssl/test2ServerSelf.p12"
    var certBytes: ByteArray? = null

    /**
     * 初始化证书 certBytes
     */
    fun initCertBytes(bytes: ByteArray) {
        certBytes = bytes
    }

    /**
     * 获取 KeyStore
     */
    fun getKeyStore(): KeyStore {
        val keyStoreFile = ByteArrayInputStream(certBytes)
//        val keyStoreFile = FileInputStream("keystore.jks")
        val keyStorePassword = "1234".toCharArray()
//        val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        val keyStore: KeyStore = KeyStore.getInstance("PKCS12")
        keyStore.load(keyStoreFile, keyStorePassword)
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