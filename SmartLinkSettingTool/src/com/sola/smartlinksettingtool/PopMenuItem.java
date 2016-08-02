package com.sola.smartlinksettingtool;

public class PopMenuItem {

	private int itemId;
	private boolean enable = true;
	private int iconId;
	private String content;
	private boolean noticeable = false;

	public int getItemId() {
		return itemId;
	}

	public boolean isEnable() {
		return enable;
	}

	public String getContent() {
		return content;
	}

	public boolean isNoticeable() {
		return noticeable;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setNoticeable(boolean noticeable) {
		this.noticeable = noticeable;
	}

}
