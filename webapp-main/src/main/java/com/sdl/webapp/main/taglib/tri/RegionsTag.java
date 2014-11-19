package com.sdl.webapp.main.taglib.tri;

import com.google.common.base.Strings;
import com.sdl.webapp.common.api.model.Page;
import com.sdl.webapp.common.api.model.Region;
import com.sdl.webapp.main.controller.ControllerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.sdl.webapp.main.RequestAttributeNames.PAGE_MODEL;

public class RegionsTag extends TagSupport {
    private static final Logger LOG = LoggerFactory.getLogger(RegionsTag.class);

    private String exclude;

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    @Override
    public int doStartTag() throws JspException {
        final Page page = (Page) pageContext.getRequest().getAttribute(PAGE_MODEL);
        if (page == null) {
            LOG.debug("Page not found in request attributes");
            return SKIP_BODY;
        }

        Set<String> excludes = new HashSet<>();
        if (!Strings.isNullOrEmpty(exclude)) {
            excludes.addAll(Arrays.asList(exclude.split("\\s*,\\s*")));
        }

        for (Region region : page.getRegions().values()) {
            String name = region.getName();
            if (excludes.contains(name)) {
                LOG.debug("Excluding region: {}", name);
                continue;
            }

            LOG.debug("Including region: {}", name);
            try {
                pageContext.include(ControllerUtils.getRequestPath(region));
            } catch (ServletException | IOException e) {
                throw new JspException("Error while processing regions tag", e);
            }
        }

        return SKIP_BODY;
    }
}