package com.delta.smsandroidproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ChargerInfoData implements Parcelable {
	private String Id;
	private String Name;
	private String Status;
	private String EvseTotal;
	private String EvseUsing;
	private String Image;

	public ChargerInfoData(String Id, String Name, String Status,
			String EvseTotal, String EvseUsing, String Image) {
		this.Id = Id;
		this.Name = Name;
		this.Status = Status;
		this.EvseTotal = EvseTotal;
		this.EvseUsing = EvseUsing;
		this.Image = Image;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		this.Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getEvseTotal() {
		return EvseTotal;
	}

	public void setEvseTotal(String evseTotal) {
		EvseTotal = evseTotal;
	}

	public String getEvseUsing() {
		return EvseUsing;
	}

	public void setEvseUsing(String evseUsing) {
		EvseUsing = evseUsing;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	@Override
	public String toString() {
		return "ChargerInfoData [Id=" + Id + ", Name=" + Name + ", Status="
				+ Status + ", EvseTotal=" + EvseTotal + ", EvseUsing="
				+ EvseUsing + ", Image=" + Image + "]";
	}

	@Override
	public int describeContents() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO 自动生成的方法存根
		dest.writeString(Id);
		dest.writeString(Name);
		dest.writeString(Status);
		dest.writeString(EvseTotal);
		dest.writeString(EvseUsing);
		dest.writeString(Image);
	}

	public static final Parcelable.Creator<ChargerInfoData> CREATOR = new Creator<ChargerInfoData>() {

		@Override
		public ChargerInfoData createFromParcel(Parcel source) {
			return new ChargerInfoData(source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString());
		}

		@Override
		public ChargerInfoData[] newArray(int size) {
			return new ChargerInfoData[size];
		}
	};

}
