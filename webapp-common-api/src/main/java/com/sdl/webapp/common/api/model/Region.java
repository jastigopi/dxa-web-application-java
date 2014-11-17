package com.sdl.webapp.common.api.model;

import java.util.List;
import java.util.Map;

public interface Region extends ViewModel {

    String getName();

    List<Entity> getEntities();

    Map<String, String> getRegionData();
}
