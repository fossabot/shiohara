package com.viglet.shiohara.utils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.url.ShURLScheme;

@Component
public class ShSiteUtils {
	@Autowired
	ShURLScheme shURLScheme;

	public JSONObject toJSON(ShSite shSite) {
		JSONObject shSiteItemSystemAttrs = new JSONObject();
		shSiteItemSystemAttrs.put("id", shSite.getId());
		shSiteItemSystemAttrs.put("title", shSite.getName());
		shSiteItemSystemAttrs.put("summary", shSite.getDescription());
		shSiteItemSystemAttrs.put("link", shURLScheme.get(shSite).toString());

		JSONObject shSiteItemAttrs = new JSONObject();

		shSiteItemAttrs.put("system", shSiteItemSystemAttrs);
		return shSiteItemAttrs;
	}

	public String generatePostLink(ShSite shSite) {
		String link = shURLScheme.get(shSite).toString();
		return link;
	}

}
