package com.sdl.dxa.tridion.modelservice;

import com.sdl.dxa.api.datamodel.model.EntityModelData;
import com.sdl.dxa.api.datamodel.model.PageModelData;
import com.sdl.dxa.common.dto.EntityRequestDto;
import com.sdl.dxa.common.dto.PageRequestDto;
import com.sdl.webapp.common.api.WebRequestContext;
import com.sdl.webapp.common.api.content.ContentProviderException;
import com.sdl.webapp.common.api.content.PageNotFoundException;
import com.sdl.webapp.common.api.localization.Localization;
import com.sdl.webapp.common.controller.exception.InternalServerErrorException;
import com.sdl.webapp.common.exceptions.DxaItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class DefaultModelService implements ModelService {

    private final ModelServiceConfiguration configuration;

    private final ModelServiceClient modelServiceClient;

    private final WebRequestContext webRequestContext;

    @Autowired
    public DefaultModelService(ModelServiceConfiguration configuration, ModelServiceClient modelServiceClient, WebRequestContext webRequestContext) {
        this.configuration = configuration;
        this.modelServiceClient = modelServiceClient;
        this.webRequestContext = webRequestContext;
    }

    @NotNull
    @Override
    public PageModelData loadPageModel(PageRequestDto pageRequest) throws ContentProviderException {
        return _loadPage(configuration.getPageModelUrl(), PageModelData.class, pageRequest);
    }

    @NotNull
    @Override
    public String loadPageContent(PageRequestDto pageRequest) throws ContentProviderException {
        String serviceUrl = UriComponentsBuilder.fromUriString(configuration.getPageModelUrl()).queryParam("raw").build().toUriString();
        return _loadPage(serviceUrl, String.class, pageRequest);
    }

    private <T> T _loadPage(String serviceUrl, Class<T> type, PageRequestDto pageRequest) throws ContentProviderException {
        Localization localization = webRequestContext.getLocalization();
        try {
            T page = modelServiceClient.getForType(serviceUrl, type,
                    pageRequest.getUriType(),
                    pageRequest.getPublicationId() != 0 ? pageRequest.getPublicationId() : localization.getId(),
                    pageRequest.getPath(),
                    pageRequest.getIncludePages());
            log.trace("Loaded '{}' for localization '{}' and pageRequest '{}'", page, localization, pageRequest);
            return page;
        } catch (DxaItemNotFoundException e) {
            throw new PageNotFoundException("Cannot load page '" + pageRequest + "'", e);
        } catch (InternalServerErrorException e) {
            throw new ContentProviderException("Cannot load page from model service", e);
        }
    }

    @NotNull
    @Override
    public EntityModelData loadEntity(@NotNull String entityId) throws ContentProviderException {
        return loadEntity(EntityRequestDto.builder().entityId(entityId).build());
    }

    @NotNull
    @Override
    public EntityModelData loadEntity(EntityRequestDto entityRequest) throws ContentProviderException {
        Localization localization = webRequestContext.getLocalization();

        EntityModelData modelData = modelServiceClient.getForType(configuration.getEntityModelUrl(), EntityModelData.class,
                entityRequest.getUriType(),
                entityRequest.getPublicationId() != 0 ? entityRequest.getPublicationId() : localization.getId(),
                entityRequest.getComponentId(),
                entityRequest.getTemplateId());
        log.trace("Loaded '{}' for entityId '{}'", modelData, entityRequest.getComponentId());
        return modelData;
    }
}
