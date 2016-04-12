package javafx.imageviewer.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.imageviewer.data.ImageVO;

public class ImageViewerModel {

//	private final SimpleObjectProperty<ImageVO> selectedImage = new SimpleObjectProperty<ImageVO>();
	private final ListProperty<ImageVO> images = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));

	public final List<ImageVO> getImages() {
		return images.get();
	}

	public final void setImages(List<ImageVO> value) {
		images.setAll(value);
	}

	public ListProperty<ImageVO> imagesProperty() {
		return images;
	}

//	public final ImageVO getSelectedImage() {
//		return selectedImage.get();
//	}
//	
//	public final void setSelectedImage(ImageVO value) {
//		selectedImage.set(value);
//	}
//	
//	public SimpleObjectProperty<ImageVO> selectedImageProperty() {
//		return selectedImage;
//	}
}
