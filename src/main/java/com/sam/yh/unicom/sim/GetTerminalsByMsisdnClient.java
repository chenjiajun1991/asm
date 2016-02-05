package com.sam.yh.unicom.sim;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.wss.ProcessingContext;
import com.sun.xml.wss.XWSSProcessor;
import com.sun.xml.wss.XWSSProcessorFactory;
import com.sun.xml.wss.XWSSecurityException;
import com.sun.xml.wss.impl.callback.PasswordCallback;
import com.sun.xml.wss.impl.callback.UsernameCallback;

public class GetTerminalsByMsisdnClient {

    private static final Logger logger = LoggerFactory.getLogger(GetTerminalsByMsisdnClient.class);

    private static final String NAMESPACE_URI = "http://api.jasperwireless.com/ws/schema";
    private static final String PREFIX = "jws";

    private SOAPConnectionFactory connectionFactory;
    private MessageFactory messageFactory;
    private URL url;
    private String userName;
    private String password;
    private String apiKey;

    private XWSSProcessorFactory processorFactory;

    /**
     * Constructor which initializes Soap Connection, messagefactory and
     * ProcessorFactory
     * 
     * @param url
     * @throws SOAPException
     * @throws MalformedURLException
     * @throws XWSSecurityException
     */
    public GetTerminalsByMsisdnClient(String serviceUrl, String username, String password, String apikey) throws SOAPException, MalformedURLException,
            XWSSecurityException {
        connectionFactory = SOAPConnectionFactory.newInstance();
        messageFactory = MessageFactory.newInstance();
        processorFactory = XWSSProcessorFactory.newInstance();
        this.url = new URL(serviceUrl);
        this.userName = username;
        this.password = password;
        this.apiKey = apikey;
        // System.setProperty("javax.net.ssl.trustStore",
        // "F:/github/asm/src/main/resources/jssecacerts");
    }

    public String callWebService(String msisdn) throws SOAPException, IOException, XWSSecurityException, GetTermalIccidException {
        SOAPMessage request = createTerminalRequest(msisdn);
        request = secureMessage(request);

        // TODO
        logger.info("Get Terminals by msisdn Request: ");
        request.writeTo(System.out);
        System.out.println("");

        SOAPConnection connection = connectionFactory.createConnection();
        SOAPMessage response = connection.call(request, url);
        // TODO
        logger.info("Get Terminals by msisdn Response: ");
        response.writeTo(System.out);
        System.out.println("");

        if (!response.getSOAPBody().hasFault()) {
            return writeTerminalResponse(response);
        } else {
            SOAPFault fault = response.getSOAPBody().getFault();
            logger.error("Received SOAP Fault");
            logger.error("SOAP Fault Code :" + fault.getFaultCode());
            logger.error("SOAP Fault String :" + fault.getFaultString());
            throw new GetTermalIccidException(fault.getFaultCode() + "," + fault.getFaultString());
        }
    }

    /**
     * This method creates a Terminal Request and sends back the SOAPMessage.
     * MSISDN value is passed into this method
     * 
     * @param msisdn
     * @return
     * @throws SOAPException
     */
    private SOAPMessage createTerminalRequest(String msisdn) throws SOAPException {
        SOAPMessage message = messageFactory.createMessage();
        message.getMimeHeaders().addHeader("SOAPAction", "http://api.jasperwireless.com/ws/service/terminal/GetTerminalsByMsisdn");
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name terminalRequestName = envelope.createName("GetTerminalsByMsisdnRequest", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalRequestElement = message.getSOAPBody().addBodyElement(terminalRequestName);
        Name msgId = envelope.createName("messageId", PREFIX, NAMESPACE_URI);
        SOAPElement msgElement = terminalRequestElement.addChildElement(msgId);
        msgElement.setValue("TCE-100-ABC-34084");
        Name version = envelope.createName("version", PREFIX, NAMESPACE_URI);
        SOAPElement versionElement = terminalRequestElement.addChildElement(version);
        versionElement.setValue("1.0");
        Name license = envelope.createName("licenseKey", PREFIX, NAMESPACE_URI);
        SOAPElement licenseElement = terminalRequestElement.addChildElement(license);
        licenseElement.setValue(apiKey);
        Name msisdns = envelope.createName("msisdns", PREFIX, NAMESPACE_URI);
        SOAPElement msisdnsElement = terminalRequestElement.addChildElement(msisdns);
        Name msisdnName = envelope.createName("msisdn", PREFIX, NAMESPACE_URI);
        SOAPElement msisdnElement = msisdnsElement.addChildElement(msisdnName);
        msisdnElement.setValue(msisdn);
        return message;
    }

    /**
     * This method is used to add the security. SecurityPolicy.xml file should
     * be in classpath.
     * 
     * @param message
     * @return
     * @throws IOException
     * @throws XWSSecurityException
     */
    private SOAPMessage secureMessage(SOAPMessage message) throws IOException, XWSSecurityException {
        CallbackHandler callbackHandler = new CallbackHandler() {
            @Override
            public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
                for (int i = 0; i < callbacks.length; i++) {
                    if (callbacks[i] instanceof UsernameCallback) {
                        UsernameCallback callback = (UsernameCallback) callbacks[i];
                        callback.setUsername(userName);
                    } else if (callbacks[i] instanceof PasswordCallback) {
                        PasswordCallback callback = (PasswordCallback) callbacks[i];
                        callback.setPassword(password);
                    } else {
                        throw new UnsupportedCallbackException(callbacks[i]);
                    }
                }
            }
        };
        InputStream policyStream = null;
        XWSSProcessor processor = null;
        try {
            policyStream = getClass().getClassLoader().getResourceAsStream("securityPolicy.xml");
            processor = processorFactory.createProcessorForSecurityConfiguration(policyStream, callbackHandler);
        } finally {
            if (policyStream != null) {
                policyStream.close();
            }
        }
        ProcessingContext context = processor.createProcessingContext(message);
        return processor.secureOutboundMessage(context);
    }

    /**
     * Gets the terminal response.
     * 
     * @param message
     * @throws SOAPException
     */
    private String writeTerminalResponse(SOAPMessage message) throws SOAPException {
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name terminalResponseName = envelope.createName("GetTerminalsByMsisdnResponse", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalResponseElement = (SOAPBodyElement) message.getSOAPBody().getChildElements(terminalResponseName).next();
        Name terminals = envelope.createName("terminals", PREFIX, NAMESPACE_URI);
        Name terminal = envelope.createName("terminal", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalsElement = (SOAPBodyElement) terminalResponseElement.getChildElements(terminals).next();
        SOAPBodyElement terminalElement = (SOAPBodyElement) terminalsElement.getChildElements(terminal).next();
        NodeList list = terminalElement.getChildNodes();
        Node n = null, node = null;
        for (int i = 0; i < list.getLength(); i++) {
            node = list.item(i);
            logger.info(node.getLocalName() + ":" + node.getFirstChild().getNodeValue());
            if ("iccid".equalsIgnoreCase(node.getLocalName())) {
                n = node;
            }
        }

        return n.getFirstChild().getNodeValue();

    }

}