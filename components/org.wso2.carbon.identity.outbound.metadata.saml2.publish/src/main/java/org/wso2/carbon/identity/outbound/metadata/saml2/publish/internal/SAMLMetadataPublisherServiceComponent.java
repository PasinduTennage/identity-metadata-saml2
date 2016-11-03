/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.outbound.metadata.saml2.publish.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.application.authentication.framework.inbound.HttpIdentityResponseFactory;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityProcessor;
import org.wso2.carbon.identity.outbound.metadata.saml2.publish.bean.HttpSAMLMetadataResponseFactory;
import org.wso2.carbon.identity.outbound.metadata.saml2.publish.processor.IDPMetadataPublishProcessor;
import org.wso2.carbon.idp.mgt.util.MetadataConverter;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;
import org.osgi.service.http.HttpService;


/**
 * @scr.component name="identity.provider.saml.metadata.publish.service.component" immediate="true"
 * @scr.reference name="registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic" bind="setRegistryService"
 * unbind="unsetRegistryService"
 * @scr.reference name="user.realmservice.default" interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"
 * unbind="unsetRealmService"
 * @scr.reference name="config.context.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContextService"
 * unbind="unsetConfigurationContextService"
 * @scr.reference name="osgi.httpservice" interface="org.osgi.service.http.HttpService"
 * cardinality="1..1" policy="dynamic" bind="setHttpService"
 * unbind="unsetHttpService"
 * @scr.reference name="identity.provider.saml.service.component"
 * interface="org.wso2.carbon.idp.mgt.util.MetadataConverter"
 * cardinality="0..n" policy="dynamic"
 * bind="setMetadataConverterService"
 * unbind="unsetMetadataConverterService"
 *
 */

public class SAMLMetadataPublisherServiceComponent {

    private static Log log = LogFactory.getLog(SAMLMetadataPublisherServiceComponent.class);


    protected void activate(ComponentContext context) {

        context.getBundleContext().registerService(IdentityProcessor.class.getName(), new IDPMetadataPublishProcessor(), null);
        context.getBundleContext().registerService(HttpIdentityResponseFactory.class.getName(), new
                HttpSAMLMetadataResponseFactory(), null);
        if (log.isDebugEnabled()) {
            log.debug("SAML metadata converter is enabled");
        }
    }

    protected void deactivate(ComponentContext context) {
        if (log.isDebugEnabled()) {
            log.debug("Identity Management bundle is de-activated");
        }
    }
    protected void setRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("RealmService is set in IDP Metadata bundle");
        }
        SAMLMetadataPublisherServiceComponentHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Realm Service is set in the IDP Metadata bundle");
        }
        SAMLMetadataPublisherServiceComponentHolder.getInstance().setRealmService(null);
    }



    public static void setRegistryService(RegistryService registryService) {
        SAMLMetadataPublisherServiceComponentHolder.getInstance().setRegistryService(registryService);
    }



    protected void unsetRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService unset in IDP Metadata bundle");
        }
        SAMLMetadataPublisherServiceComponentHolder.getInstance().setRegistryService(null);
    }

    protected void setConfigurationContextService(ConfigurationContextService configCtxService) {
        if (log.isDebugEnabled()) {
            log.debug("Configuration Context Service is set in the SAML SSO bundle");
        }
        SAMLMetadataPublisherServiceComponentHolder.getInstance().setConfigCtxService(configCtxService);
    }

    protected void unsetConfigurationContextService(ConfigurationContextService configCtxService) {
        if (log.isDebugEnabled()) {
            log.debug("Configuration Context Service is unset in the SAML SSO bundle");
        }
        SAMLMetadataPublisherServiceComponentHolder.getInstance().setConfigCtxService(null);
    }
    protected void setHttpService(HttpService httpService) {
        if (log.isDebugEnabled()) {
            log.debug("HTTP Service is set in the SAML SSO bundle");
        }
        SAMLMetadataPublisherServiceComponentHolder.getInstance().setHttpService(httpService);
    }

    protected void unsetHttpService(HttpService httpService) {
        if (log.isDebugEnabled()) {
            log.debug("HTTP Service is unset in the SAML SSO bundle");
        }
        SAMLMetadataPublisherServiceComponentHolder.getInstance().setHttpService(null);
    }

    protected void setMetadataConverterService(MetadataConverter converter) {
        if (log.isDebugEnabled()) {
            log.debug("Metadata converter set in Identity idp-mgt bundle");
        }
        try {
            SAMLMetadataPublisherServiceComponentHolder.getInstance().addMetadataConverter(converter);
        } catch (Throwable e) {
            log.error("Failed to get a reference to the Metadata Converter in idp-mgt bundle", e);
        }
    }

    protected void unsetMetadataConverterService(MetadataConverter metadataConverter) {
        if (log.isDebugEnabled()) {
            log.debug("org.wso2.carbon.idp.mgt.util.MetadataConverter unset in idp-mgt");
        }
        SAMLMetadataPublisherServiceComponentHolder.getInstance().removeMetadataConverter(metadataConverter);
    }




}
