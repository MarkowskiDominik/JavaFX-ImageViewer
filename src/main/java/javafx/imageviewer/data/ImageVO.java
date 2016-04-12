package javafx.imageviewer.data;

public class ImageVO {
	private Long id;
	private String name;
	private String path = "file:///";

	public ImageVO() {
	}

	public ImageVO(String name, Long id, String path) {
		this.name = name;
		this.id = id;
		this.path += path;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}