package com.delta.smsandroidproject.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.delta.smsandroidproject.model.IbackInterface;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public abstract class BaseFragment extends Fragment {

	private IbackInterface ibackInterface;

	public abstract boolean onBackPress();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!(getActivity() instanceof IbackInterface)) {
			throw new ClassCastException(
					"Hosting Activity must implement BackHandledInterface");
		} else {
			this.ibackInterface = (IbackInterface) getActivity();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// ����FragmentActivity����ǰFragment��ջ��
		ibackInterface.setSelectedFragment(this);
	}

}
