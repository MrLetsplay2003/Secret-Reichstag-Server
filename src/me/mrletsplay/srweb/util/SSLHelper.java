package me.mrletsplay.srweb.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import jakarta.xml.bind.DatatypeConverter;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class SSLHelper {

	public static SSLContext getContext() {
		SSLContext context;
		String password = SRWebConfig.getCertificatePassword();
		
		try {
			context = SSLContext.getInstance("TLS");
			
			File certFile = new File(SRWebConfig.getCertificatePath());
			
			if(!certFile.exists()) {
				throw new FriendlyException("SSL certificate file doesn't exist (" + certFile.getAbsolutePath() + ")");
			}
			
			File privKeyFile = new File(SRWebConfig.getPrivateKeyPath());
			
			if(!privKeyFile.exists()) {
				throw new FriendlyException("Private Key file doesn't exist (" + certFile.getAbsolutePath() + ")");
			}

			byte[] certBytes = parseDERFromPEM(getBytes(certFile),
					"-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
			byte[] keyBytes = parseDERFromPEM(getBytes(privKeyFile),
					"-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");

			X509Certificate cert = generateCertificateFromDER(certBytes);
			RSAPrivateKey key = generatePrivateKeyFromDER(keyBytes);

			KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(null);
			keystore.setCertificateEntry("cert-alias", cert);
			keystore.setKeyEntry("key-alias", key, password.toCharArray(), new Certificate[] { cert });

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(keystore, password.toCharArray());

			KeyManager[] km = kmf.getKeyManagers();

			context.init(km, null, null);
		} catch (Exception e) {
			context = null;
		}
		return context;
	}

	private static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter) {
		String data = new String(pem);
		String[] tokens = data.split(beginDelimiter);
		tokens = tokens[1].split(endDelimiter);
		return DatatypeConverter.parseBase64Binary(tokens[0]);
	}

	private static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory factory = KeyFactory.getInstance("RSA");

		return (RSAPrivateKey) factory.generatePrivate(spec);
	}

	private static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
		CertificateFactory factory = CertificateFactory.getInstance("X.509");

		return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
	}

	private static byte[] getBytes(File file) {
		byte[] bytesArray = new byte[(int) file.length()];

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fis.read(bytesArray); // read file into bytes[]
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytesArray;
	}

}
