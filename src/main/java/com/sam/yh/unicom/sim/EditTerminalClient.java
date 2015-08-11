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

public class EditTerminalClient implements ApiClientConstant {

    private static final Logger logger = LoggerFactory.getLogger(EditTerminalClient.class);

    private SOAPConnectionFactory connectionFactory;
    private MessageFactory messageFactory;
    private URL url;

    private XWSSProcessorFactory processorFactory;

    /**
     * Constructor which initializes Soap Connection, messagefactory and
     * ProcessorFactory
     * 
     * @throws SOAPException
     * @throws MalformedURLException
     * @throws XWSSecurityException
     */
    public EditTerminalClient() throws SOAPException, MalformedURLException, XWSSecurityException {
        connectionFactory = SOAPConnectionFactory.newInstance();
        messageFactory = MessageFactory.newInstance();
        processorFactory = XWSSProcessorFactory.newInstance();
        this.url = new URL(SERVICE_URL);
        // System.setProperty("javax.net.ssl.trustStore",
        // "F:/github/asm/src/main/resources/jssecacerts");
    }

    public void callWebService(String iccid, String businessValue) throws SOAPException, IOException, XWSSecurityException, Exception {
        SOAPMessage request = createTerminalRequest(iccid, businessValue);
        request = secureMessage(request);

        // TODO
        logger.info("Edit Terminal Request: ");
        request.writeTo(System.out);
        System.out.println("");

        SOAPConnection connection = connectionFactory.createConnection();
        SOAPMessage response = connection.call(request, url);
        // TODO
        logger.info("Edit Terminal Response: ");
        response.writeTo(System.out);
        System.out.println("");

        if (!response.getSOAPBody().hasFault()) {
            writeTerminalResponse(response);
        } else {
            SOAPFault fault = response.getSOAPBody().getFault();
            logger.error("Received SOAP Fault");
            logger.error("SOAP Fault Code :" + fault.getFaultCode());
            logger.error("SOAP Fault String :" + fault.getFaultString());
        }
    }

    /**
     * This method creates a Terminal Request and sends back the SOAPMessage.
     * ICCID value is passed into this method
     * 
     * @return SOAPMessage
     * @throws SOAPException
     */
    private SOAPMessage createTerminalRequest(String iccid, String businessValue) throws SOAPException {
        SOAPMessage message = messageFactory.createMessage();
        message.getMimeHeaders().addHeader("SOAPAction", "http://api.jasperwireless.com/ws/service/terminal/EditTerminal");
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name terminalRequestName = envelope.createName("EditTerminalRequest", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalRequestElement = message.getSOAPBody().addBodyElement(terminalRequestName);
        Name msgId = envelope.createName("messageId", PREFIX, NAMESPACE_URI);
        SOAPElement msgElement = terminalRequestElement.addChildElement(msgId);
        msgElement.setValue("TCE-100-ABC-34084");
        Name version = envelope.createName("version", PREFIX, NAMESPACE_URI);
        SOAPElement versionElement = terminalRequestElement.addChildElement(version);
        versionElement.setValue("1.0");
        Name license = envelope.createName("licenseKey", PREFIX, NAMESPACE_URI);
        SOAPElement licenseElement = terminalRequestElement.addChildElement(license);
        licenseElement.setValue(APIKEY);
        Name iccidName = envelope.createName("iccid", PREFIX, NAMESPACE_URI);
        SOAPElement iccidElement = terminalRequestElement.addChildElement(iccidName);
        iccidElement.setValue(iccid);
        Name targetValue = envelope.createName("targetValue", PREFIX, NAMESPACE_URI);
        SOAPElement targetElement = terminalRequestElement.addChildElement(targetValue);
        targetElement.setValue(businessValue);
        Name changeType = envelope.createName("changeType", PREFIX, NAMESPACE_URI);
        SOAPElement changeTypeElement = terminalRequestElement.addChildElement(changeType);
        changeTypeElement.setValue("3");
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
            public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
                for (int i = 0; i < callbacks.length; i++) {
                    if (callbacks[i] instanceof UsernameCallback) {
                        UsernameCallback callback = (UsernameCallback) callbacks[i];
                        callback.setUsername(USERNAME);
                    } else if (callbacks[i] instanceof PasswordCallback) {
                        PasswordCallback callback = (PasswordCallback) callbacks[i];
                        callback.setPassword(PASSWORD);
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
    private void writeTerminalResponse(SOAPMessage message) throws SOAPException {
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name terminalResponseName = envelope.createName("EditTerminalResponse", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalResponseElement = (SOAPBodyElement) message.getSOAPBody().getChildElements(terminalResponseName).next();

        NodeList list = terminalResponseElement.getChildNodes();
        Node n = null;
        for (int i = 0; i < list.getLength(); i++) {
            n = list.item(i);
            if ("status".equalsIgnoreCase(n.getLocalName()))
                break;
        }

        logger.info("status of device = " + n.getNodeValue());

    }

}