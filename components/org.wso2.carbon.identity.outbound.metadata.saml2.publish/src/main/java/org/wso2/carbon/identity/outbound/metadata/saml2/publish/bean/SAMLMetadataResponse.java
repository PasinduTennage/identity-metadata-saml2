/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.identity.outbound.metadata.saml2.publish.bean;

import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityMessageContext;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityResponse;
import org.wso2.carbon.identity.outbound.metadata.saml2.publish.util.SAMLMetadataUtil;

/**
 * This class implements SAMLMetadataResponse and the corresponding builder
 * */

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

        @Override
        public IdentityResponse build() {
            return new SAMLMetadataResponse(this);
        }
    }



}