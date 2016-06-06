package firstgroup.com.smallsun.adapter;

import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public abstract class MyAdapter<T> extends BaseAdapter {
	private List<T> list;
	private int layout;
	private LayoutInflater inflater;
	private Activity activity;

	public MyAdapter(List<T> list, int layout, Activity activity) {
		super();
		this.list = list;
		this.layout = layout;
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity);
	}


	public Activity getActivity() {
		return activity;
	}

	public View getItemView() {
		return inflater.inflate(layout, null);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

}
