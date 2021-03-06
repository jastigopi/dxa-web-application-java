package com.sdl.webapp.common.markup.html;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
public final class HtmlStartTag extends HtmlRenderable {

    private final String tagName;

    private final List<HtmlAttribute> attributes;

    /**
     * <p>Constructor for HtmlStartTag.</p>
     *
     * @param tagName    a {@link java.lang.String} object.
     * @param attributes a {@link java.util.List} object.
     */
    public HtmlStartTag(String tagName, List<HtmlAttribute> attributes) {
        this.tagName = tagName;
        this.attributes = ImmutableList.copyOf(attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String renderHtml() {
        if (StringUtils.isEmpty(tagName)) {
            return "";
        }

        final StringBuilder sb = new StringBuilder(16).append('<').append(tagName);
        for (HtmlAttribute attribute : attributes) {
            sb.append(' ').append(attribute.toHtml());
        }
        return sb.append('>').toString();
    }
}
