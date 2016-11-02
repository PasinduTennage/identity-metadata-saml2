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
        //check whether the url is correct
//        if (identityRequest instanceof SAMLIdentityRequest && ((SAMLIdentityRequest) identityRequest).getSamlRequest
//                () != null) {
//            return true;
//        }
        return true;
    }

    public SAMLMetadataResponse.SAMLMetadataResponseBuilder process(IdentityRequest identityRequest) throws
            FrameworkException {

        SAMLMetadataResponse.SAMLMetadataResponseBuilder builder;
        String tennantDomain = identityRequest.getParameter("tennantDomain");
        IdentityProviderManager identityProviderManager = IdentityProviderManager.getInstance();
        try {
            String  metadata = identityProviderManager.getResidentIDPMetadata(tennantDomain);
        }catch(IdentityProviderManagementException ex){

        }

        if (authnResult == null || !authnResult.isAuthenticated()) {

            if (log.isDebugEnabled() && authnResult != null) {
                log.debug("Unauthenticated User.");
            }

            if (authnReq.isPassive()) { //if passive

                String destination = authnReq.getAssertionConsumerServiceURL();
                try {
                    List<String> statusCodes = new ArrayList<String>();
                    statusCodes.add(SAMLSSOConstants.StatusCodes.NO_PASSIVE);
                    statusCodes.add(SAMLSSOConstants.StatusCodes.IDENTITY_PROVIDER_ERROR);
                    String errorResponse = SAMLSSOUtil.buildErrorResponse(messageContext.getId(), statusCodes,
                            "Cannot authenticate Subject in Passive Mode", destination);
                    builder = new SAMLLoginResponse.SAMLLoginResponseBuilder(messageContext);
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setRelayState(messageContext.getRelayState
                            ());
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setRespString(errorResponse);
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setAcsUrl(messageContext
                            .getAssertionConsumerURL());
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setSubject(messageContext.getSubject());
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setAuthenticatedIdPs(null);
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setTenantDomain(messageContext
                            .getTenantDomain());
                    return builder;
                } catch (IdentityException e) {
                    //TODO
                    //Handle this exception
                }
            } else { // if forceAuthn or normal flow
                //TODO send a saml response with a status message.
                try {
                    if (!authnResult.isAuthenticated()) {
                        String destination = messageContext.getDestination();
                        String errorResp = SAMLSSOUtil.buildErrorResponse(SAMLSSOConstants.StatusCodes.AUTHN_FAILURE,
                                "User authentication failed", destination);
                        builder = new SAMLErrorResponse.SAMLErrorResponseBuilder(messageContext);
                        ((SAMLErrorResponse.SAMLErrorResponseBuilder) builder).setErrorResponse(errorResp);
                        ((SAMLErrorResponse.SAMLErrorResponseBuilder) builder).setStatus(SAMLSSOConstants
                                .Notification.EXCEPTION_STATUS);
                        ((SAMLErrorResponse.SAMLErrorResponseBuilder) builder).setMessageLog(SAMLSSOConstants
                                .Notification.EXCEPTION_MESSAGE);
                        ((SAMLErrorResponse.SAMLErrorResponseBuilder) builder).setAcsUrl(authnReq
                                .getAssertionConsumerServiceURL());
                        return builder;
                    } else {
                        throw IdentityException.error("Session data is not found for authenticated user");
                    }
                }catch(IdentityException | IOException e){
                    //TODO
                    //Handle This exception
                }
            }
        } else {
            SAMLSSOUtil.setIsSaaSApplication(authnResult.isSaaSApp());
            try {
                SAMLSSOUtil.setUserTenantDomain(authnResult.getSubject().getTenantDomain());
            } catch (UserStoreException e) {
                builder = new SAMLErrorResponse.SAMLErrorResponseBuilder(messageContext);
                return builder;
            } catch (IdentityException e) {
                builder = new SAMLErrorResponse.SAMLErrorResponseBuilder(messageContext);
                return builder;
            }
            String relayState;
//TODO : Fix Identity Request in framework
            try {
                relayState = identityRequest.getParameter(SAMLSSOConstants.RELAY_STATE);
                if(StringUtils.isBlank(relayState)){
                    relayState = messageContext.getRelayState();
                }
            } catch (NullPointerException e) {
                relayState = messageContext.getRelayState();
            }

//            if (identityRequest.getParameter(SAMLSSOConstants.RELAY_STATE) != null) {
//                relayState = identityRequest.getParameter(SAMLSSOConstants.RELAY_STATE);
//            } else {
//                relayState = messageContext.getRelayState();
//            }

//            startTenantFlow(authnReqDTO.getTenantDomain());

//            if (sessionId == null) {
//                sessionId = UUIDGenerator.generateUUID();
//            }
            try {
                builder = authenticate(messageContext, authnResult.isAuthenticated(), authnResult
                        .getAuthenticatedAuthenticators(), SAMLSSOConstants.AuthnModes.USERNAME_PASSWORD);
                if (builder instanceof SAMLLoginResponse.SAMLLoginResponseBuilder) { // authenticated
//
//                storeTokenIdCookie(sessionId, req, resp, authnReqDTO.getTenantDomain());
//                removeSessionDataFromCache(req.getParameter(SAMLSSOConstants.SESSION_DATA_KEY));
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setRelayState(relayState);
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setAcsUrl(messageContext
                            .getAssertionConsumerURL());
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setSubject(messageContext.getUser()
                            .getAuthenticatedSubjectIdentifier());
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setAuthenticatedIdPs(messageContext
                            .getAuthenticationResult().getAuthenticatedIdPs());
                    ((SAMLLoginResponse.SAMLLoginResponseBuilder) builder).setTenantDomain(messageContext
                            .getTenantDomain
                                    ());
                    return builder;
                } else { // authentication FAILURE
                    ((SAMLErrorResponse.SAMLErrorResponseBuilder) builder).setStatus(SAMLSSOConstants
                            .Notification.EXCEPTION_STATUS);
                    ((SAMLErrorResponse.SAMLErrorResponseBuilder) builder).setMessageLog(SAMLSSOConstants
                            .Notification.EXCEPTION_MESSAGE);
                    ((SAMLErrorResponse.SAMLErrorResponseBuilder) builder).setAcsUrl(messageContext
                            .getSamlssoServiceProviderDO().getDefaultAssertionConsumerUrl());
                    return builder;
                }
            } catch (IdentityException e) {

            }
        }
        return null;
    }



}