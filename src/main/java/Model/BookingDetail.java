/*
 * Tran Le Minh Man - 23130186
 * Chi tiet moi tour trong gio
 */
package Model;

public class BookingDetail {
	private int detailId;
	private int bookingId;
	private int tourId;
	private String tourName;
	private double tourPrice;
	private int numPeople;
	private double subTotal; // tourPrice * numPeople

	public BookingDetail() {
	}

	public BookingDetail(int tourId, String tourName, double tourPrice, int numPeople) {
		this.tourId = tourId;
		this.tourName = tourName;
		this.tourPrice = tourPrice;
		this.numPeople = numPeople;
		this.subTotal = tourPrice * numPeople;
	}

	// Getter & Setter
	public int getDetailId() {
		return detailId;
	}

	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getTourId() {
		return tourId;
	}

	public void setTourId(int tourId) {
		this.tourId = tourId;
	}

	public String getTourName() {
		return tourName;
	}

	public void setTourName(String tourName) {
		this.tourName = tourName;
	}

	public double getTourPrice() {
		return tourPrice;
	}

	public void setTourPrice(double tourPrice) {
		this.tourPrice = tourPrice;
	}

	public int getNumPeople() {
		return numPeople;
	}

	public void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
}