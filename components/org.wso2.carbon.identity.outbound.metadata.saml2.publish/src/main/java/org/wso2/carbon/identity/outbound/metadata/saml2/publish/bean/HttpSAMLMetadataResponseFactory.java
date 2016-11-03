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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.inbound.HttpIdentityResponse;
import org.wso2.carbon.identity.application.authentication.framework.inbound.HttpIdentityResponseFactory;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * This class implements functionality to build a HTTPIdentityResponse
 * */

public class HttpSAMLMetadataResponseFactory extends HttpIdentityResponseFactory {

    private static Log log = LogFactory.getLog(HttpSAMLMetadataResponseFactory.class);

    @Override
    public String getName() {
        return "HttpSAMLMetadataResponseFactory";
    }

    @Override
    public boolean canHandle(IdentityResponse identityResponse) {
        if (identityResponse instanceof SAMLMetadataResponse) {
            return true;
        }
        return false;
    }

    @Override
    public HttpIdentityResponse.HttpIdentityResponseBuilder create(IdentityResponse identityResponse) {

        if (identityResponse instanceof SAMLMetadataResponse) {
            return sendResponse(identityResponse);
        }
        return null;
    }

    private HttpIdentityResponse.HttpIdentityResponseBuilder sendResponse(IdentityResponse identityResponse) {
        SAMLMetadataResponse metadataResponse = ((SAMLMetadataResponse) identityResponse);
        HttpIdentityResponse.HttpIdentityResponseBuilder builder = new HttpIdentityResponse
                .HttpIdentityResponseBuilder();

        String metadata = metadataResponse.getMetadata();
        StringBuilder out = new StringBuilder();
        out.append(metadata);
        builder.setBody(out.toString());
        builder.setContentType("application/xml");
        builder.setStatusCode(HttpServletResponse.SC_OK);
        return builder;
    }

    @Override
    public void create(HttpIdentityResponse.HttpIdentityResponseBuilder httpIdentityResponseBuilder, IdentityResponse
            identityResponse) {
    }
}