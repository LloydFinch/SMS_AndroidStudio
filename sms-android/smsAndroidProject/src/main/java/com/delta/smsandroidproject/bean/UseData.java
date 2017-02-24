package com.delta.smsandroidproject.bean;

import java.util.List;

/**
 * 用户角色
 * 
 * @author Jianzao.Zhang
 * 
 */
public class UseData {
	public static final String IS_SERVICE = "Service";
	public static final String IS_SUPPORT = "Support";
	private List<Function> Function;

	public List<Function> getFunction() {
		return Function;
	}

	@Override
	public String toString() {
		return "UseData [Function=" + Function + "]";
	}

	public class Function {
		private String GroupID;
		private String FunctionID;
		private String FunctionName;
		private String ParentID;
		private List<SubFuction> SubFuction;

		public String getGroupID() {
			return GroupID;
		}

		public String getFunctionID() {
			return FunctionID;
		}

		public String getFunctionName() {
			return FunctionName;
		}

		public String getParentID() {
			return ParentID;
		}

		public List<SubFuction> getSubFuction() {
			return SubFuction;
		}

		@Override
		public String toString() {
			return "Function [GroupID=" + GroupID + ", FunctionID="
					+ FunctionID + ", FunctionName=" + FunctionName
					+ ", ParentID=" + ParentID + ", SubFuction=" + SubFuction
					+ "]";
		}

		public class SubFuction {
			private String GroupID;
			private String FunctionID;
			private String FunctionName;
			private String ParentID;
			private List<SubFuction2> SubFuction2;
			private List<SubPageFuction> subPageFuction;

			public String getGroupID() {
				return GroupID;
			}

			public String getFunctionID() {
				return FunctionID;
			}

			public String getFunctionName() {
				return FunctionName;
			}

			public String getParentID() {
				return ParentID;
			}

			public List<SubFuction2> getSubFuction2() {
				return SubFuction2;
			}

			public List<SubPageFuction> getSubPageFuction() {
				return subPageFuction;
			}

			@Override
			public String toString() {
				return "SubFuction [GroupID=" + GroupID + ", FunctionID="
						+ FunctionID + ", FunctionName=" + FunctionName
						+ ", ParentID=" + ParentID + ", SubFuction2="
						+ SubFuction2 + ", subPageFuction=" + subPageFuction
						+ "]";
			}

			public class SubFuction2 {
				private String GroupID;
				private String FunctionID;
				private String FunctionName;
				private String ParentID;

				public String getGroupID() {
					return GroupID;
				}

				public String getFunctionID() {
					return FunctionID;
				}

				public String getFunctionName() {
					return FunctionName;
				}

				public String getParentID() {
					return ParentID;
				}

				@Override
				public String toString() {
					return "SubFuction2 [GroupID=" + GroupID + ", FunctionID="
							+ FunctionID + ", FunctionName=" + FunctionName
							+ ", ParentID=" + ParentID + "]";
				}

			}

			public class SubPageFuction {
				private String GroupID;
				private String FunctionID;
				private String FunctionName;
				private String ParentID;

				public String getGroupID() {
					return GroupID;
				}

				public String getFunctionID() {
					return FunctionID;
				}

				public String getFunctionName() {
					return FunctionName;
				}

				public String getParentID() {
					return ParentID;
				}

				@Override
				public String toString() {
					return "SubPageFuction [GroupID=" + GroupID
							+ ", FunctionID=" + FunctionID + ", FunctionName="
							+ FunctionName + ", ParentID=" + ParentID + "]";
				}

			}
		}
	}

}
