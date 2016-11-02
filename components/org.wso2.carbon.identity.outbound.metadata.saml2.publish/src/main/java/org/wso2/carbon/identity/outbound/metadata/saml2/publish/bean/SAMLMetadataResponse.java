package org.wso2.carbon.identity.outbound.metadata.saml2.publish.bean;

/**
 * Created by pasindutennage on 11/2/16.
 */

import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityMessageContext;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityResponse;
import org.wso2.carbon.identity.outbound.metadata.saml2.publish.util.SAMLMetadataUtil;

public class SAMLMetadataResponse extends IdentityResponse {

    private Response response;
    private String metadata ;

    protected SAMLMetadataResponse(IdentityResponseBuilder builder) {
        super(builder);
        this.response = ((SAMLMetadataResponseBuilder) builder).response;
        metadata = ((SAMLMetadataResponseBuilder) builder).metadata;
    }

    public Response getResponse() {
        return this.response;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public static class SAMLMetadataResponseBuilder extends IdentityResponseBuilder {

        private Response response;
        private String metadata ;

        //Do the bootstrap first
        static {
            SAMLMetadataUtil.doBootstrap();
        }

        public SAMLMetadataResponseBuilder(IdentityMessageContext context) {
            super(context);
            ResponseBuilder responseBuilder = new ResponseBuilder();
            this.response = responseBuilder.buildObject();
        }

        public SAMLMetadataResponseBuilder setResponse(Response response) {
            this.response = response;
            return this;
        }

        public String getMetadata() {
            return metadata;
        }

        public void setMetadata(String metadata) {
            this.metadata = metadata;
        }
    }

}