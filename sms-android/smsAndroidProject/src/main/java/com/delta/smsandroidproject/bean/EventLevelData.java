package com.delta.smsandroidproject.bean;

import java.util.List;

public class EventLevelData {
	/*LOCK(-1),

    CHARGE(1),

    CONFIGURATION(2),

    INFORMATION(3),

    WARNING(4),

    FAULT(5),

    EMERGENCY(6);
*/

	private List<Level> Level;
	private class Level{
		private String LevelName;
		private String Level_Id;
	}
	
}
