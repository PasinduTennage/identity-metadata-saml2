package org.wso2.carbon.identity.outbound.metadata.saml2.publish.internal;

/**
 * Created by pasindutennage on 11/2/16.
 */
import org.osgi.service.http.HttpService;
import org.wso2.carbon.idp.mgt.util.MetadataConverter;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.util.ArrayList;
import java.util.List;

public class SAMLMetadataPublisherServiceComponentHolder {
    private RegistryService registryService ;
    private RealmService realmService;
    private ConfigurationContextService configCtxService;
    private HttpService httpService;
    private List<MetadataConverter> metadataConverters = new ArrayList<>();

    private static SAMLMetadataPublisherServiceComponentHolder samlMetadataPublisherServiceComponentHolder = new
            SAMLMetadataPublisherServiceComponentHolder();
    public static SAMLMetadataPublisherServiceComponentHolder getInstance(){
        return samlMetadataPublisherServiceComponentHolder;
    }


    public RegistryService getRegistryService() {
        return registryService;
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    public RealmService getRealmService() {
        return realmService;
    }

    public void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }

    public ConfigurationContextService getConfigCtxService() {
        return configCtxService;
    }

    public void setConfigCtxService(ConfigurationContextService configCtxService) {
        this.configCtxService = configCtxService;
    }

    public HttpService getHttpService() {
        return httpService;
    }

    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    public void addMetadataConverter(MetadataConverter converter){
        this.getMetadataConverters().add(converter);
    }
    public void removeMetadataConverter(MetadataConverter converter){
        this.getMetadataConverters().remove(converter);
    }

    public List<MetadataConverter> getMetadataConverters() {
        return metadataConverters;
    }

    public void setMetadataConverters(List<MetadataConverter> metadataConverters) {
        this.metadataConverters = metadataConverters;
    }
}
