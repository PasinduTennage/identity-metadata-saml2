package org.wso2.carbon.identity.outbound.metadata.saml2.publish.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.saml2.core.AuthnRequest;
import org.wso2.carbon.identity.application.authentication.framework.exception.FrameworkException;
import org.wso2.carbon.identity.application.authentication.framework.inbound.FrameworkLoginResponse;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityMessageContext;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityProcessor;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityRequest;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import org.opensaml.xml.XMLObject;

import java.util.HashMap;

public class IDPMetadataPublishProcessor extends IdentityProcessor {
    private static Log log = LogFactory.getLog(IDPMetadataPublishProcessor.class);
    private String relyingParty;

    @Override
    public String getName() {
        return "IDPMetadataPublishProcessor";
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public String getCallbackPath(IdentityMessageContext context) {
        return IdentityUtil.getServerURL("identity", false, false);
    }

    @Override
    public String getRelyingPartyId() {
        return this.relyingParty;
    }

    @Override
    public boolean canHandle(IdentityRequest identityRequest) {
//        if (identityRequest instanceof SAMLIdentityRequest && ((SAMLIdentityRequest) identityRequest).getSamlRequest
//                () != null) {
//            return true;
//        }
        return true;
    }

    @Override
    public FrameworkLoginResponse.FrameworkLoginResponseBuilder process(IdentityRequest identityRequest) throws
            FrameworkException {
//        SAMLMessageContext messageContext = new SAMLMessageContext((SAMLIdentityRequest) identityRequest, new
//                HashMap<String, String>());
//        try {
//            validateSPInitSSORequest(messageContext);
//        } catch (IdentityException e) {
//            throw new FrameworkException("Error while building SAML Response.");
//        }
//        FrameworkLoginResponse.FrameworkLoginResponseBuilder builder = buildResponseForFrameworkLogin(messageContext);
        return null;
    }


//    protected boolean validateSPInitSSORequest(SAMLMessageContext messageContext) throws IdentityException {
//        SAMLIdentityRequest identityRequest = messageContext.getRequest();
//        String decodedRequest;
//        if (identityRequest.isRedirect()) {
//            decodedRequest = SAMLSSOUtil.decode(identityRequest.getSamlRequest());
//        } else {
//            decodedRequest = SAMLSSOUtil.decodeForPost(identityRequest.getSamlRequest());
//        }
//        XMLObject request = SAMLSSOUtil.unmarshall(decodedRequest);
//        if (request instanceof AuthnRequest) {
//            messageContext.setIdpInitSSO(false);
//            messageContext.setAuthnRequest((AuthnRequest) request);
//            messageContext.setTenantDomain(SAMLSSOUtil.getTenantDomainFromThreadLocal());
//            this.relyingParty = ((AuthnRequest) request).getIssuer().getValue();
//            //messageContext.setRpSessionId(identityRequest.getParameter(MultitenantConstants.SSO_AUTH_SESSION_ID));
//            SSOAuthnRequestValidator reqValidator = SAMLSSOUtil.getSPInitSSOAuthnRequestValidator(messageContext);
//            return reqValidator.validate();
//        }
//        return false;
//    }
}