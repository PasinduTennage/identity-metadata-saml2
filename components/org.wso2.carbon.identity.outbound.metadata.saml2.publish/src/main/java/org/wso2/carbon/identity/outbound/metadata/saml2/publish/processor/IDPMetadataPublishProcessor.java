package org.wso2.carbon.identity.outbound.metadata.saml2.publish.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.ssl.rmi.IntegerRMI;
import org.opensaml.saml2.core.AuthnRequest;
import org.wso2.carbon.identity.application.authentication.framework.exception.FrameworkException;
import org.wso2.carbon.identity.application.authentication.framework.inbound.*;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import org.opensaml.xml.XMLObject;
import org.wso2.carbon.identity.outbound.metadata.saml2.publish.bean.SAMLMetadataResponse;
import org.wso2.carbon.identity.outbound.metadata.saml2.publish.internal.SAMLMetadataPublisherServiceComponentHolder;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;
import org.wso2.carbon.idp.mgt.util.MetadataConverter;
import org.wso2.carbon.context.CarbonContext;

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
        if(identityRequest.getRequestURI().toString().contains("/metadata/saml2")){
            return true ;
        }else{
            return false;
        }
    }

    public SAMLMetadataResponse.SAMLMetadataResponseBuilder process(IdentityRequest identityRequest) throws
            FrameworkException {

        SAMLMetadataResponse.SAMLMetadataResponseBuilder builder;
        String tennantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        IdentityProviderManager identityProviderManager = IdentityProviderManager.getInstance();
        String  metadata = null;
        try {
            metadata = identityProviderManager.getResidentIDPMetadata(tennantDomain);
        }catch(IdentityProviderManagementException ex){

        }
        IdentityMessageContext context = new IdentityMessageContext(identityRequest);
        SAMLMetadataResponse.SAMLMetadataResponseBuilder responseBuilder = new SAMLMetadataResponse.SAMLMetadataResponseBuilder(context);
        responseBuilder.setMetadata(metadata);
        return responseBuilder;

    }



}