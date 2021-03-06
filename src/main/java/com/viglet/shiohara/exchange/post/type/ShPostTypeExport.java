package com.viglet.shiohara.exchange.post.type;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShPostTypeExchange;
import com.viglet.shiohara.exchange.ShPostTypeFieldExchange;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.utils.ShUtils;

@Component
public class ShPostTypeExport {
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShUtils shUtils;

	public StreamingResponseBody exportObject(HttpServletResponse response) throws Exception {
		String folderName = UUID.randomUUID().toString();
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			List<ShPostType> shPostTypes = shPostTypeRepository.findAll();

			List<ShPostTypeExchange> postTypeExchanges = new ArrayList<ShPostTypeExchange>();

			for (ShPostType shPostType : shPostTypes) {
				postTypeExchanges.add(this.exportPostType(shPostType));
			}

			File exportDir = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName));
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}

			ShExchange shExchange = new ShExchange();
			if (postTypeExchanges.size() > 0) {
				shExchange.setPostTypes(postTypeExchanges);
			}
			// Object to JSON in file
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(
					new File(exportDir.getAbsolutePath().concat(File.separator + "export.json")), shExchange);

			File zipFile = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName + ".zip"));

			shUtils.addFilesToZip(exportDir, zipFile);

			String strDate = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
			String zipFileName = "PostType_" + strDate + ".zip";

			response.addHeader("Content-disposition", "attachment;filename=" + zipFileName);
			response.setContentType("application/octet-stream");
			response.setStatus(HttpServletResponse.SC_OK);

			return new StreamingResponseBody() {
				@Override
				public void writeTo(java.io.OutputStream output) throws IOException {

					try {
						java.nio.file.Path path = Paths.get(zipFile.getAbsolutePath());
						byte[] data = Files.readAllBytes(path);
						output.write(data);
						output.flush();

						FileUtils.deleteDirectory(exportDir);
						FileUtils.deleteQuietly(zipFile);

					} catch (IOException ex) {
						ex.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		} else {
			return null;
		}
	}

	public ShPostTypeExchange exportPostType(ShPostType shPostType) {
		ShPostTypeExchange shPostTypeExchange = new ShPostTypeExchange();
		shPostTypeExchange.setId(shPostType.getId());
		shPostTypeExchange.setName(shPostType.getName());
		shPostTypeExchange.setLabel(shPostType.getTitle());
		shPostTypeExchange.setDate(shPostType.getDate());
		shPostTypeExchange.setDescription(shPostType.getDescription());

		shPostTypeExchange.setOwner(shPostType.getOwner());
		shPostTypeExchange.setSystem(shPostType.getSystem() == (byte) 1 ? true : false);

		if (shPostType.getShPostTypeAttrs().size() > 0) {
			Map<String, ShPostTypeFieldExchange> shPostTypeFieldExchanges = new HashMap<String, ShPostTypeFieldExchange>();

			for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
				ShPostTypeFieldExchange shPostTypeFieldExchange = this.exportPostTypeField(shPostTypeAttr);
				shPostTypeFieldExchanges.put(shPostTypeAttr.getName(), shPostTypeFieldExchange);
			}

			shPostTypeExchange.setFields(shPostTypeFieldExchanges);
		}

		return shPostTypeExchange;
	}
	public ShPostTypeFieldExchange exportPostTypeField(ShPostTypeAttr shPostTypeAttr) {
		ShPostTypeFieldExchange shPostTypeFieldExchange = new ShPostTypeFieldExchange();
		shPostTypeFieldExchange.setId(shPostTypeAttr.getId());
		shPostTypeFieldExchange.setLabel(shPostTypeAttr.getLabel());
		shPostTypeFieldExchange.setDescription(shPostTypeAttr.getDescription());

		shPostTypeFieldExchange.setOrdinal(shPostTypeAttr.getOrdinal());
		shPostTypeFieldExchange.setRequired(shPostTypeAttr.getRequired() == (byte) 1 ? true : false);
		shPostTypeFieldExchange.setSummary(shPostTypeAttr.getIsSummary() == (byte) 1 ? true : false);
		shPostTypeFieldExchange.setTitle(shPostTypeAttr.getIsTitle() == (byte) 1 ? true : false);
		shPostTypeFieldExchange.setWidget(shPostTypeAttr.getShWidget().getName());

		if (shPostTypeAttr.getShPostTypeAttrs().size() > 0) {
			Map<String, ShPostTypeFieldExchange> shPostTypeFieldExchanges = new HashMap<String, ShPostTypeFieldExchange>();

			for (ShPostTypeAttr shPostTypeAttrChild : shPostTypeAttr.getShPostTypeAttrs()) {
				ShPostTypeFieldExchange shPostTypeFieldExchangeChild = this.exportPostTypeField(shPostTypeAttrChild);
				shPostTypeFieldExchanges.put(shPostTypeAttrChild.getName(), shPostTypeFieldExchangeChild);
			}
			shPostTypeFieldExchange.setFields(shPostTypeFieldExchanges);
		}
		return shPostTypeFieldExchange;
	}
}
