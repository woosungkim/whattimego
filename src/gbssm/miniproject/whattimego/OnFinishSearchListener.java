package gbssm.miniproject.whattimego;


import java.util.List;

public interface OnFinishSearchListener {
	public void onSuccess(List<MapItem> itemList);
	public void onFail();
}