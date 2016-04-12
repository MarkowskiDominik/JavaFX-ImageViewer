package javafx.imageviewer.dataprovider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.imageviewer.data.ImageVO;

public class DataProvider {
	private static final Logger LOG = Logger.getLogger(DataProvider.class);
	private List<ImageVO> listOfImages = new ArrayList<ImageVO>();
	private List<String> imageTypes = new ArrayList<String>(Arrays.asList(new String[] { ".jpg", ".png" }));

	public DataProvider() {
		LOG.debug("DataProvider()");
	}

	public Collection<ImageVO> getImagesFromDirectory(String path) {
		File folder = new File(path);
		File[] allFies = folder.listFiles();
		listOfImages.clear();
		
		if (allFies != null && allFies.length != 0) {
			getImagesFiles(allFies);
		}
		return listOfImages;
	}

	public void getImagesFiles(File[] allFies) {
		Long id = 1L;
		for (File file : allFies) {
			if (file.isFile() && isImage(file.getName())) {
				listOfImages.add(new ImageVO(file.getName(), id++, file.getPath()));
			}
		}
	}

	private boolean isImage(String fileName) {
		for (String type : imageTypes) {
			if (fileName.endsWith(type)) {
				return true;
			}
		}
		return false;
	}

	public List<ImageVO> getImages() {
		return listOfImages;
	}
}