package com.delta.smsandroidproject.util;

import java.util.Comparator;

import com.delta.smsandroidproject.bean.BestRouteModel;

public class RoutePlanerComparator implements Comparator<BestRouteModel> {

	public RoutePlanerComparator() {
	}

	@Override
	public int compare(BestRouteModel lhs, BestRouteModel rhs) {

		if (lhs != null && rhs != null) {
			if (lhs.getDistance() > rhs.getDistance()) {
				return 1;
			} else if (lhs.getDistance() == rhs.getDistance()) {
				if (lhs.getFare() > rhs.getFare()) {
					return 1;
				}
			}
		}
		return -1;
	}
}
